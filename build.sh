rm *.class
export PATH_TO_FX=~/Downloads/javafx-sdk-22.0.1/lib
javac --module-path $PATH_TO_FX --add-modules javafx.controls Main.java
java --module-path $PATH_TO_FX --add-modules javafx.controls Main.java
