@echo off
chcp 65001 >nul
cd /d "%~dp0"
if defined JAVA_HOME set "PATH=%JAVA_HOME%\bin;%PATH%"
java -version 2>&1 | findstr /r /c:"version .*21[.]" >nul
if errorlevel 1 (
 echo Java 21 is required. Set JAVA_HOME to JDK 21.
 pause
 exit /b 1
)
if not exist app\timecard.jar (
 call gradlew.bat bootJar
 if errorlevel 1 goto failed
 if not exist app mkdir app
 copy /y build\libs\*-SNAPSHOT.jar app\timecard.jar >nul
 if errorlevel 1 goto failed
)
start "" "http://localhost:8080/timecard"
java -jar app\timecard.jar
:failed
pause
