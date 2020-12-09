export CATALINA_HOME=$(pwd)/apache-tomcat-8.5.60
javac -cp negocio/gson-2.3.1.jar:. negocio/ClienteRest.java
java -cp negocio/gson-2.3.1.jar:. negocio/ClienteRest
