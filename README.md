# Remote Folder Change Monitoring System

A Java client-server app monitoring remote folder changes in real time using MVP, Java NIO (server), Java Swing,
threads, and synchronization.

## Demo

A live version of this project is available at:  
[https://www.youtube.com/watch?v=cX7HJ6CrqCc&t=2s](https://www.youtube.com/watch?v=cX7HJ6CrqCc&t=2s)

## Features

- Real-time monitoring: detect file create/modify/delete.
- Non-blocking server (NIO) and TCP client.
- Swing UI displaying events.
- MVP architecture ensures separation of concerns.
- Concurrent handling of multiple clients.
- Configurable monitored directory.
- Maven build producing separate server/client JARs.

## Prerequisites

- JDK 17+ installed
- Maven 3.x on PATH
- Internet for dependencies

## Build

1. Clone repo:
   ```bash
   git clone https://github.com/teri1712/Remote-Folder-Change-Monitoring-System.git
   cd Remote-Folder-Change-Monitoring-System
   ```
2. Configure `pom.xml` with correct package paths for `ClientApp` and `ServerApp`, adjust <mainClass> in pom.xml to
   corresponding App.
3. Run:
   ```bash
   mvn clean package
   ```
   Produces `target/<server>.jar` and `target/<client>.jar`.

## Run

- Server:
  ```bash
  java -jar target/<server>.jar
  ```
- Client:
  ```bash
  java -jar target/<client>.jar
  ```

## License