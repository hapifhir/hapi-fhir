package ca.uhn.fhir.tinder;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Parameter;

import ca.uhn.fhir.tinder.parser.CompartmentParser;

public class CompartmentGeneratorMojo extends AbstractMojo {

	@Parameter(required = true)
	private String fhirVersion;

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		CompartmentParser p = new CompartmentParser(fhirVersion);
		try {
			p.parse();
		} catch (MojoExecutionException e) {
			throw e;
		} catch (MojoFailureException e) {
			throw e;
		} catch (Exception e) {
			throw new MojoFailureException("Failure during parse", e);
		}
	}

	public static void main(String[] args) throws MojoExecutionException, MojoFailureException {
		CompartmentGeneratorMojo mojo = new CompartmentGeneratorMojo();
		mojo.fhirVersion = "dstu2";
		mojo.execute();
	}
	
}
