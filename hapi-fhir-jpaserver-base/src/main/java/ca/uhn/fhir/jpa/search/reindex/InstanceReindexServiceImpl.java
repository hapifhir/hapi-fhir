/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package ca.uhn.fhir.jpa.search.reindex;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.dao.ReindexOutcome;
import ca.uhn.fhir.jpa.api.dao.ReindexParameters;
import ca.uhn.fhir.jpa.dao.BaseHapiFhirResourceDao;
import ca.uhn.fhir.jpa.dao.IJpaStorageResourceParser;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.model.entity.BaseResourceIndexedSearchParam;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedComboStringUnique;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedComboTokenNonUnique;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamCoords;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamDate;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamNumber;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamQuantity;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamQuantityNormalized;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamString;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamToken;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamUri;
import ca.uhn.fhir.jpa.model.entity.ResourceLink;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.model.entity.SearchParamPresentEntity;
import ca.uhn.fhir.jpa.partition.BaseRequestPartitionHelperSvc;
import ca.uhn.fhir.jpa.searchparam.extractor.ISearchParamExtractor;
import ca.uhn.fhir.jpa.searchparam.extractor.ResourceIndexedSearchParams;
import ca.uhn.fhir.jpa.searchparam.extractor.SearchParamExtractorService;
import ca.uhn.fhir.narrative.CustomThymeleafNarrativeGenerator;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import ca.uhn.fhir.rest.server.util.ResourceSearchParams;
import ca.uhn.fhir.util.StopWatch;
import ca.uhn.hapi.converters.canonical.VersionCanonicalizer;
import com.google.common.annotations.VisibleForTesting;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.DecimalType;
import org.hl7.fhir.r4.model.InstantType;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.UriType;
import org.hl7.fhir.r4.model.UrlType;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static ca.uhn.fhir.jpa.dao.index.DaoSearchParamSynchronizer.subtract;
import static java.util.Comparator.comparing;
import static org.apache.commons.collections4.CollectionUtils.intersection;
import static org.apache.commons.lang3.StringUtils.defaultIfBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class InstanceReindexServiceImpl implements IInstanceReindexService {

	private final FhirContext myContextR4 = FhirContext.forR4Cached();

	@Autowired
	protected IJpaStorageResourceParser myJpaStorageResourceParser;

	@Autowired
	private SearchParamExtractorService mySearchParamExtractorService;

	@Autowired
	private BaseRequestPartitionHelperSvc myPartitionHelperSvc;

	@Autowired
	private IHapiTransactionService myTransactionService;

	@Autowired
	private IInterceptorService myInterceptorService;

	@Autowired
	private DaoRegistry myDaoRegistry;

	@Autowired
	private VersionCanonicalizer myVersionCanonicalizer;

	@Autowired
	private PartitionSettings myPartitionSettings;

	private final CustomThymeleafNarrativeGenerator myNarrativeGenerator;

	@Autowired
	private ISearchParamRegistry mySearchParamRegistry;

	/**
	 * Constructor
	 */
	public InstanceReindexServiceImpl() {
		myNarrativeGenerator = new CustomThymeleafNarrativeGenerator(
				"classpath:ca/uhn/fhir/jpa/search/reindex/reindex-outcome-narrative.properties");
	}

	@Override
	public IBaseParameters reindexDryRun(
			RequestDetails theRequestDetails, IIdType theResourceId, @Nullable Set<String> theParameters) {
		RequestPartitionId partitionId = determinePartition(theRequestDetails, theResourceId);
		TransactionDetails transactionDetails = new TransactionDetails();

		Parameters retValCanonical = myTransactionService
				.withRequest(theRequestDetails)
				.withTransactionDetails(transactionDetails)
				.withRequestPartitionId(partitionId)
				.execute(() -> reindexDryRunInTransaction(
						theRequestDetails, theResourceId, partitionId, transactionDetails, theParameters));

		return myVersionCanonicalizer.parametersFromCanonical(retValCanonical);
	}

	@Override
	public IBaseParameters reindex(RequestDetails theRequestDetails, IIdType theResourceId) {
		RequestPartitionId partitionId = determinePartition(theRequestDetails, theResourceId);
		TransactionDetails transactionDetails = new TransactionDetails();

		Parameters retValCanonical = myTransactionService
				.withRequest(theRequestDetails)
				.withTransactionDetails(transactionDetails)
				.withRequestPartitionId(partitionId)
				.execute(() -> reindexInTransaction(theRequestDetails, theResourceId));

		return myVersionCanonicalizer.parametersFromCanonical(retValCanonical);
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	@Nonnull
	private Parameters reindexInTransaction(RequestDetails theRequestDetails, IIdType theResourceId) {
		StopWatch sw = new StopWatch();
		IFhirResourceDao dao = myDaoRegistry.getResourceDao(theResourceId.getResourceType());
		ResourceTable entity = (ResourceTable) dao.readEntity(theResourceId, theRequestDetails);
		IBaseResource resource = myJpaStorageResourceParser.toResource(entity, false);

		// Invoke the pre-access and pre-show interceptors in case there are any security
		// restrictions or audit requirements around the user accessing this resource
		BaseHapiFhirResourceDao.invokeStoragePreAccessResources(
				myInterceptorService, theRequestDetails, theResourceId, resource);
		BaseHapiFhirResourceDao.invokeStoragePreShowResources(myInterceptorService, theRequestDetails, resource);

		ResourceIndexedSearchParams existingParamsToPopulate = ResourceIndexedSearchParams.withLists(entity);
		existingParamsToPopulate.mySearchParamPresentEntities.addAll(entity.getSearchParamPresents());

		List<String> messages = new ArrayList<>();

		JpaPid pid = JpaPid.fromId(entity.getId());
		ReindexOutcome outcome = dao.reindex(pid, new ReindexParameters(), theRequestDetails, new TransactionDetails());
		messages.add("Reindex completed in " + sw);

		for (String next : outcome.getWarnings()) {
			messages.add("WARNING: " + next);
		}

		ResourceIndexedSearchParams newParamsToPopulate = ResourceIndexedSearchParams.withLists(entity);
		newParamsToPopulate.mySearchParamPresentEntities.addAll(entity.getSearchParamPresents());

		return buildIndexResponse(existingParamsToPopulate, newParamsToPopulate, true, messages);
	}

	@Nonnull
	private Parameters reindexDryRunInTransaction(
			RequestDetails theRequestDetails,
			IIdType theResourceId,
			RequestPartitionId theRequestPartitionId,
			TransactionDetails theTransactionDetails,
			Set<String> theParameters) {
		StopWatch sw = new StopWatch();

		IFhirResourceDao<?> dao = myDaoRegistry.getResourceDao(theResourceId.getResourceType());
		ResourceTable entity = (ResourceTable) dao.readEntity(theResourceId, theRequestDetails);
		IBaseResource resource = myJpaStorageResourceParser.toResource(entity, false);

		// Invoke the pre-access and pre-show interceptors in case there are any security
		// restrictions or audit requirements around the user accessing this resource
		BaseHapiFhirResourceDao.invokeStoragePreAccessResources(
				myInterceptorService, theRequestDetails, theResourceId, resource);
		BaseHapiFhirResourceDao.invokeStoragePreShowResources(myInterceptorService, theRequestDetails, resource);

		ISearchParamExtractor.ISearchParamFilter searchParamFilter = ISearchParamExtractor.ALL_PARAMS;
		if (theParameters != null) {
			searchParamFilter = params -> params.stream()
					.filter(t -> theParameters.contains(t.getName()))
					.collect(Collectors.toSet());
		}

		ResourceIndexedSearchParams newParamsToPopulate = ResourceIndexedSearchParams.withSets();
		mySearchParamExtractorService.extractFromResource(
				theRequestPartitionId,
				theRequestDetails,
				newParamsToPopulate,
				ResourceIndexedSearchParams.empty(),
				entity,
				resource,
				theTransactionDetails,
				false,
				searchParamFilter);

		ResourceIndexedSearchParams existingParamsToPopulate;
		boolean showAction;
		if (theParameters == null) {
			existingParamsToPopulate = ResourceIndexedSearchParams.withLists(entity);
			existingParamsToPopulate.mySearchParamPresentEntities.addAll(entity.getSearchParamPresents());
			fillInParamNames(
					entity, existingParamsToPopulate.mySearchParamPresentEntities, theResourceId.getResourceType());
			showAction = true;
		} else {
			existingParamsToPopulate = ResourceIndexedSearchParams.withSets();
			showAction = false;
		}

		String message = "Reindex dry-run completed in " + sw + ". No changes were committed to any stored data.";
		return buildIndexResponse(existingParamsToPopulate, newParamsToPopulate, showAction, List.of(message));
	}

	@Nonnull
	private RequestPartitionId determinePartition(RequestDetails theRequestDetails, IIdType theResourceId) {
		return myPartitionHelperSvc.determineReadPartitionForRequestForRead(theRequestDetails, theResourceId);
	}

	@Nonnull
	@VisibleForTesting
	Parameters buildIndexResponse(
			ResourceIndexedSearchParams theExistingParams,
			ResourceIndexedSearchParams theNewParams,
			boolean theShowAction,
			List<String> theMessages) {
		Parameters parameters = new Parameters();

		Parameters.ParametersParameterComponent narrativeParameter = parameters.addParameter();
		narrativeParameter.setName("Narrative");

		for (String next : theMessages) {
			parameters.addParameter("Message", new StringType(next));
		}

		// Normal indexes
		addParamsNonMissing(
				parameters,
				"CoordinateIndexes",
				"Coords",
				theExistingParams.myCoordsParams,
				theNewParams.myCoordsParams,
				new CoordsParamPopulator(),
				theShowAction);
		addParamsNonMissing(
				parameters,
				"DateIndexes",
				"Date",
				theExistingParams.myDateParams,
				theNewParams.myDateParams,
				new DateParamPopulator(),
				theShowAction);
		addParamsNonMissing(
				parameters,
				"NumberIndexes",
				"Number",
				theExistingParams.myNumberParams,
				theNewParams.myNumberParams,
				new NumberParamPopulator(),
				theShowAction);
		addParamsNonMissing(
				parameters,
				"QuantityIndexes",
				"Quantity",
				theExistingParams.myQuantityParams,
				theNewParams.myQuantityParams,
				new QuantityParamPopulator(),
				theShowAction);
		addParamsNonMissing(
				parameters,
				"QuantityIndexes",
				"QuantityNormalized",
				theExistingParams.myQuantityNormalizedParams,
				theNewParams.myQuantityNormalizedParams,
				new QuantityNormalizedParamPopulator(),
				theShowAction);
		addParamsNonMissing(
				parameters,
				"UriIndexes",
				"Uri",
				theExistingParams.myUriParams,
				theNewParams.myUriParams,
				new UriParamPopulator(),
				theShowAction);
		addParamsNonMissing(
				parameters,
				"StringIndexes",
				"String",
				theExistingParams.myStringParams,
				theNewParams.myStringParams,
				new StringParamPopulator(),
				theShowAction);
		addParamsNonMissing(
				parameters,
				"TokenIndexes",
				"Token",
				theExistingParams.myTokenParams,
				theNewParams.myTokenParams,
				new TokenParamPopulator(),
				theShowAction);

		// Resource links
		addParams(
				parameters,
				"ResourceLinks",
				"Reference",
				normalizeLinks(theExistingParams.myLinks),
				normalizeLinks(theNewParams.myLinks),
				new ResourceLinkPopulator(),
				theShowAction);

		// Combo search params
		addParams(
				parameters,
				"UniqueIndexes",
				"ComboStringUnique",
				theExistingParams.myComboStringUniques,
				theNewParams.myComboStringUniques,
				new ComboStringUniquePopulator(),
				theShowAction);
		addParams(
				parameters,
				"NonUniqueIndexes",
				"ComboTokenNonUnique",
				theExistingParams.myComboTokenNonUnique,
				theNewParams.myComboTokenNonUnique,
				new ComboTokenNonUniquePopulator(),
				theShowAction);

		// Missing (:missing) indexes
		addParamsMissing(
				parameters,
				"Coords",
				theExistingParams.myCoordsParams,
				theNewParams.myCoordsParams,
				new MissingIndexParamPopulator<>(),
				theShowAction);
		addParamsMissing(
				parameters,
				"Date",
				theExistingParams.myDateParams,
				theNewParams.myDateParams,
				new MissingIndexParamPopulator<>(),
				theShowAction);
		addParamsMissing(
				parameters,
				"Number",
				theExistingParams.myNumberParams,
				theNewParams.myNumberParams,
				new MissingIndexParamPopulator<>(),
				theShowAction);
		addParamsMissing(
				parameters,
				"Quantity",
				theExistingParams.myQuantityParams,
				theNewParams.myQuantityParams,
				new MissingIndexParamPopulator<>(),
				theShowAction);
		addParamsMissing(
				parameters,
				"QuantityNormalized",
				theExistingParams.myQuantityNormalizedParams,
				theNewParams.myQuantityNormalizedParams,
				new MissingIndexParamPopulator<>(),
				theShowAction);
		addParamsMissing(
				parameters,
				"Uri",
				theExistingParams.myUriParams,
				theNewParams.myUriParams,
				new MissingIndexParamPopulator<>(),
				theShowAction);
		addParamsMissing(
				parameters,
				"String",
				theExistingParams.myStringParams,
				theNewParams.myStringParams,
				new MissingIndexParamPopulator<>(),
				theShowAction);
		addParamsMissing(
				parameters,
				"Token",
				theExistingParams.myTokenParams,
				theNewParams.myTokenParams,
				new MissingIndexParamPopulator<>(),
				theShowAction);
		addParams(
				parameters,
				"MissingIndexes",
				"Reference",
				theExistingParams.mySearchParamPresentEntities,
				theNewParams.mySearchParamPresentEntities,
				new SearchParamPresentParamPopulator(),
				theShowAction);

		String narrativeText = myNarrativeGenerator.generateResourceNarrative(myContextR4, parameters);
		narrativeParameter.setValue(new StringType(narrativeText));

		return parameters;
	}

	/**
	 * The {@link SearchParamPresentEntity} entity doesn't actually store the parameter names
	 * in the database entity, it only stores a hash. So we brute force possible hashes here
	 * to figure out the associated param names.
	 */
	private void fillInParamNames(
			ResourceTable theEntity, Collection<SearchParamPresentEntity> theTarget, String theResourceName) {
		Map<Long, String> hashes = new HashMap<>();
		ResourceSearchParams searchParams = mySearchParamRegistry.getActiveSearchParams(theResourceName);
		for (RuntimeSearchParam next : searchParams.values()) {
			hashes.put(
					SearchParamPresentEntity.calculateHashPresence(
							myPartitionSettings, theEntity.getPartitionId(), theResourceName, next.getName(), true),
					next.getName());
			hashes.put(
					SearchParamPresentEntity.calculateHashPresence(
							myPartitionSettings, theEntity.getPartitionId(), theResourceName, next.getName(), false),
					next.getName());
		}

		for (SearchParamPresentEntity next : theTarget) {
			if (next.getParamName() == null) {
				String name = hashes.get(next.getHashPresence());
				name = defaultIfBlank(name, "(unknown)");
				next.setParamName(name);
			}
		}
	}

	private enum ActionEnum {
		ADD,
		REMOVE,
		UNKNOWN,
		NO_CHANGE
	}

	private abstract static class BaseParamPopulator<T> {

		@Nonnull
		public Parameters.ParametersParameterComponent addIndexValue(
				ActionEnum theAction,
				Parameters.ParametersParameterComponent theParent,
				T theParam,
				String theParamTypeName) {
			Parameters.ParametersParameterComponent retVal = theParent.addPart().setName(toPartName(theParam));
			retVal.addPart().setName("Action").setValue(new CodeType(theAction.name()));
			if (theParamTypeName != null) {
				retVal.addPart().setName("Type").setValue(new CodeType(theParamTypeName));
			}
			return retVal;
		}

		protected abstract String toPartName(T theParam);

		public void sort(List<T> theParams) {
			theParams.sort(comparing(this::toPartName));
		}
	}

	public abstract static class BaseIndexParamPopulator<T extends BaseResourceIndexedSearchParam>
			extends BaseParamPopulator<T> {
		@Override
		protected String toPartName(T theParam) {
			return theParam.getParamName();
		}
	}

	private static class ComboStringUniquePopulator extends BaseParamPopulator<ResourceIndexedComboStringUnique> {
		@Override
		protected String toPartName(ResourceIndexedComboStringUnique theParam) {
			return theParam.getIndexString();
		}
	}

	private static class ComboTokenNonUniquePopulator extends BaseParamPopulator<ResourceIndexedComboTokenNonUnique> {
		@Override
		protected String toPartName(ResourceIndexedComboTokenNonUnique theParam) {
			return theParam.getIndexString();
		}
	}

	private static class CoordsParamPopulator extends BaseIndexParamPopulator<ResourceIndexedSearchParamCoords> {
		@Nonnull
		@Override
		public Parameters.ParametersParameterComponent addIndexValue(
				ActionEnum theAction,
				Parameters.ParametersParameterComponent theParent,
				ResourceIndexedSearchParamCoords theParam,
				String theParamTypeName) {
			Parameters.ParametersParameterComponent retVal =
					super.addIndexValue(theAction, theParent, theParam, theParamTypeName);
			if (theParam.getLatitude() != null) {
				retVal.addPart().setName("Latitude").setValue(new DecimalType(theParam.getLatitude()));
			}
			if (theParam.getLongitude() != null) {
				retVal.addPart().setName("Longitude").setValue(new DecimalType(theParam.getLongitude()));
			}
			return retVal;
		}
	}

	private static class DateParamPopulator extends BaseIndexParamPopulator<ResourceIndexedSearchParamDate> {

		@Nonnull
		@Override
		public Parameters.ParametersParameterComponent addIndexValue(
				ActionEnum theAction,
				Parameters.ParametersParameterComponent theParent,
				ResourceIndexedSearchParamDate theParam,
				String theParamTypeName) {
			Parameters.ParametersParameterComponent retVal =
					super.addIndexValue(theAction, theParent, theParam, theParamTypeName);
			retVal.addPart().setName("High").setValue(new InstantType(theParam.getValueHigh()));
			retVal.addPart().setName("Low").setValue(new InstantType(theParam.getValueLow()));
			return retVal;
		}
	}

	private static class MissingIndexParamPopulator<T extends BaseResourceIndexedSearchParam>
			extends BaseIndexParamPopulator<T> {
		@Nonnull
		@Override
		public Parameters.ParametersParameterComponent addIndexValue(
				ActionEnum theAction,
				Parameters.ParametersParameterComponent theParent,
				T theParam,
				String theParamTypeName) {
			Parameters.ParametersParameterComponent retVal =
					super.addIndexValue(theAction, theParent, theParam, theParamTypeName);
			retVal.addPart().setName("Missing").setValue(new BooleanType(theParam.isMissing()));
			return retVal;
		}
	}

	private static class NumberParamPopulator extends BaseIndexParamPopulator<ResourceIndexedSearchParamNumber> {

		@Nonnull
		@Override
		public Parameters.ParametersParameterComponent addIndexValue(
				ActionEnum theAction,
				Parameters.ParametersParameterComponent theParent,
				ResourceIndexedSearchParamNumber theParam,
				String theParamTypeName) {
			Parameters.ParametersParameterComponent retVal =
					super.addIndexValue(theAction, theParent, theParam, theParamTypeName);
			retVal.addPart().setName("Value").setValue(new DecimalType(theParam.getValue()));
			return retVal;
		}
	}

	private static class QuantityParamPopulator extends BaseIndexParamPopulator<ResourceIndexedSearchParamQuantity> {

		@Nonnull
		@Override
		public Parameters.ParametersParameterComponent addIndexValue(
				ActionEnum theAction,
				Parameters.ParametersParameterComponent theParent,
				ResourceIndexedSearchParamQuantity theParam,
				String theParamTypeName) {
			Parameters.ParametersParameterComponent retVal =
					super.addIndexValue(theAction, theParent, theParam, theParamTypeName);
			retVal.addPart().setName("Value").setValue(new DecimalType(theParam.getValue()));
			retVal.addPart().setName("System").setValue(new UriType(theParam.getSystem()));
			retVal.addPart().setName("Units").setValue(new CodeType(theParam.getUnits()));
			return retVal;
		}
	}

	private static class QuantityNormalizedParamPopulator
			extends BaseIndexParamPopulator<ResourceIndexedSearchParamQuantityNormalized> {

		@Nonnull
		@Override
		public Parameters.ParametersParameterComponent addIndexValue(
				ActionEnum theAction,
				Parameters.ParametersParameterComponent theParent,
				ResourceIndexedSearchParamQuantityNormalized theParam,
				String theParamTypeName) {
			Parameters.ParametersParameterComponent retVal =
					super.addIndexValue(theAction, theParent, theParam, theParamTypeName);
			retVal.addPart().setName("Value").setValue(new DecimalType(theParam.getValue()));
			retVal.addPart().setName("System").setValue(new UriType(theParam.getSystem()));
			retVal.addPart().setName("Units").setValue(new CodeType(theParam.getUnits()));
			return retVal;
		}
	}

	private static class ResourceLinkPopulator extends BaseParamPopulator<ResourceLink> {

		@Nonnull
		@Override
		public Parameters.ParametersParameterComponent addIndexValue(
				ActionEnum theAction,
				Parameters.ParametersParameterComponent theParent,
				ResourceLink theParam,
				String theParamTypeName) {
			Parameters.ParametersParameterComponent retVal =
					super.addIndexValue(theAction, theParent, theParam, theParamTypeName);
			if (theParam.getTargetResourceId() != null) {
				retVal.addPart()
						.setName("TargetId")
						.setValue(new StringType(
								theParam.getTargetResourceType() + "/" + theParam.getTargetResourceId()));
			} else if (theParam.getTargetResourceUrl() != null) {
				retVal.addPart().setName("TargetUrl").setValue(new UrlType(theParam.getTargetResourceUrl()));
			}

			if (theParam.getTargetResourceVersion() != null) {
				retVal.addPart()
						.setName("TargetVersion")
						.setValue(new StringType(
								theParam.getTargetResourceVersion().toString()));
			}

			return retVal;
		}

		@Override
		protected String toPartName(ResourceLink theParam) {
			return theParam.getSourcePath();
		}
	}

	private static class SearchParamPresentParamPopulator extends BaseParamPopulator<SearchParamPresentEntity> {
		@Nonnull
		@Override
		public Parameters.ParametersParameterComponent addIndexValue(
				ActionEnum theAction,
				Parameters.ParametersParameterComponent theParent,
				SearchParamPresentEntity theParam,
				String theParamTypeName) {
			Parameters.ParametersParameterComponent retVal =
					super.addIndexValue(theAction, theParent, theParam, theParamTypeName);
			retVal.addPart().setName("Missing").setValue(new BooleanType(!theParam.isPresent()));
			return retVal;
		}

		@Override
		protected String toPartName(SearchParamPresentEntity theParam) {
			return theParam.getParamName();
		}
	}

	private static class StringParamPopulator extends BaseIndexParamPopulator<ResourceIndexedSearchParamString> {

		@Nonnull
		@Override
		public Parameters.ParametersParameterComponent addIndexValue(
				ActionEnum theAction,
				Parameters.ParametersParameterComponent theParent,
				ResourceIndexedSearchParamString theParam,
				String theParamTypeName) {
			Parameters.ParametersParameterComponent retVal =
					super.addIndexValue(theAction, theParent, theParam, theParamTypeName);
			retVal.addPart().setName("ValueNormalized").setValue(new StringType(theParam.getValueNormalized()));
			retVal.addPart().setName("ValueExact").setValue(new StringType(theParam.getValueExact()));
			return retVal;
		}
	}

	private static class TokenParamPopulator extends BaseIndexParamPopulator<ResourceIndexedSearchParamToken> {

		@Nonnull
		@Override
		public Parameters.ParametersParameterComponent addIndexValue(
				ActionEnum theAction,
				Parameters.ParametersParameterComponent theParent,
				ResourceIndexedSearchParamToken theParam,
				String theParamTypeName) {
			Parameters.ParametersParameterComponent retVal =
					super.addIndexValue(theAction, theParent, theParam, theParamTypeName);
			if (isNotBlank(theParam.getSystem())) {
				retVal.addPart().setName("System").setValue(new StringType(theParam.getSystem()));
			}
			if (isNotBlank(theParam.getValue())) {
				retVal.addPart().setName("Value").setValue(new StringType(theParam.getValue()));
			}
			return retVal;
		}
	}

	private static class UriParamPopulator extends BaseIndexParamPopulator<ResourceIndexedSearchParamUri> {

		@Nonnull
		@Override
		public Parameters.ParametersParameterComponent addIndexValue(
				ActionEnum theAction,
				Parameters.ParametersParameterComponent theParent,
				ResourceIndexedSearchParamUri theParam,
				String theParamTypeName) {
			Parameters.ParametersParameterComponent retVal =
					super.addIndexValue(theAction, theParent, theParam, theParamTypeName);
			retVal.addPart().setName("Value").setValue(new UriType(theParam.getUri()));
			return retVal;
		}
	}

	/**
	 * Links loaded from the database have a PID link to their target, but the ones
	 * extracted from the resource in memory won't have the PID. So this method
	 * strips the PIDs so that the generated hashCodes and equals comparisons
	 * will actually be equal.
	 */
	private static List<ResourceLink> normalizeLinks(Collection<ResourceLink> theLinks) {
		return theLinks.stream().map(ResourceLink::cloneWithoutTargetPid).collect(Collectors.toList());
	}

	private static <T> void addParams(
			Parameters theParameters,
			String theSectionName,
			String theTypeName,
			Collection<T> theExistingParams,
			Collection<T> theNewParams,
			BaseParamPopulator<T> thePopulator,
			boolean theShowAction) {
		List<T> addedParams = subtract(theNewParams, theExistingParams);
		thePopulator.sort(addedParams);
		for (T next : addedParams) {
			Parameters.ParametersParameterComponent parent = getOrCreateSection(theParameters, theSectionName);
			if (theShowAction) {
				thePopulator.addIndexValue(ActionEnum.ADD, parent, next, theTypeName);
			} else {
				thePopulator.addIndexValue(ActionEnum.UNKNOWN, parent, next, theTypeName);
			}
		}

		List<T> removedParams = subtract(theExistingParams, theNewParams);
		addedParams.sort(comparing(thePopulator::toPartName));
		for (T next : removedParams) {
			Parameters.ParametersParameterComponent parent = getOrCreateSection(theParameters, theSectionName);
			thePopulator.addIndexValue(ActionEnum.REMOVE, parent, next, theTypeName);
		}

		List<T> unchangedParams = new ArrayList<>(intersection(theNewParams, theExistingParams));
		addedParams.sort(comparing(thePopulator::toPartName));
		for (T next : unchangedParams) {
			Parameters.ParametersParameterComponent parent = getOrCreateSection(theParameters, theSectionName);
			thePopulator.addIndexValue(ActionEnum.NO_CHANGE, parent, next, theTypeName);
		}
	}

	private static <T extends BaseResourceIndexedSearchParam> void addParamsNonMissing(
			Parameters theParameters,
			String theSectionName,
			String theTypeName,
			Collection<T> theExistingParams,
			Collection<T> theNewParams,
			BaseParamPopulator<T> thePopulator,
			boolean theShowAction) {
		Collection<T> existingParams = filterWantMissing(theExistingParams, false);
		Collection<T> newParams = filterWantMissing(theNewParams, false);
		addParams(theParameters, theSectionName, theTypeName, existingParams, newParams, thePopulator, theShowAction);
	}

	private static <T extends BaseResourceIndexedSearchParam> void addParamsMissing(
			Parameters theParameters,
			String theTypeName,
			Collection<T> theExistingParams,
			Collection<T> theNewParams,
			BaseParamPopulator<T> thePopulator,
			boolean theShowAction) {
		Collection<T> existingParams = filterWantMissing(theExistingParams, true);
		Collection<T> newParams = filterWantMissing(theNewParams, true);
		addParams(theParameters, "MissingIndexes", theTypeName, existingParams, newParams, thePopulator, theShowAction);
	}

	private static <T extends BaseResourceIndexedSearchParam> Collection<T> filterWantMissing(
			Collection<T> theNewParams, boolean theWantMissing) {
		return theNewParams.stream()
				.filter(t -> t.isMissing() == theWantMissing)
				.collect(Collectors.toList());
	}

	@Nonnull
	private static Parameters.ParametersParameterComponent getOrCreateSection(
			Parameters theParameters, String theSectionName) {
		Parameters.ParametersParameterComponent parent = theParameters.getParameter(theSectionName);
		if (parent == null) {
			parent = theParameters.addParameter();
			parent.setName(theSectionName);
		}
		return parent;
	}
}
