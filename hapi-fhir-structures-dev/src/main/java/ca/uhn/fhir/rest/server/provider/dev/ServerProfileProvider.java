package ca.uhn.fhir.rest.server.provider.dev;

/*
 * #%L
 * HAPI FHIR Structures - DEV (FHIR Latest)
 * %%
 * Copyright (C) 2014 University Health Network
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dev.resource.Profile;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.server.IResourceProvider;

public class ServerProfileProvider implements IResourceProvider {

	private FhirContext myContext;

	public ServerProfileProvider(FhirContext theCtx) {
		myContext = theCtx;
	}
	
	@Override
	public Class<? extends IResource> getResourceType() {
		return Profile.class;
	}
	
	@Read()
	public Profile getProfileById(@IdParam IdDt theId) {
		RuntimeResourceDefinition retVal = myContext.getResourceDefinitionById(theId.getValue());
		if (retVal==null) {
			return null;
		}
		return (Profile) retVal.toProfile();
	}

	@Search()
	public List<Profile> getAllProfiles() {
		List<RuntimeResourceDefinition> defs = new ArrayList<RuntimeResourceDefinition>(myContext.getResourceDefinitions());
		Collections.sort(defs, new Comparator<RuntimeResourceDefinition>() {
			@Override
			public int compare(RuntimeResourceDefinition theO1, RuntimeResourceDefinition theO2) {
				int cmp = theO1.getName().compareTo(theO2.getName());
				if (cmp==0) {
					cmp=theO1.getResourceProfile().compareTo(theO2.getResourceProfile());
				}
				return cmp;
			}});
		ArrayList<Profile> retVal = new ArrayList<Profile>();
		for (RuntimeResourceDefinition next : defs) {
			retVal.add((Profile) next.toProfile());
		}
		return retVal;
	}

}
