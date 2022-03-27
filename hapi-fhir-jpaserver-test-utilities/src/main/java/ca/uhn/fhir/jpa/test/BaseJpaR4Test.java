package ca.uhn.fhir.jpa.test;

/*-
 * #%L
 * HAPI FHIR JPA Server Test Utilities
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoCodeSystem;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoConceptMap;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoPatient;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoStructureDefinition;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoSubscription;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoValueSet;
import ca.uhn.fhir.jpa.api.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.api.svc.ISearchCoordinatorSvc;
import ca.uhn.fhir.jpa.batch.api.IBatchJobSubmitter;
import ca.uhn.fhir.jpa.binary.provider.BinaryAccessProvider;
import ca.uhn.fhir.jpa.binary.interceptor.BinaryStorageInterceptor;
import ca.uhn.fhir.jpa.bulk.export.api.IBulkDataExportJobSchedulingHelper;
import ca.uhn.fhir.jpa.dao.IFulltextSearchSvc;
import ca.uhn.fhir.jpa.dao.data.IForcedIdDao;
import ca.uhn.fhir.jpa.dao.data.IMdmLinkDao;
import ca.uhn.fhir.jpa.dao.data.IPartitionDao;
import ca.uhn.fhir.jpa.dao.data.IResourceHistoryTableDao;
import ca.uhn.fhir.jpa.dao.data.IResourceHistoryTagDao;
import ca.uhn.fhir.jpa.dao.data.IResourceIndexedComboStringUniqueDao;
import ca.uhn.fhir.jpa.dao.data.IResourceIndexedComboTokensNonUniqueDao;
import ca.uhn.fhir.jpa.dao.data.IResourceIndexedSearchParamCoordsDao;
import ca.uhn.fhir.jpa.dao.data.IResourceIndexedSearchParamDateDao;
import ca.uhn.fhir.jpa.dao.data.IResourceIndexedSearchParamQuantityDao;
import ca.uhn.fhir.jpa.dao.data.IResourceIndexedSearchParamQuantityNormalizedDao;
import ca.uhn.fhir.jpa.dao.data.IResourceIndexedSearchParamStringDao;
import ca.uhn.fhir.jpa.dao.data.IResourceReindexJobDao;
import ca.uhn.fhir.jpa.dao.data.IResourceTableDao;
import ca.uhn.fhir.jpa.dao.data.IResourceTagDao;
import ca.uhn.fhir.jpa.dao.data.ISearchDao;
import ca.uhn.fhir.jpa.dao.data.ISearchParamPresentDao;
import ca.uhn.fhir.jpa.dao.data.ISearchResultDao;
import ca.uhn.fhir.jpa.dao.data.ITagDefinitionDao;
import ca.uhn.fhir.jpa.dao.data.ITermCodeSystemDao;
import ca.uhn.fhir.jpa.dao.data.ITermCodeSystemVersionDao;
import ca.uhn.fhir.jpa.dao.data.ITermConceptDao;
import ca.uhn.fhir.jpa.dao.data.ITermConceptDesignationDao;
import ca.uhn.fhir.jpa.dao.data.ITermConceptMapDao;
import ca.uhn.fhir.jpa.dao.data.ITermConceptMapGroupElementTargetDao;
import ca.uhn.fhir.jpa.dao.data.ITermConceptParentChildLinkDao;
import ca.uhn.fhir.jpa.dao.data.ITermConceptPropertyDao;
import ca.uhn.fhir.jpa.dao.data.ITermValueSetConceptDao;
import ca.uhn.fhir.jpa.dao.data.ITermValueSetConceptDesignationDao;
import ca.uhn.fhir.jpa.dao.data.ITermValueSetDao;
import ca.uhn.fhir.jpa.dao.index.IJpaIdHelperService;
import ca.uhn.fhir.jpa.entity.TermCodeSystem;
import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.entity.TermValueSet;
import ca.uhn.fhir.jpa.entity.TermValueSetConcept;
import ca.uhn.fhir.jpa.interceptor.PerformanceTracingLoggingInterceptor;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import ca.uhn.fhir.jpa.packages.IPackageInstallerSvc;
import ca.uhn.fhir.jpa.partition.IPartitionLookupSvc;
import ca.uhn.fhir.jpa.provider.r4.JpaSystemProviderR4;
import ca.uhn.fhir.jpa.search.DatabaseBackedPagingProvider;
import ca.uhn.fhir.jpa.search.IStaleSearchDeletingSvc;
import ca.uhn.fhir.jpa.search.reindex.IResourceReindexingSvc;
import ca.uhn.fhir.jpa.search.warm.ICacheWarmingSvc;
import ca.uhn.fhir.jpa.searchparam.registry.SearchParamRegistryImpl;
import ca.uhn.fhir.jpa.term.BaseTermReadSvcImpl;
import ca.uhn.fhir.jpa.term.TermConceptMappingSvcImpl;
import ca.uhn.fhir.jpa.term.TermDeferredStorageSvcImpl;
import ca.uhn.fhir.jpa.term.api.ITermCodeSystemStorageSvc;
import ca.uhn.fhir.jpa.term.api.ITermConceptMappingSvc;
import ca.uhn.fhir.jpa.term.api.ITermDeferredStorageSvc;
import ca.uhn.fhir.jpa.term.api.ITermLoaderSvc;
import ca.uhn.fhir.jpa.term.api.ITermReadSvc;
import ca.uhn.fhir.jpa.term.api.ITermReadSvcR4;
import ca.uhn.fhir.jpa.test.config.TestR4Config;
import ca.uhn.fhir.jpa.util.MemoryCacheService;
import ca.uhn.fhir.jpa.util.ResourceCountCache;
import ca.uhn.fhir.jpa.validation.ValidationSettings;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.parser.StrictErrorHandler;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.server.BasePagingProvider;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor;
import ca.uhn.fhir.rest.server.provider.ResourceProviderFactory;
import ca.uhn.fhir.test.utilities.ITestDataBuilder;
import ca.uhn.fhir.util.ClasspathUtil;
import ca.uhn.fhir.util.UrlUtil;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.ValidationResult;
import org.hl7.fhir.common.hapi.validation.support.CachingValidationSupport;
import org.hl7.fhir.common.hapi.validation.validator.FhirInstanceValidator;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.AllergyIntolerance;
import org.hl7.fhir.r4.model.Appointment;
import org.hl7.fhir.r4.model.AuditEvent;
import org.hl7.fhir.r4.model.Binary;
import org.hl7.fhir.r4.model.BodyStructure;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CapabilityStatement;
import org.hl7.fhir.r4.model.CarePlan;
import org.hl7.fhir.r4.model.CareTeam;
import org.hl7.fhir.r4.model.ChargeItem;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Communication;
import org.hl7.fhir.r4.model.CommunicationRequest;
import org.hl7.fhir.r4.model.CompartmentDefinition;
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
import org.hl7.fhir.r4.model.Location;
import org.hl7.fhir.r4.model.Media;
import org.hl7.fhir.r4.model.Medication;
import org.hl7.fhir.r4.model.MedicationAdministration;
import org.hl7.fhir.r4.model.MedicationRequest;
import org.hl7.fhir.r4.model.Meta;
import org.hl7.fhir.r4.model.MolecularSequence;
import org.hl7.fhir.r4.model.NamingSystem;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.OperationDefinition;
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
import org.hl7.fhir.r5.utils.validation.IResourceValidator;
import org.hl7.fhir.r5.utils.validation.IValidationPolicyAdvisor;
import org.hl7.fhir.r5.utils.validation.constants.BestPracticeWarningLevel;
import org.hl7.fhir.r5.utils.validation.constants.BindingKind;
import org.hl7.fhir.r5.utils.validation.constants.CodedContentValidationPolicy;
import org.hl7.fhir.r5.utils.validation.constants.ContainedReferenceValidationPolicy;
import org.hl7.fhir.r5.utils.validation.constants.ReferenceValidationPolicy;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.event.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.AopTestUtils;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestR4Config.class})
public abstract class BaseJpaR4Test extends BaseJpaTest implements ITestDataBuilder {
	public static final String MY_VALUE_SET = "my-value-set";
	public static final String URL_MY_VALUE_SET = "http://example.com/my_value_set";
	public static final String URL_MY_CODE_SYSTEM = "http://example.com/my_code_system";
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
	protected CachingValidationSupport myCachingValidationSupport;
	@Autowired
	protected ITermCodeSystemStorageSvc myTermCodeSystemStorageSvc;
	@Autowired
	protected ISearchDao mySearchEntityDao;
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
	protected IResourceIndexedComboStringUniqueDao myResourceIndexedCompositeStringUniqueDao;
	@Autowired
	protected IResourceIndexedComboTokensNonUniqueDao myResourceIndexedComboTokensNonUniqueDao;
	@Autowired
	@Qualifier("myAllergyIntoleranceDaoR4")
	protected IFhirResourceDao<AllergyIntolerance> myAllergyIntoleranceDao;
	@Autowired
	protected BinaryAccessProvider myBinaryAccessProvider;
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
	protected IFhirResourceDaoCodeSystem<CodeSystem, Coding, CodeableConcept> myCodeSystemDao;
	@Autowired
	protected ITermCodeSystemDao myTermCodeSystemDao;
	@Autowired
	protected ITermConceptParentChildLinkDao myTermConceptParentChildLinkDao;
	@Autowired
	protected ITermCodeSystemVersionDao myTermCodeSystemVersionDao;
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
	protected ModelConfig myModelConfig;
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
	protected IResourceTableDao myResourceTableDao;
	@Autowired
	protected IResourceHistoryTableDao myResourceHistoryTableDao;
	@Autowired
	protected IForcedIdDao myForcedIdDao;
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
	@Qualifier("mySubstanceDaoR4")
	protected IFhirResourceDao<Substance> mySubstanceDao;
	@Autowired
	@Qualifier("mySystemDaoR4")
	protected IFhirSystemDao<Bundle, Meta> mySystemDao;
	@Autowired
	protected IResourceReindexingSvc myResourceReindexingSvc;
	@Autowired
	@Qualifier("mySystemProviderR4")
	protected JpaSystemProviderR4 mySystemProvider;
	@Autowired
	protected ITagDefinitionDao myTagDefinitionDao;
	@Autowired
	@Qualifier("myTaskDaoR4")
	protected IFhirResourceDao<Task> myTaskDao;
	@Autowired
	protected ITermReadSvcR4 myTermSvc;
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
	protected IFhirResourceDaoValueSet<ValueSet, Coding, CodeableConcept> myValueSetDao;
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
	protected IServerInterceptor myInterceptor;
	@Autowired
	protected DaoRegistry myDaoRegistry;
	@Autowired
	protected IJpaIdHelperService myIdHelperService;
	@Autowired
	protected IBatchJobSubmitter myBatchJobSubmitter;
	@Autowired
	protected ValidationSettings myValidationSettings;
	@Autowired
	protected IMdmLinkDao myMdmLinkDao;
	@Autowired
	private IValidationSupport myJpaValidationSupportChainR4;
	private PerformanceTracingLoggingInterceptor myPerformanceTracingLoggingInterceptor;
	@Autowired
	private IBulkDataExportJobSchedulingHelper myBulkDataScheduleHelper;

	@AfterEach()
	public void afterCleanupDao() {
		myDaoConfig.setExpireSearchResults(new DaoConfig().isExpireSearchResults());
		myDaoConfig.setEnforceReferentialIntegrityOnDelete(new DaoConfig().isEnforceReferentialIntegrityOnDelete());
		myDaoConfig.setExpireSearchResultsAfterMillis(new DaoConfig().getExpireSearchResultsAfterMillis());
		myDaoConfig.setReuseCachedSearchResultsForMillis(new DaoConfig().getReuseCachedSearchResultsForMillis());
		myDaoConfig.setSuppressUpdatesWithNoChange(new DaoConfig().isSuppressUpdatesWithNoChange());
		myDaoConfig.setAllowContainsSearches(new DaoConfig().isAllowContainsSearches());
		myDaoConfig.setAutoCreatePlaceholderReferenceTargets(new DaoConfig().isAutoCreatePlaceholderReferenceTargets());

		myPagingProvider.setDefaultPageSize(BasePagingProvider.DEFAULT_DEFAULT_PAGE_SIZE);
		myPagingProvider.setMaximumPageSize(BasePagingProvider.DEFAULT_MAX_PAGE_SIZE);

		myPartitionSettings.setPartitioningEnabled(false);
	}

	@AfterEach
	public void afterResetInterceptors() {
		myInterceptorRegistry.unregisterAllInterceptors();
	}

	@AfterEach
	public void afterClearTerminologyCaches() {
		BaseTermReadSvcImpl baseHapiTerminologySvc = AopTestUtils.getTargetObject(myTermSvc);
		baseHapiTerminologySvc.clearCaches();
		TermConceptMappingSvcImpl.clearOurLastResultsFromTranslationCache();
		TermConceptMappingSvcImpl.clearOurLastResultsFromTranslationWithReverseCache();
		TermDeferredStorageSvcImpl termDeferredStorageSvc = AopTestUtils.getTargetObject(myTerminologyDeferredStorageSvc);
		termDeferredStorageSvc.clearDeferred();
	}

	@BeforeEach
	public void beforeCreateInterceptor() {
		myInterceptor = mock(IServerInterceptor.class);

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

		myDaoConfig.setSchedulingDisabled(true);
		myDaoConfig.setIndexMissingFields(new DaoConfig().getIndexMissingFields());
	}

	@AfterEach
	public void afterPurgeDatabase() {
		runInTransaction(() -> {
			myMdmLinkDao.deleteAll();
		});
		purgeDatabase(myDaoConfig, mySystemDao, myResourceReindexingSvc, mySearchCoordinatorSvc, mySearchParamRegistry, myBulkDataScheduleHelper);
	}

	@BeforeEach
	public void beforeResetConfig() {
		myFhirContext.setParserErrorHandler(new StrictErrorHandler());
		myValidationSettings.setLocalReferenceValidationDefaultPolicy(new ValidationSettings().getLocalReferenceValidationDefaultPolicy());
	}

	@Override
	public IIdType doCreateResource(IBaseResource theResource) {
		IFhirResourceDao dao = myDaoRegistry.getResourceDao(theResource.getClass());
		return dao.create(theResource, mySrd).getId().toUnqualifiedVersionless();
	}

	@Override
	public IIdType doUpdateResource(IBaseResource theResource) {
		IFhirResourceDao dao = myDaoRegistry.getResourceDao(theResource.getClass());
		return dao.update(theResource, mySrd).getId().toUnqualifiedVersionless();
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

	protected void validate(IBaseResource theResource) {
		FhirValidator validatorModule = myFhirContext.newValidator();
		FhirInstanceValidator instanceValidator = new FhirInstanceValidator(myValidationSupport);
		instanceValidator.setBestPracticeWarningLevel(BestPracticeWarningLevel.Ignore);
		instanceValidator.setValidatorPolicyAdvisor(new ValidationPolicyAdvisor());
		validatorModule.registerValidatorModule(instanceValidator);
		ValidationResult result = validatorModule.validateWithResult(theResource);
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

	protected void assertHierarchyContains(String... theStrings) {
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
			assertThat("\n" + String.join("\n", hierarchy), hierarchy, empty());
		} else {
			assertThat("\n" + String.join("\n", hierarchy), hierarchy, contains(theStrings));
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
		myValueSetDao.purgeCaches();
		myJpaValidationSupportChainR4.invalidateCaches();
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

	public class ValidationPolicyAdvisor implements IValidationPolicyAdvisor {
		@Override
		public ReferenceValidationPolicy policyForReference(IResourceValidator validator, Object appContext, String path, String url) {
			return ReferenceValidationPolicy.CHECK_VALID;
		}

		@Override
		public CodedContentValidationPolicy policyForCodedContent(IResourceValidator iResourceValidator, Object o, String s, ElementDefinition elementDefinition, org.hl7.fhir.r5.model.StructureDefinition structureDefinition, BindingKind bindingKind, org.hl7.fhir.r5.model.ValueSet valueSet, List<String> list) {
			return CodedContentValidationPolicy.CODE;
		}

		@Override
		public ContainedReferenceValidationPolicy policyForContained(IResourceValidator validator, Object appContext, String containerType, String containerId, Element.SpecialElement containingResourceType, String path, String url) {
			return ContainedReferenceValidationPolicy.CHECK_VALID;
		}
	}
}
