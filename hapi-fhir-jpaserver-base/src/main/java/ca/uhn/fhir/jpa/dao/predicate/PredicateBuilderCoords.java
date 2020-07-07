package ca.uhn.fhir.jpa.dao.predicate;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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

import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.dao.SearchBuilder;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamCoords;
import ca.uhn.fhir.jpa.util.CoordCalculator;
import ca.uhn.fhir.jpa.util.SearchBox;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.dstu2.resource.Location;
import ca.uhn.fhir.rest.param.QuantityParam;
import ca.uhn.fhir.rest.param.SpecialParam;
import ca.uhn.fhir.rest.param.TokenParam;
import com.google.common.annotations.VisibleForTesting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Component
@Scope("prototype")
public class PredicateBuilderCoords extends BasePredicateBuilder implements IPredicateBuilder {
	private static final Logger ourLog = LoggerFactory.getLogger(PredicateBuilderCoords.class);

	PredicateBuilderCoords(SearchBuilder theSearchBuilder) {
		super(theSearchBuilder);
	}

	private Predicate createPredicateCoords(IQueryParameterType theParam,
														 String theResourceName,
														 RuntimeSearchParam theSearchParam,
														 CriteriaBuilder theBuilder,
														 From<?, ResourceIndexedSearchParamCoords> theFrom,
														 RequestPartitionId theRequestPartitionId) {
		String latitudeValue;
		String longitudeValue;
		Double distanceKm = 0.0;

		if (theParam instanceof TokenParam) { // DSTU3
			TokenParam param = (TokenParam) theParam;
			String value = param.getValue();
			String[] parts = value.split(":");
			if (parts.length != 2) {
				throw new IllegalArgumentException("Invalid position format '" + value + "'.  Required format is 'latitude:longitude'");
			}
			latitudeValue = parts[0];
			longitudeValue = parts[1];
			if (isBlank(latitudeValue) || isBlank(longitudeValue)) {
				throw new IllegalArgumentException("Invalid position format '" + value + "'.  Both latitude and longitude must be provided.");
			}
			QuantityParam distanceParam = myParams.getNearDistanceParam();
			if (distanceParam != null) {
				distanceKm = distanceParam.getValue().doubleValue();
			}
		} else if (theParam instanceof SpecialParam) { // R4
			SpecialParam param = (SpecialParam) theParam;
			String value = param.getValue();
			String[] parts = value.split("\\|");
			if (parts.length < 2 || parts.length > 4) {
				throw new IllegalArgumentException("Invalid position format '" + value + "'.  Required format is 'latitude|longitude' or 'latitude|longitude|distance' or 'latitude|longitude|distance|units'");
			}
			latitudeValue = parts[0];
			longitudeValue = parts[1];
			if (isBlank(latitudeValue) || isBlank(longitudeValue)) {
				throw new IllegalArgumentException("Invalid position format '" + value + "'.  Both latitude and longitude must be provided.");
			}
			if (parts.length >= 3) {
				String distanceString = parts[2];
				if (!isBlank(distanceString)) {
					distanceKm = Double.valueOf(distanceString);
				}
			}
		} else {
			throw new IllegalArgumentException("Invalid position type: " + theParam.getClass());
		}

		Predicate latitudePredicate;
		Predicate longitudePredicate;
		if (distanceKm == 0.0) {
			latitudePredicate = theBuilder.equal(theFrom.get("myLatitude"), latitudeValue);
			longitudePredicate = theBuilder.equal(theFrom.get("myLongitude"), longitudeValue);
		} else if (distanceKm < 0.0) {
			throw new IllegalArgumentException("Invalid " + Location.SP_NEAR_DISTANCE + " parameter '" + distanceKm + "' must be >= 0.0");
		} else if (distanceKm > CoordCalculator.MAX_SUPPORTED_DISTANCE_KM) {
			throw new IllegalArgumentException("Invalid " + Location.SP_NEAR_DISTANCE + " parameter '" + distanceKm + "' must be <= " + CoordCalculator.MAX_SUPPORTED_DISTANCE_KM);
		} else {
			double latitudeDegrees = Double.parseDouble(latitudeValue);
			double longitudeDegrees = Double.parseDouble(longitudeValue);

			SearchBox box = CoordCalculator.getBox(latitudeDegrees, longitudeDegrees, distanceKm);
			latitudePredicate = latitudePredicateFromBox(theBuilder, theFrom, box);
			longitudePredicate = longitudePredicateFromBox(theBuilder, theFrom, box);
		}
		Predicate singleCode = theBuilder.and(latitudePredicate, longitudePredicate);
		return combineParamIndexPredicateWithParamNamePredicate(theResourceName, theSearchParam.getName(), theFrom, singleCode, theRequestPartitionId);
	}

	private Predicate latitudePredicateFromBox(CriteriaBuilder theBuilder, From<?, ResourceIndexedSearchParamCoords> theFrom, SearchBox theBox) {
		return theBuilder.and(
			theBuilder.greaterThanOrEqualTo(theFrom.get("myLatitude"), theBox.getSouthWest().getLatitude()),
			theBuilder.lessThanOrEqualTo(theFrom.get("myLatitude"), theBox.getNorthEast().getLatitude())
		);
	}

	@VisibleForTesting
	Predicate longitudePredicateFromBox(CriteriaBuilder theBuilder, From<?, ResourceIndexedSearchParamCoords> theFrom, SearchBox theBox) {
		if (theBox.crossesAntiMeridian()) {
			return theBuilder.or(
				theBuilder.greaterThanOrEqualTo(theFrom.get("myLongitude"), theBox.getNorthEast().getLongitude()),
				theBuilder.lessThanOrEqualTo(theFrom.get("myLongitude"), theBox.getSouthWest().getLongitude())
			);
		}
		return theBuilder.and(
			theBuilder.greaterThanOrEqualTo(theFrom.get("myLongitude"), theBox.getSouthWest().getLongitude()),
			theBuilder.lessThanOrEqualTo(theFrom.get("myLongitude"), theBox.getNorthEast().getLongitude())
		);
	}

	@Override
	public Predicate addPredicate(String theResourceName,
											RuntimeSearchParam theSearchParam,
											List<? extends IQueryParameterType> theList,
											SearchFilterParser.CompareOperation theOperation,
											RequestPartitionId theRequestPartitionId) {
		From<?, ResourceIndexedSearchParamCoords> join = myQueryStack.createJoin(SearchBuilderJoinEnum.COORDS, theSearchParam.getName());

		if (theList.get(0).getMissing() != null) {
			addPredicateParamMissingForNonReference(theResourceName, theSearchParam.getName(), theList.get(0).getMissing(), join, theRequestPartitionId);
			return null;
		}

		List<Predicate> codePredicates = new ArrayList<>();
		addPartitionIdPredicate(theRequestPartitionId, join, codePredicates);

		for (IQueryParameterType nextOr : theList) {

			Predicate singleCode = createPredicateCoords(nextOr,
				theResourceName,
				theSearchParam,
                    myCriteriaBuilder,
				join,
                    theRequestPartitionId);
			codePredicates.add(singleCode);
		}

		Predicate retVal = myCriteriaBuilder.or(toArray(codePredicates));
		myQueryStack.addPredicateWithImplicitTypeSelection(retVal);
		return retVal;
	}
}
