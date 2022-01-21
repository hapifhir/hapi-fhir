package ca.uhn.hapi.fhir.docs.customtype;

/*-
 * #%L
 * HAPI FHIR - Docs
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

// START SNIPPET: resource

import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.util.ElementUtil;
import org.hl7.fhir.dstu3.model.DomainResource;
import org.hl7.fhir.dstu3.model.ResourceType;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.dstu3.model.Type;

import java.util.ArrayList;
import java.util.List;

/**
 * This is an example of a custom resource that also uses a custom
 * datatype.
 * 
 * Note that we are extending DomainResource for an STU3
 * resource. For DSTU2 it would be BaseResource. 
 */
@ResourceDef(name = "CustomResource", profile = "http://hl7.org/fhir/profiles/custom-resource")
public class CustomResource extends DomainResource {

	private static final long serialVersionUID = 1L;

	/**
	 * We give the resource a field with name "television". This field has no
	 * specific type, so it's a choice[x] field for any type.
	 */
	@Child(name="television", min=1, max=Child.MAX_UNLIMITED, order=0)
	private List<Type> myTelevision;

	/**
	 * We'll give it one more field called "dogs"
	 */
   @Child(name = "dogs", min=0, max=1, order=1)
	private StringType myDogs;
	
	@Override
	public CustomResource copy() {
		CustomResource retVal = new CustomResource();
		super.copyValues(retVal);
		retVal.myTelevision = myTelevision;
      retVal.myDogs = myDogs;
		return retVal;
	}

   public List<Type> getTelevision() {
	   if (myTelevision == null) {
	      myTelevision = new ArrayList<Type>();
	   }
		return myTelevision;
	}

   public StringType getDogs() {
      return myDogs;
   }

	@Override
	public ResourceType getResourceType() {
		return null;
	}

	@Override
	public FhirVersionEnum getStructureFhirVersionEnum() {
		return FhirVersionEnum.DSTU3;
	}

	@Override
	public boolean isEmpty() {
		return ElementUtil.isEmpty(myTelevision, myDogs);
	}

	public void setTelevision(List<Type> theValue) {
		this.myTelevision = theValue;
	}

	public void setDogs(StringType theDogs) {
      myDogs = theDogs;
   }

}
// END SNIPPET: resource
