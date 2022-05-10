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
import ca.uhn.fhir.util.UrlUtil;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Set;

class RuleDisallowProfile extends BaseTypedRule {
	private final Set<String> myProfileUrls;

	RuleDisallowProfile(FhirContext theFhirContext, String theResourceType, String[] theProfileUrls) {
		super(theFhirContext, theResourceType);
		Validate.notNull(theProfileUrls);
		Validate.notEmpty(theProfileUrls);
		myProfileUrls = new HashSet<>();
		for (String theProfileUrl : theProfileUrls) {
			myProfileUrls.add(UrlUtil.normalizeCanonicalUrlForComparison(theProfileUrl));
		}
	}

	@Nonnull
	@Override
	public RuleEvaluation evaluate(RequestDetails theRequestDetails, @Nonnull IBaseResource theResource) {
		for (IPrimitiveType<String> next : theResource.getMeta().getProfile()) {
			String nextUrl = next.getValueAsString();
			String nextUrlNormalized = UrlUtil.normalizeCanonicalUrlForComparison(nextUrl);
			if (myProfileUrls.contains(nextUrlNormalized)) {
				String msg = getFhirContext().getLocalizer().getMessage(RuleRequireProfileDeclaration.class, "illegalProfile", getResourceType(), nextUrl);
				return RuleEvaluation.forFailure(this, msg);
			}
		}

		return RuleEvaluation.forSuccess(this);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
			.append("resourceType", getResourceType())
			.append("profiles", myProfileUrls)
			.toString();
	}


}
