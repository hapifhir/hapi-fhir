/*-
 * #%L
 * HAPI FHIR JPA Server Test Utilities
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.test;

import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.api.IJobMaintenanceService;
import ca.uhn.fhir.batch2.api.IJobStepExecutionServices;
import ca.uhn.fhir.batch2.jobs.export.BulkDataExportProvider;
import ca.uhn.fhir.batch2.jobs.merge.MergeAppCtx;
import ca.uhn.fhir.batch2.jobs.replacereferences.ReplaceReferencesAppCtx;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoCodeSystem;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoConceptMap;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoPatient;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoStructureDefinition;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoSubscription;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoValueSet;
import ca.uhn.fhir.jpa.api.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.api.svc.IDeleteExpungeSvc;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.api.svc.ISearchCoordinatorSvc;
import ca.uhn.fhir.jpa.binary.interceptor.BinaryStorageInterceptor;
import ca.uhn.fhir.jpa.binary.provider.BinaryAccessProvider;
import ca.uhn.fhir.jpa.bulk.export.api.IBulkDataExportJobSchedulingHelper;
import ca.uhn.fhir.jpa.dao.GZipUtil;
import ca.uhn.fhir.jpa.dao.IFulltextSearchSvc;
import ca.uhn.fhir.jpa.dao.TestDaoSearch;
import ca.uhn.fhir.jpa.dao.data.IBatch2JobInstanceRepository;
import ca.uhn.fhir.jpa.dao.data.IBatch2WorkChunkRepository;
import ca.uhn.fhir.jpa.dao.data.IMdmLinkJpaRepository;
import ca.uhn.fhir.jpa.dao.data.IPartitionDao;
import ca.uhn.fhir.jpa.dao.data.IResourceHistoryProvenanceDao;
import ca.uhn.fhir.jpa.dao.data.IResourceHistoryTableDao;
import ca.uhn.fhir.jpa.dao.data.IResourceHistoryTagDao;
import ca.uhn.fhir.jpa.dao.data.IResourceIndexedComboTokensNonUniqueDao;
import ca.uhn.fhir.jpa.dao.data.IResourceIndexedSearchParamCoordsDao;
import ca.uhn.fhir.jpa.dao.data.IResourceIndexedSearchParamDateDao;
import ca.uhn.fhir.jpa.dao.data.IResourceIndexedSearchParamQuantityDao;
import ca.uhn.fhir.jpa.dao.data.IResourceIndexedSearchParamQuantityNormalizedDao;
import ca.uhn.fhir.jpa.dao.data.IResourceIndexedSearchParamStringDao;
import ca.uhn.fhir.jpa.dao.data.IResourceReindexJobDao;
import ca.uhn.fhir.jpa.dao.data.IResourceSearchUrlDao;
import ca.uhn.fhir.jpa.dao.data.IResourceTableDao;
import ca.uhn.fhir.jpa.dao.data.IResourceTagDao;
import ca.uhn.fhir.jpa.dao.data.ISearchDao;
import ca.uhn.fhir.jpa.dao.data.ISearchIncludeDao;
import ca.uhn.fhir.jpa.dao.data.ISearchParamPresentDao;
import ca.uhn.fhir.jpa.dao.data.ISearchResultDao;
import ca.uhn.fhir.jpa.dao.data.ITagDefinitionDao;
import ca.uhn.fhir.jpa.dao.data.ITermCodeSystemDao;
import ca.uhn.fhir.jpa.dao.data.ITermConceptDao;
import ca.uhn.fhir.jpa.dao.data.ITermConceptDesignationDao;
import ca.uhn.fhir.jpa.dao.data.ITermConceptMapDao;
import ca.uhn.fhir.jpa.dao.data.ITermConceptMapGroupElementTargetDao;
import ca.uhn.fhir.jpa.dao.data.ITermConceptParentChildLinkDao;
import ca.uhn.fhir.jpa.dao.data.ITermConceptPropertyDao;
import ca.uhn.fhir.jpa.dao.data.ITermValueSetConceptDao;
import ca.uhn.fhir.jpa.dao.data.ITermValueSetConceptDesignationDao;
import ca.uhn.fhir.jpa.dao.data.ITermValueSetDao;
import ca.uhn.fhir.jpa.entity.TermCodeSystem;
import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.entity.TermValueSet;
import ca.uhn.fhir.jpa.entity.TermValueSetConcept;
import ca.uhn.fhir.jpa.interceptor.PerformanceTracingLoggingInterceptor;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.model.dao.JpaPidFk;
import ca.uhn.fhir.jpa.model.entity.ResourceHistoryTable;
import ca.uhn.fhir.jpa.packages.IPackageInstallerSvc;
import ca.uhn.fhir.jpa.partition.IPartitionLookupSvc;
import ca.uhn.fhir.jpa.provider.JpaSystemProvider;
import ca.uhn.fhir.jpa.rp.r4.PatientResourceProvider;
import ca.uhn.fhir.jpa.search.DatabaseBackedPagingProvider;
import ca.uhn.fhir.jpa.search.IStaleSearchDeletingSvc;
import ca.uhn.fhir.jpa.search.reindex.IResourceReindexingSvc;
import ca.uhn.fhir.jpa.search.warm.ICacheWarmingSvc;
import ca.uhn.fhir.jpa.searchparam.registry.SearchParamRegistryImpl;
import ca.uhn.fhir.jpa.term.TermDeferredStorageSvcImpl;
import ca.uhn.fhir.jpa.term.api.ITermCodeSystemStorageSvc;
import ca.uhn.fhir.jpa.term.api.ITermConceptMappingSvc;
import ca.uhn.fhir.jpa.term.api.ITermDeferredStorageSvc;
import ca.uhn.fhir.jpa.term.api.ITermLoaderSvc;
import ca.uhn.fhir.jpa.term.api.ITermReadSvc;
import ca.uhn.fhir.jpa.test.config.TestR4Config;
import ca.uhn.fhir.jpa.util.MemoryCacheService;
import ca.uhn.fhir.jpa.util.ResourceCountCache;
import ca.uhn.fhir.jpa.validation.ValidationSettings;
import ca.uhn.fhir.mdm.interceptor.MdmStorageInterceptor;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.parser.StrictErrorHandler;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.server.BasePagingProvider;
import ca.uhn.fhir.rest.server.provider.ResourceProviderFactory;
import ca.uhn.fhir.test.utilities.ITestDataBuilder;
import ca.uhn.fhir.util.UrlUtil;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.ValidationResult;
import jakarta.persistence.EntityManager;

import java.util.Objects;

import org.hl7.fhir.common.hapi.validation.validator.FhirInstanceValidator;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.AllergyIntolerance;
import org.hl7.fhir.r4.model.Appointment;
import org.hl7.fhir.r4.model.AuditEvent;
import org.hl7.fhir.r4.model.Basic;
import org.hl7.fhir.r4.model.Binary;
import org.hl7.fhir.r4.model.BodyStructure;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CapabilityStatement;
import org.hl7.fhir.r4.model.CarePlan;
import org.hl7.fhir.r4.model.CareTeam;
import org.hl7.fhir.r4.model.ChargeItem;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.Communication;
import org.hl7.fhir.r4.model.CommunicationRequest;
import org.hl7.fhir.r4.model.CompartmentDefinition;
import org.hl7.fhir.r4.model.Composition;
import org.hl7.fhir.r4.model.ConceptMap;
import org.hl7.fhir.r4.model.ConceptMap.ConceptMapGroupComponent;
import org.hl7.fhir.r4.model.ConceptMap.SourceElementComponent;
import org.hl7.fhir.r4.model.ConceptMap.TargetElementComponent;
import org.hl7.fhir.r4.model.Condition;
import org.hl7.fhir.r4.model.Consent;
import org.hl7.fhir.r4.model.Coverage;
import org.hl7.fhir.r4.model.Device;
import org.hl7.fhir.r4.model.DiagnosticReport;
import org.hl7.fhir.r4.model.DocumentReference;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Enumerations.ConceptMapEquivalence;
import org.hl7.fhir.r4.model.EpisodeOfCare;
import org.hl7.fhir.r4.model.ExplanationOfBenefit;
import org.hl7.fhir.r4.model.Group;
import org.hl7.fhir.r4.model.Immunization;
import org.hl7.fhir.r4.model.ImmunizationRecommendation;
import org.hl7.fhir.r4.model.Library;
import org.hl7.fhir.r4.model.ListResource;
import org.hl7.fhir.r4.model.Location;
import org.hl7.fhir.r4.model.Media;
import org.hl7.fhir.r4.model.Medication;
import org.hl7.fhir.r4.model.MedicationAdministration;
import org.hl7.fhir.r4.model.MedicationRequest;
import org.hl7.fhir.r4.model.MessageHeader;
import org.hl7.fhir.r4.model.Meta;
import org.hl7.fhir.r4.model.MolecularSequence;
import org.hl7.fhir.r4.model.NamingSystem;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.OperationDefinition;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.OrganizationAffiliation;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.PractitionerRole;
import org.hl7.fhir.r4.model.Procedure;
import org.hl7.fhir.r4.model.Provenance;
import org.hl7.fhir.r4.model.Questionnaire;
import org.hl7.fhir.r4.model.QuestionnaireResponse;
import org.hl7.fhir.r4.model.RiskAssessment;
import org.hl7.fhir.r4.model.SearchParameter;
import org.hl7.fhir.r4.model.ServiceRequest;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.hl7.fhir.r4.model.Subscription;
import org.hl7.fhir.r4.model.Substance;
import org.hl7.fhir.r4.model.Task;
import org.hl7.fhir.r4.model.UriType;
import org.hl7.fhir.r4.model.ValueSet;
import org.hl7.fhir.r5.elementmodel.Element;
import org.hl7.fhir.r5.model.ElementDefinition;
import org.hl7.fhir.r5.utils.validation.IMessagingServices;
import org.hl7.fhir.r5.utils.validation.IResourceValidator;
import org.hl7.fhir.r5.utils.validation.IValidationPolicyAdvisor;
import org.hl7.fhir.r5.utils.validation.constants.BestPracticeWarningLevel;
import org.hl7.fhir.r5.utils.validation.constants.BindingKind;
import org.hl7.fhir.r5.utils.validation.constants.ContainedReferenceValidationPolicy;
import org.hl7.fhir.r5.utils.validation.constants.ReferenceValidationPolicy;
import org.hl7.fhir.utilities.validation.ValidationMessage;
import org.hl7.fhir.validation.instance.advisor.BasePolicyAdvisorForFullValidation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.slf4j.event.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.AopTestUtils;
import org.springframework.transaction.PlatformTransactionManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.fail;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
	TestR4Config.class,
	ReplaceReferencesAppCtx.class,  // Batch job
	MergeAppCtx.class // Batch job
})
public abstract class BaseJpaR4Test extends BaseJpaTest implements ITestDataBuilder {
	public static final String MY_VALUE_SET = "my-value-set";
	public static final String URL_MY_VALUE_SET = "http://example.com/my_value_set";
	public static final String URL_MY_CODE_SYSTEM = "http://example.com/my_code_system";
	public static final String URL_MY_CODE_SYSTEM_2 = "http://example.com/my_code_system_2";

	@Autowired
	protected IJobStepExecutionServices myJobStepExecutionServices;
	@Autowired
	protected IPackageInstallerSvc myPackageInstallerSvc;
	@Autowired
	protected ITermConceptMappingSvc myConceptMappingSvc;
	@Autowired
	protected IPartitionLookupSvc myPartitionConfigSvc;
	@Autowired
	protected IPartitionDao myPartitionDao;
	@Autowired
	protected ITermReadSvc myHapiTerminologySvc;
	@Autowired
	protected ITermCodeSystemStorageSvc myTermCodeSystemStorageSvc;
	@Autowired
	protected ISearchDao mySearchEntityDao;
	@Autowired
	protected IBatch2JobInstanceRepository myJobInstanceRepository;
	@Autowired
	protected IBatch2WorkChunkRepository myWorkChunkRepository;

	@Autowired
	protected ISearchIncludeDao mySearchIncludeEntityDao;
	@Autowired
	protected ISearchResultDao mySearchResultDao;
	@Autowired
	@Qualifier("myResourceCountsCache")
	protected ResourceCountCache myResourceCountsCache;
	@Autowired
	protected ISearchParamPresentDao mySearchParamPresentDao;
	@Autowired
	protected IResourceIndexedSearchParamStringDao myResourceIndexedSearchParamStringDao;
	@Autowired
	protected IResourceIndexedSearchParamCoordsDao myResourceIndexedSearchParamCoordsDao;
	@Autowired
	protected IResourceIndexedSearchParamQuantityDao myResourceIndexedSearchParamQuantityDao;
	@Autowired
	protected IResourceIndexedSearchParamQuantityNormalizedDao myResourceIndexedSearchParamQuantityNormalizedDao;
	@Autowired
	protected IResourceIndexedSearchParamDateDao myResourceIndexedSearchParamDateDao;
	@Autowired
	protected IResourceIndexedComboTokensNonUniqueDao myResourceIndexedComboTokensNonUniqueDao;
	@Autowired
	@Qualifier("myAllergyIntoleranceDaoR4")
	protected IFhirResourceDao<AllergyIntolerance> myAllergyIntoleranceDao;
	@Autowired
	protected BinaryAccessProvider myBinaryAccessProvider;
	@Autowired
	protected BulkDataExportProvider myBulkDataExportProvider;
	@Autowired
	protected BinaryStorageInterceptor myBinaryStorageInterceptor;
	@Autowired
	protected ApplicationContext myAppCtx;
	@Autowired
	@Qualifier("myAppointmentDaoR4")
	protected IFhirResourceDao<Appointment> myAppointmentDao;
	@Autowired
	@Qualifier("myAuditEventDaoR4")
	protected IFhirResourceDao<AuditEvent> myAuditEventDao;
	@Autowired
	@Qualifier("myBundleDaoR4")
	protected IFhirResourceDao<Bundle> myBundleDao;
	@Autowired
	@Qualifier("myCommunicationDaoR4")
	protected IFhirResourceDao<Communication> myCommunicationDao;
	@Autowired
	@Qualifier("myCommunicationRequestDaoR4")
	protected IFhirResourceDao<CommunicationRequest> myCommunicationRequestDao;
	@Autowired
	@Qualifier("myCarePlanDaoR4")
	protected IFhirResourceDao<CarePlan> myCarePlanDao;
	@Autowired
	@Qualifier("myCareTeamDaoR4")
	protected IFhirResourceDao<CareTeam> myCareTeamDao;
	@Autowired
	@Qualifier("myCodeSystemDaoR4")
	protected IFhirResourceDaoCodeSystem<CodeSystem> myCodeSystemDao;
	@Autowired
	protected ITermCodeSystemDao myTermCodeSystemDao;
	@Autowired
	protected ITermConceptParentChildLinkDao myTermConceptParentChildLinkDao;
	@Autowired
	@Qualifier("myCompartmentDefinitionDaoR4")
	protected IFhirResourceDao<CompartmentDefinition> myCompartmentDefinitionDao;
	@Autowired
	@Qualifier("myConceptMapDaoR4")
	protected IFhirResourceDaoConceptMap<ConceptMap> myConceptMapDao;
	@Autowired
	protected ITermConceptDao myTermConceptDao;
	@Autowired
	protected ITermConceptDesignationDao myTermConceptDesignationDao;
	@Autowired
	protected ITermConceptPropertyDao myTermConceptPropertyDao;
	@Autowired
	@Qualifier("myConditionDaoR4")
	protected IFhirResourceDao<Condition> myConditionDao;
	@Autowired
	@Qualifier("myEpisodeOfCareDaoR4")
	protected IFhirResourceDao<EpisodeOfCare> myEpisodeOfCareDao;
	@Autowired
	protected PartitionSettings myPartitionSettings;
	@Autowired
	@Qualifier("myDeviceDaoR4")
	protected IFhirResourceDao<Device> myDeviceDao;
	@Autowired
	@Qualifier("myProvenanceDaoR4")
	protected IFhirResourceDao<Provenance> myProvenanceDao;
	@Autowired
	@Qualifier("myDiagnosticReportDaoR4")
	protected IFhirResourceDao<DiagnosticReport> myDiagnosticReportDao;
	@Autowired
	@Qualifier("myEncounterDaoR4")
	protected IFhirResourceDao<Encounter> myEncounterDao;
	// @PersistenceContext()
	@Autowired
	protected EntityManager myEntityManager;
	@Autowired
	@Qualifier("myGroupDaoR4")
	protected IFhirResourceDao<Group> myGroupDao;
	@Autowired
	@Qualifier("myListDaoR4")
	protected IFhirResourceDao<ListResource> myListDao;
	@Autowired
	@Qualifier("myMolecularSequenceDaoR4")
	protected IFhirResourceDao<MolecularSequence> myMolecularSequenceDao;
	@Autowired
	@Qualifier("myImmunizationDaoR4")
	protected IFhirResourceDao<Immunization> myImmunizationDao;
	@Autowired
	@Qualifier("myImmunizationRecommendationDaoR4")
	protected IFhirResourceDao<ImmunizationRecommendation> myImmunizationRecommendationDao;
	@Autowired
	@Qualifier("myRiskAssessmentDaoR4")
	protected IFhirResourceDao<RiskAssessment> myRiskAssessmentDao;
	@Autowired
	protected IInterceptorService myInterceptorRegistry;
	@Autowired
	@Qualifier("myLocationDaoR4")
	protected IFhirResourceDao<Location> myLocationDao;
	@Autowired
	@Qualifier("myPractitionerRoleDaoR4")
	protected IFhirResourceDao<PractitionerRole> myPractitionerRoleDao;
	@Autowired
	@Qualifier("myMediaDaoR4")
	protected IFhirResourceDao<Media> myMediaDao;
	@Autowired
	@Qualifier("myMedicationAdministrationDaoR4")
	protected IFhirResourceDao<MedicationAdministration> myMedicationAdministrationDao;
	@Autowired
	@Qualifier("myMedicationDaoR4")
	protected IFhirResourceDao<Medication> myMedicationDao;
	@Autowired
	@Qualifier("myMedicationRequestDaoR4")
	protected IFhirResourceDao<MedicationRequest> myMedicationRequestDao;
	@Autowired
	@Qualifier("myProcedureDaoR4")
	protected IFhirResourceDao<Procedure> myProcedureDao;
	@Autowired
	@Qualifier("myNamingSystemDaoR4")
	protected IFhirResourceDao<NamingSystem> myNamingSystemDao;
	@Autowired
	@Qualifier("myChargeItemDaoR4")
	protected IFhirResourceDao<ChargeItem> myChargeItemDao;
	@Autowired
	@Qualifier("myObservationDaoR4")
	protected IFhirResourceDao<Observation> myObservationDao;
	@Autowired
	@Qualifier("myCompositionDaoR4")
	protected IFhirResourceDao<Composition> myCompositionDao;
	@Autowired
	@Qualifier("myOperationDefinitionDaoR4")
	protected IFhirResourceDao<OperationDefinition> myOperationDefinitionDao;
	@Autowired
	@Qualifier("myOrganizationDaoR4")
	protected IFhirResourceDao<Organization> myOrganizationDao;
	@Autowired
	@Qualifier("myOrganizationAffiliationDaoR4")
	protected IFhirResourceDao<OrganizationAffiliation> myOrganizationAffiliationDao;

	@Autowired
	protected DatabaseBackedPagingProvider myPagingProvider;
	@Autowired
	@Qualifier("myBinaryDaoR4")
	protected IFhirResourceDao<Binary> myBinaryDao;
	@Autowired
	@Qualifier("myBodyStructureDaoR4")
	protected IFhirResourceDao<BodyStructure> myBodyStructureDao;
	@Autowired
	@Qualifier("myDocumentReferenceDaoR4")
	protected IFhirResourceDao<DocumentReference> myDocumentReferenceDao;
	@Autowired
	@Qualifier("myCapabilityStatementDaoR4")
	protected IFhirResourceDao<CapabilityStatement> myCapabilityStatementDao;
	@Autowired
	@Qualifier("myPatientDaoR4")
	protected IFhirResourceDaoPatient<Patient> myPatientDao;
	@Autowired
	@Qualifier("myExplanationOfBenefitDaoR4")
	protected IFhirResourceDao<ExplanationOfBenefit> myExplanationOfBenefitDao;
	@Autowired
	@Qualifier("myMessageHeaderDaoR4")
	protected IFhirResourceDao<MessageHeader> myMessageHeaderDao;
	@Autowired
	protected IResourceTableDao myResourceTableDao;
	@Autowired
	protected IResourceHistoryTableDao myResourceHistoryTableDao;
	@Autowired
	protected IResourceHistoryProvenanceDao myResourceHistoryProvenanceDao;
	@Autowired
	@Qualifier("myCoverageDaoR4")
	protected IFhirResourceDao<Coverage> myCoverageDao;
	@Autowired
	@Qualifier("myPractitionerDaoR4")
	protected IFhirResourceDao<Practitioner> myPractitionerDao;
	@Autowired
	@Qualifier("myServiceRequestDaoR4")
	protected IFhirResourceDao<ServiceRequest> myServiceRequestDao;
	@Autowired
	@Qualifier("myQuestionnaireDaoR4")
	protected IFhirResourceDao<Questionnaire> myQuestionnaireDao;
	@Autowired
	@Qualifier("myQuestionnaireResponseDaoR4")
	protected IFhirResourceDao<QuestionnaireResponse> myQuestionnaireResponseDao;
	@Autowired
	@Qualifier("myResourceProvidersR4")
	protected ResourceProviderFactory myResourceProviders;
	@Autowired
	protected IResourceTagDao myResourceTagDao;
	@Autowired
	protected IResourceHistoryTagDao myResourceHistoryTagDao;
	@Autowired
	protected ISearchCoordinatorSvc mySearchCoordinatorSvc;
	@Autowired(required = false)
	protected IFulltextSearchSvc mySearchDao;
	@Autowired
	protected IResourceReindexJobDao myResourceReindexJobDao;
	@Autowired
	@Qualifier("mySearchParameterDaoR4")
	protected IFhirResourceDao<SearchParameter> mySearchParameterDao;
	@Autowired
	protected SearchParamRegistryImpl mySearchParamRegistry;
	@Autowired
	protected IStaleSearchDeletingSvc myStaleSearchDeletingSvc;
	@Autowired
	@Qualifier("myStructureDefinitionDaoR4")
	protected IFhirResourceDaoStructureDefinition<StructureDefinition> myStructureDefinitionDao;
	@Autowired
	@Qualifier("myConsentDaoR4")
	protected IFhirResourceDao<Consent> myConsentDao;
	@Autowired
	@Qualifier("mySubscriptionDaoR4")
	protected IFhirResourceDaoSubscription<Subscription> mySubscriptionDao;
	@Autowired
	@Qualifier("myBasicDaoR4")
	protected IFhirResourceDao<Basic> myBasicDao;
	@Autowired
	@Qualifier("mySubstanceDaoR4")
	protected IFhirResourceDao<Substance> mySubstanceDao;
	@Autowired
	@Qualifier("mySystemDaoR4")
	protected IFhirSystemDao<Bundle, Meta> mySystemDao;
	@Autowired
	protected IResourceReindexingSvc myResourceReindexingSvc;
	@Autowired
	@Qualifier("mySystemProviderR4")
	protected JpaSystemProvider<Bundle, Meta> mySystemProvider;
	@Autowired
	protected ITagDefinitionDao myTagDefinitionDao;
	@Autowired
	@Qualifier("myTaskDaoR4")
	protected IFhirResourceDao<Task> myTaskDao;
	@Autowired
	protected ITermReadSvc myTermSvc;
	@Autowired
	protected ITermDeferredStorageSvc myTerminologyDeferredStorageSvc;
	@Autowired
	protected ITermLoaderSvc myTerminologyLoaderSvc;
	@Autowired
	protected PlatformTransactionManager myTransactionMgr;
	@Autowired
	protected PlatformTransactionManager myTxManager;
	@Autowired
	protected IValidationSupport myValidationSupport;
	@Autowired
	@Qualifier("myValueSetDaoR4")
	protected IFhirResourceDaoValueSet<ValueSet> myValueSetDao;
	@Autowired
	protected IFhirResourceDao<Library> myLibraryDao;
	@Autowired
	protected ITermValueSetDao myTermValueSetDao;
	@Autowired
	protected ITermValueSetConceptDao myTermValueSetConceptDao;
	@Autowired
	protected ITermValueSetConceptDesignationDao myTermValueSetConceptDesignationDao;
	@Autowired
	protected ITermConceptMapDao myTermConceptMapDao;
	@Autowired
	protected ITermConceptMapGroupElementTargetDao myTermConceptMapGroupElementTargetDao;
	@Autowired
	protected MemoryCacheService myMemoryCacheService;
	@Autowired
	protected ICacheWarmingSvc myCacheWarmingSvc;
	@Autowired
	protected DaoRegistry myDaoRegistry;
	@Autowired
	protected IIdHelperService<JpaPid> myIdHelperService;
	@Autowired
	protected ValidationSettings myValidationSettings;
	@Autowired
	protected IMdmLinkJpaRepository myMdmLinkRepository;
	@Autowired
	protected IMdmLinkJpaRepository myMdmLinkHistoryDao;
	@Autowired
	private IValidationSupport myJpaValidationSupportChainR4;
	private PerformanceTracingLoggingInterceptor myPerformanceTracingLoggingInterceptor;
	@Autowired
	private IBulkDataExportJobSchedulingHelper myBulkDataScheduleHelper;
	@Autowired
	protected IResourceSearchUrlDao myResourceSearchUrlDao;
	@Autowired
	private IInterceptorService myInterceptorService;
	@Autowired(required = false)
	private MdmStorageInterceptor myMdmStorageInterceptor;
	@Autowired
	protected TestDaoSearch myTestDaoSearch;
	@Autowired
	protected IDeleteExpungeSvc<JpaPid> myDeleteExpungeSvc;

	@Autowired
	protected IJobMaintenanceService myJobMaintenanceService;
	@Autowired
	protected IJobCoordinator myJobCoordinator;

	private IValidationPolicyAdvisor policyAdvisor;
	@RegisterExtension
	private final PreventDanglingInterceptorsExtension myPreventDanglingInterceptorsExtension = new PreventDanglingInterceptorsExtension(()-> myInterceptorRegistry);

	@AfterEach()
	@Order(0)
	public void afterCleanupDao() {
		// make sure there are no running jobs
		assertFalse(myBatch2JobHelper.hasRunningJobs());

		myStorageSettings.setExpireSearchResults(new JpaStorageSettings().isExpireSearchResults());
		myStorageSettings.setEnforceReferentialIntegrityOnDelete(new JpaStorageSettings().isEnforceReferentialIntegrityOnDelete());
		myStorageSettings.setExpireSearchResultsAfterMillis(new JpaStorageSettings().getExpireSearchResultsAfterMillis());
		myStorageSettings.setReuseCachedSearchResultsForMillis(new JpaStorageSettings().getReuseCachedSearchResultsForMillis());
		myStorageSettings.setSuppressUpdatesWithNoChange(new JpaStorageSettings().isSuppressUpdatesWithNoChange());
		myStorageSettings.setAllowContainsSearches(new JpaStorageSettings().isAllowContainsSearches());
		myStorageSettings.setAutoCreatePlaceholderReferenceTargets(new JpaStorageSettings().isAutoCreatePlaceholderReferenceTargets());
		myStorageSettings.setTagStorageMode(new JpaStorageSettings().getTagStorageMode());
		myStorageSettings.setInlineResourceTextBelowSize(new JpaStorageSettings().getInlineResourceTextBelowSize());
		myStorageSettings.setDeleteEnabled(new JpaStorageSettings().isDeleteEnabled());
		myStorageSettings.setMatchUrlCacheEnabled(new JpaStorageSettings().isMatchUrlCacheEnabled());
		myStorageSettings.setStoreMetaSourceInformation(new JpaStorageSettings().getStoreMetaSourceInformation());
		myStorageSettings.setUpdateWithHistoryRewriteEnabled(new JpaStorageSettings().isUpdateWithHistoryRewriteEnabled());

		myPagingProvider.setDefaultPageSize(BasePagingProvider.DEFAULT_DEFAULT_PAGE_SIZE);
		myPagingProvider.setMaximumPageSize(BasePagingProvider.DEFAULT_MAX_PAGE_SIZE);

		myPartitionSettings.setPartitioningEnabled(false);
		ourLog.info("1 - " + getClass().getSimpleName() + ".afterCleanupDao");
	}

	@Override
	@Order(Integer.MAX_VALUE)
	@AfterEach
	public void afterResetInterceptors() {
		super.afterResetInterceptors();
		myInterceptorRegistry.unregisterInterceptor(myPerformanceTracingLoggingInterceptor);

		ourLog.info("2 - " + getClass().getSimpleName() + ".afterResetInterceptors");
	}

	@AfterEach
	public void afterClearTerminologyCaches() {
		TermDeferredStorageSvcImpl termDeferredStorageSvc = AopTestUtils.getTargetObject(myTerminologyDeferredStorageSvc);
		termDeferredStorageSvc.clearDeferred();

		ourLog.info("4 - " + getClass().getSimpleName() + ".afterClearTerminologyCaches");
	}

	@BeforeEach
	public void beforeCreateInterceptor() {
		myPerformanceTracingLoggingInterceptor = new PerformanceTracingLoggingInterceptor(Level.DEBUG);
		myInterceptorRegistry.registerInterceptor(myPerformanceTracingLoggingInterceptor);
	}

	@BeforeEach
	public void beforeUnregisterAllSubscriptions() {
		mySubscriptionRegistry.unregisterAllSubscriptions();
	}

	@BeforeEach
	public void beforeFlushFT() {
		purgeHibernateSearch(this.myEntityManager);

		myStorageSettings.setSchedulingDisabled(true);
		myStorageSettings.setIndexMissingFields(new JpaStorageSettings().getIndexMissingFields());
	}

	@AfterEach
	public void afterPurgeDatabase() {
		/*
		 * We have to stop all scheduled jobs or they will
		 * interfere with the database cleanup!
		 */
		ourLog.info("Pausing Schedulers");
		mySchedulerService.pause();

		myTerminologyDeferredStorageSvc.logQueueForUnitTest();
		if (!myTermDeferredStorageSvc.isStorageQueueEmpty(true)) {
			ourLog.warn("There is deferred terminology storage stuff still in the queue. Please verify your tests clean up ok.");
			if (myTermDeferredStorageSvc instanceof TermDeferredStorageSvcImpl t) {
				t.clearDeferred();
			}
		}

		boolean registeredStorageInterceptor = false;
		if (myMdmStorageInterceptor != null && !myInterceptorService.getAllRegisteredInterceptors().contains(myMdmStorageInterceptor)) {
			myInterceptorService.registerInterceptor(myMdmStorageInterceptor);
			registeredStorageInterceptor = true;
		}
		try {
			runInTransaction(() -> {
				myMdmLinkHistoryDao.deleteAll();
				myMdmLinkDao.deleteAll();
			});
			purgeDatabase(myStorageSettings, mySystemDao, myResourceReindexingSvc, mySearchCoordinatorSvc, mySearchParamRegistry, myBulkDataScheduleHelper);

			myBatch2JobHelper.cancelAllJobsAndAwaitCancellation();
			runInTransaction(() -> {
				myWorkChunkRepository.deleteAll();
				myJobInstanceRepository.deleteAll();
			});
		} finally {
			if (registeredStorageInterceptor) {
				myInterceptorService.unregisterInterceptor(myMdmStorageInterceptor);
			}
		}

		// restart the jobs
		ourLog.info("Restarting the schedulers");
		mySchedulerService.unpause();
		ourLog.info("5 - " + getClass().getSimpleName() + ".afterPurgeDatabases");
	}

	@BeforeEach
	public void beforeResetConfig() {
		myFhirContext.setParserErrorHandler(new StrictErrorHandler());
		myValidationSettings.setLocalReferenceValidationDefaultPolicy(new ValidationSettings().getLocalReferenceValidationDefaultPolicy());
	}

	@Override
	public IIdType doCreateResource(IBaseResource theResource) {
		return doCreateResourceAndReturnOutcome(theResource).getId().toUnqualifiedVersionless();
	}

	public DaoMethodOutcome doCreateResourceAndReturnOutcome(IBaseResource theResource) {
		IFhirResourceDao dao = myDaoRegistry.getResourceDao(theResource.getClass());
		return dao.create(theResource, mySrd);
	}

	@Override
	public IIdType doUpdateResource(IBaseResource theResource) {
		return doUpdateResourceAndReturnOutcome(theResource).getId().toUnqualifiedVersionless();
	}

	public DaoMethodOutcome doUpdateResourceAndReturnOutcome(IBaseResource theResource) {
		IFhirResourceDao dao = myDaoRegistry.getResourceDao(theResource.getClass());
		return dao.update(theResource, mySrd);
	}

	@Override
	public void doDeleteResource(IIdType theIIdType){
		IFhirResourceDao dao = myDaoRegistry.getResourceDao(theIIdType.getResourceType());
		dao.delete(theIIdType);
	}

	protected String encode(IBaseResource theResource) {
		return myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(theResource);
	}

	@Override
	public FhirContext getFhirContext() {
		return myFhirContext;
	}

	@Override
	protected PlatformTransactionManager getTxManager() {
		return myTxManager;
	}

	protected void relocateResourceTextToCompressedColumn(JpaPidFk theResourcePid, Long theVersion) {
		runInTransaction(()->{
			ResourceHistoryTable historyEntity = myResourceHistoryTableDao.findForIdAndVersion(theResourcePid, theVersion);
			byte[] contents = GZipUtil.compress(historyEntity.getResourceTextVc());
			myResourceHistoryTableDao.updateNonInlinedContents(contents, historyEntity.getId());
		});
	}

	protected ValidationResult validateWithResult(IBaseResource theResource) {
		FhirValidator validatorModule = myFhirContext.newValidator();
		FhirInstanceValidator instanceValidator = new FhirInstanceValidator(myValidationSupport);
		instanceValidator.setBestPracticeWarningLevel(BestPracticeWarningLevel.Ignore);
		instanceValidator.setValidatorPolicyAdvisor(new ValidationPolicyAdvisor());
		validatorModule.registerValidatorModule(instanceValidator);
		return validatorModule.validateWithResult(theResource);
	}
	protected void validate(IBaseResource theResource) {
		ValidationResult result = validateWithResult(theResource);
		if (!result.isSuccessful()) {
			fail(myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(result.toOperationOutcome()));
		}
	}

	@SuppressWarnings("unchecked")
	protected void upload(String theClasspath) throws IOException {
		String resource = loadResource(theClasspath);
		IParser parser = EncodingEnum.detectEncoding(resource).newParser(myFhirContext);
		IBaseResource resourceParsed = parser.parseResource(resource);
		IFhirResourceDao dao = myDaoRegistry.getResourceDao(resourceParsed.getIdElement().getResourceType());
		dao.update(resourceParsed);
	}

	protected void assertHierarchyContainsExactly(String... theStrings) {
		List<String> hierarchy = runInTransaction(() -> {
			List<String> hierarchyHolder = new ArrayList<>();
			TermCodeSystem codeSystem = myTermCodeSystemDao.findAll().iterator().next();
			TermCodeSystemVersion csv = codeSystem.getCurrentVersion();
			List<TermConcept> codes = myTermConceptDao.findByCodeSystemVersion(csv);
			List<TermConcept> rootCodes = codes.stream().filter(t -> t.getParents().isEmpty()).collect(Collectors.toList());
			flattenExpansionHierarchy(hierarchyHolder, rootCodes, "");
			return hierarchyHolder;
		});
		if (theStrings.length == 0) {
			assertThat(hierarchy).as("\n" + String.join("\n", hierarchy)).isEmpty();
		} else {
			assertThat(hierarchy).as("\n" + String.join("\n", hierarchy)).containsExactly(theStrings);
		}
	}

	protected ValueSet.ConceptReferenceDesignationComponent assertConceptContainsDesignation(ValueSet.ValueSetExpansionContainsComponent theConcept, String theLanguage, String theUseSystem, String theUseCode, String theUseDisplay, String theDesignationValue) {
		Stream<ValueSet.ConceptReferenceDesignationComponent> stream = theConcept.getDesignation().stream();
		if (theLanguage != null) {
			stream = stream.filter(designation -> theLanguage.equalsIgnoreCase(designation.getLanguage()));
		}
		if (theUseSystem != null) {
			stream = stream.filter(designation -> theUseSystem.equalsIgnoreCase(designation.getUse().getSystem()));
		}
		if (theUseCode != null) {
			stream = stream.filter(designation -> theUseCode.equalsIgnoreCase(designation.getUse().getCode()));
		}
		if (theUseDisplay != null) {
			stream = stream.filter(designation -> theUseDisplay.equalsIgnoreCase(designation.getUse().getDisplay()));
		}
		if (theDesignationValue != null) {
			stream = stream.filter(designation -> theDesignationValue.equalsIgnoreCase(designation.getValue()));
		}

		Optional<ValueSet.ConceptReferenceDesignationComponent> first = stream.findFirst();
		if (!first.isPresent()) {
			String failureMessage = String.format("Concept %s did not contain designation [%s|%s|%s|%s|%s] ", theConcept, theLanguage, theUseSystem, theUseCode, theUseDisplay, theDesignationValue);
			fail(failureMessage);
			return null;
		} else {
			return first.get();
		}
	}

	protected ValueSet.ValueSetExpansionContainsComponent assertExpandedValueSetContainsConcept(ValueSet theValueSet, String theSystem, String theCode, String theDisplay, Integer theDesignationCount) {
		List<ValueSet.ValueSetExpansionContainsComponent> contains = theValueSet.getExpansion().getContains();

		Stream<ValueSet.ValueSetExpansionContainsComponent> stream = contains.stream();
		if (theSystem != null) {
			stream = stream.filter(concept -> theSystem.equalsIgnoreCase(concept.getSystem()));
		}
		if (theCode != null) {
			stream = stream.filter(concept -> theCode.equalsIgnoreCase(concept.getCode()));
		}
		if (theDisplay != null) {
			stream = stream.filter(concept -> theDisplay.equalsIgnoreCase(concept.getDisplay()));
		}
		if (theDesignationCount != null) {
			stream = stream.filter(concept -> concept.getDesignation().size() == theDesignationCount);
		}

		Optional<ValueSet.ValueSetExpansionContainsComponent> first = stream.findFirst();
		if (!first.isPresent()) {
			String expandedValueSetString = myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(theValueSet);
			String failureMessage = String.format("Expanded ValueSet %s did not contain concept [%s|%s|%s] with [%d] designations. Outcome:\n%s", theValueSet.getId(), theSystem, theCode, theDisplay, theDesignationCount, expandedValueSetString);
			fail(failureMessage);
			return null;
		} else {
			return first.get();
		}
	}

	public List<String> getExpandedConceptsByValueSetUrl(String theValuesetUrl) {
		return runInTransaction(() -> {
			List<TermValueSet> valueSets = myTermValueSetDao.findTermValueSetByUrl(Pageable.unpaged(), theValuesetUrl);
			assertEquals(1, valueSets.size());
			TermValueSet valueSet = valueSets.get(0);
			List<TermValueSetConcept> concepts = valueSet.getConcepts();
			return concepts.stream().map(concept -> concept.getCode()).collect(Collectors.toList());
		});
	}

	protected void createLocalCsAndVs() {
		//@formatter:off
		CodeSystem codeSystem = new CodeSystem();
		codeSystem.setUrl(URL_MY_CODE_SYSTEM);
		codeSystem.setContent(CodeSystem.CodeSystemContentMode.COMPLETE);
		codeSystem
			.addConcept().setCode("A").setDisplay("Code A").addDesignation(
				new CodeSystem.ConceptDefinitionDesignationComponent().setLanguage("en").setValue("CodeADesignation")).addProperty(
				new CodeSystem.ConceptPropertyComponent().setCode("CodeAProperty").setValue(new StringType("CodeAPropertyValue"))
			)
			.addConcept(new CodeSystem.ConceptDefinitionComponent().setCode("AA").setDisplay("Code AA")
				.addConcept(new CodeSystem.ConceptDefinitionComponent().setCode("AAA").setDisplay("Code AAA"))
			)
			.addConcept(new CodeSystem.ConceptDefinitionComponent().setCode("AB").setDisplay("Code AB"));
		codeSystem
			.addConcept().setCode("B").setDisplay("Code B")
			.addConcept(new CodeSystem.ConceptDefinitionComponent().setCode("BA").setDisplay("Code BA"))
			.addConcept(new CodeSystem.ConceptDefinitionComponent().setCode("BB").setDisplay("Code BB"));
		//@formatter:on
		myCodeSystemDao.create(codeSystem, mySrd);

		createLocalVs(codeSystem);
	}

	protected void createLocalVs(CodeSystem codeSystem) {
		ValueSet valueSet = new ValueSet();
		valueSet.setId(MY_VALUE_SET);
		valueSet.setUrl(URL_MY_VALUE_SET);
		valueSet.getCompose().addInclude().setSystem(codeSystem.getUrl());
		myValueSetDao.update(valueSet, mySrd);
	}

	@AfterEach
	public void afterEachClearCaches() {
		myJpaValidationSupportChainR4.invalidateCaches();
		ourLog.info("3 - " + getClass().getSimpleName() + ".afterEachClearCaches");
	}

	private static void flattenExpansionHierarchy(List<String> theFlattenedHierarchy, List<TermConcept> theCodes, String thePrefix) {
		theCodes.sort((o1, o2) -> {
			int s1 = o1.getSequence() != null ? o1.getSequence() : o1.getCode().hashCode();
			int s2 = o2.getSequence() != null ? o2.getSequence() : o2.getCode().hashCode();
			return s1 - s2;
		});

		for (TermConcept nextCode : theCodes) {
			String hierarchyEntry = thePrefix + nextCode.getCode() + " seq=" + nextCode.getSequence();
			theFlattenedHierarchy.add(hierarchyEntry);

			List<TermConcept> children = nextCode.getChildCodes();
			flattenExpansionHierarchy(theFlattenedHierarchy, children, thePrefix + " ");

		}
	}

	/**
	 * Creates a single {@link ConceptMap} entity that includes:
	 * <br>
	 * <ul>
	 * <li>
	 * One group with two elements, each identifying one target apiece.
	 * </li>
	 * <li>
	 * One group with one element, identifying two targets.
	 * </li>
	 * <li>
	 * One group with one element, identifying a target that also appears
	 * in the first element of the first group.
	 * </li>
	 * </ul>
	 * </br>
	 * The first two groups identify the same source code system and different target code systems.
	 * </br>
	 * The first two groups also include an element with the same source code.
	 *
	 * @return A {@link ConceptMap} entity for testing.
	 */
	public static ConceptMap createConceptMap() {
		ConceptMap conceptMap = new ConceptMap();
		conceptMap.setUrl(CM_URL);

		conceptMap.setSource(new UriType(VS_URL));
		conceptMap.setTarget(new UriType(VS_URL_2));

		ConceptMapGroupComponent group = conceptMap.addGroup();
		group.setSource(CS_URL);
		group.setSourceVersion("Version 1");
		group.setTarget(CS_URL_2);
		group.setTargetVersion("Version 2");

		SourceElementComponent element = group.addElement();
		element.setCode("12345");
		element.setDisplay("Source Code 12345");

		TargetElementComponent target = element.addTarget();
		target.setCode("34567");
		target.setDisplay("Target Code 34567");
		target.setEquivalence(ConceptMapEquivalence.EQUAL);

		element = group.addElement();
		element.setCode("23456");
		element.setDisplay("Source Code 23456");

		target = element.addTarget();
		target.setCode("45678");
		target.setDisplay("Target Code 45678");
		target.setEquivalence(ConceptMapEquivalence.WIDER);

		// Add a duplicate
		target = element.addTarget();
		target.setCode("45678");
		target.setDisplay("Target Code 45678");
		target.setEquivalence(ConceptMapEquivalence.WIDER);

		group = conceptMap.addGroup();
		group.setSource(CS_URL);
		group.setSourceVersion("Version 3");
		group.setTarget(CS_URL_3);
		group.setTargetVersion("Version 4");

		element = group.addElement();
		element.setCode("12345");
		element.setDisplay("Source Code 12345");

		target = element.addTarget();
		target.setCode("56789");
		target.setDisplay("Target Code 56789");
		target.setEquivalence(ConceptMapEquivalence.EQUAL);

		target = element.addTarget();
		target.setCode("67890");
		target.setDisplay("Target Code 67890");
		target.setEquivalence(ConceptMapEquivalence.WIDER);

		group = conceptMap.addGroup();
		group.setSource(CS_URL_4);
		group.setSourceVersion("Version 5");
		group.setTarget(CS_URL_2);
		group.setTargetVersion("Version 2");

		element = group.addElement();
		element.setCode("78901");
		element.setDisplay("Source Code 78901");

		target = element.addTarget();
		target.setCode("34567");
		target.setDisplay("Target Code 34567");
		target.setEquivalence(ConceptMapEquivalence.NARROWER);

		group = conceptMap.addGroup();
		group.setSource(CS_URL_4);
		group.setSourceVersion("Version 1");
		group.setTarget(CS_URL_3);
		group.setTargetVersion("Version 1");

		// This one should not show up in the results because it doesn't have a code
		element = group.addElement();
		element.setCode("89012");
		element.setDisplay("Source Code 89012");

		target = element.addTarget();
		target.setEquivalence(ConceptMapEquivalence.DISJOINT);

		// This one should show up in the results because unmatched targets are allowed to be codeless
		element = group.addElement();
		element.setCode("89123");
		element.setDisplay("Source Code 89123");

		target = element.addTarget();
		target.setEquivalence(ConceptMapEquivalence.UNMATCHED);

		return conceptMap;
	}

	public static String toSearchUuidFromLinkNext(Bundle theBundle) {
		String linkNext = theBundle.getLink("next").getUrl();
		linkNext = linkNext.substring(linkNext.indexOf('?'));
		Map<String, String[]> params = UrlUtil.parseQueryString(linkNext);
		String[] uuidParams = params.get(Constants.PARAM_PAGINGACTION);
		String uuid = uuidParams[0];
		return uuid;
	}

	public void assertHasErrors(OperationOutcome theOperationOutcome) {
		R4ValidationTestUtil.assertHasErrors(theOperationOutcome);
	}

	public void assertHasWarnings(OperationOutcome theOperationOutcome) {
		R4ValidationTestUtil.assertHasWarnings(theOperationOutcome);
	}

	public void assertHasNoErrors(OperationOutcome theOperationOutcome) {
		R4ValidationTestUtil.assertHasNoErrors(theOperationOutcome);
	}

	public class ValidationPolicyAdvisor implements IValidationPolicyAdvisor {
		@Override
		public EnumSet<ResourceValidationAction> policyForResource(IResourceValidator validator, Object appContext,
																   org.hl7.fhir.r5.model.StructureDefinition type, String path) {
			return EnumSet.allOf(ResourceValidationAction.class);
		}

		@Override
		public EnumSet<ElementValidationAction> policyForElement(IResourceValidator validator, Object appContext,
																 org.hl7.fhir.r5.model.StructureDefinition structure, ElementDefinition element, String path) {
			return EnumSet.allOf(ElementValidationAction.class);
		}
		@Override
		public EnumSet<CodedContentValidationAction> policyForCodedContent(IResourceValidator validator,
																		   Object appContext,
																		   String stackPath,
																		   ElementDefinition definition,
																		   org.hl7.fhir.r5.model.StructureDefinition structure,
																		   BindingKind kind,
																		   AdditionalBindingPurpose purpose,
																		   org.hl7.fhir.r5.model.ValueSet valueSet,
																		   List<String> systems) {
			return EnumSet.allOf(CodedContentValidationAction.class);
		}

		@Override
		public SpecialValidationAction policyForSpecialValidation(IResourceValidator validator, Object appContext, SpecialValidationRule rule, String stackPath, Element resource, Element element) {
			return null;
		}

		@Override
		public List<org.hl7.fhir.r5.model.StructureDefinition> getImpliedProfilesForResource(IResourceValidator validator, Object appContext, String stackPath, ElementDefinition definition, org.hl7.fhir.r5.model.StructureDefinition structure, Element resource, boolean valid, IMessagingServices msgServices, List<ValidationMessage> messages) {
			return List.of();
		}

		@Override
		public ContainedReferenceValidationPolicy policyForContained(IResourceValidator validator,
																	 Object appContext,
																	 org.hl7.fhir.r5.model.StructureDefinition structure,
																	 ElementDefinition element,
																	 String containerType,
																	 String containerId,
																	 Element.SpecialElement containingResourceType,
																	 String path,
																	 String url) {
			return ContainedReferenceValidationPolicy.CHECK_VALID;
		}

		@Override
		public boolean isSuppressMessageId(String path, String messageId) {
			return false;
		}

		@Override
		public ReferenceValidationPolicy policyForReference(IResourceValidator validator, Object appContext, String path, String url, ReferenceDestinationType destinationType) {
			return ReferenceValidationPolicy.CHECK_VALID;
		}

		@Override
		public IValidationPolicyAdvisor getPolicyAdvisor() {
		  if (Objects.isNull(policyAdvisor)) {
			  return new BasePolicyAdvisorForFullValidation(getReferencePolicy(), Set.of());
		  }

		  return policyAdvisor;
	  }

		@Override
		public IValidationPolicyAdvisor setPolicyAdvisor(IValidationPolicyAdvisor policyAdvisor) {
			return this;
		}

		@Override
		public ReferenceValidationPolicy getReferencePolicy() {
			return ReferenceValidationPolicy.IGNORE;
		}@Override

        public Set<String> getCheckReferencesTo() {
            return Set.of();
        }
	}


}
