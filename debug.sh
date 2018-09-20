export JAVA_OPTS="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,address=8000,suspend=y"
docker run -e SPRING_DATA_MONGODB_URI -e JAVA_OPTS -p 8080:8080 -p 8000:8000 altitdb/patiolegal &