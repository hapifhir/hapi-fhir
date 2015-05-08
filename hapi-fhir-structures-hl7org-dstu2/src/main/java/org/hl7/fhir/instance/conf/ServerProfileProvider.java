package org.hl7.fhir.instance.conf;

/*
 * #%L
 * HAPI FHIR Structures - HL7.org DSTU2
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.hl7.fhir.instance.model.StructureDefinition;
import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;

public class ServerProfileProvider implements IResourceProvider {

	private final FhirContext myContext;
	private final RestfulServer myRestfulServer;

	public ServerProfileProvider(RestfulServer theServer) {
		myContext = theServer.getFhirContext();
		myRestfulServer = theServer;
	}
	
	@Override
	public Class<? extends IBaseResource> getResourceType() {
		return StructureDefinition.class;
	}
	
	@Read()
	public StructureDefinition getProfileById(HttpServletRequest theRequest, @IdParam IdDt theId) {
		RuntimeResourceDefinition retVal = myContext.getResourceDefinitionById(theId.getValue());
		if (retVal==null) {
			return null;
		}
		String serverBase = getServerBase(theRequest);
		return (StructureDefinition) retVal.toProfile(serverBase);
	}

	@Search()
	public List<StructureDefinition> getAllProfiles(HttpServletRequest theRequest) {
		final String serverBase = getServerBase(theRequest);
		List<RuntimeResourceDefinition> defs = new ArrayList<RuntimeResourceDefinition>(myContext.getResourceDefinitions());
		Collections.sort(defs, new Comparator<RuntimeResourceDefinition>() {
			@Override
			public int compare(RuntimeResourceDefinition theO1, RuntimeResourceDefinition theO2) {
				int cmp = theO1.getName().compareTo(theO2.getName());
				if (cmp==0) {
					cmp=theO1.getResourceProfile(serverBase).compareTo(theO2.getResourceProfile(serverBase));
				}
				return cmp;
			}});
		ArrayList<StructureDefinition> retVal = new ArrayList<StructureDefinition>();
		for (RuntimeResourceDefinition next : defs) {
			retVal.add((StructureDefinition) next.toProfile(serverBase));
		}
		return retVal;
	}

	private String getServerBase(HttpServletRequest theHttpRequest) {
		return myRestfulServer.getServerBaseForRequest(theHttpRequest);
	}
}
