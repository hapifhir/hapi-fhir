package ca.uhn.fhir.starter;

import java.io.FileNotFoundException;
import java.io.IOException;

public class TinderMojo {

	public static void main(String[] args) throws Exception {
		
		ValueSetParser vsp = new ValueSetParser();
		vsp.setDirectory("src/test/resources/vs/");
		vsp.parse();
		
		DatatypeParser dtp = new DatatypeParser();
		dtp.setDirectory("src/test/resources/dt");
		dtp.parse();
		
		ResourceParser rp = new ResourceParser();
		rp.setDirectory("src/test/resources/res");
		rp.parse();

	}
	
}
