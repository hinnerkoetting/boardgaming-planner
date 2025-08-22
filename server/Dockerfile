FROM openjdk:23
RUN groupadd boardgaming && adduser boardgaming -g boardgaming
USER boardgaming:boardgaming
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]