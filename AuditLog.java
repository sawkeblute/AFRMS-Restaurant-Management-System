public class AuditLog {

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
}