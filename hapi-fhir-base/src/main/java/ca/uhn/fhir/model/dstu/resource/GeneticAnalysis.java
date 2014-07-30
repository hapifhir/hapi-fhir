















package ca.uhn.fhir.model.dstu.resource;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 University Health Network
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


import java.util.Date;
import java.util.List;

import ca.uhn.fhir.model.api.BaseIdentifiableElement;
import ca.uhn.fhir.model.api.BaseResource;
import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.IResourceBlock;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.model.api.annotation.Block;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
import ca.uhn.fhir.model.dstu.composite.CodingDt;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.primitive.DateDt;
import ca.uhn.fhir.model.primitive.IntegerDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.gclient.DateClientParam;
import ca.uhn.fhir.rest.gclient.ReferenceClientParam;


/**
 * HAPI/FHIR <b>GeneticAnalysis</b> Resource
 * (Analysis of a patient's genetic test)
 *
 * <p>
 * <b>Definition:</b>
 * Analysis of a patient's genetic test
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * 
 * </p> 
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/GeneticAnalysis">http://hl7.org/fhir/profiles/GeneticAnalysis</a> 
 * </p>
 *
 */
@ResourceDef(name="GeneticAnalysis", profile="http://hl7.org/fhir/profiles/GeneticAnalysis", id="geneticanalysis")
public class GeneticAnalysis extends BaseResource implements IResource {

	/**
	 * Search parameter constant for <b>subject</b>
	 * <p>
	 * Description: <b>Subject of the analysis</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>GeneticAnalysis.subject</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="subject", path="GeneticAnalysis.subject", description="Subject of the analysis", type="reference"  )
	public static final String SP_SUBJECT = "subject";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>subject</b>
	 * <p>
	 * Description: <b>Subject of the analysis</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>GeneticAnalysis.subject</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam SUBJECT = new ReferenceClientParam(SP_SUBJECT);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>GeneticAnalysis.subject</b>".
	 */
	public static final Include INCLUDE_SUBJECT = new Include("GeneticAnalysis.subject");

	/**
	 * Search parameter constant for <b>author</b>
	 * <p>
	 * Description: <b>Author of the analysis</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>GeneticAnalysis.author</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="author", path="GeneticAnalysis.author", description="Author of the analysis", type="reference"  )
	public static final String SP_AUTHOR = "author";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>author</b>
	 * <p>
	 * Description: <b>Author of the analysis</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>GeneticAnalysis.author</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam AUTHOR = new ReferenceClientParam(SP_AUTHOR);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>GeneticAnalysis.author</b>".
	 */
	public static final Include INCLUDE_AUTHOR = new Include("GeneticAnalysis.author");

	/**
	 * Search parameter constant for <b>date</b>
	 * <p>
	 * Description: <b>Date when result of the analysis is uploaded</b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>GeneticAnalysis.date</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="date", path="GeneticAnalysis.date", description="Date when result of the analysis is uploaded", type="date"  )
	public static final String SP_DATE = "date";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>date</b>
	 * <p>
	 * Description: <b>Date when result of the analysis is uploaded</b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>GeneticAnalysis.date</b><br/>
	 * </p>
	 */
	public static final DateClientParam DATE = new DateClientParam(SP_DATE);


	@Child(name="subject", order=0, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Patient.class	})
	@Description(
		shortDefinition="Subject of the analysis",
		formalDefinition="Subject of the analysis"
	)
	private ResourceReferenceDt mySubject;
	
	@Child(name="author", order=1, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Practitioner.class	})
	@Description(
		shortDefinition="Author of the analysis",
		formalDefinition="Author of the analysis"
	)
	private ResourceReferenceDt myAuthor;
	
	@Child(name="date", type=DateDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="Date when result of the analysis is updated",
		formalDefinition="Date when result of the analysis is updated"
	)
	private DateDt myDate;
	
	@Child(name="geneticAnalysisSummary", order=3, min=1, max=1)	
	@Description(
		shortDefinition="Summary of the analysis",
		formalDefinition="Summary of the analysis"
	)
	private GeneticAnalysisSummary myGeneticAnalysisSummary;
	
	@Child(name="dnaRegionAnalysisTestCoverage", order=4, min=0, max=1)	
	@Description(
		shortDefinition="Coverage of the genetic test",
		formalDefinition="Coverage of the genetic test"
	)
	private DnaRegionAnalysisTestCoverage myDnaRegionAnalysisTestCoverage;
	
	@Child(name="geneticAnalysisDiscreteResult", order=5, min=0, max=1)	
	@Description(
		shortDefinition="Genetic analysis discrete result",
		formalDefinition="Genetic analysis discrete result"
	)
	private GeneticAnalysisDiscreteResult myGeneticAnalysisDiscreteResult;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  mySubject,  myAuthor,  myDate,  myGeneticAnalysisSummary,  myDnaRegionAnalysisTestCoverage,  myGeneticAnalysisDiscreteResult);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, mySubject, myAuthor, myDate, myGeneticAnalysisSummary, myDnaRegionAnalysisTestCoverage, myGeneticAnalysisDiscreteResult);
	}

	/**
	 * Gets the value(s) for <b>subject</b> (Subject of the analysis).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Subject of the analysis
     * </p> 
	 */
	public ResourceReferenceDt getSubject() {  
		if (mySubject == null) {
			mySubject = new ResourceReferenceDt();
		}
		return mySubject;
	}

	/**
	 * Sets the value(s) for <b>subject</b> (Subject of the analysis)
	 *
     * <p>
     * <b>Definition:</b>
     * Subject of the analysis
     * </p> 
	 */
	public GeneticAnalysis setSubject(ResourceReferenceDt theValue) {
		mySubject = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>author</b> (Author of the analysis).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Author of the analysis
     * </p> 
	 */
	public ResourceReferenceDt getAuthor() {  
		if (myAuthor == null) {
			myAuthor = new ResourceReferenceDt();
		}
		return myAuthor;
	}

	/**
	 * Sets the value(s) for <b>author</b> (Author of the analysis)
	 *
     * <p>
     * <b>Definition:</b>
     * Author of the analysis
     * </p> 
	 */
	public GeneticAnalysis setAuthor(ResourceReferenceDt theValue) {
		myAuthor = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>date</b> (Date when result of the analysis is updated).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Date when result of the analysis is updated
     * </p> 
	 */
	public DateDt getDate() {  
		if (myDate == null) {
			myDate = new DateDt();
		}
		return myDate;
	}

	/**
	 * Sets the value(s) for <b>date</b> (Date when result of the analysis is updated)
	 *
     * <p>
     * <b>Definition:</b>
     * Date when result of the analysis is updated
     * </p> 
	 */
	public GeneticAnalysis setDate(DateDt theValue) {
		myDate = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>date</b> (Date when result of the analysis is updated)
	 *
     * <p>
     * <b>Definition:</b>
     * Date when result of the analysis is updated
     * </p> 
	 */
	public GeneticAnalysis setDate( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myDate = new DateDt(theDate, thePrecision); 
		return this; 
	}

	/**
	 * Sets the value for <b>date</b> (Date when result of the analysis is updated)
	 *
     * <p>
     * <b>Definition:</b>
     * Date when result of the analysis is updated
     * </p> 
	 */
	public GeneticAnalysis setDateWithDayPrecision( Date theDate) {
		myDate = new DateDt(theDate); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>geneticAnalysisSummary</b> (Summary of the analysis).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Summary of the analysis
     * </p> 
	 */
	public GeneticAnalysisSummary getGeneticAnalysisSummary() {  
		if (myGeneticAnalysisSummary == null) {
			myGeneticAnalysisSummary = new GeneticAnalysisSummary();
		}
		return myGeneticAnalysisSummary;
	}

	/**
	 * Sets the value(s) for <b>geneticAnalysisSummary</b> (Summary of the analysis)
	 *
     * <p>
     * <b>Definition:</b>
     * Summary of the analysis
     * </p> 
	 */
	public GeneticAnalysis setGeneticAnalysisSummary(GeneticAnalysisSummary theValue) {
		myGeneticAnalysisSummary = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>dnaRegionAnalysisTestCoverage</b> (Coverage of the genetic test).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Coverage of the genetic test
     * </p> 
	 */
	public DnaRegionAnalysisTestCoverage getDnaRegionAnalysisTestCoverage() {  
		if (myDnaRegionAnalysisTestCoverage == null) {
			myDnaRegionAnalysisTestCoverage = new DnaRegionAnalysisTestCoverage();
		}
		return myDnaRegionAnalysisTestCoverage;
	}

	/**
	 * Sets the value(s) for <b>dnaRegionAnalysisTestCoverage</b> (Coverage of the genetic test)
	 *
     * <p>
     * <b>Definition:</b>
     * Coverage of the genetic test
     * </p> 
	 */
	public GeneticAnalysis setDnaRegionAnalysisTestCoverage(DnaRegionAnalysisTestCoverage theValue) {
		myDnaRegionAnalysisTestCoverage = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>geneticAnalysisDiscreteResult</b> (Genetic analysis discrete result).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Genetic analysis discrete result
     * </p> 
	 */
	public GeneticAnalysisDiscreteResult getGeneticAnalysisDiscreteResult() {  
		if (myGeneticAnalysisDiscreteResult == null) {
			myGeneticAnalysisDiscreteResult = new GeneticAnalysisDiscreteResult();
		}
		return myGeneticAnalysisDiscreteResult;
	}

	/**
	 * Sets the value(s) for <b>geneticAnalysisDiscreteResult</b> (Genetic analysis discrete result)
	 *
     * <p>
     * <b>Definition:</b>
     * Genetic analysis discrete result
     * </p> 
	 */
	public GeneticAnalysis setGeneticAnalysisDiscreteResult(GeneticAnalysisDiscreteResult theValue) {
		myGeneticAnalysisDiscreteResult = theValue;
		return this;
	}

  
	/**
	 * Block class for child element: <b>GeneticAnalysis.geneticAnalysisSummary</b> (Summary of the analysis)
	 *
     * <p>
     * <b>Definition:</b>
     * Summary of the analysis
     * </p> 
	 */
	@Block()	
	public static class GeneticAnalysisSummary extends BaseIdentifiableElement implements IResourceBlock {
	
	@Child(name="geneticDiseaseAssessed", type=CodingDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="Genetic disease being assesed",
		formalDefinition="Genetic disease being assesed"
	)
	private CodingDt myGeneticDiseaseAssessed;
	
	@Child(name="medicationAssesed", type=CodingDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="Medication being assesed",
		formalDefinition="Medication being assesed"
	)
	private CodingDt myMedicationAssesed;
	
	@Child(name="genomicSourceClass", type=CodingDt.class, order=2, min=1, max=1)	
	@Description(
		shortDefinition="Class of the source of sample",
		formalDefinition="Class of the source of sample"
	)
	private CodingDt myGenomicSourceClass;
	
	@Child(name="geneticDiseaseAnalysisOverallInterpretation", type=CodingDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="Overall interpretation of the patient's genotype on the genetic disease being assesed",
		formalDefinition="Overall interpretation of the patient's genotype on the genetic disease being assesed"
	)
	private CodingDt myGeneticDiseaseAnalysisOverallInterpretation;
	
	@Child(name="geneticDiseaseAnalysisOverallCarrierInterpertation", type=CodingDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="Carrier status of the patietn",
		formalDefinition="Carrier status of the patietn"
	)
	private CodingDt myGeneticDiseaseAnalysisOverallCarrierInterpertation;
	
	@Child(name="drugEfficacyAnalysisOverallInterpretation", type=CodingDt.class, order=5, min=0, max=1)	
	@Description(
		shortDefinition="Analysis on the efficacy of the drug being assessed",
		formalDefinition="Analysis on the efficacy of the drug being assessed"
	)
	private CodingDt myDrugEfficacyAnalysisOverallInterpretation;
	
	@Child(name="geneticAnalysisSummaryReport", type=StringDt.class, order=6, min=0, max=1)	
	@Description(
		shortDefinition="Summary of the analysis",
		formalDefinition="Summary of the analysis"
	)
	private StringDt myGeneticAnalysisSummaryReport;
	
	@Child(name="reasonForStudyAdditionalNote", type=StringDt.class, order=7, min=0, max=1)	
	@Description(
		shortDefinition="Additional notes",
		formalDefinition="Additional notes"
	)
	private StringDt myReasonForStudyAdditionalNote;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myGeneticDiseaseAssessed,  myMedicationAssesed,  myGenomicSourceClass,  myGeneticDiseaseAnalysisOverallInterpretation,  myGeneticDiseaseAnalysisOverallCarrierInterpertation,  myDrugEfficacyAnalysisOverallInterpretation,  myGeneticAnalysisSummaryReport,  myReasonForStudyAdditionalNote);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myGeneticDiseaseAssessed, myMedicationAssesed, myGenomicSourceClass, myGeneticDiseaseAnalysisOverallInterpretation, myGeneticDiseaseAnalysisOverallCarrierInterpertation, myDrugEfficacyAnalysisOverallInterpretation, myGeneticAnalysisSummaryReport, myReasonForStudyAdditionalNote);
	}

	/**
	 * Gets the value(s) for <b>geneticDiseaseAssessed</b> (Genetic disease being assesed).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Genetic disease being assesed
     * </p> 
	 */
	public CodingDt getGeneticDiseaseAssessed() {  
		if (myGeneticDiseaseAssessed == null) {
			myGeneticDiseaseAssessed = new CodingDt();
		}
		return myGeneticDiseaseAssessed;
	}

	/**
	 * Sets the value(s) for <b>geneticDiseaseAssessed</b> (Genetic disease being assesed)
	 *
     * <p>
     * <b>Definition:</b>
     * Genetic disease being assesed
     * </p> 
	 */
	public GeneticAnalysisSummary setGeneticDiseaseAssessed(CodingDt theValue) {
		myGeneticDiseaseAssessed = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>medicationAssesed</b> (Medication being assesed).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Medication being assesed
     * </p> 
	 */
	public CodingDt getMedicationAssesed() {  
		if (myMedicationAssesed == null) {
			myMedicationAssesed = new CodingDt();
		}
		return myMedicationAssesed;
	}

	/**
	 * Sets the value(s) for <b>medicationAssesed</b> (Medication being assesed)
	 *
     * <p>
     * <b>Definition:</b>
     * Medication being assesed
     * </p> 
	 */
	public GeneticAnalysisSummary setMedicationAssesed(CodingDt theValue) {
		myMedicationAssesed = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>genomicSourceClass</b> (Class of the source of sample).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Class of the source of sample
     * </p> 
	 */
	public CodingDt getGenomicSourceClass() {  
		if (myGenomicSourceClass == null) {
			myGenomicSourceClass = new CodingDt();
		}
		return myGenomicSourceClass;
	}

	/**
	 * Sets the value(s) for <b>genomicSourceClass</b> (Class of the source of sample)
	 *
     * <p>
     * <b>Definition:</b>
     * Class of the source of sample
     * </p> 
	 */
	public GeneticAnalysisSummary setGenomicSourceClass(CodingDt theValue) {
		myGenomicSourceClass = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>geneticDiseaseAnalysisOverallInterpretation</b> (Overall interpretation of the patient's genotype on the genetic disease being assesed).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Overall interpretation of the patient's genotype on the genetic disease being assesed
     * </p> 
	 */
	public CodingDt getGeneticDiseaseAnalysisOverallInterpretation() {  
		if (myGeneticDiseaseAnalysisOverallInterpretation == null) {
			myGeneticDiseaseAnalysisOverallInterpretation = new CodingDt();
		}
		return myGeneticDiseaseAnalysisOverallInterpretation;
	}

	/**
	 * Sets the value(s) for <b>geneticDiseaseAnalysisOverallInterpretation</b> (Overall interpretation of the patient's genotype on the genetic disease being assesed)
	 *
     * <p>
     * <b>Definition:</b>
     * Overall interpretation of the patient's genotype on the genetic disease being assesed
     * </p> 
	 */
	public GeneticAnalysisSummary setGeneticDiseaseAnalysisOverallInterpretation(CodingDt theValue) {
		myGeneticDiseaseAnalysisOverallInterpretation = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>geneticDiseaseAnalysisOverallCarrierInterpertation</b> (Carrier status of the patietn).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Carrier status of the patietn
     * </p> 
	 */
	public CodingDt getGeneticDiseaseAnalysisOverallCarrierInterpertation() {  
		if (myGeneticDiseaseAnalysisOverallCarrierInterpertation == null) {
			myGeneticDiseaseAnalysisOverallCarrierInterpertation = new CodingDt();
		}
		return myGeneticDiseaseAnalysisOverallCarrierInterpertation;
	}

	/**
	 * Sets the value(s) for <b>geneticDiseaseAnalysisOverallCarrierInterpertation</b> (Carrier status of the patietn)
	 *
     * <p>
     * <b>Definition:</b>
     * Carrier status of the patietn
     * </p> 
	 */
	public GeneticAnalysisSummary setGeneticDiseaseAnalysisOverallCarrierInterpertation(CodingDt theValue) {
		myGeneticDiseaseAnalysisOverallCarrierInterpertation = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>drugEfficacyAnalysisOverallInterpretation</b> (Analysis on the efficacy of the drug being assessed).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Analysis on the efficacy of the drug being assessed
     * </p> 
	 */
	public CodingDt getDrugEfficacyAnalysisOverallInterpretation() {  
		if (myDrugEfficacyAnalysisOverallInterpretation == null) {
			myDrugEfficacyAnalysisOverallInterpretation = new CodingDt();
		}
		return myDrugEfficacyAnalysisOverallInterpretation;
	}

	/**
	 * Sets the value(s) for <b>drugEfficacyAnalysisOverallInterpretation</b> (Analysis on the efficacy of the drug being assessed)
	 *
     * <p>
     * <b>Definition:</b>
     * Analysis on the efficacy of the drug being assessed
     * </p> 
	 */
	public GeneticAnalysisSummary setDrugEfficacyAnalysisOverallInterpretation(CodingDt theValue) {
		myDrugEfficacyAnalysisOverallInterpretation = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>geneticAnalysisSummaryReport</b> (Summary of the analysis).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Summary of the analysis
     * </p> 
	 */
	public StringDt getGeneticAnalysisSummaryReport() {  
		if (myGeneticAnalysisSummaryReport == null) {
			myGeneticAnalysisSummaryReport = new StringDt();
		}
		return myGeneticAnalysisSummaryReport;
	}

	/**
	 * Sets the value(s) for <b>geneticAnalysisSummaryReport</b> (Summary of the analysis)
	 *
     * <p>
     * <b>Definition:</b>
     * Summary of the analysis
     * </p> 
	 */
	public GeneticAnalysisSummary setGeneticAnalysisSummaryReport(StringDt theValue) {
		myGeneticAnalysisSummaryReport = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>geneticAnalysisSummaryReport</b> (Summary of the analysis)
	 *
     * <p>
     * <b>Definition:</b>
     * Summary of the analysis
     * </p> 
	 */
	public GeneticAnalysisSummary setGeneticAnalysisSummaryReport( String theString) {
		myGeneticAnalysisSummaryReport = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>reasonForStudyAdditionalNote</b> (Additional notes).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Additional notes
     * </p> 
	 */
	public StringDt getReasonForStudyAdditionalNote() {  
		if (myReasonForStudyAdditionalNote == null) {
			myReasonForStudyAdditionalNote = new StringDt();
		}
		return myReasonForStudyAdditionalNote;
	}

	/**
	 * Sets the value(s) for <b>reasonForStudyAdditionalNote</b> (Additional notes)
	 *
     * <p>
     * <b>Definition:</b>
     * Additional notes
     * </p> 
	 */
	public GeneticAnalysisSummary setReasonForStudyAdditionalNote(StringDt theValue) {
		myReasonForStudyAdditionalNote = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>reasonForStudyAdditionalNote</b> (Additional notes)
	 *
     * <p>
     * <b>Definition:</b>
     * Additional notes
     * </p> 
	 */
	public GeneticAnalysisSummary setReasonForStudyAdditionalNote( String theString) {
		myReasonForStudyAdditionalNote = new StringDt(theString); 
		return this; 
	}

 

	}


	/**
	 * Block class for child element: <b>GeneticAnalysis.dnaRegionAnalysisTestCoverage</b> (Coverage of the genetic test)
	 *
     * <p>
     * <b>Definition:</b>
     * Coverage of the genetic test
     * </p> 
	 */
	@Block()	
	public static class DnaRegionAnalysisTestCoverage extends BaseIdentifiableElement implements IResourceBlock {
	
	@Child(name="dnaRegionOfInterest", order=0, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="DNA studied",
		formalDefinition="DNA studied"
	)
	private java.util.List<DnaRegionAnalysisTestCoverageDnaRegionOfInterest> myDnaRegionOfInterest;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myDnaRegionOfInterest);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myDnaRegionOfInterest);
	}

	/**
	 * Gets the value(s) for <b>dnaRegionOfInterest</b> (DNA studied).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * DNA studied
     * </p> 
	 */
	public java.util.List<DnaRegionAnalysisTestCoverageDnaRegionOfInterest> getDnaRegionOfInterest() {  
		if (myDnaRegionOfInterest == null) {
			myDnaRegionOfInterest = new java.util.ArrayList<DnaRegionAnalysisTestCoverageDnaRegionOfInterest>();
		}
		return myDnaRegionOfInterest;
	}

	/**
	 * Sets the value(s) for <b>dnaRegionOfInterest</b> (DNA studied)
	 *
     * <p>
     * <b>Definition:</b>
     * DNA studied
     * </p> 
	 */
	public DnaRegionAnalysisTestCoverage setDnaRegionOfInterest(java.util.List<DnaRegionAnalysisTestCoverageDnaRegionOfInterest> theValue) {
		myDnaRegionOfInterest = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>dnaRegionOfInterest</b> (DNA studied)
	 *
     * <p>
     * <b>Definition:</b>
     * DNA studied
     * </p> 
	 */
	public DnaRegionAnalysisTestCoverageDnaRegionOfInterest addDnaRegionOfInterest() {
		DnaRegionAnalysisTestCoverageDnaRegionOfInterest newType = new DnaRegionAnalysisTestCoverageDnaRegionOfInterest();
		getDnaRegionOfInterest().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>dnaRegionOfInterest</b> (DNA studied),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * DNA studied
     * </p> 
	 */
	public DnaRegionAnalysisTestCoverageDnaRegionOfInterest getDnaRegionOfInterestFirstRep() {
		if (getDnaRegionOfInterest().isEmpty()) {
			return addDnaRegionOfInterest();
		}
		return getDnaRegionOfInterest().get(0); 
	}
  

	}

	/**
	 * Block class for child element: <b>GeneticAnalysis.dnaRegionAnalysisTestCoverage.dnaRegionOfInterest</b> (DNA studied)
	 *
     * <p>
     * <b>Definition:</b>
     * DNA studied
     * </p> 
	 */
	@Block()	
	public static class DnaRegionAnalysisTestCoverageDnaRegionOfInterest extends BaseIdentifiableElement implements IResourceBlock {
	
	@Child(name="genomicReferenceSequenceIdentifier", type=StringDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="Genomic reference sequence identifier",
		formalDefinition="Identifier of the sequence represented in NCBI genomic nucleotide RefSeq IDs with their version number"
	)
	private StringDt myGenomicReferenceSequenceIdentifier;
	
	@Child(name="regionOfInterestStart", type=IntegerDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="Start position of the DNA region of interest",
		formalDefinition="Start position of the DNA region of interest"
	)
	private IntegerDt myRegionOfInterestStart;
	
	@Child(name="regionOfInterestStop", type=IntegerDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="End position of the DNA region of interest",
		formalDefinition="End position of the DNA region of interest"
	)
	private IntegerDt myRegionOfInterestStop;
	
	@Child(name="referenceNucleotide", type=StringDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="Genotype of the region in reference genome",
		formalDefinition="Genotype of the region in reference genome"
	)
	private StringDt myReferenceNucleotide;
	
	@Child(name="variableNucleotide", type=StringDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="The patient's genotype in the region",
		formalDefinition="The patient's genotype in the region"
	)
	private StringDt myVariableNucleotide;
	
	@Child(name="genechipId", type=StringDt.class, order=5, min=0, max=1)	
	@Description(
		shortDefinition="ID of the genechip",
		formalDefinition="ID of the genechip"
	)
	private StringDt myGenechipId;
	
	@Child(name="genechipManufacturerId", type=StringDt.class, order=6, min=0, max=1)	
	@Description(
		shortDefinition="ID of manufacturer of the genechip",
		formalDefinition="ID of manufacturer of the genechip"
	)
	private StringDt myGenechipManufacturerId;
	
	@Child(name="genechipVersion", type=StringDt.class, order=7, min=0, max=1)	
	@Description(
		shortDefinition="Version of the genechip",
		formalDefinition="Version of the genechip"
	)
	private StringDt myGenechipVersion;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myGenomicReferenceSequenceIdentifier,  myRegionOfInterestStart,  myRegionOfInterestStop,  myReferenceNucleotide,  myVariableNucleotide,  myGenechipId,  myGenechipManufacturerId,  myGenechipVersion);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myGenomicReferenceSequenceIdentifier, myRegionOfInterestStart, myRegionOfInterestStop, myReferenceNucleotide, myVariableNucleotide, myGenechipId, myGenechipManufacturerId, myGenechipVersion);
	}

	/**
	 * Gets the value(s) for <b>genomicReferenceSequenceIdentifier</b> (Genomic reference sequence identifier).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier of the sequence represented in NCBI genomic nucleotide RefSeq IDs with their version number
     * </p> 
	 */
	public StringDt getGenomicReferenceSequenceIdentifier() {  
		if (myGenomicReferenceSequenceIdentifier == null) {
			myGenomicReferenceSequenceIdentifier = new StringDt();
		}
		return myGenomicReferenceSequenceIdentifier;
	}

	/**
	 * Sets the value(s) for <b>genomicReferenceSequenceIdentifier</b> (Genomic reference sequence identifier)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier of the sequence represented in NCBI genomic nucleotide RefSeq IDs with their version number
     * </p> 
	 */
	public DnaRegionAnalysisTestCoverageDnaRegionOfInterest setGenomicReferenceSequenceIdentifier(StringDt theValue) {
		myGenomicReferenceSequenceIdentifier = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>genomicReferenceSequenceIdentifier</b> (Genomic reference sequence identifier)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier of the sequence represented in NCBI genomic nucleotide RefSeq IDs with their version number
     * </p> 
	 */
	public DnaRegionAnalysisTestCoverageDnaRegionOfInterest setGenomicReferenceSequenceIdentifier( String theString) {
		myGenomicReferenceSequenceIdentifier = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>regionOfInterestStart</b> (Start position of the DNA region of interest).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Start position of the DNA region of interest
     * </p> 
	 */
	public IntegerDt getRegionOfInterestStart() {  
		if (myRegionOfInterestStart == null) {
			myRegionOfInterestStart = new IntegerDt();
		}
		return myRegionOfInterestStart;
	}

	/**
	 * Sets the value(s) for <b>regionOfInterestStart</b> (Start position of the DNA region of interest)
	 *
     * <p>
     * <b>Definition:</b>
     * Start position of the DNA region of interest
     * </p> 
	 */
	public DnaRegionAnalysisTestCoverageDnaRegionOfInterest setRegionOfInterestStart(IntegerDt theValue) {
		myRegionOfInterestStart = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>regionOfInterestStart</b> (Start position of the DNA region of interest)
	 *
     * <p>
     * <b>Definition:</b>
     * Start position of the DNA region of interest
     * </p> 
	 */
	public DnaRegionAnalysisTestCoverageDnaRegionOfInterest setRegionOfInterestStart( int theInteger) {
		myRegionOfInterestStart = new IntegerDt(theInteger); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>regionOfInterestStop</b> (End position of the DNA region of interest).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * End position of the DNA region of interest
     * </p> 
	 */
	public IntegerDt getRegionOfInterestStop() {  
		if (myRegionOfInterestStop == null) {
			myRegionOfInterestStop = new IntegerDt();
		}
		return myRegionOfInterestStop;
	}

	/**
	 * Sets the value(s) for <b>regionOfInterestStop</b> (End position of the DNA region of interest)
	 *
     * <p>
     * <b>Definition:</b>
     * End position of the DNA region of interest
     * </p> 
	 */
	public DnaRegionAnalysisTestCoverageDnaRegionOfInterest setRegionOfInterestStop(IntegerDt theValue) {
		myRegionOfInterestStop = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>regionOfInterestStop</b> (End position of the DNA region of interest)
	 *
     * <p>
     * <b>Definition:</b>
     * End position of the DNA region of interest
     * </p> 
	 */
	public DnaRegionAnalysisTestCoverageDnaRegionOfInterest setRegionOfInterestStop( int theInteger) {
		myRegionOfInterestStop = new IntegerDt(theInteger); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>referenceNucleotide</b> (Genotype of the region in reference genome).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Genotype of the region in reference genome
     * </p> 
	 */
	public StringDt getReferenceNucleotide() {  
		if (myReferenceNucleotide == null) {
			myReferenceNucleotide = new StringDt();
		}
		return myReferenceNucleotide;
	}

	/**
	 * Sets the value(s) for <b>referenceNucleotide</b> (Genotype of the region in reference genome)
	 *
     * <p>
     * <b>Definition:</b>
     * Genotype of the region in reference genome
     * </p> 
	 */
	public DnaRegionAnalysisTestCoverageDnaRegionOfInterest setReferenceNucleotide(StringDt theValue) {
		myReferenceNucleotide = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>referenceNucleotide</b> (Genotype of the region in reference genome)
	 *
     * <p>
     * <b>Definition:</b>
     * Genotype of the region in reference genome
     * </p> 
	 */
	public DnaRegionAnalysisTestCoverageDnaRegionOfInterest setReferenceNucleotide( String theString) {
		myReferenceNucleotide = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>variableNucleotide</b> (The patient's genotype in the region).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The patient's genotype in the region
     * </p> 
	 */
	public StringDt getVariableNucleotide() {  
		if (myVariableNucleotide == null) {
			myVariableNucleotide = new StringDt();
		}
		return myVariableNucleotide;
	}

	/**
	 * Sets the value(s) for <b>variableNucleotide</b> (The patient's genotype in the region)
	 *
     * <p>
     * <b>Definition:</b>
     * The patient's genotype in the region
     * </p> 
	 */
	public DnaRegionAnalysisTestCoverageDnaRegionOfInterest setVariableNucleotide(StringDt theValue) {
		myVariableNucleotide = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>variableNucleotide</b> (The patient's genotype in the region)
	 *
     * <p>
     * <b>Definition:</b>
     * The patient's genotype in the region
     * </p> 
	 */
	public DnaRegionAnalysisTestCoverageDnaRegionOfInterest setVariableNucleotide( String theString) {
		myVariableNucleotide = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>genechipId</b> (ID of the genechip).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * ID of the genechip
     * </p> 
	 */
	public StringDt getGenechipId() {  
		if (myGenechipId == null) {
			myGenechipId = new StringDt();
		}
		return myGenechipId;
	}

	/**
	 * Sets the value(s) for <b>genechipId</b> (ID of the genechip)
	 *
     * <p>
     * <b>Definition:</b>
     * ID of the genechip
     * </p> 
	 */
	public DnaRegionAnalysisTestCoverageDnaRegionOfInterest setGenechipId(StringDt theValue) {
		myGenechipId = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>genechipId</b> (ID of the genechip)
	 *
     * <p>
     * <b>Definition:</b>
     * ID of the genechip
     * </p> 
	 */
	public DnaRegionAnalysisTestCoverageDnaRegionOfInterest setGenechipId( String theString) {
		myGenechipId = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>genechipManufacturerId</b> (ID of manufacturer of the genechip).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * ID of manufacturer of the genechip
     * </p> 
	 */
	public StringDt getGenechipManufacturerId() {  
		if (myGenechipManufacturerId == null) {
			myGenechipManufacturerId = new StringDt();
		}
		return myGenechipManufacturerId;
	}

	/**
	 * Sets the value(s) for <b>genechipManufacturerId</b> (ID of manufacturer of the genechip)
	 *
     * <p>
     * <b>Definition:</b>
     * ID of manufacturer of the genechip
     * </p> 
	 */
	public DnaRegionAnalysisTestCoverageDnaRegionOfInterest setGenechipManufacturerId(StringDt theValue) {
		myGenechipManufacturerId = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>genechipManufacturerId</b> (ID of manufacturer of the genechip)
	 *
     * <p>
     * <b>Definition:</b>
     * ID of manufacturer of the genechip
     * </p> 
	 */
	public DnaRegionAnalysisTestCoverageDnaRegionOfInterest setGenechipManufacturerId( String theString) {
		myGenechipManufacturerId = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>genechipVersion</b> (Version of the genechip).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Version of the genechip
     * </p> 
	 */
	public StringDt getGenechipVersion() {  
		if (myGenechipVersion == null) {
			myGenechipVersion = new StringDt();
		}
		return myGenechipVersion;
	}

	/**
	 * Sets the value(s) for <b>genechipVersion</b> (Version of the genechip)
	 *
     * <p>
     * <b>Definition:</b>
     * Version of the genechip
     * </p> 
	 */
	public DnaRegionAnalysisTestCoverageDnaRegionOfInterest setGenechipVersion(StringDt theValue) {
		myGenechipVersion = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>genechipVersion</b> (Version of the genechip)
	 *
     * <p>
     * <b>Definition:</b>
     * Version of the genechip
     * </p> 
	 */
	public DnaRegionAnalysisTestCoverageDnaRegionOfInterest setGenechipVersion( String theString) {
		myGenechipVersion = new StringDt(theString); 
		return this; 
	}

 

	}



	/**
	 * Block class for child element: <b>GeneticAnalysis.geneticAnalysisDiscreteResult</b> (Genetic analysis discrete result)
	 *
     * <p>
     * <b>Definition:</b>
     * Genetic analysis discrete result
     * </p> 
	 */
	@Block()	
	public static class GeneticAnalysisDiscreteResult extends BaseIdentifiableElement implements IResourceBlock {
	
	@Child(name="dnaAnalysisDiscreteSequenceVariation", type=StringDt.class, order=0, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="DNA analysis discrete sequence variation",
		formalDefinition="DNA analysis discrete sequence variation"
	)
	private java.util.List<StringDt> myDnaAnalysisDiscreteSequenceVariation;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myDnaAnalysisDiscreteSequenceVariation);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myDnaAnalysisDiscreteSequenceVariation);
	}

	/**
	 * Gets the value(s) for <b>dnaAnalysisDiscreteSequenceVariation</b> (DNA analysis discrete sequence variation).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * DNA analysis discrete sequence variation
     * </p> 
	 */
	public java.util.List<StringDt> getDnaAnalysisDiscreteSequenceVariation() {  
		if (myDnaAnalysisDiscreteSequenceVariation == null) {
			myDnaAnalysisDiscreteSequenceVariation = new java.util.ArrayList<StringDt>();
		}
		return myDnaAnalysisDiscreteSequenceVariation;
	}

	/**
	 * Sets the value(s) for <b>dnaAnalysisDiscreteSequenceVariation</b> (DNA analysis discrete sequence variation)
	 *
     * <p>
     * <b>Definition:</b>
     * DNA analysis discrete sequence variation
     * </p> 
	 */
	public GeneticAnalysisDiscreteResult setDnaAnalysisDiscreteSequenceVariation(java.util.List<StringDt> theValue) {
		myDnaAnalysisDiscreteSequenceVariation = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>dnaAnalysisDiscreteSequenceVariation</b> (DNA analysis discrete sequence variation)
	 *
     * <p>
     * <b>Definition:</b>
     * DNA analysis discrete sequence variation
     * </p> 
	 */
	public StringDt addDnaAnalysisDiscreteSequenceVariation() {
		StringDt newType = new StringDt();
		getDnaAnalysisDiscreteSequenceVariation().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>dnaAnalysisDiscreteSequenceVariation</b> (DNA analysis discrete sequence variation),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * DNA analysis discrete sequence variation
     * </p> 
	 */
	public StringDt getDnaAnalysisDiscreteSequenceVariationFirstRep() {
		if (getDnaAnalysisDiscreteSequenceVariation().isEmpty()) {
			return addDnaAnalysisDiscreteSequenceVariation();
		}
		return getDnaAnalysisDiscreteSequenceVariation().get(0); 
	}
 	/**
	 * Adds a new value for <b>dnaAnalysisDiscreteSequenceVariation</b> (DNA analysis discrete sequence variation)
	 *
     * <p>
     * <b>Definition:</b>
     * DNA analysis discrete sequence variation
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public GeneticAnalysisDiscreteResult addDnaAnalysisDiscreteSequenceVariation( String theString) {
		if (myDnaAnalysisDiscreteSequenceVariation == null) {
			myDnaAnalysisDiscreteSequenceVariation = new java.util.ArrayList<StringDt>();
		}
		myDnaAnalysisDiscreteSequenceVariation.add(new StringDt(theString));
		return this; 
	}

 

	}




}
