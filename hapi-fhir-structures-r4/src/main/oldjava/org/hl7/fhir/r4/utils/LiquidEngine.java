package org.hl7.fhir.r4.utils;

import java.util.*;

import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.exceptions.PathEngineException;
import org.hl7.fhir.r4.context.IWorkerContext;
import org.hl7.fhir.r4.model.Base;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.model.Tuple;
import org.hl7.fhir.r4.model.ExpressionNode;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.model.TypeDetails;
import org.hl7.fhir.r4.utils.FHIRPathEngine.ExpressionNodeWithOffset;
import org.hl7.fhir.r4.utils.FHIRPathEngine.IEvaluationContext;
import org.hl7.fhir.utilities.Utilities;

public class LiquidEngine implements IEvaluationContext {

  public interface ILiquidEngineIcludeResolver {
    public String fetchInclude(LiquidEngine engine, String name);
  }
  
  private IEvaluationContext externalHostServices;
  private FHIRPathEngine engine;
  private ILiquidEngineIcludeResolver includeResolver; 

  private class LiquidEngineContext {
    private Object externalContext;
    private Map<String, Base> vars = new HashMap<>();

    public LiquidEngineContext(Object externalContext) {
      super();
      this.externalContext = externalContext;
    }

    public LiquidEngineContext(LiquidEngineContext existing) {
      super();
      externalContext = existing.externalContext;
      vars.putAll(existing.vars);
    }
  }

  public LiquidEngine(IWorkerContext context, IEvaluationContext hostServices) {
    super();
    this.externalHostServices = hostServices;
    engine = new FHIRPathEngine(context);
    engine.setHostServices(this);
  }
  
  public ILiquidEngineIcludeResolver getIncludeResolver() {
    return includeResolver;
  }

  public void setIncludeResolver(ILiquidEngineIcludeResolver includeResolver) {
    this.includeResolver = includeResolver;
  }

  public LiquidDocument parse(String source, String sourceName) throws Exception {
    return new LiquidParser(source).parse(sourceName);
  }

  public String evaluate(LiquidDocument document, Resource resource, Object appContext) throws FHIRException {
    StringBuilder b = new StringBuilder();
    LiquidEngineContext ctxt = new LiquidEngineContext(appContext);
    for (LiquidNode n : document.body) {
      n.evaluate(b, resource, ctxt);
    }
    return b.toString();
  }

  private abstract class LiquidNode {
    protected void closeUp() {}

    public abstract void evaluate(StringBuilder b, Resource resource, LiquidEngineContext ctxt) throws FHIRException;
  }

  private class LiquidConstant extends LiquidNode {
    private String constant;
    private StringBuilder b = new StringBuilder();

    @Override
    protected void closeUp() {
      constant = b.toString();
      b = null;
    }

    public void addChar(char ch) {
      b.append(ch);
    }

    @Override
    public void evaluate(StringBuilder b, Resource resource, LiquidEngineContext ctxt) {
      b.append(constant);
    }
  }

  private class LiquidStatement extends LiquidNode {
    private String statement;
    private ExpressionNode compiled;

    @Override
    public void evaluate(StringBuilder b, Resource resource, LiquidEngineContext ctxt) throws FHIRException {
      if (compiled == null)
        compiled = engine.parse(statement);
      b.append(engine.evaluateToString(ctxt, resource, resource, compiled));
    }
  }

  private class LiquidIf extends LiquidNode {
    private String condition;
    private ExpressionNode compiled;
    private List<LiquidNode> thenBody = new ArrayList<>();
    private List<LiquidNode> elseBody = new ArrayList<>();

    @Override
    public void evaluate(StringBuilder b, Resource resource, LiquidEngineContext ctxt) throws FHIRException {
      if (compiled == null)
        compiled = engine.parse(condition);
      boolean ok = engine.evaluateToBoolean(ctxt, resource, resource, compiled); 
      List<LiquidNode> list = ok ? thenBody : elseBody;
      for (LiquidNode n : list) {
        n.evaluate(b, resource, ctxt);
      }
    }
  }

  private class LiquidLoop extends LiquidNode {
    private String varName;
    private String condition;
    private ExpressionNode compiled;
    private List<LiquidNode> body = new ArrayList<>();
    @Override
    public void evaluate(StringBuilder b, Resource resource, LiquidEngineContext ctxt) throws FHIRException {
      if (compiled == null)
        compiled = engine.parse(condition);
      List<Base> list = engine.evaluate(ctxt, resource, resource, compiled);
      LiquidEngineContext lctxt = new LiquidEngineContext(ctxt);
      for (Base o : list) {
        lctxt.vars.put(varName, o);
        for (LiquidNode n : body) {
          n.evaluate(b, resource, lctxt);
        }
      }
    }
  }

  private class LiquidInclude extends LiquidNode {
    private String page;
    private Map<String, ExpressionNode> params = new HashMap<>();

    @Override
    public void evaluate(StringBuilder b, Resource resource, LiquidEngineContext ctxt) throws FHIRException {
      String src = includeResolver.fetchInclude(LiquidEngine.this, page);
      LiquidParser parser = new LiquidParser(src);
      LiquidDocument doc = parser.parse(page);
      LiquidEngineContext nctxt =  new LiquidEngineContext(ctxt.externalContext);
      Tuple incl = new Tuple();
      nctxt.vars.put("include", incl);
      for (String s : params.keySet()) {
        incl.addProperty(s, engine.evaluate(ctxt, resource, resource, params.get(s)));
      }
      for (LiquidNode n : doc.body) {
        n.evaluate(b, resource, nctxt);
      }
    }
  }

  public static class LiquidDocument  {
    private List<LiquidNode> body = new ArrayList<>();

  }

  private class LiquidParser {

    private String source;
    private int cursor;
    private String name;

    public LiquidParser(String source) {
      this.source = source;
      cursor = 0;
    }

    private char next1() {
      if (cursor >= source.length())
        return 0;
      else
        return source.charAt(cursor);
    }

    private char next2() {
      if (cursor >= source.length()-1)
        return 0;
      else
        return source.charAt(cursor+1);
    }

    private char grab() {
      cursor++;
      return source.charAt(cursor-1);
    }

    public LiquidDocument parse(String name) throws FHIRException {
      this.name = name;
      LiquidDocument doc = new LiquidDocument();
      parseList(doc.body, new String[0]);
      return doc;
    }

    private String parseList(List<LiquidNode> list, String[] terminators) throws FHIRException {
      String close = null;
      while (cursor < source.length()) {
        if (next1() == '{' && (next2() == '%' || next2() == '{' )) {
          if (next2() == '%') { 
            String cnt = parseTag('%');
            if (Utilities.existsInList(cnt, terminators)) {
              close = cnt;
              break;
            } else if (cnt.startsWith("if "))
              list.add(parseIf(cnt));
            else if (cnt.startsWith("loop "))
              list.add(parseLoop(cnt.substring(4).trim()));
            else if (cnt.startsWith("include "))
              list.add(parseInclude(cnt.substring(7).trim()));
            else
              throw new FHIRException("Script "+name+": Script "+name+": Unknown flow control statement "+cnt);
          } else { // next2() == '{'
            list.add(parseStatement());
          }
        } else {
          if (list.size() == 0 || !(list.get(list.size()-1) instanceof LiquidConstant))
            list.add(new LiquidConstant());
          ((LiquidConstant) list.get(list.size()-1)).addChar(grab());
        }
      }
      for (LiquidNode n : list)
        n.closeUp();
      if (terminators.length > 0)
        if (!Utilities.existsInList(close, terminators))
          throw new FHIRException("Script "+name+": Script "+name+": Found end of script looking for "+ Arrays.asList(terminators));
      return close;
    }

    private LiquidNode parseIf(String cnt) throws FHIRException {
      LiquidIf res = new LiquidIf();
      res.condition = cnt.substring(3).trim();
      String term = parseList(res.thenBody, new String[] { "else", "endif"} );
      if ("else".equals(term))
        term = parseList(res.elseBody, new String[] { "endif"} );
      return res;
    }

    private LiquidNode parseInclude(String cnt) throws FHIRException {
      int i = 1;
      while (i < cnt.length() && !Character.isWhitespace(cnt.charAt(i)))
        i++;
      if (i == cnt.length() || i == 0)
        throw new FHIRException("Script "+name+": Error reading include: "+cnt);
      LiquidInclude res = new LiquidInclude();
      res.page = cnt.substring(0, i);
      while (i < cnt.length() && Character.isWhitespace(cnt.charAt(i)))
        i++;
      while (i < cnt.length()) {
        int j = i;
        while (i < cnt.length() && cnt.charAt(i) != '=')
          i++;
        if (i >= cnt.length() || j == i) 
          throw new FHIRException("Script "+name+": Error reading include: "+cnt);
        String n = cnt.substring(j, i);
          if (res.params.containsKey(n)) 
            throw new FHIRException("Script "+name+": Error reading include: "+cnt);
          i++;
          ExpressionNodeWithOffset t = engine.parsePartial(cnt, i);
          i = t.getOffset();
          res.params.put(n, t.getNode());
          while (i < cnt.length() && Character.isWhitespace(cnt.charAt(i)))
            i++;
      }
      return res;
    }
  

    private LiquidNode parseLoop(String cnt) throws FHIRException {
      int i = 0;
      while (!Character.isWhitespace(cnt.charAt(i)))
        i++;
      LiquidLoop res = new LiquidLoop();
      res.varName = cnt.substring(0, i);
      while (Character.isWhitespace(cnt.charAt(i)))
        i++;
      int j = i;
      while (!Character.isWhitespace(cnt.charAt(i)))
        i++;
      if (!"in".equals(cnt.substring(j, i)))
        throw new FHIRException("Script "+name+": Script "+name+": Error reading loop: "+cnt);
      res.condition = cnt.substring(i).trim();
      parseList(res.body, new String[] { "endloop"} );
      return res;
    }

    private String parseTag(char ch) throws FHIRException {
      grab(); 
      grab();
      StringBuilder b = new StringBuilder();
      while (cursor < source.length() && !(next1() == '%' && next2() == '}')) {
        b.append(grab());
      }
      if (!(next1() == '%' && next2() == '}')) 
        throw new FHIRException("Script "+name+": Unterminated Liquid statement {% "+b.toString());
      grab(); 
      grab();
      return b.toString().trim();
    }

    private LiquidStatement parseStatement() throws FHIRException {
      grab(); 
      grab();
      StringBuilder b = new StringBuilder();
      while (cursor < source.length() && !(next1() == '}' && next2() == '}')) {
        b.append(grab());
      }
      if (!(next1() == '}' && next2() == '}')) 
        throw new FHIRException("Script "+name+": Unterminated Liquid statement {{ "+b.toString());
      grab(); 
      grab();
      LiquidStatement res = new LiquidStatement();
      res.statement = b.toString().trim();
      return res;
    }

  }

  @Override
  public Base resolveConstant(Object appContext, String name, boolean beforeContext) throws PathEngineException {
    LiquidEngineContext ctxt = (LiquidEngineContext) appContext;
    if (ctxt.vars.containsKey(name))
      return ctxt.vars.get(name);
    if (externalHostServices == null)
      return null;
    return externalHostServices.resolveConstant(ctxt.externalContext, name, beforeContext);
  }

  @Override
  public TypeDetails resolveConstantType(Object appContext, String name) throws PathEngineException {
    if (externalHostServices == null)
      return null;
    LiquidEngineContext ctxt = (LiquidEngineContext) appContext;
    return externalHostServices.resolveConstantType(ctxt.externalContext, name);
  }

  @Override
  public boolean log(String argument, List<Base> focus) {
    if (externalHostServices == null)
      return false;
    return externalHostServices.log(argument, focus);
  }

  @Override
  public FunctionDetails resolveFunction(String functionName) {
    if (externalHostServices == null)
      return null;
    return externalHostServices.resolveFunction(functionName);
  }

  @Override
  public TypeDetails checkFunction(Object appContext, String functionName, List<TypeDetails> parameters) throws PathEngineException {
    if (externalHostServices == null)
      return null;
    LiquidEngineContext ctxt = (LiquidEngineContext) appContext;
    return externalHostServices.checkFunction(ctxt.externalContext, functionName, parameters);
  }

  @Override
  public List<Base> executeFunction(Object appContext, String functionName, List<List<Base>> parameters) {
    if (externalHostServices == null)
      return null;
    LiquidEngineContext ctxt = (LiquidEngineContext) appContext;
    return externalHostServices.executeFunction(ctxt.externalContext, functionName, parameters);
  }

  @Override
  public Base resolveReference(Object appContext, String url) throws FHIRException {
    if (externalHostServices == null)
      return null;
    LiquidEngineContext ctxt = (LiquidEngineContext) appContext;
    return resolveReference(ctxt.externalContext, url);
  }

  @Override
  public boolean conformsToProfile(Object appContext, Base item, String url) throws FHIRException {
    if (externalHostServices == null)
      return false;
    LiquidEngineContext ctxt = (LiquidEngineContext) appContext;
    return conformsToProfile(ctxt.externalContext, item, url);
  }

}
