package ca.uhn.fhir.parser;

import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Extension;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.dstu2.resource.Observation;
import ca.uhn.fhir.model.primitive.BooleanDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.util.ElementUtil;

@ResourceDef(name = "Observation", id = "http://example.com/reportobservation")
public class ReportObservation extends Observation {

	private static final long serialVersionUID = 1L;

	/**
	 * Each extension is defined in a field. Any valid HAPI Data Type can be used for the field type. Note that the
	 * [name=""] attribute in the @Child annotation needs to match the name for the bean accessor and mutator methods.
	 */
	@Child(name = "mandatory", order = 0)
	@Extension(url = "#mandatory", definedLocally = true, isModifier = false)
	@Description(shortDefinition = "The report observation is mandatory or not")
	private BooleanDt mandatory;

	@Child(name = "readOnly", order = 1)
	@Extension(url = "#readOnly", definedLocally = true, isModifier = false)
	@Description(shortDefinition = "The report observation is read only or not")
	private BooleanDt readOnly;

	@Child(name = "defaultCursor", order = 2)
	@Extension(url = "#defaultCursor", definedLocally = true, isModifier = false)
	@Description(shortDefinition = "The report observation is default cursor or not")
	private BooleanDt defaultCursor;

	@Child(name = "sectionContentId", order = 3)
	@Extension(url = "#sectionContentId", definedLocally = true, isModifier = false)
	@Description(shortDefinition = "The primary key of the report section content")
	private StringDt sectionContentId;

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
	public BooleanDt getMandatory() {
		if (mandatory == null) {
			mandatory = new BooleanDt(false);
		}
		return mandatory;
	}

	/** Setter for mandatory */
	public ReportObservation setMandatory(Boolean isMandatory) {
		mandatory = new BooleanDt(isMandatory);
		return this;
	}

	/** Getter for readOnly */
	public BooleanDt getReadOnly() {
		if (readOnly == null) {
			readOnly = new BooleanDt(false);
		}
		return readOnly;
	}

	/** Setter for readOnly */
	public ReportObservation setReadOnly(Boolean isReadOnly) {
		readOnly = new BooleanDt(isReadOnly);
		return this;
	}

	/** Getter for defaultCursor */
	public BooleanDt getDefaultCursor() {
		if (defaultCursor == null) {
			defaultCursor = new BooleanDt(false);
		}
		return defaultCursor;
	}

	/** Setter for defaultCursor */
	public ReportObservation setDefaultCursor(Boolean isDefaultCursor) {
		defaultCursor = new BooleanDt(isDefaultCursor);
		return this;
	}

	/** Getter for sectionContentId */
	public StringDt getSectionContentId() {
		if (sectionContentId == null) {
			sectionContentId = new StringDt();
		}
		return sectionContentId;
	}

	/** Setter for sectionContentId */
	public ReportObservation setSectionContentId(String sectionContentId) {
		this.sectionContentId = new StringDt(sectionContentId);
		return this;
	}

}