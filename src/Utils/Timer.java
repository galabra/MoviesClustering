package MiniProject.Utils;

public class Timer {

    private static long start;

    public static void start() {
        start = System.currentTimeMillis();
    }

    public static double getTimePassed() {
        double timePassed = System.currentTimeMillis() - start;
        timePassed /= 1000.0;
        return timePassed;
    }

    public static void stop() {
        double timePassed = System.currentTimeMillis() - start;
        timePassed /= 1000.0;
        System.out.println("\n=== The program took " + timePassed + " seconds to run ===");
    }

}
