package ca.uhn.fhir.jpa.searchparam.util;

/*-
 * #%L
 * HAPI FHIR Search Parameters
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.param.QuantityParam;
import ca.uhn.fhir.rest.param.ReferenceParam;
import org.hl7.fhir.dstu3.model.Location;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.Collection;
import java.util.List;


/**
 * In DSTU3, the near-distance search parameter is separate from near.  In this utility method,
 * we search for near-distance search parameters and if we find any, remove them from the list
 * of search parameters and store it in a dedicated field in {@link SearchParameterMap}.  This is so that
 * when the "near" search parameter is processed, we have access to this near-distance value.
 * This requires at most one near-distance parameter.  If more are found, we throw an {@link IllegalArgumentException}.
 */
public class Dstu3DistanceHelper {
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
							throw new IllegalArgumentException(Msg.code(494) + "Only one " + Location.SP_NEAR_DISTANCE + " parameter may be present");
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
		long sum = theParamAndList.stream().mapToLong(Collection::size).sum();

		// No near-distance Param
		if (sum == 0) {
			return null;
		// A single near-distance Param
		} else if (sum == 1) {
			return (QuantityParam) theParamAndList.get(0).get(0);
		// Too many near-distance params
		} else {
			throw new IllegalArgumentException(Msg.code(495) + "Only one " + Location.SP_NEAR_DISTANCE + " parameter may be present");
		}
	}
}
