package club.revived.oculatus.status.model;

import club.revived.oculatus.kvbus.model.Response;
import club.revived.oculatus.service.ServiceStatus;

public record StatusResponse(ServiceStatus status) implements Response {
}
