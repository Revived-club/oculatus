package club.revived.oculatus;

import org.jetbrains.annotations.NotNull;

import club.revived.oculatus.game.GameService;
import club.revived.oculatus.game.ProxyService;
import club.revived.oculatus.kvbus.providers.broker.MessageBroker;
import club.revived.oculatus.kvbus.providers.cache.DistributedCacheStore;
import club.revived.oculatus.kvbus.pubsub.ServiceMessageBus;
import club.revived.oculatus.service.ServiceStatus;
import club.revived.oculatus.service.ServiceType;

import java.net.InetAddress;
import java.util.Comparator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This is an interesting Class
 *
 * @author yyuh
 * @since 03.01.26
 */
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
  private final Map<String, GameService> services = new ConcurrentHashMap<>();

  @NotNull
  private final String ip;

  public static ServiceStatus STATUS = ServiceStatus.STARTING;

  private final ProxyServer proxyServer = ProxyPlugin.getInstance().getServer();

  /**
   * Constructs a Cluster for the given service identifier using
   * ServiceType.UNASSIGNED.
   *
   * @param broker the message broker used for inter-service communication
   * @param cache  the shared global cache instance
   * @param id     the unique identifier for this service instance
   */
  public Cluster(
      final @NotNull MessageBroker broker,
      final @NotNull GlobalCache cache,
      final @NotNull String id) {
    this(broker, cache, ServiceType.UNASSIGNED, id);
  }

  /**
   * Creates a Cluster that manages service discovery, messaging, and basic
   * health/status integration for a single service instance.
   *
   * @param broker      the message broker used for inter-service communication
   * @param cache       the shared global cache instance
   * @param serviceType the type/category of this service
   * @param id          the unique identifier for this cluster/service instance
   */
  public Cluster(
      final @NotNull MessageBroker broker,
      final @NotNull GlobalCache cache,
      final @NotNull ServiceType serviceType,
      final @NotNull String id) {
    this.broker = broker;
    this.serviceType = serviceType;
    this.serviceId = id;
    this.ip = this.serviceIp();
    this.messagingService = new MessagingService(broker, id);
    this.globalCache = cache;

    System.out.println("Setting up cluster...");

    instance = this;

    this.startServices();
    this.registerRequestHandlers();
    this.registerMessageTypes();

    System.out.println("Set up cluster...");
  }

  /**
   * Registers a request handler for WhereIsProxyRequest that answers with this
   * service's id when the requested player is connected to this proxy.
   *
   * <p>
   * When a WhereIsProxyRequest is received, the handler returns a
   * WhereIsProxyResponse containing this serviceId if the proxy has the player,
   * or `null` if the player is not present.
   * </p>
   */
  private void registerRequestHandlers() {
    this.messagingService.registerHandler(WhereIsProxyRequest.class, whereIsProxyRequest -> {
      final var player = this.proxyServer.getPlayer(whereIsProxyRequest.uuid()).orElse(null);

      if (player == null) {
        return null;
      }

      return new WhereIsProxyResponse(this.serviceId);
    });
  }

  /**
   * Publish a message to the specified cluster destination via the message
   * broker.
   *
   * @param id      the destination service identifier or topic to publish to
   * @param message the message payload to send
   */
  public <T> void send(
      final String id,
      final T message) {
    this.broker.publish(id, message);
  }

  /**
   * Selects the least-loaded cluster service of the given service type.
   *
   * @param serviceType the service type to search for
   * @return the `ClusterService` of the specified type that has the fewest online
   *         players
   */
  @NotNull
  public GameService getLeastLoadedService(final ServiceType serviceType) {
    final var services = this.services.values()
        .stream()
        .filter(clusterService -> clusterService.getType() == serviceType)
        .sorted(Comparator.comparingInt(service -> service.getOnlinePlayers().size()))
        .toList();

    return services.getFirst();
  }

  /**
   * Locate the ClusterService hosting the proxy associated with the given UUID.
   *
   * @param uuid the player's or proxy's UUID used to identify which proxy to
   *             locate
   * @return the ClusterService hosting the proxy for the given UUID, or `null` if
   *         no matching service is known
   */
  @NotNull
  public CompletableFuture<ProxyService> whereIsProxy(final UUID uuid) {
    return this.messagingService.sendRequest("global", new WhereIsProxyRequest(uuid), WhereIsProxyResponse.class)
        .thenApply(whereIsResponse -> {
          final var id = whereIsResponse.proxy();

          return this.services.get(id);
        });
  }

  /**
   * Finds the cluster service hosting the server for the given player UUID.
   *
   * @param uuid the player's UUID to locate
   * @return the ClusterService hosting that player's server, or null if unknown
   */
  @NotNull
  public CompletableFuture<ClusterService> whereIs(final UUID uuid) {
    return this.messagingService.sendRequest("global", new WhereIsRequest(uuid), WhereIsResponse.class)
        .thenApply(whereIsResponse -> {
          final var id = whereIsResponse.server();

          return this.services.get(id);
        });
  }

  /**
   * Produce the local host address combined with the default service port.
   *
   * @return the local host IP and port formatted as "ip:19132"
   * @throws IllegalStateException if the local host address cannot be determined
   */
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

  /**
   * Provide the cluster's resolved network address as "host:port".
   *
   * @return the service IP and port string in the form "host:port"
   */
  public @NotNull String getIp() {
    return ip;
  }

  /**
   * Retrieve the message broker used for inter-service communication.
   *
   * @return the MessageBroker instance for this cluster
   */
  public @NotNull MessageBroker getBroker() {
    return broker;
  }

  /**
   * Accesses the shared GlobalCache for this cluster.
   *
   * @return the GlobalCache instance used by this cluster
   */
  public @NotNull DistributedCacheStore getGlobalCache() {
    return globalCache;
  }

  /**
   * Provides the MessagingService used by this cluster for inter-service
   * communication.
   *
   * @return the cluster's MessagingService instance
   */
  public @NotNull ServiceMessageBus getMessagingService() {
    return messagingService;
  }

  /**
   * The cluster's known services mapped by service id.
   *
   * @return a thread-safe map from service id to corresponding
   *         {@link ClusterService} reflecting current cluster membership
   */
  public @NotNull Map<String, GameService> getServices() {
    return services;
  }

  /**
   * Retrieves the service type assigned to this cluster.
   *
   * @return the ServiceType of this cluster
   */
  public @NotNull ServiceType getServiceType() {
    return serviceType;
  }

  /**
   * Gets the identifier for this cluster service.
   *
   * @return the cluster's service identifier
   */
  public @NotNull String getServiceId() {
    return serviceId;
  }

  /**
   * Get the registered Cluster singleton.
   *
   * @return the registered Cluster instance
   * @throws UnsupportedOperationException if no Cluster instance is registered
   */
  public static Cluster getInstance() {
    if (instance == null) {
      throw new UnsupportedOperationException("There is no cluster registered!");
    }

    return instance;
  }
}
