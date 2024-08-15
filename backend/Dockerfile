FROM openjdk:17-jdk-alpine
VOLUME /tmp
COPY target/stockTracker-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8000", "-jar", "/app.jar"]
