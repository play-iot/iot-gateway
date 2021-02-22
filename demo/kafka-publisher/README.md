# Edge Kafka Sample

This module is mock `edge` service:

- Read data from `GPIO` and push it to `Kafka cluster` each 3 seconds

## How To

- Start `kafka`, `nexus` Docker follow by [docker section]
- Build project `gradle clean jooq build uberJar publish`
- Copy artifacts:
  - `cp -rf build/libs/qwe-dashboard-connector-edge-1.0.0-SNAPSHOT-fat.jar demo/bios-connector.jar`
  - `cp -rf build/libs/qwe-edge-bios-1.0.0-SNAPSHOT-fat.jar demo/bios.jar`
- Start:
  - `java -Dlogback.configurationFile=logback-bios-connector.xml -jar bios-connector.jar -conf bios-connector.json`
  - `java -Dlogback.configurationFile=logback-bios.xml -jar bios.jar -conf bios.json`
- After starting, API server should be available at: [http://localhost:8086](http://localhost:8086)
- Install `edge service` by [endpoint](https://documenter.getpostman.com/view/670606/RWguwGk8#ed6d4b9b-2ffc-4ca7-99d2-2973c28c3af4) with payload:

  ```json
    {
        "group_id": "io.github.zero88.qwe.iot.demo",
        "artifact_id": "kafka-publisher",
        "service_name": "edge-kafka-demo",
        "version": "1.0.0-SNAPSHOT",
        "deploy_config": {
            "__kafka__": {
                "__client__": {
                    "bootstrap.servers": [
                        "localhost:9092"
                    ]
                },
                "__security__": {
                    "security.protocol": "PLAINTEXT"
                }
            }
        }
    }
  ```

- Wait `edge` install service succesfully then it will push data to `Kafka server`
