package club.revived.oculatus.game;

import java.util.UUID;

public record OnlinePlayer(UUID uuid, String username, String server, String skin, String signature, int ping) {
}
