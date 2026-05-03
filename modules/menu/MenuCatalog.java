package modules.menu;

import java.util.ArrayList;
import java.util.List;

public class MenuCatalog {

    private final List<MenuItem> items;

    public MenuCatalog() {
        items = new ArrayList<>();
        seedMenu();
    }

    private void seedMenu() {
        MenuItem basilPork = new MenuItem(42, "Spicy Basil Pork with Rice", "ผัดกะเพราหมู", "Basil Stir-Fry", 45, 20, "assets/basil-pork-rice.jpg");
        basilPork.addRecipeIngredient("ING-001", 0.18);
        basilPork.addRecipeIngredient("ING-002", 0.13);
        basilPork.addRecipeIngredient("ING-010", 0.25);
        basilPork.addRecipeIngredient("ING-011", 0.06);
        items.add(basilPork);

        MenuItem friedEgg = new MenuItem(49, "Fried Egg", "ไข่ดาว", "Egg & Omelet", 15, 5, "assets/fried-egg.jpg");
        friedEgg.addRecipeIngredient("ING-009", 1);
        items.add(friedEgg);

        MenuItem seafoodRice = new MenuItem(11, "Fried Rice with Seafood", "ข้าวผัดทะเล", "Fried Rice", 55, 30, "assets/fried-rice-seafood.jpg");
        seafoodRice.addRecipeIngredient("ING-001", 0.18);
        seafoodRice.addRecipeIngredient("ING-005", 0.14);
        seafoodRice.addRecipeIngredient("ING-011", 0.06);
        items.add(seafoodRice);

        MenuItem thaiTea = new MenuItem(68, "Thai Tea", "ชาไทยเย็น", "Beverages", 20, 8, "assets/thai-tea.jpg");
        thaiTea.addRecipeIngredient("ING-023", 0.03);
        thaiTea.addRecipeIngredient("ING-024", 0.05);
        thaiTea.addRecipeIngredient("ING-025", 1);
        items.add(thaiTea);
    }

    public MenuItem findById(int id) {
        for (MenuItem item : items) {
            if (item.getId() == id) return item;
        }
        return null;
    }

    public List<MenuItem> getItems() {
        return new ArrayList<>(items);
    }

    public List<MenuItem> findByCategory(String category) {
        List<MenuItem> results = new ArrayList<>();
        for (MenuItem item : items) {
            if (item.getCategory().equalsIgnoreCase(category)) {
                results.add(item);
            }
        }
        return results;
    }
}
