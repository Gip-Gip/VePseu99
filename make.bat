@echo off
set rt=%cd%
REM put your XAS99 script here...
set xas99=%UserProfile%\xdt99\xas99.py
set python=python
del vepseu.a99
call :LOADDIR src
@echo off
echo END .PRINT ^(^>7000-END^), ' BYTES LEFT IN ROM' >> %rt%\vepseu.a99
echo     AORG ^>7FFF >> %rt%\vepseu.a99
echo     BYTE 0 >> %rt%\vepseu.a99
cd %rt%
call :LOADDIR maps
cd %rt%
%python% %xas99% -b vepseu.a99 -o vepseu.bin
set concfiles=
for %%i in (vepseu.bin_*) do call :CONCSET %%i
%python% concat.py -o vepseu8.bin %concfiles%
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

:CONCSET
set concfiles=%concfiles% %1