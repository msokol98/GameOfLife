package a8;
/*
 *  This class represents the controller of the game. It acts as the middleman
 *  when the view wants to interact with model. The model exposes the core
 *  app functionality of the game, so we do not want our view to interact with it 
 *  directly. This is a simple game, so the controller does not have a lot to do 
 *  besides send commands from the view toward the model.
 */


public class GameController {
	
	private GenerationModel model;
	
	public void startGame() {
		Thread gameThread = new Thread(model);
		gameThread.start();
	}
	
	public GameController(JCellBoard board) {
		model = new GenerationModel(board);
	}

	public void setLowSurThresh(int value) {
		// TODO Auto-generated method stub
		model.setLowSurThresh(value);
	}

	public void setHighSurThresh(int value) {
		// TODO Auto-generated method stub
		model.setHighSurThresh(value);
	}

	public void setHighBirthThresh(int value) {
		// TODO Auto-generated method stub
		model.setHighBirthThresh(value);
	}

	public void setLowBirthThresh(int value) {
		// TODO Auto-generated method stub
		model.setLowBirthThresh(value);
	}

	public void swapBoard(JCellBoard board) {
		// TODO Auto-generated method stub
		model.swapBoard(board);
	}

	public void setWaitTime(int delay) {
		// TODO Auto-generated method stub
		model.setWaitTime(delay);
	}

	public void setGameState(boolean b) {
		// TODO Auto-generated method stub
		model.setGameState(b);
	}

	public void changeGameState() {
		// TODO Auto-generated method stub
		model.changeGameState();
	}

	public void changeTorus() {
		// TODO Auto-generated method stub
		model.changeTorus();		
	}

	public boolean isTorus() {
		// TODO Auto-generated method stub
		return model.isTorus();
	}

	public void reset() {
		// TODO Auto-generated method stub
		model.reset();
	}

}
