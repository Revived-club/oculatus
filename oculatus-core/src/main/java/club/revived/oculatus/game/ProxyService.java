package club.revived.oculatus.game;

import org.jetbrains.annotations.NotNull;

import club.revived.oculatus.service.Service;
import club.revived.oculatus.service.ServiceType;

public final class ProxyService extends Service {

  public ProxyService(@NotNull String id, @NotNull String ip) {
    super(id, ip, ServiceType.PROXY);
  }
}
