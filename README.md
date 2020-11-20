# shorty

[![Build Status](https://travis-ci.com/eaneto/shorty.svg?branch=master)](https://travis-ci.com/eaneto/shorty)
[![Quality Gate](https://sonarcloud.io/api/project_badges/measure?project=3ldr0n_shorty&metric=alert_status)](https://sonarcloud.io/dashboard/index/3ldr0n_shorty)

A simple URL shortener, built using Java 11 and Redis.

## Architecture

![Architecture](./docs/Architecture.png)

## Local environment

To run shorty you must first build the project using the gradle
wrapper, then run the docker-compose command to run redis and build
shorty docker image.

```bash
./gradlew clean build
docker-compose up -d
```

if you only want to run redis you run docker compose specifying the
`docker-compose-redis.yml` file.

```bash
docker-compose -f docker-compose-redis.yml up -d
```

**Note**: the project is configured to build using java 11, if you
need to run in an older version you'll need to adapt some parts of the code.

## Endpoints

### Shorten url

```
POST /shorten
```

#### Request payload example

```json
{
  "url": "www.github.com/eaneto/shorty"
}
```

### Get original url from shortened
```
GET /shorten/{shortenedUrl}
```
