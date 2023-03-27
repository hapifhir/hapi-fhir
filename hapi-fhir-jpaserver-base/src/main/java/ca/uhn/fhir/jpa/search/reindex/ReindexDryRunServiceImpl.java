package ca.uhn.fhir.jpa.search.reindex;

import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.interceptor.model.ReadPartitionIdRequestDetails;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.dao.BaseHapiFhirResourceDao;
import ca.uhn.fhir.jpa.dao.IJpaStorageResourceParser;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
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
import ca.uhn.fhir.jpa.searchparam.extractor.ResourceIndexedSearchParams;
import ca.uhn.fhir.jpa.searchparam.extractor.SearchParamExtractorService;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import ca.uhn.fhir.rest.server.util.ResourceSearchParams;
import ca.uhn.hapi.converters.canonical.VersionCanonicalizer;
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

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ca.uhn.fhir.jpa.dao.index.DaoSearchParamSynchronizer.subtract;
import static java.util.Comparator.comparing;
import static org.apache.commons.collections4.CollectionUtils.intersection;
import static org.apache.commons.lang3.StringUtils.defaultIfBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class ReindexDryRunServiceImpl implements IReindexDryRunService {

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

	@Override
	public IBaseParameters reindexDryRun(RequestDetails theRequestDetails, IIdType theResourceId) {
		ReadPartitionIdRequestDetails details = ReadPartitionIdRequestDetails.forRead(theResourceId);
		RequestPartitionId partitionId = myPartitionHelperSvc.determineReadPartitionForRequest(theRequestDetails, details);
		TransactionDetails transactionDetails = new TransactionDetails();

		Parameters retValCanonical = myTransactionService
			.withRequest(theRequestDetails)
			.withTransactionDetails(transactionDetails)
			.withRequestPartitionId(partitionId)
			.execute(() -> reindexDryRunInTransaction(theRequestDetails, theResourceId, partitionId, transactionDetails));
		return myVersionCanonicalizer.parametersFromCanonical(retValCanonical);
	}

	@Nonnull
	private Parameters reindexDryRunInTransaction(RequestDetails theRequestDetails, IIdType theResourceId, RequestPartitionId partitionId, TransactionDetails transactionDetails) {
		IFhirResourceDao<?> dao = myDaoRegistry.getResourceDao(theResourceId.getResourceType());
		ResourceTable entity = (ResourceTable) dao.readEntity(theResourceId, theRequestDetails);
		IBaseResource resource = myJpaStorageResourceParser.toResource(entity, false);

		// Invoke the pre-access and pre-show interceptors in case there are any security
		// restrictions or audit requirements around the user accessing this resource
		BaseHapiFhirResourceDao.invokeStoragePreAccessResources(myInterceptorService, theRequestDetails, theResourceId, resource);
		BaseHapiFhirResourceDao.invokeStoragePreShowResources(myInterceptorService, theRequestDetails, resource);

		Parameters parameters = new Parameters();

		ResourceIndexedSearchParams newParamsToPopulate = new ResourceIndexedSearchParams();
		mySearchParamExtractorService.extractFromResource(partitionId, theRequestDetails, newParamsToPopulate, entity, resource, transactionDetails, false);

		ResourceIndexedSearchParams existingParamsToPopulate = new ResourceIndexedSearchParams(entity);
		existingParamsToPopulate.mySearchParamPresentEntities.addAll(entity.getSearchParamPresents());
		fillInParamNames(entity, existingParamsToPopulate.mySearchParamPresentEntities, theResourceId.getResourceType());

		// Normal indexes
		addParamsNonMissing(parameters, "CoordinateIndexes", "Coords", existingParamsToPopulate.myCoordsParams, newParamsToPopulate.myCoordsParams, new CoordsParamPopulator());
		addParamsNonMissing(parameters, "DateIndexes", "Date", existingParamsToPopulate.myDateParams, newParamsToPopulate.myDateParams, new DateParamPopulator());
		addParamsNonMissing(parameters, "NumberIndexes", "Number", existingParamsToPopulate.myNumberParams, newParamsToPopulate.myNumberParams, new NumberParamPopulator());
		addParamsNonMissing(parameters, "QuantityIndexes", "Quantity", existingParamsToPopulate.myQuantityParams, newParamsToPopulate.myQuantityParams, new QuantityParamPopulator());
		addParamsNonMissing(parameters, "QuantityIndexes", "QuantityNormalized", existingParamsToPopulate.myQuantityNormalizedParams, newParamsToPopulate.myQuantityNormalizedParams, new QuantityNormalizedParamPopulator());
		addParamsNonMissing(parameters, "UriIndexes", "Uri", existingParamsToPopulate.myUriParams, newParamsToPopulate.myUriParams, new UriParamPopulator());
		addParamsNonMissing(parameters, "StringIndexes", "String", existingParamsToPopulate.myStringParams, newParamsToPopulate.myStringParams, new StringParamPopulator());
		addParamsNonMissing(parameters, "TokenIndexes", "Token", existingParamsToPopulate.myTokenParams, newParamsToPopulate.myTokenParams, new TokenParamPopulator());

		// Resource links
		addParams(parameters, "ResourceLinks", "Reference", normalizeLinks(existingParamsToPopulate.myLinks), normalizeLinks(newParamsToPopulate.myLinks), new ResourceLinkPopulator());

		// Combo search params
		addParams(parameters, "UniqueIndexes", "ComboStringUnique", existingParamsToPopulate.myComboStringUniques, newParamsToPopulate.myComboStringUniques, new ComboStringUniquePopulator());
		addParams(parameters, "NonUniqueIndexes", "ComboTokenNonUnique", existingParamsToPopulate.myComboTokenNonUnique, newParamsToPopulate.myComboTokenNonUnique, new ComboTokenNonUniquePopulator());

		// Missing (:missing) indexes
		addParamsMissing(parameters, "MissingIndexes", "Coords", existingParamsToPopulate.myCoordsParams, newParamsToPopulate.myCoordsParams, new MissingIndexParamPopulator<>());
		addParamsMissing(parameters, "MissingIndexes", "Date", existingParamsToPopulate.myDateParams, newParamsToPopulate.myDateParams, new MissingIndexParamPopulator<>());
		addParamsMissing(parameters, "MissingIndexes", "Number", existingParamsToPopulate.myNumberParams, newParamsToPopulate.myNumberParams, new MissingIndexParamPopulator<>());
		addParamsMissing(parameters, "MissingIndexes", "Quantity", existingParamsToPopulate.myQuantityParams, newParamsToPopulate.myQuantityParams, new MissingIndexParamPopulator<>());
		addParamsMissing(parameters, "MissingIndexes", "QuantityNormalized", existingParamsToPopulate.myQuantityNormalizedParams, newParamsToPopulate.myQuantityNormalizedParams, new MissingIndexParamPopulator<>());
		addParamsMissing(parameters, "MissingIndexes", "Uri", existingParamsToPopulate.myUriParams, newParamsToPopulate.myUriParams, new MissingIndexParamPopulator<>());
		addParamsMissing(parameters, "MissingIndexes", "String", existingParamsToPopulate.myStringParams, newParamsToPopulate.myStringParams, new MissingIndexParamPopulator<>());
		addParamsMissing(parameters, "MissingIndexes", "Token", existingParamsToPopulate.myTokenParams, newParamsToPopulate.myTokenParams, new MissingIndexParamPopulator<>());
		addParams(parameters, "MissingIndexes", "Reference", existingParamsToPopulate.mySearchParamPresentEntities, newParamsToPopulate.mySearchParamPresentEntities, new SearchParamPresentParamPopulator());

		return parameters;
	}


	@Autowired
	private ISearchParamRegistry mySearchParamRegistry;

	/**
	 * The {@link SearchParamPresentEntity} entity doesn't actually store the parameter names
	 * in the database entity, it only stores a hash. So we brute force possible hashes here
	 * to figure out the associated param names.
	 */
	private void fillInParamNames(ResourceTable theEntity, Collection<SearchParamPresentEntity> theTarget, String theResourceName) {
		Map<Long, String> hashes = new HashMap<>();
		ResourceSearchParams searchParams = mySearchParamRegistry.getActiveSearchParams(theResourceName);
		for (RuntimeSearchParam next : searchParams.values()){
			hashes.put(SearchParamPresentEntity.calculateHashPresence(myPartitionSettings, theEntity.getPartitionId(), theResourceName, next.getName(), true), next.getName());
			hashes.put(SearchParamPresentEntity.calculateHashPresence(myPartitionSettings, theEntity.getPartitionId(), theResourceName, next.getName(), false), next.getName());
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
		NO_CHANGE

	}

	private static abstract class BaseParamPopulator<T> {


		@Nonnull
		public Parameters.ParametersParameterComponent addIndexValue(ActionEnum theAction, Parameters.ParametersParameterComponent theParent, T theParam, String theParamTypeName) {
			Parameters.ParametersParameterComponent retVal = theParent
				.addPart()
				.setName(toPartName(theParam));
			retVal
				.addPart()
				.setName("Action")
				.setValue(new CodeType(theAction.name()));
			if (theParamTypeName != null) {
				retVal
					.addPart()
					.setName("Type")
					.setValue(new CodeType(theParamTypeName));
			}
			return retVal;
		}

		protected abstract String toPartName(T theParam);

		public void sort(List<T> theParams) {
			theParams.sort(comparing(this::toPartName));
		}
	}

	public static class BaseIndexParamPopulator<T extends BaseResourceIndexedSearchParam> extends BaseParamPopulator<T> {
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
		public Parameters.ParametersParameterComponent addIndexValue(ActionEnum theAction, Parameters.ParametersParameterComponent theParent, ResourceIndexedSearchParamCoords theParam, String theParamTypeName) {
			Parameters.ParametersParameterComponent retVal = super.addIndexValue(theAction, theParent, theParam, theParamTypeName);
			retVal
				.addPart()
				.setName("Latitude")
				.setValue(new DecimalType(theParam.getLatitude()));
			retVal
				.addPart()
				.setName("Longitude")
				.setValue(new DecimalType(theParam.getLongitude()));
			return retVal;
		}


	}

	private static class DateParamPopulator extends BaseIndexParamPopulator<ResourceIndexedSearchParamDate> {

		@Nonnull
		@Override
		public Parameters.ParametersParameterComponent addIndexValue(ActionEnum theAction, Parameters.ParametersParameterComponent theParent, ResourceIndexedSearchParamDate theParam, String theParamTypeName) {
			Parameters.ParametersParameterComponent retVal = super.addIndexValue(theAction, theParent, theParam, theParamTypeName);
			retVal
				.addPart()
				.setName("High")
				.setValue(new InstantType(theParam.getValueHigh()));
			retVal
				.addPart()
				.setName("Low")
				.setValue(new InstantType(theParam.getValueLow()));
			return retVal;
		}
	}

	private static class MissingIndexParamPopulator<T extends BaseResourceIndexedSearchParam> extends BaseIndexParamPopulator<T> {
		@Nonnull
		@Override
		public Parameters.ParametersParameterComponent addIndexValue(ActionEnum theAction, Parameters.ParametersParameterComponent theParent, T theParam, String theParamTypeName) {
			Parameters.ParametersParameterComponent retVal = super.addIndexValue(theAction, theParent, theParam, theParamTypeName);
			retVal
				.addPart()
				.setName("Missing")
				.setValue(new BooleanType(theParam.isMissing()));
			return retVal;
		}


	}

	private static class NumberParamPopulator extends BaseIndexParamPopulator<ResourceIndexedSearchParamNumber> {


		@Nonnull
		@Override
		public Parameters.ParametersParameterComponent addIndexValue(ActionEnum theAction, Parameters.ParametersParameterComponent theParent, ResourceIndexedSearchParamNumber theParam, String theParamTypeName) {
			Parameters.ParametersParameterComponent retVal = super.addIndexValue(theAction, theParent, theParam, theParamTypeName);
			retVal
				.addPart()
				.setName("Value")
				.setValue(new DecimalType(theParam.getValue()));
			return retVal;
		}

	}
	private static class QuantityParamPopulator extends BaseIndexParamPopulator<ResourceIndexedSearchParamQuantity> {

		@Nonnull
		@Override
		public Parameters.ParametersParameterComponent addIndexValue(ActionEnum theAction, Parameters.ParametersParameterComponent theParent, ResourceIndexedSearchParamQuantity theParam, String theParamTypeName) {
			Parameters.ParametersParameterComponent retVal = super.addIndexValue(theAction, theParent, theParam, theParamTypeName);
			retVal
				.addPart()
				.setName("Value")
				.setValue(new DecimalType(theParam.getValue()));
			retVal
				.addPart()
				.setName("System")
				.setValue(new UriType(theParam.getSystem()));
			retVal
				.addPart()
				.setName("Units")
				.setValue(new CodeType(theParam.getUnits()));
			return retVal;
		}

	}
	private static class QuantityNormalizedParamPopulator extends BaseIndexParamPopulator<ResourceIndexedSearchParamQuantityNormalized> {

		@Nonnull
		@Override
		public Parameters.ParametersParameterComponent addIndexValue(ActionEnum theAction, Parameters.ParametersParameterComponent theParent, ResourceIndexedSearchParamQuantityNormalized theParam, String theParamTypeName) {
			Parameters.ParametersParameterComponent retVal = super.addIndexValue(theAction, theParent, theParam, theParamTypeName);
			retVal
				.addPart()
				.setName("Value")
				.setValue(new DecimalType(theParam.getValue()));
			retVal
				.addPart()
				.setName("System")
				.setValue(new UriType(theParam.getSystem()));
			retVal
				.addPart()
				.setName("Units")
				.setValue(new CodeType(theParam.getUnits()));
			return retVal;
		}

	}
	private static class ResourceLinkPopulator extends BaseParamPopulator<ResourceLink> {


		@Nonnull
		@Override
		public Parameters.ParametersParameterComponent addIndexValue(ActionEnum theAction, Parameters.ParametersParameterComponent theParent, ResourceLink theParam, String theParamTypeName) {
			Parameters.ParametersParameterComponent retVal = super.addIndexValue(theAction, theParent, theParam, theParamTypeName);
			if (theParam.getTargetResourceId() != null) {
				retVal
					.addPart()
					.setName("TargetId")
					.setValue(new StringType(theParam.getTargetResourceType() + "/" + theParam.getTargetResourceId()));
			} else if (theParam.getTargetResourceUrl() != null) {
				retVal
					.addPart()
					.setName("TargetUrl")
					.setValue(new UrlType(theParam.getTargetResourceUrl()));
			}

			if (theParam.getTargetResourceVersion() != null) {
				retVal
					.addPart()
					.setName("TargetVersion")
					.setValue(new StringType(theParam.getTargetResourceVersion().toString()));
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
		public Parameters.ParametersParameterComponent addIndexValue(ActionEnum theAction, Parameters.ParametersParameterComponent theParent, SearchParamPresentEntity theParam, String theParamTypeName) {
			Parameters.ParametersParameterComponent retVal = super.addIndexValue(theAction, theParent, theParam, theParamTypeName);
			retVal
				.addPart()
				.setName("Missing")
				.setValue(new BooleanType(!theParam.isPresent()));
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
		public Parameters.ParametersParameterComponent addIndexValue(ActionEnum theAction, Parameters.ParametersParameterComponent theParent, ResourceIndexedSearchParamString theParam, String theParamTypeName) {
			Parameters.ParametersParameterComponent retVal = super.addIndexValue(theAction, theParent, theParam, theParamTypeName);
			retVal
				.addPart()
				.setName("ValueNormalized")
				.setValue(new StringType(theParam.getValueNormalized()));
			retVal
				.addPart()
				.setName("ValueExact")
				.setValue(new StringType(theParam.getValueExact()));
			return retVal;
		}
	}

	private static class TokenParamPopulator extends BaseIndexParamPopulator<ResourceIndexedSearchParamToken> {

		@Nonnull
		@Override
		public Parameters.ParametersParameterComponent addIndexValue(ActionEnum theAction, Parameters.ParametersParameterComponent theParent, ResourceIndexedSearchParamToken theParam, String theParamTypeName) {
			Parameters.ParametersParameterComponent retVal = super.addIndexValue(theAction, theParent, theParam, theParamTypeName);
			if (isNotBlank(theParam.getSystem())) {
				retVal
					.addPart()
					.setName("System")
					.setValue(new StringType(theParam.getSystem()));
			}
			if (isNotBlank(theParam.getValue())) {
				retVal
					.addPart()
					.setName("Value")
					.setValue(new StringType(theParam.getValue()));
			}
			return retVal;
		}
	}

	private static class UriParamPopulator extends BaseIndexParamPopulator<ResourceIndexedSearchParamUri> {

		@Nonnull
		@Override
		public Parameters.ParametersParameterComponent addIndexValue(ActionEnum theAction, Parameters.ParametersParameterComponent theParent, ResourceIndexedSearchParamUri theParam, String theParamTypeName) {
			Parameters.ParametersParameterComponent retVal = super.addIndexValue(theAction, theParent, theParam, theParamTypeName);
			retVal
				.addPart()
				.setName("Value")
				.setValue(new UriType(theParam.getUri()));
			return retVal;
		}
	}

	private static void addParametersSection(ResourceIndexedSearchParams theExistingParameters, ResourceIndexedSearchParams theNewParameters, Parameters theParameters) {
		addParamsNonMissing(theParameters, "CoordinateIndexes", "Coords", theExistingParameters.myCoordsParams, theNewParameters.myCoordsParams, new CoordsParamPopulator());
		addParamsNonMissing(theParameters, "DateIndexes", "Date", theExistingParameters.myDateParams, theNewParameters.myDateParams, new DateParamPopulator());
		addParamsNonMissing(theParameters, "NumberIndexes", "Number", theExistingParameters.myNumberParams, theNewParameters.myNumberParams, new NumberParamPopulator());
		addParamsNonMissing(theParameters, "QuantityIndexes", "Quantity", theExistingParameters.myQuantityParams, theNewParameters.myQuantityParams, new QuantityParamPopulator());
		addParamsNonMissing(theParameters, "QuantityIndexes", "QuantityNormalized", theExistingParameters.myQuantityNormalizedParams, theNewParameters.myQuantityNormalizedParams, new QuantityNormalizedParamPopulator());
		addParamsNonMissing(theParameters, "UriIndexes", "Uri", theExistingParameters.myUriParams, theNewParameters.myUriParams, new UriParamPopulator());
		addParamsNonMissing(theParameters, "StringIndexes", "String", theExistingParameters.myStringParams, theNewParameters.myStringParams, new StringParamPopulator());
		addParamsNonMissing(theParameters, "TokenIndexes", "Token", theExistingParameters.myTokenParams, theNewParameters.myTokenParams, new TokenParamPopulator());

		addParams(theParameters, "ResourceLinks", "Reference", normalizeLinks(theExistingParameters.myLinks), normalizeLinks(theNewParameters.myLinks), new ResourceLinkPopulator());

		addParams(theParameters, "UniqueIndexes", "ComboStringUnique", theExistingParameters.myComboStringUniques, theNewParameters.myComboStringUniques, new ComboStringUniquePopulator());
		addParams(theParameters, "NonUniqueIndexes", "ComboTokenNonUnique", theExistingParameters.myComboTokenNonUnique, theNewParameters.myComboTokenNonUnique, new ComboTokenNonUniquePopulator());

		addParamsNonMissing(theParameters, "MissingIndexes", "Coords", theExistingParameters.myCoordsParams, theNewParameters.myCoordsParams, new CoordsParamPopulator());
		addParamsNonMissing(theParameters, "MissingIndexes", "Date", theExistingParameters.myDateParams, theNewParameters.myDateParams, new DateParamPopulator());
		addParamsNonMissing(theParameters, "MissingIndexes", "Number", theExistingParameters.myNumberParams, theNewParameters.myNumberParams, new NumberParamPopulator());
		addParamsNonMissing(theParameters, "MissingIndexes", "Quantity", theExistingParameters.myQuantityParams, theNewParameters.myQuantityParams, new QuantityParamPopulator());
		addParamsNonMissing(theParameters, "MissingIndexes", "QuantityNormalized", theExistingParameters.myQuantityNormalizedParams, theNewParameters.myQuantityNormalizedParams, new QuantityNormalizedParamPopulator());
		addParamsNonMissing(theParameters, "MissingIndexes", "Uri", theExistingParameters.myUriParams, theNewParameters.myUriParams, new UriParamPopulator());
		addParamsNonMissing(theParameters, "MissingIndexes", "String", theExistingParameters.myStringParams, theNewParameters.myStringParams, new StringParamPopulator());
		addParamsNonMissing(theParameters, "MissingIndexes", "Token", theExistingParameters.myTokenParams, theNewParameters.myTokenParams, new TokenParamPopulator());
	}

	/**
	 * Links loaded from the database have a PID link to their target, but the ones
	 * extracted from the resource in memory won't have the PID. So this method
	 * strips the PIDs so that the generated hashCodes and equals comparisons
	 * will actually be equal.
	 */
	private static List<ResourceLink> normalizeLinks(Collection<ResourceLink> theLinks) {
		return theLinks
			.stream()
			.map(ResourceLink::cloneWithoutTargetPid)
			.toList();
	}

	private static <T> void addParams(Parameters theParameters, String theSectionName, String theTypeName, Collection<T> theExistingParams, Collection<T> theNewParams, BaseParamPopulator<T> thePopulator) {
		List<T> addedParams = subtract(theNewParams, theExistingParams);
		thePopulator.sort(addedParams);
		for (T next : addedParams) {
			Parameters.ParametersParameterComponent parent = getOrCreateSection(theParameters, theSectionName);
			thePopulator.addIndexValue(ActionEnum.ADD, parent, next, theTypeName);
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

	private static <T extends BaseResourceIndexedSearchParam> void addParamsNonMissing(Parameters theParameters, String theSectionName, String theTypeName, Collection<T> theExistingParams, Collection<T> theNewParams, BaseParamPopulator<T> thePopulator) {
		Collection<T> existingParams = filterWantMissing(theExistingParams, false);
		Collection<T> newParams = filterWantMissing(theNewParams, false);
		addParams(theParameters, theSectionName, theTypeName, existingParams, newParams, thePopulator);
	}

	private static <T extends BaseResourceIndexedSearchParam> void addParamsMissing(Parameters theParameters, String theSectionName, String theTypeName, Collection<T> theExistingParams, Collection<T> theNewParams, BaseParamPopulator<T> thePopulator) {
		Collection<T> existingParams = filterWantMissing(theExistingParams, true);
		Collection<T> newParams = filterWantMissing(theNewParams, true);
		addParams(theParameters, theSectionName, theTypeName, existingParams, newParams, thePopulator);
	}

	private static <T extends BaseResourceIndexedSearchParam> Collection<T> filterWantMissing(Collection<T> theNewParams, boolean theWantMissing) {
		return theNewParams
			.stream()
			.filter(t -> t.isMissing() == theWantMissing)
			.toList();
	}

	@Nonnull
	private static Parameters.ParametersParameterComponent getOrCreateSection(Parameters theParameters, String theSectionName) {
		Parameters.ParametersParameterComponent parent = theParameters.getParameter(theSectionName);
		if (parent == null) {
			parent = theParameters.addParameter();
			parent.setName(theSectionName);
		}
		return parent;
	}
}
