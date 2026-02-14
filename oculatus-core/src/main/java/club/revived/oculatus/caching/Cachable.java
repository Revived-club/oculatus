package club.revived.oculatus.caching;

import java.lang.reflect.Field;
import java.util.concurrent.CompletableFuture;

import club.revived.commons.orm.annotations.Identifier;
import club.revived.oculatus.Cluster;

public interface Cachable {

  default void cache() {
    CompletableFuture.runAsync(() -> {
      final var clazz = this.getClass();

      Field identifierField = null;

      for (final Field field : clazz.getDeclaredFields()) {
        if (field.isAnnotationPresent(Identifier.class)) {
          identifierField = field;
          break;
        }
      }

      if (identifierField == null) {
        throw new RuntimeException("No field annotated with @Identifier found in " + clazz.getName());
      }

      identifierField.setAccessible(true);

      try {
        final Object key = identifierField.get(this);

        if (key == null) {
          throw new RuntimeException("Identifier field '" + identifierField.getName() + "' is null");
        }

        final var keyStr = key.toString();

        Cluster.getInstance()
            .getGlobalCache()
            .set(keyStr, this);

      } catch (IllegalAccessException e) {
        throw new RuntimeException("Failed to access identifier field: " + identifierField.getName(), e);
      }
    });
  }
}
