package MiniProject.DataStructure.UserProps;

public class Gender {

    public String asString;
    public String full;
    public boolean asBoolean;   // F = male, T = female
    public int asInt;           // 0 = male, 1 = female

    public Gender(String str) {
        this.asString = str;
        this.asBoolean = !str.equals("M");
        this.asInt = asBoolean ? 1 : 0;
        this.full = str.equals("M") ? "Male" : "Female";
    }

    public String toString() {
        return String.valueOf(asInt);
    }

}
