package MiniProject.DataStructure;

import java.util.Collections;

public class NumArrayList extends java.util.ArrayList<Double> {

    double mean = -1;
    double variance = -1;

    public NumArrayList() {
        super();
    }

    public NumArrayList(Integer[] list) {
        super();
        for (int i : list) {
            double d = Double.parseDouble("" + i);
            this.add(d);
        }
    }

    public boolean add(int i) {
        double d = Double.parseDouble("" + i);
        return this.add(d);
    }

    public boolean addDouble(double d) {
        return this.add(d);
    }

    public double mean() {
        double sum = 0;
        for (int i = 0; i < this.size(); i++) {
            sum += this.get(i);
        }
        mean = sum / this.size();
        return mean;
    }

    public double variance() {
        if (variance != -1.0) {
            return variance;
        }
        else {
            double squaresSum = 0;

            for (int i = 0; i < this.size(); i++) {
                double curr = this.get(i);
                squaresSum += Math.pow(curr, 2.0);
            }

            double ans = squaresSum / this.size();
            ans -= Math.pow(mean(), 2);
            variance = ans;
            return ans;
        }
    }

    public double min() {
        return Collections.min(this);
    }

    public String toString() {
        //return this.mean() + ""; //+ ", " + this.variance();
        return this.mean() + ", " + this.variance();
    }

    public String normalize(int scale) {
        return (this.mean() / scale) + "";
    }

}
