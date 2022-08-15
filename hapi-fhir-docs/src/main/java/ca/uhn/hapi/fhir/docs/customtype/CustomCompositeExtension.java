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

import ca.uhn.fhir.model.api.annotation.Block;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Extension;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.util.ElementUtil;
import org.hl7.fhir.dstu3.model.BackboneElement;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.StringType;

//START SNIPPET: resource
@ResourceDef(name = "Patient")
public class CustomCompositeExtension extends Patient {

	private static final long serialVersionUID = 1L;

	/**
	 * A custom extension
	 */
	@Child(name = "foo")
	@Extension(url="http://acme.org/fooParent", definedLocally = false, isModifier = false)
	protected FooParentExtension fooParentExtension;

	public FooParentExtension getFooParentExtension() {
		return fooParentExtension;
	}

	@Override
	public boolean isEmpty() {
		return super.isEmpty() && ElementUtil.isEmpty(fooParentExtension);
	}

	public void setFooParentExtension(FooParentExtension theFooParentExtension) {
		fooParentExtension = theFooParentExtension;
	}

	@Block
	public static class FooParentExtension extends BackboneElement {

		private static final long serialVersionUID = 4522090347756045145L;

		@Child(name = "childA")
		@Extension(url = "http://acme.org/fooChildA", definedLocally = false, isModifier = false)
		private StringType myChildA;

		@Child(name = "childB")
		@Extension(url = "http://acme.org/fooChildB", definedLocally = false, isModifier = false)
		private StringType myChildB;

		@Override
		public FooParentExtension copy() {
			FooParentExtension copy = new FooParentExtension();
			copy.myChildA = myChildA;
			copy.myChildB = myChildB;
			return copy;
		}

		@Override
		public boolean isEmpty() {
			return super.isEmpty() && ElementUtil.isEmpty(myChildA, myChildB);
		}

		public StringType getChildA() {
			return myChildA;
		}

		public StringType getChildB() {
			return myChildB;
		}

		public void setChildA(StringType theChildA) {
			myChildA = theChildA;
		}

		public void setChildB(StringType theChildB) {
			myChildB = theChildB;
		}

	}

}
//END SNIPPET: resource
