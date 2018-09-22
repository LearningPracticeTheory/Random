import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;

public class RandomNumberDialog extends MyDialog {

	private static final long serialVersionUID = 1L;
	
	Random r = new Random();
	
	JLabel jlNum = new JLabel(); //display better than JTF
	JPanel jpSouthButtons = new JPanel();
	
	/**
	 * Mark button for numbers
	 */
	JButton jbNumMark = new JButton("2/Mark");
	
	private int randomNum;
	
	public RandomNumberDialog(RandomNumberGenerator frame, String title, boolean modal) {
		super(frame, title, modal);
		this.frame = frame;
		initDialog(frame, title, modal);
	}
	
	@Override
	public void initDialog(RandomNumberGenerator frame, String title, boolean modal) {
		jbCancel = new JButton("3/Cancel");
		jbConfirm = new JButton("1/Confirm");
		
		this.frame = frame;
		this.setSize(280, 220);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		this.setLayout(new BorderLayout());
		
		randomNum = r.nextInt(frame.numRange) + 1; //[0-numRange)
		
		jlNum.setFont(new Font("Cataneo BT", Font.BOLD, 70));
		jlNum.setHorizontalAlignment(SwingConstants.CENTER);
		jlNum.setText(String.valueOf(randomNum));
		
		jpSouthButtons.add(jbConfirm);
		jpSouthButtons.add(jbNumMark);
		jpSouthButtons.add(jbCancel);
		
		this.add(jlNum, BorderLayout.CENTER);
		this.add(jpSouthButtons, BorderLayout.SOUTH);
		
		jbConfirm.addActionListener(this);
		jbCancel.addActionListener(this);
		jbNumMark.addActionListener(this);
		
		/**
		 * Confirm button associated with VK_1
		 */
		jbConfirm.registerKeyboardAction(this, 
				KeyStroke.getKeyStroke(KeyEvent.VK_1, 0), 
				JComponent.WHEN_IN_FOCUSED_WINDOW);
		
		jbNumMark.registerKeyboardAction(this, 
				KeyStroke.getKeyStroke(KeyEvent.VK_2, 0), 
				JComponent.WHEN_IN_FOCUSED_WINDOW);
		
		jbCancel.registerKeyboardAction(this, 
				KeyStroke.getKeyStroke(KeyEvent.VK_3, 0), 
				JComponent.WHEN_IN_FOCUSED_WINDOW);
		
		this.setVisible(true);
	}
	
	/**
	 * Number confirm or mark operation
	 * @param isSelected true is mark; false is just confirm without mark.
	 */
	private void numConfirmPerformed(boolean isSelected) { //true: mark; false: confirm
		String tmp = String.valueOf(randomNum);
		JCheckBox jcb = new JCheckBox(tmp);
		frame.boxesList.add(jcb);
		frame.mainDialog.panelEastCenter.setVisible(false); //hide
		frame.mainDialog.panelEastCenter.add(jcb); //hide + reappear --> refresh() != repaint();
		jcb.setSelected(isSelected);
		frame.mainDialog.panelEastCenter.setVisible(true); //reappear
		frame.mainDialog.jtaCenter.append("No." + tmp + "\n");
		this.dispose();
	}
	
	/**
	 * Number mark operation
	 */
	private void numMarkPerformed() { //true: mark -> selected
		numConfirmPerformed(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(jbCancel)) {
			this.dispose();
		} else if(e.getSource().equals(jbConfirm)) {
			numConfirmPerformed(false); //false: confirm -> !Selected
		} else if(e.getSource().equals(jbNumMark)) {
			numMarkPerformed();
		}
	}

}
