 inWebo ForgeRock AM Custom Authentication Module
 =======================================================
 
 Requirements
 ------------
 
 1. [ForgeRock AM 5.5.1 or 6.0.0](https://www.forgerock.com/platform/access-management)
 1. [Tomcat 8](http://apache.mediamirrors.org/tomcat/tomcat-8/v8.5.30/bin/apache-tomcat-8.5.30.tar.gz)
 1. [OpenJDK 1.8](http://openjdk.java.net/)
 
 Building from Source
 --------------------
 
 ```bash
 $ git clone https://github.com/inwebo-tech/plugin-forgerock-am.git
 $ cd plugin-forgerock-am 
 $ ./mvnw clean package
 ```
 
 Installation:
 -------------
 
 1. [Installation inWebo Otp Authenticator Plugin](iw-forgerock-am-otp/README.md)
 1. [Installation inWebo Push Authenticator Plugin](iw-forgerock-am-push/README.md)
 
 :warning: **If you want to install both plugins, it must be the same version.**