package a8;
import java.awt.Color;

/*
 * GenerationModel contains the logic of the game. As per the classic MVC pattern,
 * the model encapsulates the state of the application and controls the logic 
 * behind it. For this game, the state of the application is essentially the state
 * of the board. Moreover, how the next generation is formed is determined here.
 * When there is a change in state, the model notifies the view, which is then able to 
 * repaint the cells of the game.
 * 
 */
import java.util.List;

public class GenerationModel implements Runnable {

	private CellBoard board;
	private boolean setupDone;
	private int waitTime;
	private boolean stopped;
	
	private static int DEFAULT_LOW_BIRTH_THRESHOLD = 3;
	private static int DEFAULT_HIGH_BIRTH_THRESHOLD = 3;
	private static int DEFAULT_LOW_SURVIVE_THRESHOLD = 2;
	private static int DEFAULT_HIGH_SURVIVE_THRESHOLD = 3;
	
	private int lowBirthThresh;
	private int highBirthThresh;
	private int lowSurThresh;
	private int highSurThresh;
	boolean torus;
		
	public GenerationModel(CellBoard board) {
		this(board, 500);
		stopped = false;
	}
	
	public void swapBoard(CellBoard board) {
		this.board = board;
	}
	
	public void reset() {
		stopped = false;
	}
	
	public GenerationModel(CellBoard board, int waitTime) {
		this.board = board;
		this.waitTime = waitTime;
		setupDone = true;
		torus = true;
		
		lowBirthThresh = DEFAULT_LOW_BIRTH_THRESHOLD;
		highBirthThresh = DEFAULT_HIGH_BIRTH_THRESHOLD;
		lowSurThresh = DEFAULT_LOW_SURVIVE_THRESHOLD;
		highSurThresh = DEFAULT_HIGH_SURVIVE_THRESHOLD;
		
	}
	
	public void changeTorus() {
		if(!torus) {
			torus = true;
		} else {
			torus = false;
		}
	}
	
	public boolean isTorus() {
		return torus;
	}
	
	public void setWaitTime(int delay) {
		waitTime  = delay;
	}
	
	public void setGameState(boolean gameState) {
		setupDone = gameState;
	}
	
	public void changeGameState() {
		if(stopped) {
			stopped = false;
		} else {
			stopped = true;
		}
	}
	
	private void advanceGen() {
		
		if(stopped) {
			return;
		}
				
		List<Cell> liveCells = ((JCellBoard) board).getLiveCells();
		
		int minX = Integer.MAX_VALUE, maxX = Integer.MIN_VALUE, minY = Integer.MAX_VALUE, maxY = Integer.MIN_VALUE;
		
		for(Cell c: liveCells) {
			int x = c.getCellCol();
			int y = c.getCellRow();
			
			if(x < minX) {
				minX = x;
			}
			if(x > maxX) {
				maxX = x;
			}
			if(y < minY) {
				minY = y;
			}
			if(y > maxY) {
				maxY = y;
			}
		}
		
		int cols = board.getCellWidth();
		int rows = board.getCellHeight();
		
		int[][] nextGenStates = new int[cols][rows];
		
		int delta = 2;
		for(int x = minX-delta; x < maxX+delta; x++) {
			for(int y = minY-delta; y < maxY+delta; y++) {
				
				if(!torus) {
					if(x == 0 || x == cols-1 || y == 0 || y == rows-1) {
						try {
							nextGenStates[x][y] = board.getCellAt(x, y).getState();
						} catch (ArrayIndexOutOfBoundsException e) {
						} catch(IllegalArgumentException e2) {
						}
						continue;
					}
				}
				
				
				int i = (x + cols) % cols;
				int j = (y + rows) % rows;
				
				Cell currentCell = board.getCellAt(i, j);
				int currentState = currentCell.getState();
				int nextGenState = nextGenStates[i][j];
				
					int neighbors = countNeighbors(i, j, cols, rows);
					
					if(currentState == JCell.DEAD && neighbors >= lowBirthThresh && neighbors <= highBirthThresh) {
						nextGenState = JCell.ALIVE;
					} else if(currentState == JCell.ALIVE && (neighbors < lowSurThresh || neighbors > highSurThresh)) {
						nextGenState = JCell.DEAD;
					} else {
						currentCell.setState(currentState);
						nextGenState = currentState;
					}		
				nextGenStates[i][j] = nextGenState;	
			}
		}
		updateNextGenStates(rows, cols, nextGenStates);
	}
	
	
	private int countNeighbors(int x, int y, int cols, int rows) {
		int sum = 0;
		sum -= board.getCellAt(x, y).getState();
		for(int i = -1; i < 2; i++) {
			for(int j = -1; j < 2; j++) {
									
				int col; 
				int row; 
				
				col = (x+i+cols) % cols;
				row = (y+j+rows) % rows;
				
				sum+= board.getCellAt(col, row).getState();
				
				if(sum > 3) {
					return sum;
				}
			}
		}
		
		return sum;
	}

	@Override
	public void run() {
		int i = 0;
		while(setupDone) {
			try {
				Thread.sleep(waitTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			advanceGen();
		}
	}
	
	private void updateNextGenStates(int rows, int cols, int[][] states) {
		for(int i = 0; i < cols; i++) {
			for(int j = 0; j < rows; j++) {
				Cell c = board.getCellAt(i, j);
				int state = states[i][j];
				c.setState(state);
				switch(state) {
					case JCell.DEAD: {
						((JCellBoard) board).removeDeadCell(c);
						break;
					}
					case JCell.ALIVE: {
						((JCellBoard) board).addLiveCell(c);
						break;
					}
				}
			}
		}
		((JCellBoard) board).trigger_update();
	}
	
	public void setLowBirthThresh(int val) {
		lowBirthThresh = val;
	}
	public void setHighBirthThresh(int val) {
		highBirthThresh = val;
	}
	public void setLowSurThresh(int val) {
		lowSurThresh = val;
	}
	public void setHighSurThresh(int val) {
		highSurThresh = val;
	}
	
}

