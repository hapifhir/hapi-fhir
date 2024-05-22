package ca.uhn.fhir.to;

import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.server.util.ITestingUiClientFactory;
import jakarta.annotation.PostConstruct;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TesterConfig {
	public static final String SYSPROP_FORCE_SERVERS = "ca.uhn.fhir.to.TesterConfig_SYSPROP_FORCE_SERVERS";
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(TesterConfig.class);
	private final LinkedHashMap<String, Boolean> myIdToAllowsApiKey = new LinkedHashMap<>();
	private final LinkedHashMap<String, FhirVersionEnum> myIdToFhirVersion = new LinkedHashMap<>();
	private final LinkedHashMap<String, String> myIdToServerBase = new LinkedHashMap<>();
	private final LinkedHashMap<String, String> myIdToServerName = new LinkedHashMap<>();
	private final List<ServerBuilder> myServerBuilders = new ArrayList<>();
	private final LinkedHashMap<String, Map<String, IInclusionChecker>>
			myServerIdToTypeToOperationNameToInclusionChecker = new LinkedHashMap<>();
	private final LinkedHashMap<String, Map<RestOperationTypeEnum, IInclusionChecker>>
			myServerIdToTypeToInteractionNameToInclusionChecker = new LinkedHashMap<>();
	private ITestingUiClientFactory myClientFactory;
	private boolean myRefuseToFetchThirdPartyUrls = true;
	private boolean myDebugTemplatesMode;

	public IServerBuilderStep1 addServer() {
		ServerBuilder retVal = new ServerBuilder();
		myServerBuilders.add(retVal);
		return retVal;
	}

	@PostConstruct
	public void build() {
		for (ServerBuilder next : myServerBuilders) {
			Validate.notBlank(next.myId, "Found invalid server configuration - No ID supplied");
			Validate.notNull(next.myVersion, "Found invalid server configuration - No FHIR version supplied");
			Validate.notBlank(next.myBaseUrl, "Found invalid server configuration - No base URL supplied");
			Validate.notBlank(next.myName, "Found invalid server configuration - No name supplied");
			myIdToFhirVersion.put(next.myId, next.myVersion);
			myIdToServerBase.put(next.myId, next.myBaseUrl);
			myIdToServerName.put(next.myId, next.myName);
			myIdToAllowsApiKey.put(next.myId, next.myAllowsApiKey);
			myServerIdToTypeToOperationNameToInclusionChecker.put(next.myId, next.myOperationNameToInclusionChecker);
			myServerIdToTypeToInteractionNameToInclusionChecker.put(
					next.myId, next.mySearchResultRowInteractionEnabled);
			if (next.myEnableDebugTemplates) {
				myDebugTemplatesMode = true;
			}
		}
		myServerBuilders.clear();
	}

	public ITestingUiClientFactory getClientFactory() {
		return myClientFactory;
	}

	public void setClientFactory(ITestingUiClientFactory theClientFactory) {
		myClientFactory = theClientFactory;
	}

	public boolean getDebugTemplatesMode() {
		return myDebugTemplatesMode;
	}

	public LinkedHashMap<String, Boolean> getIdToAllowsApiKey() {
		return myIdToAllowsApiKey;
	}

	public LinkedHashMap<String, FhirVersionEnum> getIdToFhirVersion() {
		return myIdToFhirVersion;
	}

	public LinkedHashMap<String, String> getIdToServerBase() {
		return myIdToServerBase;
	}

	public LinkedHashMap<String, String> getIdToServerName() {
		return myIdToServerName;
	}

	/**
	 * If set to {@literal true} (default is true) the server will refuse to load URLs in
	 * response payloads that refer to third party servers (e.g. paging URLs etc)
	 */
	public boolean isRefuseToFetchThirdPartyUrls() {
		return myRefuseToFetchThirdPartyUrls;
	}

	/**
	 * If set to {@literal true} (default is true) the server will refuse to load URLs in
	 * response payloads that refer to third party servers (e.g. paging URLs etc)
	 */
	public void setRefuseToFetchThirdPartyUrls(boolean theRefuseToFetchThirdPartyUrls) {
		myRefuseToFetchThirdPartyUrls = theRefuseToFetchThirdPartyUrls;
	}

	/**
	 * Called from Thymeleaf
	 */
	@SuppressWarnings("unused")
	public List<String> getSearchResultRowOperations(String theId, IIdType theResourceId) {
		List<String> retVal = new ArrayList<>();

		Map<String, IInclusionChecker> operationNamesToInclusionCheckers =
				myServerIdToTypeToOperationNameToInclusionChecker.get(theId);
		for (String operationName : operationNamesToInclusionCheckers.keySet()) {
			IInclusionChecker checker = operationNamesToInclusionCheckers.get(operationName);
			if (checker.shouldInclude(theResourceId)) {
				retVal.add(operationName);
			}
		}

		return retVal;
	}

	/**
	 * Called from Thymeleaf
	 */
	@SuppressWarnings("unused")
	public boolean isSearchResultRowInteractionEnabled(
			String theServerId, String theInteractionName, IIdType theResourceId) {
		List<String> retVal = new ArrayList<>();

		Map<RestOperationTypeEnum, IInclusionChecker> interactionNamesToInclusionCheckers =
				myServerIdToTypeToInteractionNameToInclusionChecker.get(theServerId);
		RestOperationTypeEnum interaction = RestOperationTypeEnum.forCode(theInteractionName);
		Validate.isTrue(interaction != null, "Unknown interaction: %s", theInteractionName);
		IInclusionChecker inclusionChecker = interactionNamesToInclusionCheckers.getOrDefault(interaction, id -> false);
		return inclusionChecker.shouldInclude(theResourceId);
	}

	public void setServers(List<String> theServers) {
		List<String> servers = theServers;

		// This is mostly for unit tests
		String force = System.getProperty(SYSPROP_FORCE_SERVERS);
		if (StringUtils.isNotBlank(force)) {
			ourLog.warn("Forcing server configuration because of system property: {}", force);
			servers = Collections.singletonList(force);
		}

		for (String nextRaw : servers) {
			String[] nextSplit = nextRaw.split(",");

			if (nextSplit.length < 3) {
				throw new IllegalArgumentException(
						Msg.code(195) + "Invalid server line '" + nextRaw + "' - Must be comma separated");
			} else {
				Validate.notBlank(nextSplit[0], "theId can not be blank");
				Validate.notBlank(nextSplit[1], "theVersion can not be blank");
				Validate.notBlank(nextSplit[2], "theDisplayName can not be blank");
				Validate.notBlank(nextSplit[3], "theServerBase can not be blank");
				myIdToServerName.put(nextSplit[0].trim(), nextSplit[2].trim());
				myIdToServerBase.put(nextSplit[0].trim(), nextSplit[3].trim());
				myIdToFhirVersion.put(
						nextSplit[0].trim(),
						FhirVersionEnum.valueOf(
								nextSplit[1].trim().toUpperCase().replace('.', '_')));
			}
		}
	}

	public interface IServerBuilderStep1 {

		IServerBuilderStep2 withId(String theId);
	}

	public interface IServerBuilderStep2 {

		IServerBuilderStep3 withFhirVersion(FhirVersionEnum theVersion);
	}

	public interface IServerBuilderStep3 {

		IServerBuilderStep4 withBaseUrl(String theBaseUrl);
	}

	public interface IServerBuilderStep4 {

		IServerBuilderStep5 withName(String theName);
	}

	public interface IServerBuilderStep5 {

		IServerBuilderStep1 addServer();

		IServerBuilderStep5 allowsApiKey();

		/**
		 * If this is set, Thymeleaf UI templates will be run in debug mode, meaning
		 * no caching between executions. This is helpful if you want to make live changes
		 * to the template.
		 *
		 * @since 6.4.0
		 */
		IServerBuilderStep5 enableDebugTemplates();

		/**
		 * Use this method to add buttons to invoke operations on the search result table.
		 */
		ServerBuilder withSearchResultRowOperation(String theOperationName, IInclusionChecker theInclusionChecker);

		/**
		 * Use this method to enable/disable the interaction buttons on the search result rows table.
		 * By default {@link RestOperationTypeEnum#READ} and {@link RestOperationTypeEnum#UPDATE} are
		 * already enabled, and they are currently the only interactions supported.
		 */
		ServerBuilder withSearchResultRowInteraction(
				RestOperationTypeEnum theInteraction, IInclusionChecker theEnabled);
	}

	public interface IInclusionChecker {

		boolean shouldInclude(IIdType theResourceId);
	}

	public class ServerBuilder
			implements IServerBuilderStep1,
					IServerBuilderStep2,
					IServerBuilderStep3,
					IServerBuilderStep4,
					IServerBuilderStep5 {

		private final Map<String, IInclusionChecker> myOperationNameToInclusionChecker = new LinkedHashMap<>();
		private final Map<RestOperationTypeEnum, IInclusionChecker> mySearchResultRowInteractionEnabled =
				new LinkedHashMap<>();
		private boolean myAllowsApiKey;
		private String myBaseUrl;
		private String myId;
		private String myName;
		private FhirVersionEnum myVersion;
		private boolean myEnableDebugTemplates;

		public ServerBuilder() {
			mySearchResultRowInteractionEnabled.put(RestOperationTypeEnum.READ, id -> true);
			mySearchResultRowInteractionEnabled.put(RestOperationTypeEnum.UPDATE, id -> true);
		}

		@Override
		public IServerBuilderStep1 addServer() {
			ServerBuilder retVal = new ServerBuilder();
			myServerBuilders.add(retVal);
			return retVal;
		}

		@Override
		public IServerBuilderStep5 allowsApiKey() {
			myAllowsApiKey = true;
			return this;
		}

		@Override
		public IServerBuilderStep5 enableDebugTemplates() {
			myEnableDebugTemplates = true;
			return this;
		}

		@Override
		public ServerBuilder withSearchResultRowOperation(String theOperationName, IInclusionChecker theResourceType) {
			myOperationNameToInclusionChecker.put(theOperationName, theResourceType);
			return this;
		}

		@Override
		public ServerBuilder withSearchResultRowInteraction(
				RestOperationTypeEnum theInteraction, IInclusionChecker theEnabled) {
			mySearchResultRowInteractionEnabled.put(theInteraction, theEnabled);
			return this;
		}

		@Override
		public IServerBuilderStep4 withBaseUrl(String theBaseUrl) {
			Validate.notBlank(theBaseUrl, "theBaseUrl can not be blank");
			myBaseUrl = theBaseUrl;
			return this;
		}

		@Override
		public IServerBuilderStep3 withFhirVersion(FhirVersionEnum theVersion) {
			Validate.notNull(theVersion);
			myVersion = theVersion;
			return this;
		}

		@Override
		public IServerBuilderStep2 withId(String theId) {
			Validate.notBlank(theId, "theId can not be blank");
			myId = theId;
			return this;
		}

		@Override
		public IServerBuilderStep5 withName(String theName) {
			Validate.notBlank(theName, "theName can not be blank");
			myName = theName;
			return this;
		}
	}
}
