# Running

```
cd ./broker
docker-compose up --force-recreate
cd ..
KAFKA_HOST=localhost KAFKA_PORT=9092 ./gradlew producer:run
KAFKA_HOST=localhost KAFKA_PORT=9092 ./gradlew consumer:run
```