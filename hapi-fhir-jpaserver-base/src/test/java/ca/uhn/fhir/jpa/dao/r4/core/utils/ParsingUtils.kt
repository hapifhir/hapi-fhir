package ca.uhn.fhir.jpa.dao.r4.core.utils

import java.io.IOException

import java.net.MalformedURLException

import java.io.StringReader

import com.google.gson.Gson

import com.google.gson.JsonSyntaxException

import org.apache.derby.iapi.types.XML

import ca.uhn.fhir.rest.api.EncodingEnum
import com.google.gson.JsonObject
import org.w3c.dom.Document
import org.xml.sax.InputSource
import org.xml.sax.SAXException
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.parsers.ParserConfigurationException


const val RESOURCE_TYPE = "resourceType"

/**
 * Parses the passed in [String] and returns the name of the associated fhir resource type name.
 *
 * @param encoding One of [EncodingEnum.JSON] or [EncodingEnum.XML]
 * @param resourceAsString The entire resource as a [String]
 *
 * @return [String] name of the resource, eg. "Patient", "Bundle", "Group"
 *
 * @throws RuntimeException
 * @throws JsonSyntaxException
 */
@Throws(RuntimeException::class, JsonSyntaxException::class)
fun getResourceName(encoding: EncodingEnum?, resourceAsString: String?): String? {
   return when (encoding) {
      EncodingEnum.XML -> resourceAsString?.let { getResourceNameFromXML(it) }
      EncodingEnum.JSON -> resourceAsString?.let { getResourceNameFromJSON(resourceAsString) }
      else -> throw IllegalStateException(
         "Passed in test file is not a supported file " +
            "type for parsing in the test case. (Must be either XML or JSON)"
      )
   }
}

/**
 * Parses the passed in JSON [String] and returns the name of the primitive with the label [ParsingUtils.RESOURCE_TYPE]
 * in the resulting [JsonObject], which for our purposes, is the name of the FHIR resource.
 *
 * @param resourceAsString [String] resource.
 * @return [String] name of the resource, eg. "Patient", "Bundle", "Group"
 *
 * @throws JsonSyntaxException
 */
fun getResourceNameFromJSON(resourceAsString: String): String? {
   val parsedObject: JsonObject = Gson().fromJson(resourceAsString, JsonObject::class.java)
   return parsedObject.getAsJsonPrimitive(RESOURCE_TYPE).asString
}

/**
 * Parses the passed in XML [String] and returns the name of the base element in the [Document],
 * which for our purposes, is the name of the FHIR resource.
 *
 * @param resourceAsString [String] resource.
 * @return [String] name of the resource, eg. "Patient", "Bundle", "Group"
 *
 * @throws RuntimeException
 */
fun getResourceNameFromXML(resourceAsString: String): String? {
   val xmlDocument: Document? = parseXmlFile(resourceAsString)
   return xmlDocument?.documentElement?.nodeName
}

/**
 * This function converts String XML to Document object
 *
 * @param in - XML [String]
 * @return [Document] object
 */
fun parseXmlFile(`in`: String?): Document? {
   try {
      val dbf: DocumentBuilderFactory = DocumentBuilderFactory.newInstance()
      val db: DocumentBuilder = dbf.newDocumentBuilder()
      val `is` = InputSource(StringReader(`in`))
      return db.parse(`is`)
   } catch (e: ParserConfigurationException) {
      throw RuntimeException(e)
   } catch (e: MalformedURLException) {
      throw RuntimeException(e)
   } catch (e: SAXException) {
      throw RuntimeException(e)
   } catch (e: IOException) {
      e.printStackTrace()
   }
   return null
}
