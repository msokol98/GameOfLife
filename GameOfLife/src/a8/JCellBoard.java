package a8;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

/*
 * JCellBoard is a user interface component that implements CellBoard.
 * This JPanel is encapsulated by the view class GameOfLifeView.
 * 
 * Uses CellBoardIterator to implement Iterable<Cell>
 * 
 */

public class JCellBoard extends JPanel implements CellBoard, MouseListener {

	public static enum Pattern {
		ALTERNATING, SOLID, STRIPES
	};
	
	private static int patternCount;

	private static final Color DEFAULT_BACKGROUND_LIGHT = new Color(0.8f, 0.8f, 0.8f);
	private static final Color DEFAULT_BACKGROUND_DARK = new Color(0.5f, 0.5f, 0.5f);
	private static final Color DEFAULT_CELL_COLOR = Color.WHITE;
	private static final Color DEFAULT_HIGHLIGHT_COLOR = Color.LIGHT_GRAY;

	private Cell[][] cells;
	private List<Cell> liveCells;
	private List<Cell> toRepaint;
	
	private List<CellListener> cellListeners;
	
	private Dimension preferredSize;


	public JCellBoard(int width, int height, Color lightColor, Color darkColor, Pattern pattern) {
		
		if (width < 1 || height < 1 || width > 500 || height > 500) {
			throw new IllegalArgumentException("Illegal Cell board geometry");
		}

		patternCount = 0;
		
		setLayout(new BorderLayout(width, height));
		setBackground(Color.BLACK);
		addMouseListener(this);
		cellListeners = new ArrayList<>();
		toRepaint = new ArrayList<>();

		if (cells == null && liveCells == null) {
			cells = new Cell[width][height];
			liveCells = new LinkedList<Cell>();

		}

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				Color bg = ((x + y) % 2 == 0) ? lightColor : darkColor;
				cells[x][y] = new JCell(bg, DEFAULT_CELL_COLOR, DEFAULT_HIGHLIGHT_COLOR, this, x, y);
				//((JCell) cells[x][y]).setPreferredSize(preferredSize);
				//add(((JCell) cells[x][y]));
			}
		}
		
		trigger_update();
	}

	public JCellBoard(int width, int height) {
		this(width, height, DEFAULT_BACKGROUND_LIGHT, DEFAULT_BACKGROUND_DARK, Pattern.ALTERNATING);
	}

	public JCellBoard(int width, int height, Pattern pattern) {
		this(width, height, DEFAULT_BACKGROUND_LIGHT, DEFAULT_BACKGROUND_DARK, Pattern.STRIPES);
	}

	// Getters for CellWidth and CellHeight properties

	@Override
	public int getCellWidth() {
		return cells.length;
	}

	@Override
	public int getCellHeight() {
		return cells[0].length;
	}

	// Lookup method for Cell at position (x,y)

	@Override
	public Cell getCellAt(int x, int y) {
		if (x < 0 || x >= getCellWidth() || y < 0 || y >= getCellHeight()) {
			throw new IllegalArgumentException("Illegal Cell coordinates");
		}

		return cells[x][y];
	}

	
	public void addCellListener(CellListener l) {
		cellListeners.add(l);
	}

	@Override
	public Iterator<Cell> iterator() {
		return new CellBoardIterator(this);
	}

	public void goToNextGen(Cell[][] cells) {
		this.cells = cells;
	}

	public JCellBoard getBoard() {
		return this;
	}

	public Cell[][] getCells() {
		return cells.clone();
	}

	public List<Cell> getLiveCells() {
		return liveCells;
	}

	public boolean containsLiveCell(Cell c) {
		return liveCells.contains(c);
	}

	public void addLiveCell(Cell c) {
		liveCells.add(c);
	}

	public void removeDeadCell(Cell c) {
		liveCells.remove(c);
	}

	public void clearLiveCells() {
		liveCells.clear();
	}
	
	@Override
    public void paintComponent(Graphics g) {
		
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

		
		/*int width = DEFAULT_SCREEN_WIDTH/getCellWidth();
		int height = DEFAULT_SCREEN_HEIGHT/getCellHeight();*/
		double width = getSize().getWidth()/ (double) getCellWidth();
		double height = getSize().getHeight()/ (double) getCellHeight();
				
		//int width = ((int)(getSize().getWidth()))/getCellWidth();
		//int height = ((int)(getSize().getHeight()))/getCellHeight();
				
		super.paintComponent(g2d);
		
		for(int col = 0; col < getCellWidth(); col++) {
			for(int row = 0; row < getCellHeight(); row++) {
								
				/*int x = col*width;
				int y = row*height;*/
				
				double x = col*width;
				double y = row*height;
				
				if(getCellAt(col, row).getState() == JCell.ALIVE) {
					g2d.setColor(Color.CYAN);
				} else {
					g2d.setColor(Color.BLACK);
				}
				//g2d.fillRect(x, y, width, height);
				g2d.fill(new Rectangle2D.Double(x, y, width, height));
			}
		}
	}
	
	public void trigger_update() {
		
		repaint();		
		new Thread(new Runnable() {
			public void run() {
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
				}
				repaint();
			}
		}).start();

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
		/*int width = DEFAULT_SCREEN_WIDTH/getCellWidth();
		int height = DEFAULT_SCREEN_HEIGHT/getCellHeight();*/
		
		double width = getSize().getWidth()/ (double) getCellWidth();
		double height = getSize().getHeight()/ (double) getCellHeight();
		
		int x = e.getX();
		int y = e.getY();
		
		double tempX2 = (x / width);
		int col = (int) (Math.floor(tempX2));
		
		double tempY2 = (y / height);
		int row = (int) (Math.floor(tempY2));

		for (CellListener c : cellListeners) {
			c.CellClicked(getCellAt(col, row));
		}
	
	}
	
	public void randomGen() {
		int counter = 1;
		for(int i = 0; i < getCellWidth(); i++) {
			for(int j = 0; j < getCellHeight(); j++) {
				double rand = Math.random();
				if(rand > .90) {
					counter++;
					getCellAt(i, j).setState(JCell.ALIVE);
					addLiveCell(getCellAt(i, j));
				}
			}
		}
		trigger_update();
	}
	
	public void patternGen() {
		
		int midX = (getCellWidth()/2)-1;
		int midY = (getCellHeight()/2)-1;
		Cell midCell = getCellAt(midX, midY);
		
		int patternID = patternCount % 4;
		switch(patternID) {
			case 0:
				midCell.setState(JCell.ALIVE);
				addLiveCell(midCell);
				getCellAt(midX-1, midY).setState(JCell.ALIVE);
				addLiveCell(getCellAt(midX-1, midY));
				getCellAt(midX+1, midY).setState(JCell.ALIVE);
				addLiveCell(getCellAt(midX+1, midY));
				getCellAt(midX, midY-1).setState(JCell.ALIVE);
				addLiveCell(getCellAt(midX-1, midY-1));
				getCellAt(midX, midY+1).setState(JCell.ALIVE);
				addLiveCell(getCellAt(midX-1, midY+1));
				break;
			case 1:
				getCellAt(midX-2, midY-2).setState(JCell.ALIVE);
				addLiveCell(getCellAt(midX-2, midY-2));
				getCellAt(midX-2, midY-1).setState(JCell.ALIVE);
				addLiveCell(getCellAt(midX-2, midY-1));
				getCellAt(midX-2, midY).setState(JCell.ALIVE);
				addLiveCell(getCellAt(midX-2, midY));
				getCellAt(midX-2, midY+1).setState(JCell.ALIVE);
				addLiveCell(getCellAt(midX-2, midY+1));
				getCellAt(midX-2, midY+2).setState(JCell.ALIVE);
				addLiveCell(getCellAt(midX-2, midY+2));
				
				getCellAt(midX+2, midY-2).setState(JCell.ALIVE);
				addLiveCell(getCellAt(midX+2, midY-2));
				getCellAt(midX+2, midY-1).setState(JCell.ALIVE);
				addLiveCell(getCellAt(midX+2, midY-1));
				getCellAt(midX+2, midY).setState(JCell.ALIVE);
				addLiveCell(getCellAt(midX+2, midY));
				getCellAt(midX+2, midY+1).setState(JCell.ALIVE);
				addLiveCell(getCellAt(midX+2, midY+1));
				getCellAt(midX+2, midY+2).setState(JCell.ALIVE);
				addLiveCell(getCellAt(midX+2, midY+2));
				
				getCellAt(midX, midY+2).setState(JCell.ALIVE);
				addLiveCell(getCellAt(midX, midY+2));
				getCellAt(midX, midY-2).setState(JCell.ALIVE);
				addLiveCell(getCellAt(midX, midY-2));
				break;
			case 2:
				getCellAt(midX, midY+1).setState(JCell.ALIVE);
				addLiveCell(getCellAt(midX, midY+1));
				getCellAt(midX, midY-1).setState(JCell.ALIVE);
				addLiveCell(getCellAt(midX, midY-1));
				getCellAt(midX-1, midY+1).setState(JCell.ALIVE);
				addLiveCell(getCellAt(midX-1, midY+1));
				getCellAt(midX+1, midY+1).setState(JCell.ALIVE);
				addLiveCell(getCellAt(midX+1, midY+1));
				getCellAt(midX+1, midY).setState(JCell.ALIVE);
				addLiveCell(getCellAt(midX+1, midY));
				break;
			case 3: 
				getCellAt(midX, midY-2).setState(JCell.ALIVE);
				addLiveCell(getCellAt(midX, midY-2));
				getCellAt(midX-1, midY-2).setState(JCell.ALIVE);
				addLiveCell(getCellAt(midX-1, midY-2));
				getCellAt(midX+1, midY-2).setState(JCell.ALIVE);
				addLiveCell(getCellAt(midX+1, midY-2));
				getCellAt(midX+2, midY-2).setState(JCell.ALIVE);
				addLiveCell(getCellAt(midX+2, midY-2));
				getCellAt(midX+2, midY-1).setState(JCell.ALIVE);
				addLiveCell(getCellAt(midX+2, midY-1));
				getCellAt(midX+2, midY).setState(JCell.ALIVE);
				addLiveCell(getCellAt(midX+2, midY));
				getCellAt(midX+1, midY+1).setState(JCell.ALIVE);
				addLiveCell(getCellAt(midX+1, midY+1));
				getCellAt(midX-2, midY-1).setState(JCell.ALIVE);
				addLiveCell(getCellAt(midX-2, midY-1));
				getCellAt(midX-2, midY+1).setState(JCell.ALIVE);
				addLiveCell(getCellAt(midX-2, midY+1));
				break;
		}
		patternCount++;
		trigger_update();
			
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
