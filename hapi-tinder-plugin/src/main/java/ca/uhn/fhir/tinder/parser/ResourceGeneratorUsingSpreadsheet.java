package ca.uhn.fhir.tinder.parser;

import ca.uhn.fhir.i18n.Msg;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.maven.plugin.MojoFailureException;

import ca.uhn.fhir.tinder.model.BaseElement;
import ca.uhn.fhir.tinder.model.BaseRootType;
import ca.uhn.fhir.tinder.model.Resource;

public class ResourceGeneratorUsingSpreadsheet extends BaseStructureSpreadsheetParser {
	private List<String> myInputStreamNames;
	private ArrayList<InputStream> myInputStreams;

	public ResourceGeneratorUsingSpreadsheet(String theVersion, String theBaseDir) throws MojoFailureException {
		super(theVersion, theBaseDir);
		super.setFilenameSuffix("");
	}

	@Override
	public List<String> getInputStreamNames() {
		return myInputStreamNames;
	}

	@Override
	protected void postProcess(BaseElement theTarget) throws MojoFailureException {
		super.postProcess(theTarget);
		
		if ("Bundle".equals(theTarget.getName())) {
			addEverythingToSummary(theTarget);
		}
		
		if (getVersion().equals("dstu2") && theTarget instanceof Resource) {
			try {
				new CompartmentParser(getVersion(), (Resource) theTarget).parse();
			} catch (Exception e) {
				throw new MojoFailureException(Msg.code(161) + e.toString(), e);
			}
		}
		
	}

	private void addEverythingToSummary(BaseElement theTarget) {
		theTarget.setSummary("Y");
		for (BaseElement next : theTarget.getChildren()) {
			addEverythingToSummary(next);
		}
	}

	public void setBaseResourceNames(List<String> theBaseResourceNames) throws MojoFailureException {
		myInputStreamNames = theBaseResourceNames;
		myInputStreams = new ArrayList<>();

		for (String next : theBaseResourceNames) {
			String resName = "/res/" + getVersion() + "/" + next.toLowerCase() + "-spreadsheet.xml";
			resName = resName.replace("/dev/", "/dstu2/");
			
			InputStream nextRes = getClass().getResourceAsStream(resName);
			myInputStreams.add(nextRes);
			if (nextRes == null) {
				throw new MojoFailureException(Msg.code(162) + "Unknown base resource name: " + resName);
			}
		}
	}

	@Override
	protected BaseRootType createRootType() {
		return new Resource();
	}

	@Override
	protected Collection<InputStream> getInputStreams() {
		return myInputStreams;
	}

	@Override
	protected String getTemplate() {
		String template = super.getTemplate();
		if (template != null) {
			return template;
		} else if ("dstu".equals(getVersion())) {
			return "/vm/resource_dstu.vm";
		} else {
			return "/vm/resource.vm";
		}
	}

	@Override
	protected boolean isSpreadsheet(String theFileName) {
		return theFileName.endsWith("spreadsheet.xml");
	}

}
