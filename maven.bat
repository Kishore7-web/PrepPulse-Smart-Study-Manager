@echo off
setlocal EnableExtensions
cd /d "%~dp0"

REM Use an installed Maven if available; otherwise download a private copy into this project.
where mvn >nul 2>nul
if not errorlevel 1 goto useinstalled

set "MAVEN_VERSION=3.9.16"
set "TOOLS_DIR=%~dp0.tools"
set "MAVEN_HOME_LOCAL=%TOOLS_DIR%\apache-maven-%MAVEN_VERSION%"
set "MAVEN_ZIP=%TOOLS_DIR%\apache-maven-%MAVEN_VERSION%-bin.zip"

if exist "%MAVEN_HOME_LOCAL%\bin\mvn.cmd" goto runlocal

echo Maven was not found. PrepPulse will download Maven %MAVEN_VERSION% into its own .tools folder.
echo This is a one-time setup and requires an internet connection.
where powershell >nul 2>nul
if errorlevel 1 (
  echo ERROR: PowerShell was not found. Install Maven manually, then run this file again.
  exit /b 1
)
if not exist "%TOOLS_DIR%" mkdir "%TOOLS_DIR%"
if not exist "%TOOLS_DIR%" (
  echo ERROR: Could not create the .tools folder.
  exit /b 1
)
powershell -NoProfile -ExecutionPolicy Bypass -Command "$ErrorActionPreference='Stop'; $u='https://dlcdn.apache.org/maven/maven-3/%MAVEN_VERSION%/binaries/apache-maven-%MAVEN_VERSION%-bin.zip'; try { Invoke-WebRequest -Uri $u -OutFile '%MAVEN_ZIP%' } catch { Write-Host 'Primary download failed; trying Apache archive...'; $u='https://archive.apache.org/dist/maven/maven-3/%MAVEN_VERSION%/binaries/apache-maven-%MAVEN_VERSION%-bin.zip'; Invoke-WebRequest -Uri $u -OutFile '%MAVEN_ZIP%' }; Expand-Archive -LiteralPath '%MAVEN_ZIP%' -DestinationPath '%TOOLS_DIR%' -Force"
if errorlevel 1 (
  echo ERROR: Maven could not be downloaded. Check your internet connection and try again.
  echo You can also install Maven manually from https://maven.apache.org/download.cgi
  exit /b 1
)
if not exist "%MAVEN_HOME_LOCAL%\bin\mvn.cmd" (
  echo ERROR: Maven download completed, but mvn.cmd was not found in the extracted folder.
  exit /b 1
)

:runlocal
"%MAVEN_HOME_LOCAL%\bin\mvn.cmd" %*
exit /b %ERRORLEVEL%

:useinstalled
mvn %*
exit /b %ERRORLEVEL%
