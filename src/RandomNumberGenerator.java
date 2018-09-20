import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class RandomNumberGenerator extends JFrame {

	private static final long serialVersionUID = 1L;
	private static final int WIDTH = 400;
	private static final int HEIGHT = 500;
	String title = "Random";
	Color color = Color.LIGHT_GRAY;
	JPanel panelSouth = new JPanel();
	JPanel panelEast = new JPanel();
	JPanel panelEastUp = new JPanel();
	JPanel panelEastCenter = new JPanel();
	JButton random = new JButton("random");
//	JButton mark = new JButton("mark");
	JButton log = new JButton("log");
	JButton clear = new JButton("clear");
	JTextArea jtaCenter = new JTextArea();
	List<JCheckBox> boxesList = new LinkedList<>();

	public static void main(String args[]) {
		new RandomNumberGenerator().launchFrame();
	}
	
	public void launchFrame() {
		this.setSize(WIDTH, HEIGHT);
		this.setTitle(title);
		this.getContentPane().setBackground(color);
		this.setLayout(new BorderLayout());
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		southPanel();
		centerPanel();
		eastPanel();
		this.add(panelEast, BorderLayout.EAST);
		this.add(panelSouth, BorderLayout.SOUTH);
		this.setVisible(true);
	}
	
	private void eastPanel() {
//		boxesList.add(new JCheckBox("all"));
//		boxesList.add(new JCheckBox("sdfd"));
//		boxesList.add(new JCheckBox("klkl"));
		
		panelEast.setLayout(new BorderLayout());
//sort from up to down
		panelEastCenter.setLayout(new BoxLayout(panelEastCenter, BoxLayout.PAGE_AXIS));
		
		panelEastUp.add(new JRadioButton("Select All"));
		
		for(int i = 0; i < boxesList.size(); i++) {
			JCheckBox tmpBox = boxesList.get(i);
			panelEastCenter.add(tmpBox);
		}
		
		panelEast.add(panelEastUp, BorderLayout.NORTH);
		panelEast.add(new JScrollPane(panelEastCenter), BorderLayout.CENTER);
		
	}
	
	private void centerPanel() { //add directly without support by JPanel
		add(new JScrollPane(jtaCenter), BorderLayout.CENTER);
	}
	
	private void southPanel() {
		panelSouth.setLayout(new GridLayout(1, 3));
		panelSouth.add(random);
//		panelSouth.add(mark);
		panelSouth.add(log);
		panelSouth.add(clear);
	}
	
}
