package ca.uhn.fhir.parser;

import java.util.ArrayList;
import java.util.List;

import org.hl7.fhir.dstu3.model.DateTimeType;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.Quantity;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Extension;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.util.ElementUtil;

public class CustomTypeDstu3Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(CustomTypeDstu3Test.class);

	@Test
	public void testEncode() {

		FhirContext ctx = FhirContext.forDstu3();

		MyCustomPatient patient = new MyCustomPatient();

		patient.addIdentifier().setSystem("urn:system").setValue("1234");
		patient.addName().addFamily("Rossi").addGiven("Mario");
		patient.setInsulinLevel(new Quantity());
		patient.setGlucoseLevel(new Quantity());
		patient.setHbA1c(new Quantity());
		patient.setBloodPressure(new Quantity());
		patient.setCholesterol(new Quantity());
		patient.setWeight(new StringDt("80 kg"));
		patient.setWeight(new StringDt("185 cm"));
		patient.setCheckDates(new ArrayList<DateTimeType>());
		patient.getCheckDates().add(new DateTimeType("2014-01-26T11:11:11"));

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
		private Quantity myBloodPressure;

		// Dates of periodic tests
		@Child(name = "CheckDates", max = Child.MAX_UNLIMITED)
		@Extension(url = "http://example.com/diabetes2", definedLocally = false, isModifier = true)
		@Description(shortDefinition = "Dates of periodic tests")
		private List<DateTimeType> myCheckDates;

		@Child(name = "cholesterol") // once a year. The target is triglycerides =< 2 mmol/l e cholesterol =< 4 mmol/l
		@Extension(url = "http://example.com/Cholesterol", definedLocally = false, isModifier = false)
		@Description(shortDefinition = "The value of the patient's cholesterol")
		private Quantity myCholesterol;

		@Child(name = "glucoseLevel") // fingerprick test
		@Extension(url = "http://example.com/Glucose", definedLocally = false, isModifier = false)
		@Description(shortDefinition = "The value of the patient's blood glucose")
		private Quantity myGlucoseLevel;

		// Periodic Tests
		@Child(name = "hbA1c") // once every 6 month. The average target is 53 mmol/mol (or 7%) or less.
		@Extension(url = "http://example.com/HbA1c", definedLocally = false, isModifier = false)
		@Description(shortDefinition = "The value of the patient's glucose")
		private Quantity myHbA1c;

		@Child(name = "Height")
		@Extension(url = "http://example.com/Height", definedLocally = false, isModifier = false)
		@Description(shortDefinition = "The patient's height in cm")
		private StringDt myHeight;

		@Child(name = "insulinLevel") // Normal range is [43,208] pmol/l
		@Extension(url = "http://example.com/Insuline", definedLocally = false, isModifier = false)
		@Description(shortDefinition = "The value of the patient's insulin")
		private Quantity myInsulinLevel;

		// Other parameters
		@Child(name = "weight")
		@Extension(url = "http://example.com/Weight", definedLocally = false, isModifier = false)
		@Description(shortDefinition = "The patient's weight in Kg")
		private StringDt myWeight;

		public Quantity Cholesterol() {
			if (myCholesterol == null) {
				myCholesterol = new Quantity();
			}
			myCholesterol.getValue();
			myCholesterol.getSystem();
			myCholesterol.getCode();

			return myCholesterol;
		}

		public Quantity getBloodPressure() {
			if (myBloodPressure == null) {
				myBloodPressure = new Quantity();
			}
			myBloodPressure.getValue();
			myBloodPressure.getSystem();
			myBloodPressure.getCode();

			return myBloodPressure;
		}

		public List<DateTimeType> getCheckDates() {
			if (myCheckDates == null) {
				myCheckDates = new ArrayList<DateTimeType>();
			}
			return myCheckDates;
		}

		public Quantity getGlucoseLevel() {
			if (myGlucoseLevel == null) {
				myGlucoseLevel = new Quantity();
			}
			myGlucoseLevel.getValue();
			myGlucoseLevel.getSystem();
			myGlucoseLevel.getCode();

			return myGlucoseLevel;
		}

		public Quantity getHbA1c() {
			if (myHbA1c == null) {
				myHbA1c = new Quantity();
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

		public Quantity getInsulinLevel() {
			if (myInsulinLevel == null) {
				myInsulinLevel = new Quantity();
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

		public void setBloodPressure(Quantity bloodPressure) {
			myBloodPressure = bloodPressure;
			myBloodPressure.setValue(110);
			myBloodPressure.setSystem("http://unitsofmeasure.org");
			myBloodPressure.setCode("mmHg");
		}

		public void setCheckDates(List<DateTimeType> theCheckDates) {
			myCheckDates = theCheckDates;
			myCheckDates.add(new DateTimeType("2010-01-02"));
		}

		public void setCholesterol(Quantity cholesterol) {
			myCholesterol = cholesterol;
			myCholesterol.setValue(2);
			myCholesterol.setSystem("http://unitsofmeasure.org");
			myCholesterol.setCode("mmol/l");
		}

		public void setGlucoseLevel(Quantity glucoseLevel) {
			myGlucoseLevel = glucoseLevel;
			myGlucoseLevel.setValue(95);
			myGlucoseLevel.setSystem("http://unitsofmeasure.org");
			myGlucoseLevel.setCode("mg/dl");
		}

		public void setHbA1c(Quantity hba1c) {
			myHbA1c = hba1c;
			myHbA1c.setValue(48);
			myHbA1c.setSystem("http://unitsofmeasure.org");
			myHbA1c.setCode("mmol/mol");
		}

		public void setHeight(StringDt height) {
			myHeight = height;
		}

		// Setter/Getter methods
		public void setInsulinLevel(Quantity insulinLevel) {
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