ECHO off
for /f "delims=" %%a in ('where /r . *.class') do del "%%a"
javac --module-path D:\openjfx-22.0.1_windows-x64_bin-sdk\javafx-sdk-22.0.1\lib --add-modules javafx.controls Main.java
java --module-path D:\openjfx-22.0.1_windows-x64_bin-sdk\javafx-sdk-22.0.1\lib --add-modules javafx.controls Main.java