package ca.uhn.fhir.jpa.provider.validation.performance;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.util.StopWatch;
import ca.uhn.hapi.converters.canonical.VersionCanonicalizer;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseCoding;
import org.hl7.fhir.instance.model.api.IBaseConformance;
import org.hl7.fhir.instance.model.api.IBaseDatatype;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.AuditEvent;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.ConceptMap;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r5.model.CapabilityStatement;
import org.hl7.fhir.r5.model.CodeSystem;
import org.hl7.fhir.r5.model.Resource;
import org.hl7.fhir.r5.model.SearchParameter;
import org.hl7.fhir.r5.model.StructureDefinition;
import org.hl7.fhir.r5.model.SubscriptionTopic;
import org.hl7.fhir.r5.model.ValueSet;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class MetricCapturingVersionCanonicalizer extends VersionCanonicalizer {

	private Map<String, ConverterMetric> myMetrics = new LinkedHashMap<>();

	public MetricCapturingVersionCanonicalizer(FhirContext theTargetContext) {
		super(theTargetContext);
	}

	@Override
	public SubscriptionTopic subscriptionTopicToCanonical(IBaseResource theResource) {
		return addMetric("subscriptionTopicToCanonical", theResource, () ->
			super.subscriptionTopicToCanonical(theResource)
		);
	}

	@Override
	public IBaseResource auditEventFromCanonical(AuditEvent theResource) {
		return addMetric("auditEventFromCanonical", theResource, () ->
			super.auditEventFromCanonical(theResource)
		);
	}

	@Override
	public CodeSystem codeSystemToValidatorCanonical(IBaseResource theResource) {
		return addMetric("codeSystemToValidatorCanonical", theResource, () ->
			super.codeSystemToValidatorCanonical(theResource)
		);
	}

	@Override
	public ValueSet valueSetToValidatorCanonical(IBaseResource theResource) {
		return addMetric("valueSetToValidatorCanonical", theResource, () ->
			super.valueSetToValidatorCanonical(theResource)
		);
	}

	@Override
	public Resource resourceToValidatorCanonical(IBaseResource theResource) {
		return addMetric("resourceToValidatorCanonical", theResource, () ->
			super.resourceToValidatorCanonical(theResource)
		);
	}

	@Override
	public IBaseResource valueSetFromValidatorCanonical(ValueSet theResource) {
		return addMetric("valueSetFromValidatorCanonical", theResource, () ->
			super.valueSetFromValidatorCanonical(theResource)
		);
	}

	@Override
	public IBaseResource structureDefinitionFromCanonical(StructureDefinition theResource) {
		return addMetric("structureDefinitionFromCanonical", theResource, () ->
			super.structureDefinitionFromCanonical(theResource)
		);
	}

	@Override
	public StructureDefinition structureDefinitionToCanonical(IBaseResource theResource) {
		return addMetric("structureDefinitionToCanonical", theResource, () ->
			super.structureDefinitionToCanonical(theResource)
		);
	}

	@Override
	public IBaseParameters parametersFromCanonical(Parameters theParameters) {
		return addMetric("parametersFromCanonical", theParameters, () ->
			super.parametersFromCanonical(theParameters)
		);
	}

	@Override
	public IBaseResource searchParameterFromCanonical(SearchParameter theSearchParameter) {
		return addMetric("searchParameterFromCanonical", theSearchParameter, () ->
			super.searchParameterFromCanonical(theSearchParameter)
		);
	}

	@Override
	public <T extends IBaseResource> SearchParameter searchParameterToCanonical(T theSearchParameter) {
		return addMetric("searchParameterToCanonical", theSearchParameter, () ->
			super.searchParameterToCanonical(theSearchParameter)
		);
	}

	@Override
	public ConceptMap conceptMapToCanonical(IBaseResource theConceptMap) {
		return addMetric("conceptMapToCanonical", theConceptMap, () ->
			super.conceptMapToCanonical(theConceptMap)
		);
	}

	@Override
	public IBaseResource codeSystemFromCanonical(org.hl7.fhir.r4.model.CodeSystem theCodeSystem) {
		return addMetric("codeSystemFromCanonical", theCodeSystem, () ->
			super.codeSystemFromCanonical(theCodeSystem)
		);
	}

	@Override
	public IBaseResource valueSetFromCanonical(org.hl7.fhir.r4.model.ValueSet theValueSet) {
		return addMetric("valueSetFromCanonical", theValueSet, () ->
			super.valueSetFromCanonical(theValueSet)
		);
	}

	@Override
	public org.hl7.fhir.r4.model.CodeSystem codeSystemToCanonical(IBaseResource theCodeSystem) {
		return addMetric("codeSystemToCanonical", theCodeSystem, () ->
			super.codeSystemToCanonical(theCodeSystem)
		);
	}

	@Override
	public org.hl7.fhir.r4.model.ValueSet valueSetToCanonical(IBaseResource theValueSet) {
		return addMetric("valueSetToCanonical", theValueSet, () ->
			super.valueSetToCanonical(theValueSet)
		);
	}

	@Override
	public Coding codingToCanonical(IBaseCoding theCoding) {
		return addMetric("codingToCanonical", theCoding, () ->
			super.codingToCanonical(theCoding)
		);
	}

	@Override
	public CodeableConcept codeableConceptToCanonical(IBaseDatatype theCodeableConcept) {
		return addMetric("codeableConceptToCanonical", theCodeableConcept, () ->
			super.codeableConceptToCanonical(theCodeableConcept)
		);
	}

	@Override
	public IBaseConformance capabilityStatementFromCanonical(CapabilityStatement theCapabilityStatement) {
		return addMetric("capabilityStatementFromCanonical", theCapabilityStatement, () ->
			super.capabilityStatementFromCanonical(theCapabilityStatement)
		);
	}

	@Override
	public CapabilityStatement capabilityStatementToCanonical(IBaseResource theCapabilityStatement) {
		return addMetric("capabilityStatementToCanonical", theCapabilityStatement, () ->
			super.capabilityStatementToCanonical(theCapabilityStatement)
		);
	}

	public List<ConverterMetric> getMetrics() {
		return new ArrayList<>(myMetrics.values());
	}

	public void resetMetrics() {
		myMetrics = new LinkedHashMap<>();
	}

	private <T> T addMetric(String theMethodName, IBaseResource theResource, Supplier<T> theSupplier) {
		return addMetric(theMethodName, resourceString(theResource), theSupplier);
	}

	private <T> T addMetric(String theMethodName, IBase theIBase, Supplier<T> theSupplier) {
		return addMetric(theMethodName, theIBase.fhirType(), theSupplier);
	}

	private <T> T addMetric(String theMethodName, String theId, Supplier<T> theSupplier) {
		ConverterMetric metric = myMetrics.computeIfAbsent(theMethodName, ConverterMetric::new);
		StopWatch sw = new StopWatch();

		T result = theSupplier.get();

		int threadOffSet = 4; // don't include addMetric methods or delegate methods in stacktrace
		int maxThreadCount = 10;
		metric.addInvocation(theId, sw.getMillis(), formatStackTrace(Thread.currentThread().getStackTrace()), threadOffSet, maxThreadCount);

		return result;
	}

	private String resourceString(IBaseResource theResource) {
		return theResource == null ? "NULL" : theResource.getClass().getSimpleName() + "/" + theResource.getIdElement().getIdPart();
	}

	private List<String> formatStackTrace(StackTraceElement[] stackTrace) {

		List<String> retVal = new ArrayList<>();
		for (StackTraceElement element : stackTrace) {
			String className = element.getClassName();
			String method = element.getMethodName();
			int lineNumber = element.getLineNumber();
			retVal.add(className + "#" + method + "() [line:" + lineNumber + "]");
		}
		return retVal;
	}
}
