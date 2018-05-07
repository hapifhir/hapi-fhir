package ca.uhn.fhir.parser;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.xml.stream.*;
import javax.xml.stream.events.XMLEvent;

import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.AfterClass;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.util.TestUtil;
import ca.uhn.fhir.util.XmlUtil;

public class RoundTripDstu3Test {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(RoundTripDstu3Test.class);
	private static FhirContext ourCtx = FhirContext.forDstu3();

	@Test
	public void testIt() {
		// Just so this doesn't complain until we enable roundtrip test
	}

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}


//	@Test
	public void testRoundTrip() throws Exception {
		ZipInputStream is = new ZipInputStream(new FileInputStream("src/test/resources/examples.zip"));
		try {
			while (true) {
				ZipEntry nextEntry = is.getNextEntry();
				if (nextEntry == null) {
					break;
				}

				ByteArrayOutputStream oos = new ByteArrayOutputStream();
				byte[] buffer = new byte[2048];
				int len = 0;
				while ((len = is.read(buffer)) > 0) {
					oos.write(buffer, 0, len);
				}

				String exampleText = oos.toString("UTF-8");
				ourLog.info("Next file: {} - Size: {} bytes", nextEntry.getName(), exampleText.length());
				if (!nextEntry.getName().contains("diagnosticreport-examples-lab")) {
					continue;
				}

				IBaseResource parsed = ourCtx.newXmlParser().parseResource(exampleText);
				String encodedXml = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(parsed);

				exampleText = cleanXml(exampleText);
				encodedXml = cleanXml(encodedXml);

				XmlParserDstu3Test.compareXml(exampleText, encodedXml);
//				DetailedDiff d = new DetailedDiff(new Diff(new StringReader(exampleText), new StringReader(encodedXml)));
//				
//				boolean similar = d.similar();
//				if (!similar) {
//					exampleText = exampleText.replace(" xmlns=\"http://hl7.org/fhir\"", "");
//					encodedXml = encodedXml.replace(" xmlns=\"http://hl7.org/fhir\"", "");
//					if (exampleText.length() != encodedXml.length()) {
//						assertTrue(d.toString(), similar);
//					}
//				}

			}

		} finally {
			is.close();
		}
	}

	private String cleanXml(String exampleText) throws Error, Exception {
		XMLEventReader read = XmlUtil.createXmlReader(new StringReader(exampleText));
		StringWriter sw = new StringWriter();
		XMLEventWriter write = XmlUtil.createXmlWriter(sw);
		while (read.hasNext()) {
			XMLEvent nextEvent = read.nextEvent();
			if (nextEvent.getEventType() == XMLStreamConstants.COMMENT) {
				continue;
			}
			write.add(nextEvent);
		}
		write.add(read);
		sw.close();
		return sw.toString().replaceAll("<!--.*-->", "").replace("\n", " ").replace("\r", " ").replaceAll(">\\s+<", "><").replaceAll("<\\?.*\\?>", "").replaceAll("\\s+", " ");
	}

}
