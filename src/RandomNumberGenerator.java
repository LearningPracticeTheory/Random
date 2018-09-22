import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JFrame;

public class RandomNumberGenerator extends JFrame 
implements ActionListener {

	private static final long serialVersionUID = 1L;
	private static final int INITIALIZED_ENTER = 0;
	private static final int SHOW_RANDOM_NUM = 1;
	private static final int SHOW_LOG_MODEL = 2;
	private static final int MAIN_DIALOG_MODEL = 3;

	List<JCheckBox> boxesList = new LinkedList<>();
	
	String course = null;
	Integer numRange = null;
	
	public ShowLogDialog showLogDialog = null;
	public InitializedEnterDialog initEnterDialog = null;
	public MainDialog mainDialog = null;
	public RandomNumberDialog randomNumDialog = null;
	
	File tmpFile = new File("tmp.log");
	
	public boolean logDialogIsActive = false;

	public static void main(String args[]) {
		new RandomNumberGenerator().launchFrame();
	}
	
	public void launchFrame() {
		initEnterDialog = (InitializedEnterDialog)showDialog(INITIALIZED_ENTER);
		//No input course or number || Close dialog directly || Cancel || praseInt(String) != Integer
		while(course == null || course.length() == 0 || numRange == null) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		mainDialog = (MainDialog)showDialog(MAIN_DIALOG_MODEL);
	}
	
	private MyDialog showDialog(int model) {
		if(model == INITIALIZED_ENTER) {
			return new InitializedEnterDialog(this, "Enter", true); //true -> JFrame will display after this dialog
		} else if(model == SHOW_RANDOM_NUM) {
			return new RandomNumberDialog(this, "Random Number Dialog", true);
		} else if(model == SHOW_LOG_MODEL) {
			if(!logDialogIsActive) {
				showLogDialog = null;
			}
			if(showLogDialog == null) {
				logDialogIsActive = true;
				return new ShowLogDialog(this, "Log Dialog", false);
			}
			return showLogDialog; //showLogDialog != null && isActive -> exist one -> return it back
		} else if(model == MAIN_DIALOG_MODEL){
			return new MainDialog(this, "Random", false);
		} else {
			return null;
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(mainDialog.jbRandom)) {
			randomNumDialog = (RandomNumberDialog)showDialog(SHOW_RANDOM_NUM);
		} else if(e.getSource().equals(mainDialog.jbShowLog)) {
			showLogDialog = (ShowLogDialog)showDialog(SHOW_LOG_MODEL);
		} else if(e.getSource().equals(mainDialog.jbLog)) {
			mainDialog.logPerformed(false);
		} else if(e.getSource().equals(mainDialog.jbClear)) { //without remove boxesList, for which using fall back
			mainDialog.clearPerformed();
		} else if(e.getSource().equals(mainDialog.jrbSelectAll)) {
			mainDialog.selectAllPerformed();
		}
	}
	
}