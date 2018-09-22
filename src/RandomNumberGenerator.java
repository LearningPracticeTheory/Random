import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

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
	JButton jbRandom = new JButton("random");
//	JButton mark = new JButton("mark");
	JButton jbLog = new JButton("log");
	JButton jbClear = new JButton("clear");
	JTextArea jtaCenter = new JTextArea();
	List<JCheckBox> boxesList = new LinkedList<>();
	
//	private static MyDialog dialog = null;

	public static void main(String args[]) {
		new RandomNumberGenerator().launchFrame();
	}
	
	public void launchFrame() {
		
//		Integer i = Integer.parseInt("A"); //NumberFormatException
		
		new MyDialog(this, "Enter", true); //true -> JFrame will display after this dialog
		
		//No input course or number || Close dialog directly || Cancel || praseInt(String) != Integer
		if(course == null || course.length() == 0 || 
				number == null || number.length() == 0 ||
				integerNum == null) {
//System.out.println("NULL");
			System.exit(0); //KIA
//			return; //not just return
		}
		
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
		panelSouth.add(jbRandom);
//		panelSouth.add(mark);
		panelSouth.add(jbLog);
		panelSouth.add(jbClear);
	}
	
	String course = null;
	String number = null;
	Integer integerNum = null;
	
	private class MyDialog extends JDialog 
	implements ActionListener{ //just implements on JFrame, will throw NullPointerException
							//when click confirm to send message to ActionEvent

		private static final long serialVersionUID = 1L;
		JButton jbConfirm = new JButton("Confirm");
		JButton jbCancel = new JButton("Cancel");
		JLabel jlCourse = new JLabel("Course:");
		JLabel jlNumber = new JLabel("Number:");
		JTextField jtfCourse = new JTextField();
		JTextField jtfNumber = new JTextField();
		JPanel jpCenter = new JPanel();
		JPanel jpCenterLeft = new JPanel();
		JPanel jpCenterRight = new JPanel();
		JPanel jpSouth = new JPanel();
		
		MyDialog(RandomNumberGenerator frame, String title, boolean modal) {
			super(frame, title, modal);
			this.setSize(200, 150);
			this.setLocationRelativeTo(null);
			this.setDefaultCloseOperation(MyDialog.DISPOSE_ON_CLOSE);
			
			centerPanel();
			southPanel();
			this.setLayout(new BorderLayout());
			this.add(jpCenter, BorderLayout.CENTER);
			this.add(jpSouth, BorderLayout.SOUTH);
			
			jbConfirm.addActionListener(this);
			jbCancel.addActionListener(this);
			
			this.setVisible(true);
		}
		
		private void centerPanel() {
			jpCenter.setLayout(new BorderLayout());
			jpCenterLeft.setLayout(new GridLayout(2, 1));
			jpCenterRight.setLayout(new GridLayout(2, 1));
			
			jpCenterLeft.add(jlCourse);
			jpCenterLeft.add(jlNumber);
			jpCenterRight.add(jtfCourse);
			jpCenterRight.add(jtfNumber);
			
			jpCenter.add(jpCenterLeft, BorderLayout.WEST);
			jpCenter.add(jpCenterRight, BorderLayout.CENTER);
		}
		
		private void southPanel() {
			jpSouth.add(jbConfirm);
			jpSouth.add(jbCancel);
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource().equals(jbConfirm)) {
				
				confirmPerformed();
				
			} else if(e.getSource().equals(jbCancel)) {
				course = null;
				number = null;
				integerNum = null;
				this.dispose();
			}
		}
		
		private void confirmPerformed() {
			course = jtfCourse.getText().trim();
			number = jtfNumber.getText().trim();
			
			if(course == null || course.length() == 0) { //NOT while
				JOptionPane.showMessageDialog(this, "Course can't be NULL!", 
						"Error", JOptionPane.INFORMATION_MESSAGE);
				jtfCourse.requestFocus(true);
			} else if(number == null || number.length() == 0) { //NOT while
				JOptionPane.showMessageDialog(this, "Number can't be NULL!", 
						"Error", JOptionPane.INFORMATION_MESSAGE);
				jtfNumber.requestFocus(true);
			} else if(number.length() > 0) {
				try {
					integerNum = Integer.parseInt(number);
//System.out.println(intgerNum);
				} catch(NumberFormatException exception) {
					integerNum = null;
					JOptionPane.showMessageDialog(this, "Please input an integer number!", 
							"Error", JOptionPane.INFORMATION_MESSAGE);
					jtfNumber.setText("");
					jtfNumber.requestFocus(true);
				}
				if(integerNum != null) {
					this.dispose();
				}
			} else {
				this.dispose();
			}
		}
		
	}

}
