package MiniProject.Utils;

import MiniProject.Graph.Vertex;
import MiniProject.DataStructure.Movie;

import java.util.ArrayList;
import java.util.HashMap;

public class Utils {

    private static boolean testing = true;

    public static <T> HashMap<T, Vertex<T>> readInput(ArrayList<T> v, ArrayList<T[]> e) {
        HashMap<T, Vertex<T>> ans = new HashMap<>();

        for (T curr : v) {
            Vertex<T> vertex = new Vertex<>((Comparable) curr);
            ans.put(curr, vertex);
        }

        for (T[] edge : e) {
            ans.get(edge[0]).addNeighbor(ans.get(edge[1]));
        }

        return ans;
    }

    public static double twoMoviesP(Movie m1, Movie m2) {
        if (m1.id < m2.id) {
            return m1.watchedBoth.get(m2.id);
        }
        else {
            return m2.watchedBoth.get(m1.id);
        }
    }

    public static void printTest(Object toPrint) {
        if (Utils.testing) System.out.println(Timer.getTimePassed() + "s\t>>\t" + toPrint);
    }

    public static String printAsArray(Object[] arr, boolean shouldPrint, String delimiter) {
        StringBuilder sb = new StringBuilder("[");
        String prefix = "";
        for (Object s : arr) {
            sb.append(prefix);
            prefix = delimiter;
            sb.append(String.valueOf(s));
        }
        sb.append("]");

        if (shouldPrint) System.out.println(sb);

        return sb.toString();
    }

    public static String printAsPartOfArray(Object[] arr, String delimiter) {
        StringBuilder ans = new StringBuilder();
        String prefix = "";

        for (Object item : arr) {
            ans.append(prefix + item);
            prefix = delimiter;
        }

        return ans.toString();
    }

    public static ArrayList<Integer> range(int start, int end, int step) {
        ArrayList<Integer> ans = new ArrayList<>();

        for (int i = start; i <= end; i += step) {
            ans.add(i);
        }

        return ans;
    }

    public static double roundTwoDigits(Number input) {
        return Math.floor(100. * Double.parseDouble("" + input)) / 100.;
    }

    public static String removeParenthesis(String arr) {
        return arr.substring(1, arr.length() - 1);
    }

}
