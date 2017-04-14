package org.hl7.fhir.dstu2016may.utils;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Stack;

import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.utilities.Utilities;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;


/**
 * This is created to get a json parser that can track line numbers... grr...
 * 
 * @author Grahame Grieve
 *
 */
public class JsonTrackingParser {

	public enum TokenType {
		Open, Close, String, Number, Colon, Comma, OpenArray, CloseArray, Eof, Null, Boolean;
	}
	
	public class LocationData {
		private int line;
		private int col;
		
		protected LocationData(int line, int col) {
			super();
			this.line = line;
			this.col = col;
		}
		
		public int getLine() {
			return line;
		}
		
		public int getCol() {
			return col;
		}
		
		public void newLine() {
			line++;
			col = 1;		
		}

		public LocationData copy() {
			return new LocationData(line, col);
		}
	}
	
	private class State {
		private String name;
		private boolean isProp;
		protected State(String name, boolean isProp) {
			super();
			this.name = name;
			this.isProp = isProp;
		}
		public String getName() {
			return name;
		}
		public boolean isProp() {
			return isProp;
		}
	}
	
	private class Lexer {
		private String source;
		private int cursor;
		private String peek;
		private String value;
		private TokenType type;
		private Stack<State> states = new Stack<State>();
		private LocationData lastLocationBWS;
		private LocationData lastLocationAWS;
		private LocationData location;
		private StringBuilder b = new StringBuilder();
		
    public Lexer(String source) throws FHIRException {
    	this.source = source;
    	cursor = -1;
    	location = new LocationData(1, 1);  
    	start();
    }
    
    private boolean more() {
    	return peek != null || cursor < source.length(); 
    }
    
    private String getNext(int length) throws FHIRException {
    	String result = "";
      if (peek != null) {
      	if (peek.length() > length) {
      		result = peek.substring(0, length);
      		peek = peek.substring(length);
      	} else {
      		result = peek;
      		peek = null;
      	}
      }
      if (result.length() < length) {
      	int len = length - result.length(); 
      	if (cursor > source.length() - len) 
      		throw error("Attempt to read past end of source");
      	result = result + source.substring(cursor+1, cursor+len+1);
      	cursor = cursor + len;
      }
       for (char ch : result.toCharArray())
        if (ch == '\n')
          location.newLine();
        else
          location.col++;
      return result;
    }
    
    private char getNextChar() throws FHIRException {
      if (peek != null) {
      	char ch = peek.charAt(0);
      	peek = peek.length() == 1 ? null : peek.substring(1);
      	return ch;
      } else {
        cursor++;
        if (cursor >= source.length())
          return (char) 0;
        char ch = source.charAt(cursor);
        if (ch == '\n') {
          location.newLine();
        } else {
          location.col++;
        }
        return ch;
      }
    }
    
    private void push(char ch){
    	peek = peek == null ? String.valueOf(ch) : String.valueOf(ch)+peek;
    }
    
    private void parseWord(String word, char ch, TokenType type) throws FHIRException {
      this.type = type;
      value = ""+ch+getNext(word.length()-1);
      if (!value.equals(word))
      	throw error("Syntax error in json reading special word "+word);
    }
    
    private FHIRException error(String msg) {
      return new FHIRException("Error parsing JSON source: "+msg+" at Line "+Integer.toString(location.line)+" (path=["+path()+"])");
    }
    
    private String path() {
      if (states.empty())
        return value;
      else {
      	String result = "";
        for (State s : states) 
          result = result + '/'+ s.getName();
        result = result + value;
        return result;
      }
    }

    public void start() throws FHIRException {
//      char ch = getNextChar();
//      if (ch = '\.uEF')
//      begin
//        // skip BOM
//        getNextChar();
//        getNextChar();
//      end
//      else
//        push(ch);
      next();
    }
    
    public TokenType getType() {
    	return type;
    }
    
    public String getValue() {
    	return value;
    }


    public LocationData getLastLocationBWS() {
    	return lastLocationBWS;
    }

    public LocationData getLastLocationAWS() {
    	return lastLocationAWS;
    }

    public void next() throws FHIRException {
    	lastLocationBWS = location.copy();
    	char ch;
    	do {
    		ch = getNextChar();
    	} while (more() && Utilities.charInSet(ch, ' ', '\r', '\n', '\t'));
    	lastLocationAWS = location.copy();

    	if (!more()) {
    		type = TokenType.Eof;
    	} else {
    		switch (ch) {
    		case '{' : 
    			type = TokenType.Open;
    			break;
    		case '}' : 
    			type = TokenType.Close;
    			break;
    		case '"' :
    			type = TokenType.String;
    			b.setLength(0);
    			do {
    				ch = getNextChar();
    				if (ch == '\\') {
    					ch = getNextChar();
    					switch (ch) {
    					case '"': b.append('"'); break;
    					case '\\': b.append('\\'); break;
    					case '/': b.append('/'); break;
    					case 'n': b.append('\n'); break;
    					case 'r': b.append('\r'); break;
    					case 't': b.append('\t'); break;
    					case 'u': b.append((char) Integer.parseInt(getNext(4), 16)); break;
    					default :
    						throw error("unknown escape sequence: \\"+ch);
    					}
    					ch = ' ';
    				} else if (ch != '"')
    					b.append(ch);
    			} while (more() && (ch != '"'));
    			if (!more())
    				throw error("premature termination of json stream during a string");
    			value = b.toString();
    			break;
    		case ':' : 
    			type = TokenType.Colon;
    			break;
    		case ',' : 
    			type = TokenType.Comma;
    			break;
    		case '[' : 
    			type = TokenType.OpenArray;
    			break;
    		case ']' : 
    			type = TokenType.CloseArray;
    			break;
    		case 't' : 
    			parseWord("true", ch, TokenType.Boolean);
    			break;
    		case 'f' : 
    			parseWord("false", ch, TokenType.Boolean);
    			break;
    		case 'n' : 
    			parseWord("null", ch, TokenType.Null);
    			break;
    		default:
    			if ((ch >= '0' && ch <= '9') || ch == '-') {
    				type = TokenType.Number;
    				b.setLength(0);
    				while (more() && ((ch >= '0' && ch <= '9') || ch == '-' || ch == '.')) {
    					b.append(ch);
    					ch = getNextChar();
    				}
    				value = b.toString();
    				push(ch);
    			} else
    				throw error("Unexpected char '"+ch+"' in json stream");
    		}
    	}
    }

    public String consume(TokenType type) throws FHIRException {
      if (this.type != type)
        throw error("JSON syntax error - found "+type.toString()+" expecting "+type.toString());
      String result = value;
      next();
      return result;
    }

	}

	enum ItemType {
	  Object, String, Number, Boolean, Array, End, Eof, Null;
	}
	private Map<JsonElement, LocationData> map;
  private Lexer lexer;
  private ItemType itemType = ItemType.Object;
  private String itemName;
  private String itemValue;

	public static JsonObject parse(String source, Map<JsonElement, LocationData> map) throws FHIRException {
		JsonTrackingParser self = new JsonTrackingParser();
		self.map = map;
    return self.parse(source);
	}

	private JsonObject parse(String source) throws FHIRException {
		lexer = new Lexer(source);
		JsonObject result = new JsonObject();
		LocationData loc = lexer.location.copy();
    if (lexer.getType() == TokenType.Open) {
      lexer.next();
      lexer.states.push(new State("", false));
    } 
    else
      throw lexer.error("Unexpected content at start of JSON: "+lexer.getType().toString());

    parseProperty();
    readObject(result, true);
		map.put(result, loc);
    return result;
	}

	private void readObject(JsonObject obj, boolean root) throws FHIRException {
		map.put(obj, lexer.location.copy());

		while (!(itemType == ItemType.End) || (root && (itemType == ItemType.Eof))) {
			if (obj.has(itemName))
				throw lexer.error("Duplicated property name: "+itemName);

			switch (itemType) {
			case Object:
				JsonObject child = new JsonObject(); //(obj.path+'.'+ItemName);
				LocationData loc = lexer.location.copy();
				obj.add(itemName, child);
				next();
				readObject(child, false);
				map.put(obj, loc);
				break;
			case Boolean :
				JsonPrimitive v = new JsonPrimitive(Boolean.valueOf(itemValue));
				obj.add(itemName, v);
				map.put(v, lexer.location.copy());
				break;
			case String:
				v = new JsonPrimitive(itemValue);
				obj.add(itemName, v);
				map.put(v, lexer.location.copy());
				break;
			case Number:
				v = new JsonPrimitive(new BigDecimal(itemValue));
				obj.add(itemName, v);
				map.put(v, lexer.location.copy());
				break;
			case Null:
				JsonNull n = new JsonNull();
				obj.add(itemName, n);
				map.put(n, lexer.location.copy());
				break;
			case Array:
				JsonArray arr = new JsonArray(); // (obj.path+'.'+ItemName);
				loc = lexer.location.copy();
				obj.add(itemName, arr);
				next();
				readArray(arr, false);
				map.put(arr, loc);
				break;
			case Eof : 
				throw lexer.error("Unexpected End of File");
			default:
				break;
			}
			next();
		}
	}

	private void readArray(JsonArray arr, boolean root) throws FHIRException {
	  while (!((itemType == ItemType.End) || (root && (itemType == ItemType.Eof)))) {
	    switch (itemType) {
	    case Object:
	    	JsonObject obj  = new JsonObject(); // (arr.path+'['+inttostr(i)+']');
				LocationData loc = lexer.location.copy();
	    	arr.add(obj);
	      next();
	      readObject(obj, false);
				map.put(obj, loc);
	      break;
	    case String:
	    	JsonPrimitive v = new JsonPrimitive(itemValue);
				arr.add(v);
				map.put(v, lexer.location.copy());
				break;
	    case Number:
	    	v = new JsonPrimitive(new BigDecimal(itemValue));
				arr.add(v);
				map.put(v, lexer.location.copy());
				break;
	    case Null :
	    	JsonNull n = new JsonNull();
				arr.add(n);
				map.put(n, lexer.location.copy());
				break;
	    case Array:
        JsonArray child = new JsonArray(); // (arr.path+'['+inttostr(i)+']');
				loc = lexer.location.copy();
				arr.add(child);
        next();
	      readArray(child, false);
				map.put(arr, loc);
        break;
	    case Eof : 
	    	throw lexer.error("Unexpected End of File");
       default:
			break;
	    }
	    next();
	  }
	}

	private void next() throws FHIRException {
		switch (itemType) {
		case Object :
			lexer.consume(TokenType.Open);
			lexer.states.push(new State(itemName, false));
			if (lexer.getType() == TokenType.Close) {
				itemType = ItemType.End;
				lexer.next();
			} else
				parseProperty();
			break;
		case Null:
		case String:
		case Number: 
		case End: 
		case Boolean :
			if (itemType == ItemType.End)
				lexer.states.pop();
			if (lexer.getType() == TokenType.Comma) {
				lexer.next();
				parseProperty();
			} else if (lexer.getType() == TokenType.Close) {
				itemType = ItemType.End;
				lexer.next();
			} else if (lexer.getType() == TokenType.CloseArray) {
				itemType = ItemType.End;
				lexer.next();
			} else if (lexer.getType() == TokenType.Eof) {
				itemType = ItemType.Eof;
			} else
				throw lexer.error("Unexpected JSON syntax");
			break;
		case Array :
			lexer.next();
			lexer.states.push(new State(itemName+"[]", true));
			parseProperty();
			break;
		case Eof :
			throw lexer.error("JSON Syntax Error - attempt to read past end of json stream");
		default:
			throw lexer.error("not done yet (a): "+itemType.toString());
		}
	}

	private void parseProperty() throws FHIRException {
		if (!lexer.states.peek().isProp) {
			itemName = lexer.consume(TokenType.String);
			itemValue = null;
			lexer.consume(TokenType.Colon);
		}
		switch (lexer.getType()) {
		case Null :
			itemType = ItemType.Null;
			itemValue = lexer.value;
			lexer.next();
			break;
		case String :
			itemType = ItemType.String;
			itemValue = lexer.value;
			lexer.next();
			break;
		case Boolean :
			itemType = ItemType.Boolean;
			itemValue = lexer.value;
			lexer.next();
			break;
		case Number :
			itemType = ItemType.Number;
			itemValue = lexer.value;
			lexer.next();
			break;
		case Open :
			itemType = ItemType.Object;
			break;
		case OpenArray :
			itemType = ItemType.Array;
			break;
		case CloseArray :
			itemType = ItemType.End;
			break;
			// case Close, , case Colon, case Comma, case OpenArray,       !
		default:
			throw lexer.error("not done yet (b): "+lexer.getType().toString());
		}
	}
}
