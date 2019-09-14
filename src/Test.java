package MiniProject;

import MiniProject.Algorithm.GraphGenerator;
import MiniProject.Algorithm.VertexChoosingFunc;
import MiniProject.DataStructure.AlgorithmConfig;
import MiniProject.DataStructure.Movie;
import MiniProject.DataStructure.NumArrayList;
import MiniProject.Graph.Cluster;
import MiniProject.Graph.ClustersGroup;
import MiniProject.Graph.Graph;
import MiniProject.Utils.Tester;
import MiniProject.Utils.Timer;
import MiniProject.Utils.Utils;
import javafx.util.Pair;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class Test {

    public static void main(String[] args) {
        Timer.start();
        Reader.readDatabase();

        int todo = 0;

        if (todo == 0) {
            run();
            //Writer.writeMoviesToParsedDB("parsedDB");
        }
        else if (todo == 1) {
            HashMap<Integer, Integer> ans = new HashMap<>();
            for (int i = 0; i < Database.genresList.size(); i ++) {
                ans.put(i, 0);
            }

            double totalGenres = 0.0;
            for (Movie m : Database.movies.values()) {
                int currMovieAmountOfGenres = m.genres.size();
                for (Integer genreId : m.genres) {
                    totalGenres ++;
                    Integer genreAmount = ans.get(genreId);
                    ans.put(genreId, genreAmount + 1);
                }
            }

            for (Map.Entry<Integer, Integer> entry : ans.entrySet()) {
                System.out.println(entry.getKey() + ":\t" + entry.getValue() / totalGenres);
            }
        }
        else if (todo == 2) {
            task4();
        }
        else if (todo == 3) {
            int maxDiff = 20;
            int yearDifference;

            for (int repetitions = 0; repetitions < 10000000; repetitions ++) {
                if (repetitions > 0 && repetitions % 10000 == 0) {
                    Utils.printTest(repetitions);
                }

                boolean keepLooping = true;
                Graph graph = GraphGenerator.getRandomGraph(20);

                //ClustersGroup<Integer> ans = CorrelationClustering.getCorrelationClustering(graph);
                ClustersGroup<Integer> ans = CorrelationClustering.getMyClustering(graph);

                for (Cluster<Integer> c : ans.list) {
                    for (int i = 0; keepLooping && i < c.content.size(); i ++) {
                        for (int j = i + 1; keepLooping && j < c.content.size(); j ++) {
                            Movie m1 = Database.movies.get(c.content.get(i).data);
                            Movie m2 = Database.movies.get(c.content.get(j).data);
                            yearDifference = Math.abs(m1.year - m2.year);
                            keepLooping = yearDifference >= maxDiff;
                        }
                    }

                    if (!keepLooping) {
                        break;
                    }
                }

                if (keepLooping) {
                    Utils.printTest("repetition #" + repetitions + " passed with " + maxDiff + " years difference!");
                    System.out.println(ans);
                }
            }
            //printSpecificMovies(moviesSet);
        }


        Timer.stop();
    }


    public static int clustersGroupSize(ArrayList<Cluster<Integer>> group) {
        int ans = 0;
        for (Cluster<Integer> c : group) {
            ans += c.size();
        }
        return ans;
    }

    private static void printSpecificMovies(int[] movies) {
        for (int id : movies) {
            Movie m = Database.movies.get(id);
            System.out.println(m);
        }
    }

    private static void task4() {
        System.out.println("Set ID, Improved algorithm cost, Original algorithm cost, Original algorithm average cost");

        for (int setId = 1; setId <= 20; setId ++) {
            Graph<Integer> graph = GraphGenerator.getRandomGraph(100);

            try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("subset" + setId + ".txt")))) {
                for (Integer movieId : graph.vertices.keySet()) {
                    out.println(movieId);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            ClustersGroup<Integer> improvedAlgorithmAns = CorrelationClustering.getMyClustering(graph);
            ClustersGroup<Integer> originalAlgorithmAns = CorrelationClustering.getCorrelationClustering(graph);
            System.out.print(setId + ",\t");
            System.out.print(Utils.roundTwoDigits(improvedAlgorithmAns.cost()) + ",\t");
            System.out.print(Utils.roundTwoDigits(originalAlgorithmAns.cost()) + ",\t");

            NumArrayList originalAlgorithmResults = new NumArrayList();
            double currCost;
            for (int i = 0; i < 10000; i++) {
                originalAlgorithmAns = CorrelationClustering.getCorrelationClustering(graph);
                currCost = originalAlgorithmAns.cost();
                originalAlgorithmResults.addDouble(currCost);
            }
            double mean = originalAlgorithmResults.mean();
            System.out.print(Utils.roundTwoDigits(mean) + "\n");
        }
    }


    public static void run() {
        AlgorithmConfig random  = new AlgorithmConfig("random", VertexChoosingFunc.random, 10000000, 20);
        AlgorithmConfig highest = new AlgorithmConfig("highest", VertexChoosingFunc.highest, 1000, 100);
        AlgorithmConfig lowest  = new AlgorithmConfig("lowest", VertexChoosingFunc.lowest, 1000, 100);

        for (Integer yearsSpan : Utils.range(20, 20, 10)) {
            runNtimes(random, yearsSpan);
            //runNtimes(highest, yearsSpan);
            //runNtimes(lowest, yearsSpan);
        }
    }

    /*
        This was my main testing function, as it allows to run multiple independent iterations of both algorithmsץ
        For this reason it contains many "pieces" of different tests.
     */
    private static void runNtimes(AlgorithmConfig config, int testingParameter) {
        HashMap<Double, Integer> amountOfCosts = new HashMap<>();
        HashMap<Double, Integer> amountOfCosts_original = new HashMap<>();
        NumArrayList amountOfClusters = new NumArrayList();
        ClustersGroup<Integer> minAns = new ClustersGroup<>();
        ClustersGroup<Integer> minOriginalAns = new ClustersGroup<>();
        double minAnsValue = Double.MAX_VALUE;
        double minOriginalAnsValue = Double.MAX_VALUE;
        double maxAnsValue = 0;
        double maxOriginalAnsValue = 0;

        double average = 0;
        double originalAverage = 0;

        double diff = 0.0;

        ArrayList<Integer> currentImprovementRateClustering = null;
        ArrayList<Double> results = new ArrayList<>();

        for (int i = 0; i < config.repetitions; i++) {
            // Periodically print the testing progress
            if (config.repetitions > 1000000 && i % 10000 == 0 && i > 0) {
                Utils.printTest(i);
                //Utils.printTest("diff: " + diff + "\n>>\toriginal:\n" + minOriginalAns + "\n>>\timproved:\n" + minAns + "\n\n");
            }

            Integer[] moviesArray = {260, 3052, 3893, 2662, 3100, 2494, 1221, 1252, 2694, 3593, 1233, 3152, 1623, 771, 3396, 2791, 2788, 2875, 539, 339};
            ArrayList<Integer> moviesList = new ArrayList<>(Arrays.asList(moviesArray));
            Graph graph = GraphGenerator.getRandomGraph(moviesList);
            //Graph graph = GraphGenerator.getRandomGraph(config.graphSize, testingParameter);
            double totalCost = 0;
            double originalTotalCost = 0;

            ClustersGroup<Integer> ans = CorrelationClustering.getMyClustering(graph);
            totalCost = ans.cost();
            average += totalCost;
            Double rounded = Math.round(totalCost * 10) / 10.0;
            int count = amountOfCosts.containsKey(rounded) ? amountOfCosts.get(rounded) : 0;
            amountOfCosts.put(rounded, count + 1);

            for (int j = 0; j < 1; j ++) {
                ClustersGroup<Integer> originalAns = CorrelationClustering.getCorrelationClustering(graph, VertexChoosingFunc.random);
                originalTotalCost = originalAns.cost();

                rounded = Math.round(originalTotalCost * 10) / 10.0;
                count = amountOfCosts_original.containsKey(rounded) ? amountOfCosts_original.get(rounded) : 0;
                amountOfCosts_original.put(rounded, count + 1);

                originalAverage += originalTotalCost;

                if (totalCost < minAnsValue) {
                    minAns = ans;
                    minOriginalAns = originalAns;
                    minAnsValue = totalCost;
                }
            }

            /*
            double currentImprovementRate = (1 - (average / originalAverage)) * 100;
            if (currentImprovementRate > bestImprovementRate) {
                bestImprovementRate = currentImprovementRate;
                currentImprovementRateClustering = Parser.currentVertices;
            }
            */

        }

        Utils.printTest(config.name + "\t(" + testingParameter + ")\tThe average cost is: \t" + (average / (1 * config.repetitions)));
        Utils.printTest(config.name + "\t(" + testingParameter + ")\tThe minimal cost is: \t" + minAnsValue);
        Utils.printTest(config.name + "\t(" + testingParameter + ")\tThe average amount of clusters is: " + amountOfClusters);

        Utils.printTest(config.name + "\t(" + testingParameter + ")\tThe original average cost is: \t" + (originalAverage / (1 * config.repetitions)));
        Utils.printTest(config.name + "\t(" + testingParameter + ")\tThe original minimal cost is: \t" + minOriginalAnsValue);


        Utils.printTest(config.name + "\t(" + testingParameter + ")\tThe average improvement is of\t" + Math.floor((1 - (average / originalAverage))*1000)/10 + "%");

        Utils.printTest("\n\nOriginal Results:");
        for (Map.Entry<Double, Integer> entry : amountOfCosts_original.entrySet()) {
            System.out.println(entry.getKey() + ", " + entry.getValue());
        }
        Utils.printTest("\n\nImproved Results:");
        for (Map.Entry<Double, Integer> entry : amountOfCosts.entrySet()) {
            System.out.println(entry.getKey() + ", " + entry.getValue());
        }

        /*
        Utils.printTest(config.name + "\t(" + testingParameter + ")\tThe set with the best improvement is of\t" + bestImprovementRate + "%");
        Utils.printTest("Clustering:");
        for (Integer i : currentImprovementRateClustering) {
            System.out.println(i);
        }
        System.out.println("\n");
        */
        //System.out.print((average / config.repetitions) + " " + minAnsValue + " ");

        // Print the clusters
        Utils.printTest("ans:");
        System.out.println(minAns);
        Utils.printTest("\noriginal ans:");
        System.out.println(minOriginalAns);
        /*
        for (int i = 0; i < minAns.size(); i ++) {
            System.out.println("Cluster " + i + ":");
            Cluster<Integer> c = minAns.get(i);
            for (Vertex<Integer> v : c.content) {
                Movie m = Database.movies.get(v.data);
                System.out.println(m);
            }
            System.out.println("");
        }*/
    }


    private static void runNtimes_buildGraphTwice(AlgorithmConfig config, int testingParameter) {
        NumArrayList amountOfClusters = new NumArrayList();
        int minAmountOfClusters = 0;
        ClustersGroup<Integer> minAns = new ClustersGroup<>();
        double minAnsValue = Double.MAX_VALUE;
        double minAnsValue2 = Double.MAX_VALUE;
        double average = 0;
        double average2 = 0;

        for (int i = 0; i < config.repetitions; i++) {
            Graph[] graph = GraphGenerator.generateEdgesInTwoGraphs(config.graphSize, testingParameter);
            ClustersGroup<Integer> ans = CorrelationClustering.getCorrelationClustering(graph[0], config.choosingFunc);
            //ArrayList<Cluster<Integer>> ans = getMyClustering(graph[0], 0);
            double totalCost = ans.cost();

            if (totalCost < minAnsValue) {
                minAnsValue = totalCost;
                minAns = ans;
                minAmountOfClusters = ans.size();
            }
            average += totalCost;
            amountOfClusters.add(ans.size());


            // second calc
            ClustersGroup<Integer> ans2 = CorrelationClustering.getCorrelationClustering(graph[1], config.choosingFunc);
            double totalCost2 = ans.cost();

            if (totalCost2 < minAnsValue2) {
                minAnsValue2 = totalCost2;
            }
            average2 += totalCost2;
        }

        System.out.println("\n=== BEFORE reading users' groups ===");
        Utils.printTest(config.name + "\t(" + testingParameter + ")\tThe average cost is: \t" + (average / config.repetitions));
        Utils.printTest(config.name + "\t(" + testingParameter + ")\tThe minimal cost is: \t" + minAnsValue);
        Utils.printTest(config.name + "\t(" + testingParameter + ")\tThe average amount of clusters is: " + amountOfClusters);
        //System.out.print((average / config.repetitions) + " " + minAnsValue + " ");

        System.out.println("\n=== AFTER reading users' groups ===");
        Utils.printTest(config.name + "\t(" + testingParameter + ")\tThe average cost is: \t" + (average2 / config.repetitions));
        Utils.printTest(config.name + "\t(" + testingParameter + ")\tThe minimal cost is: \t" + minAnsValue2);

    }

    @SuppressWarnings("unchecked")
    public static ClustersGroup<Integer> getMyClustering2(Graph g) {

        ClustersGroup<Integer> ans = new ClustersGroup<>();
        ClustersGroup<Integer> initialClusters = CorrelationClustering.initializeUnitClusters(g);
        Comparator<Cluster<Integer>> comparator = (c1, c2) -> c1.cost() > c2.cost() ? 1 : -1;

        while (initialClusters.size() > 0) {
            Pair<Integer, Integer> bestClustersToMerge = new Pair(-1, -1);
            double minCost = Double.MAX_VALUE;

            for (int i = 0; i < initialClusters.size(); i ++) {
                for (int j = 0; j < initialClusters.size(); j++) {
                    if (i == j) continue;

                    Cluster<Integer> c1 = initialClusters.get(i);
                    Cluster<Integer> c2 = initialClusters.get(j);
                    Cluster<Integer> disj = c1.disj(c2);

                    if (disj.cost() < c1.cost() + c2.cost()) {
                        minCost = disj.cost();
                        bestClustersToMerge = new Pair(i, j);
                    }
                }
            }

            if (bestClustersToMerge.getKey() > -1) {
                // There's indeed a pair of clusters that's worth merging!
                initialClusters.merge(bestClustersToMerge);
            }
            else {
                ans.add(initialClusters);
                initialClusters.clear();
            }
        }

        return ans;
    }
}
