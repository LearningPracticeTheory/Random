import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;

/*
public interface MyDialog extends ActionListener {
		//No implements, All constant, All public 
	public void initDialog(RandomNumberGenerator frame, String title, boolean modal);
}
*/
public abstract class MyDialog extends JDialog 
implements ActionListener{//cannot use interface cause JDialog is a class
	//use abstract class, cannot append more constructor on subclass

	JButton jbCancel = null;
	JButton jbConfirm = null;
	RandomNumberGenerator frame = null;
	private static final long serialVersionUID = 1L;
	
	MyDialog() {
		
	}
	
	MyDialog(RandomNumberGenerator frame, String title, boolean modal) {
		super(frame, title, modal);
		this.frame = frame;
	}
	
	public void initDialog(RandomNumberGenerator frame, String title, boolean modal) {
		
	}
	
}
