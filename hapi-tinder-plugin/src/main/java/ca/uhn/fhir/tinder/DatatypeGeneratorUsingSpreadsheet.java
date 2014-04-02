package ca.uhn.fhir.tinder;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ca.uhn.fhir.tinder.model.BaseRootType;
import ca.uhn.fhir.tinder.model.Composite;

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

		for (String next : getInputStreamNames()) {
			retVal.add(getClass().getResourceAsStream(next));
		}
		
		return retVal;
	}

	@Override
	protected BaseRootType createRootType() {
		return new Composite();
	}

	@Override
	protected List<String> getInputStreamNames() {
		ArrayList<String> retVal = new ArrayList<String>();

		retVal.add(("/dt/address.xml"));
		retVal.add(("/dt/coding.xml"));
		retVal.add(("/dt/humanname.xml"));
		retVal.add(("/dt/period.xml"));
		retVal.add(("/dt/ratio.xml"));
		retVal.add(("/dt/schedule.xml"));
		retVal.add(("/dt/attachment.xml"));
		retVal.add(("/dt/contact.xml"));
		retVal.add(("/dt/identifier.xml"));
		retVal.add(("/dt/quantity.xml"));
		retVal.add(("/dt/resourcereference.xml"));
		retVal.add(("/dt/codeableconcept.xml"));
//		retVal.add(("/dt/extension.xml"));
//		retVal.add(("/dt/narrative.xml"));
		retVal.add(("/dt/range.xml"));
		retVal.add(("/dt/sampleddata.xml"));
		
		return retVal;
	}


}
