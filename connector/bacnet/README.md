# BACnet Connector

`BACnet` is a communication protocol for Building Automation and Control (`BAC`) networks that leverage the `ASHRAE`
, `ANSI`, and `ISO 16484-5` standard protocol.

## Terms

### BACnet Router

- `BACnet Router` is simply devices that connect multiple networks together, it transmits messages between **BACnet
  networks**. For example: It might connect a `BACnet/IP` system to an `MS/TP` system.
- In BACnet, a router is a device that passes a message from one network to another without changing the form or content
  of the message. However, if the **BACnet networks** are of different types, the addresses, error checking, in short,
  the **packaging** of the message may get changed

### BACnet Gateway

- `Bacnet Gateway` is **translator** between other network protocol with `BACnet network`, it must translate
  between `BACnet` concepts and the equivalent ideas in the non-BACnet system

### BACnet client

### BACnet server

References:

- [Specifying Gateway and Routers](https://polarsoft.com/Specifying%20Gateways%20and%20Routers.pdf)

### How to use

References [HOWTO](README.md)
