package ca.uhn.fhir.jpa.search;

/*
 * #%L
 * HAPI FHIR JPA Server
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

import ca.uhn.fhir.jpa.entity.TermConcept;
import org.hibernate.search.mapper.pojo.bridge.RoutingBridge;
import org.hibernate.search.mapper.pojo.bridge.binding.RoutingBindingContext;
import org.hibernate.search.mapper.pojo.bridge.mapping.programmatic.RoutingBinder;
import org.hibernate.search.mapper.pojo.bridge.runtime.RoutingBridgeRouteContext;
import org.hibernate.search.mapper.pojo.route.DocumentRoutes;

public class DeferConceptIndexingRoutingBinder implements RoutingBinder {
	@Override
	public void bind(RoutingBindingContext theRoutingBindingContext) {
		theRoutingBindingContext.dependencies().use("myIndexStatus");

		theRoutingBindingContext.bridge(TermConcept.class, new TermConceptBridge());
	}

	private class TermConceptBridge implements RoutingBridge<TermConcept> {
		@Override
		public void route(DocumentRoutes theDocumentRoutes, Object theO, TermConcept theTermConcept, RoutingBridgeRouteContext theRoutingBridgeRouteContext) {
			if (theTermConcept.getIndexStatus() == null) {
				theDocumentRoutes.notIndexed();
			} else {
				theDocumentRoutes.addRoute();
			}
		}

		@Override
		public void previousRoutes(DocumentRoutes theDocumentRoutes, Object theO, TermConcept theTermConcept, RoutingBridgeRouteContext theRoutingBridgeRouteContext) {
			theDocumentRoutes.addRoute();
		}
	}
}
