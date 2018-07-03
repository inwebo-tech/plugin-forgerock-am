Installation inWebo Otp Authenticator Plugin
============================================
 
inWebo service configuration:
-----------------------------
 
 1. Log in to the inWebo administration console.
 1. Navigate to the appropriate service view.
 1. Go to `Secure Sites` and click on the `Download a new certificate for the API` button.
 1. Remember to set the Certificate `Authentication` option to `Yes`.
 ![myInWeboConfig](../doc/images/myInWeboConfig.png)
 1. Synchronize your forgerock-am login with inWebo login.
 
Installation:
-------------

1. Copy resource:
    ```bash
    $ sudo unzip iw-forgerock-am-otp-*.zip -d /tmp/forgerock-am-opt
    $ sudo cp /tmp/forgerock-am-opt/edit-webapp/WEB-INF/lib/iw-forgerock-am-otp-*.jar /path/to/tomcat/webapps/openam/WEB-INF/lib/
    $ sudo cp /tmp/forgerock-am-opt/edit-webapp/WEB-INF/lib/idp-connector-auth-repackage-0.3.0.jar /path/to/tomcat/webapps/openam/WEB-INF/lib/   
    ```
1. restart tomcat.

Configuration:
--------------

1. Go to Admin Console and log in as `amadmin`.
2. Navigate to {REALM}->Authentification->Modules.
3. Add new module with `inWebo OTP Authenticator` Type.
![add new module](../doc/images/inWeboOtpAuthCreateModule.png)
![config new module](../doc/images/inWeboOtpAuthEditConfig.png)
4. To test config go to `http(s)://{OPENAM_HOST}/openam/XUI/#login/&module=inWeboOtpTest`
![XUI Login Page](../doc/images/inWeboOtpAuthLoginPage.png)