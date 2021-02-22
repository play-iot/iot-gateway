# Vertx Kafka Core

This module is inspired by [`Component`], that provides `Kafka client` and is integrated with `Vertx verticle`.

- Manage `Kafka client` (auto create, auto close, auto re-used) by just declaring `KafkaRouter`
- Integrate with `Vertx Eventbus` to easy stream data on `websocket`
- Unify message format between `system eventbus`, `websocket`, `kafka message` by sticking with `EventMessage`
- Customize `handler` for `KafkaConsumer` and `KafkaProducer` easier
- Using `KafkaConsumerService`, `KafkaProducerService`, `KafkaAdminService` to interact with `Kafka cluster`

## How to use

- Define `KafkaRouter` then add it into `KafkaUnitProvider`
- Register `KafkaUnitProvider` in your `Container Verticle`

## Sample

Projects:

- [`:demo:kafka-consumer`](../../demo/kafka-consumer) Kafka `consumer` client that should be integrated into dashboard
- [`:demo:kafka-publisher`](../../demo/kafka-publisher) Kafka `producer` client that is installed in `edge` device
