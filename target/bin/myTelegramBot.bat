@REM ----------------------------------------------------------------------------
@REM Copyright 2001-2004 The Apache Software Foundation.
@REM
@REM Licensed under the Apache License, Version 2.0 (the "License");
@REM you may not use this file except in compliance with the License.
@REM You may obtain a copy of the License at
@REM
@REM      http://www.apache.org/licenses/LICENSE-2.0
@REM
@REM Unless required by applicable law or agreed to in writing, software
@REM distributed under the License is distributed on an "AS IS" BASIS,
@REM WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
@REM See the License for the specific language governing permissions and
@REM limitations under the License.
@REM ----------------------------------------------------------------------------
@REM

@echo off

set ERROR_CODE=0

:init
@REM Decide how to startup depending on the version of windows

@REM -- Win98ME
if NOT "%OS%"=="Windows_NT" goto Win9xArg

@REM set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" @setlocal

@REM -- 4NT shell
if "%eval[2+2]" == "4" goto 4NTArgs

@REM -- Regular WinNT shell
set CMD_LINE_ARGS=%*
goto WinNTGetScriptDir

@REM The 4NT Shell from jp software
:4NTArgs
set CMD_LINE_ARGS=%$
goto WinNTGetScriptDir

:Win9xArg
@REM Slurp the command line arguments.  This loop allows for an unlimited number
@REM of arguments (up to the command line limit, anyway).
set CMD_LINE_ARGS=
:Win9xApp
if %1a==a goto Win9xGetScriptDir
set CMD_LINE_ARGS=%CMD_LINE_ARGS% %1
shift
goto Win9xApp

:Win9xGetScriptDir
set SAVEDIR=%CD%
%0\
cd %0\..\.. 
set BASEDIR=%CD%
cd %SAVEDIR%
set SAVE_DIR=
goto repoSetup

:WinNTGetScriptDir
set BASEDIR=%~dp0\..

:repoSetup


if "%JAVACMD%"=="" set JAVACMD=java

if "%REPO%"=="" set REPO=%BASEDIR%\repo

set CLASSPATH="%BASEDIR%"\etc;"%REPO%"\com\google\firebase\firebase-admin\8.1.0\firebase-admin-8.1.0.jar;"%REPO%"\com\google\api-client\google-api-client\1.32.1\google-api-client-1.32.1.jar;"%REPO%"\com\google\oauth-client\google-oauth-client\1.31.5\google-oauth-client-1.31.5.jar;"%REPO%"\com\google\http-client\google-http-client-gson\1.39.2\google-http-client-gson-1.39.2.jar;"%REPO%"\com\google\http-client\google-http-client-apache-v2\1.39.2\google-http-client-apache-v2-1.39.2.jar;"%REPO%"\org\apache\httpcomponents\httpcore\4.4.14\httpcore-4.4.14.jar;"%REPO%"\com\google\api-client\google-api-client-gson\1.32.1\google-api-client-gson-1.32.1.jar;"%REPO%"\com\google\http-client\google-http-client\1.39.2\google-http-client-1.39.2.jar;"%REPO%"\com\google\j2objc\j2objc-annotations\1.3\j2objc-annotations-1.3.jar;"%REPO%"\io\opencensus\opencensus-api\0.28.0\opencensus-api-0.28.0.jar;"%REPO%"\io\opencensus\opencensus-contrib-http-util\0.28.0\opencensus-contrib-http-util-0.28.0.jar;"%REPO%"\com\google\api\api-common\1.10.4\api-common-1.10.4.jar;"%REPO%"\javax\annotation\javax.annotation-api\1.3.2\javax.annotation-api-1.3.2.jar;"%REPO%"\com\google\auto\value\auto-value-annotations\1.8.1\auto-value-annotations-1.8.1.jar;"%REPO%"\com\google\auth\google-auth-library-oauth2-http\0.26.0\google-auth-library-oauth2-http-0.26.0.jar;"%REPO%"\com\google\auth\google-auth-library-credentials\0.26.0\google-auth-library-credentials-0.26.0.jar;"%REPO%"\com\google\cloud\google-cloud-storage\1.118.0\google-cloud-storage-1.118.0.jar;"%REPO%"\com\google\guava\failureaccess\1.0.1\failureaccess-1.0.1.jar;"%REPO%"\com\google\guava\listenablefuture\9999.0-empty-to-avoid-conflict-with-guava\listenablefuture-9999.0-empty-to-avoid-conflict-with-guava.jar;"%REPO%"\org\checkerframework\checker-compat-qual\2.5.5\checker-compat-qual-2.5.5.jar;"%REPO%"\com\google\errorprone\error_prone_annotations\2.7.1\error_prone_annotations-2.7.1.jar;"%REPO%"\com\google\http-client\google-http-client-jackson2\1.39.2\google-http-client-jackson2-1.39.2.jar;"%REPO%"\com\fasterxml\jackson\core\jackson-core\2.12.3\jackson-core-2.12.3.jar;"%REPO%"\com\google\apis\google-api-services-storage\v1-rev20210127-1.32.1\google-api-services-storage-v1-rev20210127-1.32.1.jar;"%REPO%"\com\google\code\gson\gson\2.8.7\gson-2.8.7.jar;"%REPO%"\com\google\cloud\google-cloud-core\1.95.4\google-cloud-core-1.95.4.jar;"%REPO%"\com\google\api\grpc\proto-google-common-protos\2.3.2\proto-google-common-protos-2.3.2.jar;"%REPO%"\com\google\cloud\google-cloud-core-http\1.95.4\google-cloud-core-http-1.95.4.jar;"%REPO%"\com\google\http-client\google-http-client-appengine\1.39.2\google-http-client-appengine-1.39.2.jar;"%REPO%"\com\google\api\gax-httpjson\0.83.0\gax-httpjson-0.83.0.jar;"%REPO%"\com\google\api\gax\1.66.0\gax-1.66.0.jar;"%REPO%"\io\grpc\grpc-context\1.39.0\grpc-context-1.39.0.jar;"%REPO%"\com\google\api\grpc\proto-google-iam-v1\1.0.14\proto-google-iam-v1-1.0.14.jar;"%REPO%"\com\google\protobuf\protobuf-java\3.17.3\protobuf-java-3.17.3.jar;"%REPO%"\com\google\protobuf\protobuf-java-util\3.17.3\protobuf-java-util-3.17.3.jar;"%REPO%"\org\threeten\threetenbp\1.5.1\threetenbp-1.5.1.jar;"%REPO%"\com\google\cloud\google-cloud-firestore\2.6.1\google-cloud-firestore-2.6.1.jar;"%REPO%"\com\google\cloud\google-cloud-core-grpc\1.95.4\google-cloud-core-grpc-1.95.4.jar;"%REPO%"\io\grpc\grpc-core\1.39.0\grpc-core-1.39.0.jar;"%REPO%"\com\google\android\annotations\4.1.1.4\annotations-4.1.1.4.jar;"%REPO%"\org\codehaus\mojo\animal-sniffer-annotations\1.20\animal-sniffer-annotations-1.20.jar;"%REPO%"\io\perfmark\perfmark-api\0.23.0\perfmark-api-0.23.0.jar;"%REPO%"\commons-logging\commons-logging\1.2\commons-logging-1.2.jar;"%REPO%"\commons-codec\commons-codec\1.15\commons-codec-1.15.jar;"%REPO%"\com\google\api\grpc\proto-google-cloud-firestore-v1\2.6.1\proto-google-cloud-firestore-v1-2.6.1.jar;"%REPO%"\com\google\cloud\proto-google-cloud-firestore-bundle-v1\2.6.1\proto-google-cloud-firestore-bundle-v1-2.6.1.jar;"%REPO%"\io\opencensus\opencensus-contrib-grpc-util\0.28.0\opencensus-contrib-grpc-util-0.28.0.jar;"%REPO%"\io\grpc\grpc-protobuf\1.39.0\grpc-protobuf-1.39.0.jar;"%REPO%"\io\grpc\grpc-protobuf-lite\1.39.0\grpc-protobuf-lite-1.39.0.jar;"%REPO%"\io\grpc\grpc-api\1.39.0\grpc-api-1.39.0.jar;"%REPO%"\com\google\api\gax-grpc\1.66.0\gax-grpc-1.66.0.jar;"%REPO%"\io\grpc\grpc-auth\1.39.0\grpc-auth-1.39.0.jar;"%REPO%"\io\grpc\grpc-netty-shaded\1.39.0\grpc-netty-shaded-1.39.0.jar;"%REPO%"\io\grpc\grpc-alts\1.39.0\grpc-alts-1.39.0.jar;"%REPO%"\io\grpc\grpc-grpclb\1.39.0\grpc-grpclb-1.39.0.jar;"%REPO%"\org\conscrypt\conscrypt-openjdk-uber\2.5.1\conscrypt-openjdk-uber-2.5.1.jar;"%REPO%"\io\grpc\grpc-stub\1.39.0\grpc-stub-1.39.0.jar;"%REPO%"\com\google\guava\guava\30.1.1-android\guava-30.1.1-android.jar;"%REPO%"\org\slf4j\slf4j-api\1.7.32\slf4j-api-1.7.32.jar;"%REPO%"\io\netty\netty-codec-http\4.1.67.Final\netty-codec-http-4.1.67.Final.jar;"%REPO%"\io\netty\netty-common\4.1.67.Final\netty-common-4.1.67.Final.jar;"%REPO%"\io\netty\netty-buffer\4.1.67.Final\netty-buffer-4.1.67.Final.jar;"%REPO%"\io\netty\netty-codec\4.1.67.Final\netty-codec-4.1.67.Final.jar;"%REPO%"\io\netty\netty-handler\4.1.67.Final\netty-handler-4.1.67.Final.jar;"%REPO%"\io\netty\netty-resolver\4.1.67.Final\netty-resolver-4.1.67.Final.jar;"%REPO%"\io\netty\netty-transport\4.1.67.Final\netty-transport-4.1.67.Final.jar;"%REPO%"\org\apache\maven\plugins\maven-compiler-plugin\3.8.1\maven-compiler-plugin-3.8.1.jar;"%REPO%"\org\apache\maven\maven-plugin-api\3.0\maven-plugin-api-3.0.jar;"%REPO%"\org\apache\maven\maven-model\3.0\maven-model-3.0.jar;"%REPO%"\org\sonatype\sisu\sisu-inject-plexus\1.4.2\sisu-inject-plexus-1.4.2.jar;"%REPO%"\org\sonatype\sisu\sisu-inject-bean\1.4.2\sisu-inject-bean-1.4.2.jar;"%REPO%"\org\sonatype\sisu\sisu-guice\2.1.7\sisu-guice-2.1.7-noaop.jar;"%REPO%"\org\apache\maven\maven-artifact\3.0\maven-artifact-3.0.jar;"%REPO%"\org\codehaus\plexus\plexus-utils\2.0.4\plexus-utils-2.0.4.jar;"%REPO%"\org\apache\maven\maven-core\3.0\maven-core-3.0.jar;"%REPO%"\org\apache\maven\maven-settings\3.0\maven-settings-3.0.jar;"%REPO%"\org\apache\maven\maven-settings-builder\3.0\maven-settings-builder-3.0.jar;"%REPO%"\org\apache\maven\maven-repository-metadata\3.0\maven-repository-metadata-3.0.jar;"%REPO%"\org\apache\maven\maven-model-builder\3.0\maven-model-builder-3.0.jar;"%REPO%"\org\apache\maven\maven-aether-provider\3.0\maven-aether-provider-3.0.jar;"%REPO%"\org\sonatype\aether\aether-impl\1.7\aether-impl-1.7.jar;"%REPO%"\org\sonatype\aether\aether-spi\1.7\aether-spi-1.7.jar;"%REPO%"\org\sonatype\aether\aether-api\1.7\aether-api-1.7.jar;"%REPO%"\org\sonatype\aether\aether-util\1.7\aether-util-1.7.jar;"%REPO%"\org\codehaus\plexus\plexus-interpolation\1.14\plexus-interpolation-1.14.jar;"%REPO%"\org\codehaus\plexus\plexus-classworlds\2.2.3\plexus-classworlds-2.2.3.jar;"%REPO%"\org\codehaus\plexus\plexus-component-annotations\1.5.5\plexus-component-annotations-1.5.5.jar;"%REPO%"\org\sonatype\plexus\plexus-sec-dispatcher\1.3\plexus-sec-dispatcher-1.3.jar;"%REPO%"\org\sonatype\plexus\plexus-cipher\1.4\plexus-cipher-1.4.jar;"%REPO%"\org\apache\maven\shared\maven-shared-utils\3.2.1\maven-shared-utils-3.2.1.jar;"%REPO%"\org\apache\maven\shared\maven-shared-incremental\1.1\maven-shared-incremental-1.1.jar;"%REPO%"\org\codehaus\plexus\plexus-java\0.9.10\plexus-java-0.9.10.jar;"%REPO%"\org\ow2\asm\asm\6.2\asm-6.2.jar;"%REPO%"\com\thoughtworks\qdox\qdox\2.0-M8\qdox-2.0-M8.jar;"%REPO%"\org\codehaus\plexus\plexus-compiler-api\2.8.4\plexus-compiler-api-2.8.4.jar;"%REPO%"\org\codehaus\plexus\plexus-compiler-manager\2.8.4\plexus-compiler-manager-2.8.4.jar;"%REPO%"\org\codehaus\plexus\plexus-compiler-javac\2.8.4\plexus-compiler-javac-2.8.4.jar;"%REPO%"\org\slf4j\slf4j-simple\1.7.21\slf4j-simple-1.7.21.jar;"%REPO%"\org\telegram\telegrambots\5.4.0.1\telegrambots-5.4.0.1.jar;"%REPO%"\org\telegram\telegrambots-meta\5.4.0.1\telegrambots-meta-5.4.0.1.jar;"%REPO%"\com\fasterxml\jackson\core\jackson-annotations\2.11.3\jackson-annotations-2.11.3.jar;"%REPO%"\com\fasterxml\jackson\jaxrs\jackson-jaxrs-json-provider\2.11.3\jackson-jaxrs-json-provider-2.11.3.jar;"%REPO%"\com\fasterxml\jackson\jaxrs\jackson-jaxrs-base\2.11.3\jackson-jaxrs-base-2.11.3.jar;"%REPO%"\com\fasterxml\jackson\module\jackson-module-jaxb-annotations\2.11.3\jackson-module-jaxb-annotations-2.11.3.jar;"%REPO%"\jakarta\xml\bind\jakarta.xml.bind-api\2.3.2\jakarta.xml.bind-api-2.3.2.jar;"%REPO%"\jakarta\activation\jakarta.activation-api\1.2.1\jakarta.activation-api-1.2.1.jar;"%REPO%"\com\fasterxml\jackson\core\jackson-databind\2.11.3\jackson-databind-2.11.3.jar;"%REPO%"\org\glassfish\jersey\inject\jersey-hk2\2.32\jersey-hk2-2.32.jar;"%REPO%"\org\glassfish\jersey\core\jersey-common\2.32\jersey-common-2.32.jar;"%REPO%"\org\glassfish\hk2\osgi-resource-locator\1.0.3\osgi-resource-locator-1.0.3.jar;"%REPO%"\com\sun\activation\jakarta.activation\1.2.2\jakarta.activation-1.2.2.jar;"%REPO%"\org\glassfish\hk2\hk2-locator\2.6.1\hk2-locator-2.6.1.jar;"%REPO%"\org\glassfish\hk2\external\aopalliance-repackaged\2.6.1\aopalliance-repackaged-2.6.1.jar;"%REPO%"\org\glassfish\hk2\hk2-api\2.6.1\hk2-api-2.6.1.jar;"%REPO%"\org\glassfish\hk2\hk2-utils\2.6.1\hk2-utils-2.6.1.jar;"%REPO%"\org\javassist\javassist\3.25.0-GA\javassist-3.25.0-GA.jar;"%REPO%"\org\glassfish\jersey\media\jersey-media-json-jackson\2.32\jersey-media-json-jackson-2.32.jar;"%REPO%"\org\glassfish\jersey\ext\jersey-entity-filtering\2.32\jersey-entity-filtering-2.32.jar;"%REPO%"\org\glassfish\jersey\containers\jersey-container-grizzly2-http\2.32\jersey-container-grizzly2-http-2.32.jar;"%REPO%"\org\glassfish\hk2\external\jakarta.inject\2.6.1\jakarta.inject-2.6.1.jar;"%REPO%"\org\glassfish\grizzly\grizzly-http-server\2.4.4\grizzly-http-server-2.4.4.jar;"%REPO%"\org\glassfish\grizzly\grizzly-http\2.4.4\grizzly-http-2.4.4.jar;"%REPO%"\org\glassfish\grizzly\grizzly-framework\2.4.4\grizzly-framework-2.4.4.jar;"%REPO%"\jakarta\ws\rs\jakarta.ws.rs-api\2.1.6\jakarta.ws.rs-api-2.1.6.jar;"%REPO%"\org\glassfish\jersey\core\jersey-server\2.32\jersey-server-2.32.jar;"%REPO%"\org\glassfish\jersey\core\jersey-client\2.32\jersey-client-2.32.jar;"%REPO%"\org\glassfish\jersey\media\jersey-media-jaxb\2.32\jersey-media-jaxb-2.32.jar;"%REPO%"\jakarta\annotation\jakarta.annotation-api\1.3.5\jakarta.annotation-api-1.3.5.jar;"%REPO%"\jakarta\validation\jakarta.validation-api\2.0.2\jakarta.validation-api-2.0.2.jar;"%REPO%"\org\apache\httpcomponents\httpclient\4.5.13\httpclient-4.5.13.jar;"%REPO%"\org\apache\httpcomponents\httpmime\4.5.13\httpmime-4.5.13.jar;"%REPO%"\commons-io\commons-io\2.8.0\commons-io-2.8.0.jar;"%REPO%"\org\projectlombok\lombok\1.18.22\lombok-1.18.22.jar;"%REPO%"\org\testng\testng\7.5\testng-7.5.jar;"%REPO%"\com\google\code\findbugs\jsr305\3.0.1\jsr305-3.0.1.jar;"%REPO%"\com\beust\jcommander\1.78\jcommander-1.78.jar;"%REPO%"\org\webjars\jquery\3.5.1\jquery-3.5.1.jar;"%REPO%"\io\rest-assured\rest-assured\4.1.1\rest-assured-4.1.1.jar;"%REPO%"\org\codehaus\groovy\groovy\2.5.6\groovy-2.5.6.jar;"%REPO%"\org\codehaus\groovy\groovy-xml\2.5.6\groovy-xml-2.5.6.jar;"%REPO%"\org\hamcrest\hamcrest\2.1\hamcrest-2.1.jar;"%REPO%"\org\ccil\cowan\tagsoup\tagsoup\1.2.1\tagsoup-1.2.1.jar;"%REPO%"\io\rest-assured\json-path\4.1.1\json-path-4.1.1.jar;"%REPO%"\org\codehaus\groovy\groovy-json\2.5.6\groovy-json-2.5.6.jar;"%REPO%"\io\rest-assured\rest-assured-common\4.1.1\rest-assured-common-4.1.1.jar;"%REPO%"\io\rest-assured\xml-path\4.1.1\xml-path-4.1.1.jar;"%REPO%"\org\apache\commons\commons-lang3\3.4\commons-lang3-3.4.jar;"%REPO%"\javax\xml\bind\jaxb-api\2.3.1\jaxb-api-2.3.1.jar;"%REPO%"\javax\activation\javax.activation-api\1.2.0\javax.activation-api-1.2.0.jar;"%REPO%"\com\sun\xml\bind\jaxb-osgi\2.3.0.1\jaxb-osgi-2.3.0.1.jar;"%REPO%"\org\apache\sling\org.apache.sling.javax.activation\0.1.0\org.apache.sling.javax.activation-0.1.0.jar;"%REPO%"\javax\activation\activation\1.1.1\activation-1.1.1.jar;"%REPO%"\com\googlecode\json-simple\json-simple\1.1.1\json-simple-1.1.1.jar;"%REPO%"\junit\junit\4.10\junit-4.10.jar;"%REPO%"\org\hamcrest\hamcrest-core\1.1\hamcrest-core-1.1.jar;"%REPO%"\org\json\json\20180130\json-20180130.jar;"%REPO%"\org\aliganexample\myTelegramBot\1.0-SNAPSHOT\myTelegramBot-1.0-SNAPSHOT.jar
set EXTRA_JVM_ARGUMENTS=
goto endInit

@REM Reaching here means variables are defined and arguments have been captured
:endInit

%JAVACMD% %JAVA_OPTS% %EXTRA_JVM_ARGUMENTS% -classpath %CLASSPATH_PREFIX%;%CLASSPATH% -Dapp.name="myTelegramBot" -Dapp.repo="%REPO%" -Dbasedir="%BASEDIR%" classes.java.MyTelegramBot %CMD_LINE_ARGS%
if ERRORLEVEL 1 goto error
goto end

:error
if "%OS%"=="Windows_NT" @endlocal
set ERROR_CODE=1

:end
@REM set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" goto endNT

@REM For old DOS remove the set variables from ENV - we assume they were not set
@REM before we started - at least we don't leave any baggage around
set CMD_LINE_ARGS=
goto postExec

:endNT
@endlocal

:postExec

if "%FORCE_EXIT_ON_ERROR%" == "on" (
  if %ERROR_CODE% NEQ 0 exit %ERROR_CODE%
)

exit /B %ERROR_CODE%
