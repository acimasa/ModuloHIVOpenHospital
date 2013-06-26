package org.isf.utils.excel;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JTable;
import javax.swing.table.TableModel;

public class ExcelExporter {
	public ExcelExporter(){}
	
	public void ExportTable(JTable jtable,File file) throws IOException
	{
		TableModel model = jtable.getModel();
		FileWriter outFile = new FileWriter(file);
		
		for(int i=0; i < model.getColumnCount(); i++)
		{
			outFile.write(model.getColumnName(i)+"\t");
		}
		outFile.write("\n");
		
		for(int i=0; i < model.getRowCount(); i++)
		{
			for(int j=0; j < model.getColumnCount(); j++)
			{
				String strVal;
				Object objVal = model.getValueAt(i, j);
				if(objVal != null)
				{
					strVal = objVal.toString();
				}
				else
				{
					strVal=" ";
				}
				
				outFile.write(strVal+"\t");
				
			}
			outFile.write("\n");
		}
		
		outFile.close();
	}
}
