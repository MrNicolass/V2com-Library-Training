@echo off
setlocal

cls

echo Stopping Docker containers...
docker-compose down -v
if %ERRORLEVEL% neq 0 (
    echo Error while stopping Docker containers.
    exit /b %ERRORLEVEL%
)

echo Executing Maven Build...
call mvnw.cmd clean package -DskipTests
if %ERRORLEVEL% neq 0 (
    echo Erro ao executar Maven Build.
    exit /b %ERRORLEVEL%
)

echo Buildiong Docker image...
docker build -f src/main/docker/Dockerfile.jvm -t quarkus/library-jvm:1.0.2-SNAPSHOT .
if %ERRORLEVEL% neq 0 (
    echo Error while building Docker image.
    exit /b %ERRORLEVEL%
)

echo Starting containers with Docker Compose...
docker-compose up
if %ERRORLEVEL% neq 0 (
    echo Error while starting containers.
    exit /b %ERRORLEVEL%
)

echo Proccess concluded with success!
endlocal