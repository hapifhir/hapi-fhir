/*-
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
package ca.uhn.fhir.util;

import ca.uhn.fhir.context.FhirContext;
import jakarta.annotation.Nonnull;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseParameters;

/**
 * This utility class can be used to create
 * <a href="https://smilecdr.com/fhir_standard/fhir_patch.html">FHIRPath Patch</a>
 * documents.
 *
 * @since 8.6.0
 */
public class FhirPatchBuilder {
	public static final String PARAMETER_ALLOW_MULTIPLE_MATCHES = "allowMultipleMatches";

	private final FhirContext myContext;
	private final IBaseParameters myPatch;

	public FhirPatchBuilder(@Nonnull FhirContext theFhirContext) {
		Validate.notNull(theFhirContext, "theFhirContext must not be null");
		myContext = theFhirContext;
		myPatch = ParametersUtil.newInstance(myContext);
	}

	/**
	 * Add a new <b>ADD</b> operation to the FHIR Patch
	 */
	public IAddStep1 add() {
		return new AddBuilder();
	}

	/**
	 * Add a new <b>ADD</b> operation to the FHIR Patch
	 */
	public IInsertStep1 insert() {
		return new InsertBuilder();
	}

	/**
	 * Add a new <b>DELETE</b> operation to the FHIR Patch
	 */
	public IDeleteStep1 delete() {
		return new DeleteBuilder();
	}

	/**
	 * Add a new <b>REPLACE</b> operation to the FHIR Patch
	 */
	public IReplaceStep1 replace() {
		return new ReplaceBuilder();
	}

	/**
	 * Add a new <b>MOVE</b> operation to the FHIR Patch
	 */
	public IMoveStep1 move() {
		return new MoveBuilder();
	}

	/**
	 * Create and return the generated FHIRPath Patch Parameters document.
	 */
	public IBaseParameters build() {
		return myPatch;
	}

	/*
	 * NOTE: See the JavaDoc for BaseOperationBuilder below for an explanation
	 * of all the interfaces that follow.
	 */

	/**
	 * This interface is returned after the final property for a given operation.
	 */
	public interface IStepComplete {

		/**
		 * @return Returns a reference to the {@link FhirPatchBuilder} instance, so
		 * 	that additional operations can be added to the chain, or {@link #build()}
		 * 	can be called to return the created patch document.
		 */
		FhirPatchBuilder andThen();
	}

	/**
	 * Interface exposing the <code>path</code> property
	 *
	 * @param <T> The interface corresponding to the next step
	 */
	public interface IStepPath<T> {

		/**
		 * The path to the element which will have an element added to it.
		 */
		T path(String thePath);
	}

	/**
	 * Interface exposing the <code>name</code> property
	 *
	 * @param <T> The interface corresponding to the next step
	 */
	public interface IStepName<T> {

		/**
		 * The name of the element to add
		 */
		T name(String theName);
	}

	/**
	 * Interface exposing the <code>value</code> property
	 *
	 * @param <T> The interface corresponding to the next step
	 */
	public interface IStepValue<T> {

		/**
		 * The new value
		 */
		T value(IBase theValue);
	}

	/**
	 * Interface exposing the <code>index</code> property
	 *
	 * @param <T> The interface corresponding to the next step
	 */
	public interface IStepIndex<T> {

		/**
		 * An index associated with the elements at the given path
		 */
		T index(int theIndex);
	}

	/**
	 * Interface exposing the <code>source</code> property
	 *
	 * @param <T> The interface corresponding to the next step
	 */
	public interface IStepSource<T> {

		/**
		 * An index associated with the elements at the given path
		 */
		T source(int theIndex);
	}

	/**
	 * Interface exposing the <code>destination</code> property
	 *
	 * @param <T> The interface corresponding to the next step
	 */
	public interface IStepDestination<T> {

		/**
		 * An index associated with the elements at the given path
		 */
		T destination(int theIndex);
	}

	/**
	 * Step 1 for creating an <b>ADD</b> operation, returned by
	 * calling {@link #add()}
	 */
	public interface IAddStep1 extends IStepPath<IAddStep2> {}

	public interface IAddStep2 extends IStepName<IAddStep3> {}

	public interface IAddStep3 extends IStepValue<IStepComplete> {}

	/**
	 * Step 1 for creating an <b>INSERT</b> operation, returned by
	 * calling {@link #insert()}
	 */
	public interface IInsertStep1 extends IStepPath<IInsertStep2> {}

	public interface IInsertStep2 extends IStepIndex<IInsertStep3> {}

	public interface IInsertStep3 extends IStepValue<IStepComplete> {}

	/**
	 * Step 1 for creating an <b>DELETE</b> operation, returned by
	 * calling {@link #delete()}
	 */
	public interface IDeleteStep1 extends IStepPath<IDeleteStepAfter> {}

	public interface IDeleteStepAfter extends IStepComplete {

		/**
		 * Marks this operation as allowing multiple matches to be deleted. Per the FHIR Patch
		 * specification, it is an error for the path to match multiple elements. This
		 * method prevents the existence of multiple matches from causing an error, and deletes
		 * all matches.
		 * <p>
		 * Note: This method adds a HAPI FHIR-specific parameter to the patch document and may
		 * not work on other implementations.
		 * </p>
		 */
		@SuppressWarnings("UnusedReturnValue")
		IDeleteStepAfter allowMultipleMatches();
	}

	/**
	 * Step 1 for creating an <b>REPLACE</b> operation, returned by
	 * calling {@link #replace()}
	 */
	public interface IReplaceStep1 extends IStepPath<IReplaceStep2> {}

	public interface IReplaceStep2 extends IStepValue<IStepComplete> {}

	/**
	 * Step 1 for creating a <b>MOVE</b> operation, returned by
	 * calling {@link #move()}
	 */
	public interface IMoveStep1 extends IStepPath<IMoveStep2> {}

	public interface IMoveStep2 extends IStepSource<IMoveStep3> {}

	public interface IMoveStep3 extends IStepDestination<IStepComplete> {}

	/**
	 * An instance of {@literal BaseOperationBuilder} is created for each new operation being
	 * created. This class has setters for each of the potential properties that the different
	 * patch operations can have, e.g. {@link #add()} takes "path", "name", and "value",
	 * {@link #delete()} takes only "path", etc.
	 * <p>
	 * This class uses some generics trickery to guide the creation of patch operations so that
	 * only the actual applicable properties are exposed depending on the specific operation.
	 * </p>
	 * <p>
	 * Each operation (add, delete, etc.) also has a concrete subclass of this class which actually
	 * builds the <code>Parameters.parameter</code> entry for the given operation type.
	 * </p>
	 *
	 * @param <RET_PATH>        The interface representing the next step after adding the path property
	 * @param <RET_NAME>        The interface representing the next step after adding the name property
	 * @param <RET_VALUE>       The interface representing the next step after adding the value property
	 * @param <RET_INDEX>       The interface representing the next step after adding the index property
	 * @param <RET_SOURCE>      The interface representing the next step after adding the source property
	 * @param <RET_DESTINATION> The interface representing the next step after adding the destination property
	 */
	private abstract class BaseOperationBuilder<RET_PATH, RET_NAME, RET_VALUE, RET_INDEX, RET_SOURCE, RET_DESTINATION>
			implements IStepPath<RET_PATH>,
					IStepName<RET_NAME>,
					IStepValue<RET_VALUE>,
					IStepIndex<RET_INDEX>,
					IStepSource<RET_SOURCE>,
					IStepDestination<RET_DESTINATION>,
					IStepComplete {

		protected String myPath;
		protected String myName;
		protected IBase myValue;
		protected Integer myIndex;
		protected Integer mySource;
		protected Integer myDestination;

		protected BaseOperationBuilder() {
			super();
		}

		@SuppressWarnings("unchecked")
		@Override
		public RET_PATH path(String thePath) {
			Validate.notBlank(thePath, "thePath must not be blank");
			myPath = thePath;
			return (RET_PATH) this;
		}

		@SuppressWarnings("unchecked")
		@Override
		public RET_NAME name(String theName) {
			Validate.notBlank(theName, "theName must not be blank");
			myName = theName;
			return (RET_NAME) this;
		}

		@SuppressWarnings("unchecked")
		@Override
		public RET_VALUE value(@Nonnull IBase theValue) {
			Validate.notNull(theValue, "theValue must not be null");
			myValue = theValue;
			return (RET_VALUE) this;
		}

		@SuppressWarnings("unchecked")
		@Override
		public RET_INDEX index(int theIndex) {
			Validate.isTrue(theIndex >= 0, "theIndex must not be negative");
			myIndex = theIndex;
			return (RET_INDEX) this;
		}

		@SuppressWarnings("unchecked")
		@Override
		public RET_DESTINATION destination(int theIndex) {
			Validate.isTrue(theIndex >= 0, "theIndex must not be negative");
			myDestination = theIndex;
			return (RET_DESTINATION) this;
		}

		@SuppressWarnings("unchecked")
		@Override
		public RET_SOURCE source(int theIndex) {
			Validate.isTrue(theIndex >= 0, "theIndex must not be negative");
			mySource = theIndex;
			return (RET_SOURCE) this;
		}

		@Override
		public FhirPatchBuilder andThen() {
			return FhirPatchBuilder.this;
		}
	}

	private class AddBuilder extends BaseOperationBuilder<IAddStep2, IAddStep3, IStepComplete, Void, Void, Void>
			implements IAddStep1, IAddStep2, IAddStep3 {

		@Override
		public IStepComplete value(@Nonnull IBase theValue) {
			super.value(theValue);

			IBase operation = ParametersUtil.addParameterToParameters(myContext, myPatch, "operation");
			ParametersUtil.addPartString(myContext, operation, "type", "add");
			ParametersUtil.addPartString(myContext, operation, "path", myPath);
			ParametersUtil.addPartString(myContext, operation, "name", myName);
			ParametersUtil.addPart(myContext, operation, "value", myValue);

			return this;
		}
	}

	private class InsertBuilder
			extends BaseOperationBuilder<IInsertStep2, Void, IStepComplete, IInsertStep3, Void, Void>
			implements IInsertStep1, IInsertStep2, IInsertStep3 {

		@Override
		public IStepComplete value(@Nonnull IBase theValue) {
			super.value(theValue);

			IBase operation = ParametersUtil.addParameterToParameters(myContext, myPatch, "operation");
			ParametersUtil.addPartString(myContext, operation, "type", "insert");
			ParametersUtil.addPartString(myContext, operation, "path", myPath);
			ParametersUtil.addPartInteger(myContext, operation, "index", myIndex);
			ParametersUtil.addPart(myContext, operation, "value", myValue);

			return this;
		}
	}

	private class DeleteBuilder extends BaseOperationBuilder<IDeleteStepAfter, Void, Void, Void, Void, Void>
			implements IDeleteStep1, IDeleteStepAfter {

		private IBase myOperation;
		private IBase myAllowMultipleMatches;

		@Override
		public IDeleteStepAfter allowMultipleMatches() {
			if (myAllowMultipleMatches == null) {
				myAllowMultipleMatches =
						ParametersUtil.addPartBoolean(myContext, myOperation, PARAMETER_ALLOW_MULTIPLE_MATCHES, true);
			}
			return this;
		}

		@Override
		public IDeleteStepAfter path(String thePath) {
			super.path(thePath);

			myOperation = ParametersUtil.addParameterToParameters(myContext, myPatch, "operation");
			ParametersUtil.addPartString(myContext, myOperation, "type", "delete");
			ParametersUtil.addPartString(myContext, myOperation, "path", myPath);

			return this;
		}
	}

	private class ReplaceBuilder extends BaseOperationBuilder<IReplaceStep2, Void, IStepComplete, Void, Void, Void>
			implements IReplaceStep1, IReplaceStep2 {

		@Override
		public IStepComplete value(@Nonnull IBase theValue) {
			super.value(theValue);

			IBase operation = ParametersUtil.addParameterToParameters(myContext, myPatch, "operation");
			ParametersUtil.addPartString(myContext, operation, "type", "replace");
			ParametersUtil.addPartString(myContext, operation, "path", myPath);
			ParametersUtil.addPart(myContext, operation, "value", myValue);

			return this;
		}
	}

	private class MoveBuilder extends BaseOperationBuilder<IMoveStep2, Void, Void, Void, IMoveStep3, IStepComplete>
			implements IMoveStep1, IMoveStep2, IMoveStep3 {

		@Override
		public IStepComplete destination(int theDestination) {
			super.destination(theDestination);

			IBase operation = ParametersUtil.addParameterToParameters(myContext, myPatch, "operation");
			ParametersUtil.addPartString(myContext, operation, "type", "move");
			ParametersUtil.addPartString(myContext, operation, "path", myPath);
			ParametersUtil.addPartInteger(myContext, operation, "source", mySource);
			ParametersUtil.addPartInteger(myContext, operation, "destination", myDestination);

			return this;
		}
	}
}
