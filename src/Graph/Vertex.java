package MiniProject.Graph;

import java.util.ArrayList;

public class Vertex<T> {

    public Comparable data;
    private ArrayList<Vertex<T>> neighbors = new ArrayList<>();

    public Vertex(Comparable data) {
        this.data = data;
    }

    public void addNeighbor(Vertex v) {
        if (data.compareTo(v.data) < 0) {
            if (!neighbors.contains(v)) {
                neighbors.add(v);
            }
        }
        else {
            if (!v.isNeighbor(this)) {
                v.addNeighbor(this);
            }
        }
    }

    public boolean isNeighbor(Vertex v) {
        return neighbors.contains(v);
    }

    public String toString() {
        return data.toString();
    }

}
