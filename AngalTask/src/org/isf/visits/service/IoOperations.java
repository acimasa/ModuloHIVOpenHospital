package org.isf.visits.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.isf.generaldata.MessageBundle;
import org.isf.patient.model.Patient;
import org.isf.utils.db.DbQueryLogger;
import org.isf.utils.exception.OHException;
import org.isf.visits.model.Visit;

public class IoOperations {

	/**
	 * returns the list of all {@link Visit}s related to a patID
	 * 
	 * @param patID - the {@link Patient} ID. If <code>0</code> return the list of all {@link Visit}s
	 * @return the list of {@link Visit}s
	 * @throws OHException 
	 */
	public ArrayList<Visit> getVisits(Integer patID) throws OHException {
		DbQueryLogger dbQuery = new DbQueryLogger();
		ArrayList<Visit> visits = null;
		List<Object> parameters = Collections.<Object>singletonList(patID);
		try {
			StringBuilder query = new StringBuilder("SELECT * FROM VISITS");
			if (patID != 0) query.append(" WHERE VST_PAT_ID = ?");
			query.append(" ORDER BY VST_PAT_ID, VST_DATE");
			
			ResultSet resultSet = null;
			if (patID != 0) {
				resultSet = dbQuery.getDataWithParams(query.toString(), parameters, true);
			} else {
				resultSet = dbQuery.getData(query.toString(), true);
			}
			visits = new ArrayList<Visit>(resultSet.getFetchSize());
			while (resultSet.next()) {
				Visit visit = new Visit();
				visit.setTime(resultSet.getTimestamp("VST_DATE"));
				visit.setNote(resultSet.getString("VST_NOTE"));
				visits.add(visit);
			}
		} catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} finally{
			dbQuery.releaseConnection();
		}
		return visits;
	}

	/**
	 * replace all {@link Visit}s related to a patID
	 * 
	 * @param patID - the {@link Patient} ID
	 * @param visits - the list of {@link Visit}s related to patID. If <code>null</code> only delete all {@link Visit}s related to patID. 
	 * @return <code>true</code> if the list has been replaced, <code>false</code> otherwise
	 * @throws OHException 
	 */
	public boolean updateVisits(int patID, ArrayList<Visit> visits) throws OHException {
		DbQueryLogger dbQuery = new DbQueryLogger();
		ArrayList<Object> parameters = new ArrayList<Object>();
		boolean result = false;
		try {
			String query = "DELETE FROM VISITS WHERE VST_PAT_ID = ?";
			parameters.add(patID);
			dbQuery.setDataWithParams(query, parameters, false);
			
			if (visits != null) {
				query = "INSERT INTO VISITS (VST_PAT_ID, VST_DATE, VST_NOTE) VALUES (?, ?, ?)";
				parameters.clear();
				for (Visit visit : visits) {
					parameters.add(patID);
					parameters.add(visit.getTime());
					parameters.add(visit.getNote());
					
					result = result && dbQuery.setDataWithParams(query, parameters, false);
					parameters.clear();
				}
			}
			if (result) {
				dbQuery.commit();
			} else {
				dbQuery.rollback();
			}
		} finally {
			dbQuery.releaseConnection();
		}
		return result;
	}
}
