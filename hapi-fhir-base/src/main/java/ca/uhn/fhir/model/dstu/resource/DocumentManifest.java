















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


import java.util.Date;
import java.util.List;

import ca.uhn.fhir.model.api.BaseResource;
import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
import ca.uhn.fhir.model.dstu.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu.valueset.DocumentReferenceStatusEnum;
import ca.uhn.fhir.model.dstu.valueset.IdentifierUseEnum;
import ca.uhn.fhir.model.primitive.BoundCodeDt;
import ca.uhn.fhir.model.primitive.CodeDt;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.model.primitive.UriDt;


/**
 * HAPI/FHIR <b>DocumentManifest</b> Resource
 * (A manifest that defines a set of documents)
 *
 * <p>
 * <b>Definition:</b>
 * A manifest that defines a set of documents
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * 
 * </p> 
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/DocumentManifest">http://hl7.org/fhir/profiles/DocumentManifest</a> 
 * </p>
 *
 */
@ResourceDef(name="DocumentManifest", profile="http://hl7.org/fhir/profiles/DocumentManifest", id="documentmanifest")
public class DocumentManifest extends BaseResource implements IResource {

	/**
	 * Search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>DocumentManifest.masterIdentifier | DocumentManifest.identifier</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="identifier", path="DocumentManifest.masterIdentifier | DocumentManifest.identifier", description="")
	public static final String SP_IDENTIFIER = "identifier";

	/**
	 * Search parameter constant for <b>subject</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>DocumentManifest.subject</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="subject", path="DocumentManifest.subject", description="")
	public static final String SP_SUBJECT = "subject";

	/**
	 * Search parameter constant for <b>type</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>DocumentManifest.type</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="type", path="DocumentManifest.type", description="")
	public static final String SP_TYPE = "type";

	/**
	 * Search parameter constant for <b>recipient</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>DocumentManifest.recipient</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="recipient", path="DocumentManifest.recipient", description="")
	public static final String SP_RECIPIENT = "recipient";

	/**
	 * Search parameter constant for <b>author</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>DocumentManifest.author</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="author", path="DocumentManifest.author", description="")
	public static final String SP_AUTHOR = "author";

	/**
	 * Search parameter constant for <b>created</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>DocumentManifest.created</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="created", path="DocumentManifest.created", description="")
	public static final String SP_CREATED = "created";

	/**
	 * Search parameter constant for <b>status</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>DocumentManifest.status</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="status", path="DocumentManifest.status", description="")
	public static final String SP_STATUS = "status";

	/**
	 * Search parameter constant for <b>supersedes</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>DocumentManifest.supercedes</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="supersedes", path="DocumentManifest.supercedes", description="")
	public static final String SP_SUPERSEDES = "supersedes";

	/**
	 * Search parameter constant for <b>description</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>DocumentManifest.description</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="description", path="DocumentManifest.description", description="")
	public static final String SP_DESCRIPTION = "description";

	/**
	 * Search parameter constant for <b>confidentiality</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>DocumentManifest.confidentiality</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="confidentiality", path="DocumentManifest.confidentiality", description="")
	public static final String SP_CONFIDENTIALITY = "confidentiality";

	/**
	 * Search parameter constant for <b>content</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>DocumentManifest.content</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="content", path="DocumentManifest.content", description="")
	public static final String SP_CONTENT = "content";


	@Child(name="masterIdentifier", type=IdentifierDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="Unique Identifier for the set of documents",
		formalDefinition="A single identifier that uniquely identifies this manifest. Principally used to refer to the manifest in non-FHIR contexts"
	)
	private IdentifierDt myMasterIdentifier;
	
	@Child(name="identifier", type=IdentifierDt.class, order=1, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Other identifiers for the manifest",
		formalDefinition="Other identifiers associated with the document, including version independent, source record and workflow related identifiers"
	)
	private java.util.List<IdentifierDt> myIdentifier;
	
	@Child(name="subject", order=2, min=1, max=Child.MAX_UNLIMITED, type={
		ca.uhn.fhir.model.dstu.resource.Patient.class, 		ca.uhn.fhir.model.dstu.resource.Practitioner.class, 		ca.uhn.fhir.model.dstu.resource.Group.class, 		ca.uhn.fhir.model.dstu.resource.Device.class	})
	@Description(
		shortDefinition="The subject of the set of documents",
		formalDefinition="Who or what the set of documents is about. The documents can be about a person, (patient or healthcare practitioner), a device (i.e. machine) or even a group of subjects (such as a document about a herd of farm animals, or a set of patients that share a common exposure). If the documents cross more than one subject, then more than one subject is allowed here (unusual use case)"
	)
	private java.util.List<ResourceReferenceDt> mySubject;
	
	@Child(name="recipient", order=3, min=0, max=Child.MAX_UNLIMITED, type={
		ca.uhn.fhir.model.dstu.resource.Patient.class, 		ca.uhn.fhir.model.dstu.resource.Practitioner.class, 		ca.uhn.fhir.model.dstu.resource.Organization.class	})
	@Description(
		shortDefinition="Intended to get notified about this set of documents",
		formalDefinition="A patient, practitioner, or organization for which this set of documents is intended"
	)
	private java.util.List<ResourceReferenceDt> myRecipient;
	
	@Child(name="type", type=CodeableConceptDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="What kind of document set this is",
		formalDefinition="Specifies the kind of this set of documents (e.g. Patient Summary, Discharge Summary, Prescription, etc.). The type of a set of documents may be the same as one of the documents in it - especially if there is only one - but it may be wider"
	)
	private CodeableConceptDt myType;
	
	@Child(name="author", order=5, min=0, max=Child.MAX_UNLIMITED, type={
		ca.uhn.fhir.model.dstu.resource.Practitioner.class, 		ca.uhn.fhir.model.dstu.resource.Device.class, 		ca.uhn.fhir.model.dstu.resource.Patient.class, 		ca.uhn.fhir.model.dstu.resource.RelatedPerson.class	})
	@Description(
		shortDefinition="Who and/or what authored the document",
		formalDefinition="Identifies who is responsible for adding the information to the document"
	)
	private java.util.List<ResourceReferenceDt> myAuthor;
	
	@Child(name="created", type=DateTimeDt.class, order=6, min=0, max=1)	
	@Description(
		shortDefinition="When this document manifest created",
		formalDefinition="When the document manifest was created for submission to the server (not necessarily the same thing as the actual resource last modified time, since it may be modified, replicated etc)"
	)
	private DateTimeDt myCreated;
	
	@Child(name="source", type=UriDt.class, order=7, min=0, max=1)	
	@Description(
		shortDefinition="The source system/application/software",
		formalDefinition="Identifies the source system, application, or software that produced the document manifest"
	)
	private UriDt mySource;
	
	@Child(name="status", type=CodeDt.class, order=8, min=1, max=1)	
	@Description(
		shortDefinition="current | superceded | entered in error",
		formalDefinition="The status of this document manifest"
	)
	private BoundCodeDt<DocumentReferenceStatusEnum> myStatus;
	
	@Child(name="supercedes", order=9, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.DocumentManifest.class	})
	@Description(
		shortDefinition="If this document manifest replaces another",
		formalDefinition="Whether this document manifest replaces another"
	)
	private ResourceReferenceDt mySupercedes;
	
	@Child(name="description", type=StringDt.class, order=10, min=0, max=1)	
	@Description(
		shortDefinition="Human-readable description (title)",
		formalDefinition="Human-readable description of the source document. This is sometimes known as the \"title\""
	)
	private StringDt myDescription;
	
	@Child(name="confidentiality", type=CodeableConceptDt.class, order=11, min=0, max=1)	
	@Description(
		shortDefinition="Sensitivity of set of documents",
		formalDefinition="A code specifying the level of confidentiality of this set of Documents"
	)
	private CodeableConceptDt myConfidentiality;
	
	@Child(name="content", order=12, min=1, max=Child.MAX_UNLIMITED, type={
		ca.uhn.fhir.model.dstu.resource.DocumentReference.class, 		ca.uhn.fhir.model.dstu.resource.Binary.class, 		ca.uhn.fhir.model.dstu.resource.Media.class	})
	@Description(
		shortDefinition="Contents of this set of documents",
		formalDefinition="The list of resources that describe the parts of this document reference. Usually, these would be document references, but direct references to binary attachments and images are also allowed"
	)
	private java.util.List<ResourceReferenceDt> myContent;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myMasterIdentifier,  myIdentifier,  mySubject,  myRecipient,  myType,  myAuthor,  myCreated,  mySource,  myStatus,  mySupercedes,  myDescription,  myConfidentiality,  myContent);
	}
	
	@Override
	public java.util.List<IElement> getAllPopulatedChildElements() {
		return getAllPopulatedChildElementsOfType(null);
	}

	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myMasterIdentifier, myIdentifier, mySubject, myRecipient, myType, myAuthor, myCreated, mySource, myStatus, mySupercedes, myDescription, myConfidentiality, myContent);
	}

	/**
	 * Gets the value(s) for <b>masterIdentifier</b> (Unique Identifier for the set of documents).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A single identifier that uniquely identifies this manifest. Principally used to refer to the manifest in non-FHIR contexts
     * </p> 
	 */
	public IdentifierDt getMasterIdentifier() {  
		if (myMasterIdentifier == null) {
			myMasterIdentifier = new IdentifierDt();
		}
		return myMasterIdentifier;
	}

	/**
	 * Sets the value(s) for <b>masterIdentifier</b> (Unique Identifier for the set of documents)
	 *
     * <p>
     * <b>Definition:</b>
     * A single identifier that uniquely identifies this manifest. Principally used to refer to the manifest in non-FHIR contexts
     * </p> 
	 */
	public DocumentManifest setMasterIdentifier(IdentifierDt theValue) {
		myMasterIdentifier = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>masterIdentifier</b> (Unique Identifier for the set of documents)
	 *
     * <p>
     * <b>Definition:</b>
     * A single identifier that uniquely identifies this manifest. Principally used to refer to the manifest in non-FHIR contexts
     * </p> 
	 */
	public DocumentManifest setMasterIdentifier( IdentifierUseEnum theUse,  String theSystem,  String theValue,  String theLabel) {
		myMasterIdentifier = new IdentifierDt(theUse, theSystem, theValue, theLabel); 
		return this; 
	}

	/**
	 * Sets the value for <b>masterIdentifier</b> (Unique Identifier for the set of documents)
	 *
     * <p>
     * <b>Definition:</b>
     * A single identifier that uniquely identifies this manifest. Principally used to refer to the manifest in non-FHIR contexts
     * </p> 
	 */
	public DocumentManifest setMasterIdentifier( String theSystem,  String theValue) {
		myMasterIdentifier = new IdentifierDt(theSystem, theValue); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>identifier</b> (Other identifiers for the manifest).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Other identifiers associated with the document, including version independent, source record and workflow related identifiers
     * </p> 
	 */
	public java.util.List<IdentifierDt> getIdentifier() {  
		if (myIdentifier == null) {
			myIdentifier = new java.util.ArrayList<IdentifierDt>();
		}
		return myIdentifier;
	}

	/**
	 * Sets the value(s) for <b>identifier</b> (Other identifiers for the manifest)
	 *
     * <p>
     * <b>Definition:</b>
     * Other identifiers associated with the document, including version independent, source record and workflow related identifiers
     * </p> 
	 */
	public DocumentManifest setIdentifier(java.util.List<IdentifierDt> theValue) {
		myIdentifier = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>identifier</b> (Other identifiers for the manifest)
	 *
     * <p>
     * <b>Definition:</b>
     * Other identifiers associated with the document, including version independent, source record and workflow related identifiers
     * </p> 
	 */
	public IdentifierDt addIdentifier() {
		IdentifierDt newType = new IdentifierDt();
		getIdentifier().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>identifier</b> (Other identifiers for the manifest),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Other identifiers associated with the document, including version independent, source record and workflow related identifiers
     * </p> 
	 */
	public IdentifierDt getIdentifierFirstRep() {
		if (getIdentifier().isEmpty()) {
			return addIdentifier();
		}
		return getIdentifier().get(0); 
	}
 	/**
	 * Adds a new value for <b>identifier</b> (Other identifiers for the manifest)
	 *
     * <p>
     * <b>Definition:</b>
     * Other identifiers associated with the document, including version independent, source record and workflow related identifiers
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public DocumentManifest addIdentifier( IdentifierUseEnum theUse,  String theSystem,  String theValue,  String theLabel) {
		if (myIdentifier == null) {
			myIdentifier = new java.util.ArrayList<IdentifierDt>();
		}
		myIdentifier.add(new IdentifierDt(theUse, theSystem, theValue, theLabel));
		return this; 
	}

	/**
	 * Adds a new value for <b>identifier</b> (Other identifiers for the manifest)
	 *
     * <p>
     * <b>Definition:</b>
     * Other identifiers associated with the document, including version independent, source record and workflow related identifiers
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public DocumentManifest addIdentifier( String theSystem,  String theValue) {
		if (myIdentifier == null) {
			myIdentifier = new java.util.ArrayList<IdentifierDt>();
		}
		myIdentifier.add(new IdentifierDt(theSystem, theValue));
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>subject</b> (The subject of the set of documents).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Who or what the set of documents is about. The documents can be about a person, (patient or healthcare practitioner), a device (i.e. machine) or even a group of subjects (such as a document about a herd of farm animals, or a set of patients that share a common exposure). If the documents cross more than one subject, then more than one subject is allowed here (unusual use case)
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getSubject() {  
		return mySubject;
	}

	/**
	 * Sets the value(s) for <b>subject</b> (The subject of the set of documents)
	 *
     * <p>
     * <b>Definition:</b>
     * Who or what the set of documents is about. The documents can be about a person, (patient or healthcare practitioner), a device (i.e. machine) or even a group of subjects (such as a document about a herd of farm animals, or a set of patients that share a common exposure). If the documents cross more than one subject, then more than one subject is allowed here (unusual use case)
     * </p> 
	 */
	public DocumentManifest setSubject(java.util.List<ResourceReferenceDt> theValue) {
		mySubject = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>subject</b> (The subject of the set of documents)
	 *
     * <p>
     * <b>Definition:</b>
     * Who or what the set of documents is about. The documents can be about a person, (patient or healthcare practitioner), a device (i.e. machine) or even a group of subjects (such as a document about a herd of farm animals, or a set of patients that share a common exposure). If the documents cross more than one subject, then more than one subject is allowed here (unusual use case)
     * </p> 
	 */
	public ResourceReferenceDt addSubject() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getSubject().add(newType);
		return newType; 
	}
  
	/**
	 * Gets the value(s) for <b>recipient</b> (Intended to get notified about this set of documents).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A patient, practitioner, or organization for which this set of documents is intended
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getRecipient() {  
		return myRecipient;
	}

	/**
	 * Sets the value(s) for <b>recipient</b> (Intended to get notified about this set of documents)
	 *
     * <p>
     * <b>Definition:</b>
     * A patient, practitioner, or organization for which this set of documents is intended
     * </p> 
	 */
	public DocumentManifest setRecipient(java.util.List<ResourceReferenceDt> theValue) {
		myRecipient = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>recipient</b> (Intended to get notified about this set of documents)
	 *
     * <p>
     * <b>Definition:</b>
     * A patient, practitioner, or organization for which this set of documents is intended
     * </p> 
	 */
	public ResourceReferenceDt addRecipient() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getRecipient().add(newType);
		return newType; 
	}
  
	/**
	 * Gets the value(s) for <b>type</b> (What kind of document set this is).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Specifies the kind of this set of documents (e.g. Patient Summary, Discharge Summary, Prescription, etc.). The type of a set of documents may be the same as one of the documents in it - especially if there is only one - but it may be wider
     * </p> 
	 */
	public CodeableConceptDt getType() {  
		if (myType == null) {
			myType = new CodeableConceptDt();
		}
		return myType;
	}

	/**
	 * Sets the value(s) for <b>type</b> (What kind of document set this is)
	 *
     * <p>
     * <b>Definition:</b>
     * Specifies the kind of this set of documents (e.g. Patient Summary, Discharge Summary, Prescription, etc.). The type of a set of documents may be the same as one of the documents in it - especially if there is only one - but it may be wider
     * </p> 
	 */
	public DocumentManifest setType(CodeableConceptDt theValue) {
		myType = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>author</b> (Who and/or what authored the document).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies who is responsible for adding the information to the document
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getAuthor() {  
		return myAuthor;
	}

	/**
	 * Sets the value(s) for <b>author</b> (Who and/or what authored the document)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies who is responsible for adding the information to the document
     * </p> 
	 */
	public DocumentManifest setAuthor(java.util.List<ResourceReferenceDt> theValue) {
		myAuthor = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>author</b> (Who and/or what authored the document)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies who is responsible for adding the information to the document
     * </p> 
	 */
	public ResourceReferenceDt addAuthor() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getAuthor().add(newType);
		return newType; 
	}
  
	/**
	 * Gets the value(s) for <b>created</b> (When this document manifest created).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * When the document manifest was created for submission to the server (not necessarily the same thing as the actual resource last modified time, since it may be modified, replicated etc)
     * </p> 
	 */
	public DateTimeDt getCreated() {  
		if (myCreated == null) {
			myCreated = new DateTimeDt();
		}
		return myCreated;
	}

	/**
	 * Sets the value(s) for <b>created</b> (When this document manifest created)
	 *
     * <p>
     * <b>Definition:</b>
     * When the document manifest was created for submission to the server (not necessarily the same thing as the actual resource last modified time, since it may be modified, replicated etc)
     * </p> 
	 */
	public DocumentManifest setCreated(DateTimeDt theValue) {
		myCreated = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>created</b> (When this document manifest created)
	 *
     * <p>
     * <b>Definition:</b>
     * When the document manifest was created for submission to the server (not necessarily the same thing as the actual resource last modified time, since it may be modified, replicated etc)
     * </p> 
	 */
	public DocumentManifest setCreated( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myCreated = new DateTimeDt(theDate, thePrecision); 
		return this; 
	}

	/**
	 * Sets the value for <b>created</b> (When this document manifest created)
	 *
     * <p>
     * <b>Definition:</b>
     * When the document manifest was created for submission to the server (not necessarily the same thing as the actual resource last modified time, since it may be modified, replicated etc)
     * </p> 
	 */
	public DocumentManifest setCreatedWithSecondsPrecision( Date theDate) {
		myCreated = new DateTimeDt(theDate); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>source</b> (The source system/application/software).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the source system, application, or software that produced the document manifest
     * </p> 
	 */
	public UriDt getSource() {  
		if (mySource == null) {
			mySource = new UriDt();
		}
		return mySource;
	}

	/**
	 * Sets the value(s) for <b>source</b> (The source system/application/software)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the source system, application, or software that produced the document manifest
     * </p> 
	 */
	public DocumentManifest setSource(UriDt theValue) {
		mySource = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>source</b> (The source system/application/software)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the source system, application, or software that produced the document manifest
     * </p> 
	 */
	public DocumentManifest setSource( String theUri) {
		mySource = new UriDt(theUri); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>status</b> (current | superceded | entered in error).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The status of this document manifest
     * </p> 
	 */
	public BoundCodeDt<DocumentReferenceStatusEnum> getStatus() {  
		if (myStatus == null) {
			myStatus = new BoundCodeDt<DocumentReferenceStatusEnum>(DocumentReferenceStatusEnum.VALUESET_BINDER);
		}
		return myStatus;
	}

	/**
	 * Sets the value(s) for <b>status</b> (current | superceded | entered in error)
	 *
     * <p>
     * <b>Definition:</b>
     * The status of this document manifest
     * </p> 
	 */
	public DocumentManifest setStatus(BoundCodeDt<DocumentReferenceStatusEnum> theValue) {
		myStatus = theValue;
		return this;
	}

	/**
	 * Sets the value(s) for <b>status</b> (current | superceded | entered in error)
	 *
     * <p>
     * <b>Definition:</b>
     * The status of this document manifest
     * </p> 
	 */
	public DocumentManifest setStatus(DocumentReferenceStatusEnum theValue) {
		getStatus().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>supercedes</b> (If this document manifest replaces another).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Whether this document manifest replaces another
     * </p> 
	 */
	public ResourceReferenceDt getSupercedes() {  
		if (mySupercedes == null) {
			mySupercedes = new ResourceReferenceDt();
		}
		return mySupercedes;
	}

	/**
	 * Sets the value(s) for <b>supercedes</b> (If this document manifest replaces another)
	 *
     * <p>
     * <b>Definition:</b>
     * Whether this document manifest replaces another
     * </p> 
	 */
	public DocumentManifest setSupercedes(ResourceReferenceDt theValue) {
		mySupercedes = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>description</b> (Human-readable description (title)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Human-readable description of the source document. This is sometimes known as the \"title\"
     * </p> 
	 */
	public StringDt getDescription() {  
		if (myDescription == null) {
			myDescription = new StringDt();
		}
		return myDescription;
	}

	/**
	 * Sets the value(s) for <b>description</b> (Human-readable description (title))
	 *
     * <p>
     * <b>Definition:</b>
     * Human-readable description of the source document. This is sometimes known as the \"title\"
     * </p> 
	 */
	public DocumentManifest setDescription(StringDt theValue) {
		myDescription = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>description</b> (Human-readable description (title))
	 *
     * <p>
     * <b>Definition:</b>
     * Human-readable description of the source document. This is sometimes known as the \"title\"
     * </p> 
	 */
	public DocumentManifest setDescription( String theString) {
		myDescription = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>confidentiality</b> (Sensitivity of set of documents).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A code specifying the level of confidentiality of this set of Documents
     * </p> 
	 */
	public CodeableConceptDt getConfidentiality() {  
		if (myConfidentiality == null) {
			myConfidentiality = new CodeableConceptDt();
		}
		return myConfidentiality;
	}

	/**
	 * Sets the value(s) for <b>confidentiality</b> (Sensitivity of set of documents)
	 *
     * <p>
     * <b>Definition:</b>
     * A code specifying the level of confidentiality of this set of Documents
     * </p> 
	 */
	public DocumentManifest setConfidentiality(CodeableConceptDt theValue) {
		myConfidentiality = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>content</b> (Contents of this set of documents).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The list of resources that describe the parts of this document reference. Usually, these would be document references, but direct references to binary attachments and images are also allowed
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getContent() {  
		return myContent;
	}

	/**
	 * Sets the value(s) for <b>content</b> (Contents of this set of documents)
	 *
     * <p>
     * <b>Definition:</b>
     * The list of resources that describe the parts of this document reference. Usually, these would be document references, but direct references to binary attachments and images are also allowed
     * </p> 
	 */
	public DocumentManifest setContent(java.util.List<ResourceReferenceDt> theValue) {
		myContent = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>content</b> (Contents of this set of documents)
	 *
     * <p>
     * <b>Definition:</b>
     * The list of resources that describe the parts of this document reference. Usually, these would be document references, but direct references to binary attachments and images are also allowed
     * </p> 
	 */
	public ResourceReferenceDt addContent() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getContent().add(newType);
		return newType; 
	}
  


}
