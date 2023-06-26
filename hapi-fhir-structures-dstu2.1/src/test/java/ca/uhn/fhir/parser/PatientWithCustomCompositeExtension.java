package ca.uhn.fhir.parser;

import org.hl7.fhir.dstu2016may.model.BackboneElement;
import org.hl7.fhir.dstu2016may.model.Patient;
import org.hl7.fhir.dstu2016may.model.StringType;

import ca.uhn.fhir.model.api.annotation.Block;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Extension;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.util.ElementUtil;

@ResourceDef(name = "Patient")
public class PatientWithCustomCompositeExtension extends Patient {

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