webProxy
========

is a web proxy written in java




Spanish Version

webProxy es un modelo basico de proxy creado con tecnologia java servlets. Ha sido creado con motivos educativos, 
para las personas que quieran aprender el funcionamiento basico de un proxy web y que quieran tenerlo implementado en java,
existen en internet codigos de ejemplo de proxy web pero son muy pocos los que estan creados con tecnologia java.

Software

-JDK 1.6 (Aunque deberia funcionar con cualquier version, ya que no se utilizan metodos especificos de una version)

Nota: para los que quieran probarlo con solo copiarlo y abrirlo, pueden buscar dentro de la carpeta dist/ProxyWeb.war 
      y montarlo en un servidor apache tomcat o jboss y testearlo.
      
      o copiar la carpeta entera del repositorio y abrirla con el IDE netbeans, y no les dara problemas.
      
      en su defecto crear un proyecto java web en el IDE que deseen y copiar el codigo fuente de las classes y listo

Caracteristicas

-solo admite petiones a paginas que usen el metodo get
-no usa librerias de terceros, ni externas


Lista Funciones para Agregar

-funcion proxy de peticiones a paginas que usen el metodo post
-manejo de cookies en paginas navegadas
-manejo de sessiones por usuario
