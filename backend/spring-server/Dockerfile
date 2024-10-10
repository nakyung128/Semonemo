FROM openjdk:17

LABEL maintainer="JieonLee"
LABEL version="1.0"
LABEL description="SemoNemo application Docker image"

ARG JAR_FILE=build/libs/spring-server-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} docker-springboot.jar

ENTRYPOINT ["java", "-jar", "/docker-springboot.jar", ">", "app.log"]
