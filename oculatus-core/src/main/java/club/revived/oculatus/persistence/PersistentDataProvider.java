package club.revived.oculatus.persistence;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface PersistentDataProvider {

  <T> CompletableFuture<Optional<T>> get(final Class<T> clazz, final String key);

  <T> CompletableFuture<Void> save(final Class<T> clazz, final T t);

}
