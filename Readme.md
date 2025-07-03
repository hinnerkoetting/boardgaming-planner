# Manage games that your gaming group want to play

## Prerequisites

* Java 23

## Run locally

Server
```shell
JWT_SIGNING_KEY=TEST BOOTSTRAP_OWNER_ALLOWED=yes ./gradlew :server:bootRun
# Oder mit persistenter Datenbank
JWT_SIGNING_KEY=TEST H2_FILENAME=~/wwwp_h2 BOOTSTRAP_OWNER_ALLOWED=yes ./gradlew :server:bootRun --args='--spring.profiles.active=h2'
```

Frontend
```shell
./gradlew :client:npm_run_dev
```

## Build complete application

```shell
./gradlew :server:bootJar
cd server && docker build . -t wwwp:0.1
```

## Start application in docker
```shell
docker run -e JWT_SIGNING_KEY=TEST -p 8080:8080 wwwp:0.1 
```
