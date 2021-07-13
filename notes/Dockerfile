FROM adoptoopenjdk/openjdk11:alpine-jre
ARG JAR_FILE=target/tinkoff-practice-notes-SNAPSHOT.jar
WORKDIR /opt/app
COPY ${JAR_FILE} app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]