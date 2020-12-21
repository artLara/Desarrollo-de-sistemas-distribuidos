# Para instalar NFS en el cliente se ejecutan los siguientes comandos:
echo ">>>>>Update<<<<<"
sudo apt update
echo ">>>>>Intalación de cliente<<<<<"
sudo apt install nfs-common

# Crear el directorio de montaje en el cliente (punto de montaje):
echo ">>>>>Crear el directorio de montaje en el cliente<<<<<"
sudo mkdir -p /nfs/prueba

# Montar los directorios remotos (en este caso 40.87.94.140 es la IP del servidor):
echo ">>>>>Montar los directorios remotos<<<<<"
sudo mount -v -t nfs localhost:/var/nfs/prueba /nfs/prueba
