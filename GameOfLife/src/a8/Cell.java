package a8;

public interface Cell {

	
	int getState();
	void setState(int state);
	
	int getCellRow();
	int getCellCol();
	CellBoard getBoard();

	boolean isEmpty();


}