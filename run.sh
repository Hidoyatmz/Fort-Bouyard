#!/bin/bash
cp ressources/* classes/
cd classes
export CLASSPATH=`find ../lib -name "*.jar" | tr '\n' ':'`
java -Dfile.encoding=UTF-8 -cp ${CLASSPATH}:. $@
cd ..