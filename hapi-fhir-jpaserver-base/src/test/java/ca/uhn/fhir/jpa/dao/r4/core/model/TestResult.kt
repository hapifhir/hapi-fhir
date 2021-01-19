package ca.uhn.fhir.jpa.dao.r4.core.model

import com.google.gson.annotations.SerializedName
import java.util.*


data class TestResult(
   @SerializedName("errorCount") var errorCount: Int = 0,
   @SerializedName("warningCount") var warningCount: Int = DO_NOT_CHECK,
   @SerializedName("output") var output: List<String> = ArrayList()
) {
   companion object {
      const val DO_NOT_CHECK = Int.MIN_VALUE
   }

   override fun toString(): String {
      return "Error <${errorCount} entries>\n" +
         "Warning <${warningCount} entries>\n" +
         "Message Output -> \n" +
         output.joinToString(separator = "\n")
   }
}

