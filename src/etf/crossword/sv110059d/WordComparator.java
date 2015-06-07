package etf.crossword.sv110059d;

import java.util.Comparator;

public class WordComparator implements Comparator<WordVariable> {

	@Override
	public int compare(WordVariable arg0, WordVariable arg1) {
		if (arg0.domain.size() < arg1.domain.size())
			return 1;
		else if (arg0.domain.size() > arg1.domain.size())
			return -1;
		else
			return 0;
	}
	
}
