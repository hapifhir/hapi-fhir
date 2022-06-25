package ca.uhn.fhir.jpa.dao.search;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;

import java.util.Optional;

//fixme jm: remove
public class SearchParameterHelperProvider {

	private HSearchParamHelper<?> myHSearchParamHelper;

	public SearchParameterHelperProvider(HSearchParamHelper<?> theHSearchParamHelper) {
		myHSearchParamHelper = theHSearchParamHelper;
	}


//	public HSearchParamHelper<?>  getTypeHelper(String theResourceTypeName, String theParamName) {
//		Optional<RestSearchParameterTypeEnum> paramTypeOpt = myHSearchParamHelper.getParamType(theResourceTypeName, theParamName);
////			fixme jm: code
//		String errMsg = Msg.code(0) + "Failed to obtain parameter type for resource " +
//			theResourceTypeName + " and parameter " + theParamName;
//
//		if (paramTypeOpt.isEmpty()) { throw new InternalErrorException(errMsg); }
//
//		HSearchParamHelper<?> helper = myHSearchParamHelper.getHelperForParamType(paramTypeOpt.get());
//
//		if (helper == null) { throw new InternalErrorException(errMsg); }
//
//		return helper;
//	}
}
