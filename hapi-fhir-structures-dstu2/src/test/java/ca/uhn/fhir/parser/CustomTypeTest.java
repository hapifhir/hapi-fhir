package ca.uhn.fhir.parser;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Extension;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.dstu2.composite.QuantityDt;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.util.ElementUtil;

public class CustomTypeTest {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(CustomTypeTest.class);

	@Test
	public void testEncode() {

		FhirContext ctx = FhirContext.forDstu2();

		MyCustomPatient patient = new MyCustomPatient();

		patient.addIdentifier().setSystem("urn:system").setValue("1234");
		patient.addName().addFamily("Rossi").addGiven("Mario");
		patient.setInsulinLevel(new QuantityDt());
		patient.setGlucoseLevel(new QuantityDt());
		patient.setHbA1c(new QuantityDt());
		patient.setBloodPressure(new QuantityDt());
		patient.setCholesterol(new QuantityDt());
		patient.setWeight(new StringDt("80 kg"));
		patient.setWeight(new StringDt("185 cm"));
		patient.setCheckDates(new ArrayList<DateTimeDt>());
		patient.getCheckDates().add(new DateTimeDt("2014-01-26T11:11:11"));

		IParser p = ctx.newXmlParser().setPrettyPrint(true);
		String messageString = p.encodeResourceToString(patient);

		ourLog.info(messageString);

	}

	@ResourceDef(name = "Patient")
	public static class MyCustomPatient extends Patient {

		private static final long serialVersionUID = 1L;

		@Child(name = "bloodPressure") // once every 3 month. The average target is 130/80 mmHg or less
		@Extension(url = "http://example.com/BloodPressure", definedLocally = false, isModifier = false)
		@Description(shortDefinition = "The value of the patient's blood pressure")
		private QuantityDt myBloodPressure;

		// Dates of periodic tests
		@Child(name = "CheckDates", max = Child.MAX_UNLIMITED)
		@Extension(url = "http://example.com/diabetes2", definedLocally = false, isModifier = true)
		@Description(shortDefinition = "Dates of periodic tests")
		private List<DateTimeDt> myCheckDates;

		@Child(name = "cholesterol") // once a year. The target is triglycerides =< 2 mmol/l e cholesterol =< 4 mmol/l
		@Extension(url = "http://example.com/Cholesterol", definedLocally = false, isModifier = false)
		@Description(shortDefinition = "The value of the patient's cholesterol")
		private QuantityDt myCholesterol;

		@Child(name = "glucoseLevel") // fingerprick test
		@Extension(url = "http://example.com/Glucose", definedLocally = false, isModifier = false)
		@Description(shortDefinition = "The value of the patient's blood glucose")
		private QuantityDt myGlucoseLevel;

		// Periodic Tests
		@Child(name = "hbA1c") // once every 6 month. The average target is 53 mmol/mol (or 7%) or less.
		@Extension(url = "http://example.com/HbA1c", definedLocally = false, isModifier = false)
		@Description(shortDefinition = "The value of the patient's glucose")
		private QuantityDt myHbA1c;

		@Child(name = "Height")
		@Extension(url = "http://example.com/Height", definedLocally = false, isModifier = false)
		@Description(shortDefinition = "The patient's height in cm")
		private StringDt myHeight;

		@Child(name = "insulinLevel") // Normal range is [43,208] pmol/l
		@Extension(url = "http://example.com/Insuline", definedLocally = false, isModifier = false)
		@Description(shortDefinition = "The value of the patient's insulin")
		private QuantityDt myInsulinLevel;

		// Other parameters
		@Child(name = "weight")
		@Extension(url = "http://example.com/Weight", definedLocally = false, isModifier = false)
		@Description(shortDefinition = "The patient's weight in Kg")
		private StringDt myWeight;

		public QuantityDt Cholesterol() {
			if (myCholesterol == null) {
				myCholesterol = new QuantityDt();
			}
			myCholesterol.getValue();
			myCholesterol.getSystem();
			myCholesterol.getCode();

			return myCholesterol;
		}

		public QuantityDt getBloodPressure() {
			if (myBloodPressure == null) {
				myBloodPressure = new QuantityDt();
			}
			myBloodPressure.getValue();
			myBloodPressure.getSystem();
			myBloodPressure.getCode();

			return myBloodPressure;
		}

		public List<DateTimeDt> getCheckDates() {
			if (myCheckDates == null) {
				myCheckDates = new ArrayList<DateTimeDt>();
			}
			return myCheckDates;
		}

		public QuantityDt getGlucoseLevel() {
			if (myGlucoseLevel == null) {
				myGlucoseLevel = new QuantityDt();
			}
			myGlucoseLevel.getValue();
			myGlucoseLevel.getSystem();
			myGlucoseLevel.getCode();

			return myGlucoseLevel;
		}

		public QuantityDt getHbA1c() {
			if (myHbA1c == null) {
				myHbA1c = new QuantityDt();
			}
			myHbA1c.getValue();
			myHbA1c.getSystem();
			myHbA1c.getCode();

			return myHbA1c;
		}

		public StringDt getHeight() {
			if (myHeight == null) {
				myHeight = new StringDt();
			}
			return myHeight;
		}

		public QuantityDt getInsulinLevel() {
			if (myInsulinLevel == null) {
				myInsulinLevel = new QuantityDt();
			}
			myInsulinLevel.getValue();
			myInsulinLevel.getSystem();
			myInsulinLevel.getCode();

			return myInsulinLevel;
		}

		public StringDt getWeight() {
			if (myWeight == null) {
				myWeight = new StringDt();
			}
			return myWeight;
		}

		@Override
		public boolean isEmpty() {
			return super.isEmpty() && ElementUtil.isEmpty(myInsulinLevel, myGlucoseLevel, myHbA1c, myBloodPressure, myCholesterol, myWeight, myHeight, myCheckDates);
		}

		public void setBloodPressure(QuantityDt bloodPressure) {
			myBloodPressure = bloodPressure;
			myBloodPressure.setValue(110);
			myBloodPressure.setSystem("http://unitsofmeasure.org");
			myBloodPressure.setCode("mmHg");
		}

		public void setCheckDates(List<DateTimeDt> theCheckDates) {
			myCheckDates = theCheckDates;
			myCheckDates.add(new DateTimeDt("2010-01-02"));
		}

		public void setCholesterol(QuantityDt cholesterol) {
			myCholesterol = cholesterol;
			myCholesterol.setValue(2);
			myCholesterol.setSystem("http://unitsofmeasure.org");
			myCholesterol.setCode("mmol/l");
		}

		public void setGlucoseLevel(QuantityDt glucoseLevel) {
			myGlucoseLevel = glucoseLevel;
			myGlucoseLevel.setValue(95);
			myGlucoseLevel.setSystem("http://unitsofmeasure.org");
			myGlucoseLevel.setCode("mg/dl");
		}

		public void setHbA1c(QuantityDt hba1c) {
			myHbA1c = hba1c;
			myHbA1c.setValue(48);
			myHbA1c.setSystem("http://unitsofmeasure.org");
			myHbA1c.setCode("mmol/mol");
		}

		public void setHeight(StringDt height) {
			myHeight = height;
		}

		// Setter/Getter methods
		public void setInsulinLevel(QuantityDt insulinLevel) {
			myInsulinLevel = insulinLevel;
			myInsulinLevel.setValue(125);
			myInsulinLevel.setSystem("http://unitsofmeasure.org");
			myInsulinLevel.setCode("pmol/l");
		}

		public void setWeight(StringDt weight) {
			myWeight = weight;
		}

	}
}