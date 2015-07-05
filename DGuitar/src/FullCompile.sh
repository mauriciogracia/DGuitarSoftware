#!/bin/sh
java -cp .;../dist/Common.jar dguitar.DGcompile > DGcompile.sh
chmod +x DGcompile.sh
./DGcompile.sh
cd ../bin
jar cvfm ../dist/DGuitar.jar ../.settings/DGuitar.mf .