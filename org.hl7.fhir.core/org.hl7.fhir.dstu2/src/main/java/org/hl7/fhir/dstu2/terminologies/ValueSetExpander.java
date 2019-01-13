package org.hl7.fhir.dstu2.terminologies;

/*-
 * #%L
 * org.hl7.fhir.dstu2
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


import java.io.FileNotFoundException;
import java.io.IOException;

import org.hl7.fhir.dstu2.model.ValueSet;

public interface ValueSetExpander {
  public class ETooCostly extends Exception {

    public ETooCostly(String msg) {
      super(msg);
    }

  }

  /**
   * Some value sets are just too big to expand. Instead of an expanded value set, 
   * you get back an interface that can test membership - usually on a server somewhere
   * 
   * @author Grahame
   */
  public class ValueSetExpansionOutcome {
    private ValueSet valueset;
    private ValueSetChecker service;
    private String error;
    public ValueSetExpansionOutcome(ValueSet valueset) {
      super();
      this.valueset = valueset;
      this.service = null;
      this.error = null;
    }
    public ValueSetExpansionOutcome(ValueSet valueset, String error) {
      super();
      this.valueset = valueset;
      this.service = null;
      this.error = error;
    }
    public ValueSetExpansionOutcome(ValueSetChecker service, String error) {
      super();
      this.valueset = null;
      this.service = service;
      this.error = error;
    }
    public ValueSetExpansionOutcome(String error) {
      this.valueset = null;
      this.service = null;
      this.error = error;
    }
    public ValueSet getValueset() {
      return valueset;
    }
    public ValueSetChecker getService() {
      return service;
    }
    public String getError() {
      return error;
    }


  }

  public ValueSetExpansionOutcome expand(ValueSet source) throws ETooCostly, FileNotFoundException, IOException;
}
