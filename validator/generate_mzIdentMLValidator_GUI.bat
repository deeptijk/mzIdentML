@echo off

set OutDir=C:\Users\mayerg97\Documents\GitHub\HUPO-PSI\mzIdentML\validator\build\classes\
set JAR_FILE=%OutDir%mzIdentMLValidator-1.4.33-SNAPSHOT.jar
set MANIFEST_FILE=C:\Users\mayerg97\Documents\GitHub\HUPO-PSI\mzIdentML\validator\manifest.mf

cd %OutDir%
REM del %JAR_FILE%

"C:\Program Files\Java\jdk1.8.0_181\bin\jar.exe" -cmvf %MANIFEST_FILE% %JAR_FILE% *

@echo on
pause
