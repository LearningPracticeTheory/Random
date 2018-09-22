import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

public class InitializedEnterDialog extends MyDialog {

	private static final long serialVersionUID = 1L;
	
	JLabel jlCourse = new JLabel("Course:");
	JLabel jlNumber = new JLabel("Number:");
	JTextField jtfCourse = new JTextField();
	JTextField jtfNumber = new JTextField();
	JPanel jpCenter = new JPanel();
	JPanel jpCenterLeft = new JPanel();
	JPanel jpCenterRight = new JPanel();
	JPanel jpSouth = new JPanel();
	
	private String number = null;
	
	InitializedEnterDialog(RandomNumberGenerator frame, String title, boolean modal) {
		super(frame, title, modal);
		jbCancel = new JButton("Cancel");
		jbConfirm = new JButton("Enter/Confirm");
		
		initDialog(frame, title, modal);
	}
	
	@Override
	public void initDialog(RandomNumberGenerator frame, String title, boolean modal) {
		this.frame = frame;
		this.setSize(220, 160);
		this.setLocationRelativeTo(null);
		
		this.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
			
		});
		
		centerPanel();
		southPanel();
		this.setLayout(new BorderLayout());
		this.add(jpCenter, BorderLayout.CENTER);
		this.add(jpSouth, BorderLayout.SOUTH);
		
		jbConfirm.addActionListener(this);
		jbCancel.addActionListener(this);
		
		jbConfirm.registerKeyboardAction(this, 
				KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), 
				JComponent.WHEN_IN_FOCUSED_WINDOW);
		
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
	
	private void confirmPerformed() {
		frame.course = jtfCourse.getText().trim();
		number = jtfNumber.getText().trim();

		if(frame.course == null || frame.course.length() == 0) { //NOT while
			JOptionPane.showMessageDialog(this, "Course can't be NULL!", 
					"Error", JOptionPane.INFORMATION_MESSAGE);
			jtfCourse.requestFocus(true);
		} else if(number == null || number.length() == 0) { //NOT while
			JOptionPane.showMessageDialog(this, "Number can't be NULL!", 
					"Error", JOptionPane.INFORMATION_MESSAGE);
			jtfNumber.requestFocus(true);
		} else if(number.length() > 0) {
			try {
				frame.numRange = Integer.parseInt(number);
			} catch(NumberFormatException exception) {
				frame.numRange = null;
				JOptionPane.showMessageDialog(this, "Please input an integer number!", 
						"Error", JOptionPane.INFORMATION_MESSAGE);
				jtfNumber.setText("");
				jtfNumber.requestFocus(true);
			}
			if(frame.numRange != null) { 
				this.dispose();
			}
		} else {
			this.dispose();
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(jbConfirm)) {
			
			confirmPerformed();
			
		} else if(e.getSource().equals(jbCancel)) {
			System.exit(0);
		} 
	}

}
