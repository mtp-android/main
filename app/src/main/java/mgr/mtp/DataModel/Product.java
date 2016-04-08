package mgr.mtp.DataModel;

/**
 * Created by lukas on 19.03.2016.
 */
public class Product {

    private int id;
    private int meal;
    private String name;
    private int amount;
    private String unit;
    private float proteins;
    private float carbohydrates;
    private float fat;
    private float calories;
    private float factor;

    public Product(int id, int meal, String name, int amount, String unit,
                   float proteins, float carbohydrates, float fat, float calories, float factor)
    {
        this.id = id;
        this.meal = meal;
        this.name = name;
        this.amount = amount;
        this.unit = unit;
        this.calories = calories;
        this.fat = fat;
        this.carbohydrates = carbohydrates;
        this.proteins = proteins;
        this.factor = factor;
    }

    public Product()
    {

    }

    public float getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public float getCarbohydrates() {
        return carbohydrates;
    }

    public void setCarbohydrates(int carbohydrates) {
        this.carbohydrates = carbohydrates;
    }

    public float getFat() {
        return fat;
    }

    public void setFat(int fat) {
        this.fat = fat;
    }

    public float getProteins() {
        return proteins;
    }

    public void setProteins(int proteins) {
        this.proteins = proteins;
    }

    public int getAmount() {
        return amount;
    }

    public int getId() {
        return id;
    }

    public int getMeal() {
        return meal;
    }

    public String getName() {
        return name;
    }

    public String getUnit() {
        return unit;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMeal(int meal) {
        this.meal = meal;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getFactor() {
        return factor;
    }

    public void setFactor(int factor) {
        this.factor = factor;
    }
}
