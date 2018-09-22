import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

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
import javax.swing.SwingConstants;

public class RandomNumberGenerator extends JFrame 
implements ActionListener {

	private static final long serialVersionUID = 1L;
	private static final int INITIALIZED_ENTER = 0;
	private static final int SHOW_RANDOM_NUM = 1;
	private static final int LOG_MODEL = 2;
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
	JButton jbShowLog = new JButton("show log");
	JButton jbClear = new JButton("clear");
	JRadioButton jrbSelectAll = new JRadioButton("Select All");
	JTextArea jtaCenter = new JTextArea();
	List<JCheckBox> boxesList = new LinkedList<>();
/*	
* BUG: the same number of elements but different choice/mark will not successfully log
*/

//	private static int oldBoxesSize = 0; //No need these variable to record the size & numbers
//	private static int selectNums = 0; //which has some bug and hard to debug
//	List<Boolean> selectIndex = new ArrayList<>(); //but use tmpFile can easily solved the problem
	
	private static Random r = new Random();
	
	private static MyDialog showLogDialog = null;
	
	private boolean logDialogIsActive = false;

	public static void main(String args[]) {
		new RandomNumberGenerator().launchFrame();
	}
	
	public void launchFrame() {
		
//		Integer i = Integer.parseInt("A"); //NumberFormatException
		/*
		while(true) {
			if(r.nextInt(10) == 0) {
				System.out.println("0"); //nextInt include 0;
				break;
			}
		}
		*/
		
		showDialog(INITIALIZED_ENTER);
		
		//No input course or number || Close dialog directly || Cancel || praseInt(String) != Integer
		if(course == null || course.length() == 0 || 
				number == null || number.length() == 0 ||
				numRange == null) {
//System.out.println("NULL");
			System.exit(0); //KIA
//			return; //not just return
		}
		
		this.setSize(WIDTH, HEIGHT);
		this.setTitle(title);
		this.getContentPane().setBackground(color);
		this.setLayout(new BorderLayout());
		this.setLocationRelativeTo(null);
//		this.setDefaultCloseOperation(EXIT_ON_CLOSE); //just close is not enough
		
		this.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) { //when click X, log from tmpFile -> logFile
				logPerformed(true); //which means running once, truly log once when exist
				tmpFile.delete();
//				tmpFile.deleteOnExit(); //file delete after VM exist/terminates
				super.windowClosing(e);
				System.exit(0);
			}
			
		});
		
		eastPanel();
		centerPanel();
		southPanel();
		this.add(panelEast, BorderLayout.EAST);
		this.add(panelSouth, BorderLayout.SOUTH);
		
		jbRandom.addActionListener(this);
		jbClear.addActionListener(this);
		jbLog.addActionListener(this);
		jbShowLog.addActionListener(this);
		jrbSelectAll.addActionListener(this);
		
		this.setVisible(true);
		
	}
	
	private void showDialog(int model) {
		if(model == INITIALIZED_ENTER) {
			new MyDialog(this, "Enter", true); //true -> JFrame will display after this dialog
		} else if(model == SHOW_RANDOM_NUM) {
			new MyDialog(this, true, "Random_Number");
		} else if(model == LOG_MODEL) {
			/*
			if(showLogDialog != null && showLogDialog.isActive()) {
				return;
			} else if(showLogDialog != null && !showLogDialog.isActive()) {
				showLogDialog = null;
			}
			*/
			if(!logDialogIsActive) {
				showLogDialog = null;
			}
			if(showLogDialog == null) {
				showLogDialog = new MyDialog(false, this, "Log_History");
				logDialogIsActive = true;
			} 
		}
	}
	
	private void eastPanel() {
		
		panelEast.setLayout(new BorderLayout());
//sort from up to down
		panelEastCenter.setLayout(new BoxLayout(panelEastCenter, BoxLayout.PAGE_AXIS));
		
		panelEastUp.add(jrbSelectAll);
		
		for(int i = 0; i < boxesList.size(); i++) {
			JCheckBox tmpBox = boxesList.get(i);
			panelEastCenter.add(tmpBox);
		}
		
		panelEast.add(panelEastUp, BorderLayout.NORTH);
		panelEast.add(new JScrollPane(panelEastCenter), BorderLayout.CENTER);
		
	}
	
	private void centerPanel() { //add directly without support by JPanel
		add(new JScrollPane(jtaCenter), BorderLayout.CENTER);
		jtaCenter.setEditable(false); //!edit
		jtaCenter.setFont(new Font("Cataneo BT", Font.BOLD, 20));
		jtaCenter.setText("Course:" + course + "\n");
		jtaCenter.selectAll();
		jtaCenter.setCaretPosition(jtaCenter.getSelectedText().length()-1);
	}
	
	private void southPanel() {
		panelSouth.setLayout(new GridLayout(1, 3));
		panelSouth.add(jbRandom);
//		panelSouth.add(mark);
		panelSouth.add(jbLog);
		panelSouth.add(jbShowLog);
		panelSouth.add(jbClear);
	}
	
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(jbRandom)) {
//System.out.println("random");
			showDialog(SHOW_RANDOM_NUM);
		} else if(e.getSource().equals(jbLog)) {
			logPerformed(false);
		} else if(e.getSource().equals(jbShowLog)) {
			showDialog(LOG_MODEL);
		} else if(e.getSource().equals(jbClear)) { //without remove boxesList, for which using fall back
			clearPerformed();
		} else if(e.getSource().equals(jrbSelectAll)) {
			selectAllPerformed();
		}
	}
	
	private void selectAllPerformed() {
		for(int i = 0; i < boxesList.size(); i++) {
			if(jrbSelectAll.isSelected()) {
				boxesList.get(i).setSelected(true);
			} else {
				boxesList.get(i).setSelected(false);
			}
		}
	}
	
	private void clearPerformed() {
		jtaCenter.setText("Course:" + course + "\n");
//		oldBoxesSize = 0;
		boxesList.clear();
		panelEastCenter.setVisible(false);
		panelEastCenter.removeAll();
		panelEastCenter.setVisible(true);
	}
	
	File file = new File("history_of_numbers.log");
	File tmpFile = new File("tmp.log");
	BufferedWriter bw = null;
	SimpleDateFormat sdf = new SimpleDateFormat("YYYY/MM/dd hh:mm:ss");
	
	private void logPerformed(boolean flag) { //false: default -> tmpFile
											//true: tmpFile -> file(history file);	
		/*
		if(boxesList.size() == oldBoxesSize) {
			return; //Now use tmpFile to log, refresh every time,
		} //so solve the problem like: same 
		*/
		if(!file.exists()) { //create once
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		if(!tmpFile.exists()) { //create every time when run the program
			try {
				tmpFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		try {
			if(flag) {
				bw = new BufferedWriter(new FileWriter(file, true)); //append
			} else {
				bw = new BufferedWriter(new FileWriter(tmpFile)); //!append -> refresh every time when log
			}
//System.out.println(date);
			bw.write("Cource:" + course + " at: " + currentDate());
			bw.newLine();
			
			for(int i = 0; i < boxesList.size(); i++) {
				JCheckBox jcb = boxesList.get(i);
				if(jcb.isSelected()) {
					bw.write(jcb.getText() + "+");
				} else {
					bw.write(jcb.getText());
				}
				bw.newLine();
			}
			
//			bw.newLine();
			bw.write("---------------------------------------------------");
			bw.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(bw != null) {
				try {
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
//		oldBoxesSize = boxesList.size();
		
		if(logDialogIsActive) {
			showLogDialog.readPerformed();
		}
		
	}
	
	public String currentDate() {
		return sdf.format(new Date());
	}
	
	String course = null;
	String number = null;
	Integer numRange = null;
	
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
		
		public MyDialog(JFrame frame, String title, boolean modal) {
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
		
//		JTextField jtfNum = new JTextField();
		JLabel jlNum = new JLabel(); //display better than JTF
		JPanel jpSouthButtons = new JPanel();
		JButton jbNumConfirm = new JButton("Confirm");
		JButton jbNumMark = new JButton("Mark");
		JButton jbNumCancel = new JButton("Cancel");
		private int randomNum;
		
		public MyDialog(JFrame frame, boolean modal, String title) {
			super(frame, title, modal);
			this.setSize(250, 200);
			this.setLocationRelativeTo(null);
			this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			this.setLayout(new BorderLayout());
			
			randomNum = r.nextInt(numRange) + 1; //[0-numRange)
			
			jlNum.setFont(new Font("Cataneo BT", Font.BOLD, 70));
			jlNum.setHorizontalAlignment(SwingConstants.CENTER);
			jlNum.setText(String.valueOf(randomNum));
			
			jpSouthButtons.add(jbNumConfirm);
			jpSouthButtons.add(jbNumMark);
			jpSouthButtons.add(jbNumCancel);
			
			this.add(jlNum, BorderLayout.CENTER);
			this.add(jpSouthButtons, BorderLayout.SOUTH);
			
			jbNumConfirm.addActionListener(this);
			jbNumCancel.addActionListener(this);
			jbNumMark.addActionListener(this);
			
			this.setVisible(true);
		}
		
		JTextArea jtaLog = new JTextArea();
		BufferedReader br = null;
		
		public MyDialog(boolean modal, JFrame frame, String title) {
			super(frame, title, modal);
			this.setSize(500, 400);
			this.setLocationRelativeTo(null);
//			this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			
			this.addWindowListener(new WindowAdapter() {

				@Override
				public void windowClosing(WindowEvent e) { //click X at the top-right corner
					logDialogIsActive = false;
//System.out.println(logDialogIsActive);
					super.windowClosed(e);
				}
				
			});
			
			jtaLog.setEditable(false);
			jtaLog.setFont(new Font("Cataneo BT", Font.ITALIC, 15));
			this.add(new JScrollPane(jtaLog));
			
			readPerformed();
			
			this.setVisible(true);
		}

		public void readPerformed() {
			String str = null;
			
			if(!tmpFile.exists()) {
				try {
					tmpFile.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			try {
				br = new BufferedReader(new FileReader(tmpFile));
				jtaLog.setText("");
				while((str = br.readLine()) != null) {
					jtaLog.append(str + "\n");
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if(br != null) {
					try {
						br.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
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
		public void actionPerformed(ActionEvent e) { //use switch??
			if(e.getSource().equals(jbConfirm)) {
				
				confirmPerformed();
				
			} else if(e.getSource().equals(jbCancel)) {
				course = null;
				number = null;
				numRange = null;
				this.dispose();
			} else if(e.getSource().equals(jbNumCancel)) {
				this.dispose();
			} else if(e.getSource().equals(jbNumConfirm)) {
				numConfirmPerformed(false); //false: confirm -> !Selected
			} else if(e.getSource().equals(jbNumMark)) {
				numMarkPerformed();
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
					numRange = Integer.parseInt(number);
//System.out.println(intgerNum);
				} catch(NumberFormatException exception) {
					numRange = null;
					JOptionPane.showMessageDialog(this, "Please input an integer number!", 
							"Error", JOptionPane.INFORMATION_MESSAGE);
					jtfNumber.setText("");
					jtfNumber.requestFocus(true);
				}
				if(numRange != null) {
					this.dispose();
				}
			} else {
				this.dispose();
			}
		}
		
		private void numConfirmPerformed(boolean isSelected) { //true: mark; false: confirm
			String tmp = String.valueOf(randomNum);
			JCheckBox jcb = new JCheckBox(tmp);
			boxesList.add(jcb);
			panelEastCenter.setVisible(false); //hide
			panelEastCenter.add(jcb); //hide + reappear --> refresh() != repaint();
			jcb.setSelected(isSelected);
			panelEastCenter.setVisible(true); //reappear
			jtaCenter.append("No." + tmp + "\n");
			this.dispose();
		}
		
		private void numMarkPerformed() { //true: mark -> selected
			numConfirmPerformed(true);
		}
		
	}


}
