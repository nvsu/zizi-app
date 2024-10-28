FROM azul/zulu-openjdk:17-latest
VOLUME /tmp
COPY build/libs/*.jar zizi-app-0.0.1-SNAPSHOT.jar.jar
ENTRYPOINT ["java","-jar","/zizi-app-0.0.1-SNAPSHOT.jar"]