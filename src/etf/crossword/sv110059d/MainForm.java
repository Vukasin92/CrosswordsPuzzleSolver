package etf.crossword.sv110059d;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class MainForm {

	private JFrame frame;
	private JTextArea textArea;
	private Game game;
	private JButton btnLoadWords;
	private JButton btnNext;
	private JButton btnSolve;
	private JButton[][] crossWordFields;
	private int numRows = 6;
	private int numCols = 5;
	private int newNumRows = 6;
	private int newNumCols = 5;
	private JPanel panel_1;
	private JPanel panel;
	private File dictionaryFile = null;
	private WordDictionary dictionary = null;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainForm window = new MainForm();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainForm() {
		game = new Game(textArea);
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setMinimumSize(new Dimension(760, 500));
		frame.setBounds(100, 100, 760, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		panel = new JPanel();

		panel.setBorder(new EmptyBorder(5, 5, 5, 5));
		panel.setPreferredSize(new Dimension(10, 140));
		frame.getContentPane().add(panel, BorderLayout.SOUTH);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_panel.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
		gbl_panel.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		JButton btnNewGame = new JButton("New game");
		btnNewGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(frame, "Choose black squares and load words once you are done.");
				dictionaryFile = null;
				for (int i=0; i<numRows; i++)
				{
					for (int j=0; j<numCols; j++)
					{
						panel_1.remove(crossWordFields[j][i]);
					}
				}
				numRows = newNumRows;
				numCols = newNumCols;
				btnLoadWords.setEnabled(true);
				btnNext.setEnabled(false);
				btnSolve.setEnabled(false);
				drawCrosswordGrid(false);
				panel_1.revalidate();
				panel_1.repaint();
			}
		});
		GridBagConstraints gbc_btnNewGame = new GridBagConstraints();
		gbc_btnNewGame.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewGame.gridx = 3;
		gbc_btnNewGame.gridy = 0;
		panel.add(btnNewGame, gbc_btnNewGame);
		
		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridheight = 5;
		gbc_scrollPane.gridwidth = 20;
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 5;
		gbc_scrollPane.gridy = 0;
		panel.add(scrollPane, gbc_scrollPane);
		
		textArea = new JTextArea();
		textArea.setBackground(Color.WHITE);
		textArea.setEditable(true);
		scrollPane.setViewportView(textArea);
		textArea.setColumns(10);
		
		btnLoadWords = new JButton("Load words");
		btnLoadWords.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ButtonGroup group = new ButtonGroup();
				JRadioButton option1 = new JRadioButton("Type manually");
				JRadioButton option2 = new JRadioButton("Load from file");
				
				
				group.add(option1);
				group.add(option2);
				group.setSelected(option1.getModel(), true);
				final JPanel myPanel = new JPanel();
				GridBagLayout layout = new GridBagLayout();
				myPanel.setLayout(layout);
				GridBagConstraints c = new GridBagConstraints();
				c.gridx = 0;
				c.gridy = 0;
		        myPanel.add(option1, c);
		        c.gridx = 10;
		        myPanel.add(option2, c);

		        JScrollPane scrollPane = new JScrollPane();
		        final JTextArea textArea_1 = new JTextArea();
		        scrollPane.setViewportView(textArea_1);
		        textArea_1.setEditable(true);
		        textArea_1.setColumns(10);
		        textArea_1.setRows(5);
		        c.gridy = 10;
		        c.gridx = 0;
		        myPanel.add(scrollPane, c);
		        
		        final JButton btnLoad = new JButton("Browse file");
		        btnLoad.setEnabled(false);
		        c.gridx = 10;
		        myPanel.add(btnLoad, c);
		        
		        option1.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						textArea_1.setEditable(true);
						btnLoad.setEnabled(false);
					}
		        });
		        
		        option2.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						textArea_1.setEditable(false);
						btnLoad.setEnabled(true);
					}
		        });
		        
		        final JFileChooser fc = new JFileChooser();
		        btnLoad.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						int returnVal = fc.showOpenDialog(myPanel);
						
						if (returnVal == JFileChooser.APPROVE_OPTION) {
				            dictionaryFile = fc.getSelectedFile();
				        } else {
				        }
					}
		        });
		        
		       
		        int result = JOptionPane.showConfirmDialog(null, myPanel, 
		                 "Please load words.", JOptionPane.OK_CANCEL_OPTION);
		        if (result == JOptionPane.OK_OPTION) {
		        	
					
					//load words
					if (option1.isSelected())
					{
						if (createDictionary(textArea_1.getText()))
						{
							startGame();
						}
						
					}
					else if (option2.isSelected())
					{
						if (createDictionary(dictionaryFile))
						{
							startGame();
						}
					}
				}
		    }
		});
		btnLoadWords.setEnabled(false);
		GridBagConstraints gbc_btnLoadWords = new GridBagConstraints();
		gbc_btnLoadWords.insets = new Insets(0, 0, 5, 5);
		gbc_btnLoadWords.gridx = 3;
		gbc_btnLoadWords.gridy = 1;
		panel.add(btnLoadWords, gbc_btnLoadWords);
		
		btnNext = new JButton("Next");
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				game.next();
			}
		});
		btnNext.setEnabled(false);
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnNewButton.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton.gridx = 3;
		gbc_btnNewButton.gridy = 2;
		panel.add(btnNext, gbc_btnNewButton);
		
		btnSolve = new JButton("Solve");
		btnSolve.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				game.solve();
				game.checkSolution();
			}
		});
		btnSolve.setEnabled(false);
		GridBagConstraints gbc_btnNewButton_1 = new GridBagConstraints();
		gbc_btnNewButton_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnNewButton_1.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton_1.gridx = 3;
		gbc_btnNewButton_1.gridy = 3;
		panel.add(btnSolve, gbc_btnNewButton_1);
		
		panel_1 = new JPanel();
		panel_1.setBorder(null);
		frame.getContentPane().add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new GridBagLayout());
		
		drawCrosswordGrid(true);
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmSetSize = new JMenuItem("Set size");
		mnFile.add(mntmSetSize);
		mntmSetSize.addActionListener(new ActionListener() {

		    @Override
		    public void actionPerformed(ActionEvent arg0) {
		    	 Integer[] possibilities = {1, 2, 3, 4, 5};
		    	 JComboBox<Integer> rowsCombo = new JComboBox<Integer>(possibilities);
		    	 JComboBox<Integer> colsCombo = new JComboBox<Integer>(possibilities);
		    	 rowsCombo.setSelectedIndex(4);
		    	 colsCombo.setSelectedIndex(4);
		    	 
		         JPanel myPanel = new JPanel();
		         myPanel.add(new JLabel("Number of rows:"));
		         myPanel.add(rowsCombo);
		         myPanel.add(Box.createHorizontalStrut(15)); // a spacer
		         myPanel.add(new JLabel("Number of columns:"));
		         myPanel.add(colsCombo);

		         int result = JOptionPane.showConfirmDialog(null, myPanel, 
		                  "Please enter number of rows and columns.", JOptionPane.OK_CANCEL_OPTION);
		         if (result == JOptionPane.OK_OPTION) {
		            newNumRows = possibilities[rowsCombo.getSelectedIndex()];
		            newNumCols = possibilities[colsCombo.getSelectedIndex()];
		         }
		    }
		});
		
		JMenuItem mntmClose = new JMenuItem("Close");
		mnFile.add(mntmClose);
		mntmClose.addActionListener(new ActionListener() {

		    @Override
		    public void actionPerformed(ActionEvent arg0) {
		        System.exit(0);
		    }
		});
	}

	protected void startGame() {
		btnLoadWords.setEnabled(false);
		btnNext.setEnabled(true);
		btnSolve.setEnabled(true);
		for (int i=0; i<numRows; i++)
		{
			for (int j=0; j<numCols; j++)
			{
				crossWordFields[j][i].setEnabled(false);
			}
		}
		game.Initialize(numRows, numCols, dictionary, crossWordFields);
		//game.checkWords();
	}

	protected boolean createDictionary(String text) {
		dictionary = new WordDictionary();
		String[] words = text.split("\n");
		for (String word : words)
		{
			dictionary.addWord(word);
		}
		return true;
	}

	protected boolean createDictionary(File dictionaryFile) {
		if (dictionaryFile == null)
			return false;
		dictionary = new WordDictionary();
		try (BufferedReader reader = new BufferedReader(new FileReader(dictionaryFile))){
		      String word = null;
		      while ((word = reader.readLine()) != null) {
		    	  dictionary.addWord(word);
		      }      
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return true;
	}

	protected void drawCrosswordGrid(boolean disableButtons) {
		GridBagConstraints c = new GridBagConstraints();
		crossWordFields = new JButton[numCols][numRows];
		for (int i=0; i<numRows; i++)
		{
			for (int j=0; j<numCols; j++)
			{
				final JButton btn = new JButton();
				btn.setEnabled(!disableButtons);
				btn.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						//change color
						Color color = btn.getBackground();
						if (color == Color.WHITE)
							btn.setBackground(Color.BLACK);
						else
							btn.setBackground(Color.WHITE);
					}
				});
				btn.setOpaque(true);
				btn.setPreferredSize(new Dimension(100, 100));
				btn.setMinimumSize(new Dimension(50, 50));
				btn.setBackground(Color.WHITE);
				btn.setBorder(new LineBorder(new Color(0, 0, 0), 1));
				c.gridx = j;
				c.gridy = i;
				panel_1.add(btn, c);
				crossWordFields[j][i] = btn;
			}
		}
	}

}
