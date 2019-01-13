package org.hl7.fhir.dstu3.terminologies;

/*-
 * #%L
 * org.hl7.fhir.dstu3
 * %%
 * Copyright (C) 2014 - 2019 Health Level 7
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


import org.hl7.fhir.dstu3.model.CodeSystem;
import org.hl7.fhir.dstu3.model.Identifier;
import org.hl7.fhir.dstu3.model.Meta;
import org.hl7.fhir.dstu3.model.UriType;
import org.hl7.fhir.dstu3.model.ValueSet;
import org.hl7.fhir.dstu3.utils.ToolingExtensions;
import org.hl7.fhir.utilities.Utilities;

public class ValueSetUtilities {

  public static ValueSet makeShareable(ValueSet vs) {
    if (!vs.hasMeta())
      vs.setMeta(new Meta());
    for (UriType t : vs.getMeta().getProfile()) 
      if (t.getValue().equals("http://hl7.org/fhir/StructureDefinition/shareablevalueset"))
        return vs;
    vs.getMeta().getProfile().add(new UriType("http://hl7.org/fhir/StructureDefinition/shareablevalueset"));
    return vs;
  }

  public static void checkShareable(ValueSet vs) {
    if (!vs.hasMeta())
      throw new Error("ValueSet "+vs.getUrl()+" is not shareable");
    for (UriType t : vs.getMeta().getProfile()) {
      if (t.getValue().equals("http://hl7.org/fhir/StructureDefinition/shareablevalueset"))
        return;
    }
    throw new Error("ValueSet "+vs.getUrl()+" is not shareable");    
  }

  public static boolean hasOID(ValueSet vs) {
    return getOID(vs) != null;
  }

  public static String getOID(ValueSet vs) {
    for (Identifier id : vs.getIdentifier()) {
      if ("urn:ietf:rfc:3986".equals(id.getSystem()) && id.hasValue() && id.getValue().startsWith("urn:oid:"))
        return id.getValue().substring(8);
    }
    return null;
  }

  public static void setOID(ValueSet vs, String oid) {
    if (!oid.startsWith("urn:oid:"))
      oid = "urn:oid:" + oid;
    for (Identifier id : vs.getIdentifier()) {
      if ("urn:ietf:rfc:3986".equals(id.getSystem()) && id.hasValue() && id.getValue().startsWith("urn:oid:")) {
        id.setValue(oid);
        return;
      }
    }
    vs.addIdentifier().setSystem("urn:ietf:rfc:3986").setValue(oid);
  }

  public static void markStatus(ValueSet vs, String wg, String status, String fmm) {
    if (wg != null) {
      if (!ToolingExtensions.hasExtension(vs, ToolingExtensions.EXT_WORKGROUP) || 
          (Utilities.existsInList(ToolingExtensions.readStringExtension(vs, ToolingExtensions.EXT_WORKGROUP), "fhir", "vocab") && !Utilities.existsInList(wg, "fhir", "vocab"))) {
        ToolingExtensions.setCodeExtension(vs, ToolingExtensions.EXT_WORKGROUP, wg);
      }
    }
    if (status != null) {
      String ss = ToolingExtensions.readStringExtension(vs, ToolingExtensions.EXT_BALLOT_STATUS);
      if (Utilities.noString(ss) || ssval(ss) < ssval(status)) 
        ToolingExtensions.setStringExtension(vs, ToolingExtensions.EXT_BALLOT_STATUS, status);
    }
    if (fmm != null) {
      String sfmm = ToolingExtensions.readStringExtension(vs, ToolingExtensions.EXT_FMM_LEVEL);
      if (Utilities.noString(sfmm) || Integer.parseInt(sfmm) < Integer.parseInt(fmm)) 
        ToolingExtensions.setIntegerExtension(vs, ToolingExtensions.EXT_FMM_LEVEL, Integer.parseInt(fmm));
    }
    if (vs.hasUserData("cs"))
      CodeSystemUtilities.markStatus((CodeSystem) vs.getUserData("cs"), wg, status, fmm);
  }

  private static int ssval(String status) {
    if ("Draft".equals("status")) 
      return 1;
    if ("Informative".equals("status")) 
      return 2;
    if ("External".equals("status")) 
      return 3;
    if ("Trial Use".equals("status")) 
      return 3;
    if ("Normative".equals("status")) 
      return 4;
    return -1;
  }

}
