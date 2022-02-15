package ca.uhn.fhir.jpa.bulk.imprt2;

import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.model.JobInstanceParameter;
import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.util.OperationOutcomeUtil;
import ca.uhn.fhir.util.ParametersUtil;
import ca.uhn.fhir.util.ValidateUtil;
import org.apache.commons.lang3.tuple.Pair;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static ca.uhn.fhir.jpa.bulk.export.provider.BulkDataExportProvider.validatePreferAsyncHeader;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class BulkImportProvider {
	public static final String PARAM_INPUT_FORMAT = "inputFormat";
	public static final String PARAM_INPUT_SOURCE = "inputSource";
	public static final String PARAM_STORAGE_DETAIL = "storageDetail";
	public static final String PARAM_STORAGE_DETAIL_TYPE = "type";
	public static final String PARAM_STORAGE_DETAIL_TYPE_VAL_HTTPS = "https";
	public static final String PARAM_INPUT = "input";
	public static final String PARAM_INPUT_URL = "url";
	public static final String PARAM_STORAGE_DETAIL_CREDENTIAL_HTTP_BASIC = "credentialHttpBasic";
	public static final String PARAM_STORAGE_DETAIL_MAX_BATCH_RESOURCE_COUNT = "maxBatchResourceCount";

	static final String PARAM_INPUT_TYPE = "type";
	private static final Logger ourLog = LoggerFactory.getLogger(BulkImportProvider.class);
	@Autowired
	private IJobCoordinator myJobCoordinator;
	@Autowired
	private FhirContext myCtx;

	private volatile List<String> myResourceTypeOrder;

	/**
	 * Constructor
	 */
	public BulkImportProvider() {
		super();
	}

	public void setJobCoordinator(IJobCoordinator theJobCoordinator) {
		myJobCoordinator = theJobCoordinator;
	}

	public void setFhirContext(FhirContext theCtx) {
		myCtx = theCtx;
	}

	/**
	 * $import operation (Import by Manifest)
	 * <p>
	 * Note that this is a weird operation, defined here: https://github.com/smart-on-fhir/bulk-import/blob/master/import-manifest.md
	 * Per the definition in that spec, this operation isn't actually a FHIR operation using a Parameters resource, but instead uses an
	 * arbitrary JSON document as its payload.
	 * </p>
	 * <p>
	 * We are using a Parameters resource here, so we're not actually technically
	 * compliant with the spec as it is written (2022-02-15). However the text
	 * indicates that there has been a suggestion to use Parameters so this
	 * feels like the right direction.
	 * </p>
	 */
	@Operation(name = JpaConstants.OPERATION_IMPORT, idempotent = false, manualResponse = true)
	public void importByManifest(
		ServletRequestDetails theRequestDetails,
		@ResourceParam IBaseParameters theRequest,
		HttpServletResponse theResponse) throws IOException {

		validatePreferAsyncHeader(theRequestDetails, JpaConstants.OPERATION_IMPORT);

		JobInstanceStartRequest request = new JobInstanceStartRequest();
		request.setJobDefinitionId(BulkImport2AppCtx.JOB_BULK_IMPORT_PULL);

		String inputFormat = ParametersUtil.getNamedParameterValueAsString(myCtx, theRequest, PARAM_INPUT_FORMAT).orElse("");
		if (!Constants.CT_FHIR_NDJSON.equals(inputFormat)) {
			throw new InvalidRequestException("Input format must be \"" + Constants.CT_FHIR_NDJSON + "\"");
		}

		Optional<IBase> storageDetailOpt = ParametersUtil.getNamedParameter(myCtx, theRequest, PARAM_STORAGE_DETAIL);
		if (storageDetailOpt.isPresent()) {
			IBase storageDetail = storageDetailOpt.get();

			String httpBasicCredential = ParametersUtil.getParameterPartValueAsString(myCtx, storageDetail, PARAM_STORAGE_DETAIL_CREDENTIAL_HTTP_BASIC);
			if (isNotBlank(httpBasicCredential)) {
				request.addParameter(new JobInstanceParameter(BulkImport2AppCtx.PARAM_HTTP_BASIC_CREDENTIALS, httpBasicCredential));
			}

			String maximumBatchResourceCount = ParametersUtil.getParameterPartValueAsString(myCtx, storageDetail, PARAM_STORAGE_DETAIL_MAX_BATCH_RESOURCE_COUNT);
			if (isNotBlank(maximumBatchResourceCount)) {
				request.addParameter(new JobInstanceParameter(BulkImport2AppCtx.PARAM_MAXIMUM_BATCH_RESOURCE_COUNT, maximumBatchResourceCount));
			}
		}

		// Extract all the URLs and order them in the order that is least
		// likely to result in conflict (e.g. Patients before Observations
		// since Observations can reference Patients but not vice versa)
		List<Pair<String, String>> typeAndUrls = new ArrayList<>();
		for (IBase input : ParametersUtil.getNamedParameters(myCtx, theRequest, BulkImportProvider.PARAM_INPUT)) {
			String type = ParametersUtil.getParameterPartValueAsString(myCtx, input, BulkImportProvider.PARAM_INPUT_TYPE);
			String url = ParametersUtil.getParameterPartValueAsString(myCtx, input, BulkImportProvider.PARAM_INPUT_URL);
			ValidateUtil.isNotBlankOrThrowInvalidRequest(type, "Missing type for input");
			ValidateUtil.isNotBlankOrThrowInvalidRequest(url, "Missing url for input");
			Pair<String, String> typeAndUrl = Pair.of(type, url);
			typeAndUrls.add(typeAndUrl);
		}
		ValidateUtil.isTrueOrThrowInvalidRequest(typeAndUrls.size() > 0, "No URLs specified");
		List<String> resourceTypeOrder = getResourceTypeOrder();
		typeAndUrls.sort(Comparator.comparing(t -> resourceTypeOrder.indexOf(t.getKey())));

		for (Pair<String, String> next : typeAndUrls) {
			request.addParameter(new JobInstanceParameter(BulkImport2AppCtx.PARAM_NDJSON_URL, next.getValue()));
		}


		ourLog.info("Requesting Bulk Import Job ($import by Manifest) with {} urls", typeAndUrls.size());

		String jobId = myJobCoordinator.startInstance(request);


		IBaseOperationOutcome response = OperationOutcomeUtil.newInstance(myCtx);
		OperationOutcomeUtil.addIssue(
			myCtx,
			response,
			"information",
			"Bulk import job has been submitted with ID: " + jobId,
			null,
			"informational"
		);

		theResponse.setStatus(202);
		myCtx.newJsonParser().setPrettyPrint(true).encodeResourceToWriter(response, theResponse.getWriter());
		theResponse.getWriter().close();
	}

	private synchronized List<String> getResourceTypeOrder() {
		List<String> retVal = myResourceTypeOrder;
		if (retVal == null) {
			retVal = ResourceOrderUtil.getResourceOrder(myCtx);
			myResourceTypeOrder = retVal;
		}
		return retVal;
	}


}
