package MiniProject.Graph;

import MiniProject.Utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Graph<T> {

    public HashMap<T, Vertex<T>> vertices;

    public Graph(HashMap<T, Vertex<T>> v) {
        this.vertices = v;
    }

    public Graph(ArrayList<T> v, ArrayList<T[]> e) {
        this.vertices = Utils.readInput(v, e);
    }

    public boolean containsVertex(Vertex<T> v) {
        return vertices.containsValue(v);
    }

    public boolean containsEdge(Vertex<T> v1, Vertex<T> v2) {
        return v1.isNeighbor(v2);
    }

    public HashMap<T, Vertex<T>> duplicateVertices() {
        HashMap<T, Vertex<T>> ans = new HashMap<>();
        for (Map.Entry<T, Vertex<T>> entry : vertices.entrySet()) {
            ans.put(entry.getKey(), entry.getValue());
        }
        return ans;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        String prefix = "";
        for (T key : vertices.keySet()) {
            sb.append(prefix + key);
            prefix = ", ";
        }
        return sb.toString();
    }

}
