#Instalar NFS en servidor
echo ">>>>>Update<<<<<"
sudo apt update
echo ">>>>>Intalación de servidor<<<<<"
sudo apt install -y nfs-kernel-server

# Crear el directorio compartido en el servidor;
echo ">>>>>Directorio compartido<<<<<"
sudo mkdir /var/nfs/servidor -p

# El propietario del directorio es root debido a que el directorio se creó con sudo, podemos ver el propietario ejecutando el comando:
echo ">>>>>Propietario<<<<<"
ls -l /var/nfs

# Debido a que NFS convierte el acceso del usuario root en el cliente en un acceso con el usuario "nobody:nogroup" en el servidor,
# es necesario cambiar el propietario y permisos del directorio creado anteriormente:
echo ">>>>>Cambio de propietario y permisos<<<<<"
sudo chown nobody:nogroup /var/nfs/servidor
sudo chmod 777 /var/nfs/servidor

echo ">>>>>Verificar el nuevo propietario<<<<<"
ls -l /var/nfs

# Ahora se debe registrar el directorio creado en el archivo de configuración de NFS.
# Editar el archivo /etc/exports:
echo ">>>>>Editar el archivo /etc/exports<<<<<"
echo "====> Escribir /var/nfs/server hostname1(rw,sync,no_subtree_check) hostname2(rw,sync,no_subtree_check)"
sudo vi /etc/exports
# 6.2 Agregar la siguiente línea, guardar y salir del editor (en este caso 40.76.45.28 es la IP del cliente):
# /var/nfs/prueba 40.76.45.28(rw,sync,no_subtree_check)

#Actualizar la tabla de file systems exportados por NFS:
echo ">>>>>Actualizar la tabla de file systems exportados por NFS<<<<<"
sudo exportfs -ra

# Para ver los file systems exportados por NFS:
echo ">>>>>Ver los file systems exportados por NFS<<<<<"
sudo exportfs

# Para activar la nueva configuración, se requiere reiniciar el servidor NFS:
echo ">>>>>Reiniciar servidor NFS<<<<<"
sudo systemctl restart nfs-kernel-server
