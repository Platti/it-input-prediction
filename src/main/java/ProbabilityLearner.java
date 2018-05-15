import java.util.*;

public class ProbabilityLearner {
    private HashMap<String, List<Map.Entry<String, Double>>> database;
    private HashMap<String, HashMap<String, Integer>> counter;
    private double total;
    public ProbabilityLearner(String filename){
         counter = new HashMap<>();

        addSucceedingWords("hello", "world");
        addSucceedingWords("hello", "world");
        addSucceedingWords("hello", "world");
        addSucceedingWords("hello", "world");
        addSucceedingWords("hello", "world");
        addSucceedingWords("hello", "you");
        addSucceedingWords("hello", "you");
        addSucceedingWords("hello", "you");
        addSucceedingWords("you", "are");
        addSucceedingWords("are", "my");
        addSucceedingWords("are", "her");
        addSucceedingWords("are", "his");
        addSucceedingWords("are", "the");
        addSucceedingWords("are", "the");



        finalizeDatabase();
    }

    private void addSucceedingWords(String w0, String w1) {
        HashMap<String, Integer> map = counter.getOrDefault(w0, new HashMap<>());
        int freq = map.getOrDefault(w1, 0);
        map.put(w1, freq + 1);
        counter.put(w0, map);
        total++;
    }

    private void finalizeDatabase(){
        database = new HashMap<>();
        for(Map.Entry<String, HashMap<String, Integer>> entry0 : counter.entrySet()) {
            List<Map.Entry<String, Double>> list = new ArrayList();
            for(Map.Entry<String, Integer> entry1 : entry0.getValue().entrySet()) {
                list.add(new AbstractMap.SimpleEntry<>(entry1.getKey(), entry1.getValue() / total));
            }
            list.sort((o1, o2) -> -o1.getValue().compareTo(o2.getValue()));
            database.put(entry0.getKey(), list);
        }
    }

    public HashMap<String, List<Map.Entry<String, Double>>> getDatabase() {
        return database;
    }
}
