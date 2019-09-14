package MiniProject.DataStructure.UserProps;

import MiniProject.Utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;

public class Age {

    private static ArrayList<String> userAgesList = new ArrayList<>(Arrays.asList("1", "18", "25", "35", "45", "50", "56"));
    public int value;

    public Age(String str) {
        this.value = userAgesList.indexOf(str);
    }

    public String toString() {
        return String.valueOf(value);
    }

    public double normalize() {
        return Utils.roundTwoDigits((double)value / 6.0);
    }

}
