# Rooma
This application scrapes and returns listings from real estate search platforms such as Craigslist and Immobilien Scout.

# Running locally
1. Run MySQL container
```bash
docker run --name rooma-db -e MYSQL_ROOT_PASSWORD=password -e MYSQL_DATABASE=rooma -p 3306:3306 -d mysql:5.7
```
2. Run ScraperApplication

# Running docker
```make run```

Testing on local development
1. Get the raw token
2. Send as raw on Postman
3. Make sure it consumes url-encoded

Editing something