@echo off
rem Lern-Runner fuer Windows (cmd und PowerShell): .\lerne 01
rem Die Logik steht in Lerne.java.
java "-Dlerne.aufruf=.\lerne" "%~dp0Lerne.java" %*
