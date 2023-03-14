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
import ca.uhn.fhir.jpa.partition.RequestPartitionHelperSvc;
import ca.uhn.fhir.jpa.searchparam.extractor.ResourceIndexedSearchParams;
import ca.uhn.fhir.jpa.searchparam.extractor.SearchParamExtractorService;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import ca.uhn.fhir.rest.param.ParameterUtil;
import ca.uhn.fhir.util.ParametersUtil;
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
	private SearchParamExtractorService mySearchParamExtractorService;
	@Autowired
	private BaseRequestPartitionHelperSvc myPartitionHelperSvc;
	@Autowired
	private IHapiTransactionService myTransactionService;
	@Autowired
	private DaoRegistry myDaoRegistry;
	@Autowired
	protected IJpaStorageResourceParser myJpaStorageResourceParser;
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
			.execute(()->{

				IFhirResourceDao dao = myDaoRegistry.getResourceDao(theResourceId.getResourceType());
				ResourceTable entity = (ResourceTable) dao.readEntity(theResourceId, theRequestDetails);
				IBaseResource resource = myJpaStorageResourceParser.toResource(entity, false);

				ResourceIndexedSearchParams paramsToPopulate = new ResourceIndexedSearchParams();
				mySearchParamExtractorService.extractFromResource(partitionId, theRequestDetails, paramsToPopulate, entity, resource, transactionDetails, false);

				Parameters parameters = new Parameters();
				Parameters.ParametersParameterComponent newParameters = parameters
					.addParameter()
					.setName("New");

				addParams(paramsToPopulate.myStringParams, "String");


				return null;

			});

	}

	private static <T extends BaseResourceIndexedSearchParam> void addParams(Collection<T> theParams, String theParamTypeName, ) {
		for (ResourceIndexedSearchParamString next : paramsToPopulate.myStringParams) {
		}

	}

	private static void addParams(Parameters.ParametersParameterComponent newParameters, ResourceIndexedSearchParamString next, String paramTypeName) {
		Parameters.ParametersParameterComponent params = addIndexValueCommon(newParameters, next, paramTypeName);
		params
			.addPart()
			.setName("ValueNormalized")
			.setValue(new StringType(next.getValueNormalized()));
		params
			.addPart()
			.setName("ValueExact")
			.setValue(new StringType(next.getValueExact()));
	}

	@Nonnull
	private static Parameters.ParametersParameterComponent addIndexValueCommon(Parameters.ParametersParameterComponent newParameters, ResourceIndexedSearchParamString next, String paramTypeName) {
		Parameters.ParametersParameterComponent params = newParameters
			.addPart()
			.setName(paramTypeName);
		params
			.addPart()
			.setName("Name")
			.setValue(new CodeType(next.getParamName()));
		return params;
	}

}
