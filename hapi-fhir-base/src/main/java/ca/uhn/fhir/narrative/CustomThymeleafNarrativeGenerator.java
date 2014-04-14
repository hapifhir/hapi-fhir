package ca.uhn.fhir.narrative;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.Validate;

public class CustomThymeleafNarrativeGenerator extends BaseThymeleafNarrativeGenerator {

	private String myPropertyFile;

	/**
	 * Create a new narrative generator
	 * 
	 * @param thePropertyFile
	 *            The name of the property file, in one of the following formats:
	 *            <ul>
	 *            <li>file:/path/to/file/file.properties</li>
	 *            <li>classpath:/com/package/file.properties</li>
	 *            </ul>
	 * @throws IOException
	 *             If the file can not be found/read
	 */
	public CustomThymeleafNarrativeGenerator(String thePropertyFile) throws IOException {
		setPropertyFile(thePropertyFile);
	}

	/**
	 * Set the property file to use
	 * 
	 * @param thePropertyFile
	 *            The name of the property file, in one of the following formats:
	 *            <ul>
	 *            <li>file:/path/to/file/file.properties</li>
	 *            <li>classpath:/com/package/file.properties</li>
	 *            </ul>
	 * @throws IOException
	 *             If the file can not be found/read
	 */
	public void setPropertyFile(String thePropertyFile) {
		Validate.notNull(thePropertyFile, "Property file can not be null");
		myPropertyFile = thePropertyFile;
	}

	@Override
	public List<String> getPropertyFile() {
		return Collections.singletonList(myPropertyFile);
	}

}
