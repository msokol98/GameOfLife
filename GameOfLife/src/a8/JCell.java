package a8;

import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.BorderFactory;

/*
 * JCell
 * 
 * A custom user interface component that implements a Cell on a Cell board
 * as an extension of JPanel.
 * 
 * A JCell acts as its own mouse listener and will translate mouse events into
 * notifications to registered CellListener objects when the Cell is entered,
 * exited, or clicked using the appropriate methods (see CellListener).
 *
 */

public class JCell /*extends JPanel implements MouseListener,*/ implements Cell {

	public static final int DEAD = 0;
	public static final int ALIVE = 1;
	private int state;

	private boolean isEmpty;
	private CellBoard board;
	private int row;
	private int col;


	public JCell(Color background, Color cellColor, Color highlight, 
			CellBoard board, int x, int y) {

		// Background color inherited from JPanel
		//setBackground(background);

		this.state = DEAD;
		this.board = board;
		
		col = x;
		row = y;

	}

	// Getters for state, X, Y, and Board properties

	@Override
	public int getState() {
		// TODO Auto-generated method stub
		return state;
	}

	@Override
	public void setState(int state) {
		// TODO Auto-generated method stub
		this.state = state;
	}
	
	@Override
	public int getCellRow() {
		return row;
	}

	@Override
	public int getCellCol() {
		return col;
	}

	@Override
	public CellBoard getBoard() {
		return board;
	}

	// Empty / Filled status methods

	@Override
	public boolean isEmpty() {
		return isEmpty;
	}

	public void clearCell() {
		isEmpty = true;
		state = DEAD;
	}



}
