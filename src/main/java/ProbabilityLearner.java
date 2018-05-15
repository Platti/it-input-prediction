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

public class ProbabilityLearner {
    private HashMap<String, List<Map.Entry<String, Double>>> database;
    private HashMap<String, HashMap<String, Integer>> counter;
    private double total;

    public ProbabilityLearner(String filename) {
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

        readXMLFile(filename);

        finalizeDatabase();
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
        for(int i = 1; i < words.length; i++){
            addSucceedingWords(words[i-1], words[i]);
        }
    }

    private void addSucceedingWords(String w0, String w1) {
        HashMap<String, Integer> map = counter.getOrDefault(w0, new HashMap<>());
        int freq = map.getOrDefault(w1, 0);
        map.put(w1, freq + 1);
        counter.put(w0, map);
        total++;
    }

    private void finalizeDatabase() {
        database = new HashMap<>();
        for (Map.Entry<String, HashMap<String, Integer>> entry0 : counter.entrySet()) {
            List<Map.Entry<String, Double>> list = new ArrayList();
            for (Map.Entry<String, Integer> entry1 : entry0.getValue().entrySet()) {
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
