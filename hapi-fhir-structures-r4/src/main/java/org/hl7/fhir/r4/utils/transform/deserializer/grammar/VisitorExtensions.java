package org.hl7.fhir.r4.utils.transform.deserializer.grammar;

import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import org.antlr.v4.runtime.tree.ParseTree;

import java.lang.reflect.Array;
import java.util.*;

  /** 
   Extension methods for visitor classes.
  */
  public final class VisitorExtensions
  {
	/** 
	 Call visit if tree non null, otherwise return null.
	 
	 @param visitor Visitor derived class
	 @param tree parse tree (may be null)
	 @return null or value of Visit method
	*/

	public static <Result> Result VisitOrDefault(AbstractParseTreeVisitor<Object> visitor, ParseTree tree, Class<?> resultClass) throws IllegalAccessException, InstantiationException {
		Result result = (Result) resultClass.newInstance();
		return VisitOrDefault(visitor, tree, result);
	}

//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: public static Result VisitOrDefault<Result>(AbstractParseTreeVisitor<Object> visitor, IParseTree tree, Result defaultValue = default(Result))
	public static <Result> Result VisitOrDefault(AbstractParseTreeVisitor<Object> visitor, ParseTree tree, Result defaultValue)
	{
	  if (tree == null)
	  {
		return defaultValue;
	  }
	  return (Result)visitor.visit(tree);
	}

	/** 
	 Call visit if tree non null, otherwise return null.
	 
	 @param visitor Visitor derived class
	 @param tree parse tree (may be null)
	 @return null or value of Visit method
	*/
	public static <Result> Result VisitSingleOrDefault(AbstractParseTreeVisitor<Object> visitor, List<? extends ParseTree> tree)
	{
	  if (tree == null)
	  {
		return null;
	  }
	  switch (tree.size())
	  {
		case 0:
			return null;
		case 1:
			return (Result)visitor.visit(tree.get(0));
		default:
			throw new RuntimeException("Expected single parse tree block");
	  }
	}

	/** 
	 Call visit if tree non null, otherwise return null.
	 
	 @param visitor Visitor derived class
	 @param tree parse tree (may be null)
	 @return null or value of Visit method
	*/
	@SuppressWarnings("Unchecked")
	public static <Result> Result[] VisitMultiple(AbstractParseTreeVisitor<Object> visitor, List<? extends ParseTree> tree, ArrayList<Result> results)
	{
		Result[] retVal = (Result[]) Array.newInstance(results.get(0).getClass(), tree.size());
		if (tree != null)
	  {
		int count = tree.size();
		for (ParseTree treeItem : tree)
		{
		  results.add((Result)visitor.visit(treeItem));
		}
	  }
	  return results.toArray(retVal);
	}
  }
