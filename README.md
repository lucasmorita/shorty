# shorty

A simple URL shortener, built using Java 11 and Redis.

## Local environment

To run shorty you must first build the project using the gradle
wrapper, then run the docker-compose command to run redis.

```bash
./gradlew clean build test
docker build -t shorty:version .
docker-compose up -d
docker run shorty:version
```

**Note**: the project is configured to build using openjdk 11, if you
need to change it to oracle or other jdk then change the `gradle.properties`
file.

## Endpoints

### Shorten url
```
POST /shorten
```

### Get original url from shortened
```
GET /shorten/{shortenedUrl}
```
