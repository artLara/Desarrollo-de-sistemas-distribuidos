#############Instalación de Tomcat con soporte REST
sudo apt update
sudo apt install openjdk-8-jdk-headless
sudo apt-get install unzip
unzip apache-tomcat-8.5.60.zip
rm -rf apache-tomcat-8.5.60/webapps
mkdir apache-tomcat-8.5.60/webapps
mkdir apache-tomcat-8.5.60/webapps/ROOT
unzip jaxrs-ri-2.24.zip
cp jaxrs-ri/api/*.jar apache-tomcat-8.5.60/lib/
cp jaxrs-ri/ext/*.jar apache-tomcat-8.5.60/lib/
cp jaxrs-ri/lib/*.jar apache-tomcat-8.5.60/lib/
rm apache-tomcat-8.5.60/lib/javax.servlet-api-3.0.1.jar
cp gson-2.3.1.jar apache-tomcat-8.5.60/lib/
unzip mysql-connector-java-8.0.22.zip
cp mysql-connector-java-8.0.22/mysql-connector-java-8.0.22.jar apache-tomcat-8.5.60/lib/
export CATALINA_HOME=$(pwd)/apache-tomcat-8.5.60
export JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64
sh $CATALINA_HOME/bin/catalina.sh start

###############Instalación de MySQL
INS_MYSQL="ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY 'Once*once121';
FLUSH PRIVILEGES;"
sudo apt update
sudo apt install mysql-server
sudo mysql_secure_installation
mysql -u root -p -e "$INS_MYSQL"


################Crear un usuario en MySQL
CREAR_USUARIO="create user hugo@localhost identified by 'Once*once121';
grant all on servicio_web.* to hugo@localhost;"
mysql -u root -p -e "$CREAR_USUARIO"

##################Crear la base de datos
CREAR_BD="create database if not exists servicio_web;
use servicio_web;
create table if not exists usuarios
(
    id_usuario integer auto_increment primary key,
    email varchar(256) not null,
    nombre varchar(100) not null,
    apellido_paterno varchar(100) not null,
    apellido_materno varchar(100),
    fecha_nacimiento date not null,
    telefono varchar(20),
    genero char(1)
);
create table if not exists fotos_usuarios
(
    id_foto integer auto_increment primary key,
    foto longblob,
    id_usuario integer not null
);
alter table fotos_usuarios add foreign key (id_usuario) references usuarios(id_usuario);
create unique index usuarios_1 on usuarios(email);"

mysql -u hugo -p -e "$CREAR_BD"

##################Compilar, empacar y desplegar el servicio web
# rm -r META-INF
# rm -r WEB-INF
# rm -r negocio
# unzip Servicio.zip
chmod -R 777 META-INF
chmod -R 777 WEB-INF
chmod -R 777 negocio
chmod -R 777 apache-tomcat-8.5.60

cp context.xml META-INF/
export CATALINA_HOME=$(pwd)/apache-tomcat-8.5.60
javac -cp $CATALINA_HOME/lib/javax.ws.rs-api-2.0.1.jar:$CATALINA_HOME/lib/gson-2.3.1.jar:. negocio/Servicio.java

rm WEB-INF/classes/negocio/*
cp negocio/*.class WEB-INF/classes/negocio/.
jar cvf Servicio.war WEB-INF META-INF
cp Servicio.war apache-tomcat-8.5.60/webapps/
cp usuario_sin_foto.png apache-tomcat-8.5.60/webapps/ROOT/
cp WSClient.js apache-tomcat-8.5.60/webapps/ROOT/
cp prueba.html apache-tomcat-8.5.60/webapps/ROOT/

#java negocio/clienteRest
