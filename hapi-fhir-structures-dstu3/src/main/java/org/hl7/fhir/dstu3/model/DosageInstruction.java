package org.hl7.fhir.dstu3.model;

/*
  Copyright (c) 2011+, HL7, Inc.
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

// Generated on Sat, Nov 5, 2016 10:42-0400 for FHIR v1.7.0

import java.util.*;

import org.hl7.fhir.utilities.Utilities;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.ChildOrder;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.model.api.annotation.Block;
import org.hl7.fhir.instance.model.api.*;
import org.hl7.fhir.exceptions.FHIRException;
/**
 * Indicates how the medication is to be used by the patient.
 */
@DatatypeDef(name="DosageInstruction")
public class DosageInstruction extends Type implements ICompositeType {

    /**
     * Indicates the order in which the dosage instructions should be applied or interpreted.
     */
    @Child(name = "sequence", type = {IntegerType.class}, order=0, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The order of the dosage instructions", formalDefinition="Indicates the order in which the dosage instructions should be applied or interpreted." )
    protected IntegerType sequence;

    /**
     * Free text dosage instructions e.g. SIG.
     */
    @Child(name = "text", type = {StringType.class}, order=1, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Free text dosage instructions e.g. SIG", formalDefinition="Free text dosage instructions e.g. SIG." )
    protected StringType text;

    /**
     * Supplemental instructions - e.g. "with meals".
     */
    @Child(name = "additionalInstructions", type = {CodeableConcept.class}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Supplemental instructions - e.g. \"with meals\"", formalDefinition="Supplemental instructions - e.g. \"with meals\"." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/additional-instructions-codes")
    protected List<CodeableConcept> additionalInstructions;

    /**
     * When medication should be administered.
     */
    @Child(name = "timing", type = {Timing.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="When medication should be administered", formalDefinition="When medication should be administered." )
    protected Timing timing;

    /**
     * Take "as needed" (for x).
     */
    @Child(name = "asNeeded", type = {BooleanType.class, CodeableConcept.class}, order=4, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Take \"as needed\" (for x)", formalDefinition="Take \"as needed\" (for x)." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/medication-as-needed-reason")
    protected Type asNeeded;

    /**
     * Body site to administer to.
     */
    @Child(name = "site", type = {CodeableConcept.class, BodySite.class}, order=5, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Body site to administer to", formalDefinition="Body site to administer to." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/approach-site-codes")
    protected Type site;

    /**
     * How drug should enter body.
     */
    @Child(name = "route", type = {CodeableConcept.class}, order=6, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="How drug should enter body", formalDefinition="How drug should enter body." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/route-codes")
    protected CodeableConcept route;

    /**
     * Technique for administering medication.
     */
    @Child(name = "method", type = {CodeableConcept.class}, order=7, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Technique for administering medication", formalDefinition="Technique for administering medication." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/administration-method-codes")
    protected CodeableConcept method;

    /**
     * Amount of medication per dose.
     */
    @Child(name = "dose", type = {Range.class, SimpleQuantity.class}, order=8, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Amount of medication per dose", formalDefinition="Amount of medication per dose." )
    protected Type dose;

    /**
     * Upper limit on medication per unit of time.
     */
    @Child(name = "maxDosePerPeriod", type = {Ratio.class}, order=9, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Upper limit on medication per unit of time", formalDefinition="Upper limit on medication per unit of time." )
    protected Ratio maxDosePerPeriod;

    /**
     * Upper limit on medication per administration.
     */
    @Child(name = "maxDosePerAdministration", type = {SimpleQuantity.class}, order=10, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Upper limit on medication per administration", formalDefinition="Upper limit on medication per administration." )
    protected SimpleQuantity maxDosePerAdministration;

    /**
     * Upper limit on medication per lifetime of the patient.
     */
    @Child(name = "maxDosePerLifetime", type = {SimpleQuantity.class}, order=11, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Upper limit on medication per lifetime of the patient", formalDefinition="Upper limit on medication per lifetime of the patient." )
    protected SimpleQuantity maxDosePerLifetime;

    /**
     * Amount of medication per unit of time.
     */
    @Child(name = "rate", type = {Ratio.class, Range.class, SimpleQuantity.class}, order=12, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Amount of medication per unit of time", formalDefinition="Amount of medication per unit of time." )
    protected Type rate;

    private static final long serialVersionUID = -1392288530L;

  /**
   * Constructor
   */
    public DosageInstruction() {
      super();
    }

    /**
     * @return {@link #sequence} (Indicates the order in which the dosage instructions should be applied or interpreted.). This is the underlying object with id, value and extensions. The accessor "getSequence" gives direct access to the value
     */
    public IntegerType getSequenceElement() { 
      if (this.sequence == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DosageInstruction.sequence");
        else if (Configuration.doAutoCreate())
          this.sequence = new IntegerType(); // bb
      return this.sequence;
    }

    public boolean hasSequenceElement() { 
      return this.sequence != null && !this.sequence.isEmpty();
    }

    public boolean hasSequence() { 
      return this.sequence != null && !this.sequence.isEmpty();
    }

    /**
     * @param value {@link #sequence} (Indicates the order in which the dosage instructions should be applied or interpreted.). This is the underlying object with id, value and extensions. The accessor "getSequence" gives direct access to the value
     */
    public DosageInstruction setSequenceElement(IntegerType value) { 
      this.sequence = value;
      return this;
    }

    /**
     * @return Indicates the order in which the dosage instructions should be applied or interpreted.
     */
    public int getSequence() { 
      return this.sequence == null || this.sequence.isEmpty() ? 0 : this.sequence.getValue();
    }

    /**
     * @param value Indicates the order in which the dosage instructions should be applied or interpreted.
     */
    public DosageInstruction setSequence(int value) { 
        if (this.sequence == null)
          this.sequence = new IntegerType();
        this.sequence.setValue(value);
      return this;
    }

    /**
     * @return {@link #text} (Free text dosage instructions e.g. SIG.). This is the underlying object with id, value and extensions. The accessor "getText" gives direct access to the value
     */
    public StringType getTextElement() { 
      if (this.text == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DosageInstruction.text");
        else if (Configuration.doAutoCreate())
          this.text = new StringType(); // bb
      return this.text;
    }

    public boolean hasTextElement() { 
      return this.text != null && !this.text.isEmpty();
    }

    public boolean hasText() { 
      return this.text != null && !this.text.isEmpty();
    }

    /**
     * @param value {@link #text} (Free text dosage instructions e.g. SIG.). This is the underlying object with id, value and extensions. The accessor "getText" gives direct access to the value
     */
    public DosageInstruction setTextElement(StringType value) { 
      this.text = value;
      return this;
    }

    /**
     * @return Free text dosage instructions e.g. SIG.
     */
    public String getText() { 
      return this.text == null ? null : this.text.getValue();
    }

    /**
     * @param value Free text dosage instructions e.g. SIG.
     */
    public DosageInstruction setText(String value) { 
      if (Utilities.noString(value))
        this.text = null;
      else {
        if (this.text == null)
          this.text = new StringType();
        this.text.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #additionalInstructions} (Supplemental instructions - e.g. "with meals".)
     */
    public List<CodeableConcept> getAdditionalInstructions() { 
      if (this.additionalInstructions == null)
        this.additionalInstructions = new ArrayList<CodeableConcept>();
      return this.additionalInstructions;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public DosageInstruction setAdditionalInstructions(List<CodeableConcept> theAdditionalInstructions) { 
      this.additionalInstructions = theAdditionalInstructions;
      return this;
    }

    public boolean hasAdditionalInstructions() { 
      if (this.additionalInstructions == null)
        return false;
      for (CodeableConcept item : this.additionalInstructions)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CodeableConcept addAdditionalInstructions() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.additionalInstructions == null)
        this.additionalInstructions = new ArrayList<CodeableConcept>();
      this.additionalInstructions.add(t);
      return t;
    }

    public DosageInstruction addAdditionalInstructions(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.additionalInstructions == null)
        this.additionalInstructions = new ArrayList<CodeableConcept>();
      this.additionalInstructions.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #additionalInstructions}, creating it if it does not already exist
     */
    public CodeableConcept getAdditionalInstructionsFirstRep() { 
      if (getAdditionalInstructions().isEmpty()) {
        addAdditionalInstructions();
      }
      return getAdditionalInstructions().get(0);
    }

    /**
     * @return {@link #timing} (When medication should be administered.)
     */
    public Timing getTiming() { 
      if (this.timing == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DosageInstruction.timing");
        else if (Configuration.doAutoCreate())
          this.timing = new Timing(); // cc
      return this.timing;
    }

    public boolean hasTiming() { 
      return this.timing != null && !this.timing.isEmpty();
    }

    /**
     * @param value {@link #timing} (When medication should be administered.)
     */
    public DosageInstruction setTiming(Timing value) { 
      this.timing = value;
      return this;
    }

    /**
     * @return {@link #asNeeded} (Take "as needed" (for x).)
     */
    public Type getAsNeeded() { 
      return this.asNeeded;
    }

    /**
     * @return {@link #asNeeded} (Take "as needed" (for x).)
     */
    public BooleanType getAsNeededBooleanType() throws FHIRException { 
      if (!(this.asNeeded instanceof BooleanType))
        throw new FHIRException("Type mismatch: the type BooleanType was expected, but "+this.asNeeded.getClass().getName()+" was encountered");
      return (BooleanType) this.asNeeded;
    }

    public boolean hasAsNeededBooleanType() { 
      return this.asNeeded instanceof BooleanType;
    }

    /**
     * @return {@link #asNeeded} (Take "as needed" (for x).)
     */
    public CodeableConcept getAsNeededCodeableConcept() throws FHIRException { 
      if (!(this.asNeeded instanceof CodeableConcept))
        throw new FHIRException("Type mismatch: the type CodeableConcept was expected, but "+this.asNeeded.getClass().getName()+" was encountered");
      return (CodeableConcept) this.asNeeded;
    }

    public boolean hasAsNeededCodeableConcept() { 
      return this.asNeeded instanceof CodeableConcept;
    }

    public boolean hasAsNeeded() { 
      return this.asNeeded != null && !this.asNeeded.isEmpty();
    }

    /**
     * @param value {@link #asNeeded} (Take "as needed" (for x).)
     */
    public DosageInstruction setAsNeeded(Type value) { 
      this.asNeeded = value;
      return this;
    }

    /**
     * @return {@link #site} (Body site to administer to.)
     */
    public Type getSite() { 
      return this.site;
    }

    /**
     * @return {@link #site} (Body site to administer to.)
     */
    public CodeableConcept getSiteCodeableConcept() throws FHIRException { 
      if (!(this.site instanceof CodeableConcept))
        throw new FHIRException("Type mismatch: the type CodeableConcept was expected, but "+this.site.getClass().getName()+" was encountered");
      return (CodeableConcept) this.site;
    }

    public boolean hasSiteCodeableConcept() { 
      return this.site instanceof CodeableConcept;
    }

    /**
     * @return {@link #site} (Body site to administer to.)
     */
    public Reference getSiteReference() throws FHIRException { 
      if (!(this.site instanceof Reference))
        throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.site.getClass().getName()+" was encountered");
      return (Reference) this.site;
    }

    public boolean hasSiteReference() { 
      return this.site instanceof Reference;
    }

    public boolean hasSite() { 
      return this.site != null && !this.site.isEmpty();
    }

    /**
     * @param value {@link #site} (Body site to administer to.)
     */
    public DosageInstruction setSite(Type value) { 
      this.site = value;
      return this;
    }

    /**
     * @return {@link #route} (How drug should enter body.)
     */
    public CodeableConcept getRoute() { 
      if (this.route == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DosageInstruction.route");
        else if (Configuration.doAutoCreate())
          this.route = new CodeableConcept(); // cc
      return this.route;
    }

    public boolean hasRoute() { 
      return this.route != null && !this.route.isEmpty();
    }

    /**
     * @param value {@link #route} (How drug should enter body.)
     */
    public DosageInstruction setRoute(CodeableConcept value) { 
      this.route = value;
      return this;
    }

    /**
     * @return {@link #method} (Technique for administering medication.)
     */
    public CodeableConcept getMethod() { 
      if (this.method == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DosageInstruction.method");
        else if (Configuration.doAutoCreate())
          this.method = new CodeableConcept(); // cc
      return this.method;
    }

    public boolean hasMethod() { 
      return this.method != null && !this.method.isEmpty();
    }

    /**
     * @param value {@link #method} (Technique for administering medication.)
     */
    public DosageInstruction setMethod(CodeableConcept value) { 
      this.method = value;
      return this;
    }

    /**
     * @return {@link #dose} (Amount of medication per dose.)
     */
    public Type getDose() { 
      return this.dose;
    }

    /**
     * @return {@link #dose} (Amount of medication per dose.)
     */
    public Range getDoseRange() throws FHIRException { 
      if (!(this.dose instanceof Range))
        throw new FHIRException("Type mismatch: the type Range was expected, but "+this.dose.getClass().getName()+" was encountered");
      return (Range) this.dose;
    }

    public boolean hasDoseRange() { 
      return this.dose instanceof Range;
    }

    /**
     * @return {@link #dose} (Amount of medication per dose.)
     */
    public SimpleQuantity getDoseSimpleQuantity() throws FHIRException { 
      if (!(this.dose instanceof SimpleQuantity))
        throw new FHIRException("Type mismatch: the type SimpleQuantity was expected, but "+this.dose.getClass().getName()+" was encountered");
      return (SimpleQuantity) this.dose;
    }

    public boolean hasDoseSimpleQuantity() { 
      return this.dose instanceof SimpleQuantity;
    }

    public boolean hasDose() { 
      return this.dose != null && !this.dose.isEmpty();
    }

    /**
     * @param value {@link #dose} (Amount of medication per dose.)
     */
    public DosageInstruction setDose(Type value) { 
      this.dose = value;
      return this;
    }

    /**
     * @return {@link #maxDosePerPeriod} (Upper limit on medication per unit of time.)
     */
    public Ratio getMaxDosePerPeriod() { 
      if (this.maxDosePerPeriod == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DosageInstruction.maxDosePerPeriod");
        else if (Configuration.doAutoCreate())
          this.maxDosePerPeriod = new Ratio(); // cc
      return this.maxDosePerPeriod;
    }

    public boolean hasMaxDosePerPeriod() { 
      return this.maxDosePerPeriod != null && !this.maxDosePerPeriod.isEmpty();
    }

    /**
     * @param value {@link #maxDosePerPeriod} (Upper limit on medication per unit of time.)
     */
    public DosageInstruction setMaxDosePerPeriod(Ratio value) { 
      this.maxDosePerPeriod = value;
      return this;
    }

    /**
     * @return {@link #maxDosePerAdministration} (Upper limit on medication per administration.)
     */
    public SimpleQuantity getMaxDosePerAdministration() { 
      if (this.maxDosePerAdministration == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DosageInstruction.maxDosePerAdministration");
        else if (Configuration.doAutoCreate())
          this.maxDosePerAdministration = new SimpleQuantity(); // cc
      return this.maxDosePerAdministration;
    }

    public boolean hasMaxDosePerAdministration() { 
      return this.maxDosePerAdministration != null && !this.maxDosePerAdministration.isEmpty();
    }

    /**
     * @param value {@link #maxDosePerAdministration} (Upper limit on medication per administration.)
     */
    public DosageInstruction setMaxDosePerAdministration(SimpleQuantity value) { 
      this.maxDosePerAdministration = value;
      return this;
    }

    /**
     * @return {@link #maxDosePerLifetime} (Upper limit on medication per lifetime of the patient.)
     */
    public SimpleQuantity getMaxDosePerLifetime() { 
      if (this.maxDosePerLifetime == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DosageInstruction.maxDosePerLifetime");
        else if (Configuration.doAutoCreate())
          this.maxDosePerLifetime = new SimpleQuantity(); // cc
      return this.maxDosePerLifetime;
    }

    public boolean hasMaxDosePerLifetime() { 
      return this.maxDosePerLifetime != null && !this.maxDosePerLifetime.isEmpty();
    }

    /**
     * @param value {@link #maxDosePerLifetime} (Upper limit on medication per lifetime of the patient.)
     */
    public DosageInstruction setMaxDosePerLifetime(SimpleQuantity value) { 
      this.maxDosePerLifetime = value;
      return this;
    }

    /**
     * @return {@link #rate} (Amount of medication per unit of time.)
     */
    public Type getRate() { 
      return this.rate;
    }

    /**
     * @return {@link #rate} (Amount of medication per unit of time.)
     */
    public Ratio getRateRatio() throws FHIRException { 
      if (!(this.rate instanceof Ratio))
        throw new FHIRException("Type mismatch: the type Ratio was expected, but "+this.rate.getClass().getName()+" was encountered");
      return (Ratio) this.rate;
    }

    public boolean hasRateRatio() { 
      return this.rate instanceof Ratio;
    }

    /**
     * @return {@link #rate} (Amount of medication per unit of time.)
     */
    public Range getRateRange() throws FHIRException { 
      if (!(this.rate instanceof Range))
        throw new FHIRException("Type mismatch: the type Range was expected, but "+this.rate.getClass().getName()+" was encountered");
      return (Range) this.rate;
    }

    public boolean hasRateRange() { 
      return this.rate instanceof Range;
    }

    /**
     * @return {@link #rate} (Amount of medication per unit of time.)
     */
    public SimpleQuantity getRateSimpleQuantity() throws FHIRException { 
      if (!(this.rate instanceof SimpleQuantity))
        throw new FHIRException("Type mismatch: the type SimpleQuantity was expected, but "+this.rate.getClass().getName()+" was encountered");
      return (SimpleQuantity) this.rate;
    }

    public boolean hasRateSimpleQuantity() { 
      return this.rate instanceof SimpleQuantity;
    }

    public boolean hasRate() { 
      return this.rate != null && !this.rate.isEmpty();
    }

    /**
     * @param value {@link #rate} (Amount of medication per unit of time.)
     */
    public DosageInstruction setRate(Type value) { 
      this.rate = value;
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("sequence", "integer", "Indicates the order in which the dosage instructions should be applied or interpreted.", 0, java.lang.Integer.MAX_VALUE, sequence));
        childrenList.add(new Property("text", "string", "Free text dosage instructions e.g. SIG.", 0, java.lang.Integer.MAX_VALUE, text));
        childrenList.add(new Property("additionalInstructions", "CodeableConcept", "Supplemental instructions - e.g. \"with meals\".", 0, java.lang.Integer.MAX_VALUE, additionalInstructions));
        childrenList.add(new Property("timing", "Timing", "When medication should be administered.", 0, java.lang.Integer.MAX_VALUE, timing));
        childrenList.add(new Property("asNeeded[x]", "boolean|CodeableConcept", "Take \"as needed\" (for x).", 0, java.lang.Integer.MAX_VALUE, asNeeded));
        childrenList.add(new Property("site[x]", "CodeableConcept|Reference(BodySite)", "Body site to administer to.", 0, java.lang.Integer.MAX_VALUE, site));
        childrenList.add(new Property("route", "CodeableConcept", "How drug should enter body.", 0, java.lang.Integer.MAX_VALUE, route));
        childrenList.add(new Property("method", "CodeableConcept", "Technique for administering medication.", 0, java.lang.Integer.MAX_VALUE, method));
        childrenList.add(new Property("dose[x]", "Range|SimpleQuantity", "Amount of medication per dose.", 0, java.lang.Integer.MAX_VALUE, dose));
        childrenList.add(new Property("maxDosePerPeriod", "Ratio", "Upper limit on medication per unit of time.", 0, java.lang.Integer.MAX_VALUE, maxDosePerPeriod));
        childrenList.add(new Property("maxDosePerAdministration", "SimpleQuantity", "Upper limit on medication per administration.", 0, java.lang.Integer.MAX_VALUE, maxDosePerAdministration));
        childrenList.add(new Property("maxDosePerLifetime", "SimpleQuantity", "Upper limit on medication per lifetime of the patient.", 0, java.lang.Integer.MAX_VALUE, maxDosePerLifetime));
        childrenList.add(new Property("rate[x]", "Ratio|Range|SimpleQuantity", "Amount of medication per unit of time.", 0, java.lang.Integer.MAX_VALUE, rate));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 1349547969: /*sequence*/ return this.sequence == null ? new Base[0] : new Base[] {this.sequence}; // IntegerType
        case 3556653: /*text*/ return this.text == null ? new Base[0] : new Base[] {this.text}; // StringType
        case -1206718612: /*additionalInstructions*/ return this.additionalInstructions == null ? new Base[0] : this.additionalInstructions.toArray(new Base[this.additionalInstructions.size()]); // CodeableConcept
        case -873664438: /*timing*/ return this.timing == null ? new Base[0] : new Base[] {this.timing}; // Timing
        case -1432923513: /*asNeeded*/ return this.asNeeded == null ? new Base[0] : new Base[] {this.asNeeded}; // Type
        case 3530567: /*site*/ return this.site == null ? new Base[0] : new Base[] {this.site}; // Type
        case 108704329: /*route*/ return this.route == null ? new Base[0] : new Base[] {this.route}; // CodeableConcept
        case -1077554975: /*method*/ return this.method == null ? new Base[0] : new Base[] {this.method}; // CodeableConcept
        case 3089437: /*dose*/ return this.dose == null ? new Base[0] : new Base[] {this.dose}; // Type
        case 1506263709: /*maxDosePerPeriod*/ return this.maxDosePerPeriod == null ? new Base[0] : new Base[] {this.maxDosePerPeriod}; // Ratio
        case 2004889914: /*maxDosePerAdministration*/ return this.maxDosePerAdministration == null ? new Base[0] : new Base[] {this.maxDosePerAdministration}; // SimpleQuantity
        case 642099621: /*maxDosePerLifetime*/ return this.maxDosePerLifetime == null ? new Base[0] : new Base[] {this.maxDosePerLifetime}; // SimpleQuantity
        case 3493088: /*rate*/ return this.rate == null ? new Base[0] : new Base[] {this.rate}; // Type
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 1349547969: // sequence
          this.sequence = castToInteger(value); // IntegerType
          break;
        case 3556653: // text
          this.text = castToString(value); // StringType
          break;
        case -1206718612: // additionalInstructions
          this.getAdditionalInstructions().add(castToCodeableConcept(value)); // CodeableConcept
          break;
        case -873664438: // timing
          this.timing = castToTiming(value); // Timing
          break;
        case -1432923513: // asNeeded
          this.asNeeded = castToType(value); // Type
          break;
        case 3530567: // site
          this.site = castToType(value); // Type
          break;
        case 108704329: // route
          this.route = castToCodeableConcept(value); // CodeableConcept
          break;
        case -1077554975: // method
          this.method = castToCodeableConcept(value); // CodeableConcept
          break;
        case 3089437: // dose
          this.dose = castToType(value); // Type
          break;
        case 1506263709: // maxDosePerPeriod
          this.maxDosePerPeriod = castToRatio(value); // Ratio
          break;
        case 2004889914: // maxDosePerAdministration
          this.maxDosePerAdministration = castToSimpleQuantity(value); // SimpleQuantity
          break;
        case 642099621: // maxDosePerLifetime
          this.maxDosePerLifetime = castToSimpleQuantity(value); // SimpleQuantity
          break;
        case 3493088: // rate
          this.rate = castToType(value); // Type
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("sequence"))
          this.sequence = castToInteger(value); // IntegerType
        else if (name.equals("text"))
          this.text = castToString(value); // StringType
        else if (name.equals("additionalInstructions"))
          this.getAdditionalInstructions().add(castToCodeableConcept(value));
        else if (name.equals("timing"))
          this.timing = castToTiming(value); // Timing
        else if (name.equals("asNeeded[x]"))
          this.asNeeded = castToType(value); // Type
        else if (name.equals("site[x]"))
          this.site = castToType(value); // Type
        else if (name.equals("route"))
          this.route = castToCodeableConcept(value); // CodeableConcept
        else if (name.equals("method"))
          this.method = castToCodeableConcept(value); // CodeableConcept
        else if (name.equals("dose[x]"))
          this.dose = castToType(value); // Type
        else if (name.equals("maxDosePerPeriod"))
          this.maxDosePerPeriod = castToRatio(value); // Ratio
        else if (name.equals("maxDosePerAdministration"))
          this.maxDosePerAdministration = castToSimpleQuantity(value); // SimpleQuantity
        else if (name.equals("maxDosePerLifetime"))
          this.maxDosePerLifetime = castToSimpleQuantity(value); // SimpleQuantity
        else if (name.equals("rate[x]"))
          this.rate = castToType(value); // Type
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1349547969: throw new FHIRException("Cannot make property sequence as it is not a complex type"); // IntegerType
        case 3556653: throw new FHIRException("Cannot make property text as it is not a complex type"); // StringType
        case -1206718612:  return addAdditionalInstructions(); // CodeableConcept
        case -873664438:  return getTiming(); // Timing
        case -544329575:  return getAsNeeded(); // Type
        case 2099997657:  return getSite(); // Type
        case 108704329:  return getRoute(); // CodeableConcept
        case -1077554975:  return getMethod(); // CodeableConcept
        case 1843195715:  return getDose(); // Type
        case 1506263709:  return getMaxDosePerPeriod(); // Ratio
        case 2004889914:  return getMaxDosePerAdministration(); // SimpleQuantity
        case 642099621:  return getMaxDosePerLifetime(); // SimpleQuantity
        case 983460768:  return getRate(); // Type
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("sequence")) {
          throw new FHIRException("Cannot call addChild on a primitive type DosageInstruction.sequence");
        }
        else if (name.equals("text")) {
          throw new FHIRException("Cannot call addChild on a primitive type DosageInstruction.text");
        }
        else if (name.equals("additionalInstructions")) {
          return addAdditionalInstructions();
        }
        else if (name.equals("timing")) {
          this.timing = new Timing();
          return this.timing;
        }
        else if (name.equals("asNeededBoolean")) {
          this.asNeeded = new BooleanType();
          return this.asNeeded;
        }
        else if (name.equals("asNeededCodeableConcept")) {
          this.asNeeded = new CodeableConcept();
          return this.asNeeded;
        }
        else if (name.equals("siteCodeableConcept")) {
          this.site = new CodeableConcept();
          return this.site;
        }
        else if (name.equals("siteReference")) {
          this.site = new Reference();
          return this.site;
        }
        else if (name.equals("route")) {
          this.route = new CodeableConcept();
          return this.route;
        }
        else if (name.equals("method")) {
          this.method = new CodeableConcept();
          return this.method;
        }
        else if (name.equals("doseRange")) {
          this.dose = new Range();
          return this.dose;
        }
        else if (name.equals("doseSimpleQuantity")) {
          this.dose = new SimpleQuantity();
          return this.dose;
        }
        else if (name.equals("maxDosePerPeriod")) {
          this.maxDosePerPeriod = new Ratio();
          return this.maxDosePerPeriod;
        }
        else if (name.equals("maxDosePerAdministration")) {
          this.maxDosePerAdministration = new SimpleQuantity();
          return this.maxDosePerAdministration;
        }
        else if (name.equals("maxDosePerLifetime")) {
          this.maxDosePerLifetime = new SimpleQuantity();
          return this.maxDosePerLifetime;
        }
        else if (name.equals("rateRatio")) {
          this.rate = new Ratio();
          return this.rate;
        }
        else if (name.equals("rateRange")) {
          this.rate = new Range();
          return this.rate;
        }
        else if (name.equals("rateSimpleQuantity")) {
          this.rate = new SimpleQuantity();
          return this.rate;
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "DosageInstruction";

  }

      public DosageInstruction copy() {
        DosageInstruction dst = new DosageInstruction();
        copyValues(dst);
        dst.sequence = sequence == null ? null : sequence.copy();
        dst.text = text == null ? null : text.copy();
        if (additionalInstructions != null) {
          dst.additionalInstructions = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : additionalInstructions)
            dst.additionalInstructions.add(i.copy());
        };
        dst.timing = timing == null ? null : timing.copy();
        dst.asNeeded = asNeeded == null ? null : asNeeded.copy();
        dst.site = site == null ? null : site.copy();
        dst.route = route == null ? null : route.copy();
        dst.method = method == null ? null : method.copy();
        dst.dose = dose == null ? null : dose.copy();
        dst.maxDosePerPeriod = maxDosePerPeriod == null ? null : maxDosePerPeriod.copy();
        dst.maxDosePerAdministration = maxDosePerAdministration == null ? null : maxDosePerAdministration.copy();
        dst.maxDosePerLifetime = maxDosePerLifetime == null ? null : maxDosePerLifetime.copy();
        dst.rate = rate == null ? null : rate.copy();
        return dst;
      }

      protected DosageInstruction typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof DosageInstruction))
          return false;
        DosageInstruction o = (DosageInstruction) other;
        return compareDeep(sequence, o.sequence, true) && compareDeep(text, o.text, true) && compareDeep(additionalInstructions, o.additionalInstructions, true)
           && compareDeep(timing, o.timing, true) && compareDeep(asNeeded, o.asNeeded, true) && compareDeep(site, o.site, true)
           && compareDeep(route, o.route, true) && compareDeep(method, o.method, true) && compareDeep(dose, o.dose, true)
           && compareDeep(maxDosePerPeriod, o.maxDosePerPeriod, true) && compareDeep(maxDosePerAdministration, o.maxDosePerAdministration, true)
           && compareDeep(maxDosePerLifetime, o.maxDosePerLifetime, true) && compareDeep(rate, o.rate, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof DosageInstruction))
          return false;
        DosageInstruction o = (DosageInstruction) other;
        return compareValues(sequence, o.sequence, true) && compareValues(text, o.text, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(sequence, text, additionalInstructions
          , timing, asNeeded, site, route, method, dose, maxDosePerPeriod, maxDosePerAdministration
          , maxDosePerLifetime, rate);
      }


}

