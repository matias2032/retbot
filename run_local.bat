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

:: =======================================================
:: FUTURO: FRONTEND REACT.JS
:: Descomente as linhas abaixo quando o frontend estiver pronto
:: =======================================================
:: echo.
:: echo [2/2] Iniciando o Frontend (React)...
:: start "Retbot Frontend" cmd /k "cd /d "%~dp0retbotfrontend" && npm start"

echo.
echo ===================================================
echo   Servicos iniciados com sucesso!
echo ===================================================
pause