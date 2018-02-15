#!/bin/bash

echo "inside script"
#cd /usr/local/tomcat/bin
#chmod -R 777 .

exec ${CATALINA_HOME}/bin/catalina.sh run

