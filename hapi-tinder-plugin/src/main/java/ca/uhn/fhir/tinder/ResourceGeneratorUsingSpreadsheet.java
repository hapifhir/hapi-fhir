package ca.uhn.fhir.tinder;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.maven.plugin.MojoFailureException;

import ca.uhn.fhir.tinder.model.BaseRootType;
import ca.uhn.fhir.tinder.model.Resource;



public class ResourceGeneratorUsingSpreadsheet extends BaseStructureSpreadsheetParser {


	private ArrayList<InputStream> myInputStreams;
	private List<String> myInputStreamNames;

	@Override
	protected String getTemplate() {
		return "/vm/resource.vm";
	}

	@Override
	protected boolean isSpreadsheet(String theFileName) {
		return theFileName.endsWith("spreadsheet.xml");
	}

	@Override
	protected String getFilenameSuffix() {
		return "";
	}


	public static void main(String[] args) throws Exception  {
		ResourceGeneratorUsingSpreadsheet p = new ResourceGeneratorUsingSpreadsheet();
		p.setBaseResourceNames(Collections.singletonList("observation"));
		p.parse();
		p.markResourcesForImports();
		p.writeAll(new File("target/gen/ca/uhn/fhir/model/dstu/resource"), "ca.uhn.fhir.model.dstu");
//		
//		// TODO: this needs to be properly populated
//		p.getAllDatatypes().add("String");
//		p.getAllDatatypes().add("Date");
//		p.getAllDatatypes().add("DateTime");
//		
////		p.setDirectory("src/test/resources/res");
////		p.setResourceName("patient");
////		p.setOutputFile(basePath + "/ca/uhn/fhir/model/dstu/resource/ResourceWithExtensionsA.java");
////		ArrayList<Extension> exts = new ArrayList<Extension>();
////		Extension ext1 = new Extension("foo1", "http://foo/1", "string");
////		exts.add(ext1);
////		Extension ext2 = new Extension("bar1", "http://bar/1", new Extension("bar11", "http://bar/1/1", "date"), new Extension("bar12", "http://bar/1/2", "date"));
////		exts.add(ext2);
////		p.setExtensions(exts);
////		p.parse();
//
////		String basePath="../hapi-fhir-base/src/main/java";
//		String basePath="target/generated/valuesets";
//		
//		p.setResourceName("medication");
//		p.setOutputFile(basePath + "/ca/uhn/fhir/model/dstu/resource/Medication.java");
//		p.parse();
//
//		p.setDirectory("src/test/resources/res");
//		p.setResourceName("substance");
//		p.setOutputFile(basePath + "/ca/uhn/fhir/model/dstu/resource/Substance.java");
//		p.parse();
//
//		
//		p.setDirectory("src/test/resources/res");
//		p.setResourceName("valueset");
//		p.setOutputFile(basePath + "/ca/uhn/fhir/model/dstu/resource/ValueSetTm.java");
//		p.parse();
//
//		p.setDirectory("src/test/resources/res");
//		p.setResourceName("observation");
//		p.setOutputFile(basePath + "/ca/uhn/fhir/model/dstu/resource/Observation.java");
//		p.parse();
//
//		p.setResourceName("profile");
//		p.setOutputFile(basePath + "/ca/uhn/fhir/model/dstu/resource/Profile.java");
//		p.parse();
//
//		p.setResourceName("device");
//		p.setOutputFile(basePath + "/ca/uhn/fhir/model/dstu/resource/Device.java");
//		p.parse();
//
//		p.setResourceName("group");
//		p.setOutputFile(basePath + "/ca/uhn/fhir/model/dstu/resource/Group.java");
//		p.parse();
//		
//		p.setResourceName("location");
//		p.setOutputFile(basePath + "/ca/uhn/fhir/model/dstu/resource/Location.java");
//		p.parse();
//
//		p.setResourceName("organization");
//		p.setOutputFile(basePath + "/ca/uhn/fhir/model/dstu/resource/Organization.java");
//		p.parse();
//
//		p.setResourceName("patient");
//		p.setOutputFile(basePath + "/ca/uhn/fhir/model/dstu/resource/Patient.java");
//		p.parse();
//
//		p.setResourceName("specimen");
//		p.setOutputFile(basePath + "/ca/uhn/fhir/model/dstu/resource/Specimen.java");
//		p.parse();
//
//		p.setResourceName("practitioner");
//		p.setOutputFile(basePath + "/ca/uhn/fhir/model/dstu/resource/Practitioner.java");
//		p.parse();
//
//		DatatypeSpreadsheetParser d = new DatatypeSpreadsheetParser();
//		d.setDirectory("src/test/resources/dt");
//		d.setDatatypeName("humanname");
//		d.setOutputFile(basePath + "/ca/uhn/fhir/model/dstu/composite/HumanNameDt.java");
//		d.parse();
//
//		d.setDatatypeName("contact");
//		d.setOutputFile(basePath + "/ca/uhn/fhir/model/dstu/composite/ContactDt.java");
//		d.parse();
//
//		d.setDatatypeName("address");
//		d.setOutputFile(basePath + "/ca/uhn/fhir/model/dstu/composite/AddressDt.java");
//		d.parse();
//
//		d.setDatatypeName("narrative");
//		d.setOutputFile(basePath + "/ca/uhn/fhir/model/dstu/composite/NarrativeDt.java");
//		d.parse();
//
//		d.setDatatypeName("quantity");
//		d.setOutputFile(basePath + "/ca/uhn/fhir/model/dstu/composite/QuantityDt.java");
//		d.parse();

	}

	@Override
	protected Collection<InputStream> getInputStreams() {
		return myInputStreams;
	}

	public void setBaseResourceNames(List<String> theBaseResourceNames) throws MojoFailureException {
		myInputStreamNames = theBaseResourceNames;
		myInputStreams = new ArrayList<InputStream>();
		
		for (String next : theBaseResourceNames) {
			InputStream nextRes = getClass().getResourceAsStream("/res/" + next + "-spreadsheet.xml");
			myInputStreams.add(nextRes);
			if (nextRes == null) {
				throw new MojoFailureException("Unknown base resource name: " + next);
			}
		}
	}

	public List<String> getInputStreamNames() {
		return myInputStreamNames;
	}

	@Override
	protected BaseRootType createRootType() {
		return new Resource();
	}

}