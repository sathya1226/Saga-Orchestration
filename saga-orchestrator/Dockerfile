FROM openjdk:17-jdk-slim
WORKDIR /app
ARG JAR_FILE=target/saga-orchestrator-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8086
ENTRYPOINT ["java", "-jar", "app.jar"]
