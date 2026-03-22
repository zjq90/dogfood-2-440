@echo off
setlocal enabledelayedexpansion

set "WRAPPER_JAR=%~dp0.mvn\wrapper\maven-wrapper.jar"
set "WRAPPER_PROPERTIES=%~dp0.mvn\wrapper\maven-wrapper.properties"

if not exist "%WRAPPER_JAR%" (
    echo Could not find maven wrapper jar at %WRAPPER_JAR%
    exit /b 1
)

set JAVA_EXE=java.exe
if defined JAVA_HOME (
    set "JAVA_EXE=%JAVA_HOME%\bin\java.exe"
)

"%JAVA_EXE%" -jar "%WRAPPER_JAR%" %*
