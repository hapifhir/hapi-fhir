
package ca.uhn.fhir.narrative.template;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.antlr.runtime.tree.CommonTree;
import org.hl7.fhir.dstu3.exceptions.FHIRException;
import org.hl7.fhir.dstu3.hapi.validation.DefaultProfileValidationSupport;
import org.hl7.fhir.dstu3.hapi.validation.HapiWorkerContext;
import org.hl7.fhir.dstu3.hapi.validation.IValidationSupport;
import org.hl7.fhir.dstu3.model.Base;
import org.hl7.fhir.dstu3.utils.FluentPathEngine;
import org.hl7.fhir.dstu3.utils.IWorkerContext;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.INarrative;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.narrative.INarrativeGenerator;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.util.FhirTerser;

public class TemplateNarrativeGenerator implements INarrativeGenerator {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(TemplateNarrativeGenerator.class);

	private FhirContext myFhirContext = FhirContext.forDstu3();
	private IContextValidationSupport myValidationSupport = new DefaultProfileValidationSupport();
	
	@Override
	public void generateNarrative(FhirContext theContext, IBaseResource theResource, INarrative theNarrative) {
		// TODO Auto-generated method stub

	}

	public String processLiquid(FhirContext theContext, String theTemplate, IBaseResource theResource) {
		Template template = Template.parse(theTemplate);

		ourLog.info(template.toStringAST());

		IWorkerContext ctx = new HapiWorkerContext(myFhirContext, myValidationSupport);
		FluentPathEngine fluentPathEngine = new FluentPathEngine(ctx);
		
		FhirTerser terser = new FhirTerser(theContext);

		HashMap<String, Object> context = new HashMap<String, Object>();
		context.put("context", theResource);
		context.put("terser", terser);
		context.put("fpEngine", fluentPathEngine);
		
		return template.render(context);
		
	}
	
	class MyContextMap extends HashMap<String, Object> {
		private static final long serialVersionUID = 1L;
		private List<Base> myContext;
		private IBaseResource myResource;

		MyContextMap(IBaseResource theResource) {
			myResource = theResource;
			myContext = null;
		}
		
		MyContextMap(Base theContext, IBaseResource theResource) {
			myContext = Collections.singletonList(theContext);
			myResource=theResource;
		}

		MyContextMap(List<Base> theContext, IBaseResource theResource) {
			myContext = (theContext);
			myResource=theResource;
		}

		@Override
		public Object get(Object theKey) {
			ourLog.info("Requesting key: {}", theKey);
			
			if (theKey.equals("resource")) {
				return new MyContextMap((Base) myResource, myResource);
			}
			
			IWorkerContext ctx = new HapiWorkerContext(myFhirContext, myValidationSupport);
			try {
				FluentPathEngine fluentPathEngine = new FluentPathEngine(ctx);
				List<Base> evaluated = new ArrayList<Base>();
				for (Base nextContext : myContext) {
					evaluated.addAll(fluentPathEngine.evaluate(nextContext, (String)theKey));
				}
				return new MyContextMap(evaluated, myResource);
			} catch (FHIRException e) {
				throw new InternalErrorException("Failed to process expression: " + theKey, e);
			}
			
		}
	}
	
}
