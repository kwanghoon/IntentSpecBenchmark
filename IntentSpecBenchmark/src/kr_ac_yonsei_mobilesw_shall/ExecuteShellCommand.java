package kr_ac_yonsei_mobilesw_shall;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import kr_ac_yonsei_mobilesw_UI.Benchmark;

public class ExecuteShellCommand {
	public static void executeCommand(Benchmark ui, String command) 
	{		
		Thread worker = new Thread()
		{
			public void run()
			{
				Process p;
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
				
				ui.setisBusy(false);
			}
		};
		
		worker.start();
	}
	
	public static void showLogcat(Benchmark ui,  String command, String filter)
	{
		Thread worker = new Thread()
		{
			public void run()
			{
				Process p;
				try {
					p = Runtime.getRuntime().exec(command);
					
					ui.appendTxt_logcat("> " + command);
					
					while(p.isAlive())
					{
						BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
						String line = "";
						
						while ((line = reader.readLine())!= null) 
						{
							if(line.equals("") == false)
							{
								if(filter != null)
								{
									if(line.contains(filter))
									{
										ui.appendTxt_logcat("\n" + line);										
									}
								}
								else
								{
									ui.appendTxt_logcat("\n" + line);
								}
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

}
