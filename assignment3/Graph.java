package assignment3;
import java.util.*;


public class Graph {

    private HashMap<String, ArrayList<String>> adjlist;

    public Graph(){
        adjlist = new HashMap<String, ArrayList<String>>();


    }
    /**
     * creates an adjacency list representation of a graph using a Hashmap
     * each word in the dictionary is a vertex and there is an edge between words differing by one letter
     * each word is a key and the edges are contained in an arraylist
     * Optimizations- removing words from the dictionary as edges were discovered
     *                if the current vertex differs by more than one from a word in a dictionary, go to the next word
     * @param start Strings for start word
     * @param dict  ArrayList holding the dictionary
     *
     *
     */
    public void makeGraph(String start, ArrayList<String> dict) {

        ArrayList<String> wordsDiscovered = new ArrayList<String>();
        wordsDiscovered.add(start);
        if(dict.contains(start)){
            int indexStart = dict.indexOf(start);
           dict.remove(indexStart);
        }


        for(String dictword : dict){
            ArrayList<String> initalEdges = new ArrayList<String>();
            adjlist.put(dictword, initalEdges);
        }

        for(int k = 0; k< wordsDiscovered.size(); k++){

            ArrayList<String> edges = new ArrayList<String>();
            String word = wordsDiscovered.get(k);
            for (int i = 0; i < dict.size(); i++) {
                int differ = 0;
                for (int j = 0; j < dict.get(i).length(); j++) {
                    if (differ > 1) {
                        break;
                    }
                    if (dict.get(i).charAt(j) != word.charAt(j)) {
                        differ++;
                    }
                }
                if (differ == 1) {
                    wordsDiscovered.add(dict.get(i));
                    edges.add(dict.get(i));
                    adjlist.put(word, edges);
                    dict.remove(i);
                }
            }
        }
    }

    /**
     * Accessor method for adjList
     * @return adjList using a Hashmap
     */
    public HashMap<String, ArrayList<String>> getAdjlist(){
        return adjlist;
    }

}
