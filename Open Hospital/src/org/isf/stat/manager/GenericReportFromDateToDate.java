package org.isf.stat.manager;

import java.io.File;
import java.sql.Connection;
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

/*--------------------------------------------------------
 * GenericReportLauncer2Dates
 *  - lancia tutti i report che come parametri hanno "da data" "a data"
 * 	- la classe prevede l'inizializzazione attraverso 
 *    dadata, adata, nome del report (senza .jasper)
 *---------------------------------------------------------
 * modification history
 * 09/06/2007 - prima versione
 *
 *-----------------------------------------------------------------*/
	public class GenericReportFromDateToDate {
		public  GenericReportFromDateToDate(String fromDate, String toDate, String jasperFileName) {
			try{
		        HashMap<String, String> parameters = new HashMap<String, String>();
				HospitalBrowsingManager hospManager = new HospitalBrowsingManager();
				Hospital hosp = hospManager.getHospital();
				
				parameters.put("Hospital", hosp.getDescription());
				parameters.put("Address", hosp.getAddress());
				parameters.put("City", hosp.getCity());
				parameters.put("Email", hosp.getEmail());
				parameters.put("Telephone", hosp.getTelephone());
				parameters.put("fromdate", fromDate + ""); // real param
				parameters.put("todate", toDate + ""); // real param
			
				StringBuilder sbFilename = new StringBuilder();
				sbFilename.append("rpt");
				sbFilename.append(File.separator);
				sbFilename.append(jasperFileName);
				sbFilename.append(".jasper");

				File jasperFile = new File(sbFilename.toString());
		
				Connection conn = DbSingleConn.getConnection();
				
				JasperReport jasperReport = (JasperReport)JRLoader.loadObject(jasperFile);
				JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);
				String PDFfile = "rpt/PDF/"+jasperFileName+".pdf";
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
