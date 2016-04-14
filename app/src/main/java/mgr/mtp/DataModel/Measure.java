package mgr.mtp.DataModel;

/**
 * Created by lmedrzycki on 14.04.2016.
 */
public class Measure {
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
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



    private String name;
    private String value;
    private int typeId;
    private int groupId;
    private String date;

    public Measure(){

    }

    public Measure(int typeId, String name, String value)
    {
        this.typeId = typeId;
        this.name = name;
        this.value = value;
    }

}
