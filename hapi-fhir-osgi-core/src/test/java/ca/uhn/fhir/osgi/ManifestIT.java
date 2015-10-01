package ca.uhn.fhir.osgi;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

import java.io.FileReader;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

public class ManifestIT {

	/**
	 * See #234 
	 */
	@Test
	public void testValidateManifest() throws Exception {
		
		FileReader r = new FileReader("./target/classes/META-INF/MANIFEST.MF");
		String file = IOUtils.toString(r);
		
		assertThat(file, not(containsString(".jar=/")));
	}
	
}
