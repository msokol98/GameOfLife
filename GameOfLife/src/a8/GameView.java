package a8;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/*
 * This class is the view class of the application. It provides a visual interface for the user
 * to interact with the program through. This class includes both the cellboard and
 * interactive buttons and sliders that the user can manipulate to change aspects of the game.
 * The view reacts to these changes by notifying the controller class GameOfLifeController. 
 * The controller then responds as needed. The view will repaint when instructed to do so by the 
 * model class GenerationModel, and in the way that is required by the model.
 * 
 */

public class GameView extends JPanel implements ActionListener, CellListener, ChangeListener {

	/* Enum to identify player. */
	
	private enum Player {BLUE, GREEN};
	
	private JCellBoard board;		/* CellBoard playing area. */
	private JLabel message;		/* Label for messages. */
	private boolean setupDone;
	private JButton gameButton;
	private JButton randomButton;
	private JButton patternButton;
	private JButton torButton;
	private JButton resetButton;
	private JButton pauseButton;
	
	private JSlider lbt;
	private JLabel lbtText;
	private JSlider hbt;
	private JLabel hbtText;
	private JSlider lst;
	private JLabel lstText;
	private JSlider hst;
	private JLabel hstText;
	
	private JSlider delay;
	private JLabel delayText;
	private int initDelay;
	private int minDelay;
	private int maxDelay;
	
	private JSlider dimensionSlider;
	private JLabel dimensionText;
	private int initDimension;
	private int minDimension;
	private int maxDimension;
	
	private GameController theController;
	
	
	public GameView() {
				
		initDelay = 500;
		minDelay = 10;
		maxDelay = 	1000;
		
		setupDone = false;		
		/* Create CellBoard and message label. */
		
		initDimension = 30;
		minDimension = 10;
		maxDimension = 500; //want to change to 500 later
		
		board = new JCellBoard(initDimension,initDimension, new Color(1.0f, 1.0f, 1.0f), new Color(1.0f, 1.0f, 1.0f), JCellBoard.Pattern.SOLID);
		message = new JLabel();
		
		/* Set layout and place CellBoard at center. */
		
		setLayout(new BorderLayout());
		add(board, BorderLayout.CENTER);

		/* Create subpanel for message area and reset button. */

		/* Reset button. Add ourselves as the action listener. */
		
		JPanel controlButtons = new JPanel();
		controlButtons.setLayout(new BorderLayout());
		
		JPanel controlButtons1 = new JPanel();
		controlButtons1.setLayout(new BorderLayout());
		JPanel controlButtons2 = new JPanel();
		controlButtons2.setLayout(new BorderLayout());
		
		gameButton = new JButton("Start");
		gameButton.setActionCommand("Start");
		gameButton.addActionListener(this);
		controlButtons1.add(gameButton, BorderLayout.CENTER);
		
		randomButton = new JButton("Random");
		randomButton.setActionCommand("Random");
		randomButton.addActionListener(this);
		controlButtons2.add(randomButton, BorderLayout.EAST);

		patternButton = new JButton("Pattern");
		patternButton.setActionCommand("Pattern");
		patternButton.addActionListener(this);
		controlButtons2.add(patternButton, BorderLayout.CENTER);
		
		resetButton = new JButton("Clear Field");
		resetButton.setActionCommand("Reset");
		resetButton.addActionListener(this);
		controlButtons1.add(resetButton, BorderLayout.EAST);
		
		pauseButton = new JButton("Pause");
		pauseButton.setActionCommand("Pause");
		pauseButton.addActionListener(this);
		controlButtons.add(pauseButton, BorderLayout.EAST);
		pauseButton.setVisible(false);
		

		torButton = new JButton("Turn Torus Off");
		torButton.setActionCommand("TORUS");
		torButton.addActionListener(this);
		
		controlButtons.add(controlButtons1, BorderLayout.NORTH);
		controlButtons.add(torButton, BorderLayout.CENTER);
		controlButtons.add(controlButtons2, BorderLayout.SOUTH);

		delay = new JSlider(JSlider.HORIZONTAL,
                minDelay, maxDelay, initDelay);
		delay.addChangeListener(this);
		delay.setName("delaySlider");
		
		delayText = new JLabel();
		delayText.setText(Integer.toString(initDelay) + " millisecond delay");
		
		JPanel theDelay = new JPanel();
		theDelay.setLayout(new BorderLayout());
		
		theDelay.add(delayText, BorderLayout.CENTER);
		theDelay.add(delay, BorderLayout.WEST);
		
		JPanel dimensions = new JPanel();
		dimensions.setLayout(new BorderLayout());
		
		dimensionSlider = new JSlider(JSlider.HORIZONTAL,
                minDimension, maxDimension, initDimension);
		dimensionSlider.addChangeListener(this);
		dimensionSlider.setName("dimensionSlider");
		dimensions.add(dimensionSlider, BorderLayout.WEST);
		
		dimensionText = new JLabel();
		dimensionText.setText(Integer.toString(initDimension) + "x" + Integer.toString(initDimension) + " grid (resets cells on change)");
		dimensions.add(dimensionText, BorderLayout.CENTER);
		
		lbt = new JSlider(JSlider.HORIZONTAL, 1, 10, 3);
		lbt.setName("lbt");
		lbt.addChangeListener(this);
		lbtText = new JLabel("Low Birth Threshold: " + 3);
		hbt = new JSlider(JSlider.HORIZONTAL, 1, 10, 3);
		hbt.addChangeListener(this);
		hbt.setName("hbt");
		hbtText = new JLabel("High Birth Threshold: " + 3);
		lst = new JSlider(JSlider.HORIZONTAL, 1, 10, 2);
		lst.addChangeListener(this);
		lst.setName("lst");
		lstText = new JLabel("Low Surivive Threshold: " + 2);
		hst = new JSlider(JSlider.HORIZONTAL, 1, 10, 3);
		hst.setName("hst");
		hst.addChangeListener(this);
		hstText = new JLabel("High Surivive Threshold: " + 3);
		
		
		JPanel lbtAll = new JPanel();
		lbtAll.setLayout(new BorderLayout());
		JPanel hbtAll = new JPanel();
		hbtAll.setLayout(new BorderLayout());
		JPanel lstAll = new JPanel();
		lstAll.setLayout(new BorderLayout());
		JPanel hstAll = new JPanel();
		hstAll.setLayout(new BorderLayout());
		
		lbtAll.add(lbt , BorderLayout.WEST);
		lbtAll.add(lbtText, BorderLayout.CENTER);
		hbtAll.add(hbt , BorderLayout.WEST);
		hbtAll.add(hbtText, BorderLayout.CENTER);
		lstAll.add(lst , BorderLayout.WEST);
		lstAll.add(lstText, BorderLayout.CENTER);
		hstAll.add(hst , BorderLayout.WEST);
		hstAll.add(hstText, BorderLayout.CENTER);

		
		JPanel threshSliders = new JPanel();
		JPanel threshSlidersA = new JPanel();
		JPanel threshSlidersB = new JPanel();

		threshSliders.setLayout(new BorderLayout());
		threshSlidersA.setLayout(new BorderLayout());
		threshSlidersB.setLayout(new BorderLayout());

		threshSlidersA.add(lbtAll, BorderLayout.NORTH);
		threshSlidersA.add(hbtAll, BorderLayout.SOUTH);
		threshSlidersB.add(lstAll, BorderLayout.NORTH);
		threshSlidersB.add(hstAll, BorderLayout.SOUTH);
		
		threshSliders.add(threshSlidersA, BorderLayout.NORTH);
		threshSliders.add(threshSlidersB, BorderLayout.SOUTH);
		
		JPanel sliders = new JPanel();
		sliders.setLayout(new BorderLayout());
		sliders.add(dimensions, BorderLayout.CENTER);
		sliders.add(theDelay, BorderLayout.NORTH);
		sliders.add(threshSliders, BorderLayout.SOUTH);

		/* Add subpanel in south area of layout. */
		
		
		JPanel buttonsAndSliders = new JPanel();
		buttonsAndSliders.setLayout(new BorderLayout());
		buttonsAndSliders.add(sliders, BorderLayout.WEST);
		buttonsAndSliders.add(controlButtons, BorderLayout.EAST);
		
		add(buttonsAndSliders, BorderLayout.SOUTH);

		/* Add ourselves as a Cell listener for all of the
		 * Cells on the Cell board.
		 */
		board.addCellListener(this);

		/* controls game operation */
		theController = new GameController(board);
		
		/* Reset game. */
	}

	/* 

	 * 
	 * Resets the game by clearing all the Cells on the board,
	 * picking a new secret Cell, resetting game status fields, 
	 * and displaying start message.
	 * 
	 */

	private void resetGame() {
		
		setupDone = false;
		theController.setGameState(false);
		patternButton.setVisible(true);
		randomButton.setVisible(true);
		gameButton.setVisible(true);
		dimensionSlider.setVisible(true);
		pauseButton.setVisible(false);
		gameButton.setText("Start");
		pauseButton.setText("Pause");
		theController.reset();
		
		board.clearLiveCells();
		/* Clear all Cells on board. Uses the fact that CellBoard
		 * implements Iterable<Cell> to do this in a for-each loop.
		 */
		for (Cell s : board) {
			((JCell)s).clearCell();
		}
		for(Cell c: ((JCellBoard) board).getLiveCells()) {
			((JCell)c).clearCell();
		}
			
		board.trigger_update();
		/* Display game start message. */
		message.setText("Welcome to Conway's Game of Life.");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getActionCommand().contentEquals("Start")) {
			if(!setupDone) {
				setupDone = true;
				patternButton.setVisible(false);
				randomButton.setVisible(false);
				gameButton.setVisible(false);
				pauseButton.setVisible(true);
				startGame();
			}
		} else if(e.getActionCommand().contentEquals("Random")) {
			if(!setupDone) {
				resetGame();
				try {
					Thread.sleep(200);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				board.randomGen();
			} else {
				resetGame();
				try {
					Thread.sleep(200);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				board.randomGen();
			}
		} else if(e.getActionCommand().contentEquals("Pattern")) {
			resetGame();
			try {
				Thread.sleep(300);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			board.patternGen();
		} else if(e.getActionCommand().contentEquals("Reset")) {
			resetGame();
		} else if(e.getActionCommand().contentEquals("Pause")) {
			if(pauseButton.getText().contentEquals("Pause")) {
				pauseButton.setText("Resume");
			} else {
				pauseButton.setText("Pause");
			}
			theController.changeGameState();
		} else if(e.getActionCommand().contentEquals("TORUS")) {
			theController.changeTorus();
			if(theController.isTorus()) {
				torButton.setText("Turn Torus Off"); 
			} else {
				torButton.setText("Turn Torus On");
			}
		}
	} 

	/* Implementation of CellListener below. Implements game
	 * logic as responses to enter/exit/click on Cells.
	 */
	
	@Override
	public void CellClicked(Cell s) {
		
		/* If game already won, do nothing. */
		if (setupDone) {
			return;
		} 

		/* Flip current state. Keeps track of live cells */
		s.setState(s.getState() == 0 ? 1 : 0);
		if(s.getState() == 1) {
			board.addLiveCell(s);			
		} else {
			if(board.containsLiveCell(s)) {
				board.removeDeadCell(s);
			}
		}
		board.trigger_update();

	}

	
	private void startGame() {
		theController.setGameState(true);
		theController.startGame();
	}
	
	public void stateChanged(ChangeEvent e) {
	    JSlider source = (JSlider)e.getSource();
	    if(source.getName().contentEquals("delaySlider")) {
		    if (!source.getValueIsAdjusting()) {
		        int delay = (int)source.getValue();
		        
		        if(delay == 1000) {
			        delayText.setText(Integer.toString(1) + " second delay");
		        } else {
		        	delayText.setText(Integer.toString(delay) + " millisecond delay");
		        }
	
		        theController.setWaitTime(delay);
		    }
	    } else if(source.getName().contentEquals("dimensionSlider")){
	    	if (!source.getValueIsAdjusting()) {
	    		resetGame();
	    		int dimension = (int)source.getValue();
	    		dimensionText.setText(Integer.toString(dimension) + "x" + Integer.toString(dimension) + " grid (resets cells on change)");
	    		remove(board);
	    		board = new JCellBoard(dimension,dimension, new Color(1.0f, 1.0f, 1.0f), new Color(1.0f, 1.0f, 1.0f), JCellBoard.Pattern.SOLID);
	    		board.addCellListener(this);
	    		theController.swapBoard(board);
	    		add(board, BorderLayout.CENTER);
	    	}
	    } else if(source.getName().contentEquals("lbt")) {
	    	if(!source.getValueIsAdjusting()) {
	    		lbtText.setText("Low Birth Threshold: " + (int)source.getValue());
	    		theController.setLowBirthThresh((int)source.getValue());
	    	}
	    } else if(source.getName().contentEquals("hbt")) {
	    	if(!source.getValueIsAdjusting()) {
	    		hbtText.setText("High Birth Threshold: " + (int)source.getValue());
	    		theController.setHighBirthThresh((int)source.getValue());
	    	}
	    } else if(source.getName().contentEquals("lst")) {
	    	if(!source.getValueIsAdjusting()) {
	    		lstText.setText("Low Survive Threshold: " + (int)source.getValue());
	    		theController.setLowSurThresh((int)source.getValue());
	    	}
	    } else if(source.getName().contentEquals("hst")) {
	    	if(!source.getValueIsAdjusting()) {
	    		hstText.setText("High Survive Threshold: " + (int)source.getValue());
	    		theController.setHighSurThresh((int)source.getValue());
	    	}
	    }
	}
	
}