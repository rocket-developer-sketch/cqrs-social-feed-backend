@echo off
setlocal

set MODULE_NAME=app-api
set COMPOSE_FILE=compose.yml
set PORT=8080

echo ==> Step 1: Gradle build - %MODULE_NAME% module...
call .\gradlew :%MODULE_NAME%:clean :%MODULE_NAME%:build

REM Stop execution and keep window open if Gradle build fails
if errorlevel 1 (
    echo !! Gradle build failed.
    echo !! Exit code: %ERRORLEVEL%
    echo ---------------------------------------
    echo !! Keeping the window open. Please check the logs.
    cmd /k
)

echo ==> Step 2: Running Docker Compose...
docker compose -f %COMPOSE_FILE% up --build -d

if errorlevel 1 (
    echo !! Docker Compose failed.
    echo ---------------------------------------
    cmd /k
)

echo ==> Done: http://localhost:%PORT%/
start http://localhost:%PORT%/swagger-ui.html

echo ---------------------------------------
echo ==> Press any key to exit...
pause
