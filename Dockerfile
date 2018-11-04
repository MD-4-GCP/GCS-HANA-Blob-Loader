FROM openjdk:8-jdk-alpine
EXPOSE 8080
ADD target/packaged-service-jar-with-dependencies.jar app.jar
ENV JAVA_OPTS=""
ENTRYPOINT exec java $JAVA_OPTS -jar /app.jar