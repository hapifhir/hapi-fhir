package ca.uhn.fhir.jpa.term;

import static org.junit.jupiter.api.Assertions.assertEquals;
import ca.uhn.fhir.jpa.entity.TermValueSet;
import ca.uhn.fhir.jpa.entity.TermValueSetPreExpansionStatusEnum;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class ValueSetExpansionWithHierarchyR4Test extends BaseTermR4Test {
   private static final String ourCodeSystemId = "CodeSystem-WithHierarchy", ourCodeSystemUrl = "http://example/" + ourCodeSystemId;
   private static final String ourCodeA = "CodeA", ourCodeB = "CodeB", ourCodeC = "CodeC", ourCodeD = "CodeD";
   private static final String ourValueSetAUrl = "http://example/ValueSetA", ourValueSetBUrl = "http://example/ValueSetB", ourValueSetUrl = "http://example/ValueSet";
   private static final int ourChildConceptCount = 10;

   @BeforeAll
   public static void setup() {
      TermReadSvcImpl.setForceDisableHibernateSearchForUnitTest(true);
   }
   @AfterAll
   public static void tearDown() {
      TermReadSvcImpl.setForceDisableHibernateSearchForUnitTest(false);
   }
   @BeforeEach
   public void setupHierarchicalCodeSystemWithValueSets() {
      myStorageSettings.setPreExpandValueSets(true);

      CodeSystem codeSystem = new CodeSystem();
      codeSystem.setId(ourCodeSystemId);
      codeSystem.setUrl(ourCodeSystemUrl);
      CodeSystem.ConceptDefinitionComponent concept1 = codeSystem.addConcept().setCode(ourCodeA);
      CodeSystem.ConceptDefinitionComponent concept2 = codeSystem.addConcept().setCode(ourCodeB);
      for (int i = 0; i < ourChildConceptCount; i++) {
         concept1.addConcept().setCode(concept1.getCode() + i);
         concept2.addConcept().setCode(concept2.getCode() + i);
      }
      codeSystem.addConcept().setCode(ourCodeC);
      codeSystem.addConcept().setCode(ourCodeD);
      myCodeSystemDao.create(codeSystem, mySrd);

      ValueSet valueSetA = new ValueSet();
      valueSetA.setUrl(ourValueSetAUrl);
      valueSetA.getCompose().addInclude().setSystem(ourCodeSystemUrl)
            .addFilter().setProperty("concept").setOp(ValueSet.FilterOperator.ISA).setValue(ourCodeA);
      myValueSetDao.create(valueSetA, mySrd);

      ValueSet valueSetB = new ValueSet();
      valueSetB.setUrl(ourValueSetBUrl);
      valueSetA.getCompose().addInclude().setSystem(ourCodeSystemUrl)
            .addFilter().setProperty("concept").setOp(ValueSet.FilterOperator.ISA).setValue(ourCodeB);
      myValueSetDao.create(valueSetB, mySrd);
   }

   static Stream<Arguments> parametersValueSets() {
      ValueSet valueSet0 = new ValueSet();
      valueSet0.setUrl(ourValueSetUrl + "-WithIncludeChildValueSet");
      valueSet0.getCompose().addInclude().addValueSet(ourValueSetAUrl);

      ValueSet valueSet1 = new ValueSet();
      valueSet1.setUrl(ourValueSetUrl + "-WithIncludeChildValueSetAndCodeSystem");
      valueSet1.getCompose().addInclude().addValueSet(ourValueSetAUrl);
      valueSet1.getCompose().addInclude().addValueSet(ourValueSetBUrl);
      valueSet1.getCompose().addInclude().setSystem(ourCodeSystemUrl);

      ValueSet valueSet2 = new ValueSet();
      valueSet2.setUrl(ourValueSetUrl + "-WithIncludeChildValueSetAndCodeSystemConceptSet-NoIntersectionCodes");
      valueSet2.getCompose().addInclude().addValueSet(ourValueSetAUrl);
      ValueSet.ConceptSetComponent conceptSetWithCodeSystem = valueSet2.getCompose().addInclude().setSystem(ourCodeSystemUrl);
      conceptSetWithCodeSystem.addConcept().setCode(ourCodeC);
      conceptSetWithCodeSystem.addConcept().setCode(ourCodeD);

      ValueSet valueSet3 = new ValueSet();
      valueSet3.setUrl(ourValueSetUrl + "-WithIncludeChildValueSetAndCodeSystemConceptSet-WithIntersectionCodes");
      valueSet3.getCompose().addInclude().addValueSet(ourValueSetAUrl);
      conceptSetWithCodeSystem = valueSet3.getCompose().addInclude().setSystem(ourCodeSystemUrl);
      conceptSetWithCodeSystem.addConcept().setCode(ourCodeA + "1");
      conceptSetWithCodeSystem.addConcept().setCode(ourCodeA + "2");

      return Stream.of(
            arguments(valueSet0, ourChildConceptCount),
            arguments(valueSet1, 4 + ourChildConceptCount * 2),
            arguments(valueSet2, 2 + ourChildConceptCount),
            arguments(valueSet3, ourChildConceptCount)
      );
   }

   @ParameterizedTest
   @MethodSource(value = "parametersValueSets")
   public void testExpandValueSet_whenUsingHierarchicalCodeSystem_willExpandSuccessfully(ValueSet theValueSet, int theExpectedConceptExpansionCount) {
      myValueSetDao.create(theValueSet, mySrd);
      myTermSvc.preExpandDeferredValueSetsToTerminologyTables();
      Optional<TermValueSet> optionalTermValueSet = runInTransaction(() -> myTermValueSetDao.findTermValueSetByUrlAndNullVersion(theValueSet.getUrl()));
		 assertThat(optionalTermValueSet).isPresent();
      TermValueSet expandedTermValueSet = optionalTermValueSet.get();
	   assertEquals(TermValueSetPreExpansionStatusEnum.EXPANDED, expandedTermValueSet.getExpansionStatus());
	   assertEquals(theExpectedConceptExpansionCount, expandedTermValueSet.getTotalConcepts());
   }
}
