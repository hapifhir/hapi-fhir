package ca.uhn.fhir.cr.behavior.r4;

import ca.uhn.fhir.cr.behavior.DaoRegistryUser;
import ca.uhn.fhir.cr.common.Searches;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.ContactDetail;
import org.hl7.fhir.r4.model.ContactPoint;
import org.hl7.fhir.r4.model.ContactPoint.ContactPointSystem;
import org.hl7.fhir.r4.model.Enumerations.PublicationStatus;
import org.hl7.fhir.r4.model.Enumerations.SearchParamType;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.MeasureReport;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.model.SearchParameter;
import org.hl7.fhir.r4.model.SearchParameter.XPathUsageType;
import org.opencds.cqf.cql.evaluator.fhir.behavior.IdCreator;
import org.opencds.cqf.cql.evaluator.fhir.util.Ids;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface MeasureReportUser extends DaoRegistryUser, IdCreator {
	Logger ourLog = LoggerFactory.getLogger(ParameterUser.class);

	static final String MEASUREREPORT_IMPROVEMENT_NOTATION_SYSTEM = "http://terminology.hl7.org/CodeSystem/measure-improvement-notation";
	static final String MEASUREREPORT_MEASURE_POPULATION_SYSTEM = "http://terminology.hl7.org/CodeSystem/measure-population";
	static final String MEASUREREPORT_MEASURE_SUPPLEMENTALDATA_EXTENSION = "http://hl7.org/fhir/us/davinci-deqm/StructureDefinition/extension-supplementalData";
	static final String MEASUREREPORT_SUPPLEMENTALDATA_SEARCHPARAMETER_URL = "http://hl7.org/fhir/us/davinci-deqm/SearchParameter/measurereport-supplemental-data";
	static final String MEASUREREPORT_SUPPLEMENTALDATA_SEARCHPARAMETER_VERSION = "0.1.0";

	static final List<ContactDetail> CQI_CONTACTDETAIL = new ArrayList<ContactDetail>() {
		{
			add(
					new ContactDetail()
							.addTelecom(
									new ContactPoint()
											.setSystem(ContactPointSystem.URL)
											.setValue("http://www.hl7.org/Special/committees/cqi/index.cfm")));
		}
	};

	static final List<CodeableConcept> US_JURISDICTION_CODING = new ArrayList<CodeableConcept>() {
		{
			add(
					new CodeableConcept()
							.addCoding(
									new Coding("urn:iso:std:iso:3166", "US", "United States of America")));
		}
	};

	default Map<String, Resource> getEvaluatedResources(MeasureReport report) {
		Map<String, Resource> resources = new HashMap<>();
		getEvaluatedResources(report, resources);

		return resources;
	}

	default MeasureReportUser getEvaluatedResources(MeasureReport report, Map<String, Resource> resources) {
		report.getEvaluatedResource().forEach(evaluatedResource -> {
			IIdType resourceId = evaluatedResource.getReferenceElement();
			if (resourceId.getResourceType() == null || resources.containsKey(Ids.simple(resourceId))) {
				return;
			}
			IBaseResource resourceBase = read(resourceId);
			if (resourceBase instanceof Resource) {
				Resource resource = (Resource) resourceBase;
				resources.put(Ids.simple(resourceId), resource);
			}
		});

		return this;
	}

	default Map<String, Resource> getSDE(MeasureReport report) {
		Map<String, Resource> sdeMap = new HashMap<>();
		getSDE(report, sdeMap);
		return sdeMap;
	}

	default MeasureReportUser getSDE(MeasureReport report, Map<String, Resource> resources) {
		if (report.hasExtension()) {
			for (Extension extension : report.getExtension()) {
				if (extension.hasUrl() && extension.getUrl().equals(MEASUREREPORT_MEASURE_SUPPLEMENTALDATA_EXTENSION)) {
					Reference sdeRef = extension.hasValue() && extension.getValue() instanceof Reference
							? (Reference) extension.getValue()
							: null;
					if (sdeRef != null && sdeRef.hasReference() && !sdeRef.getReference().startsWith("#")) {
						IdType sdeId = new IdType(sdeRef.getReference());
						if (!resources.containsKey(Ids.simple(sdeId))) {
							resources.put(Ids.simple(sdeId), read(sdeId));
						}
					}
				}
			}
		}
		return this;
	}

	default OperationOutcome generateIssue(String severity, String issue) {
		OperationOutcome error = new OperationOutcome();
		error.addIssue()
				.setSeverity(OperationOutcome.IssueSeverity.fromCode(severity))
				.setCode(OperationOutcome.IssueType.PROCESSING)
				.setDetails(new CodeableConcept().setText(issue));
		return error;
	}

	default void ensureSupplementalDataElementSearchParameter(RequestDetails requestDetails) {
		if (!search(SearchParameter.class,
				Searches.byUrlAndVersion(MEASUREREPORT_SUPPLEMENTALDATA_SEARCHPARAMETER_URL,
						MEASUREREPORT_SUPPLEMENTALDATA_SEARCHPARAMETER_VERSION),
				requestDetails).isEmpty())
			return;

		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(2022, 7, 20);

		SearchParameter searchParameter = new SearchParameter()
				.setUrl(MEASUREREPORT_SUPPLEMENTALDATA_SEARCHPARAMETER_URL)
				.setVersion(MEASUREREPORT_SUPPLEMENTALDATA_SEARCHPARAMETER_VERSION)
				.setName("DEQMMeasureReportSupplementalData")
				.setStatus(PublicationStatus.ACTIVE)
				.setDate(calendar.getTime())
				.setPublisher("HL7 International - Clinical Quality Information Work Group")
				.setContact(CQI_CONTACTDETAIL)
				.setDescription(
						String.format(
								"Returns resources (supplemental data) from references on extensions on the MeasureReport with urls matching %s.",
								MEASUREREPORT_MEASURE_SUPPLEMENTALDATA_EXTENSION))
				.setJurisdiction(US_JURISDICTION_CODING)
				.addBase("MeasureReport")
				.setCode("supplemental-data")
				.setType(SearchParamType.REFERENCE)
				.setExpression(
						String.format("MeasureReport.extension('%s').value",
								MEASUREREPORT_MEASURE_SUPPLEMENTALDATA_EXTENSION))
				.setXpath(
						String.format("f:MeasureReport/f:extension[@url='%s'].value",
								MEASUREREPORT_MEASURE_SUPPLEMENTALDATA_EXTENSION))
				.setXpathUsage(XPathUsageType.NORMAL);

		searchParameter.setId("deqm-measurereport-supplemental-data");
		searchParameter.setTitle("Supplemental Data");

		create(searchParameter, requestDetails);
	}
}
