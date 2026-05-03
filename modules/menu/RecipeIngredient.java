package modules.menu;

public class RecipeIngredient {

    private final String ingredientId;
    private final double amount;

    public RecipeIngredient(String ingredientId, double amount) {
        this.ingredientId = ingredientId;
        this.amount = amount;
    }

    public String getIngredientId() {
        return ingredientId;
    }

    public double getAmount() {
        return amount;
    }
}
