package ca.uhn.fhir.jpa.fql.provider;

import ca.uhn.fhir.jpa.fql.executor.IFqlExecutor;
import ca.uhn.fhir.jpa.fql.executor.IFqlResult;
import ca.uhn.fhir.jpa.fql.util.FqlConstants;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.util.ValidateUtil;
import org.apache.commons.csv.CSVPrinter;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStreamWriter;

import static ca.uhn.fhir.jpa.fql.jdbc.FqlRestClient.CSV_FORMAT;
import static ca.uhn.fhir.rest.api.Constants.CHARSET_UTF8_CTSUFFIX;
import static ca.uhn.fhir.rest.api.Constants.CT_TEXT_CSV;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class FqlRestProvider {

	public static final String PARAM_QUERY = "query";
	public static final String PARAM_LIMIT = "limit";

	@Autowired
	private IFqlExecutor myFqlExecutor;

	/**
	 * Constructor
	 */
	public FqlRestProvider() {
		super();
	}

	/**
	 * Constructor
	 */
	public FqlRestProvider(IFqlExecutor theFqlExecutor) {
		myFqlExecutor = theFqlExecutor;
	}

	@Operation(name = FqlConstants.FQL_EXECUTE, manualResponse = true)
	public void executeFql(
		@OperationParam(name = PARAM_QUERY, typeName = "string", min = 1, max = 1) IPrimitiveType<String> theQuery,
		@OperationParam(name = PARAM_LIMIT, typeName = "integer", min = 0, max = 1) IPrimitiveType<Integer> theLimit,
		RequestDetails theRequestDetails,
		HttpServletResponse theServletResponse
	) throws IOException {
		ValidateUtil.isTrueOrThrowInvalidRequest(theQuery != null && isNotBlank(theQuery.getValueAsString()), "Query must be provided");
		String query = theQuery.getValueAsString();
		Integer limit = null;
		if (theLimit != null) {
			limit = theLimit.getValue();
		}
		IFqlResult outcome = myFqlExecutor.execute(query, limit, theRequestDetails);

		theServletResponse.setStatus(200);
		theServletResponse.setContentType(CT_TEXT_CSV + CHARSET_UTF8_CTSUFFIX);
		try (ServletOutputStream outputStream = theServletResponse.getOutputStream()) {
			Appendable out = new OutputStreamWriter(outputStream);
			CSVPrinter csvWriter = new CSVPrinter(out, CSV_FORMAT);
			csvWriter.printRecords();

			// First row is the column names
			csvWriter.printRecord(outcome.getColumnNames());

			int rows = 0;
			while (outcome.hasNext()) {
				csvWriter.printRecord(outcome.getNextRowAsStrings());
				if (rows++ % 1000 == 0) {
					csvWriter.flush();
				}
			}
			csvWriter.close(true);
		}

	}


}
