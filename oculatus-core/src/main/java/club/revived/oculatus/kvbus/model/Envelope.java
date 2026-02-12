package club.revived.oculatus.kvbus.model;

import java.util.UUID;

public record Envelope(
    UUID correlationId,
    String senderId,
    String targetId,
    String payloadType,
    String payloadJson) {
}
