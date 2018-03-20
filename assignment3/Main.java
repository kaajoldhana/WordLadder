/* WORD LADDER Main.java
 * EE422C Project 3 submission by
 * Replace <...> with your actual data.
 * <Kaajol Dhana>
 * <krd985>
 * <Student1 5-digit Unique No.>
 * <Student2 Name>
 * <Student2 EID>
 * <Student2 5-digit Unique No.>
 * Slip days used: <0>
 * Git URL:
 * Spring 2018
 */

package assignment3;
import java.util.*;
import java.io.*;

public class Main {


	
	public static void main(String[] args) throws Exception {
		
		Scanner kb;	// input Scanner for commands
		PrintStream ps;
		// output file, for student testing and grading only
		// If arguments are specified, read/write from/to files instead of Std IO.
		if (args.length != 0) {
			kb = new Scanner(new File(args[0]));
			ps = new PrintStream(new File(args[1]));
			System.setOut(ps);			// redirect output to ps
		} else {
			kb = new Scanner(System.in);// default input from Stdin
			ps = System.out;			// default output to Stdout
		}

	//	ArrayList<String> testBFS6 = getWordLadderBFS("abaca", "patsy"); //zumin not in dictionary
	//	printLadder(testBFS6);
	//	ArrayList<String> testBFS1 = getWordLadderDFS("omens", "umber"); //zumin not in dictionary
	//	printLadder(testBFS1);
	/*
		ArrayList<String> testDFS1 = getWordLadderDFS("anlas", "zumin");
		printLadder(testDFS1);
		ArrayList<String> testBFS2 = getWordLadderBFS("anlas", "lymph");
		printLadder(testBFS2);
		ArrayList<String> testDFS2 = getWordLadderDFS("anlas", "lymph");
		printLadder(testDFS2);
		ArrayList<String> testBFS3 = getWordLadderBFS("anlas", "ogams");
		printLadder(testBFS3);
		ArrayList<String> testDFS3 = getWordLadderDFS("anlas", "ogams");
		printLadder(testDFS3);
		ArrayList<String> testBFS4 = getWordLadderBFS("irons", "apple");
		printLadder(testBFS4);
		ArrayList<String> testDFS4 = getWordLadderDFS("irons", "apple");
		printLadder(testDFS4);
		abaca patsy
		patsy porno
		*/
	//	ArrayList<String> testBFS5 = getWordLadderBFS("aldol", "drawl");
	//	printLadder(testBFS5);
		ArrayList<String> testDFS5 = getWordLadderDFS("hello", "cells");
		printLadder(testDFS5);



		while(true) {


			initialize();
			ArrayList<String> userInput = parse(kb);
			if (isQuit(userInput) || userInput.size() ==1) {
				System.out.println("Program has been terminated");
				return;
			}
			String start = userInput.get(0).toLowerCase();
			String end = userInput.get(1).toLowerCase();
			if(start.length() >5 || end.length() > 5){
				printLadder(userInput);
			}
			else{
				//System.out.println("BFS");
				ArrayList<String> testBFS = getWordLadderBFS(start,end);
				printLadder(testBFS);
				//System.out.println("DFS");
				ArrayList<String> testDFS = getWordLadderDFS(start,end);
				printLadder(testDFS);
			}

		}

		// TODO methods to read in words, output ladder
	}
	/**
	 *
	 *
	 * Initialize static variables and create dictionary
	 */
	public static void initialize() {
		Set dict = makeDictionary();

	}
	
	/**
	 * If command is /quit, return empty ArrayList.
	 * @param keyboard Scanner connected to System.in
	 * @return words ArrayList of Strings containing start word and end word.
	 *
	 */
	public static ArrayList<String> parse(Scanner keyboard) {
	    ArrayList <String> words = new  ArrayList <String>();
	    System.out.println("Type in two words to form  a word ladder : ");
	    String input = keyboard.nextLine();
	    String [] wordArray =input.split("\\s+");
		if(wordArray.length == 2){
			String start = wordArray[0].toLowerCase();
			String end = wordArray[1].toLowerCase();
			//words.add(wordArray[0].toLowerCase());
			//words.add(wordArray[1].toLowerCase());
			words.add(start);
			words.add(end);
		}
		else{
			words.add(wordArray[0].toLowerCase());
		}



        return words;






	}
	/**
	 * If command is /quit, return true
	 * @param words Arraylist holding keyboard input
	 * @return true if word in arraylist is quit keyword
	 *
	 */
	private static boolean isQuit(ArrayList<String> words ){
		if(words.contains("/quit")){
			return true;
		}
		return false;
	}


	/**
	 * DFS method to find word ladder using recursion, utilizes helper function
	 * creates graph of words with an edge connecting words differing by a letter
	 * calls a function to see if start word and end word are in the dictionary
	 * @param start Strings for start word
	 * @param end   Strings for end word
	 * @return ArrayList holding the word ladder
	 *
	 *
	 *
	 */
	public static ArrayList<String> getWordLadderDFS(String start, String end) {
		
		// Returned list should be ordered start to end.  Include start and end.
		// If ladder is empty, return list with just start and end.
		// TODO some code
		Set<String> dict = makeDictionary();
		ArrayList <String> dictionary = convertDict(dict);
	//	if(!(dictionary.contains(start) && (dictionary.contains(end)))){
	//		ArrayList<String> noSol = new ArrayList<String>();
	//		noSol.add(start);
	//		noSol.add(end);
	//		return noSol;
	//	}
		Graph g = new Graph();
		g.makeGraph(start,dictionary);
		HashMap<String, ArrayList<String>> adjlist = g.getAdjlist();

        if(!(isPath(start,end,adjlist))){
            ArrayList<String> noSol = new ArrayList<String>();
            noSol.add(start);
            noSol.add(end);
            return noSol;
        }
		Stack <String> wordladder = new Stack<String>();
		Stack <String> solution = new Stack <String>();
		ArrayList <String> marked = new ArrayList<String>();
		solution.push(start); //initial, all paths start from here
		helperDFS(start, end, adjlist, marked, wordladder,solution);

		ArrayList<String> sol = new ArrayList<String>(solution);
		if(sol.size() < 3){
			sol.clear();
			sol.add(0, start);
			sol.add(1, end);
		}



		return sol;


	}
	/**
	 * helper DFS function, uses recursion, uses stack to keep track of potential path, terminates when all words are marked
	 * @param vertex Strings for start word
	 * @param end String for end word
	 * @param adjlist Graph representation
	 * @param marked list to hold words that have already been visited
	 * @param wordladder Stack structure to hold the "current" path the DFS is traversing in the graph
	 * @param solution once the end vertex is found move the word ladder to the solution
	 *
	 */
	public static void helperDFS(String vertex , String end,HashMap<String, ArrayList<String>> adjlist, ArrayList<String> marked, Stack <String> wordladder, Stack <String> solution){

		marked.add(vertex);
		if(vertex.equals(end)){
		    for(int i =0; i<wordladder.size(); i++){
		        solution.add(wordladder.get(i));
            }

			return;
		}
		if(adjlist.get(vertex).size() == 0){

			return; //no kids
		}

		for(String word : adjlist.get(vertex)){
			if(marked.contains(word)){
				continue;
			}
			wordladder.push(word);
			helperDFS(word, end, adjlist,marked, wordladder,solution); //recursive call ends when a node has no children left
			wordladder.pop(); //back trace



		}

	}
	/**
	 * Creates graph and dictionary
	 * Calls isPath to make sure the words are in the dictionary
	 * if no path is found - returns an arraylist of start and end
	 * @param start Strings for start word
	 * @param end   Strings for end word
	 * @return ArrayList holding the word ladder
	 * BFS method to find the word ladder
	 *
	 *
	 */
	
    public static ArrayList<String> getWordLadderBFS(String start, String end) {

	    Queue <String> words = new LinkedList<String>(); //levels
	    ArrayList <String> discovered = new ArrayList<String>();
	    LinkedList <String> wordladder = new LinkedList<String>();
	    LinkedList <String> paths = new LinkedList<String>();
	    LinkedList <LinkedList<String>> allPaths = new LinkedList<LinkedList<String>>();
        Set<String> dict = makeDictionary();
        ArrayList <String> dictionary = convertDict(dict);


    //    if(!(dictionary.contains(start) && (dictionary.contains(end)))){
	//		ArrayList<String> noSol = new ArrayList<String>();
	//		noSol.add(start);
	//		noSol.add(end);
	//		return noSol;
	//	}
        Graph g = new Graph();
        g.makeGraph(start, dictionary);
        HashMap<String, ArrayList<String>> adjlist = g.getAdjlist();
       if(!(isPath(start,end,adjlist))){ //checks to make sure words are in the dictionary
              ArrayList<String> noSol = new ArrayList<String>();
              noSol.add(start);
              noSol.add(end);
              return noSol;
        }

        paths.add(start);
        allPaths.add(paths); //first path starts with the source node
        words.add(start); //all the words



        while(words.size() > 0){
            String vertex = words.poll(); //dequeue
            LinkedList <String> currentPath = allPaths.pop();
            if(vertex.equals(end)){
                wordladder = currentPath;
                break;
            }
            if(adjlist.get(vertex).size() > 0) {
				for (int k = 0; k < adjlist.get(vertex).size(); k++) { //discovering all the kids

					if (!(discovered.contains(adjlist.get(vertex).get(k)))) {

						LinkedList<String> updatePreviousPath = new LinkedList<String>(currentPath);
						updatePreviousPath.add(adjlist.get(vertex).get(k));
						allPaths.add(updatePreviousPath);
						words.add(adjlist.get(vertex).get(k)); //enqueue
					}
				}
			}
            discovered.add(vertex);

        }


		if(wordladder.size() <3 ){
        	wordladder.clear();
        	wordladder.add(0, start);
        	wordladder.add(1, end);
		}
		ArrayList <String> sol = new ArrayList<String>(wordladder);
        return sol;






	}



	/**
	 *  Prints the word ladder from the start word to end word and gives length of word ladder
	 * if no word ladder is present, print no word ladder is present
	 * @param ladder word ladder
	 *
	 *
	 */
	public static void printLadder(ArrayList<String> ladder) {
	    if(ladder.size() == 2){
	        System.out.println("no word ladder can be found between "+ ladder.get(0)+ " and " + ladder.get(1));
        }
        else{
	        System.out.println("a " + (ladder.size()-2) + "-rung word ladder exists between "+ ladder.get(0) + " and "+ ladder.get(ladder.size()-1));
            for(int i = 0; i<ladder.size(); i++){
                System.out.println(ladder.get(i));
            }
        }

		
	}
	// TODO
	// Other private static methods here

	/**
	 *
	 * @return Set containing dictionary
	 *
	 *
	 */
	/* Do not modify makeDictionary */
	public static Set<String>  makeDictionary () {
		Set<String> words = new HashSet<String>();
		Scanner infile = null;
		try {
			infile = new Scanner (new File("short_dict.txt"));
			//infile = new Scanner(new File("five_letter_words.txt"));
		} catch (FileNotFoundException e) {
			System.out.println("Dictionary File not Found!");
			e.printStackTrace();
			System.exit(1);
		}
		while (infile.hasNext()) {
			words.add(infile.next().toUpperCase());
		}
		return words;
	}
	/**
	 * converts the dictionary in Set type to a dictionary of ArrayList type
	 * @param dict Dictionary of Set type
	 * @return ArrayList holding the dictionary
	 *
	 *
	 *
	 */
	public static ArrayList<String> convertDict(Set<String> dict){
	    ArrayList <String> dictionary  = new ArrayList<String>(dict);

	    for(int i = 0; i<dictionary.size(); i++){
	        dictionary.set(i, dictionary.get(i).toLowerCase());

        }
	    Collections.sort(dictionary);
	    return dictionary;



    }
	/**
	 * checks to see if the start/end words are in the dictionary
	 * @param start Strings for start word
	 * @param end   Strings for end word
	 * @param adjlist Graph representation
	 * @return true if start/end words are in the dictionary
	 *
	 *
	 */
    public static boolean isPath(String start, String end, HashMap<String, ArrayList<String>> adjlist){
	    if((adjlist.keySet().contains(start)) && adjlist.keySet().contains(end)){
	        return true;
        }
        return false;
    }



}
