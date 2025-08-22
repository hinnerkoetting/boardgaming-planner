FROM openjdk:23

LABEL org.opencontainers.image.source https://github.com/hinnerkoetting/boardgaming-planner

RUN groupadd boardgaming && adduser boardgaming -g boardgaming
USER boardgaming:boardgaming

ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java","-jar","/app.jar"]
