package ca.uhn.fhir.jpa.dao.r4.core.model

import com.google.gson.annotations.SerializedName
import java.util.*


data class BundleParam(
   @SerializedName("rule") var rule: String = "",
   @SerializedName("profile") var profile: String = "",
)
