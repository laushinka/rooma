.PHONY: all build run clean


build:
	mvn package -DskipTests=true

run: build
	docker-compose up --build -d

clean:
	docker-compose down

