package ca.uhn.fhir.jpa.search.reindex;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.model.ReadPartitionIdRequestDetails;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.dao.IJpaStorageResourceParser;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.model.entity.BaseResourceIndexedSearchParam;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamString;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.partition.BaseRequestPartitionHelperSvc;
import ca.uhn.fhir.jpa.searchparam.extractor.ResourceIndexedSearchParams;
import ca.uhn.fhir.jpa.searchparam.extractor.SearchParamExtractorService;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.StringType;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;
import java.util.Collection;

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
	private DaoRegistry myDaoRegistry;
	@Autowired
	private FhirContext myContext;

	@Override
	public IBaseParameters reindexDryRun(RequestDetails theRequestDetails, IIdType theResourceId) {
		ReadPartitionIdRequestDetails details = ReadPartitionIdRequestDetails.forRead(theResourceId);
		RequestPartitionId partitionId = myPartitionHelperSvc.determineReadPartitionForRequest(theRequestDetails, details);
		TransactionDetails transactionDetails = new TransactionDetails();

		return myTransactionService
			.withRequest(theRequestDetails)
			.withTransactionDetails(transactionDetails)
			.withRequestPartitionId(partitionId)
			.execute(() -> {

				IFhirResourceDao dao = myDaoRegistry.getResourceDao(theResourceId.getResourceType());
				ResourceTable entity = (ResourceTable) dao.readEntity(theResourceId, theRequestDetails);
				IBaseResource resource = myJpaStorageResourceParser.toResource(entity, false);

				ResourceIndexedSearchParams paramsToPopulate = new ResourceIndexedSearchParams();
				mySearchParamExtractorService.extractFromResource(partitionId, theRequestDetails, paramsToPopulate, entity, resource, transactionDetails, false);

				Parameters parameters = new Parameters();
				Parameters.ParametersParameterComponent newParameters = parameters
					.addParameter()
					.setName("New");

				addParams(paramsToPopulate.myStringParams, new StringParamPopulator(), newParameters, "String");


				return null;

			});

	}

	private static class BaseParamPopulator<T extends BaseResourceIndexedSearchParam> {


		@Nonnull
		public Parameters.ParametersParameterComponent addIndexValue(Parameters.ParametersParameterComponent theParent, T theParam, String theParamTypeName) {
			Parameters.ParametersParameterComponent retVal = theParent
				.addPart()
				.setName(theParamTypeName);
			retVal
				.addPart()
				.setName("Name")
				.setValue(new CodeType(theParam.getParamName()));
			return retVal;
		}


	}

	private static class StringParamPopulator extends BaseParamPopulator<ResourceIndexedSearchParamString> {


		@Nonnull
		@Override
		public Parameters.ParametersParameterComponent addIndexValue(Parameters.ParametersParameterComponent theParent, ResourceIndexedSearchParamString theParam, String theParamTypeName) {
			Parameters.ParametersParameterComponent retVal = super.addIndexValue(theParent, theParam, theParamTypeName);
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

	private static <T extends BaseResourceIndexedSearchParam> void addParams(Collection<T> theParams, BaseParamPopulator<T> thePopulator, Parameters.ParametersParameterComponent theParent, String theParamTypeName) {
		for (T next : theParams) {
			thePopulator.addIndexValue(theParent, next, theParamTypeName);
		}
	}


}
