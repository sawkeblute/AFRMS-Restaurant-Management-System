package modules.auth;

import modules.audit.AuditLog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AuthService {

    private final List<User> users;
    private final Map<String, List<String>> rolePermissions;

    public AuthService() {
        users = new ArrayList<>();
        rolePermissions = new HashMap<>();
        seedUsers();
        seedRolePermissions();
    }

    private void seedUsers() {
        users.add(new User("USR-001", "Owner Demo", "Owner", "owner", "owner"));
        users.add(new User("USR-002", "Manager Demo", "Manager", "manager", "manager"));
        users.add(new User("USR-003", "Cashier Demo", "Cashier", "cashier", "cashier"));
        users.add(new User("USR-004", "Kitchen Demo", "Kitchen Staff", "kitchen", "kitchen"));
        users.add(new User("USR-005", "Inventory Demo", "Inventory Staff", "inventory", "inventory"));
        users.add(new User("USR-006", "Full System Demo", "Demo", "demo", "demo"));
    }

    private void seedRolePermissions() {
        rolePermissions.put("Owner", Arrays.asList("dashboard", "menu", "pos", "orders", "inventory", "reports", "audit", "settings"));
        rolePermissions.put("Manager", Arrays.asList("dashboard", "menu", "orders", "inventory", "reports"));
        rolePermissions.put("Cashier", Arrays.asList("dashboard", "pos", "orders"));
        rolePermissions.put("Kitchen Staff", Arrays.asList("orders"));
        rolePermissions.put("Inventory Staff", Arrays.asList("dashboard", "inventory", "reports"));
        rolePermissions.put("Demo", Arrays.asList("dashboard", "menu", "pos", "orders", "inventory", "reports", "audit", "settings"));
    }

    public User login(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.matchesPassword(password)) {
                AuditLog.logEvent(user.getId(), "LOGIN", user.getId(), "User session started");
                return user;
            }
        }

        AuditLog.logError("Failed login for username: " + username);
        return null;
    }

    public boolean canAccess(User user, String view) {
        if (user == null) return false;
        return rolePermissions.getOrDefault(user.getRole(), new ArrayList<>()).contains(view);
    }

    public List<String> getPermissions(String role) {
        return new ArrayList<>(rolePermissions.getOrDefault(role, new ArrayList<>()));
    }
}
