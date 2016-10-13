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

// Generated on Thu, Aug 25, 2016 23:04-0400 for FHIR v1.6.0
import java.util.ArrayList;
import java.util.List;

import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.ICompositeType;

import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.model.api.annotation.Description;
/**
 * Specifies clinical metadata that can be used to retrieve, index and/or categorize the knowledge artifact. This metadata can either be specific to the applicable population (e.g., age category, DRG) or the specific context of care (e.g., venue, care setting, provider of care).
 */
@DatatypeDef(name="UsageContext")
public class UsageContext extends Type implements ICompositeType {

    /**
     * The gender of the patient. For this item type, use HL7 administrative gender codes (OID: 2.16.840.1.113883.1.11.1).
     */
    @Child(name = "patientGender", type = {CodeableConcept.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Patient gender", formalDefinition="The gender of the patient. For this item type, use HL7 administrative gender codes (OID: 2.16.840.1.113883.1.11.1)." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/v3-AdministrativeGender")
    protected List<CodeableConcept> patientGender;

    /**
     * A patient demographic category for which this artifact is applicable. Allows specification of age groups using coded values originating from the MeSH Code system (OID: 2.16.840.1.113883.6.177). More specifically, only codes from the AgeGroupObservationValue value set are valid for this field  [2.16.840.1.113883.11.75].
     */
    @Child(name = "patientAgeGroup", type = {CodeableConcept.class}, order=1, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Demographic category", formalDefinition="A patient demographic category for which this artifact is applicable. Allows specification of age groups using coded values originating from the MeSH Code system (OID: 2.16.840.1.113883.6.177). More specifically, only codes from the AgeGroupObservationValue value set are valid for this field  [2.16.840.1.113883.11.75]." )
    protected List<CodeableConcept> patientAgeGroup;

    /**
     * The clinical concept(s) addressed by the artifact.  For example, disease, diagnostic test interpretation, medication ordering. Please refer to the implementation guide on which code system and codes to use.
     */
    @Child(name = "clinicalFocus", type = {CodeableConcept.class}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Clinical concepts addressed", formalDefinition="The clinical concept(s) addressed by the artifact.  For example, disease, diagnostic test interpretation, medication ordering. Please refer to the implementation guide on which code system and codes to use." )
    protected List<CodeableConcept> clinicalFocus;

    /**
     * The user types to which an artifact is targeted.  For example, PCP, Patient, Cardiologist, Behavioral Professional, Oral Health Professional, Prescriber, etc... taken from the NUCC Health Care provider taxonomyCode system (OID: 2.16.840.1.113883.6.101).
     */
    @Child(name = "targetUser", type = {CodeableConcept.class}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Target user type", formalDefinition="The user types to which an artifact is targeted.  For example, PCP, Patient, Cardiologist, Behavioral Professional, Oral Health Professional, Prescriber, etc... taken from the NUCC Health Care provider taxonomyCode system (OID: 2.16.840.1.113883.6.101)." )
    protected List<CodeableConcept> targetUser;

    /**
     * The settings in which the artifact is intended for use.  For example, admission, pre-op, etc.
     */
    @Child(name = "workflowSetting", type = {CodeableConcept.class}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Workflow setting", formalDefinition="The settings in which the artifact is intended for use.  For example, admission, pre-op, etc." )
    protected List<CodeableConcept> workflowSetting;

    /**
     * The context for the clinical task(s) represented by this artifact. Can be any task context represented by the HL7 ActTaskCode value set (OID: 2.16.840.1.113883.1.11.19846). General categories include: order entry, patient documentation and patient information review.
     */
    @Child(name = "workflowTask", type = {CodeableConcept.class}, order=5, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Clinical task context", formalDefinition="The context for the clinical task(s) represented by this artifact. Can be any task context represented by the HL7 ActTaskCode value set (OID: 2.16.840.1.113883.1.11.19846). General categories include: order entry, patient documentation and patient information review." )
    protected List<CodeableConcept> workflowTask;

    /**
     * The venue in which an artifact could be used.  For example, Outpatient, Inpatient, Home, Nursing home. The code value may originate from either the HL7 ActEncounter (OID: 2.16.840.1.113883.1.11.13955) or NUCC non-individual provider codes OID: 2.16.840.1.113883.1.11.19465.
     */
    @Child(name = "clinicalVenue", type = {CodeableConcept.class}, order=6, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Applicable venue", formalDefinition="The venue in which an artifact could be used.  For example, Outpatient, Inpatient, Home, Nursing home. The code value may originate from either the HL7 ActEncounter (OID: 2.16.840.1.113883.1.11.13955) or NUCC non-individual provider codes OID: 2.16.840.1.113883.1.11.19465." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/v3-ActEncounterCode")
    protected List<CodeableConcept> clinicalVenue;

    /**
     * The jurisdiction in which the artifact is intended to be used.
     */
    @Child(name = "jurisdiction", type = {CodeableConcept.class}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Intended jurisdiction", formalDefinition="The jurisdiction in which the artifact is intended to be used." )
    protected List<CodeableConcept> jurisdiction;

    private static final long serialVersionUID = -1962387338L;

  /**
   * Constructor
   */
    public UsageContext() {
      super();
    }

    /**
     * @return {@link #patientGender} (The gender of the patient. For this item type, use HL7 administrative gender codes (OID: 2.16.840.1.113883.1.11.1).)
     */
    public List<CodeableConcept> getPatientGender() { 
      if (this.patientGender == null)
        this.patientGender = new ArrayList<CodeableConcept>();
      return this.patientGender;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public UsageContext setPatientGender(List<CodeableConcept> thePatientGender) { 
      this.patientGender = thePatientGender;
      return this;
    }

    public boolean hasPatientGender() { 
      if (this.patientGender == null)
        return false;
      for (CodeableConcept item : this.patientGender)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CodeableConcept addPatientGender() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.patientGender == null)
        this.patientGender = new ArrayList<CodeableConcept>();
      this.patientGender.add(t);
      return t;
    }

    public UsageContext addPatientGender(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.patientGender == null)
        this.patientGender = new ArrayList<CodeableConcept>();
      this.patientGender.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #patientGender}, creating it if it does not already exist
     */
    public CodeableConcept getPatientGenderFirstRep() { 
      if (getPatientGender().isEmpty()) {
        addPatientGender();
      }
      return getPatientGender().get(0);
    }

    /**
     * @return {@link #patientAgeGroup} (A patient demographic category for which this artifact is applicable. Allows specification of age groups using coded values originating from the MeSH Code system (OID: 2.16.840.1.113883.6.177). More specifically, only codes from the AgeGroupObservationValue value set are valid for this field  [2.16.840.1.113883.11.75].)
     */
    public List<CodeableConcept> getPatientAgeGroup() { 
      if (this.patientAgeGroup == null)
        this.patientAgeGroup = new ArrayList<CodeableConcept>();
      return this.patientAgeGroup;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public UsageContext setPatientAgeGroup(List<CodeableConcept> thePatientAgeGroup) { 
      this.patientAgeGroup = thePatientAgeGroup;
      return this;
    }

    public boolean hasPatientAgeGroup() { 
      if (this.patientAgeGroup == null)
        return false;
      for (CodeableConcept item : this.patientAgeGroup)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CodeableConcept addPatientAgeGroup() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.patientAgeGroup == null)
        this.patientAgeGroup = new ArrayList<CodeableConcept>();
      this.patientAgeGroup.add(t);
      return t;
    }

    public UsageContext addPatientAgeGroup(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.patientAgeGroup == null)
        this.patientAgeGroup = new ArrayList<CodeableConcept>();
      this.patientAgeGroup.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #patientAgeGroup}, creating it if it does not already exist
     */
    public CodeableConcept getPatientAgeGroupFirstRep() { 
      if (getPatientAgeGroup().isEmpty()) {
        addPatientAgeGroup();
      }
      return getPatientAgeGroup().get(0);
    }

    /**
     * @return {@link #clinicalFocus} (The clinical concept(s) addressed by the artifact.  For example, disease, diagnostic test interpretation, medication ordering. Please refer to the implementation guide on which code system and codes to use.)
     */
    public List<CodeableConcept> getClinicalFocus() { 
      if (this.clinicalFocus == null)
        this.clinicalFocus = new ArrayList<CodeableConcept>();
      return this.clinicalFocus;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public UsageContext setClinicalFocus(List<CodeableConcept> theClinicalFocus) { 
      this.clinicalFocus = theClinicalFocus;
      return this;
    }

    public boolean hasClinicalFocus() { 
      if (this.clinicalFocus == null)
        return false;
      for (CodeableConcept item : this.clinicalFocus)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CodeableConcept addClinicalFocus() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.clinicalFocus == null)
        this.clinicalFocus = new ArrayList<CodeableConcept>();
      this.clinicalFocus.add(t);
      return t;
    }

    public UsageContext addClinicalFocus(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.clinicalFocus == null)
        this.clinicalFocus = new ArrayList<CodeableConcept>();
      this.clinicalFocus.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #clinicalFocus}, creating it if it does not already exist
     */
    public CodeableConcept getClinicalFocusFirstRep() { 
      if (getClinicalFocus().isEmpty()) {
        addClinicalFocus();
      }
      return getClinicalFocus().get(0);
    }

    /**
     * @return {@link #targetUser} (The user types to which an artifact is targeted.  For example, PCP, Patient, Cardiologist, Behavioral Professional, Oral Health Professional, Prescriber, etc... taken from the NUCC Health Care provider taxonomyCode system (OID: 2.16.840.1.113883.6.101).)
     */
    public List<CodeableConcept> getTargetUser() { 
      if (this.targetUser == null)
        this.targetUser = new ArrayList<CodeableConcept>();
      return this.targetUser;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public UsageContext setTargetUser(List<CodeableConcept> theTargetUser) { 
      this.targetUser = theTargetUser;
      return this;
    }

    public boolean hasTargetUser() { 
      if (this.targetUser == null)
        return false;
      for (CodeableConcept item : this.targetUser)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CodeableConcept addTargetUser() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.targetUser == null)
        this.targetUser = new ArrayList<CodeableConcept>();
      this.targetUser.add(t);
      return t;
    }

    public UsageContext addTargetUser(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.targetUser == null)
        this.targetUser = new ArrayList<CodeableConcept>();
      this.targetUser.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #targetUser}, creating it if it does not already exist
     */
    public CodeableConcept getTargetUserFirstRep() { 
      if (getTargetUser().isEmpty()) {
        addTargetUser();
      }
      return getTargetUser().get(0);
    }

    /**
     * @return {@link #workflowSetting} (The settings in which the artifact is intended for use.  For example, admission, pre-op, etc.)
     */
    public List<CodeableConcept> getWorkflowSetting() { 
      if (this.workflowSetting == null)
        this.workflowSetting = new ArrayList<CodeableConcept>();
      return this.workflowSetting;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public UsageContext setWorkflowSetting(List<CodeableConcept> theWorkflowSetting) { 
      this.workflowSetting = theWorkflowSetting;
      return this;
    }

    public boolean hasWorkflowSetting() { 
      if (this.workflowSetting == null)
        return false;
      for (CodeableConcept item : this.workflowSetting)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CodeableConcept addWorkflowSetting() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.workflowSetting == null)
        this.workflowSetting = new ArrayList<CodeableConcept>();
      this.workflowSetting.add(t);
      return t;
    }

    public UsageContext addWorkflowSetting(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.workflowSetting == null)
        this.workflowSetting = new ArrayList<CodeableConcept>();
      this.workflowSetting.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #workflowSetting}, creating it if it does not already exist
     */
    public CodeableConcept getWorkflowSettingFirstRep() { 
      if (getWorkflowSetting().isEmpty()) {
        addWorkflowSetting();
      }
      return getWorkflowSetting().get(0);
    }

    /**
     * @return {@link #workflowTask} (The context for the clinical task(s) represented by this artifact. Can be any task context represented by the HL7 ActTaskCode value set (OID: 2.16.840.1.113883.1.11.19846). General categories include: order entry, patient documentation and patient information review.)
     */
    public List<CodeableConcept> getWorkflowTask() { 
      if (this.workflowTask == null)
        this.workflowTask = new ArrayList<CodeableConcept>();
      return this.workflowTask;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public UsageContext setWorkflowTask(List<CodeableConcept> theWorkflowTask) { 
      this.workflowTask = theWorkflowTask;
      return this;
    }

    public boolean hasWorkflowTask() { 
      if (this.workflowTask == null)
        return false;
      for (CodeableConcept item : this.workflowTask)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CodeableConcept addWorkflowTask() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.workflowTask == null)
        this.workflowTask = new ArrayList<CodeableConcept>();
      this.workflowTask.add(t);
      return t;
    }

    public UsageContext addWorkflowTask(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.workflowTask == null)
        this.workflowTask = new ArrayList<CodeableConcept>();
      this.workflowTask.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #workflowTask}, creating it if it does not already exist
     */
    public CodeableConcept getWorkflowTaskFirstRep() { 
      if (getWorkflowTask().isEmpty()) {
        addWorkflowTask();
      }
      return getWorkflowTask().get(0);
    }

    /**
     * @return {@link #clinicalVenue} (The venue in which an artifact could be used.  For example, Outpatient, Inpatient, Home, Nursing home. The code value may originate from either the HL7 ActEncounter (OID: 2.16.840.1.113883.1.11.13955) or NUCC non-individual provider codes OID: 2.16.840.1.113883.1.11.19465.)
     */
    public List<CodeableConcept> getClinicalVenue() { 
      if (this.clinicalVenue == null)
        this.clinicalVenue = new ArrayList<CodeableConcept>();
      return this.clinicalVenue;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public UsageContext setClinicalVenue(List<CodeableConcept> theClinicalVenue) { 
      this.clinicalVenue = theClinicalVenue;
      return this;
    }

    public boolean hasClinicalVenue() { 
      if (this.clinicalVenue == null)
        return false;
      for (CodeableConcept item : this.clinicalVenue)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CodeableConcept addClinicalVenue() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.clinicalVenue == null)
        this.clinicalVenue = new ArrayList<CodeableConcept>();
      this.clinicalVenue.add(t);
      return t;
    }

    public UsageContext addClinicalVenue(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.clinicalVenue == null)
        this.clinicalVenue = new ArrayList<CodeableConcept>();
      this.clinicalVenue.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #clinicalVenue}, creating it if it does not already exist
     */
    public CodeableConcept getClinicalVenueFirstRep() { 
      if (getClinicalVenue().isEmpty()) {
        addClinicalVenue();
      }
      return getClinicalVenue().get(0);
    }

    /**
     * @return {@link #jurisdiction} (The jurisdiction in which the artifact is intended to be used.)
     */
    public List<CodeableConcept> getJurisdiction() { 
      if (this.jurisdiction == null)
        this.jurisdiction = new ArrayList<CodeableConcept>();
      return this.jurisdiction;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public UsageContext setJurisdiction(List<CodeableConcept> theJurisdiction) { 
      this.jurisdiction = theJurisdiction;
      return this;
    }

    public boolean hasJurisdiction() { 
      if (this.jurisdiction == null)
        return false;
      for (CodeableConcept item : this.jurisdiction)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CodeableConcept addJurisdiction() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.jurisdiction == null)
        this.jurisdiction = new ArrayList<CodeableConcept>();
      this.jurisdiction.add(t);
      return t;
    }

    public UsageContext addJurisdiction(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.jurisdiction == null)
        this.jurisdiction = new ArrayList<CodeableConcept>();
      this.jurisdiction.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #jurisdiction}, creating it if it does not already exist
     */
    public CodeableConcept getJurisdictionFirstRep() { 
      if (getJurisdiction().isEmpty()) {
        addJurisdiction();
      }
      return getJurisdiction().get(0);
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("patientGender", "CodeableConcept", "The gender of the patient. For this item type, use HL7 administrative gender codes (OID: 2.16.840.1.113883.1.11.1).", 0, java.lang.Integer.MAX_VALUE, patientGender));
        childrenList.add(new Property("patientAgeGroup", "CodeableConcept", "A patient demographic category for which this artifact is applicable. Allows specification of age groups using coded values originating from the MeSH Code system (OID: 2.16.840.1.113883.6.177). More specifically, only codes from the AgeGroupObservationValue value set are valid for this field  [2.16.840.1.113883.11.75].", 0, java.lang.Integer.MAX_VALUE, patientAgeGroup));
        childrenList.add(new Property("clinicalFocus", "CodeableConcept", "The clinical concept(s) addressed by the artifact.  For example, disease, diagnostic test interpretation, medication ordering. Please refer to the implementation guide on which code system and codes to use.", 0, java.lang.Integer.MAX_VALUE, clinicalFocus));
        childrenList.add(new Property("targetUser", "CodeableConcept", "The user types to which an artifact is targeted.  For example, PCP, Patient, Cardiologist, Behavioral Professional, Oral Health Professional, Prescriber, etc... taken from the NUCC Health Care provider taxonomyCode system (OID: 2.16.840.1.113883.6.101).", 0, java.lang.Integer.MAX_VALUE, targetUser));
        childrenList.add(new Property("workflowSetting", "CodeableConcept", "The settings in which the artifact is intended for use.  For example, admission, pre-op, etc.", 0, java.lang.Integer.MAX_VALUE, workflowSetting));
        childrenList.add(new Property("workflowTask", "CodeableConcept", "The context for the clinical task(s) represented by this artifact. Can be any task context represented by the HL7 ActTaskCode value set (OID: 2.16.840.1.113883.1.11.19846). General categories include: order entry, patient documentation and patient information review.", 0, java.lang.Integer.MAX_VALUE, workflowTask));
        childrenList.add(new Property("clinicalVenue", "CodeableConcept", "The venue in which an artifact could be used.  For example, Outpatient, Inpatient, Home, Nursing home. The code value may originate from either the HL7 ActEncounter (OID: 2.16.840.1.113883.1.11.13955) or NUCC non-individual provider codes OID: 2.16.840.1.113883.1.11.19465.", 0, java.lang.Integer.MAX_VALUE, clinicalVenue));
        childrenList.add(new Property("jurisdiction", "CodeableConcept", "The jurisdiction in which the artifact is intended to be used.", 0, java.lang.Integer.MAX_VALUE, jurisdiction));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -2040417754: /*patientGender*/ return this.patientGender == null ? new Base[0] : this.patientGender.toArray(new Base[this.patientGender.size()]); // CodeableConcept
        case 1582616293: /*patientAgeGroup*/ return this.patientAgeGroup == null ? new Base[0] : this.patientAgeGroup.toArray(new Base[this.patientAgeGroup.size()]); // CodeableConcept
        case 1219842437: /*clinicalFocus*/ return this.clinicalFocus == null ? new Base[0] : this.clinicalFocus.toArray(new Base[this.clinicalFocus.size()]); // CodeableConcept
        case 486646012: /*targetUser*/ return this.targetUser == null ? new Base[0] : this.targetUser.toArray(new Base[this.targetUser.size()]); // CodeableConcept
        case -255039087: /*workflowSetting*/ return this.workflowSetting == null ? new Base[0] : this.workflowSetting.toArray(new Base[this.workflowSetting.size()]); // CodeableConcept
        case 1560512996: /*workflowTask*/ return this.workflowTask == null ? new Base[0] : this.workflowTask.toArray(new Base[this.workflowTask.size()]); // CodeableConcept
        case 1234331420: /*clinicalVenue*/ return this.clinicalVenue == null ? new Base[0] : this.clinicalVenue.toArray(new Base[this.clinicalVenue.size()]); // CodeableConcept
        case -507075711: /*jurisdiction*/ return this.jurisdiction == null ? new Base[0] : this.jurisdiction.toArray(new Base[this.jurisdiction.size()]); // CodeableConcept
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -2040417754: // patientGender
          this.getPatientGender().add(castToCodeableConcept(value)); // CodeableConcept
          break;
        case 1582616293: // patientAgeGroup
          this.getPatientAgeGroup().add(castToCodeableConcept(value)); // CodeableConcept
          break;
        case 1219842437: // clinicalFocus
          this.getClinicalFocus().add(castToCodeableConcept(value)); // CodeableConcept
          break;
        case 486646012: // targetUser
          this.getTargetUser().add(castToCodeableConcept(value)); // CodeableConcept
          break;
        case -255039087: // workflowSetting
          this.getWorkflowSetting().add(castToCodeableConcept(value)); // CodeableConcept
          break;
        case 1560512996: // workflowTask
          this.getWorkflowTask().add(castToCodeableConcept(value)); // CodeableConcept
          break;
        case 1234331420: // clinicalVenue
          this.getClinicalVenue().add(castToCodeableConcept(value)); // CodeableConcept
          break;
        case -507075711: // jurisdiction
          this.getJurisdiction().add(castToCodeableConcept(value)); // CodeableConcept
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("patientGender"))
          this.getPatientGender().add(castToCodeableConcept(value));
        else if (name.equals("patientAgeGroup"))
          this.getPatientAgeGroup().add(castToCodeableConcept(value));
        else if (name.equals("clinicalFocus"))
          this.getClinicalFocus().add(castToCodeableConcept(value));
        else if (name.equals("targetUser"))
          this.getTargetUser().add(castToCodeableConcept(value));
        else if (name.equals("workflowSetting"))
          this.getWorkflowSetting().add(castToCodeableConcept(value));
        else if (name.equals("workflowTask"))
          this.getWorkflowTask().add(castToCodeableConcept(value));
        else if (name.equals("clinicalVenue"))
          this.getClinicalVenue().add(castToCodeableConcept(value));
        else if (name.equals("jurisdiction"))
          this.getJurisdiction().add(castToCodeableConcept(value));
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -2040417754:  return addPatientGender(); // CodeableConcept
        case 1582616293:  return addPatientAgeGroup(); // CodeableConcept
        case 1219842437:  return addClinicalFocus(); // CodeableConcept
        case 486646012:  return addTargetUser(); // CodeableConcept
        case -255039087:  return addWorkflowSetting(); // CodeableConcept
        case 1560512996:  return addWorkflowTask(); // CodeableConcept
        case 1234331420:  return addClinicalVenue(); // CodeableConcept
        case -507075711:  return addJurisdiction(); // CodeableConcept
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("patientGender")) {
          return addPatientGender();
        }
        else if (name.equals("patientAgeGroup")) {
          return addPatientAgeGroup();
        }
        else if (name.equals("clinicalFocus")) {
          return addClinicalFocus();
        }
        else if (name.equals("targetUser")) {
          return addTargetUser();
        }
        else if (name.equals("workflowSetting")) {
          return addWorkflowSetting();
        }
        else if (name.equals("workflowTask")) {
          return addWorkflowTask();
        }
        else if (name.equals("clinicalVenue")) {
          return addClinicalVenue();
        }
        else if (name.equals("jurisdiction")) {
          return addJurisdiction();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "UsageContext";

  }

      public UsageContext copy() {
        UsageContext dst = new UsageContext();
        copyValues(dst);
        if (patientGender != null) {
          dst.patientGender = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : patientGender)
            dst.patientGender.add(i.copy());
        };
        if (patientAgeGroup != null) {
          dst.patientAgeGroup = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : patientAgeGroup)
            dst.patientAgeGroup.add(i.copy());
        };
        if (clinicalFocus != null) {
          dst.clinicalFocus = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : clinicalFocus)
            dst.clinicalFocus.add(i.copy());
        };
        if (targetUser != null) {
          dst.targetUser = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : targetUser)
            dst.targetUser.add(i.copy());
        };
        if (workflowSetting != null) {
          dst.workflowSetting = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : workflowSetting)
            dst.workflowSetting.add(i.copy());
        };
        if (workflowTask != null) {
          dst.workflowTask = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : workflowTask)
            dst.workflowTask.add(i.copy());
        };
        if (clinicalVenue != null) {
          dst.clinicalVenue = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : clinicalVenue)
            dst.clinicalVenue.add(i.copy());
        };
        if (jurisdiction != null) {
          dst.jurisdiction = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : jurisdiction)
            dst.jurisdiction.add(i.copy());
        };
        return dst;
      }

      protected UsageContext typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof UsageContext))
          return false;
        UsageContext o = (UsageContext) other;
        return compareDeep(patientGender, o.patientGender, true) && compareDeep(patientAgeGroup, o.patientAgeGroup, true)
           && compareDeep(clinicalFocus, o.clinicalFocus, true) && compareDeep(targetUser, o.targetUser, true)
           && compareDeep(workflowSetting, o.workflowSetting, true) && compareDeep(workflowTask, o.workflowTask, true)
           && compareDeep(clinicalVenue, o.clinicalVenue, true) && compareDeep(jurisdiction, o.jurisdiction, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof UsageContext))
          return false;
        UsageContext o = (UsageContext) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(patientGender, patientAgeGroup
          , clinicalFocus, targetUser, workflowSetting, workflowTask, clinicalVenue, jurisdiction
          );
      }


}

