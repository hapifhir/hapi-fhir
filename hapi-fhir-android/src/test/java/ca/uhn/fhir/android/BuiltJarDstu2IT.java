package ca.uhn.fhir.android;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import ca.uhn.fhir.i18n.Msg;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.composite.QuantityDt;
import ca.uhn.fhir.model.dstu2.resource.*;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.exceptions.FhirClientConnectionException;

public class BuiltJarDstu2IT {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(BuiltJarDstu2IT.class);

	@BeforeAll
	public static void beforeClass() {
		System.setProperty("javax.xml.stream.XMLInputFactory", "FOO");
		System.setProperty("javax.xml.stream.XMLOutputFactory", "FOO");
	}

	@Test
	public void testParserXml() throws Exception {

		FhirContext ctx = FhirContext.forDstu2();

		Patient p = new Patient();
		p.addIdentifier().setSystem("system");

		try {
			ctx.newXmlParser().encodeResourceToString(p);
			fail();
		} catch (ca.uhn.fhir.context.ConfigurationException e) {
			assertEquals(Msg.code(1754) + "Unable to initialize StAX - XML processing is disabled",e.getMessage());
		}
	}

	@Test
	public void testParserJson() {

		FhirContext ctx = FhirContext.forDstu2();

		Observation o = new Observation();
		o.getCode().setText("TEXT");
		o.setValue(new QuantityDt(123));
		o.addIdentifier().setSystem("system");

		String str = ctx.newJsonParser().encodeResourceToString(o);
		Observation p2 = ctx.newJsonParser().parseResource(Observation.class, str);

		assertEquals("TEXT", p2.getCode().getText());

		QuantityDt dt = (QuantityDt) p2.getValue();
		dt.getComparatorElement().getValueAsEnum();

	}

	/**
	 * A simple client test - We try to connect to a server that doesn't exist, but
	 * if we at least get the right exception it means we made it up to the HTTP/network stack
	 * 
	 * Disabled for now - TODO: add the old version of the apache client (the one that
	 * android uses) and see if this passes
	 */
	@SuppressWarnings("deprecation")
	@Test
	public void testClient() {
		FhirContext ctx = FhirContext.forDstu2();
		try {
			IGenericClient client = ctx.newRestfulGenericClient("http://127.0.0.1:44442/SomeBase");
			client.capabilities().ofType(Conformance.class).execute();
		} catch (FhirClientConnectionException e) {
			// this is good
		}
	}

	/**
	 * Android does not like duplicate entries in the JAR
	 */
	@Test
	public void testJarContents() throws Exception {
		String wildcard = "hapi-fhir-android-*.jar";
		Collection<File> files = FileUtils.listFiles(new File("target"), new WildcardFileFilter(wildcard), null);
		if (files.isEmpty()) {
			throw new Exception("No files matching " + wildcard);
		}

		for (File file : files) {
			if (file.getName().endsWith("sources.jar")) {
				continue;
			}
			if (file.getName().endsWith("javadoc.jar")) {
				continue;
			}
			if (file.getName().contains("original.jar")) {
				continue;
			}

			ourLog.info("Testing file: {}", file);

			ZipFile zip = new ZipFile(file);

			int totalClasses = 0;
			int totalMethods = 0;
			TreeSet<ClassMethodCount> topMethods = new TreeSet<ClassMethodCount>();

			try {
				Set<String> names = new HashSet<String>();
				for (Enumeration<? extends ZipEntry> iter = zip.entries(); iter.hasMoreElements();) {
					ZipEntry next = iter.nextElement();
					String nextName = next.getName();
					if (!names.add(nextName)) {
						throw new Exception("File " + file + " contains duplicate contents: " + nextName);
					}

					if (nextName.contains("$") == false) {
						if (nextName.endsWith(".class")) {
							String className = nextName.replace("/", ".").replace(".class", "");
							try {
								Class<?> clazz = Class.forName(className);
								int methodCount = clazz.getMethods().length;
								topMethods.add(new ClassMethodCount(className, methodCount));
								totalClasses++;
								totalMethods += methodCount;
							} catch (NoClassDefFoundError e) {
								// ignore
							} catch (ClassNotFoundException e) {
								// ignore
							}
						}
					}
				}

				ourLog.info("File {} contains {} entries", file, names.size());
				ourLog.info("Total classes {} - Total methods {}", totalClasses, totalMethods);
				ourLog.info("Top classes {}", new ArrayList<ClassMethodCount>(topMethods).subList(Math.max(0, topMethods.size() - 10), topMethods.size()));

			} finally {
				zip.close();
			}
		}
	}

	private static class ClassMethodCount implements Comparable<ClassMethodCount> {

		private String myClassName;
		private int myMethodCount;

		public ClassMethodCount(String theClassName, int theMethodCount) {
			myClassName = theClassName;
			myMethodCount = theMethodCount;
		}

		@Override
		public String toString() {
			return myClassName + "[" + myMethodCount + "]";
		}

		@Override
		public int compareTo(ClassMethodCount theO) {
			return myMethodCount - theO.myMethodCount;
		}

		public String getClassName() {
			return myClassName;
		}

		public void setClassName(String theClassName) {
			myClassName = theClassName;
		}

		public int getMethodCount() {
			return myMethodCount;
		}

		public void setMethodCount(int theMethodCount) {
			myMethodCount = theMethodCount;
		}

	}

}
