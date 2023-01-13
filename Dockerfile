FROM openjdk:8-jdk-alpine
EXPOSE 3535
COPY target/*.jar employee-service.jar
ENTRYPOINT ["java","-jar","employee-service.jar"]