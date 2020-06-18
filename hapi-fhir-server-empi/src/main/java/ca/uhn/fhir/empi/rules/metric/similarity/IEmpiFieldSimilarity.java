package ca.uhn.fhir.empi.rules.metric.similarity;

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
import ca.uhn.fhir.empi.rules.metric.IEmpiFieldMetric;
import org.hl7.fhir.instance.model.api.IBase;

/**
 * Measure how similar two IBase (resource fields) are to one another.  1.0 means identical.  0.0 means completely different.
 */
public interface IEmpiFieldSimilarity extends IEmpiFieldMetric {
	double similarity(FhirContext theFhirContext, IBase theLeftBase, IBase theRightBase, boolean theExact);
}
