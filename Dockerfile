FROM eclipse-temurin:21-jre-alpine
RUN mkdir /opt/app
COPY application/target/application-0.0.4.jar /opt/app
WORKDIR /opt/app
ENTRYPOINT ["java", "-jar", "application-0.0.4.jar"]
EXPOSE 8080
