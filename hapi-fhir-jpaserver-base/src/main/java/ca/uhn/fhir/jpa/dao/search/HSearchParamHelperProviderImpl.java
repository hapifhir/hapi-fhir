package ca.uhn.fhir.jpa.dao.search;

import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import ca.uhn.fhir.rest.server.util.ResourceSearchParams;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class HSearchParamHelperProviderImpl implements IHSearchParamHelperProvider {

	@Autowired
	private ISearchParamRegistry mySearchParamRegistry;


//	fixme jm: cache this
	@Override
	public HSearchParamHelper<?> provideHelper(String theResourceTypeName, String theParamName) {
		Optional<RestSearchParameterTypeEnum> paramTypeOpt = getParamType(theResourceTypeName, theParamName);

		if (paramTypeOpt.isEmpty()) {
			//	fixme jm: code
			throw new InternalErrorException(Msg.code(0) + "Failed to obtain parameter type for resource " +
				theResourceTypeName + " and parameter " + theParamName);
		}

		HSearchParamHelper<?> helper = HSearchParamHelper.getTypeHelperMap().get(paramTypeOpt.get());

		if (helper == null) {
			//	fixme jm: code
			throw new InternalErrorException(Msg.code(0) +
				"HSearchParamHelper.myTypeHelperMap doesn't contain an entry for " + paramTypeOpt.get());
		}

		return helper;
	}


	public Optional<RestSearchParameterTypeEnum> getParamType(String theResourceTypeName, String theParamName) {
		ResourceSearchParams activeSearchParams = mySearchParamRegistry.getActiveSearchParams(theResourceTypeName);
		RuntimeSearchParam searchParam = activeSearchParams.get(theParamName);
		if (searchParam == null) {
			return Optional.empty();
		}

		return Optional.of(searchParam.getParamType());
	}



}
