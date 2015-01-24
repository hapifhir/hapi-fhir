package ca.uhn.fhir.parser;

import java.util.Collections;
import java.util.List;

import ca.uhn.fhir.model.api.BaseIdentifiableElement;
import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.model.api.IResourceBlock;
import ca.uhn.fhir.model.api.annotation.Block;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Extension;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.dstu.composite.AttachmentDt;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.primitive.DateDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.util.ElementUtil;

@ResourceDef(name="Patient")
public class MyPatientWithExtensions extends Patient {
	
	@Extension(url = "urn:patientext:att", definedLocally = false, isModifier = false)
	@Child(name = "extAtt", order = 0)
	private AttachmentDt myExtAtt;

	@Extension(url = "urn:patientext:moreext", definedLocally = false, isModifier = false)
	@Child(name = "moreExt", order = 1)
	private MoreExt myMoreExt;

	@Extension(url = "urn:modext", definedLocally = false, isModifier = true)
	@Child(name = "modExt", order = 2)
	private DateDt myModExt;

	public AttachmentDt getExtAtt() {
		return myExtAtt;
	}

	public MoreExt getMoreExt() {
		return myMoreExt;
	}

	public void setMoreExt(MoreExt theMoreExt) {
		myMoreExt = theMoreExt;
	}

	public DateDt getModExt() {
		return myModExt;
	}

	public void setModExt(DateDt theModExt) {
		myModExt = theModExt;
	}

	public void setExtAtt(AttachmentDt theExtAtt) {
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
	public static class MoreExt extends BaseIdentifiableElement implements IResourceBlock {

		@Extension(url = "urn:patientext:moreext:1", definedLocally = false, isModifier = false)
		@Child(name = "str1", order = 0)
		private StringDt myStr1;

		@Extension(url = "urn:patientext:moreext:2", definedLocally = false, isModifier = false)
		@Child(name = "str2", order = 1)
		private StringDt myStr2;

		public StringDt getStr1() {
			return myStr1;
		}

		public void setStr1(StringDt theStr1) {
			myStr1 = theStr1;
		}

		public StringDt getStr2() {
			return myStr2;
		}

		public void setStr2(StringDt theStr2) {
			myStr2 = theStr2;
		}

		@Override
		public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
			return Collections.emptyList();
		}

		@Override
		public boolean isEmpty() {
			return ElementUtil.isEmpty(myStr1, myStr2);
		}

	}

}
