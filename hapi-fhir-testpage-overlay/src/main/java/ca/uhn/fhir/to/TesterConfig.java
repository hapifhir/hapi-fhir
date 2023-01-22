package ca.uhn.fhir.to;

import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.server.util.ITestingUiClientFactory;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IIdType;
import org.springframework.beans.factory.annotation.Required;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

public class TesterConfig {
	public static final String SYSPROP_FORCE_SERVERS = "ca.uhn.fhir.to.TesterConfig_SYSPROP_FORCE_SERVERS";
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(TesterConfig.class);
	private final LinkedHashMap<String, Boolean> myIdToAllowsApiKey = new LinkedHashMap<>();
	private final LinkedHashMap<String, FhirVersionEnum> myIdToFhirVersion = new LinkedHashMap<>();
	private final LinkedHashMap<String, String> myIdToServerBase = new LinkedHashMap<>();
	private final LinkedHashMap<String, String> myIdToServerName = new LinkedHashMap<>();
	private final List<ServerBuilder> myServerBuilders = new ArrayList<>();
	private final LinkedHashMap<String, Multimap<String, IInclusionChecker>> myIdToTypeToOperationNameToInclusionChecker = new LinkedHashMap<>();
	private ITestingUiClientFactory myClientFactory;
	private boolean myRefuseToFetchThirdPartyUrls = true;

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
			myIdToTypeToOperationNameToInclusionChecker.put(next.myId, next.myOperationNameToInclusionChecker);
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
		return true;
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

	public List<String> getInstanceLevelOperations(String theId, IIdType theResourceId) {
		List<String> retVal = new ArrayList<>();

		Multimap<String, IInclusionChecker> operationNamesToInclusionCheckers = myIdToTypeToOperationNameToInclusionChecker.get(theId);
		for (String operationName : operationNamesToInclusionCheckers.keySet()) {
			for (IInclusionChecker checker : operationNamesToInclusionCheckers.get(operationName)) {
				if (checker.shouldInclude(theResourceId)) {
					retVal.add(operationName);
					break;
				}
			}
		}

		return retVal;
	}

	@Required
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
				throw new IllegalArgumentException(Msg.code(195) + "Invalid serveer line '" + nextRaw + "' - Must be comma separated");
			} else {
				Validate.notBlank(nextSplit[0], "theId can not be blank");
				Validate.notBlank(nextSplit[1], "theVersion can not be blank");
				Validate.notBlank(nextSplit[2], "theDisplayName can not be blank");
				Validate.notBlank(nextSplit[3], "theServerBase can not be blank");
				myIdToServerName.put(nextSplit[0].trim(), nextSplit[2].trim());
				myIdToServerBase.put(nextSplit[0].trim(), nextSplit[3].trim());
				myIdToFhirVersion.put(nextSplit[0].trim(), FhirVersionEnum.valueOf(nextSplit[1].trim().toUpperCase().replace('.', '_')));
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

		ServerBuilder withInstanceLevelOperationOnSearchResults(IInclusionChecker theInclusionChecker, String theOperationName);

	}

	public class ServerBuilder implements IServerBuilderStep1, IServerBuilderStep2, IServerBuilderStep3, IServerBuilderStep4, IServerBuilderStep5 {

		private final Multimap<String, IInclusionChecker> myOperationNameToInclusionChecker = ArrayListMultimap.create();
		private boolean myAllowsApiKey;
		private String myBaseUrl;
		private String myId;
		private String myName;
		private FhirVersionEnum myVersion;

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
		public ServerBuilder withInstanceLevelOperationOnSearchResults(IInclusionChecker theResourceType, String theOperationName) {
			myOperationNameToInclusionChecker.put(theOperationName, theResourceType);
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

	public interface IInclusionChecker {

		boolean shouldInclude(IIdType theResourceId);

	}
}
