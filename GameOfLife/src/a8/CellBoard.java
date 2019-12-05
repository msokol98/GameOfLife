package a8;
import java.util.Iterator;

/* 
 * CellBoard
 * 
 * A 2D field of Cells.
 * 
 * getCellWidth() and getCellHeight() retrieve geometry of CellBoard.
 * 
 * The upper left Cell is Cell (0,0) and the lower right
 * Cell is considered (getCellWidth()-1, getCellHeight()-1).
 * 
 * The method getCellAt(int x, int y) will return the Cell at position (x,y).
 * Throws IllegalArgumentException if x or y is illegal.
 * 
 * The CellBoard provides convenience methods addCellListener() and removeCellListener()
 * for CellListeners to register / deregister with all of the Cells on the board.
 * 
 * CellBoard extends Iterable<Cell> meaning the method iterator()
 * will provide an iterator of type Iterator<Cell> that will traverse each
 * Cell on the board (order up to implementation).
 * 
 */


public interface CellBoard extends Iterable<Cell> {

	int getCellWidth();
 
	int getCellHeight();

	Cell getCellAt(int x, int y);

	Iterator<Cell> iterator();

}