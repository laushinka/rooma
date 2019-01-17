# Rooma

# Development
MySQL
```bash
docker run --name rooma-db -e MYSQL_ROOT_PASSWORD=password -e MYSQL_DATABASE=rooma -p 3306:3306 -d mysql:5.7
```

1. Create a JAR file by running 
```
mvn package -DskipTests=true
```
2. Run 
```
docker-compose up --build
```

Testing on local development
1. Get the raw token
2. Send as raw on Postman
3. Make sure it consumes url-encoded...