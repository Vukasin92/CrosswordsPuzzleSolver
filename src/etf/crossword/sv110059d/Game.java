package etf.crossword.sv110059d;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Queue;

import javax.swing.JButton;
import javax.swing.JTextArea;

import etf.crossword.sv110059d.WordVariable.Direction;

public class Game {
	
	private HashMap<Integer, WordVariable> nodes;
	private ArrayList<WordVariable> words;
	private int numRows;
	private int numCols;
	private WordDictionary dictionary;
	private JTextArea textArea;
	private JButton[][] crossWordFields;
	private Queue<Tuple> set;
	
	Game(JTextArea textArea)
	{		
		this.textArea = textArea;
	}
	/**
	 * Initialize the game.
	 */
	public void Initialize(int numRows, int numCols, WordDictionary dictionary, JButton[][] crossWordFields) {
		this.numRows = numRows;
		this.numCols = numCols;
		this.dictionary = dictionary;
		this.crossWordFields = crossWordFields;
		this.nodes = new HashMap<Integer, WordVariable>();
		this.words = new ArrayList<WordVariable>();
		this.set = new LinkedList<Tuple>();
		findWordsAndConstraints();
	}
	
	private void findWordsAndConstraints() {
		for (int i=0; i<numRows; i++)
		{
			for (int j=0; j<numCols; j++)
			{
				WordVariable previous = null;
				if (crossWordFields[j][i].getBackground() != Color.BLACK)
				{
					if (j==0 || crossWordFields[j-1][i].getBackground() == Color.BLACK)
					{
						int count = 0;
						WordVariable wordVar = previous = new WordVariable(Direction.ACROSS, count, i, j, words.size());
						for (int k=j; k<numCols; k++)
						{
							if (crossWordFields[k][i].getBackground() != Color.BLACK)
							{
								count++;
								WordVariable neighbour = findNeighbour(Direction.DOWN, i, k);
								if (neighbour != null)
								{
									addNeighbours(wordVar, neighbour);
									addConstraints(wordVar, k, neighbour, i-neighbour.getRow());
								}
							}
							else
							{
								break;
							}
						}
						wordVar.setLength(count);
						wordVar.setDomain(dictionary.words[count-1]);
						nodes.put(i*numCols+j, wordVar);
						words.add(wordVar);
					}
					if (i==0 || crossWordFields[j][i-1].getBackground() == Color.BLACK)
					{
						int count = 0;
						WordVariable wordVar = new WordVariable(Direction.DOWN, count, i, j, words.size());
						for (int k=i; k<numRows; k++)
						{
							if (crossWordFields[j][k].getBackground() != Color.BLACK)
							{
								count++;
								WordVariable neighbour = findNeighbour(Direction.ACROSS, k, j);
								if (neighbour != null)
								{
									addNeighbours(wordVar, neighbour);
									addConstraints(wordVar, k, neighbour, j-neighbour.getCol());
								}
							}
							else
							{
								break;
							}
						}
						wordVar.setLength(count);
						wordVar.setDomain(dictionary.words[count-1]);
						nodes.put(i*numCols+j+numCols*numRows, wordVar);
						words.add(wordVar);
						if (previous != null) {
							addNeighbours(wordVar, previous);
							addConstraints(wordVar, 0, previous, 0);
						}
					}
				}
			}
		}
		for (WordVariable word : words) {
		    for (Entry<WordVariable, Integer> entry : word.constraints.entrySet())
		    {
		    	 set.add(new Tuple(word, entry.getKey()));
		    }
		}
	}
	private void addConstraints(WordVariable wordVar, int i,
			WordVariable neighbour, int j) { // j is neighbour's index of character that is crossed, i is wordVar's
		wordVar.addConstraint(neighbour, i);
		neighbour.addConstraint(wordVar, j);
	}
	private WordVariable findNeighbour(Direction direction, int startRow, int startCol) {
		if (direction == Direction.DOWN)
		{
			if (startRow == 0 || crossWordFields[startCol][startRow-1].getBackground() == Color.BLACK)
				return null;
			
			int i;
			for (i=startRow; i>0; i--)
			{
				if (crossWordFields[startCol][i-1].getBackground() == Color.BLACK)
				{
					break;
				}
			}
			return nodes.get(i*numCols+startCol+numCols*numRows);
		}
		if (direction == Direction.ACROSS)
		{
			if (startCol == 0 || crossWordFields[startCol-1][startRow].getBackground() == Color.BLACK)
				return null;
			
			int i;
			for (i=startCol; i>0; i--)
			{
				if (crossWordFields[i-1][startRow].getBackground() == Color.BLACK)
				{
					break;
				}
			}
			return nodes.get(startRow*numCols+i);
		}
		return null;
	}
	private void addNeighbours(WordVariable wordVar, WordVariable neighbour) {
		wordVar.addNeighbour(neighbour);
		neighbour.addNeighbour(wordVar);
	}
	
	public void checkWords() {
		for (WordVariable word : words) {
			System.out.println("index "+word.index+" row "+word.getRow()+" col "+word.getCol());
		    for (Entry<WordVariable, Integer> entry : word.constraints.entrySet())
		    {
		    	 System.out.println(entry.getKey().index + " " + entry.getKey().getRow() + " " + entry.getKey().getCol() + " = " + entry.getValue());
		    }
		}
	}
	public void solve() {
		// TODO Auto-generated method stub
		ac3();
	}
	private boolean ac3() {
		// TODO Auto-generated method stub
		while (!set.isEmpty()) {
			if (!next()) {
				return false;
			}
		}
		return true;
	}
	public boolean next() {
		// TODO Auto-generated method stub
		Tuple tuple = set.remove();
		WordVariable xi = tuple.getXi();
		WordVariable xj = tuple.getXj();
		if (constraintPropagation(xi, xj)) {
			if (xi.domain.isEmpty()) {
				return false;
			}
			else {
				for (WordVariable word : xi.neighbours) {
				    if (xj != word) {
				    	Tuple t = new Tuple(word, xi);
				    	if (!set.contains(t)) {
				    		set.add(t);
				    	}
				    }
				}
			}
		}
		return true;
	}
	private boolean constraintPropagation(WordVariable xi, WordVariable xj) {
		boolean change = false;
		List<String> toRemove = new LinkedList<String>();
		for (String u : xi.domain) {
			boolean exists = false;
			for (String v : xj.domain) {
				if (satisfy(xi, xj, u, v)) {
					exists = true;
					break;
				}
			}
			if (!exists) {
				change = true;
				toRemove.add(u);
			}
		}
		for (String s : toRemove) {
			xi.domain.remove(s);
		}
		return change;
	}
	private boolean satisfy(WordVariable xi, WordVariable xj, String u, String v) {
		int idxi = xi.constraints.get(xj);
		int idxj = xj.constraints.get(xi);
		if (u.charAt(idxi) == v.charAt(idxj)) {
			return true;
		}
		else {
			return false;
		}
	}
	public void checkSolution() {
		for (WordVariable word : words) {
			System.out.println("index "+word.index+" row "+word.getRow()+" col "+word.getCol());
		    for (String u: word.domain)
		    {
		    	 System.out.println(u);
		    }
		}
	}
}
