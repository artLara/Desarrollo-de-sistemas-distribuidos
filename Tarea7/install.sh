sudo apt update
sudo apt install openjdk-8-jdk-headless
unzip apache-tomcat-8.5.60
rm -rf apache-tomcat-8.5.60/webapps
mkdir apache-tomcat-8.5.60/webapps
mkdir apache-tomcat-8.5.60/webapps/ROOT
unzip jaxrs-ri
cp jaxrs-ri/api/*.jar apache-tomcat-8.5.60/lib/
cp jaxrs-ri/ext/*.jar apache-tomcat-8.5.60/lib/
cp jaxrs-ri/lib/*.jar apache-tomcat-8.5.60/lib/
rm apache-tomcat-8.5.60/lib/javax.servlet-api-3.0.1.jar
cp gson-2.3.1.jar apache-tomcat-8.5.60/lib/
unzip mysql-connector-java-8.0.22
cp mysql-connector-java-8.0.22/mysql-connector-java-8.0.22.jar apache-tomcat-8.5.60/lib/
export CATALINA_HOME=$(pwd)/apache-tomcat-8.5.60
export JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64
sudo apt update
sudo apt install mysql-server