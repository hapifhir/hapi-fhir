package ca.uhn.fhir.rest.param;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.model.api.ICompositeDatatype;
import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.base.composite.BaseCodingDt;
import ca.uhn.fhir.model.base.composite.BaseResourceReferenceDt;
import ca.uhn.fhir.model.primitive.BooleanDt;
import ca.uhn.fhir.model.primitive.CodeDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.model.primitive.UriDt;
import ca.uhn.fhir.util.CoverageIgnore;

import java.util.List;

@CoverageIgnore
public class InternalCodingDt extends BaseCodingDt implements ICompositeDatatype {

	private static final long serialVersionUID = 993056016725918652L;

	/**
	 * Constructor
	 */
	public InternalCodingDt() {
		super();
	}

	/**
	 * Creates a new Coding with the given system and code
	 */
	public InternalCodingDt(String theSystem, String theCode) {
		setSystem(theSystem);
		setCode(theCode);
	}

	@Child(name = "system", type = UriDt.class, order = 0, min = 0, max = 1)
	@Description(shortDefinition = "Identity of the terminology system", formalDefinition = "The identification of the code system that defines the meaning of the symbol in the code.")
	private UriDt mySystem;

	@Child(name = "version", type = StringDt.class, order = 1, min = 0, max = 1)
	@Description(shortDefinition = "Version of the system - if relevant", formalDefinition = "The version of the code system which was used when choosing this code. Note that a well-maintained code system does not need the version reported, because the meaning of codes is consistent across versions. However this cannot consistently be assured. and When the meaning is not guaranteed to be consistent, the version SHOULD be exchanged")
	private StringDt myVersion;

	@Child(name = "code", type = CodeDt.class, order = 2, min = 0, max = 1)
	@Description(shortDefinition = "Symbol in syntax defined by the system", formalDefinition = "A symbol in syntax defined by the system. The symbol may be a predefined code or an expression in a syntax defined by the coding system (e.g. post-coordination)")
	private CodeDt myCode;

	@Child(name = "display", type = StringDt.class, order = 3, min = 0, max = 1)
	@Description(shortDefinition = "Representation defined by the system", formalDefinition = "A representation of the meaning of the code in the system, following the rules of the system.")
	private StringDt myDisplay;

	@Child(name = "primary", type = BooleanDt.class, order = 4, min = 0, max = 1)
	@Description(shortDefinition = "If this code was chosen directly by the user", formalDefinition = "Indicates that this code was chosen by a user directly - i.e. off a pick list of available items (codes or displays)")
	private BooleanDt myPrimary;

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(mySystem, myVersion, myCode, myDisplay, myPrimary);
	}

	@Deprecated //override deprecated method
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, mySystem, myVersion, myCode, myDisplay, myPrimary);
	}

	/**
	 * Gets the value(s) for <b>system</b> (Identity of the terminology system). creating it if it does not exist. Will not return <code>null</code>.
	 *
	 * <p>
	 * <b>Definition:</b> The identification of the code system that defines the meaning of the symbol in the code.
	 * </p>
	 */
	@Override
	public UriDt getSystemElement() {
		if (mySystem == null) {
			mySystem = new UriDt();
		}
		return mySystem;
	}

	/**
	 * Sets the value(s) for <b>system</b> (Identity of the terminology system)
	 *
	 * <p>
	 * <b>Definition:</b> The identification of the code system that defines the meaning of the symbol in the code.
	 * </p>
	 */
	public InternalCodingDt setSystem(UriDt theValue) {
		mySystem = theValue;
		return this;
	}

	/**
	 * Sets the value for <b>system</b> (Identity of the terminology system)
	 *
	 * <p>
	 * <b>Definition:</b> The identification of the code system that defines the meaning of the symbol in the code.
	 * </p>
	 */
	@Override
	public InternalCodingDt setSystem(String theUri) {
		mySystem = new UriDt(theUri);
		return this;
	}

	/**
	 * Gets the value(s) for <b>version</b> (Version of the system - if relevant). creating it if it does not exist. Will not return <code>null</code>.
	 *
	 * <p>
	 * <b>Definition:</b> The version of the code system which was used when choosing this code. Note that a well-maintained code system does not need the version reported, because the meaning of codes
	 * is consistent across versions. However this cannot consistently be assured. and When the meaning is not guaranteed to be consistent, the version SHOULD be exchanged
	 * </p>
	 */
	public StringDt getVersion() {
		if (myVersion == null) {
			myVersion = new StringDt();
		}
		return myVersion;
	}

	/**
	 * Sets the value(s) for <b>version</b> (Version of the system - if relevant)
	 *
	 * <p>
	 * <b>Definition:</b> The version of the code system which was used when choosing this code. Note that a well-maintained code system does not need the version reported, because the meaning of codes
	 * is consistent across versions. However this cannot consistently be assured. and When the meaning is not guaranteed to be consistent, the version SHOULD be exchanged
	 * </p>
	 */
	public InternalCodingDt setVersion(StringDt theValue) {
		myVersion = theValue;
		return this;
	}

	/**
	 * Sets the value for <b>version</b> (Version of the system - if relevant)
	 *
	 * <p>
	 * <b>Definition:</b> The version of the code system which was used when choosing this code. Note that a well-maintained code system does not need the version reported, because the meaning of codes
	 * is consistent across versions. However this cannot consistently be assured. and When the meaning is not guaranteed to be consistent, the version SHOULD be exchanged
	 * </p>
	 */
	public InternalCodingDt setVersion(String theString) {
		myVersion = new StringDt(theString);
		return this;
	}

	/**
	 * Gets the value(s) for <b>code</b> (Symbol in syntax defined by the system). creating it if it does not exist. Will not return <code>null</code>.
	 *
	 * <p>
	 * <b>Definition:</b> A symbol in syntax defined by the system. The symbol may be a predefined code or an expression in a syntax defined by the coding system (e.g. post-coordination)
	 * </p>
	 */
	@Override
	public CodeDt getCodeElement() {
		if (myCode == null) {
			myCode = new CodeDt();
		}
		return myCode;
	}

	/**
	 * Sets the value(s) for <b>code</b> (Symbol in syntax defined by the system)
	 *
	 * <p>
	 * <b>Definition:</b> A symbol in syntax defined by the system. The symbol may be a predefined code or an expression in a syntax defined by the coding system (e.g. post-coordination)
	 * </p>
	 */
	public InternalCodingDt setCode(CodeDt theValue) {
		myCode = theValue;
		return this;
	}

	/**
	 * Sets the value for <b>code</b> (Symbol in syntax defined by the system)
	 *
	 * <p>
	 * <b>Definition:</b> A symbol in syntax defined by the system. The symbol may be a predefined code or an expression in a syntax defined by the coding system (e.g. post-coordination)
	 * </p>
	 */
	@Override
	public InternalCodingDt setCode(String theCode) {
		myCode = new CodeDt(theCode);
		return this;
	}

	/**
	 * Gets the value(s) for <b>display</b> (Representation defined by the system). creating it if it does not exist. Will not return <code>null</code>.
	 *
	 * <p>
	 * <b>Definition:</b> A representation of the meaning of the code in the system, following the rules of the system.
	 * </p>
	 */
	public StringDt getDisplay() {
		if (myDisplay == null) {
			myDisplay = new StringDt();
		}
		return myDisplay;
	}

	/**
	 * Sets the value(s) for <b>display</b> (Representation defined by the system)
	 *
	 * <p>
	 * <b>Definition:</b> A representation of the meaning of the code in the system, following the rules of the system.
	 * </p>
	 */
	public InternalCodingDt setDisplay(StringDt theValue) {
		myDisplay = theValue;
		return this;
	}

	/**
	 * Sets the value for <b>display</b> (Representation defined by the system)
	 *
	 * <p>
	 * <b>Definition:</b> A representation of the meaning of the code in the system, following the rules of the system.
	 * </p>
	 */
	@Override
	public InternalCodingDt setDisplay(String theString) {
		myDisplay = new StringDt(theString);
		return this;
	}

	/**
	 * Gets the value(s) for <b>primary</b> (If this code was chosen directly by the user). creating it if it does not exist. Will not return <code>null</code>.
	 *
	 * <p>
	 * <b>Definition:</b> Indicates that this code was chosen by a user directly - i.e. off a pick list of available items (codes or displays)
	 * </p>
	 */
	public BooleanDt getPrimary() {
		if (myPrimary == null) {
			myPrimary = new BooleanDt();
		}
		return myPrimary;
	}

	/**
	 * Sets the value(s) for <b>primary</b> (If this code was chosen directly by the user)
	 *
	 * <p>
	 * <b>Definition:</b> Indicates that this code was chosen by a user directly - i.e. off a pick list of available items (codes or displays)
	 * </p>
	 */
	public InternalCodingDt setPrimary(BooleanDt theValue) {
		myPrimary = theValue;
		return this;
	}

	/**
	 * Sets the value for <b>primary</b> (If this code was chosen directly by the user)
	 *
	 * <p>
	 * <b>Definition:</b> Indicates that this code was chosen by a user directly - i.e. off a pick list of available items (codes or displays)
	 * </p>
	 */
	public InternalCodingDt setPrimary(boolean theBoolean) {
		myPrimary = new BooleanDt(theBoolean);
		return this;
	}

	/**
	 * Gets the value(s) for <b>valueSet</b> (Set this coding was chosen from). creating it if it does not exist. Will not return <code>null</code>.
	 *
	 * <p>
	 * <b>Definition:</b> The set of possible coded values this coding was chosen from or constrained by
	 * </p>
	 */
	public BaseResourceReferenceDt getValueSet() {
		throw new UnsupportedOperationException(Msg.code(1949));
	}

	@Override
	public StringDt getDisplayElement() {
		return getDisplay();
	}

	@Deprecated //override deprecated method
	@Override
	public Boolean getMissing() {
		throw new UnsupportedOperationException(Msg.code(1950));
	}

	@Deprecated //override deprecated method
	@Override
	public IQueryParameterType setMissing(Boolean theMissing) {
		throw new UnsupportedOperationException(Msg.code(1951));
	}

}
