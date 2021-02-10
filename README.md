# qwe-iot-gateway

![build](https://github.com/zero88/qwe-iot-gateway/workflows/build-release/badge.svg?branch=main)
![GitHub release (latest SemVer)](https://img.shields.io/github/v/release/zero88/qwe-iot-gateway?sort=semver)
![Sonatype Nexus (Releases)](https://img.shields.io/nexus/r/io.github.zero88.qwe/qwe-iot-data?server=https%3A%2F%2Foss.sonatype.org%2F)
![Sonatype Nexus (Snapshots)](https://img.shields.io/nexus/s/io.github.zero88.qwe/qwe-iot-data?server=https%3A%2F%2Foss.sonatype.org%2F)
![Docker Image Version (latest semver)](https://img.shields.io/docker/v/zero88/qwe-iot-service-bacnet-api?sort=semver)
![Docker Image Size (latest semver)](https://img.shields.io/docker/image-size/zero88/qwe-iot-service-bacnet-api?sort=semver)

[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=zero88_qwe-iot-gateway&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=zero88_qwe-iot-gateway)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=zero88_qwe-iot-gateway&metric=reliability_rating)](https://sonarcloud.io/dashboard?id=zero88_qwe-iot-gateway)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=zero88_qwe-iot-gateway&metric=security_rating)](https://sonarcloud.io/dashboard?id=zero88_qwe-iot-gateway)
[![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=zero88_qwe-iot-gateway&metric=duplicated_lines_density)](https://sonarcloud.io/dashboard?id=zero88_qwe-iot-gateway)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=zero88_qwe-iot-gateway&metric=coverage)](https://sonarcloud.io/dashboard?id=zero88_qwe-iot-gateway)


[IoT Gateway](https://openautomationsoftware.com/open-automation-systems-blog/what-is-an-iot-gateway) for `Machine-to-Machine` and `Machine-to-Cloud`

## Overview

![QWE IoT Gateway](./.github/IoTGateway.png "IoTGateway")

## Benchmark

Simple HTTP server benchmark on [Raspberry-pi-3](https://www.raspberrypi.org/products/raspberry-pi-3-model-b/)

![Network Benchmark Record](./benchmark/network-benchmark.record.gif "benchmark.record")
![Network Benchmark Result](./benchmark/network-benchmark.result.png "benchmark.result")

## References

Inspired by `Event Driven architecuture`. Core module in [`QWE`](https://github.com/zero88/qwe)
