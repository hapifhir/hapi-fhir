/*-
 * #%L
 * HAPI FHIR JPA Server - International Patient Summary (IPS)
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package ca.uhn.fhir.jpa.ips.strategy;

import ca.uhn.fhir.jpa.ips.api.IIpsGenerationStrategy;
import ca.uhn.fhir.jpa.ips.api.IpsContext;
import ca.uhn.fhir.jpa.ips.api.SectionRegistry;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.rest.api.SortOrderEnum;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.param.TokenParam;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static ca.uhn.fhir.jpa.term.api.ITermLoaderSvc.LOINC_URI;

@SuppressWarnings({"EnhancedSwitchMigration", "HttpUrlsUsage"})
public class DefaultIpsGenerationStrategy implements IIpsGenerationStrategy {

	public static final String DEFAULT_IPS_NARRATIVES_PROPERTIES =
			"classpath:ca/uhn/fhir/jpa/ips/narrative/ips-narratives.properties";
	private SectionRegistry mySectionRegistry;

	/**
	 * Constructor
	 */
	public DefaultIpsGenerationStrategy() {
		setSectionRegistry(new SectionRegistry());
	}

	@Override
	public SectionRegistry getSectionRegistry() {
		return mySectionRegistry;
	}

	public void setSectionRegistry(SectionRegistry theSectionRegistry) {
		if (!theSectionRegistry.isInitialized()) {
			theSectionRegistry.initialize();
		}
		mySectionRegistry = theSectionRegistry;
	}

	@Override
	public List<String> getNarrativePropertyFiles() {
		return Lists.newArrayList(DEFAULT_IPS_NARRATIVES_PROPERTIES);
	}

	@Override
	public IBaseResource createAuthor() {
		Organization organization = new Organization();
		organization
				.setName("eHealthLab - University of Cyprus")
				.addAddress(new Address()
						.addLine("1 University Avenue")
						.setCity("Nicosia")
						.setPostalCode("2109")
						.setCountry("CY"))
				.setId(IdType.newRandomUuid());
		return organization;
	}

	@Override
	public String createTitle(IpsContext theContext) {
		return "Patient Summary as of "
				+ DateTimeFormatter.ofPattern("MM/dd/yyyy").format(LocalDate.now());
	}

	@Override
	public String createConfidentiality(IpsContext theIpsContext) {
		return Composition.DocumentConfidentiality.N.toCode();
	}

	@Override
	public IIdType massageResourceId(@Nullable IpsContext theIpsContext, @Nonnull IBaseResource theResource) {
		return IdType.newRandomUuid();
	}

	@Override
	public void massageResourceSearch(
			IpsContext.IpsSectionContext theIpsSectionContext, SearchParameterMap theSearchParameterMap) {
		switch (theIpsSectionContext.getSection()) {
			case ALLERGY_INTOLERANCE:
			case PROBLEM_LIST:
			case PROCEDURES:
			case MEDICAL_DEVICES:
			case ILLNESS_HISTORY:
			case FUNCTIONAL_STATUS:
				return;
			case IMMUNIZATIONS:
				theSearchParameterMap.setSort(new SortSpec(Immunization.SP_DATE).setOrder(SortOrderEnum.DESC));
				return;
			case VITAL_SIGNS:
				if (theIpsSectionContext.getResourceType().equals(ResourceType.Observation.name())) {
					theSearchParameterMap.add(
							Observation.SP_CATEGORY,
							new TokenOrListParam()
									.addOr(new TokenParam(
											"http://terminology.hl7.org/CodeSystem/observation-category",
											"vital-signs")));
					return;
				}
				break;
			case SOCIAL_HISTORY:
				if (theIpsSectionContext.getResourceType().equals(ResourceType.Observation.name())) {
					theSearchParameterMap.add(
							Observation.SP_CATEGORY,
							new TokenOrListParam()
									.addOr(new TokenParam(
											"http://terminology.hl7.org/CodeSystem/observation-category",
											"social-history")));
					return;
				}
				break;
			case DIAGNOSTIC_RESULTS:
				if (theIpsSectionContext.getResourceType().equals(ResourceType.DiagnosticReport.name())) {
					return;
				} else if (theIpsSectionContext.getResourceType().equals(ResourceType.Observation.name())) {
					theSearchParameterMap.add(
							Observation.SP_CATEGORY,
							new TokenOrListParam()
									.addOr(new TokenParam(
											"http://terminology.hl7.org/CodeSystem/observation-category",
											"laboratory")));
					return;
				}
				break;
			case PREGNANCY:
				if (theIpsSectionContext.getResourceType().equals(ResourceType.Observation.name())) {
					theSearchParameterMap.add(
							Observation.SP_CODE,
							new TokenOrListParam()
									.addOr(new TokenParam(LOINC_URI, "82810-3"))
									.addOr(new TokenParam(LOINC_URI, "11636-8"))
									.addOr(new TokenParam(LOINC_URI, "11637-6"))
									.addOr(new TokenParam(LOINC_URI, "11638-4"))
									.addOr(new TokenParam(LOINC_URI, "11639-2"))
									.addOr(new TokenParam(LOINC_URI, "11640-0"))
									.addOr(new TokenParam(LOINC_URI, "11612-9"))
									.addOr(new TokenParam(LOINC_URI, "11613-7"))
									.addOr(new TokenParam(LOINC_URI, "11614-5"))
									.addOr(new TokenParam(LOINC_URI, "33065-4")));
					return;
				}
				break;
			case MEDICATION_SUMMARY:
				if (theIpsSectionContext.getResourceType().equals(ResourceType.MedicationStatement.name())) {
					theSearchParameterMap.add(
							MedicationStatement.SP_STATUS,
							new TokenOrListParam()
									.addOr(new TokenParam(
											MedicationStatement.MedicationStatementStatus.ACTIVE.getSystem(),
											MedicationStatement.MedicationStatementStatus.ACTIVE.toCode()))
									.addOr(new TokenParam(
											MedicationStatement.MedicationStatementStatus.INTENDED.getSystem(),
											MedicationStatement.MedicationStatementStatus.INTENDED.toCode()))
									.addOr(new TokenParam(
											MedicationStatement.MedicationStatementStatus.UNKNOWN.getSystem(),
											MedicationStatement.MedicationStatementStatus.UNKNOWN.toCode()))
									.addOr(new TokenParam(
											MedicationStatement.MedicationStatementStatus.ONHOLD.getSystem(),
											MedicationStatement.MedicationStatementStatus.ONHOLD.toCode())));
					return;
				} else if (theIpsSectionContext.getResourceType().equals(ResourceType.MedicationRequest.name())) {
					theSearchParameterMap.add(
							MedicationRequest.SP_STATUS,
							new TokenOrListParam()
									.addOr(new TokenParam(
											MedicationRequest.MedicationRequestStatus.ACTIVE.getSystem(),
											MedicationRequest.MedicationRequestStatus.ACTIVE.toCode()))
									.addOr(new TokenParam(
											MedicationRequest.MedicationRequestStatus.UNKNOWN.getSystem(),
											MedicationRequest.MedicationRequestStatus.UNKNOWN.toCode()))
									.addOr(new TokenParam(
											MedicationRequest.MedicationRequestStatus.ONHOLD.getSystem(),
											MedicationRequest.MedicationRequestStatus.ONHOLD.toCode())));
					return;
				} else if (theIpsSectionContext
						.getResourceType()
						.equals(ResourceType.MedicationAdministration.name())) {
					theSearchParameterMap.add(
							MedicationAdministration.SP_STATUS,
							new TokenOrListParam()
									.addOr(new TokenParam(
											MedicationAdministration.MedicationAdministrationStatus.INPROGRESS
													.getSystem(),
											MedicationAdministration.MedicationAdministrationStatus.INPROGRESS
													.toCode()))
									.addOr(new TokenParam(
											MedicationAdministration.MedicationAdministrationStatus.UNKNOWN.getSystem(),
											MedicationAdministration.MedicationAdministrationStatus.UNKNOWN.toCode()))
									.addOr(new TokenParam(
											MedicationAdministration.MedicationAdministrationStatus.ONHOLD.getSystem(),
											MedicationAdministration.MedicationAdministrationStatus.ONHOLD.toCode())));
					return;
				} else if (theIpsSectionContext.getResourceType().equals(ResourceType.MedicationDispense.name())) {
					theSearchParameterMap.add(
							MedicationDispense.SP_STATUS,
							new TokenOrListParam()
									.addOr(new TokenParam(
											MedicationDispense.MedicationDispenseStatus.INPROGRESS.getSystem(),
											MedicationDispense.MedicationDispenseStatus.INPROGRESS.toCode()))
									.addOr(new TokenParam(
											MedicationDispense.MedicationDispenseStatus.UNKNOWN.getSystem(),
											MedicationDispense.MedicationDispenseStatus.UNKNOWN.toCode()))
									.addOr(new TokenParam(
											MedicationDispense.MedicationDispenseStatus.ONHOLD.getSystem(),
											MedicationDispense.MedicationDispenseStatus.ONHOLD.toCode())));
					return;
				}
				break;
			case PLAN_OF_CARE:
				if (theIpsSectionContext.getResourceType().equals(ResourceType.CarePlan.name())) {
					theSearchParameterMap.add(
							CarePlan.SP_STATUS,
							new TokenOrListParam()
									.addOr(new TokenParam(
											CarePlan.CarePlanStatus.ACTIVE.getSystem(),
											CarePlan.CarePlanStatus.ACTIVE.toCode()))
									.addOr(new TokenParam(
											CarePlan.CarePlanStatus.ONHOLD.getSystem(),
											CarePlan.CarePlanStatus.ONHOLD.toCode()))
									.addOr(new TokenParam(
											CarePlan.CarePlanStatus.UNKNOWN.getSystem(),
											CarePlan.CarePlanStatus.UNKNOWN.toCode())));
					return;
				}
				break;
			case ADVANCE_DIRECTIVES:
				if (theIpsSectionContext.getResourceType().equals(ResourceType.Consent.name())) {
					theSearchParameterMap.add(
							Consent.SP_STATUS,
							new TokenOrListParam()
									.addOr(new TokenParam(
											Consent.ConsentState.ACTIVE.getSystem(),
											Consent.ConsentState.ACTIVE.toCode())));
					return;
				}
				break;
		}

		// Shouldn't happen: This means none of the above switches handled the Section+resourceType combination
		assert false
				: "Don't know how to handle " + theIpsSectionContext.getSection() + "/"
						+ theIpsSectionContext.getResourceType();
	}

	@Nonnull
	@Override
	public Set<Include> provideResourceSearchIncludes(IpsContext.IpsSectionContext theIpsSectionContext) {
		switch (theIpsSectionContext.getSection()) {
			case MEDICATION_SUMMARY:
				if (ResourceType.MedicationStatement.name().equals(theIpsSectionContext.getResourceType())) {
					return Sets.newHashSet(MedicationStatement.INCLUDE_MEDICATION);
				}
				if (ResourceType.MedicationRequest.name().equals(theIpsSectionContext.getResourceType())) {
					return Sets.newHashSet(MedicationRequest.INCLUDE_MEDICATION);
				}
				if (ResourceType.MedicationAdministration.name().equals(theIpsSectionContext.getResourceType())) {
					return Sets.newHashSet(MedicationAdministration.INCLUDE_MEDICATION);
				}
				if (ResourceType.MedicationDispense.name().equals(theIpsSectionContext.getResourceType())) {
					return Sets.newHashSet(MedicationDispense.INCLUDE_MEDICATION);
				}
				break;
			case MEDICAL_DEVICES:
				if (ResourceType.DeviceUseStatement.name().equals(theIpsSectionContext.getResourceType())) {
					return Sets.newHashSet(DeviceUseStatement.INCLUDE_DEVICE);
				}
				break;
			case IMMUNIZATIONS:
				if (ResourceType.Immunization.name().equals(theIpsSectionContext.getResourceType())) {
					return Sets.newHashSet(Immunization.INCLUDE_MANUFACTURER);
				}
				break;
			case ALLERGY_INTOLERANCE:
			case PROBLEM_LIST:
			case PROCEDURES:
			case DIAGNOSTIC_RESULTS:
			case VITAL_SIGNS:
			case ILLNESS_HISTORY:
			case PREGNANCY:
			case SOCIAL_HISTORY:
			case FUNCTIONAL_STATUS:
			case PLAN_OF_CARE:
			case ADVANCE_DIRECTIVES:
				break;
		}
		return Collections.emptySet();
	}

	@SuppressWarnings("EnhancedSwitchMigration")
	@Override
	public boolean shouldInclude(IpsContext.IpsSectionContext theIpsSectionContext, IBaseResource theCandidate) {

		switch (theIpsSectionContext.getSection()) {
			case MEDICATION_SUMMARY:
			case PLAN_OF_CARE:
			case ADVANCE_DIRECTIVES:
				return true;
			case ALLERGY_INTOLERANCE:
				if (theIpsSectionContext.getResourceType().equals(ResourceType.AllergyIntolerance.name())) {
					AllergyIntolerance allergyIntolerance = (AllergyIntolerance) theCandidate;
					return !allergyIntolerance
									.getClinicalStatus()
									.hasCoding(
											"http://terminology.hl7.org/CodeSystem/allergyintolerance-clinical",
											"inactive")
							&& !allergyIntolerance
									.getClinicalStatus()
									.hasCoding(
											"http://terminology.hl7.org/CodeSystem/allergyintolerance-clinical",
											"resolved")
							&& !allergyIntolerance
									.getVerificationStatus()
									.hasCoding(
											"http://terminology.hl7.org/CodeSystem/allergyintolerance-verification",
											"entered-in-error");
				}
				break;
			case PROBLEM_LIST:
				if (theIpsSectionContext.getResourceType().equals(ResourceType.Condition.name())) {
					Condition prob = (Condition) theCandidate;
					return !prob.getClinicalStatus()
									.hasCoding("http://terminology.hl7.org/CodeSystem/condition-clinical", "inactive")
							&& !prob.getClinicalStatus()
									.hasCoding("http://terminology.hl7.org/CodeSystem/condition-clinical", "resolved")
							&& !prob.getVerificationStatus()
									.hasCoding(
											"http://terminology.hl7.org/CodeSystem/condition-ver-status",
											"entered-in-error");
				}
				break;
			case IMMUNIZATIONS:
				if (theIpsSectionContext.getResourceType().equals(ResourceType.Immunization.name())) {
					Immunization immunization = (Immunization) theCandidate;
					return immunization.getStatus() != Immunization.ImmunizationStatus.ENTEREDINERROR;
				}
				break;
			case PROCEDURES:
				if (theIpsSectionContext.getResourceType().equals(ResourceType.Procedure.name())) {
					Procedure proc = (Procedure) theCandidate;
					return proc.getStatus() != Procedure.ProcedureStatus.ENTEREDINERROR
							&& proc.getStatus() != Procedure.ProcedureStatus.NOTDONE;
				}
				break;
			case MEDICAL_DEVICES:
				if (theIpsSectionContext.getResourceType().equals(ResourceType.DeviceUseStatement.name())) {
					DeviceUseStatement deviceUseStatement = (DeviceUseStatement) theCandidate;
					return deviceUseStatement.getStatus() != DeviceUseStatement.DeviceUseStatementStatus.ENTEREDINERROR;
				}
				return true;
			case DIAGNOSTIC_RESULTS:
				if (theIpsSectionContext.getResourceType().equals(ResourceType.DiagnosticReport.name())) {
					return true;
				}
				if (theIpsSectionContext.getResourceType().equals(ResourceType.Observation.name())) {
					// code filtering not yet applied
					Observation observation = (Observation) theCandidate;
					return (observation.getStatus() != Observation.ObservationStatus.PRELIMINARY);
				}
				break;
			case VITAL_SIGNS:
				if (theIpsSectionContext.getResourceType().equals(ResourceType.Observation.name())) {
					// code filtering not yet applied
					Observation observation = (Observation) theCandidate;
					return (observation.getStatus() != Observation.ObservationStatus.PRELIMINARY);
				}
				break;
			case ILLNESS_HISTORY:
				if (theIpsSectionContext.getResourceType().equals(ResourceType.Condition.name())) {
					Condition prob = (Condition) theCandidate;
					if (prob.getVerificationStatus()
							.hasCoding(
									"http://terminology.hl7.org/CodeSystem/condition-ver-status", "entered-in-error")) {
						return false;
					} else {
						return prob.getClinicalStatus()
										.hasCoding(
												"http://terminology.hl7.org/CodeSystem/condition-clinical", "inactive")
								|| prob.getClinicalStatus()
										.hasCoding(
												"http://terminology.hl7.org/CodeSystem/condition-clinical", "resolved")
								|| prob.getClinicalStatus()
										.hasCoding(
												"http://terminology.hl7.org/CodeSystem/condition-clinical",
												"remission");
					}
				}
				break;
			case PREGNANCY:
				if (theIpsSectionContext.getResourceType().equals(ResourceType.Observation.name())) {
					// code filtering not yet applied
					Observation observation = (Observation) theCandidate;
					return (observation.getStatus() != Observation.ObservationStatus.PRELIMINARY);
				}
				break;
			case SOCIAL_HISTORY:
				if (theIpsSectionContext.getResourceType().equals(ResourceType.Observation.name())) {
					// code filtering not yet applied
					Observation observation = (Observation) theCandidate;
					return (observation.getStatus() != Observation.ObservationStatus.PRELIMINARY);
				}
				break;
			case FUNCTIONAL_STATUS:
				if (theIpsSectionContext.getResourceType().equals(ResourceType.ClinicalImpression.name())) {
					ClinicalImpression clinicalImpression = (ClinicalImpression) theCandidate;
					return clinicalImpression.getStatus() != ClinicalImpression.ClinicalImpressionStatus.INPROGRESS
							&& clinicalImpression.getStatus()
									!= ClinicalImpression.ClinicalImpressionStatus.ENTEREDINERROR;
				}
				break;
		}

		return true;
	}
}
