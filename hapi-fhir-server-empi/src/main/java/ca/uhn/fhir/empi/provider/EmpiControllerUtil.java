package ca.uhn.fhir.empi.provider;

/*-
 * #%L
 * HAPI FHIR - Enterprise Master Patient Index
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

import ca.uhn.fhir.empi.api.EmpiLinkSourceEnum;
import ca.uhn.fhir.empi.api.EmpiMatchResultEnum;
import ca.uhn.fhir.empi.util.EmpiUtil;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.hl7.fhir.instance.model.api.IIdType;

public class EmpiControllerUtil {
	public static EmpiMatchResultEnum extractMatchResultOrNull(String theMatchResult) {
		if (theMatchResult == null) {
			return null;
		}
		return EmpiMatchResultEnum.valueOf(theMatchResult);
	}

	public static EmpiLinkSourceEnum extractLinkSourceOrNull(String theLinkSource) {
		if (theLinkSource == null) {
			return null;
		}
		return EmpiLinkSourceEnum.valueOf(theLinkSource);
	}

	public static IIdType extractPersonIdDtOrNull(String theName, String thePersonId) {
		if (thePersonId == null) {
			return null;
		}
		return getGoldenIdDtOrThrowException(theName, thePersonId);
	}

	public static IIdType extractTargetIdDtOrNull(String theName, String theTargetId) {
		if (theTargetId == null) {
			return null;
		}
		return getTargetIdDtOrThrowException(theName, theTargetId);
	}

	static IdDt getGoldenIdDtOrThrowException(String theParamName, String theId) {
		IdDt goldenResourceId = new IdDt(theId);
		//TODO GGG MDM: maybe add a gate here to only consider resources that can possibly be EMPI'ed?
		if (goldenResourceId.getIdPart() == null) {
			throw new InvalidRequestException(theParamName + " is '" + theId + "'.  must have form <resourceType>/<id> where <id> is the id of the resource");
		}
		return goldenResourceId;
	}

	public static IIdType getTargetIdDtOrThrowException(String theParamName, String theId) {
		IdDt targetId = new IdDt(theId);
		String resourceType = targetId.getResourceType();
		if (!EmpiUtil.supportedTargetType(resourceType) ||
			targetId.getIdPart() == null) {
			throw new InvalidRequestException(theParamName + " is '" + theId + "'.  must have form Patient/<id> or Practitioner/<id> where <id> is the id of the resource");
		}
		return targetId;
	}
}
