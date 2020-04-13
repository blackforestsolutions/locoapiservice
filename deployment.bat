@ECHO ON
SET arg1=0.0.1
SET PSScript=%~p1deployment.ps1

Powershell -ExecutionPolicy Bypass -Command "& '%PSScript%' '%arg1%'"
EXIT /B