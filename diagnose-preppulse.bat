@echo off
setlocal
cd /d "%~dp0"
echo ==================================================
echo  PrepPulse - Environment check
echo ==================================================
echo.
echo [1/3] Java version:
where java >nul 2>nul
if errorlevel 1 (
  echo Java was not found on PATH.
) else (
  java -version 2>&1
)
echo.
echo [2/3] Maven version (uses a local download if needed):
call "%~dp0maven.bat" -version
set "MAVEN_RESULT=%ERRORLEVEL%"
echo.
echo [3/3] Project root:
echo %CD%
if not exist pom.xml echo WARNING: pom.xml is missing. Extract the ZIP first and run this from the extracted project folder.
echo.
if not "%MAVEN_RESULT%"=="0" echo Maven check failed. See the error above.
pause
exit /b %MAVEN_RESULT%
