package org.hl7.fhir.instance.model;

/*
 * #%L
 * HAPI FHIR Structures - HL7.org DSTU2
 * %%
 * Copyright (C) 2014 - 2015 University Health Network
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


public class Enumerations {

  public enum ConformanceResourceStatus {
    /**
     * This resource is still under development
     */
    DRAFT, 
    /**
     * This resource is ready for normal use
     */
    ACTIVE, 
    /**
     * This resource has been withdrawn or superceded and should no longer be used
     */
    RETIRED, 
    /**
     * added to help the parsers
     */
    NULL;
    public static ConformanceResourceStatus fromCode(String codeString) throws Exception {
      if (codeString == null || "".equals(codeString))
        return null;
      if ("draft".equals(codeString))
        return DRAFT;
      if ("active".equals(codeString))
        return ACTIVE;
      if ("retired".equals(codeString))
        return RETIRED;
      throw new Exception("Unknown ConformanceResourceStatus code '"+codeString+"'");
    }
    public String toCode() {
      switch (this) {
      case DRAFT: return "draft";
      case ACTIVE: return "active";
      case RETIRED: return "retired";
      default: return "?";
      }
    }
    public String getSystem() {
      switch (this) {
      case DRAFT: return "";
      case ACTIVE: return "";
      case RETIRED: return "";
      default: return "?";
      }
    }
    public String getDefinition() {
      switch (this) {
      case DRAFT: return "This resource is still under development";
      case ACTIVE: return "This resource is ready for normal use";
      case RETIRED: return "This resource has been withdrawn or superceded and should no longer be used";
      default: return "?";
      }
    }
    public String getDisplay() {
      switch (this) {
      case DRAFT: return "Draft";
      case ACTIVE: return "Active";
      case RETIRED: return "Retired";
      default: return "?";
      }
    }
  }

  public static class ConformanceResourceStatusEnumFactory implements EnumFactory<ConformanceResourceStatus> {
    public ConformanceResourceStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
        if (codeString == null || "".equals(codeString))
          return null;
      if ("draft".equals(codeString))
        return ConformanceResourceStatus.DRAFT;
      if ("active".equals(codeString))
        return ConformanceResourceStatus.ACTIVE;
      if ("retired".equals(codeString))
        return ConformanceResourceStatus.RETIRED;
      throw new IllegalArgumentException("Unknown ConformanceResourceStatus code '"+codeString+"'");
    }
    public String toCode(ConformanceResourceStatus code) {
      if (code == ConformanceResourceStatus.DRAFT)
        return "draft";
      if (code == ConformanceResourceStatus.ACTIVE)
        return "active";
      if (code == ConformanceResourceStatus.RETIRED)
        return "retired";
      return "?";
    }
  }

}
