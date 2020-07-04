package ca.uhn.fhir.parser;

import ca.uhn.fhir.model.api.annotation.Block;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Extension;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.util.ElementUtil;
import org.hl7.fhir.dstu2.model.Attachment;
import org.hl7.fhir.dstu2.model.BackboneElement;
import org.hl7.fhir.dstu2.model.DateType;
import org.hl7.fhir.dstu2.model.Patient;
import org.hl7.fhir.dstu2.model.StringType;
import org.hl7.fhir.instance.model.api.IBaseBackboneElement;


@ResourceDef(name="Patient")
public class MyObservationWithExtensions extends Patient {
	
	@Extension(url = "urn:patientext:att", definedLocally = false, isModifier = false)
	@Child(name = "extAtt", order = 0)
	private Attachment myExtAtt;

	@Extension(url = "urn:patientext:moreext", definedLocally = false, isModifier = false)
	@Child(name = "moreExt", order = 1)
	private MoreExt myMoreExt;

	@Extension(url = "urn:modext", definedLocally = false, isModifier = true)
	@Child(name = "modExt", order = 2)
	private DateType myModExt;

	public Attachment getExtAtt() {
		return myExtAtt;
	}

	public MoreExt getMoreExt() {
		return myMoreExt;
	}

	public void setMoreExt(MoreExt theMoreExt) {
		myMoreExt = theMoreExt;
	}

	public DateType getModExt() {
		return myModExt;
	}

	public void setModExt(DateType theModExt) {
		myModExt = theModExt;
	}

	public void setExtAtt(Attachment theExtAtt) {
		myExtAtt = theExtAtt;
	}

	@Override
	public boolean isEmpty() {
		return super.isEmpty() && ElementUtil.isEmpty(myExtAtt, myModExt, myMoreExt);
	}

	/**
	 * Block class for child element: <b>Observation.referenceRange</b> (Provides guide for interpretation)
	 * 
	 * <p>
	 * <b>Definition:</b> Guidance on how to interpret the value by comparison to a normal or recommended range
	 * </p>
	 */
	@Block(name = "Observation.someExtensions")
	public static class MoreExt extends BackboneElement implements IBaseBackboneElement {

		@Extension(url = "urn:patientext:moreext:1", definedLocally = false, isModifier = false)
		@Child(name = "str1", order = 0)
		private StringType myStr1;

		@Extension(url = "urn:patientext:moreext:2", definedLocally = false, isModifier = false)
		@Child(name = "str2", order = 1)
		private StringType myStr2;

		public StringType getStr1() {
			return myStr1;
		}

		public void setStr1(StringType theStr1) {
			myStr1 = theStr1;
		}

		public StringType getStr2() {
			return myStr2;
		}

		public void setStr2(StringType theStr2) {
			myStr2 = theStr2;
		}

		@Override
		public boolean isEmpty() {
			return ElementUtil.isEmpty(myStr1, myStr2);
		}

		@Override
		public BackboneElement copy() {
			throw new UnsupportedOperationException();
		}

	}

}
