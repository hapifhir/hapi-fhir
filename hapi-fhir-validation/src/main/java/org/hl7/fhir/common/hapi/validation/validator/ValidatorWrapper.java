package org.hl7.fhir.common.hapi.validation.validator;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.util.Logs;
import ca.uhn.fhir.util.XmlUtil;
import ca.uhn.fhir.validation.IValidationContext;
import ca.uhn.fhir.validation.ValidationOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.commons.io.input.ReaderInputStream;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.r5.context.IWorkerContext;
import org.hl7.fhir.r5.elementmodel.Manager;
import org.hl7.fhir.r5.fhirpath.IHostApplicationServices;
import org.hl7.fhir.r5.model.Enumerations.BindingStrength;
import org.hl7.fhir.r5.model.StructureDefinition;
import org.hl7.fhir.r5.utils.validation.IValidationPolicyAdvisor;
import org.hl7.fhir.r5.utils.validation.IValidatorResourceFetcher;
import org.hl7.fhir.r5.utils.validation.ValidatorSession;
import org.hl7.fhir.r5.utils.validation.constants.BestPracticeWarningLevel;
import org.hl7.fhir.r5.utils.validation.constants.IdStatus;
import org.hl7.fhir.r5.utils.xver.XVerExtensionManager;
import org.hl7.fhir.r5.utils.xver.XVerExtensionManagerOld;
import org.hl7.fhir.utilities.i18n.I18nConstants;
import org.hl7.fhir.utilities.validation.ValidationMessage;
import org.hl7.fhir.validation.ValidatorSettings;
import org.hl7.fhir.validation.instance.InstanceValidator;
import org.slf4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

class ValidatorWrapper {

	private static final Logger ourLog = Logs.getTerminologyTroubleshootingLog();
	private BestPracticeWarningLevel myBestPracticeWarningLevel;
	private boolean myAnyExtensionsAllowed;
	private boolean myErrorForUnknownProfiles;
	private boolean myNoTerminologyChecks;
	private boolean myAssumeValidRestReferences;
	private boolean myNoExtensibleWarnings;
	private boolean myNoBindingMsgSuppressed;

	private Collection<? extends String> myExtensionDomains;
	private IValidatorResourceFetcher myValidatorResourceFetcher;
	private IValidationPolicyAdvisor myValidationPolicyAdvisor;
	private IHostApplicationServices myHostApplicationServices;
	private boolean myAllowExamples;

	/**
	 * Constructor
	 */
	public ValidatorWrapper() {
		super();
	}

	public boolean isAssumeValidRestReferences() {
		return myAssumeValidRestReferences;
	}

	public ValidatorWrapper setAssumeValidRestReferences(boolean assumeValidRestReferences) {
		this.myAssumeValidRestReferences = assumeValidRestReferences;
		return this;
	}

	public ValidatorWrapper setAllowExamples(boolean theAllowExamples) {
		myAllowExamples = theAllowExamples;
		return this;
	}

	public ValidatorWrapper setBestPracticeWarningLevel(BestPracticeWarningLevel theBestPracticeWarningLevel) {
		myBestPracticeWarningLevel = theBestPracticeWarningLevel;
		return this;
	}

	public ValidatorWrapper setAnyExtensionsAllowed(boolean theAnyExtensionsAllowed) {
		myAnyExtensionsAllowed = theAnyExtensionsAllowed;
		return this;
	}

	public ValidatorWrapper setErrorForUnknownProfiles(boolean theErrorForUnknownProfiles) {
		myErrorForUnknownProfiles = theErrorForUnknownProfiles;
		return this;
	}

	public ValidatorWrapper setNoTerminologyChecks(boolean theNoTerminologyChecks) {
		myNoTerminologyChecks = theNoTerminologyChecks;
		return this;
	}

	public ValidatorWrapper setNoExtensibleWarnings(boolean theNoExtensibleWarnings) {
		myNoExtensibleWarnings = theNoExtensibleWarnings;
		return this;
	}

	public ValidatorWrapper setNoBindingMsgSuppressed(boolean theNoBindingMsgSuppressed) {
		myNoBindingMsgSuppressed = theNoBindingMsgSuppressed;
		return this;
	}

	public ValidatorWrapper setExtensionDomains(Collection<? extends String> theExtensionDomains) {
		myExtensionDomains = theExtensionDomains;
		return this;
	}

	public ValidatorWrapper setValidationPolicyAdvisor(IValidationPolicyAdvisor validationPolicyAdvisor) {
		this.myValidationPolicyAdvisor = validationPolicyAdvisor;
		return this;
	}

	public ValidatorWrapper setValidatorResourceFetcher(IValidatorResourceFetcher validatorResourceFetcher) {
		this.myValidatorResourceFetcher = validatorResourceFetcher;
		return this;
	}

	public ValidatorWrapper setHostApplicationServices(IHostApplicationServices evaluationContext) {
		this.myHostApplicationServices = evaluationContext;
		return this;
	}

	public List<ValidationMessage> validate(
			IWorkerContext theWorkerContext, IValidationContext<?> theValidationContext) {
		InstanceValidator v = buildInstanceValidator(theWorkerContext);

		v.setAssumeValidRestReferences(isAssumeValidRestReferences());
		v.setBestPracticeWarningLevel(myBestPracticeWarningLevel);
		v.setAnyExtensionsAllowed(myAnyExtensionsAllowed);
		v.setResourceIdRule(IdStatus.OPTIONAL);
		v.setNoTerminologyChecks(myNoTerminologyChecks);
		v.setErrorForUnknownProfiles(myErrorForUnknownProfiles);
		/*
		 * Start strict: unknown CodeSystems become errors, then selectively down-grade preferred/example
		 * bindings (e.g. DocumentReference.content.format) in post-processing.
		 */
		/* setUnknownCodeSystemsCauseErrors interacts with UnknownCodeSystemWarningValidationSupport. Until this interaction is resolved, the value here should remain fixed. */
		v.setUnknownCodeSystemsCauseErrors(true);
		v.getExtensionDomains().addAll(myExtensionDomains);
		v.setFetcher(myValidatorResourceFetcher);
		v.setPolicyAdvisor(myValidationPolicyAdvisor);
		v.setNoExtensibleWarnings(myNoExtensibleWarnings);
		v.setNoBindingMsgSuppressed(myNoBindingMsgSuppressed);
		v.setAllowExamples(myAllowExamples);
		v.setAllowXsiLocation(true);

		List<ValidationMessage> messages = new ArrayList<>();

		List<StructureDefinition> profiles = new ArrayList<>();
		List<ValidationMessage> invalidProfileValidationMessages = new ArrayList<>();
		for (String nextProfileUrl : theValidationContext.getOptions().getProfiles()) {
			fetchAndAddProfile(theWorkerContext, profiles, nextProfileUrl, invalidProfileValidationMessages);
		}

		String input = theValidationContext.getResourceAsString();
		EncodingEnum encoding = theValidationContext.getResourceAsStringEncoding();
		InputStream inputStream = constructNewReaderInputStream(new StringReader(input));

		if (encoding == EncodingEnum.XML) {
			Document document;
			try {
				document = XmlUtil.parseDocument(input);
			} catch (Exception e2) {
				ourLog.error("Failure to parse XML input", e2);
				ValidationMessage m = new ValidationMessage();
				m.setLevel(ValidationMessage.IssueSeverity.FATAL);
				m.setMessage("Failed to parse input, it does not appear to be valid XML:" + e2.getMessage());
				messages.add(m);
				return messages;
			}

			// Determine if meta/profiles are present...
			ArrayList<String> profileUrls = determineIfProfilesSpecified(document);
			for (String nextProfileUrl : profileUrls) {
				fetchAndAddProfile(theWorkerContext, profiles, nextProfileUrl, invalidProfileValidationMessages);
			}

			Manager.FhirFormat format = Manager.FhirFormat.XML;
			v.validate(null, messages, inputStream, format, profiles);

		} else if (encoding == EncodingEnum.JSON) {

			Gson gson = new GsonBuilder().create();
			JsonObject json = gson.fromJson(input, JsonObject.class);

			JsonObject meta = json.getAsJsonObject("meta");
			if (meta != null) {
				JsonElement profileElement = meta.get("profile");
				if (profileElement != null && profileElement.isJsonArray()) {
					JsonArray profilesArray = profileElement.getAsJsonArray();
					for (JsonElement element : profilesArray) {
						String nextProfileUrl = element.getAsString();
						fetchAndAddProfile(
								theWorkerContext, profiles, nextProfileUrl, invalidProfileValidationMessages);
					}
				}
			}

			Manager.FhirFormat format = Manager.FhirFormat.JSON;
			ValidationOptions options = theValidationContext.getOptions();
			v.validate(options.getAppContext(), messages, inputStream, format, profiles);
		} else {
			throw new IllegalArgumentException(Msg.code(649) + "Unknown encoding: " + encoding);
		}

		adjustUnknownCodeSystemIssueLevels(profiles, theWorkerContext, messages);

		if (profiles.isEmpty() && !invalidProfileValidationMessages.isEmpty()) {
			messages.addAll(invalidProfileValidationMessages);
		}

		// TODO: are these still needed?
		messages = messages.stream()
				.filter(m -> m.getMessageId() == null
						|| !(m.getMessageId().equals(I18nConstants.TERMINOLOGY_TX_BINDING_NOSOURCE)
								|| (m.getMessageId().equals(I18nConstants.TERMINOLOGY_TX_VALUESET_NOTFOUND)
										&& m.getMessage().contains("http://hl7.org/fhir/ValueSet/mimetypes"))))
				.collect(Collectors.toList());

		messages.stream()
				.filter(m -> I18nConstants.VALIDATION_VAL_PROFILE_UNKNOWN.equals(m.getMessageId())
						|| I18nConstants.VALIDATION_VAL_PROFILE_UNKNOWN_NOT_POLICY.equals(m.getMessageId()))
				.forEach(m -> m.setLevel(
						myErrorForUnknownProfiles
								? ValidationMessage.IssueSeverity.ERROR
								: ValidationMessage.IssueSeverity.WARNING));

		return messages;
	}

	private InstanceValidator buildInstanceValidator(IWorkerContext theWorkerContext) {

		final IHostApplicationServices hostApplicationServices = Objects.requireNonNullElseGet(
				this.myHostApplicationServices, FhirInstanceValidator.NullEvaluationContext::new);
		XVerExtensionManager xverManager = new XVerExtensionManagerOld(theWorkerContext);
		try {
			return new InstanceValidator(
					theWorkerContext,
					hostApplicationServices,
					xverManager,
					new ValidatorSession(),
					new ValidatorSettings());
		} catch (Exception e) {
			throw new ConfigurationException(Msg.code(648) + e.getMessage(), e);
		}
	}

	private ReaderInputStream constructNewReaderInputStream(Reader theReader) {
		try {
			return ReaderInputStream.builder()
					.setCharset(StandardCharsets.UTF_8)
					.setReader(theReader)
					.get();
		} catch (Exception ex) {
			// we don't expect this ever
			throw new IllegalArgumentException(
					Msg.code(2596) + "Error constructing input reader stream while validating resource.", ex);
		}
	}

	private void fetchAndAddProfile(
			IWorkerContext theWorkerContext,
			List<StructureDefinition> theProfileStructureDefinitions,
			String theUrl,
			List<ValidationMessage> theValidationMessages) {
		try {
			StructureDefinition structureDefinition = theWorkerContext.fetchResource(StructureDefinition.class, theUrl);
			if (structureDefinition != null) {
				theProfileStructureDefinitions.add(structureDefinition);
			} else {
				ValidationMessage m = new ValidationMessage();
				m.setMessageId(I18nConstants.VALIDATION_VAL_PROFILE_UNKNOWN);
				m.setLevel(ValidationMessage.IssueSeverity.ERROR);
				m.setMessage("Invalid profile. Failed to retrieve profile with url=" + theUrl);
				theValidationMessages.add(m);
			}
		} catch (FHIRException e) {
			ourLog.debug("Failed to load profile: {}", theUrl);
		}
	}

	private ArrayList<String> determineIfProfilesSpecified(Document theDocument) {
		ArrayList<String> profileNames = new ArrayList<>();
		NodeList list = theDocument.getChildNodes().item(0).getChildNodes();
		for (int i = 0; i < list.getLength(); i++) {
			if (list.item(i).getNodeName().compareToIgnoreCase("meta") == 0) {
				NodeList metaList = list.item(i).getChildNodes();
				for (int j = 0; j < metaList.getLength(); j++) {
					if (metaList.item(j).getNodeName().compareToIgnoreCase("profile") == 0) {
						profileNames.add(
								metaList.item(j).getAttributes().item(0).getNodeValue());
					}
				}
				break;
			}
		}
		return profileNames;
	}

	/**
	 * Down-grade unknown CodeSystem issues for known preferred/example paths.
	 */
	private void adjustUnknownCodeSystemIssueLevels(
			List<StructureDefinition> theProfiles,
			IWorkerContext theWorkerContext,
			List<ValidationMessage> theMessages) {
		for (ValidationMessage next : theMessages) {
			if (!isUnknownCodeSystemIssue(next)) {
				continue;
			}

			String path = cleanLocationPath(extractLocation(next));
			Optional<BindingStrength> strengthOpt = determineBindingStrength(theProfiles, theWorkerContext, path);
			if (strengthOpt.isPresent()) {
				BindingStrength strength = strengthOpt.get();
				switch (strength) {
					case REQUIRED:
					case EXTENSIBLE:
						if (next.getLevel().ordinal() < ValidationMessage.IssueSeverity.ERROR.ordinal()) {
							next.setLevel(ValidationMessage.IssueSeverity.ERROR);
						}
						break;
					case PREFERRED:
					case EXAMPLE:
						if (next.getLevel() == ValidationMessage.IssueSeverity.ERROR) {
							next.setLevel(ValidationMessage.IssueSeverity.WARNING);
						}
						break;
					default:
						// no-op
				}
			} else {
				// Fallback: only down-grade the known preferred/example slice on DocumentReference.content.format
				boolean likelyPreferredFormat =
						path != null && path.contains("DocumentReference.content") && path.contains("format");
				if (likelyPreferredFormat && next.getLevel() == ValidationMessage.IssueSeverity.ERROR) {
					next.setLevel(ValidationMessage.IssueSeverity.WARNING);
				}
			}
		}
	}

	private boolean isUnknownCodeSystemIssue(ValidationMessage theMessage) {
		String messageId = theMessage.getMessageId();
		String msg = theMessage.getMessage() != null ? theMessage.getMessage() : "";
		return "Terminology_PassThrough_TX_Message".equals(messageId)
				|| msg.contains("CodeSystem is unknown")
				|| msg.contains("Unknown code system")
				|| msg.contains("Code system is unknown")
				|| msg.contains("CodeSystem is unknown and can't be validated")
				|| (msg.contains("Unable to validate code") && msg.contains("CodeSystem"));
	}

	private String cleanLocationPath(String theLocation) {
		if (theLocation == null) {
			return null;
		}
		return theLocation.replaceAll("\\[[0-9]+]", "");
	}

	private String extractLocation(ValidationMessage theMessage) {
		try {
			Method m = ValidationMessage.class.getMethod("getLocationString");
			Object out = m.invoke(theMessage);
			return out != null ? out.toString() : null;
		} catch (Exception e) {
			// ignore
		}
		try {
			Method m2 = ValidationMessage.class.getMethod("getLocation");
			Object out = m2.invoke(theMessage);
			if (out instanceof Collection) {
				return ((Collection<?>) out)
						.stream().findFirst().map(Object::toString).orElse(null);
			}
			if (out instanceof String) {
				return (String) out;
			}
		} catch (Exception e) {
			// ignore
		}
		return null;
	}

	private Optional<BindingStrength> determineBindingStrength(
			List<StructureDefinition> theProfiles, IWorkerContext theWorkerContext, String thePath) {
		if (thePath == null || thePath.isEmpty()) {
			return Optional.empty();
		}

		for (StructureDefinition nextProfile : theProfiles) {
			Optional<BindingStrength> strength = bindingForPath(nextProfile, thePath);
			if (strength.isPresent()) {
				return strength;
			}
		}

		String resourceName = thePath.split("\\.")[0];
		StructureDefinition baseDef = theWorkerContext.fetchTypeDefinition(resourceName);
		if (baseDef != null) {
			Optional<BindingStrength> strength = bindingForPath(baseDef, thePath);
			if (strength.isPresent()) {
				return strength;
			}
		}

		return Optional.empty();
	}

	private Optional<BindingStrength> bindingForPath(StructureDefinition theSd, String thePath) {
		if (theSd == null || !theSd.hasSnapshot()) {
			return Optional.empty();
		}
		return theSd.getSnapshot().getElement().stream()
				.filter(ed -> thePath.equals(ed.getPath()))
				.filter(ed -> ed.hasBinding() && ed.getBinding().hasStrength())
				.map(ed -> ed.getBinding().getStrength())
				.findFirst();
	}
}
