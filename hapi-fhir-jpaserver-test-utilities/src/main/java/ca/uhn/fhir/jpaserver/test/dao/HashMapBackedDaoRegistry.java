package ca.uhn.fhir.jpaserver.test.dao;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.dao.DaoRegistry;
import ca.uhn.fhir.jpa.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.searchparam.matcher.SearchParamMatcher;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nullable;
import java.util.Set;

public class HashMapBackedDaoRegistry extends DaoRegistry {
	@Autowired
	FhirContext myFhirContext;
	@Autowired
	SearchParamMatcher mySearchParamMatcher;

	@Override
	public <T extends IBaseResource> IFhirResourceDao<T> getResourceDao(Class<T> theResourceType) {
		if (super.getResourceDaoOrNull(theResourceType) == null) {
			HashMapBackedDao<T> dao = new HashMapBackedDao<>(myFhirContext, theResourceType, mySearchParamMatcher);
			super.register(dao);
		}
		return super.getResourceDao(theResourceType);
	}

	public void deleteAllResources() {
		Set<String> daoTypes = super.getRegisteredDaoTypes();
		for (String daoType : daoTypes) {
			@Nullable IFhirResourceDao<IBaseResource> dao = super.getResourceDaoOrNull(daoType);
			if (dao instanceof HashMapBackedDao) {
				((HashMapBackedDao<?>)dao).clear();
			}
		}
	}
}
