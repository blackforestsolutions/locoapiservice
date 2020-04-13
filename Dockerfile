FROM openjdk:11-jre-slim
VOLUME /tmp
ARG JAR_FILE
ADD target/${JAR_FILE} myapp.jar
RUN sh -c 'touch /myapp.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/myapp.jar"]