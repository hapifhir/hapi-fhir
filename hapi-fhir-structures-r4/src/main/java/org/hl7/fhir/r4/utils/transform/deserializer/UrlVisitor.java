package org.hl7.fhir.r4.utils.transform.deserializer;

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
	@FunctionalInterface
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
	 Parse antlr rule url
	 
	 @param context
	 @return url
	*/
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object visitUrl([NotNull] UrlContext context)
	@Override
	public Object visitUrl(UrlJavaParser.UrlContext context) {
	  UrlData retVal = new UrlData();
		try {
			retVal.CompleteUrl = context.getText();
			retVal.Authority = VisitorExtensions.<String>VisitOrDefault(this, context.authority(), "");
			retVal.Login = VisitorExtensions.<UrlLogin>VisitOrDefault(this, context.login(), UrlLogin.class);
			retVal.Host = VisitorExtensions.<String>VisitOrDefault(this, context.host(), "");
			retVal.Port = VisitorExtensions.<Integer>VisitOrDefault(this, context.port(), -1);
			retVal.Path = VisitorExtensions.<String[]>VisitOrDefault(this, context.path(), new String[0]);
			retVal.Search = VisitorExtensions.<UrlSearch[]>VisitOrDefault(this, context.search(), new UrlSearch[0]);
			return retVal;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
		return null;
	}

	/** 
	 Parse antlr rule authority
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
	 Parse antlr rule hostName
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
	 Parse antlr rule port
	 
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
	 Parse antlr rule search
	 
	 @param context
	 @return SearchData array of search components
	*/
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object visitSearch([NotNull] SearchContext context)
	@Override
	public Object visitSearch(UrlJavaParser.SearchContext context)
	{
	  ArrayList<UrlSearch> retVal = new ArrayList<UrlSearch>();
	  return VisitorExtensions.<UrlSearch>VisitMultiple(this, context.searchParameter(), retVal);
	}

	/** 
	 Parse antlr rule searchParameter
	 
	 @param context
	 @return Path component
	*/
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object visitSearchParameter([NotNull] SearchParameterContext context)
	@Override
	public Object visitSearchParameter(UrlJavaParser.SearchParameterContext context) {
		try {
			UrlSearch tempVar = new UrlSearch();
			tempVar.Name = VisitorExtensions.<String>VisitOrDefault(this, context.searchParameterName(), String.class);
			tempVar.Value = VisitorExtensions.<String>VisitOrDefault(this, context.searchParameterValue(), String.class);
			return tempVar;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/** 
	 Parse antlr rule searchParameterName
	 
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
	 Parse antlr rule searchParameterValue
	 
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
	 Parse antlr rule path
	 
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
	 Parse antlr rule login
	 Load LoginData instance with parsed login data.
	 
	 @param context
	 @return LoginData instance
	*/
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object visitLogin([NotNull] LoginContext context)
	@Override
	public Object visitLogin(UrlJavaParser.LoginContext context) {
	  UrlLogin retVal = new UrlLogin();
		try {
			retVal.Name = VisitorExtensions.<String>VisitOrDefault(this, context.user(), String.class);
			retVal.Password = VisitorExtensions.<String>VisitOrDefault(this, context.password(), String.class);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
	  return retVal;
	}

	/** 
	 Parse antlr rule string
	 
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
	 Parse antlr rule user
	 
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
	 Parse antlr rule password
	 
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
