package com.inwebo.integrations.forgerock.am.otp;

import com.google.inject.Inject;
import com.inwebo.integrations.util.BuildProperties;
import org.forgerock.openam.plugins.AmPlugin;
import org.forgerock.openam.plugins.PluginException;
import org.forgerock.openam.plugins.PluginTools;

public class InWeboOtpAuthPlugin implements AmPlugin {

  private PluginTools pluginTools;

  @Inject
  public InWeboOtpAuthPlugin(PluginTools pluginTools) {
    this.pluginTools = pluginTools;
  }

  @Override
  public String getPluginVersion() {
    return BuildProperties.getVersion();
  }

  @Override
  public void onInstall() throws PluginException {
    pluginTools.addAuthModule(InWeboOtpAuth.class, getClass().getClassLoader().getResourceAsStream("amAuthInWeboOtpAuth.xml"));
  }
}