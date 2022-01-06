package ca.uhn.fhir.mdm.rules.matcher;

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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.util.ExtensionUtil;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseExtension;
import org.hl7.fhir.instance.model.api.IBaseHasExtensions;

import java.util.List;

public class ExtensionMatcher implements IMdmFieldMatcher {

	@Override
	public boolean matches(FhirContext theFhirContext, IBase theLeftBase, IBase theRightBase, boolean theExact, String theIdentifierSystem) {
		if (!(theLeftBase instanceof IBaseHasExtensions && theRightBase instanceof IBaseHasExtensions)) {
			return false;
		}
		List<? extends IBaseExtension<?, ?>> leftExtension = ((IBaseHasExtensions) theLeftBase).getExtension();
		List<? extends IBaseExtension<?, ?>> rightExtension = ((IBaseHasExtensions) theRightBase).getExtension();

		boolean match = false;
		for (IBaseExtension leftExtensionValue : leftExtension) {
			for (IBaseExtension rightExtensionValue : rightExtension) {
				match |= ExtensionUtil.equals(leftExtensionValue, rightExtensionValue);
			}
		}
		return match;
	}
}
