/*-
 * #%L
 * HAPI FHIR Subscription Server
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
package ca.uhn.fhir.jpa.topic;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import org.hl7.fhir.convertors.factory.VersionConvertorFactory_43_50;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.model.SubscriptionTopic;

public final class SubscriptionTopicCanonicalizer {
	private static final FhirContext ourFhirContextR5 = FhirContext.forR5();

	private SubscriptionTopicCanonicalizer() {}

	public static SubscriptionTopic canonicalizeTopic(FhirContext theFhirContext, IBaseResource theSubscriptionTopic) {
		switch (theFhirContext.getVersion().getVersion()) {
			case R4B:
				return (SubscriptionTopic) VersionConvertorFactory_43_50.convertResource(
						(org.hl7.fhir.r4b.model.SubscriptionTopic) theSubscriptionTopic);
			case R5:
				return (SubscriptionTopic) theSubscriptionTopic;
			default:
				throw new UnsupportedOperationException(
						Msg.code(2337) + "Subscription topics are not supported in FHIR version "
								+ theFhirContext.getVersion().getVersion());
		}
	}
}
