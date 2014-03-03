package ca.uhn.fhir.tinder;


public class TinderMojo {

	public static void main(String[] args) throws Exception {
		
		ValueSetParser vsp = new ValueSetParser();
		vsp.setDirectory("src/test/resources/vs/");
		vsp.parse();
		
		DatatypeSpreadsheetParser dtp = new DatatypeSpreadsheetParser();
		dtp.setDirectory("src/test/resources/dt");
		dtp.parse();
		dtp.bindValueSets(vsp);
		
		String dtOutputDir = "target/generated/valuesets/ca/uhn/fhir/model/dstu/composite";
		dtp.writeAll(dtOutputDir);
		
		ResourceSpreadsheetParser rp = new ResourceSpreadsheetParser();
		rp.setDirectory("src/test/resources/res");
		rp.parse();
		rp.bindValueSets(vsp);

		String rpOutputDir = "target/generated/valuesets/ca/uhn/fhir/model/dstu/resource";
		rp.writeAll(rpOutputDir);
		
		String vsOutputDir = "target/generated/valuesets/ca/uhn/fhir/model/dstu/valueset";
		vsp.writeMarkedValueSets(vsOutputDir);
	}
	
}
