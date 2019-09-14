package MiniProject.Graph;

import MiniProject.Database;
import MiniProject.DataStructure.Movie;
import MiniProject.Utils.Utils;

import java.util.ArrayList;

public class Cluster<T> {

    public ArrayList<Vertex<T>> content;

    public Cluster() {
        this.content = new ArrayList<>();
    }

    public void add(Vertex<T> v) {
        content.add(v);
    }

    public int size() {
        return content.size();
    }

    @SuppressWarnings("unused")
    public String toString() {
        String ans = "{ }";

        if (content.size() > 0) {
            //boolean cond = isThisMovieCluster();
            //ans = printArrayList(cond);
            ans = printFinalFormat();
        }

        /*
        NumArrayList arr = new NumArrayList();
        for (Vertex<T> v : content) {
            arr.add((Database.users.get(v.data)).age.value);
        }
        ans = arr.toString();
        */
        return ans;
    }

    private String printFinalFormat() {
        StringBuilder s = new StringBuilder();
        String prefix = "";
        for (Vertex<T> v : content) {
            Movie m = Database.movies.get(v.data);
            s.append(prefix + m.id + " " + m.name);
            prefix = ", ";
        }
        return s.toString();
    }

    private String printArrayList(boolean isMovie) {
        StringBuilder s = new StringBuilder("{ ");
        String prefix = "";
        for (Vertex<T> v : content) {
            Object toPrint = isMovie ?
                    Database.movies.get(v.data) :
                    v.toString();
            //s.append(prefix + toPrint);
            s.append(prefix + Database.users.get(v.data));

            //prefix = ",\t";
            prefix = "\n";
        }
        s.append(" }");
        return s.toString();
    }

    public String printUsersWithFirstColumn(Object firstColumn) {
        StringBuilder s = new StringBuilder();
        String prefix = "";
        for (Vertex<T> v : content) {
            s.append(prefix + firstColumn.toString() + ", " + Utils.removeParenthesis(Database.users.get(v.data).toString()));
            prefix = "\n";
        }
        return s.toString();
    }

    @SuppressWarnings("unused")
    public double cost() {
        int size = this.content.size();
        double ans = 0;

        if (size == 1) {
            Movie m = getMovieByClusterIndex(0);
            ans = Math.log(1 / m.watchedBoth.get(m.id));
        }
        else {
            for (int i = 0; i < size; i ++) {
                for (int j = i + 1; j < size; j ++) {
                    Movie m1 = getMovieByClusterIndex(i);
                    Movie m2 = getMovieByClusterIndex(j);
                    double tmp = (1.0 / ((double)size - 1.0)) * Math.log(1.0 / Utils.twoMoviesP(m1, m2));
                    ans += tmp;
                }
            }
        }

        return ans;
    }

    private Movie getMovieByClusterIndex(int id) {
        return Database.movies.get(this.content.get(id).data);
    }

    @SuppressWarnings("unused")
    private boolean isThisMovieCluster() {
        try {
            Movie test = (Movie) content.get(0).data;
            return true;
        }
        catch (ClassCastException e) {
            return false;
        }
    }

    public Cluster<T> disj(Cluster<T> other) {
        Cluster<T> ans = new Cluster<>();

        for (Vertex<T> v : this.content) {
            ans.add(v);
        }
        for (Vertex<T> v : other.content) {
            ans.add(v);
        }

        return ans;
    }

}
