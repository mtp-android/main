package mgr.mtp.DataModel;

/**
 * Created by lukas on 19.03.2016.
 */
public class Food {

    private int id;
    private int meal;
    private String name;
    private int amount;
    private String unit;

    public Food(int id, int meal, String name, int amount, String unit)
    {
        this.id = id;
        this.meal = meal;
        this.name = name;
        this.amount = amount;
        this.unit = unit;
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
}
