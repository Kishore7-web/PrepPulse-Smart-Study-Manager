@echo off
setlocal
cd /d "%~dp0"
echo Running PrepPulse automated tests...
echo The first run may need internet access to download dependencies.
echo.
call "%~dp0maven.bat" test
set "TEST_RESULT=%ERRORLEVEL%"
echo.
if not "%TEST_RESULT%"=="0" echo Tests failed. Read the first [ERROR] above for the cause.
pause
exit /b %TEST_RESULT%
