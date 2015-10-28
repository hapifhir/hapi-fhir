package ca.uhn.fhir.jpa.provider;

import org.springframework.beans.factory.annotation.Required;

import ca.uhn.fhir.jpa.dao.IFhirSystemDao;

public class BaseJpaPlainProvider extends BaseJpaProvider {

	private IFhirSystemDao<?> myDao;

	public BaseJpaPlainProvider() {
		// nothing
	}

	@Required
	public void setDao(IFhirSystemDao<?> theDao) {
		myDao = theDao;
	}

	public IFhirSystemDao<?> getDao() {
		return myDao;
	}

}
