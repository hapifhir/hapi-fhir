package ca.uhn.fhir.signature;

import java.io.StringWriter;
import java.nio.charset.Charset;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.Collections;

import javax.xml.crypto.dsig.CanonicalizationMethod;
import javax.xml.crypto.dsig.DigestMethod;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.SignatureMethod;
import javax.xml.crypto.dsig.SignedInfo;
import javax.xml.crypto.dsig.Transform;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.dom.DOMValidateContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.keyinfo.KeyValue;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.output.WriterOutputStream;
import org.apache.jcp.xml.dsig.internal.dom.DOMXMLSignatureFactory;
import org.apache.jcp.xml.dsig.internal.dom.XMLDSigRI;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.dstu.resource.Patient;

import com.phloc.commons.io.streams.StringInputStream;

public class XmlResourceSigner {

	public static void main(String[] args) throws Exception {

		Patient p = new Patient();
		p.addName().addFamily("Family");

		Bundle b = new Bundle();
		b.addEntry().setResource(p);

		FhirContext ctx = new FhirContext();

		StringWriter w = new StringWriter();
		WriterOutputStream wos = new WriterOutputStream(w);
//		{
//			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//			dbf.setNamespaceAware(true);
//			DocumentBuilder builder = dbf.newDocumentBuilder();
//			Document doc = builder.parse(new StringInputStream(ctx.newXmlParser().encodeBundleToString(b), Charset.forName("UTF-8")));
//
//			KeyPairGenerator kpg = KeyPairGenerator.getInstance("DSA");
//			kpg.initialize(512);
//			KeyPair kp = kpg.generateKeyPair();
//			DOMSignContext dsc = new DOMSignContext(kp.getPrivate(), doc.getDocumentElement());
//
////			org.apache.jcp.xml.dsig.internal.dom.DOMXMLSignatureFactory fac = new DOMXMLSignatureFactory();
//			
//			XMLSignatureFactory fac = XMLSignatureFactory.getInstance("DOM", new XMLDSigRI());
//			
//			Reference ref = fac.newReference("", fac.newDigestMethod(DigestMethod.SHA1, null), Collections.singletonList(fac.newTransform(Transform.ENVELOPED, (TransformParameterSpec) null)), null,
//					null);
//			CanonicalizationMethod canonicalizationMethod = fac.newCanonicalizationMethod(CanonicalizationMethod.INCLUSIVE_WITH_COMMENTS, (C14NMethodParameterSpec) null);
//			SignedInfo si = fac.newSignedInfo(canonicalizationMethod,
//					fac.newSignatureMethod(SignatureMethod.DSA_SHA1, null), Collections.singletonList(ref));
//			KeyInfoFactory kif = fac.getKeyInfoFactory();
//			KeyValue kv = kif.newKeyValue(kp.getPublic());
//			KeyInfo ki = kif.newKeyInfo(Collections.singletonList(kv));
//			XMLSignature signature = fac.newXMLSignature(si, ki);
//			signature.sign(dsc);
//
//			TransformerFactory tf = TransformerFactory.newInstance();
//			Transformer trans = tf.newTransformer();
//			trans.transform(new DOMSource(doc), new StreamResult(wos));
//		}
//
//		String string = w.toString();
//		ourLog.info(string);

		String string = "<feed xmlns=\"http://www.w3.org/2005/Atom\"><title/><id/><entry   ><id/><content type=\"text/xml\"><Patient xmlns=\"http://hl7.org/fhir\"><name><family value=\"Family\"/></name></Patient></content></entry><Signature xmlns=\"http://www.w3.org/2000/09/xmldsig#\"><SignedInfo><CanonicalizationMethod Algorithm=\"http://www.w3.org/TR/2001/REC-xml-c14n-20010315#WithComments\"/><SignatureMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#dsa-sha1\"/><Reference URI=\"\"><Transforms><Transform Algorithm=\"http://www.w3.org/2000/09/xmldsig#enveloped-signature\"/></Transforms><DigestMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#sha1\"/><DigestValue>YeUjNkNN2zM8h9efm03m4pqw26k=</DigestValue></Reference></SignedInfo><SignatureValue>EFeep64sOzuDEBKm9FHQw5Z0v6FAkUVA8PjfviUxQruQtcZQP8LL6A==</SignatureValue><KeyInfo><KeyValue><DSAKeyValue><P>/KaCzo4Syrom78z3EQ5SbbB4sF7ey80etKII864WF64B81uRpH5t9jQTxeEu0ImbzRMqzVDZkVG9\n" + 
				"xD7nN1kuFw==</P><Q>li7dzDacuo67Jg7mtqEm2TRuOMU=</Q><G>Z4Rxsnqc9E7pGknFFH2xqaryRPBaQ01khpMdLRQnG541Awtx/XPaF5Bpsy4pNWMOHCBiNU0Nogps\n" + 
				"QW5QvnlMpA==</G><Y>J6EmmArsEbOGlQfQGRjOG+VIkQNqAMuM6AvFSr6cGls87vchFzNDK/DcjbO4TnnyEqhrwrjZqt/D\n" + 
				"UdaMRq1Y1Q==</Y></DSAKeyValue></KeyValue></KeyInfo></Signature></feed>";
		
		{
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setNamespaceAware(true);
			DocumentBuilder builder = dbf.newDocumentBuilder();
			Document doc = builder.parse(new StringInputStream(string, Charset.forName("UTF-8")));
			NodeList nl = doc.getElementsByTagNameNS(XMLSignature.XMLNS, "Signature");
			if (nl.getLength() == 0) {
				throw new Exception("Cannot find Signature element");
			}

			DOMValidateContext valContext = new DOMValidateContext(new KeyValueKeySelector(), nl.item(0));
			
			XMLSignatureFactory factory =  XMLSignatureFactory.getInstance("DOM", new XMLDSigRI()); 
			XMLSignature signature = 
					  factory.unmarshalXMLSignature(valContext); 
			boolean coreValidity = signature.validate(valContext);
			ourLog.info(""+coreValidity);
		}

	}

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(XmlResourceSigner.class);
}
