package ca.uhn.fhir.fp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementCompositeDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.fp.FhirPathCompiler_Bak.BinopOperator;
import ca.uhn.fhir.fp.FhirPathCompiler_Bak.MyBaseState;
import ca.uhn.fhir.fp.antlr.fhirpathLexer;
import ca.uhn.fhir.fp.antlr.fhirpathListener;
import ca.uhn.fhir.fp.antlr.fhirpathParser;
import ca.uhn.fhir.fp.antlr.fhirpathParser.Axis_specContext;
import ca.uhn.fhir.fp.antlr.fhirpathParser.BinopContext;
import ca.uhn.fhir.fp.antlr.fhirpathParser.Binop_operatorContext;
import ca.uhn.fhir.fp.antlr.fhirpathParser.ElementContext;
import ca.uhn.fhir.fp.antlr.fhirpathParser.ExprContext;
import ca.uhn.fhir.fp.antlr.fhirpathParser.Expr_no_binopContext;
import ca.uhn.fhir.fp.antlr.fhirpathParser.FpconstContext;
import ca.uhn.fhir.fp.antlr.fhirpathParser.FunctionContext;
import ca.uhn.fhir.fp.antlr.fhirpathParser.ItemContext;
import ca.uhn.fhir.fp.antlr.fhirpathParser.LineContext;
import ca.uhn.fhir.fp.antlr.fhirpathParser.Param_listContext;
import ca.uhn.fhir.fp.antlr.fhirpathParser.PredicateContext;
import ca.uhn.fhir.fp.antlr.fhirpathParser.ProgContext;
import ca.uhn.fhir.fp.antlr.fhirpathParser.RecurseContext;
import ca.uhn.fhir.model.primitive.BooleanDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.util.ObjectUtil;
import net.sourceforge.cobertura.CoverageIgnore;

public class FhirPathCompiler_Bak {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirPathCompiler_Bak.class);

	private final FhirContext myCtx;
	private ExprContext myExpr;
	private fhirpathLexer myLexer;

	public FhirPathCompiler_Bak(FhirContext theCtx) {
		super();
		myCtx = theCtx;
	}

	public List<IBase> applyTo(IBase theApplyTo) {
		fhirpathParser parser = new fhirpathParser(new CommonTokenStream(myLexer));
		MyStateDelegator delegator = new MyStateDelegator(theApplyTo);
		parser.addParseListener(delegator);

		ourLog.info("Starting FHIRPath evaluation");
		parser.expr();

		return delegator.getOutput();
	}

	public void compile(String theExpression) {
		myLexer = new fhirpathLexer(new ANTLRInputStream(theExpression));
	}

	public class BinopOperator extends MyBaseState {

		private String myOperator;

		public BinopOperator(List<IBase> theContext) {
			super(theContext);
		}

		public String getOperator() {
			return myOperator;
		}

		@Override
		public void visitTerminal(TerminalNode theArg0) {
			myOperator = theArg0.getText();
		}

	}

	public class BinopState extends ExprNoBinopState {

		private MyBaseState myLeftOperand;

		private BinopOperator myOperator;
		private MyBaseState myRightOperand;

		public BinopState(List<IBase> theContext) {
			super(theContext);
		}

		private void addingState(MyBaseState theState) {
			if (myLeftOperand == null) {
				myLeftOperand = theState;
			} else {
				myRightOperand = theState;
			}
		}

		@Override
		public void enterBinop_operator(Binop_operatorContext theCtx) {
			myOperator = new BinopOperator(myContext);
			myDelegator.push(myOperator, this);
		}

		@Override
		public void enterExpr_no_binop(Expr_no_binopContext theCtx) {
			ExprNoBinopState state = new ExprNoBinopState(myContext);
			addingState(state);
			myDelegator.push(state, this);
		}

		private List<IBase> operationEquals(MyStateDelegator theDelegator, List<IBase> theLeftValues, List<IBase> theRightValues, String theOperator) {

			if (theLeftValues.size() != theRightValues.size()) {
				theDelegator.addInformation(theOperator + " operator returned empty collection because left hand side returned a different number of values from right hand side");
				return Collections.emptyList();
			}

			for (int i = 0; i < theLeftValues.size(); i++) {
				IBase leftValue = theLeftValues.get(i);
				IBase rightValue = theRightValues.get(i);
				if (!(leftValue.getClass().equals(rightValue.getClass()))) {
					theDelegator.addInformation(theOperator + " operator returned empty collection because left hand side returned a type from right hand side");
					return Collections.emptyList();
				}
				if (!(leftValue instanceof IPrimitiveType)) {
					theDelegator.addInformation(theOperator + " operator returned empty collection because left hand side returned a non-primitive type: " + leftValue.getClass().getSimpleName());
					return Collections.emptyList();
				}
				if (!(rightValue instanceof IPrimitiveType)) {
					theDelegator.addInformation(theOperator + " operator returned empty collection because right hand side returned a non-primitive type: " + rightValue.getClass().getSimpleName());
					return Collections.emptyList();
				}

				Object leftObject = ((IPrimitiveType<?>) leftValue).getValue();
				Object rightObject = ((IPrimitiveType<?>) rightValue).getValue();

				if ("=".equals(theOperator)) {
					return Collections.singletonList((IBase) new BooleanDt(ObjectUtil.equals(leftObject, rightObject)));
				} else {
					throw new DataFormatException("Unknown operator: " + theOperator);
				}
				// if ("=".equals(myOperator) || "~".equals(myOperator) || "!=".equals(myOperator) ||
				// "!~".equals(myOperator) || ">".equals(myOperator)) {
				// }

			}
			return Collections.singletonList((IBase) new BooleanDt(true));
		}

		@Override
		public void popping(MyBaseState theBaseState) {
			if (theBaseState == myLeftOperand) {
				return;
			}
			if (theBaseState == myOperator) {
				return;
			}

			List<IBase> leftValues = myLeftOperand.getOutput();
			List<IBase> rightValues = myRightOperand.getOutput();
			setOutput(operationEquals(myDelegator, leftValues, rightValues, myOperator.getOperator()));
		}

	}

	public class ExprNoBinopState extends MyBaseState {

		public ExprNoBinopState(List<IBase> theContext) {
			super(theContext);
		}

		@Override
		public void enterExpr(ExprContext theCtx) {
			myDelegator.push(new ExprState(myContext), this);
		}

		@Override
		public void enterFpconst(FpconstContext theCtx) {
			myDelegator.push(new FpConstState(myContext), this);
		}

		@Override
		public void enterPredicate(PredicateContext theCtx) {
			myDelegator.push(new PredicateState(myContext), this);
		}

		@Override
		public void popping(MyBaseState theBaseState) {
			setOutput(theBaseState.getOutput());
		}

	}

	public class ExprState extends MyBaseState {

		public ExprState(List<IBase> theContext) {
			super(theContext);
		}

		@Override
		public void enterBinop(BinopContext theCtx) {
			myDelegator.push(new BinopState(myContext), this);
		}

		@Override
		public void enterExpr_no_binop(Expr_no_binopContext theCtx) {
			myDelegator.push(new ExprNoBinopState(myContext), this);
		}

	}

	public class FpConstState extends MyBaseState {

		public FpConstState(List<IBase> theContext) {
			super(theContext);
		}

		@Override
		public void visitTerminal(TerminalNode theArg0) {
			IBase value = new StringDt(theArg0.getText());
			setOutput(Collections.singletonList(value));
		}

	}

	class FunctionParamListState extends MyBaseState {

		public FunctionParamListState(List<IBase> theContext) {
			super(theContext);
		}

		@Override
		public void enterExpr(ExprContext theCtx) {
			myDelegator.push(new ExprState(myContext), this);
		}

	}

	class FunctionState extends MyBaseState {

		private String myFunctionName;
		private ArrayList<MyBaseState> myCandidateStates;

		public FunctionState(List<IBase> theContext, String theFunctionName) {
			super(theContext);
			myFunctionName = theFunctionName;
		}

		@Override
		public void enterParam_list(Param_listContext theCtx) {
			if ("where".equals(myFunctionName)) {
				myCandidateStates = new ArrayList<MyBaseState>();
				for (IBase next : myContext) {
					myCandidateStates.add(new FunctionParamListState(Collections.singletonList(next)));
				}
				myDelegator.push(myCandidateStates, this);
			} else {
				throw new DataFormatException("Unknown function: " + myFunctionName);
			}
		}

		@Override
		public void beingPopped() {
			super.beingPopped();
		}

		@Override
		public void popping(MyBaseState theBaseState) {
			super.popping(theBaseState);
		}

	}

	public class InitialState extends MyBaseState {

		public InitialState(IBase theContext) {
			super(Collections.singletonList(theContext));
		}

		@Override
		public void enterExpr(ExprContext theCtx) {
			myDelegator.push(new ExprState(myContext), this);
		}

		@Override
		public void popping(MyBaseState theBaseState) {
			setOutput(theBaseState.getOutput());
		}

	}

	@CoverageIgnore
	public class MyBaseState implements fhirpathListener {

		protected List<IBase> myContext;
		MyStateDelegator myDelegator;
		protected List<IBase> myOutput;
		MyBaseState myParent;

		public MyBaseState(List<IBase> theContext) {
			myContext = theContext;
		}

		public void beingPopped() {
			// nothing
		}

		@Override
		public void enterAxis_spec(Axis_specContext theCtx) {
			throw new IllegalStateException("Unexpected event in state " + getClass().getSimpleName());
		}

		@Override
		public void enterBinop(BinopContext theCtx) {
			throw new IllegalStateException("Unexpected event in state " + getClass().getSimpleName());
		}

		@Override
		public void enterBinop_operator(Binop_operatorContext theCtx) {
			throw new IllegalStateException("Unexpected event in state " + getClass().getSimpleName());
		}

		@Override
		public void enterElement(ElementContext theCtx) {
			throw new IllegalStateException("Unexpected event in state " + getClass().getSimpleName());
		}

		@Override
		@CoverageIgnore
		public final void enterEveryRule(ParserRuleContext theArg0) {
			throw new IllegalStateException("Unexpected event in state " + getClass().getSimpleName());
		}

		@Override
		public void enterExpr(ExprContext theCtx) {
			throw new IllegalStateException("Unexpected event in state " + getClass().getSimpleName());
		}

		@Override
		public void enterExpr_no_binop(Expr_no_binopContext theCtx) {
			throw new IllegalStateException("Unexpected event in state " + getClass().getSimpleName());
		}

		@Override
		public void enterFpconst(FpconstContext theCtx) {
			throw new IllegalStateException("Unexpected event in state " + getClass().getSimpleName());

		}

		@Override
		public void enterFunction(FunctionContext theCtx) {
			throw new IllegalStateException("Unexpected event in state " + getClass().getSimpleName());

		}

		@Override
		public void enterItem(ItemContext theCtx) {
			throw new IllegalStateException("Unexpected event in state " + getClass().getSimpleName() + " with text: " + theCtx.getText());

		}

		@Override
		public void enterLine(LineContext theCtx) {
			throw new IllegalStateException("Unexpected event in state " + getClass().getSimpleName());

		}

		@Override
		public void enterParam_list(Param_listContext theCtx) {
			throw new IllegalStateException("Unexpected event in state " + getClass().getSimpleName());

		}

		@Override
		public void enterPredicate(PredicateContext theCtx) {
			throw new IllegalStateException("Unexpected event in state " + getClass().getSimpleName());

		}

		@Override
		public void enterProg(ProgContext theCtx) {
			throw new IllegalStateException("Unexpected event in state " + getClass().getSimpleName());

		}

		@Override
		public void enterRecurse(RecurseContext theCtx) {
			throw new IllegalStateException("Unexpected event in state " + getClass().getSimpleName());

		}

		@CoverageIgnore
		@Override
		public final void exitAxis_spec(Axis_specContext theCtx) {
			throw new IllegalStateException("Unexpected event in state " + getClass().getSimpleName());
		}

		@Override
		public final void exitBinop(BinopContext theCtx) {
			throw new IllegalStateException("Unexpected event in state " + getClass().getSimpleName());
		}

		@Override
		public final void exitBinop_operator(Binop_operatorContext theCtx) {
			throw new IllegalStateException("Unexpected event in state " + getClass().getSimpleName());
		}

		@Override
		@CoverageIgnore
		public final void exitElement(ElementContext theCtx) {
			throw new IllegalStateException("Unexpected event in state " + getClass().getSimpleName());
		}

		@Override
		@CoverageIgnore
		public final void exitEveryRule(ParserRuleContext theArg0) {
			throw new IllegalStateException("Unexpected event in state " + getClass().getSimpleName());
		}

		@Override
		@CoverageIgnore
		public final void exitExpr(ExprContext theCtx) {
			// throw new IllegalStateException("Unexpected event in state " + getClass().getSimpleName());
		}

		@Override
		public final void exitExpr_no_binop(Expr_no_binopContext theCtx) {
			throw new IllegalStateException("Unexpected event in state " + getClass().getSimpleName());
		}

		@Override
		@CoverageIgnore
		public final void exitFpconst(FpconstContext theCtx) {
			throw new IllegalStateException("Unexpected event in state " + getClass().getSimpleName());
		}

		@Override
		@CoverageIgnore
		public final void exitFunction(FunctionContext theCtx) {
			throw new IllegalStateException("Unexpected event in state " + getClass().getSimpleName());

		}

		@Override
		@CoverageIgnore
		public final void exitItem(ItemContext theCtx) {
			throw new IllegalStateException("Unexpected event in state " + getClass().getSimpleName());
		}

		@Override
		@CoverageIgnore
		public final void exitLine(LineContext theCtx) {
			throw new IllegalStateException("Unexpected event in state " + getClass().getSimpleName());

		}

		@Override
		@CoverageIgnore
		public final void exitParam_list(Param_listContext theCtx) {
			throw new IllegalStateException("Unexpected event in state " + getClass().getSimpleName());
		}

		@Override
		@CoverageIgnore
		public final void exitPredicate(PredicateContext theCtx) {
			throw new IllegalStateException("Unexpected event in state " + getClass().getSimpleName());
		}

		@CoverageIgnore
		@Override
		public final void exitProg(ProgContext theCtx) {
			throw new IllegalStateException("Unexpected event in state " + getClass().getSimpleName());

		}

		@Override
		@CoverageIgnore
		public final void exitRecurse(RecurseContext theCtx) {
			throw new IllegalStateException("Unexpected event in state " + getClass().getSimpleName());

		}

		List<IBase> getContext() {
			return myContext;
		}

		List<IBase> getOutput() {
			return myOutput;
		}

		public void popping(MyBaseState theBaseState) {
			setOutput(theBaseState.getOutput());
		}

		void setContext(List<IBase> theContext) {
			myContext = theContext;
		}

		public void setOutput(List<IBase> theOutput) {
			Validate.isTrue(theOutput == null || myOutput == null, "Error evaluating FHIRPath: can not set the output twice");
			myOutput = theOutput;
		}

		@Override
		public void visitErrorNode(ErrorNode theArg0) {
			throw new IllegalStateException("Unexpected event in state " + getClass().getSimpleName());

		}

		@Override
		public void visitTerminal(TerminalNode theArg0) {
			// ignore
		}

	}

	class MyStateDelegator implements fhirpathListener {

		private List<MyBaseState> myBaseState;
		private int myDepth;
		private Object myDepthLogger = new Object() {
			@Override
			public String toString() {
				StringBuilder b = new StringBuilder(myDepth);
				for (int i = 0; i < myDepth; i++) {
					b.append(' ');
				}
				return b.toString();
			}
		};

		public MyStateDelegator(IBase theApplyTo) {
			push(new InitialState(theApplyTo), null);
		}

		public void addInformation(String theString) {
			// TODO Auto-generated method stub

		}

		@Override
		public void enterAxis_spec(Axis_specContext theCtx) {
			ourLog.info("{}enterAxis_spec", new Object[] { myDepthLogger });
			for (MyBaseState nextBaseState : myBaseState) {
				nextBaseState.enterAxis_spec(theCtx);
			}
			myDepth++;
		}

		@Override
		public void enterBinop(BinopContext theCtx) {
			ourLog.info("{}enterBinop", new Object[] { myDepthLogger });
			for (MyBaseState nextBaseState : myBaseState) {
				nextBaseState.enterBinop(theCtx);
			}
			myDepth++;
		}

		@Override
		public void enterBinop_operator(Binop_operatorContext theCtx) {
			ourLog.info("{}enterBinop_operator", new Object[] { myDepthLogger });
			for (MyBaseState nextBaseState : myBaseState) {
				nextBaseState.enterBinop_operator(theCtx);
			}
			myDepth++;
		}

		@Override
		public void enterElement(ElementContext theCtx) {
			ourLog.info("{}enterElement", new Object[] { myDepthLogger });
			for (MyBaseState nextBaseState : myBaseState) {
				nextBaseState.enterElement(theCtx);
			}
			myDepth++;
		}

		@Override
		public void enterEveryRule(ParserRuleContext theArg0) {
			// ourLog.info("{}enterEveryRule: {}", new Object[] { myDepthLogger, theArg0.getStart().getText() });
			// myBaseState.enterEveryRule(theArg0);
			// myDepth++;
			// nothing
		}

		@Override
		public void enterExpr(ExprContext theCtx) {
			ourLog.info("{}enterExpr: {}", new Object[] { myDepthLogger, theCtx.getStart().getText() });
			for (MyBaseState nextBaseState : myBaseState) {
				nextBaseState.enterExpr(theCtx);
			}
			myDepth++;
		}

		@Override
		public void enterExpr_no_binop(Expr_no_binopContext theCtx) {
			ourLog.info("{}enterExpr_no_binop", new Object[] { myDepthLogger });
			for (MyBaseState nextBaseState : myBaseState) {
				nextBaseState.enterExpr_no_binop(theCtx);
			}
			myDepth++;
		}

		@Override
		public void enterFpconst(FpconstContext theCtx) {
			ourLog.info("{}enterFpconst", new Object[] { myDepthLogger });
			for (MyBaseState nextBaseState : myBaseState) {
				nextBaseState.enterFpconst(theCtx);
			}
			myDepth++;
		}

		@Override
		public void enterFunction(FunctionContext theCtx) {
			ourLog.info("{}enterFunction", new Object[] { myDepthLogger });
			for (MyBaseState nextBaseState : myBaseState) {
				nextBaseState.enterFunction(theCtx);
			}
			myDepth++;
		}

		@Override
		public void enterItem(ItemContext theCtx) {
			ourLog.info("{}enterItem: {}", new Object[] { myDepthLogger, theCtx.getStart().getText() });
			for (MyBaseState nextBaseState : myBaseState) {
				nextBaseState.enterItem(theCtx);
			}
			myDepth++;
		}

		@Override
		public void enterLine(LineContext theCtx) {
			ourLog.info("{}enterLine", new Object[] { myDepthLogger });
			for (MyBaseState nextBaseState : myBaseState) {
				nextBaseState.enterLine(theCtx);
			}
			myDepth++;
		}

		@Override
		public void enterParam_list(Param_listContext theCtx) {
			ourLog.info("{}enterParam_list", new Object[] { myDepthLogger });
			for (MyBaseState nextBaseState : myBaseState) {
				nextBaseState.enterParam_list(theCtx);
			}
			myDepth++;
		}

		@Override
		public void enterPredicate(PredicateContext theCtx) {
			ourLog.info("{}enterPredicate: {}", new Object[] { myDepthLogger, theCtx.getStart().getText() });
			for (MyBaseState nextBaseState : myBaseState) {
				nextBaseState.enterPredicate(theCtx);
			}
			myDepth++;
		}

		@Override
		public void enterProg(ProgContext theCtx) {
			ourLog.info("{}enterProg", new Object[] { myDepthLogger });
			for (MyBaseState nextBaseState : myBaseState) {
				nextBaseState.enterProg(theCtx);
			}
			myDepth++;
		}

		@Override
		public void enterRecurse(RecurseContext theCtx) {
			ourLog.info("{}enterRecurse", new Object[] { myDepthLogger });
			for (MyBaseState nextBaseState : myBaseState) {
				nextBaseState.enterRecurse(theCtx);
			}
			myDepth++;
		}

		@Override
		public void exitAxis_spec(Axis_specContext theCtx) {
			myDepth--;
			ourLog.info("{}exitAxis_spec", new Object[] { myDepthLogger });
			// myBaseState.exitAxis_spec(theCtx);
			pop();
		}

		@Override
		public void exitBinop(BinopContext theCtx) {
			myDepth--;
			ourLog.info("{}exitBinop", new Object[] { myDepthLogger });
			pop();
		}

		@Override
		public void exitBinop_operator(Binop_operatorContext theCtx) {
			myDepth--;
			ourLog.info("{}exitBinop_operator", new Object[] { myDepthLogger });
			pop();
		}

		@Override
		public void exitElement(ElementContext theCtx) {
			myDepth--;
			ourLog.info("{}exitElement", new Object[] { myDepthLogger });
			// myBaseState.exitElement(theCtx);
			pop();
		}

		@Override
		public void exitEveryRule(ParserRuleContext theArg0) {
			// myDepth--;
			// ourLog.info("{}exitEveryRule", new Object[] { myDepthLogger });
			// myBaseState.exitEveryRule(theArg0);
			// nothing
		}

		@Override
		public void exitExpr(ExprContext theCtx) {
			myDepth--;
			ourLog.info("{}exitExpr", new Object[] { myDepthLogger });
			// myBaseState.exitExpr(theCtx);
			if (myDepth == -1) {
				return;
			}
			pop();
		}

		@Override
		public void exitExpr_no_binop(Expr_no_binopContext theCtx) {
			myDepth--;
			ourLog.info("{}exitExpr_no_binop", new Object[] { myDepthLogger });
			pop();
		}

		@Override
		public void exitFpconst(FpconstContext theCtx) {
			myDepth--;
			ourLog.info("{}exitFpconst", new Object[] { myDepthLogger });
			// myBaseState.exitFpconst(theCtx);
			pop();
		}

		@Override
		public void exitFunction(FunctionContext theCtx) {
			myDepth--;
			ourLog.info("{}exitFunction", new Object[] { myDepthLogger });
			// myBaseState.exitFunction(theCtx);
			pop();
		}

		@Override
		public void exitItem(ItemContext theCtx) {
			myDepth--;
			ourLog.info("{}exitItem", new Object[] { myDepthLogger });
			// myBaseState.exitItem(theCtx);
			pop();
		}

		@Override
		public void exitLine(LineContext theCtx) {
			myDepth--;
			ourLog.info("{}exitLine", new Object[] { myDepthLogger });
			// myBaseState.exitLine(theCtx);
			pop();
		}

		@Override
		public void exitParam_list(Param_listContext theCtx) {
			myDepth--;
			ourLog.info("{}exitParam_list", new Object[] { myDepthLogger });
			// myBaseState.exitParam_list(theCtx);
			pop();
		}

		@Override
		public void exitPredicate(PredicateContext theCtx) {
			myDepth--;
			ourLog.info("{}exitPredicate", new Object[] { myDepthLogger });
			// myBaseState.exitPredicate(theCtx);
			pop();
		}

		@Override
		public void exitProg(ProgContext theCtx) {
			myDepth--;
			ourLog.info("{}exitProg", new Object[] { myDepthLogger });
			// myBaseState.exitProg(theCtx);
			pop();
		}

		@Override
		public void exitRecurse(RecurseContext theCtx) {
			myDepth--;
			ourLog.info("{}exitRecurse", new Object[] { myDepthLogger });
			// myBaseState.exitRecurse(theCtx);
			pop();
		}

		public List<IBase> getOutput() {
			if (myBaseState.size() != 1) {
				throw new DataFormatException("Invalid FHIRPath expression, multiple states found at root of expression");
			}
			return myBaseState.get(0).getOutput();
		}

		public void pop() {
			
			
			for (MyBaseState nextBaseState : myBaseState) {
				if (nextBaseState.myParent != null) {
					nextBaseState.beingPopped();
					nextBaseState.myParent.popping(nextBaseState);
				}
			}
			if (myBaseState.size() == 1) {
				myBaseState = Collections.singletonList(myBaseState.get(0).myParent);
			} else {
				throw new IllegalStateException();
			}

		}

		void push(List<MyBaseState> theStates, MyBaseState theParentState) {
			if (theParentState != null) {
				boolean found = false;
				for (MyBaseState next : myBaseState) {
					if (next == theParentState) {
						found = true;
					}
				}
				Validate.isTrue(found);
			}

			myBaseState = new ArrayList<MyBaseState>();
			for (MyBaseState nextState : theStates) {
				nextState.myParent = theParentState;
				nextState.myDelegator = this;
				myBaseState.add(nextState);
			}
		}

		void push(MyBaseState theState, MyBaseState theParentState) {
			push(Collections.singletonList(theState), theParentState);
		}

		@Override
		public void visitErrorNode(ErrorNode theArg0) {
			ourLog.info("{}visitErrorNode", new Object[] { myDepthLogger });
			for (MyBaseState nextBaseState : myBaseState) {
				nextBaseState.visitErrorNode(theArg0);
			}
		}

		@Override
		public void visitTerminal(TerminalNode theArg0) {
			ourLog.info("{}visitTerminal: {}", new Object[] { myDepthLogger, theArg0.getText() });
			for (MyBaseState nextBaseState : myBaseState) {
				nextBaseState.visitTerminal(theArg0);
			}
		}

	}

	class NullState extends MyBaseState {

		public NullState() {
			super(new ArrayList<IBase>());
		}

	}

	public class PredicateChildBaseState extends MyBaseState {

		public PredicateChildBaseState(List<IBase> theContext) {
			super(theContext);
		}

	}

	class PredicateChildItemState extends PredicateChildBaseState {

		public PredicateChildItemState(List<IBase> theContext) {
			super(theContext);
		}

		@Override
		public void enterAxis_spec(Axis_specContext theCtx) {
			String text = theCtx.getStart().getText();
			List<IBase> output = new ArrayList<IBase>();

			if ("*".equals(text)) {
				extractAllChildren(output, myContext, false);
			} else if ("**".equals(text)) {
				extractAllChildren(output, myContext, true);
			} else {
				throw new DataFormatException("Invalid FHIRPath String, didn't expect '" + text + "' at this position");
			}

			setOutput(output);
			myDelegator.push(new NullState(), this);
		}

		@Override
		public void enterElement(ElementContext theCtx) {
			String text = theCtx.getStart().getText();

			List<IBase> output = new ArrayList<IBase>();

			for (IBase next : myContext) {
				BaseRuntimeElementDefinition<?> def = myCtx.getElementDefinition(next.getClass());
				if (!(def instanceof BaseRuntimeElementCompositeDefinition)) {
					continue;
				}
				BaseRuntimeElementCompositeDefinition<?> compositeDef = (BaseRuntimeElementCompositeDefinition<?>) def;
				BaseRuntimeChildDefinition child = compositeDef.getChildByName(text);
				if (child == null) {
					continue;
				}
				List<IBase> values = child.getAccessor().getValues(next);
				output.addAll(values);
			}

			setOutput(output);

			myDelegator.push(new NullState(), this);
		}

		@Override
		public void enterFunction(FunctionContext theCtx) {
			String functionName = theCtx.getParent().getStart().getText();
			myDelegator.push(new FunctionState(myContext, functionName), this);
		}

		private void extractAllChildren(List<IBase> theOutput, List<IBase> theInput, boolean theRecurse) {
			for (IBase next : theInput) {
				BaseRuntimeElementDefinition<?> def = myCtx.getElementDefinition(next.getClass());
				if (!(def instanceof BaseRuntimeElementCompositeDefinition)) {
					continue;
				}
				BaseRuntimeElementCompositeDefinition<?> compositeDef = (BaseRuntimeElementCompositeDefinition<?>) def;
				List<BaseRuntimeChildDefinition> children = compositeDef.getChildren();
				for (BaseRuntimeChildDefinition nextChild : children) {
					List<IBase> values = nextChild.getAccessor().getValues(next);
					if (values.isEmpty() == false) {
						theOutput.addAll(values);
						if (theRecurse) {
							extractAllChildren(theOutput, values, theRecurse);
						}
					}
				}
			}
		}

		@Override
		public void popping(MyBaseState theBaseState) {
			if (theBaseState instanceof NullState) {
				return;
			}
			if (theBaseState instanceof FunctionState) {
				setOutput(((FunctionState) theBaseState).getOutput());
			}
		}

	}

	public class PredicateState extends MyBaseState {

		public PredicateState(List<IBase> theContext) {
			super(theContext);
		}

		@Override
		public void enterItem(ItemContext theCtx) {
			if (getOutput() != null) {
				// This means we're in the second item in a dotted row.. E.g. the "name" in "family.name"
				setContext(getOutput());
				setOutput(null);
			}
			myDelegator.push(new PredicateChildItemState(myContext), this);
		}

		@Override
		public void popping(MyBaseState theBaseState) {
			PredicateChildBaseState state = (PredicateChildBaseState) theBaseState;
			setOutput(state.getOutput());
		}

	}

	// private class MyVisiter extends fhirpathBaseVisitor<Void> {
	//
	// @Override
	// public Void visitPredicate(PredicateContext theCtx) {
	// ourLog.info("Predicate: {}", theCtx.getText());
	// return null;
	// }
	//
	// }

}
