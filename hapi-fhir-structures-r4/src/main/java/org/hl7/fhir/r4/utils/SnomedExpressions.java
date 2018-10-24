package org.hl7.fhir.r4.utils;

import java.util.ArrayList;
import java.util.List;

import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.utilities.Utilities;

public class SnomedExpressions {

  public class Base {
    private int stop;
    private int start;
    public int getStop() {
      return stop;
    }
    public void setStop(int stop) {
      this.stop = stop;
    }
    public int getStart() {
      return start;
    }
    public void setStart(int start) {
      this.start = start;
    }
  }

  public class Concept extends Base {
    private long reference;
    private String code;
    private String description;
    private String literal;
    private String decimal;
    public long getReference() {
      return reference;
    }
    public void setReference(long reference) {
      this.reference = reference;
    }
    public String getCode() {
      return code;
    }
    public void setCode(String code) {
      this.code = code;
    }
    public String getDescription() {
      return description;
    }
    public void setDescription(String description) {
      this.description = description;
    }
    public String getLiteral() {
      return literal;
    }
    public void setLiteral(String literal) {
      this.literal = literal;
    }
    public String getDecimal() {
      return decimal;
    }
    public void setDecimal(String decimal) {
      this.decimal = decimal;
    }
    @Override
    public String toString() {
      if (code != null) 
      return code;
    else if (decimal != null) 
      return "#"+decimal;
    else if (literal != null)
      return "\""+literal+"\"";
    else
      return "";
    }
  }

  public enum ExpressionStatus {
    Unknown, Equivalent, SubsumedBy;
  }

  public class Expression extends Base {
    private List<RefinementGroup> refinementGroups = new ArrayList<RefinementGroup>();
    private List<Refinement> refinements = new ArrayList<Refinement>();
    private List<Concept> concepts = new ArrayList<Concept>();
    private ExpressionStatus status;
    public ExpressionStatus getStatus() {
      return status;
    }
    public void setStatus(ExpressionStatus status) {
      this.status = status;
    }
    public List<RefinementGroup> getRefinementGroups() {
      return refinementGroups;
    }
    public List<Refinement> getRefinements() {
      return refinements;
    }
    public List<Concept> getConcepts() {
      return concepts;
    }
    @Override
    public String toString() {
      StringBuilder b = new StringBuilder();
      if (status == ExpressionStatus.Equivalent)
        b.append("===");
      else if (status == ExpressionStatus.SubsumedBy)
        b.append("<<<");
      boolean first = true;
      for (Concept concept : concepts) {
        if (first) first = false; else b.append(',');
        b.append(concept.toString());
      }
      for (Refinement refinement : refinements) {
        if (first) first = false; else b.append(',');
        b.append(refinement.toString());
      }
      for (RefinementGroup refinementGroup : refinementGroups) {
        if (first) first = false; else b.append(',');
        b.append(refinementGroup.toString());
      }
      return b.toString();
    }
  }

  public class Refinement extends Base {
    private Concept name;
    private Expression value;
    public Concept getName() {
      return name;
    }
    public void setName(Concept name) {
      this.name = name;
    }
    public Expression getValue() {
      return value;
    }
    public void setValue(Expression value) {
      this.value = value;
    }

    @Override
    public String toString() {
      return name.toString()+"="+value.toString();
    }
  }

  public class RefinementGroup extends Base {
    private List<Refinement> refinements = new ArrayList<Refinement>();

    public List<Refinement> getRefinements() {
      return refinements;
    }

    @Override
    public String toString() {
      StringBuilder b = new StringBuilder();
      boolean first = true;
      for (Refinement refinement : refinements) {
        if (first) first = false; else b.append(',');
        b.append(refinement.toString());
      }
      return b.toString();
    }
  }

  private static final int MAX_TERM_LIMIT = 1024;

    private String source;
    private int cursor;

    private Concept concept() throws FHIRException {
      Concept res = new Concept();
      res.setStart(cursor);
      ws();
      if (peek() == '#')
        res.decimal = decimal();
      else if (peek() == '"') 
        res.literal = stringConstant();
      else
        res.code = conceptId();
      ws();
      if (gchar('|')) {
        ws();
        res.description = term().trim();
        ws();
        fixed('|');
        ws();
      }
      res.setStop(cursor);
      return res;
    }

    private void refinements(Expression expr) throws FHIRException {
      boolean n = true;
      while (n) {
        if (peek() != '{')
          expr.refinements.add(attribute());
        else
          expr.refinementGroups.add(attributeGroup());
        ws();
        n = gchar(',');
        ws();
      }
    }

    private RefinementGroup attributeGroup() throws FHIRException {
      RefinementGroup res = new RefinementGroup();
      fixed('{');
      ws();
      res.setStart(cursor);
      res.refinements.add(attribute());
      while (gchar(','))
        res.refinements.add(attribute());
      res.setStop(cursor);
      ws();
      fixed('}');
      ws();
      return res;
    }

    private Refinement attribute() throws FHIRException {
      Refinement res = new Refinement();
      res.setStart(cursor);
      res.name = attributeName();
      fixed('=');
      res.value = attributeValue();
      ws();
      res.setStop(cursor);
      return res;
    }

    private Concept attributeName() throws FHIRException {
      Concept res = new Concept();
      res.setStart(cursor);
      ws();
      res.code = conceptId();
      ws();
      if (gchar('|')) {
        ws();
        res.description = term();
        ws();
        fixed('|');
        ws();
      }
      res.setStop(cursor);
      return res;
    }

    private Expression attributeValue() throws FHIRException {
      Expression res;
      ws();
      if (gchar('(')) {
        res = expression();
        fixed(')');
      } else {
        res = expression();
      }
      return res;
    }

    private Expression expression() throws FHIRException {
      Expression res = new Expression();
      res.setStart(cursor);
      ws();
      res.concepts.add(concept());
      while (gchar('+'))
        res.concepts.add(concept());
      if (gchar(':')) {
        ws();
        refinements(res);
      }
      res.setStop(cursor);
      return res;
    }

    private String conceptId() throws FHIRException {
      StringBuffer res = new StringBuffer(Utilities.padLeft("", ' ', 18));
      int i = 0;
      while (peek() >= '0' && peek() <= '9') {
        res.setCharAt(i, next());
        i++;
      }
      rule(i > 0, "Concept not found (next char = \""+peekDisp()+"\", in '"+source+"')");
      return res.substring(0, i);
    }

    private String decimal() throws FHIRException {
      StringBuffer res = new StringBuffer(Utilities.padLeft("", ' ', MAX_TERM_LIMIT));
      int i = 0;
      fixed('#');
      while ((peek() >= '0' && peek() <= '9') || peek() == '.') {
        res.setCharAt(i, next());
        i++;
      }
      return res.substring(0, i);
    }

    private String term() {
      StringBuffer res = new StringBuffer(Utilities.padLeft("", ' ', MAX_TERM_LIMIT));
      int i = 0;
      while (peek() != '|') {
        res.setCharAt(i, next());
        i++;
      }
      return res.substring(0, i);
    }

    private void ws() {
      while (Utilities.existsInList(peek(), ' ', '\t', '\r', 'n'))
        next();
    }

    private boolean gchar(char  ch) {
      boolean result = peek() == ch;
      if (result)
        next();
      return result;
    }

    private void fixed(char ch) throws FHIRException {
      boolean b = gchar(ch);
      rule(b, "Expected character \""+ch+"\" but found "+peek());
      ws();
    }

    private Expression parse() throws FHIRException {
      Expression res = new Expression();
      res.setStart(cursor);
      ws();
      if (peek() == '=') {
        res.status = ExpressionStatus.Equivalent;
        prefix('=');
      } else if (peek() == '<') {
        res.status = ExpressionStatus.SubsumedBy;
        prefix('<');
      }

      res.concepts.add(concept());
      while (gchar('+'))
        res.concepts.add(concept());
      if (gchar(':')) {
        ws();
        refinements(res);
      }
      res.setStop(cursor);
      rule(cursor >= source.length(), "Found content (\""+peekDisp()+"\") after end of expression");
      return res;
    }
    
    public static Expression parse(String source) throws FHIRException {
      SnomedExpressions self = new SnomedExpressions();
      self.source = source;
      self.cursor = 0;
      return self.parse();
    }

    private char peek() {
      if (cursor >= source.length())
        return '\0';
      else
        return source.charAt(cursor);
    }

    private String peekDisp() {
      if (cursor >= source.length()) 
        return "[n/a: overrun]";
      else
        return String.valueOf(source.charAt(cursor));
    }

    private void prefix(char c) throws FHIRException {
      fixed(c);
      fixed(c);
      fixed(c);
      ws();
    }

    private char next() {
      char res = peek();
      cursor++;
      return res;
    }

    private void rule(boolean test, String message) throws FHIRException {
      if (!test) 
        throw new FHIRException(message+" at character "+Integer.toString(cursor));
    }

    private String stringConstant() throws FHIRException {
      StringBuffer res = new StringBuffer(Utilities.padLeft("", ' ', MAX_TERM_LIMIT));
      fixed('"');
      int i = 0;
      while (peek() != '"') {
        i++;
        res.setCharAt(i, next());
      }
      fixed('"');
      return res.substring(0, i);
    }

}
