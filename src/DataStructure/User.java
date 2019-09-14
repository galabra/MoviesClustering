package MiniProject.DataStructure;

import MiniProject.DataStructure.UserProps.Age;
import MiniProject.DataStructure.UserProps.Gender;
import MiniProject.Database;
import MiniProject.Utils.Utils;

import java.util.HashMap;

public class User {

    public int id;
    public Gender gender;
    public Age age;
    public int occupation;
    public double zipcode;
    public HashMap<Integer, Integer> watchList = new HashMap<>();   // movieId -> Rating
    public NumArrayList ratings = new NumArrayList();

    public User(String[] arr) {
        this.id         = Integer.parseInt(arr[0]);
        this.gender     = new Gender(arr[1]);
        this.age        = new Age(arr[2]);
        this.occupation = Integer.parseInt(arr[3]);
        this.zipcode    = parseZipcode(arr[4]);
    }

    private double parseZipcode(String input) {
        // TODO: change the amount of groups to normalize the zipcode
        double ans = Double.parseDouble(input.replace("-", "."));
        if (ans > 99999) {  // normalize to 5 digits
            double tmp = Math.log10(ans);
            tmp = Math.floor(tmp) - 4;
            tmp = Math.pow(10, tmp);
            ans /= tmp;
        }

        return Math.floor(ans / 5000) / 18.0;
    }

    public void addMovie(int movieId, int rating) {
        watchList.put(movieId, rating);
        ratings.add(rating);
    }

    public int amountWatched() {
        return this.watchList.size();
    }

    public String toString() {
        boolean prettyPrint = false;
        if (prettyPrint) {
            return "{User #" + id + ":" + (id < 1000 ? "\t" : "") +
                    "\t" + gender.full + ",\tage:" + age + ",\toccupation:" + occupation +
                    ",\t" + "zipcode:" + zipcode + ",\t" + (zipcode < 10000 ? "\t" : "") +
                    "watched:" + amountWatched() + "}";
        }

        String delimiter = ", ";
        Object[] listToPrint = new Object[] {
                //printGroup(),
                id,
                gender,
                age.normalize(),
                printOccupation(delimiter),
                //occupation,
                zipcode,
                printGenres(delimiter)
            };
        return Utils.printAsArray(listToPrint, false, delimiter);
    }

    private String printGroup() {
        try {
            return Database.usersToGroups.get(id).toString();
        }
        catch (NullPointerException e) {
            return "";
        }
    }

    private String printOccupation(String delimiter) {
        StringBuilder sb = new StringBuilder();
        String prefix = "";
        for (int i = 0; i < 21; i ++) {
            String toPrint = i == occupation ? "1" : "0";
            sb.append(prefix + toPrint);
            prefix = delimiter;
        }

        return sb.toString();
    }

    private String printGenres(String delimiter) {
        HashMap<Integer, Integer> genresToAmountWatched = new HashMap<>();
        int totalGenresAmount = 0;

        for (int i = 0; i < Database.genresList.size(); i ++) {
            genresToAmountWatched.put(i, 0);
        }

        for (int movieId : watchList.keySet()) {
            Movie m = Database.movies.get(movieId);
            for (int genreId : m.genres) {
                int tmp = genresToAmountWatched.get(genreId) + 1;
                genresToAmountWatched.put(genreId, tmp);
                totalGenresAmount ++;
            }
        }

        StringBuilder ans = new StringBuilder();
        String prefix = "";
        for (int i = 0; i < Database.genresList.size(); i ++) {
            ans.append(prefix);
            double genreProportion = (double)genresToAmountWatched.get(i) / (double)totalGenresAmount;
            //genreProportion = Utils.roundTwoDigits(genreProportion);
            ans.append(genreProportion);
            prefix = delimiter;
        }

        return ans.toString();
    }

}
