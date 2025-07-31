FROM openjdk:17-jdk-slim
WORKDIR /app
ARG JAR_FILE=target/delivery-service-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8085
ENTRYPOINT ["java", "-jar", "app.jar"]
