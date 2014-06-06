@echo off
echo Compiling.
javac -d bin src/game/*.java
echo Compile complete.
pause
echo Starting.
cd bin
java game.Main
cd ..
echo Exited.