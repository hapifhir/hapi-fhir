package ca.uhn.fhir.narrative.template.nodes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.hl7.fhir.dstu3.exceptions.FHIRException;
import org.hl7.fhir.dstu3.model.Base;
import org.hl7.fhir.dstu3.model.BooleanType;
import org.hl7.fhir.dstu3.utils.FluentPathEngine;
import org.hl7.fhir.instance.model.api.IBaseBooleanDatatype;
import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.util.FhirTerser;

class LookupNode implements LNode {

	private final String id;

	public LookupNode(String id) {
		this.id = id;
	}

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(LookupNode.class);

	@Override
	public Object render(Map<String, Object> context) {

		ourLog.info("Processing path: {}", id);

		FhirTerser terser = (FhirTerser) context.get("terser");
		FluentPathEngine fpEngine = (FluentPathEngine) context.get("fpEngine");
		IBaseResource resContext = (IBaseResource) context.get("context");
		
		List<Base> retVal;
		try {
			retVal = fpEngine.evaluate((Base)resContext, id);
		} catch (FHIRException e) {
			throw new InternalErrorException(e);
		} 
		// terser.getValues(resContext, id);

		ourLog.info("Evaluated to: {}", retVal);
		
		if (retVal.size() == 1) {
			if (IBaseBooleanDatatype.class.isAssignableFrom(retVal.get(0).getClass())) {
				IBaseBooleanDatatype bool = (IBaseBooleanDatatype) retVal.get(0);
				Boolean newRetVal = bool.getValue();
				ourLog.info("Returning: {}", newRetVal);
				return newRetVal;
			}
		}
		
		
		return retVal;
	}

	interface Indexable {
		Object get(Object value, Map<String, Object> context);
	}

	@Override
	public String toString() {

		StringBuilder builder = new StringBuilder();

		builder.append(id);

		return builder.toString();
	}
}
