package ca.uhn.fhir.android;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.rest.client.IGenericClient;
import ca.uhn.fhir.rest.client.exceptions.FhirClientConnectionException;

public class BuiltJarIT {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(BuiltJarIT.class);

	@Test
	public void testParser() {
		FhirContext ctx = FhirContext.forDstu2();
		
		Patient p = new Patient();
		p.addIdentifier().setSystem("system");
		
		String str = ctx.newXmlParser().encodeResourceToString(p);
		Patient p2 = ctx.newXmlParser().parseResource(Patient.class, str);
		
		assertEquals("system", p2.getIdentifierFirstRep().getSystemElement().getValueAsString());
	}
	
	/**
	 * A simple client test - We try to connect to a server that doesn't exist, but
	 * if we at least get the right exception it means we made it up to the HTTP/network stack
	 * 
	 * Disabled for now - TODO: add the old version of the apache client (the one that
	 * android uses) and see if this passes
	 */
	public void testClient() {
		FhirContext ctx = FhirContext.forDstu2();
		try {
			IGenericClient client = ctx.newRestfulGenericClient("http://127.0.0.1:44442/SomeBase");
			client.conformance();
		} catch (FhirClientConnectionException e) {
			// this is good
		}
	}
	
	/**
	 * Android does not like duplicate entries in the JAR
	 */
	@Test
	public void testJarForDuplicates() throws Exception {
		Collection<File> files = FileUtils.listFiles(new File("target"), new WildcardFileFilter("*-shaded.jar"), null);
		if (files.isEmpty()) {
			throw new Exception("No files matching target/*-shaded.jar");
		}

		for (File file : files) {
			ourLog.info("Testing file: {}", file);

			ZipFile zip = new ZipFile(file);
			try {
				Set<String> names = new HashSet<String>();
				for (Enumeration<? extends ZipEntry> iter = zip.entries(); iter.hasMoreElements();) {
					ZipEntry next = iter.nextElement();
					String nextName = next.getName();
					if (!names.add(nextName)) {
						throw new Exception("File " + file + " contains duplicate contents: " + nextName);
					}
				}
				
				ourLog.info("File {} contains {} entries", file, names.size());
				
			} finally {
				zip.close();
			}
		}
	}

}
