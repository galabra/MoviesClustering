package MiniProject.Exceptions;

public class MovieDoesntExistException extends Exception {

    public int invalidMovieId;

    public MovieDoesntExistException(int movieId) {
        this.invalidMovieId = movieId;
    }
}
