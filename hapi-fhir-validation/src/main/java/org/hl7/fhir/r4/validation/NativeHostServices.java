package org.hl7.fhir.r4.validation;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.hl7.fhir.convertors.VersionConvertorAdvisor40;
import org.hl7.fhir.convertors.VersionConvertor_10_40;
import org.hl7.fhir.convertors.VersionConvertor_14_40;
import org.hl7.fhir.convertors.VersionConvertor_30_40;
import org.hl7.fhir.exceptions.FHIRException;

/**
 * This class wraps up the validation and conversion infrastructure
 * so it can be hosted inside a native server
 * 
 * workflow is pretty simple:
 *  - create a DelphiLibraryHost, provide with path to library and tx server to use
 *    (tx server is usually the host server)
 *  - any structure definitions, value sets, code systems changes on the server get sent to tp seeResource or dropResource
 *  - server wants to validate a resource, it calls validateResource and gets an operation outcome back
 *  - server wants to convert from R4 to something else, it calls convertResource  
 *  - server wants to convert to R4 from something else, it calls unConvertResource  
 *  
 * threading: todo: this class should be thread safe
 *  
 * note: this is a solution that uses lots of RAM...  
 */

import org.hl7.fhir.r4.elementmodel.Manager.FhirFormat;
import org.hl7.fhir.r4.formats.XmlParser;
import org.hl7.fhir.r4.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.FhirPublication;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.model.ValueSet;
import org.hl7.fhir.r4.utils.IResourceValidator.BestPracticeWarningLevel;
import org.hl7.fhir.r4.utils.IResourceValidator.CheckDisplayOption;
import org.hl7.fhir.r4.utils.IResourceValidator.IdStatus;
import org.hl7.fhir.utilities.Utilities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

/**
 * This class allows you to host the java validator in another service, and use the services it has in a wider context. The way it works is 

- put the jar in your class path
- Find the class org.hl7.fhir.r4.validation.NativeHostServices or org.hl7.fhir.dstu3.validation.NativeHostServices
- call init(path) where path refers to one of the definitions files from the main build (e.g. definitions.xml.zip) - required, do only once, do before anything else
- call load(path) where path refers to the igpack.zip produced by the ig publisher (do this once for each IG you care about)
- call connectToTxSvc(url) where the url is your terminology service of choice (can be http://tx.fhir.org/r4 or /r3)

now the jar is ready for action. There's 3 functions you can call (all are thread safe):
- validate - given a resource, validate it against all known rules
- convert - given a resource in a different version convert it to this version (if possible)
- unconvert - given a resource, convert it to a different version (if possible)


also, call "status" to get a json object that describes the internals of the jar (e.g. for server status)


The interface is optimised for JNI. 
 * @author Grahame Grieve
 *
 */
public class NativeHostServices {
  private class NH_10_40_Advisor implements VersionConvertorAdvisor40 {

    @Override
    public boolean ignoreEntry(BundleEntryComponent src) {
      return false;
    }

    @Override
    public org.hl7.fhir.dstu2016may.model.Resource convertR2016May(Resource resource) throws FHIRException {
      return null;
    }

    @Override
    public org.hl7.fhir.dstu2.model.Resource convertR2(Resource resource) throws FHIRException {
      return null;
    }

    @Override
    public org.hl7.fhir.dstu3.model.Resource convertR3(Resource resource) throws FHIRException {
      return null;
    }

    @Override
    public void handleCodeSystem(CodeSystem tgtcs, ValueSet source) throws FHIRException {
    }

    @Override
    public CodeSystem getCodeSystem(ValueSet src) throws FHIRException {
      throw new FHIRException("Code systems cannot be handled at this time"); // what to do? need thread local storage? 
    }

  }

  private ValidationEngine validator;
  private int validationCount = 0;
  private int resourceCount = 0;
  private int convertCount = 0;
  private int unConvertCount = 0;
  private int exceptionCount = 0;
  private String lastException = null;  
  private Object lock = new Object();

  private VersionConvertorAdvisor40 conv_10_40_advisor = new NH_10_40_Advisor();

  /**
   * Create an instance of the service
   */
  public NativeHostServices()  {
    super();
  } 

  /**
   * Initialize the service and prepare it for use
   * 
   * @param pack - the filename of a pack from the main build - either definitions.xml.zip, definitions.json.zip, or igpack.zip 
   * @throws Exception
   */
  public void init(String pack) throws Exception {
    validator = new ValidationEngine(pack);
    validator.getContext().setAllowLoadingDuplicates(true);
  }

  /** 
   * Load an IG so that the validator knows all about it.
   * 
   * @param pack - the filename (or URL) of a validator.pack produced by the IGPublisher
   * 
   * @throws Exception
   */
  public void load(String pack) throws Exception {
    validator.loadIg(pack);
  }

  /** 
   * Set up the validator with a terminology service 
   * 
   * @param txServer - the URL of the terminology service (http://tx.fhir.org/r4 default)
   * @throws Exception
   */
  public void connectToTxSvc(String txServer, String log) throws Exception {
    validator.connectToTSServer(txServer, log, FhirPublication.R4);
  }

  /**
   * get back a JSON object with information about the process.
   * @return
   */
  public String status() {
    JsonObject json = new JsonObject();
    json.addProperty("custom-resource-count", resourceCount);
    validator.getContext().reportStatus(json);
    json.addProperty("validation-count", validationCount);
    json.addProperty("convert-count", convertCount);
    json.addProperty("unconvert-count", unConvertCount);
    json.addProperty("exception-count", exceptionCount);
    synchronized (lock) {
      json.addProperty("last-exception", lastException);      
    }

    json.addProperty("mem-max", Runtime.getRuntime().maxMemory() / (1024*1024));
    json.addProperty("mem-total", Runtime.getRuntime().totalMemory() / (1024*1024));
    json.addProperty("mem-free", Runtime.getRuntime().freeMemory() / (1024*1024));
    json.addProperty("mem-used", (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024*1024));

    Gson gson = new GsonBuilder().create();
    return gson.toJson(json);
  }

  /**
   * Call when the host process encounters one of the following:
   *  - (for validation):
   *    - profile
   *    - extension definition
   *    - value set
   *    - code system
   * 
   *  - (for conversion):
   *    - structure map 
   *    - concept map
   *  
   * @param source
   * @throws Exception
   */
  public void seeResource(byte[] source) throws Exception {
    try {
      Resource r = new XmlParser().parse(source);
      validator.seeResource(r);
      resourceCount++;
    } catch (Exception e) {
      exceptionCount++;

      synchronized (lock) {
        lastException = e.getMessage();
      }
      throw e;
    }
  }

  /**
   * forget a resource that was previously seen (using @seeResource)
   * 
   * @param type - the resource type
   * @param id - the resource id 
   * 
   * @throws Exception
   */
  public void dropResource(String type, String id) throws Exception  {
    try {
      validator.dropResource(type, id);
      resourceCount--;
    } catch (Exception e) {
      exceptionCount++;
      synchronized (lock) {
        lastException = e.getMessage();
      }
      throw e;
    }
  }

  /**
   * Validate a resource. 
   * 
   * Possible options:
   *   - id-optional : no resource id is required (default) 
   *   - id-required : a resource id is required
   *   - id-prohibited : no resource id is allowed
   *   - any-extensions : allow extensions other than those defined by the encountered structure definitions
   *   - bp-ignore : ignore best practice recommendations (default)
   *   - bp-hint : treat best practice recommendations as a hint
   *   - bp-warning : treat best practice recommendations as a warning 
   *   - bp-error : treat best practice recommendations as an error
   *   - display-ignore : ignore Coding.display and do not validate it (default)
   *   - display-check : check Coding.display - must be correct
   *   - display-case-space : check Coding.display but allow case and whitespace variation
   *   - display-case : check Coding.display but allow case variation
   *   - display-space : check Coding.display but allow whitespace variation
   *    
   * @param location - a text description of the context of validation (for human consumers to help locate the problem - echoed into error messages)
   * @param source - the bytes to validate
   * @param cntType - the format of the content. one of XML, JSON, TURTLE
   * @param options - a list of space separated options 
   * @return
   * @throws Exception
   */
  public byte[] validateResource(String location, byte[] source, String cntType, String options) throws Exception {
    try {
      IdStatus resourceIdRule = IdStatus.OPTIONAL;
      boolean anyExtensionsAllowed = true;
      BestPracticeWarningLevel bpWarnings = BestPracticeWarningLevel.Ignore;
      CheckDisplayOption displayOption = CheckDisplayOption.Ignore;
      for (String s : options.split(" ")) {
        if ("id-optional".equalsIgnoreCase(s))
          resourceIdRule = IdStatus.OPTIONAL;
        else if ("id-required".equalsIgnoreCase(s))
          resourceIdRule = IdStatus.REQUIRED;
        else if ("id-prohibited".equalsIgnoreCase(s))
          resourceIdRule = IdStatus.PROHIBITED;
        else if ("any-extensions".equalsIgnoreCase(s))
          anyExtensionsAllowed = true; // This is already the default
        else if ("strict-extensions".equalsIgnoreCase(s))
          anyExtensionsAllowed = false;
        else if ("bp-ignore".equalsIgnoreCase(s))
          bpWarnings = BestPracticeWarningLevel.Ignore;
        else if ("bp-hint".equalsIgnoreCase(s))
          bpWarnings = BestPracticeWarningLevel.Hint;
        else if ("bp-warning".equalsIgnoreCase(s))
          bpWarnings = BestPracticeWarningLevel.Warning;
        else if ("bp-error".equalsIgnoreCase(s))
          bpWarnings = BestPracticeWarningLevel.Error;
        else if ("display-ignore".equalsIgnoreCase(s))
          displayOption = CheckDisplayOption.Ignore;
        else if ("display-check".equalsIgnoreCase(s))
          displayOption = CheckDisplayOption.Check;
        else if ("display-case-space".equalsIgnoreCase(s))
          displayOption = CheckDisplayOption.CheckCaseAndSpace;
        else if ("display-case".equalsIgnoreCase(s))
          displayOption = CheckDisplayOption.CheckCase;
        else if ("display-space".equalsIgnoreCase(s))
          displayOption = CheckDisplayOption.CheckSpace;
        else if (!Utilities.noString(s))
          throw new Exception("Unknown option "+s);
      }

      OperationOutcome oo = validator.validate(location, source, FhirFormat.valueOf(cntType), null, resourceIdRule, anyExtensionsAllowed, bpWarnings, displayOption);
      ByteArrayOutputStream bs = new ByteArrayOutputStream();
      new XmlParser().compose(bs, oo);
      validationCount++;
      return bs.toByteArray();
    } catch (Exception e) {
      exceptionCount++;
      synchronized (lock) {
        lastException = e.getMessage();
      }
      throw e;
    }
  }

  /**
   * Convert a resource to R4 from the specified version
   * 
   * @param r - the source of the resource to convert from
   * @param fmt  - the format of the content. one of XML, JSON, TURTLE
   * @param version - the version of the content. one of r2, r3
   * @return - the converted resource (or an exception if can't be converted)
   * @throws FHIRException
   * @throws IOException
   */
  public byte[] convertResource(byte[] r, String fmt, String version) throws FHIRException, IOException  {
    try {
      if ("3.0".equals(version) || "3.0.1".equals(version) || "r3".equals(version)) {
        org.hl7.fhir.dstu3.formats.ParserBase p3 = org.hl7.fhir.dstu3.formats.FormatUtilities.makeParser(fmt);
        org.hl7.fhir.dstu3.model.Resource res3 = p3.parse(r);
        Resource res4 = VersionConvertor_30_40.convertResource(res3, false);
        org.hl7.fhir.r4.formats.ParserBase p4 = org.hl7.fhir.r4.formats.FormatUtilities.makeParser(fmt);
        convertCount++;
        return p4.composeBytes(res4);
      } else if ("1.0".equals(version) || "1.0.2".equals(version) || "r2".equals(version)) {
        org.hl7.fhir.dstu2.formats.ParserBase p2 = org.hl7.fhir.dstu2.formats.FormatUtilities.makeParser(fmt);
        org.hl7.fhir.dstu2.model.Resource res2 = p2.parse(r);
        VersionConvertor_10_40 conv = new VersionConvertor_10_40(conv_10_40_advisor );
        Resource res4 = conv.convertResource(res2);
        org.hl7.fhir.r4.formats.ParserBase p4 = org.hl7.fhir.r4.formats.FormatUtilities.makeParser(fmt);
        convertCount++;
        return p4.composeBytes(res4);
      } else if ("1.4".equals(version) || "1.4.0".equals(version)) {
        org.hl7.fhir.dstu2016may.formats.ParserBase p2 = org.hl7.fhir.dstu2016may.formats.FormatUtilities.makeParser(fmt);
        org.hl7.fhir.dstu2016may.model.Resource res2 = p2.parse(r);
        Resource res4 = VersionConvertor_14_40.convertResource(res2);
        org.hl7.fhir.r4.formats.ParserBase p4 = org.hl7.fhir.r4.formats.FormatUtilities.makeParser(fmt);
        convertCount++;
        return p4.composeBytes(res4);
      } else
        throw new FHIRException("Unsupported version "+version);
    } catch (Exception e) {
      exceptionCount++;
      synchronized (lock) {
        lastException = e.getMessage();
      }
      throw e;
    }
  }

  /**
   * Convert a resource from R4 to the specified version
   * 
   * @param r - the source of the resource to convert from
   * @param fmt  - the format of the content. one of XML, JSON, TURTLE
   * @param version - the version to convert to. one of r2, r3
   * @return - the converted resource (or an exception if can't be converted)
   * @throws FHIRException
   * @throws IOException
   */
  public byte[] unConvertResource(byte[] r, String fmt, String version) throws FHIRException, IOException  {
    try {
      if ("3.0".equals(version) || "3.0.1".equals(version) || "r3".equals(version)) {
        org.hl7.fhir.r4.formats.ParserBase p4 = org.hl7.fhir.r4.formats.FormatUtilities.makeParser(fmt);
        org.hl7.fhir.r4.model.Resource res4 = p4.parse(r);
        org.hl7.fhir.dstu3.model.Resource res3 = VersionConvertor_30_40.convertResource(res4, false);
        org.hl7.fhir.dstu3.formats.ParserBase p3 = org.hl7.fhir.dstu3.formats.FormatUtilities.makeParser(fmt);
        unConvertCount++;
        return p3.composeBytes(res3);
      } else if ("1.0".equals(version) || "1.0.2".equals(version) || "r2".equals(version)) {
        org.hl7.fhir.r4.formats.ParserBase p4 = org.hl7.fhir.r4.formats.FormatUtilities.makeParser(fmt);
        org.hl7.fhir.r4.model.Resource res4 = p4.parse(r);
        VersionConvertor_10_40 conv = new VersionConvertor_10_40(conv_10_40_advisor );
        org.hl7.fhir.dstu2.model.Resource res2 = conv.convertResource(res4);
        org.hl7.fhir.dstu2.formats.ParserBase p2 = org.hl7.fhir.dstu2.formats.FormatUtilities.makeParser(fmt);
        unConvertCount++;
        return p2.composeBytes(res2);
      } else if ("1.4".equals(version) || "1.4.0".equals(version)) {
        org.hl7.fhir.r4.formats.ParserBase p4 = org.hl7.fhir.r4.formats.FormatUtilities.makeParser(fmt);
        org.hl7.fhir.r4.model.Resource res4 = p4.parse(r);
        org.hl7.fhir.dstu2016may.model.Resource res2 = VersionConvertor_14_40.convertResource(res4);
        org.hl7.fhir.dstu2016may.formats.ParserBase p2 = org.hl7.fhir.dstu2016may.formats.FormatUtilities.makeParser(fmt);
        unConvertCount++;
        return p2.composeBytes(res2);
      } else
        throw new FHIRException("Unsupported version "+version);
    } catch (Exception e) {
      exceptionCount++;
      synchronized (lock) {
        lastException = e.getMessage();
      }
      throw e;
    }
  }


}   
