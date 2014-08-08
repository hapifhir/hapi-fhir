package ca.uhn.fhir.jpa.provider;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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

public class JpaSystemProvider extends BaseJpaProvider {

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
	public List<IResource> transaction(HttpServletRequest theRequest, @TransactionParam List<IResource> theResources) {
		startRequest(theRequest);
		try {
			return myDao.transaction(theResources);
		} finally {
			endRequest(theRequest);
		}
	}

	@History
	public IBundleProvider historyServer(HttpServletRequest theRequest, @Since Date theDate) {
		startRequest(theRequest);
		try {
			return myDao.history(theDate);
		} finally {
			endRequest(theRequest);
		}
	}

	@GetTags
	public TagList getAllTagsOnServer(HttpServletRequest theRequest) {
		startRequest(theRequest);
		try {
		return myDao.getAllTags();
		} finally {
			endRequest(theRequest);
		}
	}

}
