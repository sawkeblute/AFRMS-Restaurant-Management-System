import java.time.LocalDateTime;

public class AuditLog {
    private final String userId;
    private final String action;
    private final String entityId;
    private final String justification;
    private final LocalDateTime timestamp;

    public AuditLog(String userId, String action, String entityId, String justification) {
        if (userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("User ID is required.");
        }
        if (action == null || action.isBlank()) {
            throw new IllegalArgumentException("Action is required.");
        }

        this.userId = userId;
        this.action = action;
        this.entityId = entityId;
        this.justification = justification;
        this.timestamp = LocalDateTime.now();
    }

    public String toSummary() {
        return timestamp + " | " + userId + " | " + action + " | " + entityId + " | " + justification;
    }
}
