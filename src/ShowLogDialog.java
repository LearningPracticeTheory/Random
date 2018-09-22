import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ShowLogDialog extends MyDialog {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Display record text
	 */
	JTextArea jtaLog = new JTextArea();
	private BufferedReader br = null;
	
	public ShowLogDialog(RandomNumberGenerator frame, String title, boolean modal) {
		super(frame, title, modal);
		this.frame = frame;
		initDialog(frame, title, modal);
	}
	
	@Override
	public void initDialog(RandomNumberGenerator frame, String title, boolean modal) {
		this.setSize(500, 400);
		this.setTitle(title);
		this.setLocationRelativeTo(null);
		
		this.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) { //click X at the top-right corner
				frame.logDialogIsActive = false;
				super.windowClosing(e);
			}
			
		});
		
		jtaLog.setEditable(false);
		jtaLog.setFont(new Font("Cataneo BT", Font.BOLD, 18));
		this.add(new JScrollPane(jtaLog));
		
		readPerformed();
		
		this.setVisible(true);
	}

	/**
	 * Read data from log file
	 */
	public void readPerformed() {
		String str = null;
		
		if(!frame.tmpFile.exists()) {
			try {
				frame.tmpFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		try {
			br = new BufferedReader(new FileReader(frame.tmpFile));
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

	@Override
	public void actionPerformed(ActionEvent e) {
		
	}
	
}
