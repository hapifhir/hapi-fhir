package ca.uhn.fhir.rest.param;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import ca.uhn.fhir.model.dstu.valueset.SearchParamTypeEnum;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.util.QueryUtil;

public abstract class IQueryParameter implements IParameter {

	public abstract List<List<String>> encode(Object theObject) throws InternalErrorException;

	public abstract String getName();

	public abstract Object parse(List<List<String>> theString) throws InternalErrorException, InvalidRequestException;

	public abstract boolean isRequired();
	
	/**
	 * Parameter should return true if {@link #parse(List)} should be called even
	 * if the query string contained no values for the given parameter 
	 */
	public abstract boolean handlesMissing();

	public abstract SearchParamTypeEnum getParamType();

	@Override
	public void translateClientArgumentIntoQueryArgument(Object theSourceClientArgument, Map<String, List<String>> theTargetQueryArguments) throws InternalErrorException {
		if (theSourceClientArgument == null) {
			if (isRequired()) {
				throw new NullPointerException("SearchParameter '" + getName() + "' is required and may not be null");
			}
		} else {
			List<List<String>> value = encode(theSourceClientArgument);
			ArrayList<String> paramValues = new ArrayList<String>(value.size());
			theTargetQueryArguments.put(getName(), paramValues);

			for (List<String> nextParamEntry : value) {
				StringBuilder b = new StringBuilder();
				for (String str : nextParamEntry) {
					if (b.length() > 0) {
						b.append(",");
					}
					b.append(str.replace(",", "\\,"));
				}
				paramValues.add(b.toString());
			}

		}
	}
	
	@Override
	public Object translateQueryParametersIntoServerArgument(Map<String, String[]> theQueryParameters, Object theRequestContents) throws InternalErrorException, InvalidRequestException {
		String[] value = theQueryParameters.get(getName());
		if (value == null || value.length == 0) {
			if (handlesMissing()) {
				return parse(new ArrayList<List<String>>(0));
			}else {
				return null;
			}
		}

		List<List<String>> paramList = new ArrayList<List<String>>(value.length);
		for (String nextParam : value) {
			if (nextParam.contains(",") == false) {
				paramList.add(Collections.singletonList(nextParam));
			} else {
				paramList.add(QueryUtil.splitQueryStringByCommasIgnoreEscape(nextParam));
			}
		}

		return parse(paramList);
		
	}
	

}