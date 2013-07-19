package org.isf.medicalstock.manager;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import org.isf.medicalstock.model.Lot;
import org.isf.medicalstock.model.Movement;
import org.isf.medicalstock.service.IoOperations;
import org.isf.medicals.model.Medical;
import org.isf.utils.exception.OHException;
import org.isf.generaldata.MessageBundle;


public class MovStockInsertingManager {

	private IoOperations ioOperations;
	private org.isf.medicals.service.IoOperations ioOperationsMedicals;

	public MovStockInsertingManager(){
		ioOperations = new IoOperations();
		ioOperationsMedicals = new org.isf.medicals.service.IoOperations();
	}

	//Replaced by getMedical in MedicalBrowsingManager
	/*
	 * Gets the current quantity for the specified {@link Medical}.
	 * In case of error a message error is shown and a <code>0</code> value is returned.
	 * @param medical the medical to check.
	 * @return the current quantity of medical.

	public int getCurrentQuantity(Medical medical){
		try {
			return ioOperations.getCurrentQuantity(medical);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return 0;
		}
	} */

	/**
	 * Stores the specified movement.
	 * In case of error a message error is shown and a <code>null</code> value is returned.
	 * @param movement the movement to store.
	 * @return <code>true</code> if the movement has been stored, <code>false</code> otherwise.
	 */
	public boolean newMovement(Movement movement) {

		try {

			if(movement.getQuantity()==0){
				JOptionPane.showMessageDialog(null,MessageBundle.getMessage("angal.medicalstock.thequantitymustnotbe"));
				return false;
			}

			if(movement.getMedical()==null){
				JOptionPane.showMessageDialog(null,MessageBundle.getMessage("angal.medicalstock.chooseamedical"));
				return false;
			}

			if(movement.getType()==null){
				JOptionPane.showMessageDialog(null, MessageBundle.getMessage("angal.medicalstock.chooseatype"));
				return false;
			}

			if(movement.getLot()!=null) {

				if(movement.getLot().getCode().equalsIgnoreCase("")){
					JOptionPane.showMessageDialog(null, MessageBundle.getMessage("angal.medicalstock.insertavalidlotidentifier"));
					return false;
				}

				if(movement.getLot().getCode().length()>=50) {
					JOptionPane.showMessageDialog(null, MessageBundle.getMessage("angal.medicalstock.changethelotidbecauseitstoolong"));
					return false;
				}

				if(movement.getLot().getDueDate()==null){
					JOptionPane.showMessageDialog(null, MessageBundle.getMessage("angal.medicalstock.insertavalidduedate"));
					return false;
				}

				if(movement.getLot().getPreparationDate()==null){
					JOptionPane.showMessageDialog(null,MessageBundle.getMessage("angal.medicalstock.insertavalidpreparationdate"));
					return false;
				}

				if(movement.getLot().getPreparationDate().compareTo(movement.getLot().getDueDate())>0){
					JOptionPane.showMessageDialog(null,MessageBundle.getMessage("angal.medicalstock.preparationdatecannotbelaterthanduedate"));
					return false;
				}

				if((movement.getType().getType().equalsIgnoreCase("-"))&&
						(movement.getQuantity()>movement.getLot().getQuantity())){
					JOptionPane.showMessageDialog(null,MessageBundle.getMessage("angal.medicalstock.movementquantityisgreaterthanthequantityof"));
					return false;
				}

				//checks if the lot is already used by other medicals
				List<Integer> medicalIds = ioOperations.getMedicalsFromLot(movement.getLot().getCode());
				if (!(medicalIds.size()==0 || (medicalIds.size() == 1 && medicalIds.get(0).intValue() == movement.getMedical().getCode().intValue()))) {
					JOptionPane.showMessageDialog(null,	MessageBundle.getMessage("angal.medicalstock.thislotreferstoanothermedical"));
					return false;

				}
			}

			//we check movement quantity in outgoing stock case
			if (!movement.getType().getType().equals("+")) { 

				Medical medical = ioOperationsMedicals.getMedical(movement.getMedical().getCode());
				double totalQuantity = medical.getTotalQuantity() - movement.getQuantity();
				
				//we check if the outgoing movement has a quantity greater than the stocked one
				if (totalQuantity < 0) {
					JOptionPane.showMessageDialog(null, MessageBundle.getMessage("angal.medicalstock.thetotalquantitycannotbelessthan"));
					return false;
				}
				
				//we check if we are near to critical quantity
				if (totalQuantity < medical.getMinqty()) {
					int reply = JOptionPane.showConfirmDialog(
							null,
							MessageBundle.getMessage("angal.medicalstock.youaregoingtorununderthecritical"),
							MessageBundle.getMessage("angal.medicalstock.select"), JOptionPane.YES_NO_OPTION);
					boolean abort = reply == JOptionPane.NO_OPTION;
					
					if (abort) return false;
				}		
			}

			return ioOperations.newMovement(movement);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return false;
		}
	}

	/*
	 * Moved to medstockmovtype module
	 * @return
	 
	public ArrayList<MovementType> getMovementType(){
		return ioOperations.getMovementType();
	}*/

	/**
	 * Retrieves all the {@link Lot} associated to the specified {@link Medical}.
	 * In case of error a message error is shown and a <code>null</code> value is returned.
	 * @param medical the medical.
	 * @return the retrieved lots.
	 */
	public ArrayList<Lot> getLotByMedical(Medical medical){
		if(medical==null){
			return new ArrayList<Lot>();
		}
		try {
			return ioOperations.getLotsByMedical(medical);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return null;
		}
	}

	/**
	 * Checks if the provided quantity is under the medical limits.
	 * In case of error a message error is shown and a <code>false</code> value is returned.
	 * @param medicalSelected the selected medical.
	 * @param specifiedQuantity the quantity provided by the user.
	 * @return <code>true</code> if is under the limit, false otherwise.
	 */
	public boolean alertCriticalQuantity(Medical medicalSelected, int specifiedQuantity) {
		try {
			Medical medical = ioOperationsMedicals.getMedical(medicalSelected.getCode());
			double totalQuantity = medical.getTotalQuantity();
			double residual = totalQuantity - specifiedQuantity;			
			return residual < medical.getMinqty();
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return false;
		}
	}

}