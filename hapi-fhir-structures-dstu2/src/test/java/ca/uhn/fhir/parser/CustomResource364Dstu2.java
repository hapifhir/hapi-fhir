package ca.uhn.fhir.parser;

import java.util.List;

import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.junit.jupiter.api.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.model.api.BaseIdentifiableElement;
import ca.uhn.fhir.model.api.ICompositeDatatype;
import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.model.api.IResourceBlock;
import ca.uhn.fhir.model.api.annotation.Block;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.dstu2.resource.BaseResource;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.util.ElementUtil;

/**
 * See #364
 */
@ResourceDef(name = "CustomResource", profile = "http://hl7.org/fhir/profiles/custom-resource", id = "custom-resource")
public class CustomResource364Dstu2 extends BaseResource implements IBaseOperationOutcome {

	private static final long serialVersionUID = 1L;

	@Child(name = "baseValue", min = 1, max = Child.MAX_UNLIMITED, type= {})
	private IElement baseValues;

	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ElementUtil.allPopulatedChildElements(theType, baseValues);
	}

	public IElement getBaseValues() {
		return baseValues;
	}

	@Override
	public String getResourceName() {
		return "CustomResource";
	}

	@Override
	public FhirVersionEnum getStructureFhirVersionEnum() {
		return FhirVersionEnum.DSTU2;
	}

	@Override
	public boolean isEmpty() {
		return ElementUtil.isEmpty(baseValues);
	}

	public void setBaseValues(IElement theValue) {
		this.baseValues = theValue;
	}

	@DatatypeDef(name="CustomDate")
	public static class CustomResource364CustomDate extends BaseIdentifiableElement implements ICompositeDatatype {

		private static final long serialVersionUID = 1L;
		
		@Child(name = "date", order = 0, min = 1, max = 1, type = { DateTimeDt.class })
		private DateTimeDt date;

		@Override
		public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
			return ElementUtil.allPopulatedChildElements(theType, date);
		}

		public DateTimeDt getDate() {
			if (date == null)
				date = new DateTimeDt();
			return date;
		}

		@Override
		public boolean isEmpty() {
			return ElementUtil.isEmpty(date);
		}

		public CustomResource364CustomDate setDate(DateTimeDt theValue) {
			date = theValue;
			return this;
		}
	}

}
