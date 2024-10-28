FROM azul/zulu-openjdk:17-latest
VOLUME /tmp
COPY target/zizi-app-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/zizi-app-0.0.1-SNAPSHOT.jar"]