package snake;

import javax.swing.*;

public class MainWindow extends JFrame {
	
	public MainWindow() {
		add(new GameField());
		setTitle("Snake");
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setSize(352, 372);
		setLocation(400, 400);
		setVisible(true);
	}

	public static void main(String[] args) {
		MainWindow mw = new MainWindow();
	}
	
}
