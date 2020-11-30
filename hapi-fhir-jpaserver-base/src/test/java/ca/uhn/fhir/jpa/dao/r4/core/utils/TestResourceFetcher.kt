package ca.uhn.fhir.jpa.dao.r4.core.utils

import ca.uhn.fhir.jpa.dao.r4.core.model.TestEntry
import org.hl7.fhir.exceptions.FHIRException
import org.hl7.fhir.r5.elementmodel.Element
import org.hl7.fhir.r5.elementmodel.Manager
import org.hl7.fhir.r5.elementmodel.ObjectConverter
import org.hl7.fhir.r5.model.Patient
import org.hl7.fhir.r5.test.utils.TestingUtilities
import org.hl7.fhir.r5.utils.IResourceValidator
import java.io.IOException
import java.util.*

class TestResourceFetcher(private val testEntry: TestEntry) : IResourceValidator.IValidatorResourceFetcher {
   @Throws(IOException::class, FHIRException::class)
   override fun fetch(appContext: Any, url: String): Element {
      var res: Element? = null
      if (url == "Patient/test") {
         res = ObjectConverter(TestingUtilities.context()).convert(Patient())
      } else if (TestingUtilities.findTestResource("validator", url.replace("/", "-").toLowerCase() + ".json")) {
         res = Manager.makeParser(TestingUtilities.context(), Manager.FhirFormat.JSON)
            .parse(TestingUtilities.loadTestResourceStream("validator", url.replace("/", "-").toLowerCase() + ".json"))
      } else if (TestingUtilities.findTestResource("validator", url.replace("/", "-").toLowerCase() + ".xml")) {
         res = Manager.makeParser(TestingUtilities.context(), Manager.FhirFormat.XML)
            .parse(TestingUtilities.loadTestResourceStream("validator", url.replace("/", "-").toLowerCase() + ".xml"))
      }
      if (res == null && url.contains("/")) {
         val tail = url.substring(url.indexOf("/") + 1)
         if (TestingUtilities.findTestResource("validator", tail.replace("/", "-").toLowerCase() + ".json")) {
            res = Manager.makeParser(TestingUtilities.context(), Manager.FhirFormat.JSON)
               .parse(
                  TestingUtilities.loadTestResourceStream(
                     "validator",
                     tail.replace("/", "-").toLowerCase() + ".json"
                  )
               )
         } else if (TestingUtilities.findTestResource("validator", tail.replace("/", "-").toLowerCase() + ".xml")) {
            res = Manager.makeParser(TestingUtilities.context(), Manager.FhirFormat.XML)
               .parse(
                  TestingUtilities.loadTestResourceStream(
                     "validator",
                     tail.replace("/", "-").toLowerCase() + ".xml"
                  )
               )
         }
      }
      return res!!
   }

   override fun validationPolicy(
      appContext: Any,
      path: String,
      url: String
   ): IResourceValidator.ReferenceValidationPolicy {
      return if (testEntry.validate != null) IResourceValidator.ReferenceValidationPolicy.valueOf(
         testEntry.validate!!
      ) else IResourceValidator.ReferenceValidationPolicy.IGNORE
   }

   @Throws(FHIRException::class)
   override fun resolveURL(appContext: Any, path: String, url: String): Boolean {
      return !url.contains("example.org")
   }

   override fun fetchRaw(p0: String?): ByteArray {
      TODO("Not yet implemented")
   }

   override fun setLocale(p0: Locale?) {
      TODO("Not yet implemented")
   }
}
