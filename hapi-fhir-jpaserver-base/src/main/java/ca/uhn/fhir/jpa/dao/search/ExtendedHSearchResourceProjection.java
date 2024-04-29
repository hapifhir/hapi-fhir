/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.dao.search;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.parser.IParser;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;

/**
 * Query result when fetching full resources from HSearch.
 */
public class ExtendedHSearchResourceProjection {
	public static final String RESOURCE_NOT_STORED_ERROR = "Resource not stored in search index: ";

	final long myPid;
	final String myForcedId;
	final String myResourceString;

	public ExtendedHSearchResourceProjection(long thePid, String theForcedId, String theResourceString) {
		if (StringUtils.isEmpty(theResourceString)) {
			throw new ResourceNotFoundInIndexException(Msg.code(2130) + RESOURCE_NOT_STORED_ERROR + thePid);
		}
		myPid = thePid;
		myForcedId = theForcedId;
		myResourceString = theResourceString;
	}

	public IBaseResource toResource(IParser theParser) {
		IBaseResource result = theParser.parseResource(myResourceString);

		IdDt id;
		if (myForcedId != null) {
			id = new IdDt(myForcedId);
		} else {
			id = new IdDt(myPid);
		}
		result.setId(id);

		return result;
	}

	public long getPid() {
		return myPid;
	}
}
