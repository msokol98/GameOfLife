package a8;
import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class GameOfLife {
	public static void main(String[] args) {
		
		/* Create top level window. */
		
		JFrame main_frame = new JFrame();
		main_frame.setTitle("Conway's Game of Life");
		main_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		/* Create panel for content. Uses BorderLayout. */
		JPanel top_panel = new JPanel();
		top_panel.setLayout(new BorderLayout());
		main_frame.setContentPane(top_panel);

		
		GameView ttt = new GameView();
		top_panel.add(ttt, BorderLayout.CENTER);


		/* Pack main frame and make visible. */
		
		main_frame.setPreferredSize(new Dimension(700, 630));
		main_frame.pack();
		//main_frame.setResizable(false);
		main_frame.setVisible(true);		
	}
}