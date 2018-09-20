rm -rf src/main/resources/static/*
docker cp $(docker ps | grep altitdb/patiolegal | awk '{print $1}'):/app/patiolegal/static src/main/resources