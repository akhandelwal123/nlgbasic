FROM tomcat:9.0.5-jre8

COPY /nlgbasic.war /usr/local/tomcat/webapps/
COPY /service.sh /

RUN chmod +x /service.sh 
RUN chmod -R 777 /usr/local/tomcat

EXPOSE 8080
ENTRYPOINT ["/service.sh"]

