package org.isf.priceslist.manager;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.isf.priceslist.model.List;
import org.isf.priceslist.model.Price;
import org.isf.priceslist.service.IoOperations;
import org.isf.utils.exception.OHException;

public class PriceListManager {

	private IoOperations ioOperations = new IoOperations();
	
	/**
	 * return the list of {@link List}s in the DB
	 * @return the list of {@link List}s
	 */
	public ArrayList<List> getLists() {
		try {
			return ioOperations.getLists();
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return null;
		} 
	}
	
	/**
	 * return the list of {@link Price}s in the DB
	 * @return the list of {@link Price}s
	 */
	public ArrayList<Price> getPrices() {
		try {
			return ioOperations.getPrices();
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return null;
		}
	}

	/**
	 * updates all {@link Price}s in the specified {@link List}
	 * @param list - the {@link List}
	 * @param prices - the list of {@link Price}s
	 * @return <code>true</code> if the list has been replaced, <code>false</code> otherwise
	 */
	public boolean updatePrices(List list, ArrayList<Price> prices) {
		try {
			return ioOperations.updatePrices(list, prices);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return false;
		}
	}

	/**
	 * insert a new {@link List} in the DB
	 * 
	 * @param list - the {@link List}
	 * @return <code>true</code> if the list has been inserted, <code>false</code> otherwise
	 */
	public boolean newList(List list) {
		try {
			return ioOperations.newList(list);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return false;
		}
	}

	/**
	 * update a {@link List} in the DB
	 * 
	 * @param list - the {@link List} to update
	 * @return <code>true</code> if the list has been updated, <code>false</code> otherwise
	 */
	public boolean updateList(List updateList) {
		try {
			return ioOperations.updateList(updateList);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return false;
		}
	}

	/**
	 * delete a {@link List} in the DB
	 * 
	 * @param list - the {@link List} to delete
	 * @return <code>true</code> if the list has been deleted, <code>false</code> otherwise
	 */
	public boolean deleteList(List deleteList) {
		try {
			return ioOperations.deleteList(deleteList);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return false;
		}
	}

	/**
	 * duplicate specified {@List list}
	 * 
	 * @param list
	 * @return <code>true</code> if the list has been duplicated, <code>false</code> otherwise
	 */
	public boolean copyList(List list) {
		return copyList(list, 1., 0.);
	}
	
	/**
	 * duplicate {@link list} multiplying by <code>factor</code> and rounding by <code>step</code>
	 * 
	 * @param list - the {@link list} to be duplicated
	 * @param factor - the multiplying factor
	 * @param step - the rounding step
	 * @return <code>true</code> if the list has been duplicated, <code>false</code> otherwise
	 */
	public boolean copyList(List list, double factor, double step) {
		try {
			return ioOperations.copyList(list, factor, step);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return false;
		}
	}
}