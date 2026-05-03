package modules.audit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AuditLog {

    private static final List<AuditEntry> entries = new ArrayList<>();

    // Log normal system actions
    public static void log(String message) {
        System.out.println("[LOG] " + message);
    }

    // Log errors
    public static void logError(String message) {
        System.out.println("[ERROR] " + message);
    }

    // Optional: log warnings
    public static void logWarning(String message) {
        System.out.println("[WARNING] " + message);
    }

    // Optional: log user actions (useful for your system)
    public static void logUserAction(String user, String action) {
        System.out.println("[USER] " + user + " -> " + action);
    }

    public static void logEvent(String userId, String action, String entityId, String justification) {
        entries.add(0, new AuditEntry(userId, action, entityId, justification));
        log(action + " | " + entityId + " | " + justification);
    }

    public static List<AuditEntry> getEntries() {
        return Collections.unmodifiableList(entries);
    }

    public static void clear() {
        entries.clear();
    }
}
