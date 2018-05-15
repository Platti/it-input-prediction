import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class PredictionLearner {
    private static final int MAX_PREDICTIONS = 100;
    private final int maxPrefixLetters;
    private HashMap<String, List<Map.Entry<String, Integer>>> databaseSucceeding;
    private HashMap<String, List<Map.Entry<String, Integer>>> databaseCompletion;
    private HashMap<String, HashMap<String, Integer>> counterSucceeding;
    private HashMap<String, HashMap<String, Integer>> counterCompletion;

    public PredictionLearner(String filename, int maxPrefixLetters) {
        System.out.println("Learning predictions from " + filename + " ...");
        this.maxPrefixLetters = maxPrefixLetters;
        counterSucceeding = new HashMap<>();
        counterCompletion = new HashMap<>();

        readXMLFile(filename);

        databaseSucceeding = finalizeDatabase(counterSucceeding);
        databaseCompletion = finalizeDatabase(counterCompletion);
        counterSucceeding = null;
        counterCompletion = null;
        System.out.println("Finished learning!");

    }

    private void readXMLFile(String filename) {
        File fXmlFile = new File(filename);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("message");
            for (int i = 0; i < nList.getLength(); i++) {
                Node n = nList.item(i);
                if (n.getNodeType() == Node.ELEMENT_NODE) {
                    Element e = (Element) n;
                    addTextMessage(e.getElementsByTagName("text").item(0).getTextContent());
                }
            }
        } catch (SAXException | IOException | ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    private void addTextMessage(String text) {
        String[] words = text.replaceAll("[^a-zA-Z ]", "").toLowerCase().split("\\s+");
        for (int i = 0; i < words.length; i++) {
            addCompletionWord(words[i]);
            if (i > 0)
                addSucceedingWords(words[i - 1], words[i]);
        }
    }

    private void addCompletionWord(String word) {
        for (int i = 1; i <= maxPrefixLetters && i < word.length(); i++) {
            String prefix = word.substring(0, i);
            HashMap<String, Integer> map = counterCompletion.getOrDefault(prefix, new HashMap<>());
            int freq = map.getOrDefault(word, 0);
            map.put(word, freq + 1);
            counterCompletion.put(prefix, map);
        }
    }

    private void addSucceedingWords(String w0, String w1) {
        HashMap<String, Integer> map = counterSucceeding.getOrDefault(w0, new HashMap<>());
        int freq = map.getOrDefault(w1, 0);
        map.put(w1, freq + 1);
        counterSucceeding.put(w0, map);
    }

    private HashMap<String, List<Map.Entry<String, Integer>>> finalizeDatabase(HashMap<String, HashMap<String, Integer>> counts) {
        HashMap<String, List<Map.Entry<String, Integer>>> db = new HashMap<>();
        for (Map.Entry<String, HashMap<String, Integer>> entry0 : counts.entrySet()) {
            List<Map.Entry<String, Integer>> list = new ArrayList();
            for (Map.Entry<String, Integer> entry1 : entry0.getValue().entrySet()) {
                list.add(new AbstractMap.SimpleEntry<>(entry1.getKey(), entry1.getValue()));
            }
            list.sort((o1, o2) -> -o1.getValue().compareTo(o2.getValue()));
            if(list.size() > MAX_PREDICTIONS)
                list = list.subList(0, MAX_PREDICTIONS);
            db.put(entry0.getKey(), list);
        }
        return db;
    }

    public HashMap<String, List<Map.Entry<String, Integer>>> getDatabaseSucceeding() {
        return databaseSucceeding;
    }

    public HashMap<String, List<Map.Entry<String, Integer>>> getDatabaseCompletion() {
        return databaseCompletion;
    }
}
