package com.inwebo.integrations.forgerock.am.push;

import com.inwebo.integrations.auth.InWeboRestAuthenticator;
import com.sun.identity.authentication.spi.AMLoginModule;
import com.sun.identity.authentication.spi.AuthLoginException;
import com.sun.identity.authentication.spi.InvalidPasswordException;
import com.sun.identity.shared.debug.Debug;
import org.apache.commons.lang.StringUtils;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.login.LoginException;
import java.security.Principal;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import static com.inwebo.integrations.auth.Property.*;
import static com.sun.identity.authentication.util.ISAuthConstants.LOGIN_SUCCEED;
import static com.sun.identity.shared.datastruct.CollectionHelper.getIntMapAttr;
import static com.sun.identity.shared.datastruct.CollectionHelper.getMapAttr;
import static java.util.concurrent.TimeUnit.MINUTES;

public class InWeboPushAuth extends AMLoginModule {

  // Name for the DEBUG-log
  private final static String DEBUG_NAME = "InWeboPushAuth";
  private final static Debug DEBUG = Debug.getInstance(DEBUG_NAME);

  private final static Logger LOGGER = Logger.getLogger(DEBUG_NAME);

  // Name of the resource bundle
  private final static String amAuthInWeboPushAuth = "amAuthInWeboPushAuth";

  // Orders defined in the callbacks file
  private final static int STATE_BEGIN = 1;
  private final static int STATE_AUTH = 2;
  private final static int STATE_ERROR = 3;

  private ResourceBundle bundle;
  private Map<String, String> sharedState;
  private InWeboRestAuthenticator inWeboRestAuthenticator;
  private String sharedUserName;

  public InWeboPushAuth() {
    super();
  }

  @Override
  @SuppressWarnings("unchecked")
  public void init(final Subject subject, final Map sharedState, final Map options) {
    if (DEBUG.messageEnabled()) {
      DEBUG.message("InWeboPushAuthModule::init");
    }
    try {
      this.sharedState = sharedState;
      this.bundle = amCache.getResBundle(amAuthInWeboPushAuth, getLoginLocale());
      final Properties property = new Properties();
      property.setProperty(BASE_URL.key(), getMapAttr(options, "iplanet-am-auth-inwebo-push-base-url"));
      property.setProperty(SERVICE_ID.key(), String.valueOf(getIntMapAttr(options, "iplanet-am-auth-inwebo-push-service-id", 0, DEBUG)));
      property.setProperty(CERTIFICATE_PATH.key(), getMapAttr(options, "iplanet-am-auth-inwebo-push-certificate-path"));
      property.setProperty(CERTIFICATE_PASSWORD.key(), getMapAttr(options, "iplanet-am-auth-inwebo-push-certificate-password"));
      final String proxyUri = getMapAttr(options, "iplanet-am-auth-inwebo-push-proxy-url");
      final String proxyUserName = getMapAttr(options, "iplanet-am-auth-inwebo-push-proxy-username");
      final String proxyPassword = getMapAttr(options, "iplanet-am-auth-inwebo-push-proxy-password");
      if (StringUtils.isNotBlank(proxyUri)) {
        property.setProperty(PROXY_URI.key(), proxyUri);
        if (StringUtils.isNotBlank(proxyUserName) && StringUtils.isNotBlank(proxyPassword)) {
          property.setProperty(PROXY_USER.key(), proxyUserName);
          property.setProperty(PROXY_PASSWORD.key(), proxyPassword);
        }
      }
      this.inWeboRestAuthenticator = new InWeboRestAuthenticator(property, LOGGER);
    } catch (final Exception e) {
      DEBUG.error("InWeboPushAuthModule::init - Internal Error", e);
    }
  }

  @Override
  public int process(final Callback[] callbacks, final int state) throws LoginException {
    if (DEBUG.messageEnabled()) {
      DEBUG.message("InWeboPushAuthModule::process - state: '{}'", state);
    }
    switch (state) {
      case STATE_BEGIN:
        substituteUIStrings();
        return STATE_AUTH;
      case STATE_AUTH:
        return authenticatePushValidator(callbacks);
      case STATE_ERROR:
        return STATE_ERROR;
      default:
        throw new AuthLoginException("invalid state");
    }
  }

  private int authenticatePushValidator(final Callback[] callbacks) throws AuthLoginException {
    // Get data from callbacks. Refer to callbacks XML file.
    this.sharedUserName = getUserName(NameCallback.class.cast(callbacks[0]));
    final boolean result = inWeboRestAuthenticator.authorizeByPush(sharedUserName, 2L, MINUTES);
    if (result) {
      return LOGIN_SUCCEED;
    } else {
      throw new InvalidPasswordException("Access Denied", sharedUserName);
    }
  }

  private String getUserName(final NameCallback nc) {
    String userName = getSharedUserName();
    if (userName == null || userName.isEmpty()) {
      userName = nc.getName();
    }
    return userName;
  }

  private String getSharedUserName() {
    return String.class.cast(sharedState.get(getUserKey()));
  }

  private void substituteUIStrings() throws AuthLoginException {
    substituteHeader(STATE_AUTH, bundle.getString("inwebo.i18n.ui.login.header"));
    final String userName = getSharedUserName();
    if (DEBUG.messageEnabled()) {
      DEBUG.message("InWeboPushAuthModule::begin - sharedUserName '{}'", userName);
    }
    final NameCallback userNameCallBack = new NameCallback(bundle.getString("inwebo.i18n.ui.login.username.prompt"));
    if (StringUtils.isNotBlank(userName)) {
      userNameCallBack.setName(userName);
    }
    replaceCallback(STATE_AUTH, 0, userNameCallBack);
  }

  @Override
  public Principal getPrincipal() {
    return new InWeboPushAuthPrincipal(this.sharedUserName);
  }

  @Override
  public void destroyModuleState() {
    this.sharedUserName = null;
  }
}