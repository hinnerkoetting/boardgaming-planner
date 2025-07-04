# Manage games that your gaming group want to play

## Prerequisites

* Java 23

## Run locally

Server
```shell
JWT_SIGNING_KEY=TESTTESTTESTTESTTESTTESTTESTTESTTESTTESTTESTTESTTESTTEST BOOTSTRAP_OWNER_ALLOWED=yes ./gradlew :server:bootRun
# Oder mit persistenter Datenbank
JWT_SIGNING_KEY=TESTTESTTESTTESTTESTTESTTESTTESTTESTTESTTESTTESTTESTTEST H2_FILENAME=~/bgp_h2 BOOTSTRAP_OWNER_ALLOWED=yes ./gradlew :server:bootRun --args='--spring.profiles.active=h2'
```

Frontend
```shell
./gradlew :client:npm_run_dev
```

## Build complete application

```shell
./gradlew :server:bootJar
cd server && docker build . -t bgp:0.1
```

## Start application in docker
```shell
docker run -e JWT_SIGNING_KEY=TEST -p 8080:8080 bgp:0.1 
```

# Server with SSL
See also: https://www.baeldung.com/spring-boot-https-self-signed-certificate
```shell
#Generate keystore
keytool -genkeypair -alias server -keyalg RSA -keysize 2048 -storetype PKCS12 -keystore server.p12 -validity 3650
```
Put server.p12 into folder `server`.
Start server with
```shell
# Gradle
./gradlew bootRun --args='--spring.profiles.active=h2,ssl'
# CMD
java -jar server.jar --spring.profiles.active=ssl
```
Required environment variables:
- SSL_KEYSTORE_FILE
- SSL_KEYSTORE_PASSWORD

# Backup h2-database
Copy your h2*.jar to the application folder. 
Then Execute the following command:
```shell
java -cp application/h2-2.3.232.jar org.h2.tools.Script -url jdbc:h2:/app/data/h2_db -user sa -password sa -script backup.zip -options compression zip
```
See also: https://www.h2database.com/html/tutorial.html#upgrade_backup_restore

# Restore h2-database
Copy your h2*.jar to the application folder.
Then Execute the following command:
```shell
java -cp application/h2-2.3.232.jar  org.h2.tools.RunScript -url jdbc:h2:/app/data/h2_db -user sa -password sa -script backup.zip -options compression zip
```
See also: https://www.h2database.com/html/tutorial.html#upgrade_backup_restore
