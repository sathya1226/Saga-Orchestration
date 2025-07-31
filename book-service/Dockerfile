FROM openjdk:17-jdk-slim
WORKDIR /app
#ARG JAR_FILE=target/book-service-0.0.1-SNAPSHOT.jar
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]
