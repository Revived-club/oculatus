package club.revived.oculatus.status;

import club.revived.oculatus.Cluster;
import club.revived.oculatus.kvbus.pubsub.ServiceMessageBus;
import club.revived.oculatus.status.model.StatusRequest;
import club.revived.oculatus.status.model.StatusResponse;

public final class StatusService {

  public StatusService(final ServiceMessageBus messageBus) {
    messageBus.registerHandler(StatusRequest.class, statusRequest -> {
      final var status = Cluster.getInstance().getStatus();

      return new StatusResponse(status);
    });
  }
}
