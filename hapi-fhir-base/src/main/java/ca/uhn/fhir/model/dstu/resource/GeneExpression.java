















package ca.uhn.fhir.model.dstu.resource;

/*
 * #%L
 * HAPI FHIR Library
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


import java.util.List;

import ca.uhn.fhir.model.api.BaseIdentifiableElement;
import ca.uhn.fhir.model.api.BaseResource;
import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.IResourceBlock;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.api.annotation.Block;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.primitive.DecimalDt;
import ca.uhn.fhir.model.primitive.IntegerDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.gclient.ReferenceParam;
import ca.uhn.fhir.rest.gclient.StringParam;


/**
 * HAPI/FHIR <b>GeneExpression</b> Resource
 * (Resource that records the patient's expression of a gene)
 *
 * <p>
 * <b>Definition:</b>
 * Resource that records the patient's expression of a gene
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * 
 * </p> 
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/GeneExpression">http://hl7.org/fhir/profiles/GeneExpression</a> 
 * </p>
 *
 */
@ResourceDef(name="GeneExpression", profile="http://hl7.org/fhir/profiles/GeneExpression", id="geneexpression")
public class GeneExpression extends BaseResource implements IResource {

	/**
	 * Search parameter constant for <b>subject</b>
	 * <p>
	 * Description: <b>subject being described by the resource</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>GeneExpression.subject</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="subject", path="GeneExpression.subject", description="subject being described by the resource", type="reference")
	public static final String SP_SUBJECT = "subject";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>subject</b>
	 * <p>
	 * Description: <b>subject being described by the resource</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>GeneExpression.subject</b><br/>
	 * </p>
	 */
	public static final ReferenceParam SUBJECT = new ReferenceParam(SP_SUBJECT);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>GeneExpression.subject</b>".
	 */
	public static final Include INCLUDE_SUBJECT = new Include("GeneExpression.subject");

	/**
	 * Search parameter constant for <b>gene</b>
	 * <p>
	 * Description: <b>Id of the gene</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>GeneExpression.gene.identifier</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="gene", path="GeneExpression.gene.identifier", description="Id of the gene", type="string")
	public static final String SP_GENE = "gene";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>gene</b>
	 * <p>
	 * Description: <b>Id of the gene</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>GeneExpression.gene.identifier</b><br/>
	 * </p>
	 */
	public static final StringParam GENE = new StringParam(SP_GENE);

	/**
	 * Search parameter constant for <b>coordinate</b>
	 * <p>
	 * Description: <b>Coordinate of the gene</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>GeneExpression.gene.coordinate</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="coordinate", path="GeneExpression.gene.coordinate", description="Coordinate of the gene", type="string")
	public static final String SP_COORDINATE = "coordinate";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>coordinate</b>
	 * <p>
	 * Description: <b>Coordinate of the gene</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>GeneExpression.gene.coordinate</b><br/>
	 * </p>
	 */
	public static final StringParam COORDINATE = new StringParam(SP_COORDINATE);


	@Child(name="subject", order=0, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Patient.class	})
	@Description(
		shortDefinition="Subject described by the resource",
		formalDefinition="Subject described by the resource"
	)
	private ResourceReferenceDt mySubject;
	
	@Child(name="gene", order=1, min=1, max=1)	
	@Description(
		shortDefinition="Gene of study",
		formalDefinition="Gene of study"
	)
	private Gene myGene;
	
	@Child(name="microarray", order=2, min=0, max=Child.MAX_UNLIMITED, type={
		ca.uhn.fhir.model.dstu.resource.Microarray.class	})
	@Description(
		shortDefinition="Microarray that studies the gene",
		formalDefinition="Microarray that studies the gene"
	)
	private java.util.List<ResourceReferenceDt> myMicroarray;
	
	@Child(name="rnaSeq", order=3, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="RNA-Seq that studies the gene",
		formalDefinition="RNA-Seq that studies the gene"
	)
	private java.util.List<RnaSeq> myRnaSeq;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  mySubject,  myGene,  myMicroarray,  myRnaSeq);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, mySubject, myGene, myMicroarray, myRnaSeq);
	}

	/**
	 * Gets the value(s) for <b>subject</b> (Subject described by the resource).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Subject described by the resource
     * </p> 
	 */
	public ResourceReferenceDt getSubject() {  
		if (mySubject == null) {
			mySubject = new ResourceReferenceDt();
		}
		return mySubject;
	}

	/**
	 * Sets the value(s) for <b>subject</b> (Subject described by the resource)
	 *
     * <p>
     * <b>Definition:</b>
     * Subject described by the resource
     * </p> 
	 */
	public GeneExpression setSubject(ResourceReferenceDt theValue) {
		mySubject = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>gene</b> (Gene of study).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Gene of study
     * </p> 
	 */
	public Gene getGene() {  
		if (myGene == null) {
			myGene = new Gene();
		}
		return myGene;
	}

	/**
	 * Sets the value(s) for <b>gene</b> (Gene of study)
	 *
     * <p>
     * <b>Definition:</b>
     * Gene of study
     * </p> 
	 */
	public GeneExpression setGene(Gene theValue) {
		myGene = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>microarray</b> (Microarray that studies the gene).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Microarray that studies the gene
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getMicroarray() {  
		if (myMicroarray == null) {
			myMicroarray = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return myMicroarray;
	}

	/**
	 * Sets the value(s) for <b>microarray</b> (Microarray that studies the gene)
	 *
     * <p>
     * <b>Definition:</b>
     * Microarray that studies the gene
     * </p> 
	 */
	public GeneExpression setMicroarray(java.util.List<ResourceReferenceDt> theValue) {
		myMicroarray = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>microarray</b> (Microarray that studies the gene)
	 *
     * <p>
     * <b>Definition:</b>
     * Microarray that studies the gene
     * </p> 
	 */
	public ResourceReferenceDt addMicroarray() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getMicroarray().add(newType);
		return newType; 
	}
  
	/**
	 * Gets the value(s) for <b>rnaSeq</b> (RNA-Seq that studies the gene).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * RNA-Seq that studies the gene
     * </p> 
	 */
	public java.util.List<RnaSeq> getRnaSeq() {  
		if (myRnaSeq == null) {
			myRnaSeq = new java.util.ArrayList<RnaSeq>();
		}
		return myRnaSeq;
	}

	/**
	 * Sets the value(s) for <b>rnaSeq</b> (RNA-Seq that studies the gene)
	 *
     * <p>
     * <b>Definition:</b>
     * RNA-Seq that studies the gene
     * </p> 
	 */
	public GeneExpression setRnaSeq(java.util.List<RnaSeq> theValue) {
		myRnaSeq = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>rnaSeq</b> (RNA-Seq that studies the gene)
	 *
     * <p>
     * <b>Definition:</b>
     * RNA-Seq that studies the gene
     * </p> 
	 */
	public RnaSeq addRnaSeq() {
		RnaSeq newType = new RnaSeq();
		getRnaSeq().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>rnaSeq</b> (RNA-Seq that studies the gene),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * RNA-Seq that studies the gene
     * </p> 
	 */
	public RnaSeq getRnaSeqFirstRep() {
		if (getRnaSeq().isEmpty()) {
			return addRnaSeq();
		}
		return getRnaSeq().get(0); 
	}
  
	/**
	 * Block class for child element: <b>GeneExpression.gene</b> (Gene of study)
	 *
     * <p>
     * <b>Definition:</b>
     * Gene of study
     * </p> 
	 */
	@Block()	
	public static class Gene extends BaseIdentifiableElement implements IResourceBlock {
	
	@Child(name="identifier", type=StringDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="Identifier of the gene",
		formalDefinition="Identifier of the gene"
	)
	private StringDt myIdentifier;
	
	@Child(name="coordinate", order=1, min=0, max=1)	
	@Description(
		shortDefinition="Coordinate of the gene",
		formalDefinition="Coordinate of the gene"
	)
	private GeneCoordinate myCoordinate;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myIdentifier,  myCoordinate);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myIdentifier, myCoordinate);
	}

	/**
	 * Gets the value(s) for <b>identifier</b> (Identifier of the gene).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier of the gene
     * </p> 
	 */
	public StringDt getIdentifier() {  
		if (myIdentifier == null) {
			myIdentifier = new StringDt();
		}
		return myIdentifier;
	}

	/**
	 * Sets the value(s) for <b>identifier</b> (Identifier of the gene)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier of the gene
     * </p> 
	 */
	public Gene setIdentifier(StringDt theValue) {
		myIdentifier = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>identifier</b> (Identifier of the gene)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier of the gene
     * </p> 
	 */
	public Gene setIdentifier( String theString) {
		myIdentifier = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>coordinate</b> (Coordinate of the gene).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Coordinate of the gene
     * </p> 
	 */
	public GeneCoordinate getCoordinate() {  
		if (myCoordinate == null) {
			myCoordinate = new GeneCoordinate();
		}
		return myCoordinate;
	}

	/**
	 * Sets the value(s) for <b>coordinate</b> (Coordinate of the gene)
	 *
     * <p>
     * <b>Definition:</b>
     * Coordinate of the gene
     * </p> 
	 */
	public Gene setCoordinate(GeneCoordinate theValue) {
		myCoordinate = theValue;
		return this;
	}

  

	}

	/**
	 * Block class for child element: <b>GeneExpression.gene.coordinate</b> (Coordinate of the gene)
	 *
     * <p>
     * <b>Definition:</b>
     * Coordinate of the gene
     * </p> 
	 */
	@Block()	
	public static class GeneCoordinate extends BaseIdentifiableElement implements IResourceBlock {
	
	@Child(name="chromosome", type=StringDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="Chromosome",
		formalDefinition="Chromosome"
	)
	private StringDt myChromosome;
	
	@Child(name="start", type=IntegerDt.class, order=1, min=1, max=1)	
	@Description(
		shortDefinition="Start position",
		formalDefinition="Start position"
	)
	private IntegerDt myStart;
	
	@Child(name="end", type=IntegerDt.class, order=2, min=1, max=1)	
	@Description(
		shortDefinition="End position",
		formalDefinition="End position"
	)
	private IntegerDt myEnd;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myChromosome,  myStart,  myEnd);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myChromosome, myStart, myEnd);
	}

	/**
	 * Gets the value(s) for <b>chromosome</b> (Chromosome).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Chromosome
     * </p> 
	 */
	public StringDt getChromosome() {  
		if (myChromosome == null) {
			myChromosome = new StringDt();
		}
		return myChromosome;
	}

	/**
	 * Sets the value(s) for <b>chromosome</b> (Chromosome)
	 *
     * <p>
     * <b>Definition:</b>
     * Chromosome
     * </p> 
	 */
	public GeneCoordinate setChromosome(StringDt theValue) {
		myChromosome = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>chromosome</b> (Chromosome)
	 *
     * <p>
     * <b>Definition:</b>
     * Chromosome
     * </p> 
	 */
	public GeneCoordinate setChromosome( String theString) {
		myChromosome = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>start</b> (Start position).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Start position
     * </p> 
	 */
	public IntegerDt getStart() {  
		if (myStart == null) {
			myStart = new IntegerDt();
		}
		return myStart;
	}

	/**
	 * Sets the value(s) for <b>start</b> (Start position)
	 *
     * <p>
     * <b>Definition:</b>
     * Start position
     * </p> 
	 */
	public GeneCoordinate setStart(IntegerDt theValue) {
		myStart = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>start</b> (Start position)
	 *
     * <p>
     * <b>Definition:</b>
     * Start position
     * </p> 
	 */
	public GeneCoordinate setStart( int theInteger) {
		myStart = new IntegerDt(theInteger); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>end</b> (End position).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * End position
     * </p> 
	 */
	public IntegerDt getEnd() {  
		if (myEnd == null) {
			myEnd = new IntegerDt();
		}
		return myEnd;
	}

	/**
	 * Sets the value(s) for <b>end</b> (End position)
	 *
     * <p>
     * <b>Definition:</b>
     * End position
     * </p> 
	 */
	public GeneCoordinate setEnd(IntegerDt theValue) {
		myEnd = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>end</b> (End position)
	 *
     * <p>
     * <b>Definition:</b>
     * End position
     * </p> 
	 */
	public GeneCoordinate setEnd( int theInteger) {
		myEnd = new IntegerDt(theInteger); 
		return this; 
	}

 

	}



	/**
	 * Block class for child element: <b>GeneExpression.rnaSeq</b> (RNA-Seq that studies the gene)
	 *
     * <p>
     * <b>Definition:</b>
     * RNA-Seq that studies the gene
     * </p> 
	 */
	@Block()	
	public static class RnaSeq extends BaseIdentifiableElement implements IResourceBlock {
	
	@Child(name="inputLab", order=0, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.SequencingLab.class	})
	@Description(
		shortDefinition="Input lab for the RNA-Seq",
		formalDefinition="Input lab for the RNA-Seq"
	)
	private ResourceReferenceDt myInputLab;
	
	@Child(name="inputAnalysis", order=1, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.SequencingAnalysis.class	})
	@Description(
		shortDefinition="Input analysis for the RNA-Seq",
		formalDefinition="Input analysis for the RNA-Seq"
	)
	private ResourceReferenceDt myInputAnalysis;
	
	@Child(name="expression", type=DecimalDt.class, order=2, min=1, max=1)	
	@Description(
		shortDefinition="Expression level of the gene in RPKM",
		formalDefinition="Expression level of the gene in RPKM"
	)
	private DecimalDt myExpression;
	
	@Child(name="isoform", order=3, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Isoform of the gene",
		formalDefinition="Isoform of the gene"
	)
	private java.util.List<RnaSeqIsoform> myIsoform;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myInputLab,  myInputAnalysis,  myExpression,  myIsoform);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myInputLab, myInputAnalysis, myExpression, myIsoform);
	}

	/**
	 * Gets the value(s) for <b>inputLab</b> (Input lab for the RNA-Seq).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Input lab for the RNA-Seq
     * </p> 
	 */
	public ResourceReferenceDt getInputLab() {  
		if (myInputLab == null) {
			myInputLab = new ResourceReferenceDt();
		}
		return myInputLab;
	}

	/**
	 * Sets the value(s) for <b>inputLab</b> (Input lab for the RNA-Seq)
	 *
     * <p>
     * <b>Definition:</b>
     * Input lab for the RNA-Seq
     * </p> 
	 */
	public RnaSeq setInputLab(ResourceReferenceDt theValue) {
		myInputLab = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>inputAnalysis</b> (Input analysis for the RNA-Seq).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Input analysis for the RNA-Seq
     * </p> 
	 */
	public ResourceReferenceDt getInputAnalysis() {  
		if (myInputAnalysis == null) {
			myInputAnalysis = new ResourceReferenceDt();
		}
		return myInputAnalysis;
	}

	/**
	 * Sets the value(s) for <b>inputAnalysis</b> (Input analysis for the RNA-Seq)
	 *
     * <p>
     * <b>Definition:</b>
     * Input analysis for the RNA-Seq
     * </p> 
	 */
	public RnaSeq setInputAnalysis(ResourceReferenceDt theValue) {
		myInputAnalysis = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>expression</b> (Expression level of the gene in RPKM).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Expression level of the gene in RPKM
     * </p> 
	 */
	public DecimalDt getExpression() {  
		if (myExpression == null) {
			myExpression = new DecimalDt();
		}
		return myExpression;
	}

	/**
	 * Sets the value(s) for <b>expression</b> (Expression level of the gene in RPKM)
	 *
     * <p>
     * <b>Definition:</b>
     * Expression level of the gene in RPKM
     * </p> 
	 */
	public RnaSeq setExpression(DecimalDt theValue) {
		myExpression = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>expression</b> (Expression level of the gene in RPKM)
	 *
     * <p>
     * <b>Definition:</b>
     * Expression level of the gene in RPKM
     * </p> 
	 */
	public RnaSeq setExpression( long theValue) {
		myExpression = new DecimalDt(theValue); 
		return this; 
	}

	/**
	 * Sets the value for <b>expression</b> (Expression level of the gene in RPKM)
	 *
     * <p>
     * <b>Definition:</b>
     * Expression level of the gene in RPKM
     * </p> 
	 */
	public RnaSeq setExpression( double theValue) {
		myExpression = new DecimalDt(theValue); 
		return this; 
	}

	/**
	 * Sets the value for <b>expression</b> (Expression level of the gene in RPKM)
	 *
     * <p>
     * <b>Definition:</b>
     * Expression level of the gene in RPKM
     * </p> 
	 */
	public RnaSeq setExpression( java.math.BigDecimal theValue) {
		myExpression = new DecimalDt(theValue); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>isoform</b> (Isoform of the gene).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Isoform of the gene
     * </p> 
	 */
	public java.util.List<RnaSeqIsoform> getIsoform() {  
		if (myIsoform == null) {
			myIsoform = new java.util.ArrayList<RnaSeqIsoform>();
		}
		return myIsoform;
	}

	/**
	 * Sets the value(s) for <b>isoform</b> (Isoform of the gene)
	 *
     * <p>
     * <b>Definition:</b>
     * Isoform of the gene
     * </p> 
	 */
	public RnaSeq setIsoform(java.util.List<RnaSeqIsoform> theValue) {
		myIsoform = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>isoform</b> (Isoform of the gene)
	 *
     * <p>
     * <b>Definition:</b>
     * Isoform of the gene
     * </p> 
	 */
	public RnaSeqIsoform addIsoform() {
		RnaSeqIsoform newType = new RnaSeqIsoform();
		getIsoform().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>isoform</b> (Isoform of the gene),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Isoform of the gene
     * </p> 
	 */
	public RnaSeqIsoform getIsoformFirstRep() {
		if (getIsoform().isEmpty()) {
			return addIsoform();
		}
		return getIsoform().get(0); 
	}
  

	}

	/**
	 * Block class for child element: <b>GeneExpression.rnaSeq.isoform</b> (Isoform of the gene)
	 *
     * <p>
     * <b>Definition:</b>
     * Isoform of the gene
     * </p> 
	 */
	@Block()	
	public static class RnaSeqIsoform extends BaseIdentifiableElement implements IResourceBlock {
	
	@Child(name="identity", type=StringDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="Identifier of the isoform",
		formalDefinition="Identifier of the isoform"
	)
	private StringDt myIdentity;
	
	@Child(name="expression", type=DecimalDt.class, order=1, min=1, max=1)	
	@Description(
		shortDefinition="Expression level of the isoform in RPKM",
		formalDefinition="Expression level of the isoform in RPKM"
	)
	private DecimalDt myExpression;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myIdentity,  myExpression);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myIdentity, myExpression);
	}

	/**
	 * Gets the value(s) for <b>identity</b> (Identifier of the isoform).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier of the isoform
     * </p> 
	 */
	public StringDt getIdentity() {  
		if (myIdentity == null) {
			myIdentity = new StringDt();
		}
		return myIdentity;
	}

	/**
	 * Sets the value(s) for <b>identity</b> (Identifier of the isoform)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier of the isoform
     * </p> 
	 */
	public RnaSeqIsoform setIdentity(StringDt theValue) {
		myIdentity = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>identity</b> (Identifier of the isoform)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier of the isoform
     * </p> 
	 */
	public RnaSeqIsoform setIdentity( String theString) {
		myIdentity = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>expression</b> (Expression level of the isoform in RPKM).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Expression level of the isoform in RPKM
     * </p> 
	 */
	public DecimalDt getExpression() {  
		if (myExpression == null) {
			myExpression = new DecimalDt();
		}
		return myExpression;
	}

	/**
	 * Sets the value(s) for <b>expression</b> (Expression level of the isoform in RPKM)
	 *
     * <p>
     * <b>Definition:</b>
     * Expression level of the isoform in RPKM
     * </p> 
	 */
	public RnaSeqIsoform setExpression(DecimalDt theValue) {
		myExpression = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>expression</b> (Expression level of the isoform in RPKM)
	 *
     * <p>
     * <b>Definition:</b>
     * Expression level of the isoform in RPKM
     * </p> 
	 */
	public RnaSeqIsoform setExpression( long theValue) {
		myExpression = new DecimalDt(theValue); 
		return this; 
	}

	/**
	 * Sets the value for <b>expression</b> (Expression level of the isoform in RPKM)
	 *
     * <p>
     * <b>Definition:</b>
     * Expression level of the isoform in RPKM
     * </p> 
	 */
	public RnaSeqIsoform setExpression( double theValue) {
		myExpression = new DecimalDt(theValue); 
		return this; 
	}

	/**
	 * Sets the value for <b>expression</b> (Expression level of the isoform in RPKM)
	 *
     * <p>
     * <b>Definition:</b>
     * Expression level of the isoform in RPKM
     * </p> 
	 */
	public RnaSeqIsoform setExpression( java.math.BigDecimal theValue) {
		myExpression = new DecimalDt(theValue); 
		return this; 
	}

 

	}





}