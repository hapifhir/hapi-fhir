package ca.uhn.fhir.narrative;

import java.io.IOException;

public class CustomThymeleafNarrativeGenerator extends BaseThymeleafNarrativeGenerator {

	/**
	 * Create a new narrative generator
	 * 
	 * @param thePropertyFile
	 *            The name of the property file, in one of the following
	 *            formats:
	 *            <ul>
	 *            <li>file:/path/to/file/file.properties</li>
	 *            <li>classpath:/com/package/file.properties</li>
	 *            </ul>
	 * @throws IOException
	 *             If the file can not be found/read
	 */
	public CustomThymeleafNarrativeGenerator(String thePropertyFile) throws IOException {
		super();
	}

}
