package mgr.mtp.DataModel;

/**
 * Created by lukas on 04.04.2016.
 */
public class ExerciseSet {

    public ExerciseSet() {

    }

    public ExerciseSet(int id, int weight, int reps, int setNo) {
        this.id = id;
        this.weight = weight;
        this.reps = reps;
        this.setNo = setNo;
    }

    private int id;
    private int exerciseId;
    private String exerciseName;
    private int weight;
    private int reps;
    private int setNo;

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

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

    public int getSetNo() {
        return setNo;
    }

    public void setSetNo(int set) {
        this.setNo = setNo;
    }

    public int getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(int exerciseId) {
        this.exerciseId = exerciseId;
    }
}
