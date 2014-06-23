package ca.uhn.fhir.jpa.provider;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;

import ca.uhn.fhir.jpa.dao.IFhirSystemDao;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.rest.annotation.GetTags;
import ca.uhn.fhir.rest.annotation.History;
import ca.uhn.fhir.rest.annotation.Since;
import ca.uhn.fhir.rest.annotation.Transaction;
import ca.uhn.fhir.rest.annotation.TransactionParam;
import ca.uhn.fhir.rest.server.IBundleProvider;

public class JpaSystemProvider {

	private IFhirSystemDao myDao;

	public JpaSystemProvider() {
		// nothing
	}

	public JpaSystemProvider(IFhirSystemDao theDao) {
		myDao = theDao;
	}

	@Required
	public void setDao(IFhirSystemDao theDao) {
		myDao = theDao;
	}

	@Transaction
	public List<IResource> transaction(@TransactionParam List<IResource> theResources) {
		myDao.transaction(theResources);
		return theResources;
	}

	@History
	public IBundleProvider historyServer(@Since Date theDate) {
		return myDao.history(theDate);
	}
	
	@GetTags
	public TagList getAllTagsOnServer() {
		return myDao.getAllTags();
	}
	
}
