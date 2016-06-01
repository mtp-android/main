package mgr.mtp.DataModel;

/**
 * Created by lmedrzycki on 14.04.2016.
 */
public class Measure {
    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }


    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    private String name;
    private int value;
    private int typeId;
    private int groupId;
    private String date;
    private String unit;

    public Measure(){

    }

    public Measure(int typeId, String name, int value)
    {
        this.typeId = typeId;
        this.name = name;
        this.value = value;
    }

}
