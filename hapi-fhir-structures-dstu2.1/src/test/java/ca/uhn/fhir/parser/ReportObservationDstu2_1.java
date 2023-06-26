package ca.uhn.fhir.parser;

import org.hl7.fhir.dstu2016may.model.BooleanType;
import org.hl7.fhir.dstu2016may.model.Observation;
import org.hl7.fhir.dstu2016may.model.StringType;

import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Extension;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.util.ElementUtil;

@ResourceDef(name = "Observation", id = "reportobservation")
public class ReportObservationDstu2_1 extends Observation {

	/**
	 * Each extension is defined in a field. Any valid HAPI Data Type can be used for the field type. Note that the
	 * [name=""] attribute in the @Child annotation needs to match the name for the bean accessor and mutator methods.
	 */
	@Child(name = "mandatory", order = 0)
	@Extension(url = "#mandatory", definedLocally = true, isModifier = false)
	@Description(shortDefinition = "The report observation is mandatory or not")
	private BooleanType mandatory;

	@Child(name = "readOnly", order = 1)
	@Extension(url = "#readOnly", definedLocally = true, isModifier = false)
	@Description(shortDefinition = "The report observation is read only or not")
	private BooleanType readOnly;

	@Child(name = "defaultCursor", order = 2)
	@Extension(url = "#defaultCursor", definedLocally = true, isModifier = false)
	@Description(shortDefinition = "The report observation is default cursor or not")
	private BooleanType defaultCursor;

	@Child(name = "sectionContentId", order = 3)
	@Extension(url = "#sectionContentId", definedLocally = true, isModifier = false)
	@Description(shortDefinition = "The primary key of the report section content")
	private StringType sectionContentId;

	/**
	 * It is important to override the isEmpty() method, adding a check for any newly added fields.
	 */
	@Override
	public boolean isEmpty() {
		return super.isEmpty() && ElementUtil.isEmpty(mandatory, readOnly, defaultCursor, sectionContentId);
	}

	/********
	 * Accessors and mutators follow
	 *
	 * IMPORTANT: Each extension is required to have an getter/accessor and a stter/mutator. You are highly recommended
	 * to create getters which create instances if they do not already exist, since this is how the rest of the HAPI
	 * FHIR API works.
	 ********/

	/** Getter for mandatory */
	public BooleanType getMandatory() {
		if (mandatory == null) {
			mandatory = new BooleanType(false);
		}
		return mandatory;
	}

	/** Setter for mandatory */
	public ReportObservationDstu2_1 setMandatory(Boolean isMandatory) {
		mandatory = new BooleanType(isMandatory);
		return this;
	}

	/** Getter for readOnly */
	public BooleanType getReadOnly() {
		if (readOnly == null) {
			readOnly = new BooleanType(false);
		}
		return readOnly;
	}

	/** Setter for readOnly */
	public ReportObservationDstu2_1 setReadOnly(Boolean isReadOnly) {
		readOnly = new BooleanType(isReadOnly);
		return this;
	}

	/** Getter for defaultCursor */
	public BooleanType getDefaultCursor() {
		if (defaultCursor == null) {
			defaultCursor = new BooleanType(false);
		}
		return defaultCursor;
	}

	/** Setter for defaultCursor */
	public ReportObservationDstu2_1 setDefaultCursor(Boolean isDefaultCursor) {
		defaultCursor = new BooleanType(isDefaultCursor);
		return this;
	}

	/** Getter for sectionContentId */
	public StringType getSectionContentId() {
		if (sectionContentId == null) {
			sectionContentId = new StringType();
		}
		return sectionContentId;
	}

	/** Setter for sectionContentId */
	public ReportObservationDstu2_1 setSectionContentId(String sectionContentId) {
		this.sectionContentId = new StringType(sectionContentId);
		return this;
	}

}