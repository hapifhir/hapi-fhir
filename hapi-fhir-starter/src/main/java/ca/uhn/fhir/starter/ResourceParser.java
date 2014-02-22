package ca.uhn.fhir.starter;


public class ResourceParser extends BaseParser {
	private String myResourceName;

	private void setResourceName(String theString) {
		myResourceName = theString;
	}

	@Override
	protected String getFilename() {
		return myResourceName + "-spreadsheet.xml";
	}

	@Override
	protected String getTemplate() {
		return "/resource.vm";
	}

//	@Override
//	protected void parseBasicElements(Element theRowXml, BaseElement theTarget) {
//		String name = cellValue(theRowXml, 0);
//		theTarget.setName(name);
//
//		int lastDot = name.lastIndexOf('.');
//		if (lastDot == -1) {
//			theTarget.setElementName(name);
//		} else {
//			String elementName = name.substring(lastDot + 1);
//			String elementParentName = name.substring(0, lastDot);
//			theTarget.setElementName(elementName);
//			theTarget.setElementParentName(elementParentName);
//		}
//
//		String cardValue = cellValue(theRowXml, 1);
//		if (cardValue != null && cardValue.contains("..")) {
//			String[] split = cardValue.split("\\.\\.");
//			theTarget.setCardMin(split[0]);
//			theTarget.setCardMax(split[1]);
//		}
//
//		String type = cellValue(theRowXml, 5);
//		theTarget.setTypeFromString(type);
//
//		theTarget.setBinding(cellValue(theRowXml, 6));
//		theTarget.setShortName(cellValue(theRowXml, 7));
//		theTarget.setDefinition(cellValue(theRowXml, 8));
//		theTarget.setRequirement(cellValue(theRowXml, 9));
//		theTarget.setV2Mapping(cellValue(theRowXml, 14));
//	}

	public static void main(String[] args) throws Exception {
		ResourceParser p = new ResourceParser();
		p.setDirectory("src/test/resources/res");
		p.setResourceName("patient");
		p.setOutputFile("../hapi-fhir-base/src/main/java/ca/uhn/fhir/model/resource/Patient.java");
		p.parse();

		p.setResourceName("observation");
		p.setOutputFile("../hapi-fhir-base/src/main/java/ca/uhn/fhir/model/resource/Observation.java");
		p.parse();

		DatatypeParser d = new DatatypeParser();
		d.setDirectory("src/test/resources/dt");
		d.setDatatypeName("humanname");
		d.setOutputFile("../hapi-fhir-base/src/main/java/ca/uhn/fhir/model/datatype/HumanNameDt.java");
		d.parse();

		d.setDatatypeName("contact");
		d.setOutputFile("../hapi-fhir-base/src/main/java/ca/uhn/fhir/model/datatype/ContactDt.java");
		d.parse();

		d.setDatatypeName("address");
		d.setOutputFile("../hapi-fhir-base/src/main/java/ca/uhn/fhir/model/datatype/AddressDt.java");
		d.parse();

		d.setDatatypeName("narrative");
		d.setOutputFile("../hapi-fhir-base/src/main/java/ca/uhn/fhir/model/datatype/NarrativeDt.java");
		d.parse();

		d.setDatatypeName("quantity");
		d.setOutputFile("../hapi-fhir-base/src/main/java/ca/uhn/fhir/model/datatype/QuantityDt.java");
		d.parse();

	}
}