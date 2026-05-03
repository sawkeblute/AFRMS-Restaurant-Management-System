package modules.menu;

import java.util.ArrayList;
import java.util.List;

public class MenuItem {

    private final int id;
    private final String name;
    private final String thaiName;
    private final String category;
    private final double price;
    private final double cost;
    private final String imagePath;
    private final List<RecipeIngredient> recipe;

    public MenuItem(int id, String name, String thaiName, String category, double price, double cost, String imagePath) {
        this.id = id;
        this.name = name;
        this.thaiName = thaiName;
        this.category = category;
        this.price = price;
        this.cost = cost;
        this.imagePath = imagePath;
        this.recipe = new ArrayList<>();
    }

    public void addRecipeIngredient(String ingredientId, double amount) {
        recipe.add(new RecipeIngredient(ingredientId, amount));
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getThaiName() {
        return thaiName;
    }

    public String getCategory() {
        return category;
    }

    public double getPrice() {
        return price;
    }

    public double getCost() {
        return cost;
    }

    public String getImagePath() {
        return imagePath;
    }

    public List<RecipeIngredient> getRecipe() {
        return new ArrayList<>(recipe);
    }
}
