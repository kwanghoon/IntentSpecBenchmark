package kr_ac_yonsei_mobilesw_UI;

public class BenchStart {

	public static void start(Benchmark ui, String command) 
	{
		
		Thread worker = new Thread()
		{
			public void run()
			{
				if(ui.modelAdbCommand.getRowCount() < 1)
				{
					return;
				}
				
				for(int i = 0; i < ui.modelAdbCommand.getRowCount(); i++)
				{
					String adbCommand = ui.modelAdbCommand.getValueAt(i, 1).toString();
					String packageName = adbCommand.substring(adbCommand.indexOf("-n ") + 3, adbCommand.indexOf('/', adbCommand.indexOf("-n ") + 3));
					
					ui.txtAdbCommand.setText(adbCommand);
					ui.txtFilter.setText(packageName);
					
					ui.LogcatClear();
					ui.filterEvent();
					ui.exec();
					
					try {
						Thread.currentThread().sleep(7000);
					} catch (InterruptedException e) {
						
					}
				}
			}
		};
		
		worker.start();
	
	}
}
