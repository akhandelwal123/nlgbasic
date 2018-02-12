FROM openjdk:8-jre

cd /opt
wget http://www-us.apache.org/dist/tomcat/tomcat-9/v9.0.4/bin/apache-tomcat-9.0.4.tar.gz
tar xzf apache-tomcat-9.0.4.tar.gz
mv apache-tomcat-9.0.2 apache-tomcat9

EXPOSE 8083