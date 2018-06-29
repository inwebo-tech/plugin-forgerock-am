package com.inwebo.integrations.forgerock.am.push;

import com.google.inject.Inject;
import com.inwebo.integrations.util.BuildProperties;
import org.forgerock.openam.plugins.AmPlugin;
import org.forgerock.openam.plugins.PluginException;
import org.forgerock.openam.plugins.PluginTools;

public class InWeboPushAuthPlugin implements AmPlugin {

  private PluginTools pluginTools;

  @Inject
  public InWeboPushAuthPlugin(PluginTools pluginTools) {
    this.pluginTools = pluginTools;
  }

  @Override
  public String getPluginVersion() {
    return BuildProperties.getVersion();
  }

  @Override
  public void onInstall() throws PluginException {
    pluginTools.addAuthModule(InWeboPushAuth.class, getClass().getClassLoader().getResourceAsStream("amAuthInWeboPushAuth.xml"));
  }
}