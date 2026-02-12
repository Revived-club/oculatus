package club.revived.oculatus.heartbeat;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import club.revived.oculatus.Cluster;
import club.revived.oculatus.game.GameService;
import club.revived.oculatus.kvbus.providers.broker.MessageBroker;
import club.revived.oculatus.kvbus.pubsub.MessageHandler;

public abstract class HeartbeatService implements MessageHandler<Heartbeat> {

  private final static long INTERVAL = 1000;
  private final static long TIMEOUT = 5000;

  private final Map<String, Long> lastSeen = new ConcurrentHashMap<>();
  private final ScheduledExecutorService subServer = Executors.newScheduledThreadPool(1);
  private final MessageBroker broker;

  private final Cluster cluster = Cluster.getInstance();

  public HeartbeatService(final MessageBroker broker) {
    this.broker = broker;
  }

  public abstract ScheduledFuture<?> startTask();

  @Override
  public void handle(final Heartbeat message) {
    final var service = new GameService(message.id(), message.serverIp(), message.serviceType(),
        message.onlinePlayers());

    final var now = System.currentTimeMillis();

    this.lastSeen.put(message.id(), now);
    Cluster.getInstance().getServices().put(message.id(), service);

  }
}
