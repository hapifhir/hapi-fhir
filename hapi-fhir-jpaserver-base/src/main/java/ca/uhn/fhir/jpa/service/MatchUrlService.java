package ca.uhn.fhir.jpa.service;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.jpa.dao.BaseHapiFhirDao;
import ca.uhn.fhir.jpa.dao.IDao;
import ca.uhn.fhir.jpa.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.dao.SearchParameterMap;
import ca.uhn.fhir.jpa.subscription.DaoProvider;
import ca.uhn.fhir.model.api.IQueryParameterAnd;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.QualifiedParamList;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.ParameterUtil;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import com.google.common.collect.ArrayListMultimap;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Service
public class MatchUrlService {

	@Autowired
	private FhirContext myContext;
	@Autowired
	private DaoProvider myDaoProvider;

	public <R extends IBaseResource> Set<Long> processMatchUrl(IDao theCallingDao, String theMatchUrl, Class<R> theResourceType) {
		RuntimeResourceDefinition resourceDef = myContext.getResourceDefinition(theResourceType);

		SearchParameterMap paramMap = BaseHapiFhirDao.translateMatchUrl(theCallingDao, myContext, theMatchUrl, resourceDef);
		paramMap.setLoadSynchronous(true);

		if (paramMap.isEmpty() && paramMap.getLastUpdated() == null) {
			throw new InvalidRequestException("Invalid match URL[" + theMatchUrl + "] - URL has no search parameters");
		}

		IFhirResourceDao<R> dao = myDaoProvider.getDao(theResourceType);
		if (dao == null) {
			throw new InternalErrorException("No DAO for resource type: " + theResourceType.getName());
		}

		return dao.searchForIds(paramMap);
	}
}
