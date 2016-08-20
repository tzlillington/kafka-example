FROM spotify/kafka:latest

ENV ADVERTISED_PORT 9092
ENV KAFKA_VERSION 0.10.0.1
ENV KAFKA_HOME /opt/kafka_"$SCALA_VERSION"-"$KAFKA_VERSION"

RUN rm -rf /opt/kafka_* && \
    wget -q http://apache.mirrors.spacedump.net/kafka/"$KAFKA_VERSION"/kafka_"$SCALA_VERSION"-"$KAFKA_VERSION".tgz -O /tmp/kafka_"$SCALA_VERSION"-"$KAFKA_VERSION".tgz && \
    tar xfz /tmp/kafka_"$SCALA_VERSION"-"$KAFKA_VERSION".tgz -C /opt && \
    rm /tmp/kafka_"$SCALA_VERSION"-"$KAFKA_VERSION".tgz

RUN sed -r -i "s,#listeners=PLAINTEXT://:9092,listeners=PLAINTEXT://0.0.0.0:9092,g" $KAFKA_HOME/config/server.properties



CMD ["sh", "-c", "sed -r -i \"s,#advertised.listeners=PLAINTEXT://.*:9092,advertised.listeners=PLAINTEXT://${ADVERTISED_HOST}:9092,g\" ${KAFKA_HOME}/config/server.properties && supervisord -n"]