package ca.uhn.fhir.util;

import ca.uhn.fhir.context.FhirContext;
import jakarta.annotation.Nonnull;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseParameters;

public class FhirPatchBuilder {

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


	public IBaseParameters build() {
		return myPatch;
	}

	public interface IStepComplete {
		// nothing
	}


	public interface IStepPath<T> {

		/**
		 * The path to the element which will have an element added to it.
		 */
		T path(String thePath);

	}

	public interface IStepName<T> {

		/**
		 * The name of the element to add
		 */
		T name(String theName);

	}

	public interface IStepValue<T> {

		/**
		 * The new value
		 */
		T value(IBase theValue);

	}

	public interface IStepIndex<T> {

		/**
		 * An index associated with the elements at the given path
		 */
		T index(int theIndex);

	}

	public interface IStepSource<T> {

		/**
		 * An index associated with the elements at the given path
		 */
		T source(int theIndex);

	}

	public interface IStepDestination<T> {

		/**
		 * An index associated with the elements at the given path
		 */
		T destination(int theIndex);

	}

	private static class BaseOperationBuilder<RET_PATH, RET_NAME, RET_VALUE, RET_INDEX, RET_SOURCE, RET_DESTINATION> implements IStepPath<RET_PATH>, IStepName<RET_NAME>, IStepValue<RET_VALUE>, IStepIndex<RET_INDEX>, IStepSource<RET_SOURCE>, IStepDestination<RET_DESTINATION>, IStepComplete {

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
	}


	public interface IAddStep1 extends IStepPath<IAddStep2> {

	}

	public interface IAddStep2 extends IStepName<IAddStep3> {

	}

	public interface IAddStep3 extends IStepValue<IStepComplete> {

	}

	private class AddBuilder extends BaseOperationBuilder<IAddStep2, IAddStep3, IStepComplete, Void, Void, Void> implements IAddStep1, IAddStep2, IAddStep3 {

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

	public interface IInsertStep1 extends IStepPath<IInsertStep2> {

	}

	public interface IInsertStep2 extends IStepIndex<IInsertStep3> {

	}

	public interface IInsertStep3 extends IStepValue<IStepComplete> {

	}

	private class InsertBuilder extends BaseOperationBuilder<IInsertStep2, Void, IStepComplete, IInsertStep3, Void, Void> implements IInsertStep1, IInsertStep2, IInsertStep3 {

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


	public interface IDeleteStep1 extends IStepPath<IStepComplete> {}

	private class DeleteBuilder extends BaseOperationBuilder<IStepComplete, Void, Void, Void, Void, Void> implements IDeleteStep1 {

		@Override
		public IStepComplete path(String thePath) {
			super.path(thePath);

			IBase operation = ParametersUtil.addParameterToParameters(myContext, myPatch, "operation");
			ParametersUtil.addPartString(myContext, operation, "type", "delete");
			ParametersUtil.addPartString(myContext, operation, "path", myPath);

			return this;
		}
	}


	public interface IReplaceStep1 extends IStepPath<IReplaceStep2> {}

	public interface IReplaceStep2 extends IStepValue<IStepComplete> {}

	private class ReplaceBuilder extends BaseOperationBuilder<IReplaceStep2, Void, IStepComplete, Void, Void, Void> implements IReplaceStep1, IReplaceStep2 {

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



	public interface IMoveStep1 extends IStepPath<IMoveStep2> {}

	public interface IMoveStep2 extends IStepSource<IMoveStep3> {}

	public interface IMoveStep3 extends IStepDestination<IStepComplete> {}

	private class MoveBuilder extends BaseOperationBuilder<IMoveStep2, Void, Void, Void, IMoveStep3, IStepComplete> implements IMoveStep1, IMoveStep2, IMoveStep3 {

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
