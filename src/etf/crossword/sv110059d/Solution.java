package etf.crossword.sv110059d;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Solution {
	
	static List<Solution> solutions;
	HashMap<WordVariable, String> solution;
	
	public Solution(HashMap<WordVariable, String> newSolution) {
		solution = newSolution;
	}
	
	static void init() {
		solutions = new LinkedList<Solution>();
	}
	
	public Solution() {
		solution = new HashMap<WordVariable, String>();
	}

	public static void addSolution(Solution s) {
		solutions.add(addAll(s));
	}
	
	public String get(WordVariable word) {
		if (solution.containsKey(word))
			return solution.get(word);
		return null;
	}

	private static Solution addAll(Solution s) {
		HashMap<WordVariable, String> newSolution = new HashMap<WordVariable, String>();
		newSolution.putAll(s.solution);
		return new Solution(newSolution);
	}
	
	public void add(WordVariable word, String val) {
		// TODO Auto-generated method stub
		solution.put(word, val);
	}

	public void remove(WordVariable word) {
		// TODO Auto-generated method stub
		solution.remove(word);
	}

	public boolean contains(WordVariable word) {
		// TODO Auto-generated method stub
		return solution.containsKey(word);
	}

}
