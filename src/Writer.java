package MiniProject;

import MiniProject.DataStructure.Movie;
import MiniProject.Utils.Utils;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Writer {

    public static void writeDatabaseToFile(String moviesOrUsers) {
        boolean isUsers = moviesOrUsers.toLowerCase().equals("users");
        String fileName              = isUsers ? "allUsers.txt" : "allMovies.txt";
        HashMap<Integer, ?> database = isUsers ? Database.users : Database.movies;

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(fileName)))) {
            String prefix = "";
            for (Object curr : database.values()) {
                if (isUsers || ((Movie) curr).isValid()) {
                    out.print(prefix);
                    prefix = "\n";
                    out.print(Utils.removeParenthesis(curr.toString()));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeMoviesToParsedDB(String dir) {
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(dir + "/normalized_movies.dat")))) {
            String line;
            String prefix = "";
            for (Movie m : Database.movies.values()) {
                out.print(prefix);
                prefix = "\n";
                line = m.id + "::" +
                        //m.name + " (" + m.year + ")::" +
                        //m.printGenres(false, "|") + "::" +
                        m.printWatchedBoth();
                out.print(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void printUsersByGroups(HashMap<Integer, Integer> ans, int maxClusterId) {
        HashMap<Integer, ArrayList<Integer>> groupsList = new HashMap<>();
        for (int i : Utils.range(0, maxClusterId, 1)) {
            groupsList.put(i, new ArrayList<>());
        }

        for (Map.Entry<Integer, Integer> entry : ans.entrySet()) {
            int groupId = entry.getValue();
            int userId = entry.getKey();
            groupsList.get(groupId).add(userId);
        }

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("usersByGroups_detailed.txt")))) {
            out.println("Group#\tUser");

            for (Map.Entry<Integer, ArrayList<Integer>> entry : groupsList.entrySet()) {
                int groupId = entry.getKey();
                for (int userId : entry.getValue()) {
                    String user = Database.users.get(userId).toString().replaceAll("[,\\[\\]]", "");
                    out.println("[" + groupId + " " + user + "]");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
