package MiniProject;

import MiniProject.DataStructure.Movie;
import MiniProject.DataStructure.User;
import MiniProject.Utils.Utils;

import java.util.*;
import java.util.stream.Stream;

public class Database {

    public static ArrayList<String> genresList = new ArrayList<>(Arrays.asList("Action", "Adventure", "Animation", "Children's", "Comedy", "Crime", "Documentary", "Drama", "Fantasy", "Film-Noir", "Horror", "Musical", "Mystery", "Romance", "Sci-Fi", "Thriller", "War", "Western")); // 18 genres
    public static HashMap<Integer, User> users = new LinkedHashMap<>();    // id to user
    public static HashMap<Integer, Movie> movies = new LinkedHashMap<>();  // id to movie
    public static HashMap<Integer, Integer> usersToGroups = new HashMap<>();
    public static HashMap<Integer, Integer> invalidMovies = new HashMap<>();    // movieId -> amount of ratings (non-existent movies has -1 ratings)
    public static ArrayList<Integer> validUsersIds = new ArrayList<>();
    public static ArrayList<Integer> validMoviesIds = new ArrayList<>();
    private static int usersAmount = 6040;
    private static int moviesAmount = 3883;
    public static int amountOfUsersGroups;

    public static String moviesDBpath  = "parsedDB/original_normalized_movies.dat";
    public static String usersDBpath   = "rawDB/users.dat";
    public static String ratingsDBpath = "rawDB/ratings.dat";

    public static Movie getMovieByIndex(int index) {
        Movie ans = null;
        for (Map.Entry<Integer, Movie> entry : movies.entrySet()) {
            if (--index == 0) {
                ans = entry.getValue();
                break;
            }
        }
        return ans;
    }

    public static ArrayList<Integer> printRandomVertices(String moviesOrUsers, int size, String config) {
        ArrayList<Integer> verticesList = new ArrayList<>();
        boolean isMovies = moviesOrUsers.toLowerCase().equals("movies");

        ArrayList<Integer> originalIds = new ArrayList<>();
        HashMap<Integer, ?> database = isMovies ? Database.movies : Database.users;

        for (Object curr : database.values()) {
            if (isMovies) {
                Movie currMovie = (Movie) curr;
                if (currMovie.isValid()) {
                    originalIds.add(currMovie.id);
                }
            }
            else {
                originalIds.add(((User) curr).id);
            }
        }

        for (int i = 0; i < size; i ++) {
            int random = (int) Math.floor(Math.random() * originalIds.size());
            Integer randomId = originalIds.remove(random);
            verticesList.add(randomId);
        }

        if (config.toLowerCase().equals("print items")) {
            //      Print the actual movies/users
            Stream<Object> listOfItems = verticesList.stream().map(id -> database.get(id));
            Utils.printAsArray(listOfItems.toArray(), true, ", ");
        }
        else if (config.toLowerCase().equals("print ids")) {
            //      Print only the movies/users' ids
            System.out.println(size + " random " + (isMovies ? "movies:" : "users:"));
            Utils.printAsArray(verticesList.toArray(), true, ", ");
        }
        return verticesList;
    }

}
