package club.revived.oculatus.microservice;

import org.jetbrains.annotations.NotNull;

import club.revived.oculatus.service.Service;
import club.revived.oculatus.service.ServiceType;

public abstract class MicroService extends Service {

  public MicroService(final @NotNull String id) {
    super(id, "localhost", ServiceType.MICROSERVICE);
  }
}
