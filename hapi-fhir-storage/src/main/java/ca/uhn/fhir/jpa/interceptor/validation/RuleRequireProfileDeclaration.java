package ca.uhn.fhir.jpa.interceptor.validation;

/*-
 * #%L
 * HAPI FHIR Storage api
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
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hl7.fhir.instance.model.api.IBaseResource;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Optional;

class RuleRequireProfileDeclaration extends BaseTypedRule {
	private final Collection<String> myProfileOptions;

	RuleRequireProfileDeclaration(FhirContext theFhirContext, String theType, Collection<String> theProfileOptions) {
		super(theFhirContext, theType);
		myProfileOptions = theProfileOptions;
	}

	@Nonnull
	@Override
	public IRepositoryValidatingRule.RuleEvaluation evaluate(RequestDetails theRequestDetails, @Nonnull IBaseResource theResource) {
		Optional<String> matchingProfile = theResource
			.getMeta()
			.getProfile()
			.stream()
			.map(t -> t.getValueAsString())
			.filter(t -> myProfileOptions.contains(t))
			.findFirst();
		if (matchingProfile.isPresent()) {
			return IRepositoryValidatingRule.RuleEvaluation.forSuccess(this);
		}
		String msg = getFhirContext().getLocalizer().getMessage(RuleRequireProfileDeclaration.class, "noMatchingProfile", getResourceType(), myProfileOptions);
		return IRepositoryValidatingRule.RuleEvaluation.forFailure(this, msg);
	}


	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
			.append("resourceType", getResourceType())
			.append("profiles", myProfileOptions)
			.toString();
	}


}
