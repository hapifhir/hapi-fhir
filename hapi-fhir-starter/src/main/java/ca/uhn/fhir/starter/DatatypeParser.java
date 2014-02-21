package ca.uhn.fhir.starter;

import org.w3c.dom.Element;

import ca.uhn.fhir.starter.model.BaseElement;

public class DatatypeParser extends BaseParser {

	private String myDatatypeName;

	@Override
	protected String getTemplate() {
		return "/dt_composite.vm";
	}

	public void setDatatypeName(String theString) {
		myDatatypeName=theString;
	}

	@Override
	protected String getFilename() {
		return myDatatypeName.toLowerCase() + ".xml";
	}

//	@Override
//	protected void parseBasicElements(Element theRowXml, BaseElement theTarget) {
//			String name = cellValue(theRowXml, 0);
//			theTarget.setName(name);
//
//			int lastDot = name.lastIndexOf('.');
//			if (lastDot == -1) {
//				theTarget.setElementName(name);
//			} else {
//				String elementName = name.substring(lastDot + 1);
//				String elementParentName = name.substring(0, lastDot);
//				theTarget.setElementName(elementName);
//				theTarget.setElementParentName(elementParentName);
//			}
//
//			String cardValue = cellValue(theRowXml, 1);
//			if (cardValue != null && cardValue.contains("..")) {
//				String[] split = cardValue.split("\\.\\.");
//				theTarget.setCardMin(split[0]);
//				theTarget.setCardMax(split[1]);
//			}
//
//
//			String type = cellValue(theRowXml, 4);
//			theTarget.setTypeFromString(type);
//			
//			theTarget.setBinding(cellValue(theRowXml, 5));
//			theTarget.setShortName(cellValue(theRowXml, 6));
//			theTarget.setDefinition(cellValue(theRowXml, 7));
//			theTarget.setRequirement(cellValue(theRowXml, 6));
//			theTarget.setV2Mapping(cellValue(theRowXml, 13));
//		}

	
}
