FROM adoptopenjdk/openjdk11:alpine
VOLUME /tmp
EXPOSE 8080
ADD build/libs/notes-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]