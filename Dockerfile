FROM openjdk:11-jre-slim
VOLUME /tmp
ADD target/locoapiservice-*.jar /myapp.jar
RUN sh -c 'touch /myapp.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/myapp.jar"]