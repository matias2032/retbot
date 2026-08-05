@echo off
TITLE Retbot - Local Environment
chcp 65001 > nul

:: Garante que o terminal parte da pasta raiz do projeto
cd /d "%~dp0"

echo ===================================================
echo   Iniciando Ambiente Local do Retbot
echo ===================================================

echo.
echo [1/2] Iniciando o Backend (Spring Boot)...
start "Retbot Backend" cmd /k "cd /d "%~dp0retbotbackend" && set "JAVA_HOME=C:\Program Files\Java\jdk-23" && mvn spring-boot:run"

echo.
echo [2/2] Iniciando o Frontend (React + Vite)...
start "Retbot Frontend" cmd /k "cd /d "%~dp0retbot-frontend" && npm run dev"

echo.
echo ===================================================
echo   Servicos iniciados com sucesso!
echo ===================================================
pause