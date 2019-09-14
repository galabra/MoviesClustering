package MiniProject;

import MiniProject.Algorithm.GraphGenerator;
import MiniProject.Algorithm.VertexChoosingFunc;
import MiniProject.Exceptions.MovieDoesntExistException;
import MiniProject.Graph.ClustersGroup;
import MiniProject.Graph.Graph;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class Main {

    private enum AlgorithmType {
        ORIGINAL,
        IMPROVED
    }

    public static void main(String[] args) {
        Reader.readInvalidMovies();
        //args = new String[]{"rawDB", "1", "testInput.txt"};

        if (args.length != 3) {
            System.err.println("Invalid arguments. Please try again.");
        } else {
            try {
                ArrayList<Integer> currentDataSet = Reader.readMovieIdsFromFile(args[2]);
                Reader.readDatabase(args[0], currentDataSet);
                ClustersGroup<Integer> ans = getClustering(String.valueOf(args[1]), currentDataSet);
                System.out.println(ans);

            } catch (FileNotFoundException e) {
                System.err.println("Can't find the input file '" + args[2] + "'. Please try again.");
            } catch (IllegalArgumentException e) {
                System.err.println("Illegal argument '" + args[1] + "'. Please try again.");
            } catch (MovieDoesntExistException e) {
                System.err.println("Wrong input: Movie #" + e.invalidMovieId + " doesn't exist. Please try again.");
            } catch (IOException e) {
                System.err.println("An error has occurred. Please try again.");
            }
        }
    }

    private static ClustersGroup<Integer> getClustering(String typeStr, ArrayList<Integer> currentDataSet) {
        Graph g = GraphGenerator.getRandomGraph(currentDataSet);

        switch(getAlgorithmType(typeStr)) {
            case ORIGINAL:
                return CorrelationClustering.getCorrelationClustering(g, VertexChoosingFunc.random);
            case IMPROVED:
                return CorrelationClustering.getMyClustering(g);
        }
        return null;
    }

    private static AlgorithmType getAlgorithmType(String t) {
        int asInt = Integer.parseInt(t);
        switch (asInt) {
            case 1:
                return AlgorithmType.ORIGINAL;
            case 2:
                return AlgorithmType.IMPROVED;
            default:
                throw new IllegalArgumentException();
        }
    }

}
