/*-
 * #%L
 * hapi-fhir-storage-mdm
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
package ca.uhn.fhir.mdm.batch2.clear;

import ca.uhn.fhir.batch2.api.IJobParametersValidator;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.mdm.api.IMdmSettings;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MdmClearJobParametersValidator implements IJobParametersValidator<MdmClearJobParameters> {

	private final DaoRegistry myDaoRegistry;
	private final IMdmSettings myMdmSettings;

	public MdmClearJobParametersValidator(DaoRegistry theDaoRegistry, IMdmSettings theMdmSettings) {
		myDaoRegistry = theDaoRegistry;
		myMdmSettings = theMdmSettings;
	}

	@Nullable
	@Override
	public List<String> validate(RequestDetails theRequestDetails, @Nonnull MdmClearJobParameters theParameters) {
		if (myMdmSettings == null || !myMdmSettings.isEnabled()) {
			return Collections.singletonList("Mdm is not enabled on this server");
		}
		List<String> retval = new ArrayList<>();
		if (theParameters.getResourceNames() == null
				|| theParameters.getResourceNames().isEmpty()) {
			retval.add("Mdm Clear Job Parameters must define at least one resource type");
		} else {
			for (String resourceType : theParameters.getResourceNames()) {
				if (!myDaoRegistry.isResourceTypeSupported(resourceType)) {
					retval.add("Resource type '" + resourceType + "' is not supported on this server.");
				}
				if (!myMdmSettings.getMdmRules().getMdmTypes().contains(resourceType)) {
					retval.add("There are no mdm rules for resource type '" + resourceType + "'");
				}
			}
		}

		return retval;
	}
}
