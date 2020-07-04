package ca.uhn.fhir.empi.rules.metric.matcher;

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

import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.instance.model.api.IBase;

public class HapiDateMatcher implements IEmpiFieldMatcher {
	private final HapiDateMatcherDstu3 myHapiDateMatcherDstu3 = new HapiDateMatcherDstu3();
	private final HapiDateMatcherR4 myHapiDateMatcherR4 = new HapiDateMatcherR4();

	@Override
	public boolean matches(FhirContext theFhirContext, IBase theLeftBase, IBase theRightBase, boolean theExact) {
		switch (theFhirContext.getVersion().getVersion()) {
			case DSTU3:
				return myHapiDateMatcherDstu3.match(theLeftBase, theRightBase);
			case R4:
				return myHapiDateMatcherR4.match(theLeftBase, theRightBase);
			default:
				throw new UnsupportedOperationException("Version not supported: " + theFhirContext.getVersion().getVersion());
		}
	}
}
