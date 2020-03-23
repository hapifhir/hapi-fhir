package ca.uhn.fhir.rest.server.interceptor;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.fhirpath.FhirPathExecutionException;
import ca.uhn.fhir.fhirpath.IFhirPath;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.ResponseDetails;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.util.ParametersUtil;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * This interceptor looks for a URL parameter on requests called <code>_fhirpath</code> and
 * replaces the resource being returned with a Parameters resource containing the results of
 * the given FHIRPath expression evaluated against the resource that would otherwise
 * have been returned.
 *
 * @see <a href="https://hapifhir.io/hapi-fhir/docs/interceptors/built_in_server_interceptors.html#response-customizing-evaluate-fhirpath">Interceptors - Response Customization: Evaluate FHIRPath</a>
 * @since 5.0.0
 */
public class FhirPathFilterInterceptor {

	@Hook(Pointcut.SERVER_OUTGOING_RESPONSE)
	public void preProcessOutgoingResponse(RequestDetails theRequestDetails, ResponseDetails theResponseDetails) {
		IBaseResource responseResource = theResponseDetails.getResponseResource();
		if (responseResource != null) {
			String[] fhirPathParams = theRequestDetails.getParameters().get(Constants.PARAM_FHIRPATH);
			if (fhirPathParams != null) {

				FhirContext ctx = theRequestDetails.getFhirContext();
				IBaseParameters responseParameters = ParametersUtil.newInstance(ctx);

				for (String expression : fhirPathParams) {
					if (isNotBlank(expression)) {
						IBase resultPart = ParametersUtil.addParameterToParameters(ctx, responseParameters, "result");
						ParametersUtil.addPartString(ctx, resultPart, "expression", expression);

						IFhirPath fhirPath = ctx.newFhirPath();
						List<IBase> outputs;
						try {
							outputs = fhirPath.evaluate(responseResource, expression, IBase.class);
						} catch (FhirPathExecutionException e) {
							throw new InvalidRequestException("Error parsing FHIRPath expression: " + e.getMessage());
						}

						for (IBase nextOutput : outputs) {
							if (nextOutput instanceof IBaseResource) {
								ParametersUtil.addPartResource(ctx, resultPart, "result", (IBaseResource) nextOutput);
							} else {
								ParametersUtil.addPart(ctx, resultPart, "result", nextOutput);
							}
						}
					}
				}

				theResponseDetails.setResponseResource(responseParameters);
			}
		}
	}

}
