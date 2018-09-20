import java.awt.Color;

import javax.swing.JFrame;

public class RandomNumberGenerator extends JFrame {

	private static final long serialVersionUID = 1L;
	private static final int WIDTH = 400;
	private static final int HEIGHT = 500;
	private String title = "Random";
	private Color color = Color.LIGHT_GRAY;
	
	public static void main(String args[]) {
		new RandomNumberGenerator().launchFrame();
	}
	
	public void launchFrame() {
		this.setSize(WIDTH, HEIGHT);
		this.setTitle(title);
		this.getContentPane().setBackground(color);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	
}
