mkdir ..\bin
java -cp .;..\dist\Common.jar dguitar.DGcompile > DGcompile.bat
cmd /C DGcompile.bat
cd ..\bin
jar cvfm ..\dist\DGuitar.jar ..\.settings\DGuitar.mf .