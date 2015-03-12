package kr_ac_yonsei_mobilesw_UI;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.JTable;
import javax.swing.JButton;
import javax.swing.JTextField;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import kr_ac_yonsei_mobilesw_shall.ExecuteShellCommand;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Benchmark extends JFrame {

	private static final long serialVersionUID = -8114454317556683079L;
	
	private boolean isBusy = false;
	
	private JPanel contentPane;
	private JButton btn_Start;
	private JTextField txt_adbCommand;
	private JTextArea txt_adbCommandLog;
	private JScrollPane scrollPane;
	private JTextArea txt_logcat;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

		try {
	        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	    } catch(Exception e) {
	        System.out.println("Error setting native LAF: " + e);
	    }

		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Benchmark frame = new Benchmark();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Benchmark() {
		setTitle("IntentSpecBenchmark v1.0");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1024, 750);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnViewlogcat = new JButton("ViewLogcat");
		btnViewlogcat.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				ExecuteShellCommand.showLogcat(Benchmark.this);
			}
		});
		btnViewlogcat.setBounds(868, 11, 128, 30);
		contentPane.add(btnViewlogcat);
		
		btn_Start = new JButton("exec");
		btn_Start.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				String command = txt_adbCommand.getText().trim();		//Don't have command
				if(command.equals("") == true)
				{
					return;
				}
				
				if(getisBusy() == true)										//Already processing
				{
					JOptionPane.showMessageDialog (null, "Now Processing...");
					return;
				}
				
				setisBusy(true);
				
				////D:/adt-bundle-windows-x86_64-20140702/sdk/platform-tools/adb shell am start -n com.enterpriseandroid.androidSecurity/.MainActivity -a android.intent.action.ERROR
				ExecuteShellCommand.executeCommand(Benchmark.this, command);
			}
		});
		btn_Start.setBounds(868, 51, 128, 30);
		contentPane.add(btn_Start);
		
		txt_adbCommand = new JTextField();
		txt_adbCommand.setBounds(6, 48, 850, 38);
		contentPane.add(txt_adbCommand);
		txt_adbCommand.setColumns(10);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(6, 96, 990, 274);
		contentPane.add(scrollPane);
		
		txt_adbCommandLog = new JTextArea();
		scrollPane.setViewportView(txt_adbCommandLog);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(6, 380, 990, 322);
		contentPane.add(scrollPane_1);
		
		txt_logcat = new JTextArea();
		scrollPane_1.setViewportView(txt_logcat);
	}
	
	public void appendTxt_adbCommandLog(String str)
	{
		txt_adbCommandLog.append(str);
		txt_adbCommandLog.setCaretPosition(txt_adbCommandLog.getCaretPosition() + str.length());
	}
	
	public void appendTxt_logcat(String str)
	{
		txt_logcat.append(str);
		txt_logcat.setCaretPosition(txt_logcat.getCaretPosition() + str.length());
	}
	
	public void setisBusy(boolean bool)
	{
		isBusy = bool;
	}
	
	public boolean getisBusy()
	{
		return isBusy;
	}
}
