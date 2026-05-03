package modules.audit;

import java.time.LocalDateTime;

public class AuditEntry {

    private final String userId;
    private final String action;
    private final String entityId;
    private final String justification;
    private final LocalDateTime timestamp;

    public AuditEntry(String userId, String action, String entityId, String justification) {
        this.userId = userId;
        this.action = action;
        this.entityId = entityId;
        this.justification = justification;
        this.timestamp = LocalDateTime.now();
    }

    public String getUserId() {
        return userId;
    }

    public String getAction() {
        return action;
    }

    public String getEntityId() {
        return entityId;
    }

    public String getJustification() {
        return justification;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
