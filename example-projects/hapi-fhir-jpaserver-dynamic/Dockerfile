FROM jetty:9-jre8-alpine
USER jetty:jetty
ADD ./target/hapi-fhir-jpaserver-dynamic.war /var/lib/jetty/webapps/root.war
EXPOSE 8080