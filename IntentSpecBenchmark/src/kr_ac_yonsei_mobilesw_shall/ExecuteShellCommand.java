package kr_ac_yonsei_mobilesw_shall;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import kr_ac_yonsei_mobilesw_UI.BenchAdd;
import kr_ac_yonsei_mobilesw_UI.Benchmark;

public class ExecuteShellCommand {
	public static void executeCommand(Benchmark ui, String command) 
	{		
		Thread worker = new Thread()
		{
			public void run()
			{
				Process p = null;
				try {					
					p = Runtime.getRuntime().exec(command);
					
					ui.appendTxt_adbCommandLog("> " + command);
					
					while(p.isAlive())
					{
						BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
						String line = "";
						
						while ((line = reader.readLine())!= null) 
						{
							if(line.equals("") == false)
							{
								ui.appendTxt_adbCommandLog("\n" + line);
							}
						}
						
						ui.appendTxt_adbCommandLog("\n\n");
					}
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				
				p.destroy();
			}
		};
		
		worker.start();
	}
	
	public static void showLogcat(Benchmark ui,  String command)
	{
		Thread worker = new Thread()
		{
			public void run()
			{
				Process p;
				try {
					p = Runtime.getRuntime().exec(command);
					
					while(p.isAlive())
					{
						BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
						String line = "";
						
						while ((line = reader.readLine())!= null) 
						{
							if(line.equals("") == false)
							{
								ui.appendTxt_logcat(line);
								ui.showLogcat();
							}
						}
					}
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		};
		
		worker.start();
	}
	
	public static void readDevice(Benchmark ui, String command) 
	{		
		Thread worker = new Thread()
		{
			public void run()
			{
				Process p = null;
				try {
					
					for(int i = 0; i < 3; i ++)
					{
						p = Runtime.getRuntime().exec(command);
						
						while(p.isAlive())
						{
							if(i == 2)
							{
								BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
								String line = "";
								
								while ((line = reader.readLine())!= null) 
								{
									if(line.equals("") == false)
									{
										ui.showDeviceList(line);
									}
								}
							}
						}
					}
					
					p.destroy();
					
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		};
		
		worker.start();
	}
		
	public static void executeMakeAdbCommand(BenchAdd ui, String command) 
	{		
		Thread worker = new Thread()
		{
			public void run()
			{
				Process p = null;
				try {					
					p = Runtime.getRuntime().exec(command);
					
					//ui.appendTxt_adbCommand("> " + command);
					
					while(p.isAlive())
					{
						BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
						String line = "";
						
						while ((line = reader.readLine())!= null) 
						{
							if(line.equals("") == false)
							{
								ui.appendTxt_adbCommand(line + "\n");
							}
						}
					}
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				
				p.destroy();
			}
		};
		
		worker.start();
	}
	
}
