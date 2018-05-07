package org.hl7.fhir.utilities.graphql;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.Map.Entry;

import org.hl7.fhir.utilities.TextFile;
import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.utilities.graphql.Argument.ArgumentListStatus;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Parser {  
  public static Package parse(String source) throws IOException, EGraphQLException, EGraphEngine {
    Parser self = new Parser();
    self.reader = new StringReader(source);
    self.next();
    Document doc = self.parseDocument();
    return new Package(doc);
  }

  public static Package parse(InputStream source) throws IOException, EGraphQLException, EGraphEngine {
    Parser self = new Parser();
    self.reader = new InputStreamReader(source);
    self.next();
    Document doc = self.parseDocument();
    return new Package(doc);
  }

  public static Package parseFile(String filename) throws FileNotFoundException, IOException, EGraphQLException, EGraphEngine {
    String src = TextFile.fileToString(filename);
    return parse(src);
  }

  public static Package parseJson(InputStream source) throws EGraphQLException, IOException, EGraphEngine {
    JsonObject json = (JsonObject) new com.google.gson.JsonParser().parse(TextFile.streamToString(source));
    Parser self = new Parser();
    self.reader = new StringReader(json.get("query").getAsString());
    self.next();
    Package result = new Package(self.parseDocument());
    result.setOperationName(json.get("operationName").getAsString());
    if (json.has("variables")) {
      JsonObject vl = json.getAsJsonObject("variables");
      for (Entry<String, JsonElement> n : vl.entrySet())
        result.getVariables().add(new Argument(n.getKey(), n.getValue()));
    }
    return result;
  }

  enum LexType {gqlltNull, gqlltName, gqlltPunctuation, gqlltString, gqlltNumber}

  static class SourceLocation {
    int line;
    int col;
  }
  
  private Reader reader;
  private StringBuilder token;
  private String peek;
  private LexType lexType;
  private SourceLocation location = new SourceLocation();
  boolean readerDone = false;

  private char getNextChar() throws IOException {
    char result = '\0';
    if (peek != null) {
      result = peek.charAt(0);
      peek = peek.length() == 1 ? null : peek.substring(1);
    } else if (reader.ready()) {
      int c = reader.read();
      if (c > -1) {
        result = (char) c;
        if (result == '\n') {
          location.line++;
          location.col = 1;
        } else
          location.col++;
      }
    }
    readerDone = result == '\0';
    return result;
  }

  private void pushChar(char ch) {
    if (ch != '\0')
      if (peek == null)
        peek = String.valueOf(ch);
      else
        peek = String.valueOf(ch)+peek;
  }

  private void skipIgnore() throws IOException{
    char ch = getNextChar();
    while (Character.isWhitespace(ch) || (ch == ',')) 
      ch = getNextChar();
    if (ch == '#') {
      while (ch != '\r' && ch != '\n')
        ch = getNextChar();
      pushChar(ch);
      skipIgnore();
    } else
      pushChar(ch);
  }

  private void next() throws IOException, EGraphQLException {
    //  var
    //    ch : Char;
    //    hex : String;
    skipIgnore();
    token = new StringBuilder();
    if (readerDone && peek == null)
      lexType = LexType.gqlltNull;
    else {
      char ch = getNextChar();
      if (Utilities.existsInList(ch, '!', '$', '(', ')', ':', '=', '@', '[', ']', '{', '|', '}')) {
        lexType = LexType.gqlltPunctuation;
        token.append(ch);
      } else if (ch == '.') {
        do {
          token.append(ch);
          ch = getNextChar();
        } while (ch == '.');
        pushChar(ch);
        if ((token.length() != 3))
          throw new EGraphQLException("Found \""+token.toString()+"\" expecting \"...\"");
      } else if ((ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z') || (ch == '_')) {
        lexType = LexType.gqlltName;
        do {
          token.append(ch);
          ch = getNextChar();
        } while ((ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z') || (ch >= '0' && ch <= '9') || (ch == '_'));
        pushChar(ch);
      } else if ((ch >= '0' && ch <= '9') || (ch == '-')) {
        lexType = LexType.gqlltNumber;
        do {
          token.append(ch);
          ch = getNextChar();
        } while ((ch >= '0' && ch <= '9') || ((ch == '.') && token.toString().indexOf('.') == -1)  || ((ch == 'e') && token.toString().indexOf('e') == -1));
        pushChar(ch);
      } else if ((ch == '"')) {
        lexType = LexType.gqlltString;
        do {
          ch = getNextChar();
          if (ch == '\\') {
            if (!reader.ready())
              throw new EGraphQLException("premature termination of GraphQL during a string constant");
            ch = getNextChar();
            if (ch == '"') token.append('"');
            else if (ch == '\\') token.append('\'');
            else if (ch == '/') token.append('/');
            else if (ch == 'n') token.append('\n');
            else if (ch == 'r') token.append('\r');
            else if (ch == 't') token.append('\t');
            else if (ch == 'u') {
              String hex = String.valueOf(getNextChar()) + getNextChar() + getNextChar() + getNextChar();
              token.append((char) Integer.parseInt(hex, 16));
            } else
              throw new EGraphQLException("Unexpected character: \""+ch+"\"");
            ch = '\0';
          }  else if (ch != '"') 
            token.append(ch);
        } while (!(readerDone || ch == '"'));
        if (ch != '"')
          throw new EGraphQLException("premature termination of GraphQL during a string constant");
      } else
        throw new EGraphQLException("Unexpected character \""+ch+"\"");
    }
  }

  private boolean hasPunctuation(String punc) {
    return lexType == LexType.gqlltPunctuation && token.toString().equals(punc);
  }

  private void consumePunctuation(String punc) throws EGraphQLException, IOException {
    if (lexType != LexType.gqlltPunctuation)
      throw new EGraphQLException("Found \""+token.toString()+"\" expecting \""+punc+"\"");
    if (!token.toString().equals(punc))
      throw new EGraphQLException("Found \""+token.toString()+"\" expecting \""+punc+"\"");
    next();
  }

  private boolean hasName() {
    return (lexType == LexType.gqlltName) && (token.toString().length() > 0);
  }

  private boolean hasName(String name) {
    return (lexType == LexType.gqlltName) && (token.toString().equals(name));
  }

  private String consumeName() throws EGraphQLException, IOException {
    if (lexType != LexType.gqlltName)
      throw new EGraphQLException("Found \""+token.toString()+"\" expecting a name");
    String result = token.toString();
    next();
    return result;
  }

  private void consumeName(String name) throws EGraphQLException, IOException{
    if (lexType != LexType.gqlltName)
      throw new EGraphQLException("Found \""+token.toString()+"\" expecting a name");
    if (!token.toString().equals(name))
      throw new EGraphQLException("Found \""+token.toString()+"\" expecting \""+name+"\"");
    next();
  }

  private Value parseValue() throws EGraphQLException, IOException {
    Value result = null;
    switch (lexType) {
    case gqlltNull: throw new EGraphQLException("Attempt to read a value after reading off the } of the GraphQL statement");
    case gqlltName: 
      result = new NameValue(token.toString());
      break;
    case gqlltPunctuation:
      if (hasPunctuation("$")) {
        consumePunctuation("$");
        result = new VariableValue(token.toString());
      } else if (hasPunctuation("{")) {
        consumePunctuation("{");
        ObjectValue obj = new ObjectValue();
        while (!hasPunctuation("}"))
          obj.getFields().add(parseArgument());
        result = obj;
      } else
        throw new EGraphQLException("Attempt to read a value at \""+token.toString()+"\"");
      break;
    case gqlltString: 
      result = new StringValue(token.toString());
      break;
    case gqlltNumber: 
      result = new NumberValue(token.toString());
      break;
    }
    next();
    return result;
  }

  private Argument parseArgument() throws EGraphQLException, IOException {
    Argument result = new Argument();
    result.setName(consumeName());
    consumePunctuation(":");
    if (hasPunctuation("[")) {
      result.setListStatus(ArgumentListStatus.REPEATING);
      consumePunctuation("[");
      while (!hasPunctuation("]"))
        result.getValues().add(parseValue());
      consumePunctuation("]");
    } else
      result.getValues().add(parseValue());
    return result;
  }

  private Directive parseDirective() throws EGraphQLException, IOException {
    Directive result = new Directive();
    consumePunctuation("@");
    result.setName(consumeName());
    if (hasPunctuation("(")) {
      consumePunctuation("(");
      do { 
        result.getArguments().add(parseArgument());
      } while (!hasPunctuation(")"));
      consumePunctuation(")");
    }
    return result;
  }

  private Document parseDocument() throws EGraphQLException, IOException, EGraphEngine {
    Document doc = new Document();
    if (!hasName()) {
      Operation op = new Operation();
      parseOperationInner(op);
      doc.getOperations().add(op);

    } else {
      while (!readerDone || (peek != null)) {
        String s = consumeName();
        if (s.equals("mutation") || (s.equals("query"))) 
          doc.getOperations().add(parseOperation(s));
        else if (s.equals("fragment"))
          doc.getFragments().add(parseFragment());
        else
          throw new EGraphEngine("Not done yet"); // doc.Operations.Add(parseOperation(s))?          
      }
    }
    return doc;
  }

  private Field parseField() throws EGraphQLException, IOException {
    Field result = new Field();
    result.setName(consumeName());
    result.setAlias(result.getName());
    if (hasPunctuation(":")) {
      consumePunctuation(":");
      result.setName(consumeName());
    }
    if (hasPunctuation("(")) {
      consumePunctuation("(");
      while (!hasPunctuation(")"))
        result.getArguments().add(parseArgument());
      consumePunctuation(")");
    }
    while (hasPunctuation("@")) 
      result.getDirectives().add(parseDirective());

    if (hasPunctuation("{")) {
      consumePunctuation("{");
      do {
        result.getSelectionSet().add(parseSelection());
      } while (!hasPunctuation("}"));
      consumePunctuation("}");
    }
    return result;
  }

  private void parseFragmentInner(Fragment fragment) throws EGraphQLException, IOException {
    while (hasPunctuation("@"))
      fragment.getDirectives().add(parseDirective());
    consumePunctuation("{");
    do
      fragment.getSelectionSet().add(parseSelection());
    while (!hasPunctuation("}"));
    consumePunctuation("}");
  }

  private Fragment parseFragment() throws EGraphQLException, IOException {
    Fragment result = new Fragment();
    result.setName(consumeName());
    consumeName("on");
    result.setTypeCondition(consumeName());
    parseFragmentInner(result);
    return result;
  }

  private FragmentSpread parseFragmentSpread() throws EGraphQLException, IOException {
    FragmentSpread result = new FragmentSpread();
    result.setName(consumeName());
    while (hasPunctuation("@"))
      result.getDirectives().add(parseDirective());
    return result;
  }

  private Fragment parseInlineFragment() throws EGraphQLException, IOException {
    Fragment result = new Fragment();
    if (hasName("on"))
    {
      consumeName("on");
      result.setTypeCondition(consumeName());
    }
    parseFragmentInner(result);
    return result;
  }

  private Operation parseOperation(String name) throws EGraphQLException, IOException {
    Operation result = new Operation();
    if (name.equals("mutation")) {
      result.setOperationType(Operation.OperationType.qglotMutation);
      if (hasName())
        result.setName(consumeName());
    } else if (name.equals("query")) {
      result.setOperationType(Operation.OperationType.qglotQuery);
      if (hasName())
        result.setName(consumeName());
    }  else
      result.setName(name);
    parseOperationInner(result);
    return result;
  }

  private void parseOperationInner(Operation op) throws EGraphQLException, IOException {
    if (hasPunctuation("(")) {
      consumePunctuation("(");
      do 
        op.getVariables().add(parseVariable());
      while (!hasPunctuation(")"));
      consumePunctuation(")");
    }
    while (hasPunctuation("@"))
      op.getDirectives().add(parseDirective());
    if (hasPunctuation("{")) {
      consumePunctuation("{");
      do
        op.getSelectionSet().add(parseSelection());
      while (!hasPunctuation("}"));
      consumePunctuation("}");
    }
  }

  private Selection parseSelection() throws EGraphQLException, IOException {
    Selection result = new Selection();
    if (hasPunctuation("...")) {
      consumePunctuation("...");
      if (hasName() && !token.toString().equals("on")) 
        result.setFragmentSpread(parseFragmentSpread());
      else
        result.setInlineFragment(parseInlineFragment());
    } else
      result.setField(parseField());
    return result;
  }

  private Variable parseVariable() throws EGraphQLException, IOException {
    Variable result = new Variable();
    consumePunctuation("$");
    result.setName(consumeName());
    consumePunctuation(":");
    result.setTypeName(consumeName());
    if (hasPunctuation("="))
    {
      consumePunctuation("=");
      result.setDefaultValue(parseValue());
    }
    return result;
  }

}