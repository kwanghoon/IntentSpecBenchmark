package kr_ac_yonsei_mobilesw_UI;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import java.awt.Font;

import javax.swing.JButton;
import javax.swing.ScrollPaneConstants;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.Random;

public class BenchAdd extends JFrame {

	private JPanel contentPane;
	private static Benchmark benchmarkUI;
	private JTextArea txtAdbCommand;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BenchAdd frame = new BenchAdd(benchmarkUI);
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
	public BenchAdd(Benchmark mUI) {
		
		this.benchmarkUI = mUI;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 639, 502);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setBounds(12, 10, 599, 410);
		contentPane.add(scrollPane);
		
		txtAdbCommand = new JTextArea();
		txtAdbCommand.setFont(new Font("Courier New", Font.PLAIN, 12));
		scrollPane.setViewportView(txtAdbCommand);
		
		JButton btnOk = new JButton("OK");
		btnOk.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				AddAdbCommand();
			}
		});
		btnOk.setBounds(401, 430, 99, 30);
		contentPane.add(btnOk);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				Close();
			}
		});
		btnCancel.setBounds(512, 430, 99, 30);
		contentPane.add(btnCancel);
	}
	
	public void Close()
	{
		super.dispose();
	}
	
	public void AddAdbCommand()
	{
		String[] adbCommand = parseStr(txtAdbCommand.getText().trim());

		DefaultTableModel modelAdbCommand = this.benchmarkUI.getModelAdbCommand();
		
		for(int i = 0; i < adbCommand.length; i++)
		{
			modelAdbCommand.addRow(makeRow(i, adbCommand[i]));			
		}
		
		Close();
	}
	
	public String[] parseStr(String str)
	{
		Random rand = new Random(System.currentTimeMillis());
		
		String[] spLine = str.split("\n");
		
		for(int i = 0; i < spLine.length; i++)
		{
			String org = "";
			String[] spToken = spLine[i].split(" ");
			
			for(int k = 0; k < spToken.length; k++)
			{
				if(spToken[k].equals("--es") || spToken[k].equals("-e"))
				{
					//String
				}
				else if(spToken[k].equals("--ez"))
				{
					if(spToken[k + 2].equals("true") == false && spToken[k + 2].equals("false") == false)
					{
						spToken[k + 2] = String.valueOf(rand.nextBoolean());						
					}
				}
				else if(spToken[k].equals("--ei"))
				{
					try{
						Integer.parseInt(spToken[k + 2]);
					}
					catch(NumberFormatException e)
					{
						spToken[k + 2] = String.valueOf(rand.nextInt());
					}
				}
				else if(spToken[k].equals("--el"))
				{
					try{
						Long.parseLong(spToken[k + 2]);
					}
					catch(NumberFormatException e)
					{
						spToken[k + 2] = String.valueOf(rand.nextLong());
					}
				}
				else if(spToken[k].equals("--ef"))
				{
					try{
						Float.parseFloat(spToken[k + 2]);
					}
					catch(NumberFormatException e)
					{
						spToken[k + 2] = String.valueOf(rand.nextFloat());
					}
				}
				else if(spToken[k].equals("--eu"))
				{
					//String
				}
				else if(spToken[k].equals("--ecn"))
				{
					//String
				}
				else if(spToken[k].equals("--eia"))
				{
					try{
						Integer.parseInt(spToken[k + 2]);
					}
					catch(NumberFormatException e)
					{
						spToken[k + 2] = randomIntArray();
					}
				}
				else if(spToken[k].equals("--ela"))
				{
					try{
						Long.parseLong(spToken[k + 2]);
					}
					catch(NumberFormatException e)
					{
						spToken[k + 2] = randomLongArray();
					}
				}
				else if(spToken[k].equals("--efa"))
				{
					try{
						Float.parseFloat(spToken[k + 2]);
					}
					catch(NumberFormatException e)
					{
						spToken[k + 2] = randomfloatArray();
					}
				}
			}
			
			for(int k = 0; k < spToken.length; k++)
			{
				org += spToken[k] + " ";
			}
			
			spLine[i] = org;
		}
		
		return spLine;
	}
	
	public String randomIntArray()
	{
		Random rand = new Random(System.currentTimeMillis());
		String intArray = "";
		
		int count = (rand.nextInt() % 10) + 1;
		
		intArray = String.valueOf(rand.nextInt());
		for(int i = 1; i < count; i++)
		{
			intArray += ", " + rand.nextInt();
		}
		
		return intArray;
	}
	
	public String randomLongArray()
	{
		Random rand = new Random(System.currentTimeMillis());
		String longArray = "";
		
		int count = (rand.nextInt() % 10) + 1;
		
		longArray = String.valueOf(rand.nextLong());
		for(int i = 1; i < count; i++)
		{
			longArray += ", " + rand.nextLong();
		}
		
		return longArray;
	}
	
	public String randomfloatArray()
	{
		Random rand = new Random(System.currentTimeMillis());
		String floatArray = "";
		
		int count = (rand.nextInt() % 10) + 1;
		
		floatArray = String.valueOf(rand.nextFloat());
		for(int i = 1; i < count; i++)
		{
			floatArray += ", " + rand.nextFloat();
		}
		
		return floatArray;
	}
	
	public Object[] makeRow(int seq, String str)
	{
		//String packageName = str.substring(str.indexOf("-n ") + 3, str.indexOf('/', str.indexOf("-n ") + 3));
		
		Object[] row = new Object[]{String.valueOf(seq), str};
		
		return row;
	}
}
