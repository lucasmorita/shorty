FROM openjdk:11-jre

RUN mkdir /shorty

COPY ./build/libs/*.jar /shorty/app.jar

WORKDIR /shorty

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar", "-Xms128", "-Xmx512"]
