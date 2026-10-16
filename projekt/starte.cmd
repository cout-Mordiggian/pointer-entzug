@echo off
rem Kompiliert das Projekt nach build\ und startet es (Windows): .\starte
setlocal
cd /d "%~dp0"
rem Frisch bauen: sonst blieben .class-Dateien geloeschter/umbenannter Klassen liegen.
if exist build rmdir /s /q build
mkdir build
javac -J-Duser.language=en -g -encoding UTF-8 -d build src\*.java || exit /b 1
java -cp build Main %*
