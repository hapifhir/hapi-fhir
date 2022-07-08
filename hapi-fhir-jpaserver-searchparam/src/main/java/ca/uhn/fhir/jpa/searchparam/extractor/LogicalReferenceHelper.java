package ca.uhn.fhir.jpa.searchparam.extractor;

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

import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.Set;

import static org.apache.commons.lang3.StringUtils.trim;

public class LogicalReferenceHelper {

	public static boolean isLogicalReference(ModelConfig myConfig, IIdType theId) {
		Set<String> treatReferencesAsLogical = myConfig.getTreatReferencesAsLogical();
		if (treatReferencesAsLogical != null) {
			for (String nextLogicalRef : treatReferencesAsLogical) {
				nextLogicalRef = trim(nextLogicalRef);
				if (nextLogicalRef.charAt(nextLogicalRef.length() - 1) == '*') {
					if (theId.getValue().startsWith(nextLogicalRef.substring(0, nextLogicalRef.length() - 1))) {
						return true;
					}
				} else {
					if (theId.getValue().equals(nextLogicalRef)) {
						return true;
					}
				}
			}

		}

		/*
		 * Account for common logical references
		 */

		if (theId.getValue().startsWith("http://fhir.org/guides/argonaut/")) {
			return true;
		}

		return false;
	}


}
