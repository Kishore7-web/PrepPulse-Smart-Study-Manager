@echo off
setlocal
cd /d "%~dp0"

echo ==================================================
echo  PrepPulse - Smart Study Manager
echo ==================================================
echo.
where java >nul 2>nul
if errorlevel 1 (
  echo ERROR: Java was not found. Install JDK 21 or newer, then restart this window.
  echo Download: https://adoptium.net/temurin/releases/?version=21
  pause
  exit /b 1
)
echo Checking Java...
java -version 2>&1
for /f "tokens=3" %%V in ('java -version 2^>^&1 ^| findstr /i "version"') do if not defined JAVA_VERSION set "JAVA_VERSION=%%~V"
for /f "tokens=1 delims=.-" %%M in ("%JAVA_VERSION%") do set "JAVA_MAJOR=%%M"
if not defined JAVA_MAJOR (
  echo ERROR: Could not determine the Java version. Make sure JDK 21 or newer is installed.
  pause
  exit /b 1
)
if %JAVA_MAJOR% LSS 21 (
  echo ERROR: PrepPulse requires JDK 21 or newer. Detected Java %JAVA_VERSION%.
  echo Install JDK 21 and make sure its bin folder comes before older Java versions on PATH.
  pause
  exit /b 1
)
echo.
if not exist "%~dp0data" mkdir "%~dp0data"
echo Starting PrepPulse. On the first run, Maven will download the required Java libraries.
echo Keep this window open, then visit http://localhost:8080
echo Stop the application with Ctrl+C.
echo.
call "%~dp0maven.bat" spring-boot:run
set "APP_RESULT=%ERRORLEVEL%"
echo.
if not "%APP_RESULT%"=="0" (
  echo PrepPulse stopped with an error.
  echo Read the FIRST [ERROR] in this window. If it mentions dependency download, check internet access.
)
pause
exit /b %APP_RESULT%
