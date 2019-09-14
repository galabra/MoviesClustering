package MiniProject;

import MiniProject.DataStructure.Movie;
import MiniProject.DataStructure.User;
import MiniProject.Utils.Utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/*
    This class was used to extract and generate data for the improved algorithm (in its many versions)
 */
public class Parser {

    private static String originDataDir = "rawDB";
    private static String parsedDataDir = "parsedDB";

    private static int usersAmount = 6040;
    private static int moviesAmount = 3883;

    private static void calculateAll_watchedProportions() {
        for (Movie m : Database.movies.values()) {
            double watchedProportion = (2.0 / moviesAmount);
            for (User u : Database.users.values()) {
                if (u.watchList.keySet().contains(m.id)) {
                    watchedProportion += (2.0 / (double)(u.amountWatched()));
                }
            }
            watchedProportion *= (1.0 / (usersAmount + 1.0));
            m.watchedBoth.put(m.id, watchedProportion);
        }

        Utils.printTest("Calculated p(m) for all movies");
    }

    private static void calculateAll_watchedBothProportions() {
        double fixedElement = 2.0 / ((usersAmount + 1.0) * (moviesAmount * (moviesAmount - 1.0)));

        for (int j = 0; j < moviesAmount; j ++) {
            Movie m1 = Database.getMovieByIndex(j);

            for (int t = j + 1; t < moviesAmount; t ++) {
                Movie m2 = Database.getMovieByIndex(t);

                double watchedBothProportion = 0;

                for (User u : Database.users.values()) {
                    Set<Integer> watchList = u.watchList.keySet();
                    if (watchList.contains(m1.id) && watchList.contains(m2.id)) {
                        double amountWatched = u.amountWatched();
                        watchedBothProportion += (2.0 / (amountWatched * (amountWatched - 1.0)));
                    }
                }

                watchedBothProportion /= (usersAmount + 1.0);
                watchedBothProportion += fixedElement;

                m1.watchedBoth.put(m2.id, watchedBothProportion);
            }

            Utils.printTest("Calculated p(m" + (j+1) + ", i) for all i > " + (j+1));
        }

        Utils.printTest("Calculated p(m1, m2) for all couples of movies");
    }

    public static Double[] getMinMaxValues() {
        Double[] ans = new Double[] {-1.0, 1.0};

        try (BufferedReader br = new BufferedReader(new FileReader("parsedDB/movies.dat"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] arr = line.split("::");
                String[] tmp = arr[3].substring(2, arr[3].length() - 2).split(">,<");

                ArrayList<Double> list = new ArrayList<>();
                for (String s : tmp) {
                    String num = s.split(",")[1];
                    list.add(Double.parseDouble(num));
                }
                ans[0] = Math.max(ans[0], Collections.max(list));
                ans[1] = Math.min(ans[1], Collections.min(list));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ans;
    }

}
