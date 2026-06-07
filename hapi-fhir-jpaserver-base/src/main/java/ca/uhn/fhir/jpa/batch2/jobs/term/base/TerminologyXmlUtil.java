package ca.uhn.fhir.jpa.batch2.jobs.term.base;

import ca.uhn.fhir.batch2.api.AttachmentDetails;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.util.XmlUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

public class TerminologyXmlUtil {

	/**
	 * Non-instantiable
	 */
	private TerminologyXmlUtil() {
		// nothing
	}

	/**
	 * This method is mostly a wrapper around {@link XmlUtil#parseDocument(Reader, boolean, boolean)}
	 * but it catches parse and IO exceptions and wraps them in a {@link JobExecutionFailedException},
	 * which will cause an instant job failure.
	 */
	public static Element parseXmlDocument(AttachmentDetails theAttachment, String theSourceFilename) {
		InputStreamReader reader = new InputStreamReader(theAttachment.getInputStream(), StandardCharsets.UTF_8);
		Document document;
		try {
			document = XmlUtil.parseDocument(reader, false, true);
		} catch (SAXException | IOException e) {
			throw new JobExecutionFailedException(
					Msg.code(2967) + "Failed to parse file " + theSourceFilename + ": " + e.getMessage(), e);
		}

		return document.getDocumentElement();
	}
}
