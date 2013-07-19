/*
 * Created on 15/giu/08
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.isf.stat.manager;

import java.io.File;
import java.sql.Connection;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

import org.isf.generaldata.GeneralData;
import org.isf.hospital.manager.HospitalBrowsingManager;
import org.isf.hospital.model.Hospital;
import org.isf.utils.db.DbSingleConn;

public class GenericReportPharmaceuticalOrder {

	public GenericReportPharmaceuticalOrder(String jasperFileName) {
		try{
	        HashMap<String, String> parameters = new HashMap<String, String>();
			HospitalBrowsingManager hospManager = new HospitalBrowsingManager();
			Hospital hosp = hospManager.getHospital();
			
			Date date = new Date();
			Format formatter;
			formatter = new SimpleDateFormat("E d, MMMM yyyy");
		    String todayReport = formatter.format(date);
		    formatter = new SimpleDateFormat("yyyyMMdd");
		    String todayFile = formatter.format(date);
		    
			parameters.put("Hospital", hosp.getDescription());
			parameters.put("Address", hosp.getAddress());
			parameters.put("City", hosp.getCity());
			parameters.put("Email", hosp.getEmail());
			parameters.put("Telephone", hosp.getTelephone());
			parameters.put("Date", todayReport);

			StringBuilder sbFilename = new StringBuilder();
			sbFilename.append("rpt");
			sbFilename.append(File.separator);
			sbFilename.append(jasperFileName);
			sbFilename.append(".jasper");
			//System.out.println("Jasper Report Name:"+sbFilename.toString());
			
			File jasperFile = new File(sbFilename.toString());

			Connection conn = DbSingleConn.getConnection();
			
			JasperReport jasperReport = (JasperReport)JRLoader.loadObject(jasperFile);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);

			String PDFfile = "rpt/PDF/"+jasperFileName + "_" + todayFile.toString()+".pdf";
			JasperExportManager.exportReportToPdfFile(jasperPrint, PDFfile);
			
			if (GeneralData.INTERNALVIEWER)
				JasperViewer.viewReport(jasperPrint,false);
			else { 
				try{
					Runtime rt = Runtime.getRuntime();
					rt.exec(GeneralData.VIEWER +" "+ PDFfile);
					
				} catch(Exception e){
					e.printStackTrace();
				}
			}		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
