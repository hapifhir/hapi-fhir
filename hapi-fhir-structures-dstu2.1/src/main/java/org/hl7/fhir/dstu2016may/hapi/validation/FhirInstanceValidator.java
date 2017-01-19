package org.hl7.fhir.dstu2016may.hapi.validation;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang3.Validate;
import org.hl7.fhir.dstu2016may.model.OperationOutcome.IssueSeverity;
import org.hl7.fhir.dstu2016may.model.StructureDefinition;
import org.hl7.fhir.dstu2016may.validation.IResourceValidator;
import org.hl7.fhir.dstu2016may.validation.IResourceValidator.BestPracticeWarningLevel;
import org.hl7.fhir.dstu2016may.validation.InstanceValidator;
import org.hl7.fhir.dstu2016may.validation.ValidationMessage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.server.EncodingEnum;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.validation.IValidationContext;
import ca.uhn.fhir.validation.IValidatorModule;

public class FhirInstanceValidator extends BaseValidatorBridge implements IValidatorModule {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirInstanceValidator.class);
	private BestPracticeWarningLevel myBestPracticeWarningLevel;
	private DocumentBuilderFactory myDocBuilderFactory;
	private StructureDefinition myStructureDefintion;
	private IValidationSupport myValidationSupport;

	/**
	 * Constructor
	 * 
	 * Uses {@link DefaultProfileValidationSupport} for {@link IValidationSupport validation support}
	 */
	public FhirInstanceValidator() {
		this(new DefaultProfileValidationSupport());
	}

	/**
	 * Constructor which uses the given validation support
	 * 
	 * @param theValidationSupport
	 *           The validation support
	 */
	public FhirInstanceValidator(IValidationSupport theValidationSupport) {
		myDocBuilderFactory = DocumentBuilderFactory.newInstance();
		myDocBuilderFactory.setNamespaceAware(true);
		myValidationSupport = theValidationSupport;
	}

	private String determineResourceName(Document theDocument) {
		Element root = null;

		NodeList list = theDocument.getChildNodes();
		for (int i = 0; i < list.getLength(); i++) {
			if (list.item(i) instanceof Element) {
				root = (Element) list.item(i);
				break;
			}
		}
		root = theDocument.getDocumentElement();
		return root.getLocalName();
	}

	/**
	 * Returns the "best practice" warning level (default is {@link BestPracticeWarningLevel#Hint}).
	 * <p>
	 * The FHIR Instance Validator has a number of checks for best practices in terms of FHIR usage. If this setting is
	 * set to {@link BestPracticeWarningLevel#Error}, any resource data which does not meet these best practices will be
	 * reported at the ERROR level. If this setting is set to {@link BestPracticeWarningLevel#Ignore}, best practice
	 * guielines will be ignored.
	 * </p>
	 * 
	 * @see {@link #setBestPracticeWarningLevel(BestPracticeWarningLevel)}
	 */
	public BestPracticeWarningLevel getBestPracticeWarningLevel() {
		return myBestPracticeWarningLevel;
	}

	/**
	 * Returns the {@link IValidationSupport validation support} in use by this validator. Default is an instance of
	 * {@link DefaultProfileValidationSupport} if the no-arguments constructor for this object was used.
	 */
	public IValidationSupport getValidationSupport() {
		return myValidationSupport;
	}

	/**
	 * Sets the "best practice warning level". When validating, any deviations from best practices will be reported at
	 * this level.
	 * <p>
	 * The FHIR Instance Validator has a number of checks for best practices in terms of FHIR usage. If this setting is
	 * set to {@link BestPracticeWarningLevel#Error}, any resource data which does not meet these best practices will be
	 * reported at the ERROR level. If this setting is set to {@link BestPracticeWarningLevel#Ignore}, best practice
	 * guielines will be ignored.
	 * </p>
	 * 
	 * @param theBestPracticeWarningLevel
	 *           The level, must not be <code>null</code>
	 */
	public void setBestPracticeWarningLevel(BestPracticeWarningLevel theBestPracticeWarningLevel) {
		Validate.notNull(theBestPracticeWarningLevel);
		myBestPracticeWarningLevel = theBestPracticeWarningLevel;
	}

	public void setStructureDefintion(StructureDefinition theStructureDefintion) {
		myStructureDefintion = theStructureDefintion;
	}

	/**
	 * Sets the {@link IValidationSupport validation support} in use by this validator. Default is an instance of
	 * {@link DefaultProfileValidationSupport} if the no-arguments constructor for this object was used.
	 */
	public void setValidationSupport(IValidationSupport theValidationSupport) {
		myValidationSupport = theValidationSupport;
	}

	protected List<ValidationMessage> validate(final FhirContext theCtx, String theInput, EncodingEnum theEncoding) {
		HapiWorkerContext workerContext = new HapiWorkerContext(theCtx, myValidationSupport);

		InstanceValidator v;
		try {
			v = new InstanceValidator(workerContext);
		} catch (Exception e) {
			throw new ConfigurationException(e);
		}

		v.setBestPracticeWarningLevel(myBestPracticeWarningLevel);
		v.setAnyExtensionsAllowed(true);
		v.setResourceIdRule(IResourceValidator.IdStatus.OPTIONAL);

		List<ValidationMessage> messages = new ArrayList<ValidationMessage>();

		if (theEncoding == EncodingEnum.XML) {
			Document document;
			try {
				DocumentBuilder builder = myDocBuilderFactory.newDocumentBuilder();
				InputSource src = new InputSource(new StringReader(theInput));
				document = builder.parse(src);
			} catch (Exception e2) {
				ourLog.error("Failure to parse XML input", e2);
				ValidationMessage m = new ValidationMessage();
				m.setLevel(IssueSeverity.FATAL);
				m.setMessage("Failed to parse input, it does not appear to be valid XML:" + e2.getMessage());
				return Collections.singletonList(m);
			}

			String resourceName = determineResourceName(document);
			StructureDefinition profile = findStructureDefinitionForResourceName(theCtx, resourceName);
			if (profile != null) {
				try {
					v.validate(messages, document, profile);
				} catch (Exception e) {
					throw new InternalErrorException("Unexpected failure while validating resource", e);
				}
			}
		} else if (theEncoding == EncodingEnum.JSON) {
			Gson gson = new GsonBuilder().create();
			JsonObject json = gson.fromJson(theInput, JsonObject.class);

			String resourceName = json.get("resourceType").getAsString();
			StructureDefinition profile = findStructureDefinitionForResourceName(theCtx, resourceName);
			if (profile != null) {
				try {
					v.validate(messages, json, profile);
				} catch (Exception e) {
					throw new InternalErrorException("Unexpected failure while validating resource", e);
				}
			}
		} else {
			throw new IllegalArgumentException("Unknown encoding: " + theEncoding);
		}

		for (int i = 0; i < messages.size(); i++) {
			ValidationMessage next = messages.get(i);
			if ("Binding has no source, so can't be checked".equals(next.getMessage())) {
				messages.remove(i);
				i--;
			}
		}
		return messages;
	}

	private StructureDefinition findStructureDefinitionForResourceName(final FhirContext theCtx, String resourceName) {
		String sdName = "http://hl7.org/fhir/StructureDefinition/" + resourceName;
		StructureDefinition profile = myStructureDefintion != null ? myStructureDefintion : myValidationSupport.fetchStructureDefinition(theCtx, sdName);
		return profile;
	}

	@Override
	protected List<ValidationMessage> validate(IValidationContext<?> theCtx) {
		return validate(theCtx.getFhirContext(), theCtx.getResourceAsString(), theCtx.getResourceAsStringEncoding());
	}


}
