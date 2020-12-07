package ca.uhn.fhir.jpa.dao.r4.core

import ca.uhn.fhir.context.FhirContext
import ca.uhn.fhir.jpa.api.dao.DaoRegistry
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao
import ca.uhn.fhir.jpa.dao.r4.BaseJpaR4Test
import ca.uhn.fhir.jpa.dao.r4.core.model.TestEntry
import ca.uhn.fhir.jpa.dao.r4.core.model.TestResult
import ca.uhn.fhir.jpa.dao.r4.core.utils.*
import ca.uhn.fhir.parser.DataFormatException
import ca.uhn.fhir.rest.api.EncodingEnum
import ca.uhn.fhir.rest.api.MethodOutcome
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import org.hl7.fhir.common.hapi.validation.validator.FhirInstanceValidator
import org.hl7.fhir.instance.model.api.IBaseResource
import org.hl7.fhir.r4.model.OperationOutcome
import org.hl7.fhir.r4.model.Resource
import org.hl7.fhir.r5.utils.IResourceValidator
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.beans.factory.annotation.Autowired
import java.util.ArrayList
import java.util.stream.Stream


internal open class CoreValidationTests : BaseJpaR4Test() {

   //private lateinit var myCtx: FhirContext

   @Autowired
   protected lateinit var validator: FhirInstanceValidator

   @Autowired
   protected lateinit var daoRegistry: DaoRegistry

   @ParameterizedTest
   @MethodSource("testCaseProvider")
   fun `Run core validation testcases`(fileName: String, testEntry: TestEntry) {
      if (!testEntry.usesTest) {
         println("test case for file $fileName set to disabled, skipping")
      } else if (!validVersion(testEntry)) {
         println("test case fhir version ${testEntry.version} is not recognized as a valid version for testing, skipping entry")
      } else if (testEntry.testResult == null) {
         println("test case for file $fileName contains no valid test results")
      } else {
         println("test id $fileName")
         validator.configureValidator(testEntry)
         val resourceAsString: String = loadFileContents(TEST_FILES_BASE_PATH + fileName)
         Assertions.assertNotNull(resourceAsString, "Could not load resource string from file <$fileName>")
         validate(fileName, testEntry.testResult!!, resourceAsString, getTestProfile(testEntry))
      }
   }

   private fun FhirInstanceValidator.configureValidator(testEntry: TestEntry): FhirInstanceValidator {
      testEntry.bundleParam?.let { bundleRule ->
         println("\nBundle validation rule:\n${bundleRule}\n")
         val rule = IResourceValidator.BundleValidationRule(bundleRule.rule, bundleRule.profile)
         this.bundleValidationRules.add(rule)
      } ?: this.bundleValidationRules.clear()
      testEntry.supporting?.let { files ->
         println("\nSupporting files to load for validation:${files.joinToString(prefix = "\n", separator = ",\n")}\n")
         files.forEach { fileName ->
            val baseResource: IBaseResource = loadResource(myCtx, TEST_FILES_BASE_PATH + fileName)
            doCreateResource(baseResource)
         }
      }
//      testEntry.bundleParam?.let { bundleRule -> this.bundleValidationRules.add()}
//      validator.isAssumeValidRestReferences = testEntry.profile.assumeValidRestReferences
//      validator.isAllowExamples = testEntry.allowExamples
//      validator.validatorResourceFetcher = TestResourceFetcher(testEntry)
//      validator.bestPracticeWarningLevel = IResourceValidator.BestPracticeWarningLevel.Ignore
//      myDaoConfig.isAllowExternalReferences = true

      return this
   }



   protected fun validate(
      testFile: String,
      testResult: TestResult,
      resourceAsString: String = "",
      testProfile: String
   ) {
      var resourceName: String? = null
      var operationOutcome = OperationOutcome()
      try {
         resourceName = extractResourceName(testFile, resourceAsString)
         val resourceDao: IFhirResourceDao<IBaseResource?> = daoRegistry.getResourceDaoOrNull(resourceName)!!
         operationOutcome = validate(myCtx, testProfile, resourceName, resourceAsString, resourceDao)
      } catch (e: Exception) {
         operationOutcome.addIssue().setSeverity(OperationOutcome.IssueSeverity.ERROR).diagnostics = e.message
      }
      verifyOperationOutcome(testResult, operationOutcome)
   }

   /**
    * Validates the passed in [IBaseResource] using the provided [IFhirResourceDao] then returns the
    * [OperationOutcome] of the result.
    *
    * @param input
    * @param resourceDao The [IFhirResourceDao] to use to validate the passed in [IBaseResource]
    * @return The resulting [OperationOutcome] from validating the resource.
    */
   fun validate(
      ctx: FhirContext,
      testProfile: String?,
      resourceName: String?,
      input: String?,
      resourceDao: IFhirResourceDao<IBaseResource?>
   ): OperationOutcome {
      return try {
         resourceDao.validate(
            ctx.getResourceDefinition(resourceName).newInstance(),
            null,
            input,
            EncodingEnum.detectEncoding(input),
            null,
            testProfile,
            null
         ).operationOutcome as OperationOutcome
      } catch (e: PreconditionFailedException) {
         e.operationOutcome as OperationOutcome
      } catch (e: DataFormatException) {
         val dataFormatOperationOutcome = OperationOutcome()
         dataFormatOperationOutcome.addIssue().setSeverity(OperationOutcome.IssueSeverity.ERROR).diagnostics = e.message
         dataFormatOperationOutcome
      } catch (e: NullPointerException) {
         val dataFormatOperationOutcome = OperationOutcome()
         dataFormatOperationOutcome.addIssue().setSeverity(OperationOutcome.IssueSeverity.ERROR).diagnostics = e.message
         dataFormatOperationOutcome
      }
   }

   @Throws(RuntimeException::class)
   protected fun extractResourceName(testFile: String, resourceAsString: String?): String? {
      var resourceName: String? = null
      try {
         val baseResource: IBaseResource = loadResource(myCtx, TEST_FILES_BASE_PATH + testFile)
         resourceName = myCtx.getResourceDefinition(baseResource).name
      } catch (e: Exception) {
         println(e.message)
      }
      if (resourceName == null) {
         val encoding: EncodingEnum = getEncoding(testFile)
         resourceName = getResourceName(encoding, resourceAsString)
      }
      return resourceName
   }

   companion object {
      protected const val TEST_FILES_BASE_PATH = "/org/hl7/fhir/testcases/validator/"
      protected const val TEST_MANIFEST_PATH = TEST_FILES_BASE_PATH + "manifest.json"

      private lateinit var gson: Gson
      private lateinit var myCtx: FhirContext

      @BeforeAll
      @JvmStatic
      fun beforeAll() {
         // We only ever need one FhirContext, InstanceValidator, and Gson parser, so we create them once for all the tests
         myCtx = FhirContext.forR4() // Need to create proper context for resource
         gson = Gson()
         println("---------- Beginning Validation Tests for 4.0 ----------")
      }

      @JvmStatic
      fun testCaseProvider(): Stream<Arguments> {
         /*
          * All tests and outcomes are listed in the manifest.json file, found in the fhir-test-cases repository. We
          * first load in that file, and from it we create a Stream of test cases we can run as parameterized tests.
          * If this load fails, testing cannot be completed, and we barf out an IllegalStateException.
          */
         val contents = loadFileContents(TEST_MANIFEST_PATH)

         /*
          * Each test case consists of a named source file like 'allergy.json', and a list of expected outputs and
          * behaviours (formatted as a json entry). We split, sort, then stream these test entries so they can be run
          * individually.
          */

         val manifest = JsonParser().parse(contents) as JsonObject
         return manifest.getAsJsonObject("test-cases")
            .entrySet()
            .sortedBy { it.key }
            .filter { (_, testContent) ->
               cleanVersionString(extractVersion(gson.fromJson(testContent, TestEntry::class.java))) == "4.0.0"
                  || cleanVersionString(extractVersion(gson.fromJson(testContent, TestEntry::class.java))) == "4.0.1"
            }.map { (testId, testContent) ->
               Arguments.arguments(testId, gson.fromJson(testContent, TestEntry::class.java))
            }.subList(9, 10).stream()
      }

      fun getTestProfile(testEntry: TestEntry): String {
         return testEntry.profile.source?.let {
            loadFileContents(TEST_FILES_BASE_PATH + it)
         } ?: ""
      }

      /**
       * Loads the file at the given path, and return the contents as a String. If no such file exists, will throw an
       * IllegalStateException.
       */
      fun loadFileContents(path: String): String {
         return CoreValidationTests::class.java.getResource(path)?.readText()
            ?: throw IllegalStateException("Unable to load <$path>.\nCheck dependencies are loaded correctly.")
      }

   }
}
