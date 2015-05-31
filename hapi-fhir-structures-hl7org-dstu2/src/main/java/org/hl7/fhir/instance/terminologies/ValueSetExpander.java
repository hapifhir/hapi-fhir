package org.hl7.fhir.instance.terminologies;

/*
Copyright (c) 2011+, HL7, Inc
All rights reserved.

Redistribution and use in source and binary forms, with or without modification, 
are permitted provided that the following conditions are met:

 * Redistributions of source code must retain the above copyright notice, this 
   list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, 
   this list of conditions and the following disclaimer in the documentation 
   and/or other materials provided with the distribution.
 * Neither the name of HL7 nor the names of its contributors may be used to 
   endorse or promote products derived from this software without specific 
   prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. 
IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, 
INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT 
NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR 
PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
POSSIBILITY OF SUCH DAMAGE.

*/

import org.hl7.fhir.instance.model.ValueSet;
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

  public ValueSetExpansionOutcome expand(ValueSet source) throws ETooCostly;
}
