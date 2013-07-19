package org.isf.hospital.manager;

import javax.swing.JOptionPane;

import org.isf.hospital.model.*;
import org.isf.hospital.service.IoOperations;
import org.isf.utils.exception.OHException;

/**
 * Class that provides gui separation from database operations and gives some
 * useful logic manipulations of the dinamic data (memory)
 * 
 * @author bob
 * 
 */
public class HospitalBrowsingManager {
	
	private IoOperations ioOperations = new IoOperations();

	/**
	 * Reads from database hospital informations
	 * 
	 * @return {@link Hospital} object
	 */
	public Hospital getHospital() {
		try {
			return ioOperations.getHospital();
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return null;
		}
	}

	/**
	 * updates hospital informations
	 * 
	 * @return <code>true</code> if the hospital informations have been updated, <code>false</code> otherwise
	 */
	public boolean updateHospital(Hospital hospital) {
		try {
			return ioOperations.updateHospital(hospital);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return false;
		}
    }	
}
	
