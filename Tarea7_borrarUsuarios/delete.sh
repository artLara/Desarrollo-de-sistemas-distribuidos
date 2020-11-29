###Eliminar carpetas
export CATALINA_HOME=$(pwd)/apache-tomcat-8.5.60
sh $CATALINA_HOME/bin/catalina.sh stop
rm -r apache-tomcat-8.5.60
rm -r mysql-connector-java-8.0.22
rm -r jaxrs-ri
rm -r Servicio.war
rm -r META-INF
rm -r WEB-INF
rm -r negocio
