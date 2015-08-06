package ca.uhn.fhir.android;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Observable;
import java.util.Set;
import java.util.TreeSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ctc.wstx.stax.WstxInputFactory;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu.valueset.QuantityCompararatorEnum;
import ca.uhn.fhir.model.dstu2.composite.QuantityDt;
import ca.uhn.fhir.model.dstu2.resource.Observation;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.rest.client.IGenericClient;
import ca.uhn.fhir.rest.client.exceptions.FhirClientConnectionException;

public class BuiltJarIT {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(BuiltJarIT.class);

	@BeforeClass
	public static void beforeClass() {
		System.setProperty("javax.xml.stream.XMLInputFactory", "com.ctc.wstx.stax.WstxInputFactory");
		System.setProperty("javax.xml.stream.XMLOutputFactory", "com.ctc.wstx.stax.WstxOutputFactory");
	}
	
	@Test
	public void testParserXml() throws Exception {
//		fail("*******: " + WstxInputFactory.class.getProtectionDomain().getCodeSource().getLocation().toString());
		
		FhirContext ctx = FhirContext.forDstu2();
		
		Patient p = new Patient();
		p.addIdentifier().setSystem("system");
		
		String str = ctx.newXmlParser().encodeResourceToString(p);
		Patient p2 = ctx.newXmlParser().parseResource(Patient.class, str);
		
		assertEquals("system", p2.getIdentifierFirstRep().getSystemElement().getValueAsString());
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
		
		QuantityCompararatorEnum.GREATERTHAN.name();
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
				ourLog.info("Top classes {}", new ArrayList<ClassMethodCount>(topMethods).subList(Math.max(0,topMethods.size() - 10), topMethods.size()));
				
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
