package club.revived.oculatus.service;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import club.revived.oculatus.game.GameService;
import club.revived.oculatus.game.OnlinePlayer;
import club.revived.oculatus.game.ProxyService;

public final class ServiceFactory {

  private ServiceFactory() {
  }

  @NotNull
  public static Service createService(@NotNull String id,
      @NotNull String ip,
      @NotNull ServiceType type,
      @Nullable List<OnlinePlayer> onlinePlayers) {
    return switch (type) {
      case PROXY -> new ProxyService(id, ip);
      case LOBBY, DUEL, LIMBO, MICROSERVICE -> {
        if (onlinePlayers == null) {
          throw new IllegalArgumentException(
              "Online players list is required for service type: " + type);
        }
        yield new GameService(id, ip, type, onlinePlayers);
      }
      default -> throw new IllegalArgumentException("Unsupported service type: " + type);
    };
  }

  @NotNull
  public static Service createService(@NotNull String id,
      @NotNull String ip,
      @NotNull ServiceType type) {
    return createService(id, ip, type, null);
  }
}
