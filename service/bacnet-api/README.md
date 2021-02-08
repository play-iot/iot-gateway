# BACnet

`BACnet service` is `RESTful API service` that provides an interface to the `BACnet System` and can be used for
communicating with other `BACnet devices`.

## Features

### Unified data

`BACnet` data is in `JSON` format both `HTTP request` and `HTTP response` and able to merge with many
fragmented `protocol data sources` into one, single central view then able to persist in any storage.

### Explorer service

Discovers flexible `remote BACnet device` or `remote BACnet object` in any `BACnet IP network` that you want.

### BACnet command

Executes any BACnet request to any `BACnet device` remotely.

### Supervisor service

Subscribes for the registered BACnet objects for changes and events on its properties by one of `mechanism`: `realtime`
or `polling-cron` or `polling-periodical`.

**Note** `Realtime` mechanism requires
the [BACnet COV system](https://store.chipkin.com/articles/bacnet-what-is-the-bacnet-change-of-value-cov) is enabled in
remote `BACnet device `

### Streaming COV

Simple yet flexible distributes a BACnet change of value in `realtime` to any output that you
want: `internal websocket server`, `external websocket server`, `REST API`, `MQTT`, `Kafka` etc...

### BACnet Server

Able start in role `BACnet Server` to allow another `BACnet device` in network can `read/write` any point in its
persistence and from other `protocol` (thanks to [Unified data](#unified-data))

## REST API

Default parameter:

- `_pretty=true`: Pretty `JSON` output

### Explore API

Prefix: `/api/discover/bacnet`

#### Discover Network

- `GET::/discover/bacnet/network`
- `GET::/discover/bacnet/network/:networkId`

#### Discover Device

- `GET::/discover/bacnet/network/:networkId/device`
- `GET::/discover/bacnet/network/:networkId/device/:deviceInstance`

#### Discover Object

- `GET::/discover/bacnet/network/:networkId/device/:deviceInstance/object`
- `GET::/discover/bacnet/network/:networkId/device/:deviceInstance/object/:objectCode`

### Command API

Prefix: `/api/command/bacnet`

#### Read

- `GET::/command/bacnet/network/:networkId/device/:deviceInstance/object/:objectCode/read/priority-array`

#### Write

- `POST::/command/bacnet/network/:networkId/device/:deviceInstance/object/:objectCode/write/point-value`

### Supervisor API

#### Supervisor BACnet COV - Point Value

- `GET::/coordinator/bacnet/network/:networkId/device/:deviceInstance/object/:objectCode/cov`
- `PUT::/coordinator/bacnet/network/:networkId/device/:deviceInstance/object/:objectCode/cov`
- `DELETE::/coordinator/bacnet/network/:networkId/device/:deviceInstance/object/:objectCode/cov`

### Scheduler API

- `POST::/scheduler/:jobKey/`
- `GET::/scheduler/:jobKey/:triggerKey`
- `DELETE::/scheduler/:jobKey/:triggerKey`

## Configuration

TBD
