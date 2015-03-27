package kr_ac_yonsei_mobilesw_UI;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import jxl.*;
import jxl.format.BoldStyle;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.Pattern;
import jxl.format.UnderlineStyle;
import jxl.write.Font;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

public class BenchStart {

	WritableWorkbook workbook;
	WritableSheet sheet;
	
	public void start(Benchmark ui) 
	{
		Thread worker = new Thread()
		{
			public void run()
			{
				makeExcel();
				
				int ForceFinishing = 0;
				int Finishing = 0;
				int ForceRemoving = 0;
				int Displayed = 0;
				int CantAnalyze = 0;
				
				
				if(ui.modelAdbCommand.getRowCount() < 1)
				{
					return;
				}
				
				for(int i = 0; i < ui.modelAdbCommand.getRowCount(); i++)
				{
					String adbCommand = ui.modelAdbCommand.getValueAt(i, 1).toString();
					String packageName = adbCommand.substring(adbCommand.indexOf("-n ") + 3, adbCommand.indexOf('/', adbCommand.indexOf("-n ") + 3));
					
					ui.exec("adb shell am force-stop " + packageName);
					try {
						Thread.currentThread().sleep(2000);
					} catch (InterruptedException e) {
						
					}
					
					ui.txtAdbCommand.setText(adbCommand);
					ui.txtFilter.setText(packageName);
					ui.txtAdbCommandLog.setText("");
					
					ui.LogcatClear();
					ui.filterEvent();
					ui.exec();
					try {
						Thread.currentThread().sleep(5000);
					} catch (InterruptedException e) {
						
					}
					
					String[] spLine = ui.txtAdbCommandLog.getText().split("\n");
					for(int k = 0; k < spLine.length; k++)
					{
						addRowinExcel(spLine[k]);
					}
					
					String result = benchResult(ui, packageName);
					ui.modelAdbCommand.setValueAt(result, i, 2);
					ui.modelAdbCommand.fireTableDataChanged();
					
					if(result.equals("Force finishing"))
					{
						ForceFinishing++;
					}
					else if(result.equals("Finishing"))
					{
						Finishing++;
					}
					else if(result.equals("Force removing"))
					{
						ForceRemoving++;
					}
					else if(result.equals("Displayed"))
					{
						Displayed++;
					}
					else if(result.equals("can't analyze"))
					{
						CantAnalyze++;
					}
					
					ui.txtBenchResult.setText("Force finishing \t: " + ForceFinishing
							+ "\nFinishing \t: " + Finishing 
							+ "\nForce removing: " + ForceRemoving
							+ "\nDisplayed \t: " + Displayed
							+ "\nCan't analyze \t: " + CantAnalyze 
							+ "\nResult Count \t: " + (ForceFinishing + Finishing + ForceRemoving + Displayed + CantAnalyze));
					
					addRowinExcel("result : " + result);
					addRowinExcel("--");
				}
				
				String resultAll = "Force finishing \t: " + ForceFinishing
				+ "\nFinishing \t: " + Finishing 
				+ "\nForce removing: " + ForceRemoving
				+ "\nDisplayed \t: " + Displayed
				+ "\nCan't analyze \t: " + CantAnalyze 
				+ "\nResult Count \t: " + (ForceFinishing + Finishing + ForceRemoving + Displayed + CantAnalyze);
				
				String[] resultAllLine = resultAll.split("\n");
				
				for(int i = 0; i < resultAllLine.length; i++)
				{
					addRowinExcel(resultAllLine[i]);
				}
				
				try {
					workbook.write();
					workbook.close();
				} catch (WriteException | IOException e) {
					e.printStackTrace();
				}
			}
		};
		
		worker.start();
	}
	
	public void addRowinExcel(String str)
	{
		WritableFont font = new WritableFont(WritableFont.createFont("Courier New"), WritableFont.DEFAULT_POINT_SIZE, WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
		WritableCellFormat analysisFormat = new WritableCellFormat(font);
		
		int nowRow = sheet.getRows();
		
		try {
			sheet.addCell(new Label(0, nowRow, str, analysisFormat));
		} catch (WriteException e) {
			e.printStackTrace();
		}
	}
	
	public void makeExcel()
	{
		try {
			SimpleDateFormat formatter = new SimpleDateFormat ( "yyyy년MM월dd일 HH시mm분ss초", Locale.KOREA );
			Date currentTime = new Date( );
			String dTime = formatter.format ( currentTime );
			
		    String fileName = dTime + ".xls";
		    workbook = Workbook.createWorkbook(new File(fileName));
		    sheet = workbook.createSheet("Benchmark Result", 0);
		    
		    WritableFont font = new WritableFont(WritableFont.createFont("Courier New"));
		    WritableCellFormat format = new WritableCellFormat(font);;
		    
		    sheet.addCell(new Label(0, 0, "Level", format));
			sheet.addCell(new Label(1, 0, "Time", format));
			sheet.addCell(new Label(2, 0, "PID", format));
			sheet.addCell(new Label(3, 0, "TID", format));
			sheet.addCell(new Label(4, 0, "Application", format));
			sheet.addCell(new Label(5, 0, "Tag", format));
			sheet.addCell(new Label(6, 0, "Text", format));
			sheet.addCell(new Label(7, 0, "RawMessage", format));
			
			sheet.setColumnView(0, 3);
			sheet.setColumnView(1, 25);
			sheet.setColumnView(2, 6);
			sheet.setColumnView(3, 6);
			sheet.setColumnView(4, 20);
			sheet.setColumnView(5, 20);
			sheet.setColumnView(6, 150);
			sheet.setColumnView(7, 150);
		    
		} catch (IOException | WriteException e) {
			e.printStackTrace();
		}
	}
	
    public String benchResult(Benchmark ui, String filter)
    {
    	String result = "";
    	
    	ui.LogcatLock.lock();
		
		try {
			
			for(int i = 0; i < ui.modelLogcatView.getRowCount(); i++)
			{
				WritableFont font;
				
				String level = ui.modelLogcatView.getValueAt(i, 0).toString();
                if(level.equals("V"))
                {
                	font = new WritableFont(WritableFont.createFont("Courier New"), WritableFont.DEFAULT_POINT_SIZE, WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
                }	
                else if(level.equals("D"))
                {
                	font = new WritableFont(WritableFont.createFont("Courier New"), WritableFont.DEFAULT_POINT_SIZE, WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.BLUE);
                }
                else if(level.equals("I"))
                {
                	font = new WritableFont(WritableFont.createFont("Courier New"), WritableFont.DEFAULT_POINT_SIZE, WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.GREEN);
                }
                else if(level.equals("W"))
                {
                	font = new WritableFont(WritableFont.createFont("Courier New"), WritableFont.DEFAULT_POINT_SIZE, WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.ORANGE);
                }
                else if(level.equals("E"))
                {
                	font = new WritableFont(WritableFont.createFont("Courier New"), WritableFont.DEFAULT_POINT_SIZE, WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.RED);
                }
                else
                {
                	font = new WritableFont(WritableFont.createFont("Courier New"), WritableFont.DEFAULT_POINT_SIZE, WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
                }
				
				
		    	WritableCellFormat analysisFormat = new WritableCellFormat(font);
				
				String rawLog = ui.modelLogcatView.getValueAt(i, 7).toString();
				
				if(rawLog.toString().contains("Force finishing"))
				{
					result = "Force finishing";
					analysisFormat.setBorder(Border.BOTTOM, BorderLineStyle.THICK, Colour.RED);
				}
				else if(rawLog.toString().contains("Finishing"))
				{
					result = "Finishing";
					analysisFormat.setBorder(Border.BOTTOM, BorderLineStyle.THICK, Colour.ORANGE);
				}
				else if(rawLog.toString().contains("Force removing"))
				{
					result = "Force removing";
					analysisFormat.setBorder(Border.BOTTOM, BorderLineStyle.THICK, Colour.LIGHT_GREEN);
				}
				else if(rawLog.toString().contains("Displayed"))
				{
					result = "Displayed";
					analysisFormat.setBorder(Border.BOTTOM, BorderLineStyle.THICK, Colour.LIGHT_BLUE);
				}
				else
				{

				}
				
				int nowRow = sheet.getRows();
				
				sheet.addCell(new Label(0, nowRow, ui.modelLogcatView.getValueAt(i, 0).toString(), analysisFormat));
				sheet.addCell(new Label(1, nowRow, ui.modelLogcatView.getValueAt(i, 1).toString(), analysisFormat));
				sheet.addCell(new Label(2, nowRow, ui.modelLogcatView.getValueAt(i, 2).toString(), analysisFormat));
				sheet.addCell(new Label(3, nowRow, ui.modelLogcatView.getValueAt(i, 3).toString(), analysisFormat));
				sheet.addCell(new Label(4, nowRow, ui.modelLogcatView.getValueAt(i, 4).toString(), analysisFormat));
				sheet.addCell(new Label(5, nowRow, ui.modelLogcatView.getValueAt(i, 5).toString(), analysisFormat));
				sheet.addCell(new Label(6, nowRow, ui.modelLogcatView.getValueAt(i, 6).toString(), analysisFormat));
				sheet.addCell(new Label(7, nowRow, ui.modelLogcatView.getValueAt(i, 7).toString(), analysisFormat));
				
			}
			
			if(result.equals(""))
			{
				result = "can't analyze";
			}
		
		} catch (WriteException e) {
			e.printStackTrace();
		}
		
		ui.LogcatLock.unlock();
		
		return result;
    }
}
