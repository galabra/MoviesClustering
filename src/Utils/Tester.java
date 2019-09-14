package MiniProject.Utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.ExecutorService;

import static java.util.concurrent.Executors.newFixedThreadPool;
import static java.util.concurrent.Executors.newSingleThreadExecutor;

public class Tester {

    public static ExecutorService pool = newFixedThreadPool(10);
    private static Comparator<Object> comp = null;
    private static ExecutorService resultUpdater = newSingleThreadExecutor();
    private static Object result = null;
    public static ArrayList<Double> results = new ArrayList<>();

    public static void addResult(Double d) {
        Runnable task = () -> {
            results.add(d);

            if (results.size() == 10000) {
                try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("output_test.txt", true)))) {
                    for (Double line : results) {
                        out.println(line);
                    }
                    results = new ArrayList<>();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        resultUpdater.execute(task);
    }

    public static void setComparator(Comparator<Object> c) {
        comp = c;
    }

    public static void updateResult(Object o) {
        Runnable task = () -> {
            if (test(o)) {
                result = o;
            }
        };
        resultUpdater.execute(task);
    }

    private static boolean test(Object o) {
        return comp.compare(o, result) > 0;
    }

    public static void printResult(String msg) {
        System.out.println(">>\t" + msg + "\n" + result + "\n");
    }

}
