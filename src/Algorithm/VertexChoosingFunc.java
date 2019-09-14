package MiniProject.Algorithm;

import MiniProject.Database;
import MiniProject.Graph.Vertex;
import MiniProject.DataStructure.Movie;
import java.util.HashMap;
import java.util.function.Function;

public class VertexChoosingFunc {

    public static Function random = (map) -> {
        int randomKey = (int) Math.floor(Math.random() * ((HashMap) map).size());
        return (int) ((HashMap) map).keySet().toArray()[randomKey];
    };

    public static Function highest = (map) -> {
        int ans = 0;
        double currMax = 0;
        for (Vertex v : ((HashMap<Integer, Vertex<Integer>>) map).values()) {
            Movie m = Database.movies.get(v.data);
            double currValue = m.p();
            if (currValue > currMax) {
                currMax = currValue;
                ans = m.id;
            }
        }
        return ans;
    };

    public static Function lowest = (map) -> {
        int ans = 0;
        double currMin = Double.MAX_VALUE;
        for (Vertex v : ((HashMap<Integer, Vertex<Integer>>) map).values()) {
            Movie m = Database.movies.get(v.data);
            double currValue = m.p();
            if (currValue < currMin) {
                currMin = currValue;
                ans = m.id;
            }
        }
        return ans;
    };

}
