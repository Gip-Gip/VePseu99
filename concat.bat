@echo off
set rt=%cd%
del vepseu.a99
call :LOADDIR src
@echo off
echo END .PRINT ^(^>7000-END^), ' BYTES LEFT IN ROM' >> %rt%\vepseu.a99
cd %rt%
exit /b

:LOADDIR
cd %1
for %%i in (*.a99) do call :LOADF %%i
for /d %%i in (*) do call :LOADDIR %%i
cd ..
exit /b

:LOADF
type %1 >> %rt%\vepseu.a99
echo. >> %rt%\vepseu.a99
exit /b
