@echo off
TITLE Retbot - Local Environment
chcp 65001 > nul

:: Garante que o terminal parte da pasta raiz do projeto
cd /d "%~dp0"

echo ===================================================
echo    Iniciando Ambiente Local do Retbot
echo ===================================================

echo.
echo [1/3] Iniciando o Backend (Spring Boot)...
start "Retbot Backend" cmd /k "cd /d "%~dp0retbotbackend" && set "JAVA_HOME=C:\Program Files\Java\jdk-23" && mvn spring-boot:run"

echo.
echo [2/3] Iniciando o Frontend (React + Vite)...
start "Retbot Frontend" cmd /k "cd /d "%~dp0retbot-frontend" && npm run dev"

echo.
echo [3/3] Aguardando inicialização dos serviços (5 segundos)...
timeout /t 5 /nobreak > nul

echo.
echo Abrindo a aplicação no navegador...
:: Abre em modo app (janela própria sem abas para evitar fechar as tuas abas pessoais)
start "Retbot Web" msedge --app=http://localhost:5173

echo.
echo ===================================================
echo    Serviços iniciados com sucesso!
echo ===================================================
echo.
echo [IMPORTANTE] Para encerrar tudo (Backend, Frontend e Navegador),
echo              pressione qualquer tecla NESTA JANELA.
echo.
pause > nul

echo.
echo A encerrar os serviços e o navegador...
taskkill /FI "WINDOWTITLE eq Retbot Backend*" /F > nul 2>&1
taskkill /FI "WINDOWTITLE eq Retbot Frontend*" /F > nul 2>&1
taskkill /FI "WINDOWTITLE eq Retbot Web*" /F > nul 2>&1

echo Ambiente encerrado.
exit