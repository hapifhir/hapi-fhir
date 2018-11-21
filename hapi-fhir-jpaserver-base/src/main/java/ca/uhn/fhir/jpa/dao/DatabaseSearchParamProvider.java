package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.jpa.searchparam.BaseSearchParamRegistry;
import ca.uhn.fhir.jpa.searchparam.ISearchParamProvider;
import ca.uhn.fhir.model.dstu2.valueset.ResourceTypeEnum;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

public class DatabaseSearchParamProvider implements ISearchParamProvider {
	@Autowired
	private PlatformTransactionManager myTxManager;
	@Autowired
	private DaoRegistry myDaoRegistry;

	@Override
	public IBundleProvider search(SearchParameterMap theParams) {
		return myDaoRegistry.getResourceDao(ResourceTypeEnum.SEARCHPARAMETER.getCode()).search(theParams);
	}

	@Override
	public <SP extends IBaseResource> void refreshCache(BaseSearchParamRegistry<SP> theSearchParamRegistry, long theRefreshInterval) {
		TransactionTemplate txTemplate = new TransactionTemplate(myTxManager);
		txTemplate.execute(t->{
			theSearchParamRegistry.doRefresh(theRefreshInterval);
			return null;
		});

	}
}
