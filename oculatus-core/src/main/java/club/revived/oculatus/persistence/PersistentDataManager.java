package club.revived.oculatus.persistence;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.jetbrains.annotations.NotNull;

public final class PersistentDataManager {

  private final PersistentDataProvider provider;

  private static PersistentDataManager instance;

  public PersistentDataManager(final PersistentDataProvider provider) {
    this.provider = provider;
  }

  @NotNull
  public <T> CompletableFuture<Optional<T>> get(final Class<T> clazz, final String key) {
    return this.provider.get(clazz, key);
  }

  @NotNull
  public <T> CompletableFuture<Void> save(final Class<T> clazz, final T t) {
    return this.provider.save(clazz, t);
  }

  public static PersistentDataManager getInstance() {
    if (instance == null) {
      throw new IllegalStateException("PersistentDataManager is not initialized");
    }

    return instance;
  }
}
