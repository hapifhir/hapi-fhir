package ca.uhn.fhir.cql.common.provider;

/*-
 * #%L
 * HAPI FHIR - Clinical Quality Language
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

import ca.uhn.fhir.cql.dstu3.provider.MeasureOperationsProvider;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CqlProviderFactory {
	@Autowired
	private EvaluationProviderFactory myEvaluationProviderFactory;
	@Autowired
	private DaoRegistry myDaoRegistry;

	// Dstu3 Instances
	@Autowired(required = false)
	private org.opencds.cqf.tooling.library.stu3.NarrativeProvider myNarrativeProviderDstu3;
	@Autowired(required = false)
	private ca.uhn.fhir.cql.dstu3.provider.HQMFProvider myHQMFProviderDstu3;
	@Autowired(required = false)
	private ca.uhn.fhir.cql.dstu3.provider.LibraryOperationsProvider myLibraryOperationsProviderDstu3;
	@Autowired(required = false)
	private ca.uhn.fhir.jpa.rp.dstu3.MeasureResourceProvider myMeasureResourceProviderDstu3;

	// R4 Instances
	@Autowired(required = false)
	private org.opencds.cqf.tooling.library.r4.NarrativeProvider myNarrativeProviderR4;
	@Autowired(required = false)
	private ca.uhn.fhir.cql.r4.provider.HQMFProvider myHQMFProviderR4;
	@Autowired(required = false)
	private ca.uhn.fhir.cql.r4.provider.LibraryOperationsProvider myLibraryOperationsProviderR4;
	@Autowired(required = false)
	private ca.uhn.fhir.jpa.rp.r4.MeasureResourceProvider myMeasureResourceProviderR4;

	public Object getMeasureOperationsProvider() {
		if (myNarrativeProviderDstu3 != null) {
			return new MeasureOperationsProvider(myDaoRegistry, myEvaluationProviderFactory, myNarrativeProviderDstu3, myHQMFProviderDstu3, myLibraryOperationsProviderDstu3, myMeasureResourceProviderDstu3);
		}
		if (myNarrativeProviderR4 != null) {
			return new ca.uhn.fhir.cql.r4.provider.MeasureOperationsProvider(myDaoRegistry, myEvaluationProviderFactory, myNarrativeProviderR4, myHQMFProviderR4, myLibraryOperationsProviderR4, myMeasureResourceProviderR4);
		}
		throw new UnsupportedOperationException("No NarrativeProvider in context");
	}
}
