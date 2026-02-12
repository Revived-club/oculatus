# Oculatus

A modern, cross-platform RPC (Remote Procedure Call) and networking library for Minecraft servers.

## Overview

Oculatus (Latin: "sharp-eyed") is a lightweight Java library that provides a unified networking layer and RPC framework for Minecraft server infrastructure.

## Features

- **Cross-Platform RPC**: Type-safe remote procedure calls between servers and proxies
- **Multiple Backends**: Support for Redis Pub/Sub and message brokers
- **Bidirectional Communication**: Request/response and publish/subscribe patterns
- **Heartbeat System**: Built-in service discovery and health monitoring
- **Game Service Management**: Track and manage game servers, players, and proxies
- **Microservice Architecture**: Designed for distributed Minecraft server networks
- **Easy Integration**: Simple setup for both Bukkit and Velocity platforms

## Architecture

```
oculatus/
├── oculatus-core/           # Core RPC framework and networking
│   ├── game/               # Game service management
│   ├── heartbeat/          # Service heartbeat and discovery
│   ├── kvbus/              # Key-value message bus
│   ├── providers/          # Backend implementations (Redis, etc.)
│   ├── pubsub/             # Pub/sub messaging
│   ├── cache/              # Distributed caching
│   ├── microservice/       # Microservice utilities
│   └── service/            # Service abstraction layer
├── oculatus-bukkit/        # Bukkit/Spigot/Paper integration
└── oculatus-velocity/      # Velocity proxy integration
```

## Installation

### Maven

```xml
<repositories>
    <repository>
        <id>revived-releases</id>
        <url>https://mvn.revived.club/releases</url>
    </repository>
</repositories>

<dependencies>
    <!-- Core library -->
    <dependency>
        <groupId>club.revived.oculatus</groupId>
        <artifactId>oculatus-core</artifactId>
        <version>VERSION</version>
    </dependency>
    
    <!-- For Bukkit/Spigot/Paper servers -->
    <dependency>
        <groupId>club.revived.oculatus</groupId>
        <artifactId>oculatus-bukkit</artifactId>
        <version>VERSION</version>
    </dependency>
    
    <!-- For Velocity proxies -->
    <dependency>
        <groupId>club.revived.oculatus</groupId>
        <artifactId>oculatus-velocity</artifactId>
        <version>VERSION</version>
    </dependency>
</dependencies>
```

### Gradle (Kotlin DSL)

```kotlin

maven {
    name = "revived-releases"
    url = uri("https://mvn.revived.club/releases")
}


dependencies {
    // Core library
    implementation("club.revived.oculatus:oculatus-core:VERSION")
    
    // For Bukkit servers
    implementation("club.revived.oculatus:oculatus-bukkit:VERSION")
    
    // For Velocity proxies
    implementation("club.revived.oculatus:oculatus-velocity:VERSION")
}
```

## Use Cases

### Multi-Server Game Networks

- **Game Mode Management**: Route players between different game servers
- **Cross-Server Chat**: Enable global chat across your network
- **Player Synchronization**: Share player data between servers
- **Load Balancing**: Distribute players based on server load

### Proxy-Server Communication

- **Server Status**: Report server health to the proxy
- **Player Tracking**: Monitor players across all backend servers
- **Dynamic Routing**: Route players based on game state
- **Maintenance Mode**: Coordinate maintenance across servers

### Microservices Architecture

- **Service Discovery**: Automatically detect available services
- **RPC Communication**: Call methods on remote services
- **Event Broadcasting**: Notify all services of important events
- **Distributed State**: Share state across service instances

## Building from Source

Requirements:

- Java 25 or higher
- Gradle 9.2 or higher

```bash
# Clone the repository
git clone https://github.com/Revived-club/oculatus.git
cd oculatus

# Build all modules
./gradlew build

```

## Development Status

⚠️ **Work in Progress** - This project is currently under active development. APIs may change without notice until the first stable release.

## Requirements

- **Java**: 25 or higher
- **Bukkit/Spigot/Paper**: 1.21.10 or higher
- **Velocity**: 3.0.0 or higher
- **Redis**

## Contributing

We welcome contributions! Please follow these guidelines:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the terms specified in the repository. Please check the LICENSE file for details.
