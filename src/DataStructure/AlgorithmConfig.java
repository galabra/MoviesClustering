package MiniProject.DataStructure;

import java.util.function.Function;

public class AlgorithmConfig {

    public String name;
    public Function choosingFunc;
    public int repetitions;
    public int graphSize;

    public AlgorithmConfig(String name, Function func, int repetitions, int graphSize) {
        this.name = name;
        this.choosingFunc = func;
        this.repetitions = repetitions;
        this.graphSize = graphSize;
    }

}
