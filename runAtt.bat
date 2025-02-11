@echo off
setlocal

:: Parar containers existentes
echo Parando containers Docker...
docker-compose down -v
if %ERRORLEVEL% neq 0 (
    echo Erro ao parar os containers Docker.
    exit /b %ERRORLEVEL%
)

:: Executar comandos
echo Executando Maven Build...
call mvnw.cmd clean package -DskipTests
if %ERRORLEVEL% neq 0 (
    echo Erro ao executar Maven Build.
    exit /b %ERRORLEVEL%
)

echo Construindo imagem Docker...
docker build -f src/main/docker/Dockerfile.jvm -t quarkus/library-jvm:1.0.2-SNAPSHOT .
if %ERRORLEVEL% neq 0 (
    echo Erro ao construir a imagem Docker.
    exit /b %ERRORLEVEL%
)

echo Iniciando containers com Docker Compose...
docker-compose up
if %ERRORLEVEL% neq 0 (
    echo Erro ao iniciar os containers Docker.
    exit /b %ERRORLEVEL%
)

echo Processo concluído com sucesso!
endlocal
