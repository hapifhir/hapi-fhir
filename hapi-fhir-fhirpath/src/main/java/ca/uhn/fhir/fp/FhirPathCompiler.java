package ca.uhn.fhir.fp;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementCompositeDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeChildChoiceDefinition;
import ca.uhn.fhir.fp.antlr.fhirpathLexer;
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
import ca.uhn.fhir.fp.antlr.fhirpathVisitor;
import ca.uhn.fhir.model.primitive.BooleanDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.util.ObjectUtil;

public class FhirPathCompiler {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirPathCompiler.class);

	private final FhirContext myCtx;
	private ExprContext myExpr;
	private fhirpathLexer myLexer;

	public FhirPathCompiler(FhirContext theCtx) {
		super();
		myCtx = theCtx;
	}

	public List<IBase> applyTo(IBase theApplyTo) {
		fhirpathParser parser = new fhirpathParser(new CommonTokenStream(myLexer));

		ourLog.info("Starting FHIRPath evaluation");

		BaseVisitor visitor = new InitialVisitor(Collections.singletonList(theApplyTo), 0);
		return parser.expr().accept(visitor);
	}

	public void compile(String theExpression) {
		myLexer = new fhirpathLexer(new ANTLRInputStream(theExpression));
	}

	enum FunctionEnum {
		WHERE("where");

		private String myName;
		private static Map<String, FunctionEnum> ourNameToFunction;

		FunctionEnum(String theEncoding) {
			myName = theEncoding;
		}

		public static FunctionEnum forName(String theName) {
			Map<String, FunctionEnum> map = ourNameToFunction;
			if (map == null) {
				map = new HashMap<String, FunctionEnum>();
				for (FunctionEnum next : values()) {
					map.put(next.getName(), next);
				}
				ourNameToFunction = map;
			}
			return map.get(theName);
		}

		private String getName() {
			return myName;
		}
	}

	class ParamListVisitor extends BaseVisitor {

		ParamListVisitor(IBase theNext, int theDepth) {
			super(Collections.singletonList(theNext), theDepth);
		}

		@Override
		public List<IBase> visitExpr(ExprContext theCtx) {
			ourLog.info("{}Expr", new Object[] { myDepthLogger });
			return accept(theCtx, new ExprVisitor(getInput(), getNextDepth()));
		}

	}

	class FunctionVisitor extends BaseVisitor {

		private FunctionEnum myFunction;
		private ArrayList<List<IBase>> myFunctionArgs;

		FunctionVisitor(String theFunctionName, List<IBase> theInput, int theDepth) {
			super(theInput, theDepth);
			myFunction = FunctionEnum.forName(theFunctionName);
			if (myFunction == null) {
				throw new DataFormatException("Unknown function: " + theFunctionName);
			}
		}

		@Override
		public List<IBase> visitParam_list(Param_listContext theCtx) {
			int nextDepth = getNextDepth();

			myFunctionArgs = new ArrayList<List<IBase>>();

			for (int paramIndex = 0; paramIndex < theCtx.getChildCount(); paramIndex++) {

				List<IBase> arg = new ArrayList<IBase>();
				for (IBase next : getInput()) {
					ParamListVisitor v = new ParamListVisitor(next, nextDepth);
					List<IBase> retVal = theCtx.getChild(paramIndex).accept(v);
					if (retVal.size() > 0) {
						arg.add(next);
					}
				}
				myFunctionArgs.add(arg);

			}

			return Collections.emptyList();
		}

		@Override
		public List<IBase> visitTerminal(TerminalNode theArg0) {
			super.visitTerminal(theArg0);
			if (theArg0.getText().equals(")")) {
				switch (myFunction) {
				case WHERE:
					if (myFunctionArgs.size() != 1) {
						throw new DataFormatException("Wrong number of arguments for function: " + myFunction.getName());
					}
					return myFunctionArgs.get(0);
				}
				throw new InternalErrorException("Unknown function: " + myFunction); // should not happen
			}

			return Collections.emptyList();
		}

	}

	class ItemVisitor extends BaseVisitor {

		ItemVisitor(List<IBase> theInput, int theDepth) {
			super(theInput, theDepth);
		}

		@Override
		public List<IBase> visitAxis_spec(Axis_specContext theCtx) {
			String text = theCtx.getStart().getText();
			ourLog.info("{}Axis_spec: {}", new Object[] { myDepthLogger, text });

			List<IBase> output = new ArrayList<IBase>();
			if ("*".equals(text)) {
				extractAllChildren(output, getInput(), false);
			} else if ("**".equals(text)) {
				extractAllChildren(output, getInput(), true);
			} else {
				throw new DataFormatException("Invalid FHIRPath String, didn't expect '" + text + "' at this position");
			}

			return output;
		}

		@Override
		public List<IBase> visitFunction(FunctionContext theCtx) {
			String functionName = theCtx.getParent().getStart().getText();
			ourLog.info("{}Function: {}", new Object[] { myDepthLogger, functionName });
			return accept(theCtx, new FunctionVisitor(functionName, getInput(), getNextDepth()));
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
		public List<IBase> visitElement(ElementContext theCtx) {
			String text = theCtx.getStart().getText();
			String end = theCtx.getStop().getText();
			if ("[x]".equals(end)) {
				text = text + end;
			}
			ourLog.info("{}Element: {}", new Object[] { myDepthLogger, text });

			List<IBase> output = new ArrayList<IBase>();

			for (IBase next : getInput()) {
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
				if (child instanceof RuntimeChildChoiceDefinition) {
					if (!text.endsWith("[x]")) {
						for (IBase nextValue : values) {
							String childName = ((RuntimeChildChoiceDefinition)child).getChildNameByDatatype(nextValue.getClass());
							if (text.equals(childName)) {
								output.add(nextValue);
							}
						}
					} else {
						output.addAll(values);
					}
				} else {
					output.addAll(values);
				}

			}

			return output;
		}

	}

	class PredicateVisitor extends BaseVisitor {

		PredicateVisitor(List<IBase> theInput, int theDepth) {
			super(theInput, theDepth);
		}

		@Override
		public List<IBase> visitItem(ItemContext theCtx) {
			ourLog.info("{}Item", new Object[] { myDepthLogger });
			return accept(theCtx, new ItemVisitor(getInput(), getNextDepth()));
		}

	}

	class ExprNoBinopVisitor extends BaseVisitor {

		ExprNoBinopVisitor(List<IBase> theInput, int theDepth) {
			super(theInput, theDepth);
		}

		@Override
		public List<IBase> visitPredicate(PredicateContext theCtx) {
			ourLog.info("{}Predicate", new Object[] { myDepthLogger });
			return accept(theCtx, new PredicateVisitor(getInput(), getNextDepth()));
		}

		@Override
		public List<IBase> visitFpconst(FpconstContext theCtx) {
			String constValue = theCtx.getStart().getText();
			ourLog.info("{}Const: {}", new Object[] { myDepthLogger, constValue });
			if (constValue.length() > 1 && constValue.startsWith("'") && constValue.endsWith("'")) {
				IBase value = new StringDt(constValue.substring(1, constValue.length() - 1));
				return (Collections.singletonList(value));
			} else {
				throw new DataFormatException("Invalid constant: " + constValue);
			}
		}

	}

	class ExprBinopVisitor extends BaseVisitor {

		private List<IBase> myLeftOperand;
		private String myOperator;

		ExprBinopVisitor(List<IBase> theInput, int theDepth) {
			super(theInput, theDepth);
		}

		@Override
		public List<IBase> visitExpr_no_binop(Expr_no_binopContext theCtx) {
			ourLog.info("{}ExprNoBinop", new Object[] { myDepthLogger });
			List<IBase> operand = accept(theCtx, new ExprNoBinopVisitor(getInput(), getNextDepth()));
			if (myLeftOperand == null) {
				myLeftOperand = operand;
				return Collections.emptyList();
			} else {
				List<IBase> theRightValues = operand;
				if (myLeftOperand.size() != theRightValues.size()) {
					addInformation(myOperator + " operator returned empty collection because left hand side returned a different number of values from right hand side");
					return Collections.emptyList();
				}

				boolean equals = false;
				for (int i = 0; i < myLeftOperand.size(); i++) {
					IBase leftValue = myLeftOperand.get(i);
					IBase rightValue = theRightValues.get(i);

					if (!(leftValue instanceof IPrimitiveType)) {
						addInformation(myOperator + " operator returned empty collection because left hand side returned a non-primitive type: " + leftValue.getClass().getSimpleName());
						return Collections.emptyList();
					}
					if (!(rightValue instanceof IPrimitiveType)) {
						addInformation(myOperator + " operator returned empty collection because right hand side returned a non-primitive type: " + rightValue.getClass().getSimpleName());
						return Collections.emptyList();
					}

					Object leftObject = ((IPrimitiveType<?>) leftValue).getValue();
					Object rightObject = ((IPrimitiveType<?>) rightValue).getValue();

					if (leftObject == null && rightObject == null) {
						// ok
					} else if (leftObject == null || rightObject == null) {
						return Collections.emptyList();
					} else if (!(leftObject.getClass().equals(rightObject.getClass()))) {
						addInformation(myOperator + " operator returned empty collection because left hand side returned a type from right hand side");
						return Collections.emptyList();
					}

					if ("=".equals(myOperator)) {
						equals = ObjectUtil.equals(leftObject, rightObject);
					} else {
						throw new DataFormatException("Unknown operator: " + operand);
					}
					// if ("=".equals(myOperator) || "~".equals(myOperator) || "!=".equals(myOperator) ||
					// "!~".equals(myOperator) || ">".equals(myOperator)) {
					// }

				}

				if (!equals) {
					return Collections.emptyList();
				}

				return Collections.singletonList((IBase) new BooleanDt(true));
			}
		}

		private void addInformation(String theString) {
			// TODO Auto-generated method stub

		}

		@Override
		public List<IBase> visitBinop_operator(Binop_operatorContext theCtx) {
			String text = theCtx.getText();
			ourLog.info("{}ExprBinop: {}", new Object[] { myDepthLogger, text });
			myOperator = text;
			return Collections.emptyList();
		}

	}

	class InitialVisitor extends BaseVisitor {

		InitialVisitor(List<IBase> theInput, int theDepth) {
			super(theInput, theDepth);
		}

		@Override
		public List<IBase> visitExpr(ExprContext theCtx) {
			ourLog.info("{}Expr", new Object[] { myDepthLogger });
			return accept(theCtx, new ExprVisitor(getInput(), getNextDepth()));
		}

	}

	class ExprVisitor extends BaseVisitor {

		ExprVisitor(List<IBase> theInput, int theDepth) {
			super(theInput, theDepth);
		}

		@Override
		public List<IBase> visitBinop(BinopContext theCtx) {
			ourLog.info("{}ExprBinop", new Object[] { myDepthLogger });
			return accept(theCtx, new ExprBinopVisitor(getInput(), getNextDepth()));
		}

		@Override
		public List<IBase> visitExpr_no_binop(Expr_no_binopContext theCtx) {
			ourLog.info("{}ExprNoBinop", new Object[] { myDepthLogger });
			return accept(theCtx, new ExprNoBinopVisitor(getInput(), getNextDepth()));
		}

	}

	class BaseVisitor implements fhirpathVisitor<List<IBase>> {

		public int getNextDepth() {
			return myDepth + 1;
		}

		private int myDepth;

		protected Object myDepthLogger = new Object() {
			@Override
			public String toString() {
				StringBuilder b = new StringBuilder(myDepth);
				for (int i = 0; i < myDepth; i++) {
					b.append(' ');
				}
				return b.toString();
			}
		};

		private List<IBase> myInput;

		BaseVisitor(List<IBase> theInput, int theDepth) {
			myInput = theInput;
			myDepth = theDepth;
		}

		protected List<IBase> accept(ParserRuleContext theCtx, BaseVisitor visitor) {
			for (int i = 0; i < theCtx.getChildCount(); i++) {
				ParseTree nextChild = theCtx.getChild(i);
				myInput = nextChild.accept(visitor);
			}

			return myInput;
		}

		public List<IBase> getInput() {
			return myInput;
		}

		@Override
		public List<IBase> visit(ParseTree theArg0) {
			ourLog.info("{}visit", new Object[] { myDepthLogger });
			throw new UnsupportedOperationException();

		}

		@Override
		public List<IBase> visitChildren(RuleNode theArg0) {
			ourLog.info("{}visitChildren", new Object[] { myDepthLogger });
			throw new UnsupportedOperationException();

		}

		@Override
		public List<IBase> visitErrorNode(ErrorNode theArg0) {
			ourLog.info("{}visitErrorNode", new Object[] { myDepthLogger });
			throw new UnsupportedOperationException();

		}

		@Override
		public List<IBase> visitTerminal(TerminalNode theArg0) {
			ourLog.info("{}visitTerminal: {}", new Object[] { myDepthLogger, theArg0.getText() });
			return getInput();
		}

		@Override
		public List<IBase> visitItem(ItemContext theCtx) {
			ourLog.info("{}visitItem", new Object[] { myDepthLogger });
			throw new UnsupportedOperationException();
		}

		@Override
		public List<IBase> visitLine(LineContext theCtx) {
			ourLog.info("{}visitLine", new Object[] { myDepthLogger });
			throw new UnsupportedOperationException();

		}

		@Override
		public List<IBase> visitProg(ProgContext theCtx) {
			ourLog.info("{}visitProg", new Object[] { myDepthLogger });
			throw new UnsupportedOperationException();

		}

		@Override
		public List<IBase> visitAxis_spec(Axis_specContext theCtx) {
			ourLog.info("{}visitAxis_spec", new Object[] { myDepthLogger });
			throw new UnsupportedOperationException();

		}

		@Override
		public List<IBase> visitBinop_operator(Binop_operatorContext theCtx) {
			ourLog.info("{}visitBinop_operator", new Object[] { myDepthLogger });
			throw new UnsupportedOperationException();
		}

		@Override
		public List<IBase> visitParam_list(Param_listContext theCtx) {
			ourLog.info("{}visitParam_list", new Object[] { myDepthLogger });
			throw new UnsupportedOperationException();

		}

		@Override
		public List<IBase> visitPredicate(PredicateContext theCtx) {
			ourLog.info("{}visitPredicate", new Object[] { myDepthLogger });
			throw new UnsupportedOperationException();
		}

		@Override
		public List<IBase> visitExpr_no_binop(Expr_no_binopContext theCtx) {
			ourLog.info("{}visitExpr_no_binop", new Object[] { myDepthLogger });
			throw new UnsupportedOperationException();

		}

		@Override
		public List<IBase> visitRecurse(RecurseContext theCtx) {
			ourLog.info("{}visitRecurse", new Object[] { myDepthLogger });
			throw new UnsupportedOperationException();

		}

		@Override
		public List<IBase> visitFunction(FunctionContext theCtx) {
			ourLog.info("{}visitFunction", new Object[] { myDepthLogger });
			throw new UnsupportedOperationException();

		}

		@Override
		public List<IBase> visitFpconst(FpconstContext theCtx) {
			ourLog.info("{}visitFpconst", new Object[] { myDepthLogger });
			throw new UnsupportedOperationException();

		}

		@Override
		public List<IBase> visitExpr(ExprContext theCtx) {
			ourLog.info("{}visitExpr", new Object[] { myDepthLogger });
			throw new UnsupportedOperationException();
		}

		@Override
		public List<IBase> visitBinop(BinopContext theCtx) {
			ourLog.info("{}visitBinop", new Object[] { myDepthLogger });
			throw new UnsupportedOperationException();

		}

		@Override
		public List<IBase> visitElement(ElementContext theCtx) {
			ourLog.info("{}visitElement", new Object[] { myDepthLogger });
			throw new UnsupportedOperationException();
		}

	}

}
