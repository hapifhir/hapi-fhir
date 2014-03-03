package ca.uhn.fhir.tinder;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;

public class DatatypeGeneratorUsingSpreadsheet extends BaseStructureSpreadsheetParser {

	@Override
	protected String getTemplate() {
		return "/vm/dt_composite.vm";
	}

	@Override
	protected String getFilenameSuffix() {
		return "Dt";
	}

	@Override
	protected Collection<InputStream> getInputStreams() {
		ArrayList<InputStream> retVal = new ArrayList<InputStream>();

		retVal.add(getClass().getResourceAsStream("/dt/address.xml"));
		retVal.add(getClass().getResourceAsStream("/dt/coding.xml"));
		retVal.add(getClass().getResourceAsStream("/dt/humanname.xml"));
		retVal.add(getClass().getResourceAsStream("/dt/period.xml"));
		retVal.add(getClass().getResourceAsStream("/dt/ratio.xml"));
		retVal.add(getClass().getResourceAsStream("/dt/schedule.xml"));
		retVal.add(getClass().getResourceAsStream("/dt/attachment.xml"));
		retVal.add(getClass().getResourceAsStream("/dt/contact.xml"));
		retVal.add(getClass().getResourceAsStream("/dt/identifier.xml"));
		retVal.add(getClass().getResourceAsStream("/dt/quantity.xml"));
		retVal.add(getClass().getResourceAsStream("/dt/resourcereference.xml"));
		retVal.add(getClass().getResourceAsStream("/dt/codeableconcept.xml"));
//		retVal.add(getClass().getResourceAsStream("/dt/extension.xml"));
//		retVal.add(getClass().getResourceAsStream("/dt/narrative.xml"));
		retVal.add(getClass().getResourceAsStream("/dt/range.xml"));
		retVal.add(getClass().getResourceAsStream("/dt/sampleddata.xml"));

		return retVal;
	}


}
