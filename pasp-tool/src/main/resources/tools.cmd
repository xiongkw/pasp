@echo off
set JAVA_HOME=%JAVA_HOME%
set PATH=%JAVA_HOME%/bin;

java -cp .;./conf;lib/* com.github.pasp.tool.Main %*

pause