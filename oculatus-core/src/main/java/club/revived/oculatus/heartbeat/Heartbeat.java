package club.revived.oculatus.heartbeat;

import java.util.List;

import club.revived.oculatus.game.OnlinePlayer;
import club.revived.oculatus.service.ServiceType;

public record Heartbeat(
    long timestamp,
    ServiceType serviceType,
    String id,
    int playerCount,
    List<OnlinePlayer> onlinePlayers,
    String serverIp) {
}
