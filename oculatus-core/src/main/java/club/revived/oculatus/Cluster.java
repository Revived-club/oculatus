package club.revived.oculatus;

import org.jetbrains.annotations.NotNull;

import club.revived.oculatus.game.GameService;
import club.revived.oculatus.game.ProxyService;
import club.revived.oculatus.kvbus.providers.broker.MessageBroker;
import club.revived.oculatus.kvbus.providers.cache.DistributedCacheStore;
import club.revived.oculatus.kvbus.pubsub.ServiceMessageBus;
import club.revived.oculatus.service.Service;
import club.revived.oculatus.service.ServiceStatus;
import club.revived.oculatus.service.ServiceType;

import java.net.InetAddress;
import java.util.Comparator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public final class Cluster {

  private static Cluster instance;

  @NotNull
  private final String serviceId;

  @NotNull
  private final ServiceType serviceType;

  @NotNull
  private final MessageBroker broker;
  @NotNull
  private final DistributedCacheStore globalCache;

  @NotNull
  private final ServiceMessageBus messagingService;

  @NotNull
  private final Map<String, Service> services = new ConcurrentHashMap<>();

  @NotNull
  private final String ip;

  public static ServiceStatus STATUS = ServiceStatus.STARTING;

  public Cluster(
      final @NotNull MessageBroker broker,
      final @NotNull DistributedCacheStore cache,
      final @NotNull ServiceType serviceType,
      final @NotNull String id) {
    this.broker = broker;
    this.serviceType = serviceType;
    this.serviceId = id;
    this.ip = this.serviceIp();
    this.messagingService = new ServiceMessageBus(broker, id);
    this.globalCache = cache;

    instance = this;
  }

  public <T> void send(
      final String id,
      final T message) {
    this.broker.publish(id, message);
  }

  @NotNull
  public GameService getLeastLoadedGameServer(final ServiceType serviceType) {
    final var services = this.services.values()
        .stream()
        .filter(clusterService -> clusterService.getType() == serviceType)
        .filter(clusterService -> clusterService instanceof GameService)
        .map(clusterService -> (GameService) clusterService)
        .sorted(Comparator.comparingInt(service -> service.getOnlinePlayers().size()))
        .toList();

    return services.getFirst();
  }

  @NotNull
  private String serviceIp() {
    try {
      final var ip = InetAddress.getLocalHost().getHostAddress();
      final var port = 19132;

      return ip + ":" + port;
    } catch (final Exception e) {
      throw new IllegalStateException("Service failed to get IP");
    }
  }

  public @NotNull String getIp() {
    return ip;
  }

  public @NotNull MessageBroker getBroker() {
    return broker;
  }

  public @NotNull DistributedCacheStore getGlobalCache() {
    return globalCache;
  }

  public @NotNull ServiceMessageBus getMessagingService() {
    return messagingService;
  }

  public @NotNull Map<String, Service> getServices() {
    return services;
  }

  public @NotNull ServiceType getServiceType() {
    return serviceType;
  }

  public @NotNull String getServiceId() {
    return serviceId;
  }

  public static Cluster getInstance() {
    if (instance == null) {
      throw new UnsupportedOperationException("There is no cluster registered!");
    }

    return instance;
  }
}
