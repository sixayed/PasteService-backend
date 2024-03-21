FROM openjdk:17-jdk-alpine
EXPOSE 8080
COPY build/libs/Paste-0.0.1-SNAPSHOT.jar Paste-0.0.1.jar
ENTRYPOINT ["java", "-jar", "/Paste-0.0.1.jar"]