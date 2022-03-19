package ca.uhn.fhir.rest.server.util;

/*-
 * #%L
 * HAPI FHIR - Server Framework
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.context.phonetic.IPhoneticEncoder;
import ca.uhn.fhir.i18n.Msg;
import org.apache.commons.lang3.Validate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class FhirContextSearchParamRegistry implements ISearchParamRegistry {


	private final List<RuntimeSearchParam> myExtraSearchParams = new ArrayList<>();
	private final FhirContext myCtx;

	/**
	 * Constructor
	 */
	public FhirContextSearchParamRegistry(@Nonnull FhirContext theCtx) {
		Validate.notNull(theCtx, "theCtx must not be null");
		myCtx = theCtx;
	}

	@Override
	public void forceRefresh() {
		// nothing
	}

	@Override
	public RuntimeSearchParam getActiveSearchParam(String theResourceName, String theParamName) {
		return getActiveSearchParams(theResourceName).get(theParamName);
	}

	@Override
	public ResourceSearchParams getActiveSearchParams(String theResourceName) {
		ResourceSearchParams retval = new ResourceSearchParams(theResourceName);
		RuntimeResourceDefinition nextResDef = myCtx.getResourceDefinition(theResourceName);
		for (RuntimeSearchParam nextSp : nextResDef.getSearchParams()) {
			retval.put(nextSp.getName(), nextSp);
		}

		for (RuntimeSearchParam next : myExtraSearchParams) {
			retval.put(next.getName(), next);
		}

		return retval;
	}

	public void addSearchParam(RuntimeSearchParam theSearchParam) {
		myExtraSearchParams.add(theSearchParam);
	}

	@Override
	public List<RuntimeSearchParam> getActiveComboSearchParams(String theResourceName, Set<String> theParamNames) {
		throw new UnsupportedOperationException(Msg.code(2066));
	}

	@Nullable
	@Override
	public RuntimeSearchParam getActiveSearchParamByUrl(String theUrl) {
		throw new UnsupportedOperationException(Msg.code(2067));
	}

	@Override
	public List<RuntimeSearchParam> getActiveComboSearchParams(String theResourceName) {
		throw new UnsupportedOperationException(Msg.code(2068));
	}

	@Override
	public void requestRefresh() {
		// nothing
	}

	@Override
	public void setPhoneticEncoder(IPhoneticEncoder thePhoneticEncoder) {
		// nothing
	}
}
