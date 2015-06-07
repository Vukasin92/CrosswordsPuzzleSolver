package etf.crossword.sv110059d;

import java.util.ArrayList;

public class WordDictionary {
	private static final int MAX_LENGTH = 5;
	
	@SuppressWarnings("unchecked")
	ArrayList<String>[] words = (ArrayList<String>[])new ArrayList[MAX_LENGTH];
	
	WordDictionary() 
	{
		for (int i = 0; i < MAX_LENGTH; i++) {
			words[i] = new ArrayList<String>();
		}
	}
	
	boolean addWord(String word)
	{
		int len = word.length();
		if (len<1 || len>MAX_LENGTH)
			return false;
		word = word.toUpperCase();
		if (!words[len-1].contains(word))
		{
			words[len-1].add(word);
		}
		return true;
	}
}
