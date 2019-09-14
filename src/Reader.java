package MiniProject;

import MiniProject.DataStructure.Movie;
import MiniProject.DataStructure.SubmissionMovie;
import MiniProject.DataStructure.User;
import MiniProject.Exceptions.MovieDoesntExistException;
import MiniProject.Utils.Utils;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.GZIPInputStream;

public class Reader {

    public static void readInvalidMovies() {
        try (BufferedReader br = new BufferedReader(new FileReader("bin/invalidMovies.txt"))) {
            String invalidMovies = br.readLine();
            String[] invalidMoviesArray = invalidMovies.split(" ");

            for (String movieToRatings : invalidMoviesArray) {
                String[] tmp = movieToRatings.split(",");
                int movieId = Integer.parseInt(tmp[0]);
                int ratingsAmount = Integer.parseInt(tmp[1]);
                Database.invalidMovies.put(movieId, ratingsAmount);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Integer> readMovieIdsFromFile(String path) throws IOException, MovieDoesntExistException {
        ArrayList<Integer> ans = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String str_movieId;
            while ((str_movieId = br.readLine()) != null) {
                int int_movieId = Integer.parseInt(str_movieId);

                if (isMovieValid(int_movieId)) {
                    ans.add(int_movieId);
                }
            }
        }

        return ans;
    }

    public static boolean isMovieValid(int movieId) throws MovieDoesntExistException {
        if (Movie.isInvalid(movieId)) {
            int invalidMovieRatings = Movie.getInvalidMovieRatings(movieId);

            if (invalidMovieRatings >= 0) {
                System.err.println("Movie " + movieId + " ignored because it has only " + invalidMovieRatings + " ratings");
                return false;
            }
            else {
                throw new MovieDoesntExistException(movieId);
            }
        }

        return true;
    }

    public static void readAllIdsFromFile() {
        List<Integer> allUsersIds = new ArrayList<>();
        List<Integer> allMoviesIds = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("parsedDB/movies_users_ids.txt"))) {
            String line = br.readLine();
            line = br.readLine();
            Stream<Integer> usersStream = Arrays.stream(line.split(", ")).map(x -> Integer.parseInt(x));
            allUsersIds.addAll(usersStream.collect(Collectors.toList()));

            line = br.readLine();
            line = br.readLine();
            Stream<Integer> moviesStream = Arrays.stream(line.split(", ")).map(x -> Integer.parseInt(x));
            allMoviesIds.addAll(moviesStream.collect(Collectors.toList()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Database.validUsersIds.addAll(allUsersIds);
        Database.validMoviesIds.addAll(allMoviesIds);
    }

    public static void readDatabase() {
        Database.users = generateUsersDB();
        Database.movies = generateMoviesDB();
        Reader.readRatingsDB();
    }

    public static void readDatabase(String datasetPath, ArrayList<Integer> moviesToRead) {
        try (BufferedReader br = new BufferedReader(new FileReader(datasetPath + "/movies.dat"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] arr = line.split("::");
                int movieId = Integer.parseInt(arr[0]);
                if (moviesToRead.contains(movieId)) {
                    SubmissionMovie newMovie = new SubmissionMovie(arr);
                    Database.movies.put(movieId, newMovie);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (InputStream fileStream = new FileInputStream("bin/normalized_movies.dat.gz");
             InputStream gzipStream = new GZIPInputStream(fileStream);
             java.io.Reader decoder = new InputStreamReader(gzipStream, "UTF-8");
             BufferedReader br = new BufferedReader(decoder)) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] arr = line.split("::");
                int movieId = Integer.parseInt(arr[0]);
                if (moviesToRead.contains(movieId)) {
                    SubmissionMovie currMovie = (SubmissionMovie) Database.movies.get(movieId);
                    currMovie.addWatchedBoth(arr);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static HashMap<Integer, User> generateUsersDB() {
        HashMap<Integer, User> usersDB = new LinkedHashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(Database.usersDBpath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] arr = line.split("::");
                User newUser = new User(arr);
                usersDB.put(Integer.parseInt(arr[0]), newUser);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Utils.printTest("Imported " + usersDB.size() + " new users");
        return usersDB;
    }

    public static HashMap<Integer, Movie> generateMoviesDB() {
        HashMap<Integer, Movie> moviesDB = new LinkedHashMap<>();  // id to movie

        try (BufferedReader br = new BufferedReader(new FileReader(Database.moviesDBpath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] arr = line.split("::");
                Movie newMovie = new Movie(arr);
                moviesDB.put(Integer.parseInt(arr[0]), newMovie);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Utils.printTest("Imported " + moviesDB.size() + " new movies");
        return moviesDB;
    }

    public static void readRatingsDB() {
        int counter = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(Database.ratingsDBpath))) {
            String line;
            while ((line = br.readLine()) != null) {
                counter ++;
                String[] arr = line.split("::");
                int userId = Integer.parseInt(arr[0]);
                int movieId = Integer.parseInt(arr[1]);
                int rating = Integer.parseInt(arr[2]);

                Database.users .get(userId).addMovie(movieId, rating);
                Database.movies.get(movieId).addUser(userId, rating);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Utils.printTest("Read " + counter + " ratings");
    }

    public static String readFromPythonFile() {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader("python_input.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line + '\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static String readFromFile(String path) {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line + '\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

}
