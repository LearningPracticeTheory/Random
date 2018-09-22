import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;

public class MainDialog extends MyDialog {

	private static final long serialVersionUID = 1L;
 	private static final int WIDTH = 460;
	private static final int HEIGHT = 500;
	
	String title = "Random";
	Color color = Color.LIGHT_GRAY;
	
	JPanel panelSouth = new JPanel();
	JPanel panelEast = new JPanel();
	JPanel panelEastUp = new JPanel();
	JPanel panelEastCenter = new JPanel();
	JButton jbRandom = new JButton("Alt+1/random");
	JButton jbLog = new JButton("2:log");
	JButton jbShowLog = new JButton("3:show log");
	JButton jbClear = new JButton("clear");
	JRadioButton jrbSelectAll = new JRadioButton("Select All");
	JTextArea jtaCenter = new JTextArea();
	
	MainDialog(RandomNumberGenerator frame, String title, boolean modal) {
		super(frame, title, modal);
		this.frame = frame;
		
		initDialog(frame, title, modal);
	}
	
	public void initDialog(RandomNumberGenerator frame, String title, boolean modal) {
		this.setSize(WIDTH, HEIGHT);
		this.setTitle(title);
		this.getContentPane().setBackground(color);
		this.setLayout(new BorderLayout());
		this.setLocationRelativeTo(null);
		
		this.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) { //when click X, log from tmpFile -> logFile
				logPerformed(true); //which means running once, truly log once when exist
				frame.tmpFile.delete();
//				tmpFile.deleteOnExit(); //file delete after VM exist/terminates
				super.windowClosing(e);
				System.exit(0);
			}
			
		});

/*JButton add keyboard shortcuts*/
	//i.
		jbRandom.setMnemonic(KeyEvent.VK_1); //Alt + 1
		
	//ii.
		/*This method is now obsolete*/
		jbLog.registerKeyboardAction(this, //2
				KeyStroke.getKeyStroke(KeyEvent.VK_2, 0), //0 means only single key
				JComponent.WHEN_IN_FOCUSED_WINDOW); //when in the focused window
		
		jbShowLog.registerKeyboardAction(this, 
				KeyStroke.getKeyStroke(KeyEvent.VK_3, 0), 
				JComponent.WHEN_IN_FOCUSED_WINDOW);
		
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
	
	private void eastPanel() {
		
		panelEast.setLayout(new BorderLayout());
		panelEastCenter.setLayout(new BoxLayout(panelEastCenter, BoxLayout.PAGE_AXIS));
		
		panelEastUp.add(jrbSelectAll);
		
		for(int i = 0; i < frame.boxesList.size(); i++) {
			JCheckBox tmpBox = frame.boxesList.get(i);
			panelEastCenter.add(tmpBox);
		}
		
		panelEast.add(panelEastUp, BorderLayout.NORTH);
		panelEast.add(new JScrollPane(panelEastCenter), BorderLayout.CENTER);
		
	}
	
	private void centerPanel() { //add directly without support by JPanel
		add(new JScrollPane(jtaCenter), BorderLayout.CENTER);
		jtaCenter.setEditable(false); //!edit
		jtaCenter.setFont(new Font("Cataneo BT", Font.BOLD, 20));
		jtaCenter.setText("Course:" + frame.course + "\n");
		jtaCenter.selectAll();
		jtaCenter.setCaretPosition(jtaCenter.getSelectedText().length()-1);
	}
	
	private void southPanel() {
		panelSouth.setLayout(new GridLayout(1, 3));
		panelSouth.add(jbRandom);
		panelSouth.add(jbLog);
		panelSouth.add(jbShowLog);
		panelSouth.add(jbClear);
	}
	
	public void selectAllPerformed() {
		for(int i = 0; i < frame.boxesList.size(); i++) {
			if(jrbSelectAll.isSelected()) {
				frame.boxesList.get(i).setSelected(true);
			} else {
				frame.boxesList.get(i).setSelected(false);
			}
		}
	}
	
	public void clearPerformed() {
		jtaCenter.setText("Course:" + frame.course + "\n");
		frame.boxesList.clear();
		panelEastCenter.setVisible(false);
		panelEastCenter.removeAll();
		panelEastCenter.setVisible(true);
		if(frame.showLogDialog != null && frame.logDialogIsActive) {
			frame.showLogDialog.jtaLog.setText("Course:" + frame.course + "\n");
		}
	}
	
	File file = new File("history_of_numbers.log");
	BufferedWriter bw = null;
	SimpleDateFormat sdf = new SimpleDateFormat("YYYY/MM/dd hh:mm:ss");
	
	public String currentDate() {
		return sdf.format(new Date());
	}
	
	public void logPerformed(boolean flag) { //false: default -> tmpFile
											//true: tmpFile -> file(history file);	
		if (!file.exists()) { // create once
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (!frame.tmpFile.exists()) { // create every time when run the program
			try {
				frame.tmpFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		try {
			if (flag) {
				bw = new BufferedWriter(new FileWriter(file, true)); // append
			} else {
				bw = new BufferedWriter(new FileWriter(frame.tmpFile)); 
				// !append -> refresh every time when log
			}
			bw.write("Cource:" + frame.course + " at: " + currentDate());
			bw.newLine();

			for (int i = 0; i < frame.boxesList.size(); i++) {
				JCheckBox jcb = frame.boxesList.get(i);
				if (jcb.isSelected()) {
					bw.write(jcb.getText() + "+");
				} else {
					bw.write(jcb.getText());
				}
				bw.newLine();
			}

			bw.write("---------------------------------------------------");
			bw.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		if (frame.logDialogIsActive) {
			frame.showLogDialog.readPerformed();
		}

	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		frame.actionPerformed(e);
	}

}
