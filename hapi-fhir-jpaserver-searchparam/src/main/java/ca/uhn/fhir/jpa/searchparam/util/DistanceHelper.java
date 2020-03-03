package ca.uhn.fhir.jpa.searchparam.util;

import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.param.QuantityParam;
import ca.uhn.fhir.rest.param.ReferenceParam;
import org.hl7.fhir.dstu3.model.Location;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.List;

public class DistanceHelper {
	public static void setNearDistance(Class<? extends IBaseResource> theResourceType, SearchParameterMap theParams) {
		if (theResourceType == Location.class && theParams.containsKey(Location.SP_NEAR_DISTANCE)) {
			List<List<IQueryParameterType>> paramAndList = theParams.get(Location.SP_NEAR_DISTANCE);
			QuantityParam quantityParam = getNearDistanceParam(paramAndList);
			theParams.setNearDistanceParam(quantityParam);

			// Need to remove near-distance or it we'll get a hashcode predicate for it
			theParams.remove(Location.SP_NEAR_DISTANCE);
		} else if (theParams.containsKey("location")) {
			List<List<IQueryParameterType>> paramAndList = theParams.get("location");
			ReferenceParam referenceParam = getChainedLocationNearDistanceParam(paramAndList);
			if (referenceParam != null) {
				QuantityParam quantityParam = new QuantityParam(referenceParam.getValue());
				theParams.setNearDistanceParam(quantityParam);
			}
		}
	}

	private static ReferenceParam getChainedLocationNearDistanceParam(List<List<IQueryParameterType>> theParamAndList) {
		ReferenceParam retval = null;
		List<IQueryParameterType> andParamToRemove = null;
		for (List<IQueryParameterType> paramOrList : theParamAndList) {
			IQueryParameterType orParamToRemove = null;
			for (IQueryParameterType param : paramOrList) {
				if (param instanceof ReferenceParam) {
					ReferenceParam referenceParam = (ReferenceParam) param;
					if (Location.SP_NEAR_DISTANCE.equals(referenceParam.getChain())) {
						if (retval != null) {
							throw new IllegalArgumentException("Only one " + ca.uhn.fhir.model.dstu2.resource.Location.SP_NEAR_DISTANCE + " parameter may be present");
						} else {
							retval = referenceParam;
							orParamToRemove = param;
						}
					}
				}
			}
			if (orParamToRemove != null) {
				paramOrList.remove(orParamToRemove);
				if (paramOrList.isEmpty()) {
					andParamToRemove = paramOrList;
				}
			}
		}
		if (andParamToRemove != null) {
			theParamAndList.remove(andParamToRemove);
		}
		return retval;
	}

	private static QuantityParam getNearDistanceParam(List<List<IQueryParameterType>> theParamAndList) {
		if (theParamAndList.isEmpty()) {
			return null;
		}
		if (theParamAndList.size() > 1) {
			throw new IllegalArgumentException("Only one " + ca.uhn.fhir.model.dstu2.resource.Location.SP_NEAR_DISTANCE + " parameter may be present");
		}
		List<IQueryParameterType> paramOrList = theParamAndList.get(0);
		if (paramOrList.isEmpty()) {
			return null;
		}
		if (paramOrList.size() > 1) {
			throw new IllegalArgumentException("Only one " + ca.uhn.fhir.model.dstu2.resource.Location.SP_NEAR_DISTANCE + " parameter may be present");
		}
		return (QuantityParam) paramOrList.get(0);
	}
}
