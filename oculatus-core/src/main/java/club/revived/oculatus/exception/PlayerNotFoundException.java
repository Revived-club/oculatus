package club.revived.oculatus.exception;

public class PlayerNotFoundException extends RuntimeException {

  public PlayerNotFoundException() {
    super("Player not found");
  }
}
