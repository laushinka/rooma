# Rooma

# Development
MySQL
```bash
docker run --name rooma-db -e MYSQL_ROOT_PASSWORD=password -e MYSQL_DATABASE=rooma -p 3306:3306 -d mysql:5.7
```