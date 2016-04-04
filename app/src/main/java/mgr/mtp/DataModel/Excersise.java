package mgr.mtp.DataModel;

/**
 * Created by lukas on 04.04.2016.
 */
public class Excersise {

    public Excersise(){

    }

    public Excersise(int id,int weight, int reps, int set)
    {
        this.id = id;
        this.weight = weight;
        this.reps = reps;
        this.set = set;
    }

    private int id;
    private int weight;
    private int reps;
    private int set;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public int getSet() {
        return set;
    }

    public void setSet(int set) {
        this.set = set;
    }
}
