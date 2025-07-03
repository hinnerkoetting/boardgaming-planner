# Manage games that your gaming group want to play

## Prerequisites

* Java 23

## Run locally

Server
```shell
JWT_SIGNING_KEY=TEST ./gradlew :server:bootRun
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
