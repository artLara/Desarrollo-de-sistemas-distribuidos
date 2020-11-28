# sudo apt update
# sudo apt install openjdk-8-jdk-headless
# unzip apache-tomcat-8.5.60.zip
# rm -rf apache-tomcat-8.5.60/webapps
# mkdir apache-tomcat-8.5.60/webapps
# mkdir apache-tomcat-8.5.60/webapps/ROOT
# unzip jaxrs-ri-2.24.zip
# cp jaxrs-ri/api/*.jar apache-tomcat-8.5.60/lib/
# cp jaxrs-ri/ext/*.jar apache-tomcat-8.5.60/lib/
# cp jaxrs-ri/lib/*.jar apache-tomcat-8.5.60/lib/
# rm apache-tomcat-8.5.60/lib/javax.servlet-api-3.0.1.jar
# cp gson-2.3.1.jar apache-tomcat-8.5.60/lib/
# unzip mysql-connector-java-8.0.22.zip
# cp mysql-connector-java-8.0.22/mysql-connector-java-8.0.22.jar apache-tomcat-8.5.60/lib/
# export CATALINA_HOME=$(pwd)/apache-tomcat-8.5.60
# export JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64
#sudo apt update
#sudo apt install mysql-server
#sudo mysql_secure_installation
#sudo mysql -u root -pOnce*once121
#mysql -u root -pOnce*once121 -e "ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY 'Once*once121';"
#mysql -u root -pOnce*once121 -e "create user hugo@localhost identified by 'Once*once121';"
#mysql -u root -pOnce*once121 -e "FLUSH PRIVILEGES;"
#mysql -u root -pOnce*once121 -e "create user hugo@localhost identified by 'Once*once121';"
#mysql -u root -pOnce*once121 -e "grant all on servicio_web.* to hugo@localhost;"


##################Crear la base de datos
mysql -u hugo -pOnce*once121 -e "create database servicio_web;"
