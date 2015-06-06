package etf.crossword.sv110059d;

import java.util.ArrayList;
import java.util.HashMap;

public class WordVariable {
	
	enum Direction {
		ACROSS,
		DOWN
	}
	
	int length;
	Direction direction;
	ArrayList<WordVariable> neighbours = new ArrayList<WordVariable>();
	HashMap<WordVariable, Integer> constraints = new HashMap<WordVariable, Integer>();
	ArrayList<String> domain = new ArrayList<String>();
	private int row;
	private int col;
	int index;
	
	WordVariable(Direction direction, int length, int row, int col, int index)
	{
		this.direction = direction;
		this.length = length;
		this.row = row;
		this.col = col;
		this.index = index;
	}
	
	void addNeighbour(WordVariable w)
	{
		if (!neighbours.contains(w))
		{
			neighbours.add(w);
		}
	}
	
	void addConstraint(WordVariable w, int crossingIndex) // crossingIndex is index from the beginning of the this word that w is crossing
	{
		
		if (!constraints.containsKey(w))
		{
			constraints.put(w, crossingIndex);
		}
	}

	public ArrayList<String> getDomain() {
		return domain;
	}

	public void setDomain(ArrayList<String> domain) {
		this.domain.addAll(domain);
	}

	void setLength(int count) {
		this.length = count;
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}
}
