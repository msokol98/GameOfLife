package a8;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class CellBoardIterator implements Iterator<Cell> {

	private CellBoard board;
	private int x;
	private int y;
	
	public CellBoardIterator(CellBoard board) {
		this.board = board;
		x = 0;
		y = 0;
	}

	@Override
	public boolean hasNext() {
		return (y < board.getCellHeight());
	}

	@Override
	public Cell next() {
		if (!hasNext()) {
			throw new NoSuchElementException();
		}
		Cell s = board.getCellAt(x, y);
		if (x < board.getCellWidth()-1) {
			x++;
		} else {
			x = 0;
			y++;
		}
		return s;
	}

}