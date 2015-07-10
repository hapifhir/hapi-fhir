package ca.uhn.fhir.jpa.provider;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2015 University Health Network
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

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import ca.uhn.fhir.model.dstu2.resource.ValueSet;
import ca.uhn.fhir.model.dstu2.resource.ValueSet.ComposeInclude;
import ca.uhn.fhir.model.dstu2.resource.ValueSet.ComposeIncludeConcept;
import ca.uhn.fhir.model.dstu2.resource.ValueSet.DefineConcept;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;

public class BaseJpaResourceProviderValueSetDstu2 extends JpaResourceProviderDstu2<ValueSet> {

	@Operation(name = "$expand", idempotent = true)
	public ValueSet everything(HttpServletRequest theServletRequest, @IdParam IdDt theId, @OperationParam(name = "filter") StringDt theFilter) {
		startRequest(theServletRequest);
		try {
			ValueSet retVal = new ValueSet();
			retVal.setDate(DateTimeDt.withCurrentTime());

			ValueSet source = read(theServletRequest, theId);

			Map<String, ComposeInclude> systemToCompose = new HashMap<String, ComposeInclude>();

			/*
			 * Add composed concepts
			 */
			
			for (ComposeInclude nextInclude : source.getCompose().getInclude()) {
				for (ComposeIncludeConcept next : nextInclude.getConcept()) {
					ComposeInclude include = null;
					if (theFilter == null || theFilter.isEmpty()) {
						if (include == null) {
							include = getOrAddComposeInclude(retVal, systemToCompose, nextInclude.getSystem());
						}
						include.addConcept(next);
					} else {
						String filter = theFilter.getValue().toLowerCase();
						if (next.getDisplay().toLowerCase().contains(filter) || next.getCode().toLowerCase().contains(filter)) {
							if (include == null) {
								include = getOrAddComposeInclude(retVal, systemToCompose, nextInclude.getSystem());
							}
							include.addConcept(next);
						}
					}
				}
			}

			/*
			 * Add defined concepts
			 */
			
			ComposeInclude include = null;
			for (DefineConcept next : source.getDefine().getConcept()) {
				if (theFilter == null || theFilter.isEmpty()) {
					if (include == null) {
						include = getOrAddComposeInclude(retVal, systemToCompose, source.getDefine().getSystem());
					}
					include.addConcept(new ComposeIncludeConcept().setCode(next.getCode()).setDisplay(next.getDisplay()));
				} else {
					String filter = theFilter.getValue().toLowerCase();
					if (next.getDisplay().toLowerCase().contains(filter) || next.getCode().toLowerCase().contains(filter)) {
						if (include == null) {
							include = getOrAddComposeInclude(retVal, systemToCompose, source.getDefine().getSystem());
						}
						include.addConcept(new ComposeIncludeConcept().setCode(next.getCode()).setDisplay(next.getDisplay()));
					}
				}
			}

			return retVal;

		} finally {
			endRequest(theServletRequest);
		}
	}

	private ComposeInclude getOrAddComposeInclude(ValueSet retVal, Map<String, ComposeInclude> systemToCompose, String system) {
		ComposeInclude include;
		include = systemToCompose.get(system);
		if (include == null) {
			include = retVal.getCompose().addInclude();
			include.setSystem(system);
			systemToCompose.put(system, include);
		}
		return include;
	}

}
