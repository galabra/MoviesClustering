package MiniProject.DataStructure;

public class SubmissionMovie extends Movie {

    public SubmissionMovie(String[] arr) {
        this.id = Integer.parseInt(arr[0]);
        String[] nameAndYear = splitNameAndYear(arr[1]);
        this.name = nameAndYear[0];
    }

    public void addWatchedBoth(String[] arr) {
        String[] tmpArr = arr[1].split("\\|");
        for (String tmpEntry : tmpArr) {
            String[] entry = tmpEntry.split(",");

            int    key = Integer.parseInt(entry[0]);
            double val = Double.parseDouble(entry[1]);

            watchedBoth.put(key, val);
        }
    }

}
