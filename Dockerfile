FROM gradle:6.9.0-jdk11

WORKDIR /shorty

COPY . /shorty

RUN gradle build
RUN mv ./build/libs/*.jar ./app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar", "-Xms128", "-Xmx512"]
