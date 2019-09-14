package MiniProject.DataStructure;

import MiniProject.Database;
import MiniProject.Utils.Utils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Movie implements Comparable {

    public String name;
    public int id;
    public int year;
    public HashMap<Integer, Double> watchedBoth = new HashMap<>();
    public NumArrayList rating = new NumArrayList();
    public NumArrayList ratersAge = new NumArrayList();
    public ArrayList<Integer> genres = new ArrayList<>();
    public ArrayList<Integer> ratersOccupation = new ArrayList<>();
    public ArrayList<Integer> ratersIds = new ArrayList<>();

    public Movie() {}

    public Movie(String[] arr) {
        this.id = Integer.parseInt(arr[0]);

        String[] nameAndYear = splitNameAndYear(arr[1]);
        this.name = nameAndYear[0];
        this.year = Integer.parseInt(nameAndYear[1]);

        String[] tmpGenres = arr[2].split("\\|");
        for (String genreStr : tmpGenres) {
            this.genres.add(Integer.parseInt(genreStr));
        }

        //String tmp = arr[3].substring(2, arr[3].length() - 2);
        //String[] tmpArr = tmp.split(">,<");

        String[] tmpArr = arr[3].split("\\|");


        // String[] tmpArr = arr[1].split("\\|");
        for (String tmpEntry : tmpArr) {
            String[] entry = tmpEntry.split(",");

            int    key = Integer.parseInt(entry[0]);
            double val = Double.parseDouble(entry[1]);

            watchedBoth.put(key, val);
        }

    }

    protected String[] splitNameAndYear(String s) {
        String name = "", year = "";

        String pattern = "^(.*[^ ]) ?\\((\\d{4})\\)$";
        Matcher m = Pattern.compile(pattern).matcher(s);
        if (m.find()) {
            name = m.group(1);
            year = m.group(2);
        }

        return new String[]{name, year};
    }

    public double p() {
        return watchedBoth.get(id);
    }

    public boolean isValid() {
        return rating.size() >= 10;
    }

    public String printGenres(boolean parenthesis, String pre) {
        StringBuilder ans = new StringBuilder();
        String prefix = parenthesis? "(" : "";
        for (double genre : genres) {
            ans.append(prefix);
            prefix = pre;
            ans.append(Integer.parseInt("" + genre));
        }
        ans.append(parenthesis? ")" : "");
        return ans.toString();
    }

    public String printWatchedBoth() {
        StringBuilder ans = new StringBuilder();
        String prefix = "";
        String item;
        for (Map.Entry<Integer, Double> entry : watchedBoth.entrySet()) {
            ans.append(prefix);
            prefix = "|";
            item = entry.getKey() + "," + normalizeWatchedBoth(entry.getValue());
            ans.append(item);
        }
        return ans.toString();
    }

    private double normalizeWatchedBoth(Double input) {
        //return input;
        return Math.floor(input * Math.pow(10, 11)) / Math.pow(10, 11);
        /*
        double min = Double.parseDouble("2.196333260045009E-11");
        double max = Double.parseDouble("0.013689251052754444");
        return (input - min) / (max - min);
        */
    }

    public String toString() {
        String delimiter = ",\t";

        Object[] arr = new Object[] {
                id,                                     // 1
                year,
                //printYear(),                          // 1 year
                printGenres(delimiter, true),   // 18 genres
                rating.normalize(5),              // 1 average rating
                ratersAge.normalize(56),          // 1 average age
                printOccupations(delimiter),            // 21 occupations
                //printUsersGroups(delimiter)           // dynamic
            };

        return Utils.printAsArray(arr, false, delimiter);
    }

    private String printYear() {
        double maxYear = 2000;
        double minYear = 1919;
        return "" + (((double)year - minYear) / (maxYear - minYear));
    }

    private String printGenres(String delimiter, boolean asString) {
        StringBuilder ans = new StringBuilder();
        String prefix = "";
        if (asString) {
            for (int i = 0; i < Database.genresList.size(); i++) {
                ans.append(prefix);
                ans.append(genres.contains(i) ? "1" : "0");
                prefix = delimiter;
            }
        }
        else {
            for (int id : genres) {
                ans.append(prefix);
                ans.append(idToGenre(id));
                prefix = delimiter;
            }
        }

        return ans.toString();
    }

    private String printOccupations(String delimiter) {
        int[] occupationsAmounts = new int[21];
        for (Integer occupation : ratersOccupation) {
            occupationsAmounts[occupation] ++;
        }

        int totalAmountOfRaters = ratersOccupation.size();

        ArrayList<Double> ans = new ArrayList<>();
        for (Integer amount : occupationsAmounts) {
            double numerator = Double.parseDouble("" + amount);
            ans.add(numerator / ((double) totalAmountOfRaters));
        }

        return Utils.printAsPartOfArray(ans.toArray(), delimiter);
    }

    private int genreToId(String genre) {
        return Database.genresList.indexOf(genre);
    }

    private Object idToGenre(int id) {
        return Database.genresList.get(id);
    }

    public void addUser(int userId, int rating) {
        User currUser = Database.users.get(userId);
        this.ratersIds.add(userId);
        this.rating.add(rating);
        this.ratersAge.add(currUser.age.value);
        this.ratersOccupation.add(currUser.occupation);
    }

    private String printUsersGroups(String delimiter) {
        Object[] arr = getUsersGroups();
        return Utils.printAsPartOfArray(arr, delimiter);
    }

    public Object[] getUsersGroups() {
        ArrayList<Integer> groupsIndices = Utils.range(0, Database.amountOfUsersGroups - 1, 1);

        HashMap<Integer, Double> groupIdToProportion = new HashMap<>();
        for (int i : groupsIndices) {
            groupIdToProportion.put(i, 0.);
        }


        for (int currUserId : ratersIds) {
            int currUserGroup = Database.usersToGroups.get(currUserId);
            try {
                double tmp = groupIdToProportion.get(currUserGroup) + 1;
                groupIdToProportion.put(currUserGroup, tmp);
            } catch (NullPointerException e) {
                System.out.println(currUserGroup);
            }
        }

        // Turn the absolute values into proportions
        for (int i : groupsIndices) {
            double tmp = groupIdToProportion.get(i) / ratersIds.size();
            groupIdToProportion.put(i, tmp);
        }

        return groupIdToProportion.values().toArray();
    }

    @Override
    public int compareTo(Object o) {
        return this.id - ((Movie) o).id;
    }

    public Set<Integer> commonRaters(Movie other) {
        Set<Integer> ans = new HashSet<>();

        for (int id : this.ratersIds) {
            if (other.ratersIds.contains(id)) {
                ans.add(id);
            }
        }

        return ans;
    }

    public static boolean isInvalid(int movieId) {
        return Database.invalidMovies.containsKey(movieId);
    }

    public static int getInvalidMovieRatings(int movieId) {
        return Database.invalidMovies.get(movieId);
    }

}
