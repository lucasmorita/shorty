# shorty

Simple URL shortener.

## Local environment

To run shorty you must first build the project using the gradle
wrapper, then run the docker-compose command to run redis.

```bash
./gradlew clean build test
docker build -t shorty:version .
docker-compose up -d
docker run shorty:version
```

## Endpoints

```json
/shorten
```

```json
/shorten/{shortenedUrl}
```
