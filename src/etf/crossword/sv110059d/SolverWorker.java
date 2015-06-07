package etf.crossword.sv110059d;

import java.util.Collections;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.ListSelectionModel;
import javax.swing.SwingWorker;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

class SolverWorker extends SwingWorker<Integer, Integer>
{
	private Game game;
	private JButton btnAbort;

	public SolverWorker(Game game, JButton btnAbort) {
		this.game = game;
		this.btnAbort = btnAbort;
	}
	
    protected Integer doInBackground() throws Exception
    {
    	game.wordsSolutions.addAll(game.words);
		Collections.sort(game.wordsSolutions, new WordComparator());
		game.backtrack(0);
        return 0;
    }

    protected void done()
    {
        try
        {
        }
        catch (Exception e)
        {
            //e.printStackTrace();
        }
        finally
        {
        	game.isCancelled = true;
        	btnAbort.setEnabled(false);
        	if (game.measure) {
        		long endTime = System.nanoTime();
				game.textArea.append("Execution time : "+((endTime-game.startTime)*1.0/1000/1000)+" ms.\n");
        	}
        	game.textArea.append("Found "+ Solution.solutions.size() +" solution(s)!\n");
        	DefaultListModel model = new DefaultListModel();
    		for (int i=0; i<Solution.solutions.size(); i++) {
    			model.addElement("Solution "+(i+1));
    		}
    		ListSelectionModel listSelectionModel = game.solutionList.getSelectionModel();
    		listSelectionModel.addListSelectionListener(
                    new ListSelectionListener() {

    					@Override
    					public void valueChanged(ListSelectionEvent arg0) {
    						int index = game.solutionList.getSelectedIndex();
    						if (index>=0)
    							game.displaySolution(index);
    					}
                    	
                    });
    		game.solutionList.setModel(model);
    		game.solutionList.revalidate();
    		game.solutionList.repaint();
        }
    }
}
