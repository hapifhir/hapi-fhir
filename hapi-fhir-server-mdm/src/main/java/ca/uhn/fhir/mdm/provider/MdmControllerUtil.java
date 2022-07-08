package ca.uhn.fhir.mdm.provider;

/*-
 * #%L
 * HAPI FHIR - Master Data Management
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
import ca.uhn.fhir.mdm.api.MdmLinkSourceEnum;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.hl7.fhir.instance.model.api.IIdType;

public class MdmControllerUtil {
	public static MdmMatchResultEnum extractMatchResultOrNull(String theMatchResult) {
		if (theMatchResult == null) {
			return null;
		}
		return MdmMatchResultEnum.valueOf(theMatchResult);
	}

	public static MdmLinkSourceEnum extractLinkSourceOrNull(String theLinkSource) {
		if (theLinkSource == null) {
			return null;
		}
		return MdmLinkSourceEnum.valueOf(theLinkSource);
	}

	public static IIdType extractGoldenResourceIdDtOrNull(String theName, String theGoldenResourceId) {
		if (theGoldenResourceId == null) {
			return null;
		}
		return getGoldenIdDtOrThrowException(theName, theGoldenResourceId);
	}

	public static IIdType extractSourceIdDtOrNull(String theName, String theSourceId) {
		if (theSourceId == null) {
			return null;
		}
		return getSourceIdDtOrThrowException(theName, theSourceId);
	}

	static IdDt getGoldenIdDtOrThrowException(String theParamName, String theId) {
		IdDt goldenResourceId = new IdDt(theId);
		if (goldenResourceId.getIdPart() == null) {
			throw new InvalidRequestException(Msg.code(1505) + theParamName + " is '" + theId + "'.  must have form <resourceType>/<id> where <id> is the id of the resource");
		}
		return goldenResourceId;
	}

	public static IIdType getSourceIdDtOrThrowException(String theParamName, String theSourceId) {
		IdDt sourceId = new IdDt(theSourceId);
		if (sourceId.getIdPart() == null) {
			throw new InvalidRequestException(Msg.code(1506) + theParamName + " is '" + theSourceId + "'.  must have form <resourceType>/<id>  where <id> is the id of the resource and <resourceType> is the type of the resource");
		}
		return sourceId;
	}
}
