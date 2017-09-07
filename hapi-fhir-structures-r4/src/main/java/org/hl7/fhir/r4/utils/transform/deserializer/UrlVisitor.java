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
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object visitUrl([NotNull] UrlContext context)
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
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object visitAuthority([NotNull] AuthorityContext context)
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
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object visitHost([NotNull] HostContext context)
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
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object visitPort([NotNull] PortContext context)
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
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object visitSearch([NotNull] SearchContext context)
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
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object visitSearchParameter([NotNull] SearchParameterContext context)
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
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object visitSearchParameterName([NotNull] SearchParameterNameContext context)
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
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object visitSearchParameterValue([NotNull] SearchParameterValueContext context)
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
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object visitPath([NotNull] PathContext context)
	@Override
	public Object visitPath(UrlJavaParser.PathContext context)
	{
	  ArrayList<String> values = new ArrayList<String>();
	  return VisitorExtensions.<String>VisitMultiple(this, context.stringVal(), values);
	}

	/** 
	 Parse grammar rule login
	 Load LoginData instance with parsed login data.
	 
	 @param context
	 @return LoginData instance
	*/
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object visitLogin([NotNull] LoginContext context)
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
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object visitStringVal([NotNull] StringValContext context)
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
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object visitUser([NotNull] UserContext context)
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
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object visitPassword([NotNull] PasswordContext context)
	@Override
	public Object visitPassword(UrlJavaParser.PasswordContext context)
	{
	  return context.getText();
	}
  }
