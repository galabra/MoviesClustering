package MiniProject;

import MiniProject.Algorithm.GraphGenerator;
import MiniProject.Algorithm.VertexChoosingFunc;
import MiniProject.DataStructure.AlgorithmConfig;
import MiniProject.DataStructure.NumArrayList;
import MiniProject.Graph.Cluster;
import MiniProject.Graph.ClustersGroup;
import MiniProject.Graph.Graph;
import MiniProject.Graph.Vertex;
import MiniProject.Utils.Tester;
import MiniProject.Utils.Utils;
import javafx.util.Pair;

import java.util.*;
import java.util.function.Function;

public class CorrelationClustering {

    // set random picking function as a default
    public static <T> ClustersGroup<T> getCorrelationClustering(Graph g) {
        return getCorrelationClustering(g, VertexChoosingFunc.random);
    }

    @SuppressWarnings("unchecked")
    public static <T> ClustersGroup<T> getCorrelationClustering(Graph g, Function chooseVertexFrom) {
        ClustersGroup<T> ans = new ClustersGroup<>();
        HashMap<T, Vertex<T>> verticesToSort = g.duplicateVertices();

        while (verticesToSort.size() > 0) {
            Cluster<T> cluster = new Cluster<>();
            T chosenKey = (T) chooseVertexFrom.apply(verticesToSort);

            Vertex<T> randomVertex = verticesToSort.remove(chosenKey);
            cluster.add(randomVertex);

            for (int i = 0; i < verticesToSort.keySet().toArray().length; i ++) {
                T key = (T) verticesToSort.keySet().toArray()[i];

                if (g.containsEdge(randomVertex, verticesToSort.get(key))) {
                    Vertex<T> newVertex = verticesToSort.remove(key);
                    cluster.add(newVertex);
                }
            }

            ans.add(cluster);
        }

        return ans;
    }

    @SuppressWarnings("unchecked")
    public static ClustersGroup<Integer> getMyClustering(Graph g) {
        boolean shouldCompare = true;
        boolean shouldShuffle = false;
        boolean shouldMinCond = true;

        ClustersGroup<Integer> ans = new ClustersGroup<>();
        ClustersGroup<Integer> initialClusters = initializeUnitClusters(g);
        Comparator<Cluster<Integer>> comparator = (c1, c2) -> c1.cost() > c2.cost() ? 1 : -1;

        while (initialClusters.size() > 0) {
            if (shouldShuffle) initialClusters.shuffle();
            if (shouldCompare) initialClusters.sort(comparator);

            Cluster<Integer> c1 = initialClusters.remove(0);
            double minCost = Double.MAX_VALUE;
            int minCostIndex = -1;

            for (int j = 0; j < initialClusters.size(); j ++) {
                Cluster<Integer> c2 = initialClusters.get(j);
                Cluster<Integer> disj = c1.disj(c2);
                boolean minCond = !shouldMinCond || minCost > disj.cost();

                if (c1.cost() + c2.cost() > disj.cost() && minCond) {
                    minCost = disj.cost();
                    minCostIndex = j;
                    if (!shouldMinCond) break;
                }
            }

            if (minCostIndex > -1) {
                // unite the two clusters c1, c2
                Cluster<Integer> c2 = initialClusters.remove(minCostIndex);
                initialClusters.add( c1.disj(c2) );
            } else {
                ans.add(c1);
            }
        }

        return ans;
    }

    public static ClustersGroup<Integer> initializeUnitClusters(Graph g) {
        HashMap<Integer, Vertex<Integer>> vertices = g.duplicateVertices();
        ClustersGroup<Integer> initialClusters = new ClustersGroup<>();

        for (Vertex<Integer> v : vertices.values()) {
            Cluster<Integer> c = new Cluster<>();
            c.add(v);
            initialClusters.add(c);
        }

        return initialClusters;
    }



    /*
        Here start the testing functions
     */


}
