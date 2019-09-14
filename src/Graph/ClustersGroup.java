package MiniProject.Graph;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ClustersGroup<T> {

    public ArrayList<Cluster<T>> list;

    public ClustersGroup(ArrayList<Cluster<T>> input) {
        this.list = input;
    }

    public ClustersGroup() {
        this.list = new ArrayList<>();
    }

    public Cluster<T> get(int index) {
        return list.get(index);
    }

    public void add(Cluster<T> c) {
        list.add(c);
    }

    public void add(ClustersGroup<T> cg) {
        for (int i = 0; i < cg.size(); i ++) {
            add(cg.get(i));
        }
    }

    public Cluster<T> remove(int index) {
        return list.remove(index);
    }

    public void clear() {
        list.clear();
    }

    public void merge(Pair<Integer, Integer> pair) {
        Cluster<T> first = null;
        Cluster<T> second = null;

        if (pair.getKey() < pair.getValue()) {
            first = list.remove((int) pair.getKey());
            second = list.remove(pair.getValue() - 1);
        }
        else {
            first = list.remove((int) pair.getKey());
            second = list.remove((int) pair.getValue());
        }

        Cluster<T> disj = first.disj(second);
        list.add(disj);
    }

    public int size() {
        return list.size();
    }

    public double cost() {
        double ans = 0.0;

        for (Cluster<T> c : list) {
            ans += c.cost();
        }

        return ans;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (Cluster<T> c : list) {
            sb.append(c + "\n");
        }

        sb.append(this.cost());
        return sb.toString();
    }

    public void shuffle() {
        Collections.shuffle(list);
    }

    public void sort(Comparator<Cluster<T>> comparator) {
        list.sort(comparator);
    }

}
