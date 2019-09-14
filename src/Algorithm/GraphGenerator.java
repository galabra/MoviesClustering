package MiniProject.Algorithm;

import MiniProject.DataStructure.Movie;
import MiniProject.Database;
import MiniProject.Graph.Graph;
import MiniProject.Graph.Vertex;
import MiniProject.Utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;

public class GraphGenerator {

    public static Graph<Integer> getRandomGraph(ArrayList<Integer> verticesList) {
        LinkedHashMap<Integer, Vertex<Integer>> vertices = generateEdges(verticesList);
        return new Graph<>(vertices);
    }

    public static Graph<Integer> getRandomGraph(int amountOfMoviesInGraph) {
        return getRandomGraph(amountOfMoviesInGraph, 0);
    }

    public static Graph<Integer> getRandomGraph(int amountOfMoviesInGraph, int testingParameter) {
        ArrayList<Integer> verticesList = Database.printRandomVertices("movies", amountOfMoviesInGraph, "");
        LinkedHashMap<Integer, Vertex<Integer>> vertices = generateEdges(verticesList);
        return new Graph<>(vertices);
    }

    public static LinkedHashMap<Integer, Vertex<Integer>> generateEdges(ArrayList<Integer> verticesList) {
        LinkedHashMap<Integer, Vertex<Integer>> vertices = new LinkedHashMap<>();

        // insert into LinkedHashMap by order
        Collections.sort(verticesList);
        for (Integer id : verticesList) {
            vertices.put(id, new Vertex<>(id));
        }

        for (int j = 0; j < verticesList.size(); j ++) {
            Movie m1 = Database.movies.get(verticesList.get(j));

            for (int t = j + 1; t < verticesList.size(); t++) {
                Movie m2 = Database.movies.get(verticesList.get(t));
                double p_m1_m2 = Utils.twoMoviesP(m1, m2);
                double p_m1 = m1.p();
                double p_m2 = m2.p();

                boolean additionalCondition = true; //haveSameLargestGroup(m1, m2); //haveCommonGenre(m1, m2);

                if (p_m1_m2 >= p_m1 * p_m2 && additionalCondition) {
                    /* equivalent to:
                       cost({m1, m2}) <= cost({m1}, {m2}) */
                    vertices.get(m1.id).addNeighbor(vertices.get(m2.id));
                }
            }
        }

        return vertices;
    }

    public static Graph<Integer>[] generateEdgesInTwoGraphs(int amountOfMoviesInGraph, int testingParameter) {
        LinkedHashMap<Integer, Vertex<Integer>> vertices = new LinkedHashMap<>();
        LinkedHashMap<Integer, Vertex<Integer>> vertices2 = new LinkedHashMap<>();
        ArrayList<Integer> verticesList = Database.printRandomVertices("movies", amountOfMoviesInGraph, "");

        // insert into LinkedHashMap by order
        Collections.sort(verticesList);
        for (Integer id : verticesList) {
            vertices.put(id, new Vertex<>(id));
            vertices2.put(id, new Vertex<>(id));
        }

        for (int j = 0; j < amountOfMoviesInGraph; j ++) {
            Movie m1 = Database.movies.get(verticesList.get(j));

            for (int t = j + 1; t < amountOfMoviesInGraph; t++) {
                Movie m2 = Database.movies.get(verticesList.get(t));
                double p_m1_m2 = Utils.twoMoviesP(m1, m2);
                double p_m1 = m1.p();
                double p_m2 = m2.p();

                boolean additionalCondition = true; //haveSameLargestGroup(m1, m2); //haveCommonGenre(m1, m2);

                if (p_m1_m2 >= p_m1 * p_m2 && additionalCondition) {
                    /* equivalent to:
                       cost({m1, m2}) <= cost({m1}, {m2}) */
                    vertices.get(m1.id).addNeighbor(vertices.get(m2.id));
                }
            }
        }

        // second calc

        for (int j = 0; j < amountOfMoviesInGraph; j ++) {
            Movie m1 = Database.movies.get(verticesList.get(j));

            for (int t = j + 1; t < amountOfMoviesInGraph; t++) {
                Movie m2 = Database.movies.get(verticesList.get(t));
                double p_m1_m2 = Utils.twoMoviesP(m1, m2);
                double p_m1 = m1.p();
                double p_m2 = m2.p();

                boolean additionalCondition = haveSameLargestGroup(m1, m2); //haveCommonGenre(m1, m2);//

                if (p_m1_m2 >= p_m1 * p_m2 && additionalCondition) {
                    /* equivalent to:
                       cost({m1, m2}) <= cost({m1}, {m2}) */
                    vertices2.get(m1.id).addNeighbor(vertices2.get(m2.id));
                }
            }
        }

        Graph<Integer> g1 = new Graph<>(vertices);
        Graph<Integer> g2 = new Graph<>(vertices2);
        return new Graph[] {g1, g2};
    }


    private static boolean haveSameLargestGroup(Movie m1, Movie m2) {
        Object[] arr1 = m1.getUsersGroups();
        Object[] arr2 = m2.getUsersGroups();
        int max1 = -1, max2 = -1;

        for (int i = 0; i < arr1.length; i ++) {
            max1 = (int) Math.max((Double) arr1[i], max1);
            max2 = (int) Math.max((Double) arr2[i], max2);
        }

        return max1 == max2;
    }

    private static boolean haveCommonGenre(Movie m1, Movie m2) {
        for (int genre : m1.genres) {
            if (m2.genres.contains(genre)) {
                return true;
            }
        }

        return false;
    }

}
