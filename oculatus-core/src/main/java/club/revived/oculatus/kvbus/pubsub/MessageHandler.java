package club.revived.oculatus.kvbus.pubsub;

@FunctionalInterface
public interface MessageHandler<T> {

  void handle(final T message);
}
