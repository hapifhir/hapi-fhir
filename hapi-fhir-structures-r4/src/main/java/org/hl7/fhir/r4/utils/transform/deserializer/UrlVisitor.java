package org.hl7.fhir.r4.utils.transform.deserializer;

import org.antlr.v4.runtime.tree.ParseTree;
import org.hl7.fhir.r4.utils.transform.deserializer.grammar.VisitorExtensions;
import org.hl7.fhir.r4.utils.transform.deserializer.grammar.antlr.javaAntlr.UrlJavaBaseVisitor;
import org.hl7.fhir.r4.utils.transform.deserializer.grammar.antlr.javaAntlr.UrlJavaParser;

import java.util.ArrayList;

// <copyright company="Applicadia LLC">
// Copyright (c) 2017
// by Applicadia LLC
// </copyright>


  /**
   ANTLR visitor class.
  */
  public class UrlVisitor extends UrlJavaBaseVisitor<Object>
  {
	/**
	 Delegate for optional dumping of info.

	*/
	public interface DumpDelegate
	{
		void invoke(String msg);
	}

	/**
	 Set this to callback function to dump parsing messages.
	*/
	public DumpDelegate DumpFcn = null;

	/**
	 Constructor.

	*/
	public UrlVisitor()
	{
	}

	/**
	 Parse grammar rule url

	 @param context
	 @return url
	*/
	@Override
	public Object visitUrl(UrlJavaParser.UrlContext context) {
	  UrlData retVal = new UrlData();
			retVal.CompleteUrl = context.getText();
			retVal.Authority = (String) this.visit( context.authority());
			if (context.login() != null)
			  retVal.Login = (UrlLogin) this.visit( context.login());
			if (context.host()!= null)
			retVal.Host = (String) this.visit(context.host());
			if (context.port() != null)
			retVal.Port = (int) this.visit(context.port());
			if (context.path() != null)
			retVal.Path = (String[]) this.visit(context.path());
			if (context.search() != null)
			retVal.Search = (UrlSearch[]) this.visit(context.search());
			return retVal;
	}

	/**
	 Parse grammar rule authority
	 Return Uri authority field

	 @param context
	 @return authority name (i.e. http)
	*/
	@Override
	public Object visitAuthority(UrlJavaParser.AuthorityContext context)
	{
	  return context.getText();
	}

	/**
	 Parse grammar rule hostName
	 Return host name

	 @param context
	 @return host name
	*/
	@Override
	public Object visitHost(UrlJavaParser.HostContext context)
	{
	  return context.getText();
	}

	/**
	 Parse grammar rule port

	 @param context
	 @return Port number
	*/
	@Override
	public Object visitPort(UrlJavaParser.PortContext context)
	{
	  return Integer.parseInt(context.getText());
	}

	/**
	 Parse grammar rule search

	 @param context
	 @return SearchData array of search components
	*/
	@Override
	public Object visitSearch(UrlJavaParser.SearchContext context)
	{
	  ArrayList<UrlSearch> retVal = new ArrayList<UrlSearch>();
	  if (context.searchParameter()!= null){
	    for (ParseTree treeItem : context.searchParameter()){
	      retVal.add((UrlSearch)this.visit(treeItem));
       }
     }
     return retVal;
	}

	/**
	 Parse grammar rule searchParameter

	 @param context
	 @return Path component
	*/
	@Override
	public Object visitSearchParameter(UrlJavaParser.SearchParameterContext context) {
		try {
			UrlSearch tempVar = new UrlSearch();
			tempVar.Name = (String) this.visit(context.searchParameterName());
			tempVar.Value = (String) this.visit( context.searchParameterValue());
			return tempVar;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 Parse grammar rule searchParameterName

	 @param context
	 @return String search parameter name
	*/
	@Override
	public Object visitSearchParameterName(UrlJavaParser.SearchParameterNameContext context)
	{
	  return context.getText();
	}

	/**
	 Parse grammar rule searchParameterValue

	 @param context
	 @return String search parameter value
	*/
	@Override
	public Object visitSearchParameterValue(UrlJavaParser.SearchParameterValueContext context)
	{
	  return context.getText();
	}

	/**
	 Parse grammar rule path

	 @param context
	 @return String array of components
	*/
	@Override
	public Object visitPath(UrlJavaParser.PathContext context)
	{
	  ArrayList<String> values = new ArrayList<String>();
     String[] retVals = new String[context.stringVal().size()];
     if (context.stringVal() != null){
       int count = context.stringVal().size();
       for (ParseTree treeItem : context.stringVal()){
         values.add((String) this.visit(treeItem));
       }
     }
	  return VisitorExtensions.<String>VisitMultiple(this, context.stringVal(), values);
	}

	/**
	 Parse grammar rule login
	 Load LoginData instance with parsed login data.

	 @param context
	 @return LoginData instance
	*/
	@Override
	public Object visitLogin(UrlJavaParser.LoginContext context) {
	  UrlLogin retVal = new UrlLogin();
			retVal.Name = (String) this.visit(context.user());
			retVal.Password = (String) this.visit( context.password());

	  return retVal;
	}

	/**
	 Parse grammar rule string

	 @param context
	 @return String user name
	*/
	@Override
	public Object visitStringVal(UrlJavaParser.StringValContext context)
	{
	  return context.getText();
	}

	/**
	 Parse grammar rule user

	 @param context
	 @return String user name
	*/
	@Override
	public Object visitUser(UrlJavaParser.UserContext context)
	{
	  return context.getText();
	}

	/**
	 Parse grammar rule password

	 @param context
	 @return String user name
	*/
	@Override
	public Object visitPassword(UrlJavaParser.PasswordContext context)
	{
	  return context.getText();
	}
  }
