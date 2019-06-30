package org.hl7.fhir.r4.utils.formats;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.hl7.fhir.exceptions.FHIRFormatError;
import org.hl7.fhir.utilities.Utilities;

public class Turtle {

	public static final String GOOD_IRI_CHAR = "a-zA-Z0-9\u00A0-\uFFFE";

  public static final String IRI_URL = "(([a-z])+:)*((%[0-9a-fA-F]{2})|[&'\\(\\)*+,;:@_~?!$\\/\\-\\#.\\="+GOOD_IRI_CHAR+"])+"; 
  public static final String LANG_REGEX = "[a-z]{2}(\\-[a-zA-Z]{2})?";

	// Object model
	public abstract class Triple {
		private String uri;
	}

	public class StringType extends Triple {
		private String value;

		public StringType(String value) {
			super();
			this.value = value;
		}
	}

	public class Complex extends Triple {
		protected List<Predicate> predicates = new ArrayList<Predicate>();

		public Complex predicate(String predicate, String object) {
			predicateSet.add(predicate);
			objectSet.add(object);
			return predicate(predicate, new StringType(object));
		}

    public Complex linkedPredicate(String predicate, String object, String link) {
      predicateSet.add(predicate);
      objectSet.add(object);
      return linkedPredicate(predicate, new StringType(object), link);
    }

		public Complex predicate(String predicate, Triple object) {
      Predicate p = getPredicate(predicate);
      if (p == null) {
        p = new Predicate();
			p.predicate = predicate;
			predicateSet.add(predicate);
        predicates.add(p);
      }
			if (object instanceof StringType)
				objectSet.add(((StringType) object).value);
      p.objects.add(object);
			return this;
		}

    protected Predicate getPredicate(String predicate) {
      for (Predicate p : predicates)
        if (p.predicate.equals(predicate))
          return p;
      return null;
    }

    public Complex linkedPredicate(String predicate, Triple object, String link) {
      Predicate p = getPredicate(predicate);
      if (p == null) {
        p = new Predicate();
      p.predicate = predicate;
      p.link = link;
      predicateSet.add(predicate);
        predicates.add(p);
      }
      if (object instanceof StringType)
        objectSet.add(((StringType) object).value);
      p.objects.add(object);
      return this;
    }

		public Complex predicate(String predicate) {
			predicateSet.add(predicate);
			Complex c = complex();
			predicate(predicate, c);
			return c;
		}

    public Complex linkedPredicate(String predicate, String link) {
      predicateSet.add(predicate);
      Complex c = complex();
      linkedPredicate(predicate, c, link);
      return c;
    }

		public void prefix(String code, String url) {
			Turtle.this.prefix(code, url);
		}
	}

	private class Predicate {
		protected String predicate;
		protected String link;
    protected List<Triple> objects = new ArrayList<Turtle.Triple>();
		protected String comment;

		public String getPredicate() {
			return predicate;
		}
		public String makelink() {
      if (link == null)
        return predicate;
      else
        return "<a href=\""+link+"\">"+Utilities.escapeXml(predicate)+"</a>";
    }
		
    public List<Triple> getObjects() {
      return objects;
		}
		public String getComment() {
			return comment;
		}
	}

	public class Subject extends Complex {
		private String id;

		public Predicate predicate(String predicate, Triple object, String comment) {
      Predicate p = getPredicate(predicate);
      if (p == null) {
        p = new Predicate();
			p.predicate = predicate;
			predicateSet.add(predicate);
			predicates.add(p);
			p.comment = comment; 
      }
      if (object instanceof StringType)
        objectSet.add(((StringType) object).value);
      p.objects.add(object);
			return p;
		}

		public void comment(String comment) {
			if (!Utilities.noString(comment)) {
				predicate("rdfs:comment", literal(comment));
				predicate("dcterms:description", literal(comment));
			}
		}

		public void label(String label) {
			if (!Utilities.noString(label)) {
				predicate("rdfs:label", literal(label));
				predicate("dc:title", literal(label));
			}
		}

	}

	public class Section {
		private String name;
		private List<Subject> subjects = new ArrayList<Subject>();

		public Subject triple(String subject, String predicate, String object, String comment) {
			return triple(subject, predicate, new StringType(object), comment);
		}

		public Subject triple(String subject, String predicate, String object) {
			return triple(subject, predicate, new StringType(object));
		}

		public Subject triple(String subject, String predicate, Triple object) {
			return triple(subject, predicate, object, null);     
		}

		public Subject triple(String subject, String predicate, Triple object, String comment) {
			Subject s = subject(subject);
			s.predicate(predicate, object, comment);
			return s;
		}

		public void comment(String subject, String comment) {
			triple(subject, "rdfs:comment", literal(comment));
			triple(subject, "dcterms:description", literal(comment));
		}

		public void label(String subject, String comment) {
			triple(subject, "rdfs:label", literal(comment));
			triple(subject, "dc:title", literal(comment));
		}

		public Subject subject(String subject) {
			for (Subject ss : subjects) 
				if (ss.id.equals(subject))
					return ss;
			Subject s = new Subject();
			s.id = subject;
			subjects.add(s);
			return s;
		}

    public boolean hasSubject(String subject) {
      for (Subject ss : subjects) 
        if (ss.id.equals(subject))
          return true;
      return false;
    }
	}

	private List<Section> sections = new ArrayList<Section>();
	protected Set<String> subjectSet = new HashSet<String>();
	protected Set<String> predicateSet = new HashSet<String>();
	protected Set<String> objectSet = new HashSet<String>();
	protected Map<String, String> prefixes = new HashMap<String, String>();

	public void prefix(String code, String url) {
		prefixes.put(code, url);
	}

	protected boolean hasSection(String sn) {
		for (Section s : sections)
			if (s.name.equals(sn))
				return true;
		return false;

	}

	public Section section(String sn) {
		if (hasSection(sn))
			throw new Error("Duplicate section name "+sn);
		Section s = new Section();
		s.name = sn;
		sections.add(s);
		return s;
	}

	protected String matches(String url, String prefixUri, String prefix) {
		if (url.startsWith(prefixUri)) {
			prefixes.put(prefix, prefixUri);
			return prefix+":"+escape(url.substring(prefixUri.length()), false);
		}
		return null;
	}

	protected Complex complex() {
		return new Complex();
	}

	private void checkPrefix(Triple object) {
		if (object instanceof StringType)
			checkPrefix(((StringType) object).value);
		else {
			Complex obj = (Complex) object;
			for (Predicate po : obj.predicates) {
				checkPrefix(po.getPredicate());
        for (Triple o : po.getObjects())
          checkPrefix(o);
			}
		}
	}

	protected void checkPrefix(String pname) {
		if (pname.startsWith("("))
			return;
		if (pname.startsWith("\""))
			return;
		if (pname.startsWith("<"))
			return;

		if (pname.contains(":")) {
			String prefix = pname.substring(0, pname.indexOf(":"));
			if (!prefixes.containsKey(prefix) && !prefix.equals("http")&& !prefix.equals("urn"))
				throw new Error("undefined prefix "+prefix); 
		}
	}

	protected StringType literal(String s) {
		return new StringType("\""+escape(s, true)+"\"");
	}

  protected StringType literalTyped(String s, String t) {
    return new StringType("\""+escape(s, true)+"\"^^xs:"+t);
  }

	public static String escape(String s, boolean string) {
		if (s == null)
			return "";

		StringBuilder b = new StringBuilder();
		for (char c : s.toCharArray()) {
			if (c == '\r')
				b.append("\\r");
			else if (c == '\n')
				b.append("\\n");
			else if (c == '"')
				b.append("\\\"");
			else if (c == '\\')
				b.append("\\\\");
			else if (c == '/' && !string)
				b.append("\\/");
			else 
				b.append(c);
		}   
		return b.toString();
	}

	protected String pctEncode(String s) {
		if (s == null)
			return "";

		StringBuilder b = new StringBuilder();
		for (char c : s.toCharArray()) {
			if (c >= 'A' && c <= 'Z')
				b.append(c);
			else if (c >= 'a' && c <= 'z')
				b.append(c);
			else if (c >= '0' && c <= '9')
				b.append(c);
			else if (c == '.')
				b.append(c);
			else 
				b.append("%"+Integer.toHexString(c));
		}   
		return b.toString();
	}

	protected List<String> sorted(Set<String> keys) {
		List<String> names = new ArrayList<String>();
		names.addAll(keys);
		Collections.sort(names);
		return names;
	}

	public void commit(OutputStream destination, boolean header) throws IOException {
		LineOutputStreamWriter writer = new LineOutputStreamWriter(destination);
		commitPrefixes(writer, header);
		for (Section s : sections) {
			commitSection(writer, s);
		}
		writer.ln("# -------------------------------------------------------------------------------------");
		writer.ln();
		writer.flush();
		writer.close();
	}

  public String asHtml() throws Exception {
    StringBuilder b = new StringBuilder();
    b.append("<pre class=\"rdf\">\r\n");
    commitPrefixes(b);
    for (Section s : sections) {
      commitSection(b, s);
    }
    b.append("</pre>\r\n");
    b.append("\r\n");
    return b.toString();
  }

	private void commitPrefixes(LineOutputStreamWriter writer, boolean header) throws IOException {
		if (header) {
			writer.ln("# FHIR Sub-definitions");
			writer.write("# This is work in progress, and may change rapidly \r\n");
			writer.ln();
			writer.write("# A note about policy: the focus here is providing the knowledge from \r\n"); 
			writer.write("# the FHIR specification as a set of triples for knowledge processing. \r\n");
			writer.write("# Where appopriate, predicates defined external to FHIR are used. \"Where \r\n");
			writer.write("# appropriate\" means that the predicates are a faithful representation \r\n");
			writer.write("# of the FHIR semantics, and do not involve insane (or owful) syntax. \r\n");
			writer.ln();
			writer.write("# Where the community agrees on additional predicate statements (such \r\n");
			writer.write("# as OWL constraints) these are added in addition to the direct FHIR \r\n");
			writer.write("# predicates \r\n");
			writer.ln();
			writer.write("# This it not a formal ontology, though it is possible it may start to become one eventually\r\n");
			writer.ln();
			writer.write("# this file refers to concepts defined in rim.ttl and to others defined elsewhere outside HL7 \r\n");
			writer.ln();
		}
		for (String p : sorted(prefixes.keySet()))
			writer.ln("@prefix "+p+": <"+prefixes.get(p)+"> .");
		writer.ln();
		if (header) {
			writer.ln("# Predicates used in this file:");
			for (String s : sorted(predicateSet)) 
				writer.ln(" # "+s);
			writer.ln();
		}
	}

  private void commitPrefixes(StringBuilder b) throws Exception {
    for (String p : sorted(prefixes.keySet()))
      b.append("@prefix "+p+": &lt;"+prefixes.get(p)+"&gt; .\r\n");
    b.append("\r\n");
  }

	//  private String lastSubject = null;
	//  private String lastComment = "";

	private void commitSection(LineOutputStreamWriter writer, Section section) throws IOException {
		writer.ln("# - "+section.name+" "+Utilities.padLeft("", '-', 75-section.name.length()));
		writer.ln();
		for (Subject sbj : section.subjects) {
      if (Utilities.noString(sbj.id)) {
        writer.write("[");
      } else {
			writer.write(sbj.id);
			writer.write(" ");
      }
			int i = 0;

			for (Predicate p : sbj.predicates) {
				writer.write(p.getPredicate());
				writer.write(" ");
        boolean first = true;
        for (Triple o : p.getObjects()) {
          if (first)
            first = false;
          else
            writer.write(", ");
          if (o instanceof StringType)
            writer.write(((StringType) o).value);
				else {
					writer.write("[");
            if (write((Complex) o, writer, 4))
						writer.write("\r\n  ]");
					else
						writer.write("]");
				}
        }
				String comment = p.comment == null? "" : " # "+p.comment;
				i++;
				if (i < sbj.predicates.size())
					writer.write(";"+comment+"\r\n  ");
        else {
          if (Utilities.noString(sbj.id)) 
            writer.write("]");
          writer.write(" ."+comment+"\r\n\r\n");
		}
	  }
	}
  }

  private void commitSection(StringBuilder b, Section section) throws Exception {
    b.append("# - "+section.name+" "+Utilities.padLeft("", '-', 75-section.name.length())+"\r\n");
    b.append("\r\n");
    for (Subject sbj : section.subjects) {
      b.append(Utilities.escapeXml(sbj.id));
      b.append(" ");
      int i = 0;

      for (Predicate p : sbj.predicates) {
        b.append(p.makelink());
        b.append(" ");
        boolean first = true;
        for (Triple o : p.getObjects()) {
          if (first)
            first = false;
          else
            b.append(", ");
          if (o instanceof StringType)
            b.append(Utilities.escapeXml(((StringType) o).value));
        else {
          b.append("[");
            if (write((Complex) o, b, 4))
            b.append("\r\n  ]");
          else
            b.append("]");
        }
        }
        String comment = p.comment == null? "" : " # "+p.comment;
        i++;
        if (i < sbj.predicates.size())
          b.append(";"+Utilities.escapeXml(comment)+"\r\n  ");
        else
          b.append("."+Utilities.escapeXml(comment)+"\r\n\r\n");
      }
    }
  }

	protected class LineOutputStreamWriter extends OutputStreamWriter {
		private LineOutputStreamWriter(OutputStream out) throws UnsupportedEncodingException {
			super(out, "UTF-8");
		}

		private void ln() throws IOException {
			write("\r\n");
		}

		private void ln(String s) throws IOException {
			write(s);
			write("\r\n");
		}
	}

	public boolean write(Complex complex, LineOutputStreamWriter writer, int indent) throws IOException {
		if (complex.predicates.isEmpty()) 
			return false;
    if (complex.predicates.size() == 1 && complex.predicates.get(0).getObjects().size()== 1 && complex.predicates.get(0).getObjects().get(0) instanceof StringType && Utilities.noString(complex.predicates.get(0).comment)) {
      writer.write(" "+complex.predicates.get(0).predicate+" "+((StringType) complex.predicates.get(0).getObjects().get(0)).value);
			return false;
		}
		String left = Utilities.padLeft("", ' ', indent);
		int i = 0;
		for (Predicate po : complex.predicates) {
			writer.write("\r\n");
      boolean first = true;
      for (Triple o : po.getObjects()) {
        if (first) {
          first = false;
          writer.write(left+" "+po.getPredicate()+" ");
        } else
          writer.write(", ");
        if (o instanceof StringType)
          writer.write(((StringType) o).value);
			else {
          writer.write("[");
          if (write((Complex) o, writer, indent+2))
            writer.write("\r\n"+left+" ]");
				else
					writer.write(" ]");
			}
      }
			i++;
			if (i < complex.predicates.size())
				writer.write(";");
			if (!Utilities.noString(po.comment)) 
				writer.write(" # "+escape(po.comment, false));
		}
		return true;      
	}

  public boolean write(Complex complex, StringBuilder b, int indent) throws Exception {
    if (complex.predicates.isEmpty()) 
      return false;
    if (complex.predicates.size() == 1 && complex.predicates.get(0).getObjects().size()== 1 && complex.predicates.get(0).getObjects().get(0) instanceof StringType && Utilities.noString(complex.predicates.get(0).comment)) {
      b.append(" "+complex.predicates.get(0).makelink()+" "+Utilities.escapeXml(((StringType) complex.predicates.get(0).getObjects().get(0)).value));
      return false;
    }
    String left = Utilities.padLeft("", ' ', indent);
    int i = 0;
    for (Predicate po : complex.predicates) {
      b.append("\r\n");
      boolean first = true;
      for (Triple o : po.getObjects()) {
        if (first) {
          first = false;
          b.append(left+" "+po.makelink()+" ");
        } else
          b.append(", ");
        if (o instanceof StringType)
          b.append(Utilities.escapeXml(((StringType) o).value));
      else {
          b.append("[");
          if (write((Complex) o, b, indent+2))
          b.append(left+" ]");
        else
          b.append(" ]");
      }
      }
      i++;
      if (i < complex.predicates.size())
        b.append(";");
      if (!Utilities.noString(po.comment)) 
        b.append(" # "+Utilities.escapeXml(escape(po.comment, false)));
    }
    return true;      
  }


  public abstract class TTLObject {
		protected int line;
		protected int col;

    abstract public boolean hasValue(String value);

    public int getLine() {
      return line;
    }

    public int getCol() {
      return col;
    }
    
    
	}


	public class TTLLiteral extends TTLObject {

		private String value;
		private String type;
		protected TTLLiteral(int line, int col) {
			this.line = line;
			this.col = col;
		}
    @Override
    public boolean hasValue(String value) {
      return value.equals(this.value);
    }
    public String getValue() {
      return value;
    }
    public String getType() {
      return type;
    }

	}

	public class TTLURL extends TTLObject {
		private String uri;

		protected TTLURL(int line, int col) {
			this.line = line;
			this.col = col;
		}

		public String getUri() {
			return uri;
		}

    public void setUri(String uri) throws FHIRFormatError {
			if (!uri.matches(IRI_URL))
        throw new FHIRFormatError("Illegal URI "+uri);
			this.uri = uri;
		}

    @Override
    public boolean hasValue(String value) {
      return value.equals(this.uri);
	}
  }

  public class TTLList extends TTLObject {
    private List<TTLObject> list = new ArrayList<Turtle.TTLObject>();

    public TTLList(TTLObject obj) {
      super();
      list.add(obj);
    }
    
    @Override
    public boolean hasValue(String value) {
      for (TTLObject obj : list)
        if (obj.hasValue(value))
          return true;
      return false;
    }

    public List<TTLObject> getList() {
      return list;
    }
    
  }
	public class TTLComplex extends TTLObject {
		private Map<String, TTLObject> predicates = new HashMap<String, Turtle.TTLObject>();
		protected TTLComplex(int line, int col) {
			this.line = line;
			this.col = col;
		}
    public Map<String, TTLObject> getPredicates() {
      return predicates;
	}
    @Override
    public boolean hasValue(String value) {
      return false;
    }
    public void addPredicate(String uri, TTLObject obj) {
      if (!predicates.containsKey(uri))
        predicates.put(uri, obj);
      else {
        TTLObject eo = predicates.get(uri);
        TTLList list = null; 
        if (eo instanceof TTLList) 
          list = (TTLList) eo; 
        else {
          list = new TTLList(eo);
          predicates.put(uri, list);
        }
        list.list.add(obj);
      }
    }
    public void addPredicates(Map<String, TTLObject> values) {
      for (String s : values.keySet()) {
        addPredicate(s, values.get(s));
      }
    }
  }

  private Map<TTLURL, TTLComplex> objects = new HashMap<TTLURL, Turtle.TTLComplex>();

  private Object base;

	public enum LexerTokenType {
		TOKEN, // [, ], :, @
		WORD, // a word 
		URI, // a URI <>
		LITERAL // "..."
	}

	public class Lexer {


		private String source;
		private LexerTokenType type;
		private int cursor, line, col, startLine, startCol;
		private String token;

    public Lexer(String source) throws FHIRFormatError {
			this.source = source;
			cursor = 0;
			line = 1;
			col = 1;
      readNext(false);
		}

		private void skipWhitespace() {
			while (cursor < source.length()) {
				char ch = source.charAt(cursor);
				if (Character.isWhitespace(ch))
					grab();
				else if (ch == '#') {
					ch = grab();
					while (cursor < source.length()) {
						ch = grab();
						if (ch == '\r' || ch == '\n') {
							break;
						}
					}          
				} else
					break;
			}
		}

		private char grab() {
			char c = source.charAt(cursor);
			if (c == '\n') {
				line++;
				col = 1;
			} else
				col++;

			cursor++;
			return c;
		}

    private void readNext(boolean postColon) throws FHIRFormatError {    
			token = null;
			type = null;
			skipWhitespace();
			if (cursor >= source.length())
				return;
			startLine = line;
			startCol = col;
			char ch = grab();
			StringBuilder b = new StringBuilder();
			switch (ch) {
			case '@':
			case '.': 
			case ':': 
			case ';': 
			case '^': 
			case ',': 
			case ']': 
			case '[': 
			case '(': 
			case ')': 
				type = LexerTokenType.TOKEN;
				b.append(ch);
				token = b.toString();
				return;
			case '<': 
				while (cursor < source.length()) {
					ch = grab();
					if (ch == '>')
						break;
					b.append(ch);
				}
				type = LexerTokenType.URI;
				token = unescape(b.toString(), true);
				return;        
			case '"': 
				b.append(ch);
				String end = "\"";
				while (cursor < source.length()) {
					ch = grab();
          if (b.length() == 2 && ch != '"' && b.equals("\"\"")) {
						cursor--;
						break;
					}
					b.append(ch);
          if (ch == '"')
					if (b.toString().equals("\"\"\""))
						end = "\"\"\"";
            else if (!b.toString().equals("\"\"") && b.toString().endsWith(end) && !b.toString().endsWith("\\"+end))
						break;
				}
				type = LexerTokenType.LITERAL;
				token = unescape(b.toString().substring(end.length(), b.length()-end.length()), false);
				return;        
			case '\'': 
				b.append(ch);
				end = "'";
				while (cursor < source.length()) {
					ch = grab();
					if (b.equals("''") && ch != '\'') {
						cursor--;
						break;
					}
					b.append(ch);
					if (b.toString().equals("'''"))
						end = "'''";
					else if (!b.toString().equals("''") && b.toString().endsWith(end))
						break;
				}
				type = LexerTokenType.LITERAL;
				token = unescape(b.toString().substring(end.length(), b.length()-end.length()), false);
				return;        
			default:
        if (Utilities.charInRange(ch, '0', '9') || Utilities.charInRange(ch, 'a', 'z') || Utilities.charInRange(ch, 'A', 'Z') || Utilities.charInSet(ch, '_', '-', '+', '%')) {
					b.append(ch);
					while (cursor < source.length()) {
						ch = grab();
            //						if (!Utilities.charInRange(ch, '0', '9') && !Utilities.charInRange(ch, 'a', 'z') && !Utilities.charInRange(ch, 'A', 'Z') && !Utilities.charInSet(ch, '_', '-', '+', '.', '\\', '#'))
            if (Character.isWhitespace(ch) || Utilities.charInSet(ch, ';', ']', ')', '~') || (( ch == ':') && !postColon))
							break;
						b.append(ch);
					}
					type = LexerTokenType.WORD;
					token = b.toString();
					cursor--;
					return;        
				} else
					throw error("unexpected lexer char "+ch);
			}
		}

    private String unescape(String s, boolean isUri) throws FHIRFormatError {
			StringBuilder b = new StringBuilder();
			int i = 0;
			while (i < s.length()) {
				char ch = s.charAt(i);
				if (ch == '\\' && i < s.length()-1) {
					i++;
					switch (s.charAt(i)) {
					case 't': 
						b.append('\t');
						break;
					case 'r':
						b.append('\r');
						break;
					case 'n': 
						b.append('\n');
						break;
					case 'f': 
						b.append('\f');
						break;
					case '\'':
						b.append('\'');
						break;
          case '\"':
            b.append('\"');
            break;
					case '\\': 
						b.append('\\');
						break;
					case '/': 
						b.append('\\');
						break;
					case 'U':
					case 'u':
						i++;
						int l = 4;
						int uc = Integer.parseInt(s.substring(i, i+l), 16);
						if (uc < (isUri ? 33 : 32)) {
							l = 8;
							uc = Integer.parseInt(s.substring(i, i+8), 16);
						}
						if (uc < (isUri ? 33 : 32) || (isUri && (uc == 0x3C || uc == 0x3E)))
              throw new FHIRFormatError("Illegal unicode character");
						b.append((char) uc);
						i = i + l;
						break;
					default:
            throw new FHIRFormatError("Unknown character escape \\"+s.charAt(i));
					}
				} else {
					b.append(ch);
				}
        i++;
			}
			return b.toString();
		}

		public boolean done() {
			return type == null;
		}

    public String next(LexerTokenType type, boolean postColon) throws FHIRFormatError {
			if (type != null && this.type != type)
				throw error("Unexpected type. Found "+this.type.toString()+" looking for a "+type.toString());
			String res = token;
      readNext(postColon);
			return res;
		}

		public String peek() throws Exception {
			return token;
		}

		public LexerTokenType peekType() {
			return type;
		}

    public void token(String token) throws FHIRFormatError {
			if (!token.equals(this.token))
				throw error("Unexpected word "+this.token+" looking for "+token);
      next(LexerTokenType.TOKEN, token.equals(":"));
		}

		public void word(String word) throws Exception {
			if (!word.equals(this.token))
				throw error("Unexpected word "+this.token+" looking for "+word);
      next(LexerTokenType.WORD, false);
		}

    public String word() throws FHIRFormatError {
			String t = token;
      next(LexerTokenType.WORD, false);
			return t;
		}

    public String uri() throws FHIRFormatError {
			if (this.type != LexerTokenType.URI)
				throw error("Unexpected type. Found "+this.type.toString()+" looking for a URI");
			String t = token;
      next(LexerTokenType.URI, false);
			return t;
		}

    public String literal() throws FHIRFormatError {
			if (this.type != LexerTokenType.LITERAL)
				throw error("Unexpected type. Found "+this.type.toString()+" looking for a Literal");
			String t = token;
      next(LexerTokenType.LITERAL, false);
			return t;
		}

		public boolean peek(LexerTokenType type, String token) {
			return this.type == type && this.token.equals(token);
		}

    public FHIRFormatError error(String message) {
      return new FHIRFormatError("Syntax Error parsing Turtle on line "+Integer.toString(line)+" col "+Integer.toString(col)+": "+message);
		}

	}
	//	
	//	public void importTtl(Section sct, String ttl) throws Exception {
	//		if (!Utilities.noString(ttl)) {
	//			//        System.out.println("import ttl: "+ttl);
	//			Lexer lexer = new Lexer(ttl);
	//			String subject = null;
	//			String predicate = null;
	//			while (!lexer.done()) {
	//				if (subject == null)
	//					subject = lexer.next();
	//				if (predicate == null)
	//					predicate = lexer.next();
	//				if (lexer.peekType() == null) {
	//					throw new Error("Unexpected end of input parsing turtle");
	//				} if (lexer.peekType() == LexerTokenType.TOKEN) {
	//					sct.triple(subject, predicate, lexer.next());
	//				} else if (lexer.peek() == null) {
	//					throw new Error("Unexected - turtle lexer found no token");
	//				} else if (lexer.peek().equals("[")) {
	//					sct.triple(subject, predicate, importComplex(lexer));
	//				} else
	//					throw new Exception("Not done yet");
	//				String n = lexer.next();
	//				if (Utilities.noString(n))
	//					break;
	//				if (n.equals(".")) {
	//					subject = null;
	//					predicate = null;
	//				} else if (n.equals(";")) {
	//					predicate = null;
	//				} else if (!n.equals(","))
	//					throw new Exception("Unexpected token "+n);          
	//			}
	//		}
	//	}

  public void parse(String source) throws FHIRFormatError {
		prefixes.clear();
		prefixes.put("_", "urn:uuid:4425b440-2c33-4488-b9fc-cf9456139995#");
		parse(new Lexer(source));
	}

  private void parse(Lexer lexer) throws FHIRFormatError {
		boolean doPrefixes = true;
		while (!lexer.done()) {
      if (doPrefixes && (lexer.peek(LexerTokenType.TOKEN, "@") || lexer.peek(LexerTokenType.WORD, "PREFIX") || lexer.peek(LexerTokenType.WORD, "BASE"))) {
				boolean sparqlStyle = false;
        boolean base = false;
				if (lexer.peek(LexerTokenType.TOKEN, "@")) {
					lexer.token("@");
          String p = lexer.word();
          if (p.equals("base"))
            base = true;
          else if (!p.equals("prefix"))
            throw new FHIRFormatError("Unexpected token "+p);  
				} else {
					sparqlStyle = true;
          String p = lexer.word();
          if (p.equals("BASE"))
            base = true;
          else if (!p.equals("PREFIX"))
            throw new FHIRFormatError("Unexpected token "+p);  
        }
        String prefix = null; 
        if (!base) {
          prefix = lexer.peekType() == LexerTokenType.WORD ? lexer.next(LexerTokenType.WORD, false) : null;
				lexer.token(":");
        }
        String url = lexer.next(LexerTokenType.URI, false);
				if (!sparqlStyle)
					lexer.token(".");
        if (!base)
				prefix(prefix, url);
        else if (this.base == null)
          this.base = url;
        else
          throw new FHIRFormatError("Duplicate @base");  
			} else if (lexer.peekType() == LexerTokenType.URI) {
				doPrefixes = false;
				TTLURL uri = new TTLURL(lexer.startLine, lexer.startCol);
				uri.setUri(lexer.uri());
				TTLComplex complex = parseComplex(lexer);
				objects.put(uri, complex);
				lexer.token(".");
			} else if (lexer.peekType() == LexerTokenType.WORD) {
				doPrefixes = false;
				TTLURL uri = new TTLURL(lexer.startLine, lexer.startCol);
				String pfx = lexer.word();
				if (!prefixes.containsKey(pfx))
          throw new FHIRFormatError("Unknown prefix "+pfx);
				lexer.token(":");
				uri.setUri(prefixes.get(pfx)+lexer.word());
				TTLComplex complex = parseComplex(lexer);
				objects.put(uri, complex);
				lexer.token(".");
			} else if (lexer.peek(LexerTokenType.TOKEN, ":")) {
				doPrefixes = false;
				TTLURL uri = new TTLURL(lexer.startLine, lexer.startCol);
				lexer.token(":");
				if (!prefixes.containsKey(null))
          throw new FHIRFormatError("Unknown prefix ''");
				uri.setUri(prefixes.get(null)+lexer.word());
				TTLComplex complex = parseComplex(lexer);
				objects.put(uri, complex);
				lexer.token(".");
			} else if (lexer.peek(LexerTokenType.TOKEN, "[")) {
				doPrefixes = false;
				lexer.token("[");
				TTLComplex bnode = parseComplex(lexer);
				lexer.token("]");
				TTLComplex complex = null;
        if (!lexer.peek(LexerTokenType.TOKEN, ".")) {
					complex = parseComplex(lexer);
          // at this point, we collapse bnode and complex, and give bnode a fictional identity
          bnode.addPredicates(complex.predicates);
        }
        
        objects.put(anonymousId(), bnode);
				lexer.token(".");
			} else 
				throw lexer.error("Unknown token "+lexer.token);
		}
	}

  private TTLURL anonymousId() throws FHIRFormatError {
    TTLURL url = new TTLURL(-1, -1);
    url.setUri("urn:uuid:"+UUID.randomUUID().toString().toLowerCase());
    return url;
  }

  private TTLComplex parseComplex(Lexer lexer) throws FHIRFormatError {
		TTLComplex result = new TTLComplex(lexer.startLine, lexer.startCol);

		boolean done = lexer.peek(LexerTokenType.TOKEN, "]");
		while (!done) {
			String uri = null;
			if (lexer.peekType() == LexerTokenType.URI)
				uri = lexer.uri();
			else {
				String t = lexer.peekType() == LexerTokenType.WORD ? lexer.word() : null;
				if (lexer.type == LexerTokenType.TOKEN && lexer.token.equals(":")) {
					lexer.token(":");
					if (!prefixes.containsKey(t))
            throw new FHIRFormatError("unknown prefix "+t);
					uri = prefixes.get(t)+lexer.word();
				} else if (t.equals("a"))
					uri = prefixes.get("rdfs")+"type";
				else
					throw lexer.error("unexpected token");
			}

			boolean inlist = false;
			if (lexer.peek(LexerTokenType.TOKEN, "(")) {
				inlist = true;
				lexer.token("(");
			}

			boolean rpt = false;
			do {
				if (lexer.peek(LexerTokenType.TOKEN, "[")) {
					lexer.token("[");
          result.addPredicate(uri, parseComplex(lexer));
					lexer.token("]");
				} else if (lexer.peekType() == LexerTokenType.URI) {
					TTLURL u = new TTLURL(lexer.startLine, lexer.startCol);
					u.setUri(lexer.uri());
          result.addPredicate(uri, u);
				} else if (lexer.peekType() == LexerTokenType.LITERAL) {
					TTLLiteral u = new TTLLiteral(lexer.startLine, lexer.startCol);
					u.value = lexer.literal();
					if (lexer.peek(LexerTokenType.TOKEN, "^")) {
						lexer.token("^");
						lexer.token("^");
						if (lexer.peekType() == LexerTokenType.URI) {
							u.type = lexer.uri();
						} else {
							String l = lexer.word();
							lexer.token(":");
							u.type = prefixes.get(l)+ lexer.word();
						}
					}
					if (lexer.peek(LexerTokenType.TOKEN, "@")) {
						//lang tag - skip it 
						lexer.token("@");
            String lang = lexer.word();
            if (!lang.matches(LANG_REGEX)) {
              throw new FHIRFormatError("Invalid Language tag "+lang);
            }
					}
          result.addPredicate(uri, u);
				} else if (lexer.peekType() == LexerTokenType.WORD || lexer.peek(LexerTokenType.TOKEN, ":")) {
					int sl = lexer.startLine;
					int sc = lexer.startCol;
					String pfx = lexer.peekType() == LexerTokenType.WORD ? lexer.word() : null;
					if (Utilities.isDecimal(pfx) && !lexer.peek(LexerTokenType.TOKEN, ":")) {
						TTLLiteral u = new TTLLiteral(sl, sc);
						u.value = pfx;
            result.addPredicate(uri, u);					
					} else if (("false".equals(pfx) || "true".equals(pfx)) && !lexer.peek(LexerTokenType.TOKEN, ":")) {
						TTLLiteral u = new TTLLiteral(sl, sc);
						u.value = pfx;
            result.addPredicate(uri, u);					
					} else {
						if (!prefixes.containsKey(pfx))
              throw new FHIRFormatError("Unknown prefix "+(pfx == null ? "''" : pfx));						
						TTLURL u = new TTLURL(sl, sc);
						lexer.token(":");
						u.setUri(prefixes.get(pfx)+lexer.word());
            result.addPredicate(uri, u);
					} 
				} else if (!lexer.peek(LexerTokenType.TOKEN, ";") && (!inlist || !lexer.peek(LexerTokenType.TOKEN, ")"))) {
          throw new FHIRFormatError("unexpected token "+lexer.token);
				}

				if (inlist)
					rpt = !lexer.peek(LexerTokenType.TOKEN, ")");
				else {
					rpt = lexer.peek(LexerTokenType.TOKEN, ",");
					if (rpt)
            lexer.readNext(false);
				}
			} while (rpt);
			if (inlist)
				lexer.token(")");

			if (lexer.peek(LexerTokenType.TOKEN, ";")) {
        while ((lexer.peek(LexerTokenType.TOKEN, ";")))
				lexer.token(";");
        done = lexer.peek(LexerTokenType.TOKEN, ".") || lexer.peek(LexerTokenType.TOKEN, "]");
			} else {
				done = true;
			}
		}
		return result;
	}

  public Map<TTLURL, TTLComplex> getObjects() {
    return objects;
  }

  public TTLComplex getObject(String url) {
    for (TTLURL t : objects.keySet()) {
      if (t.getUri().equals(url))
        return objects.get(t);
    }
    return null;
  }

	//	public void parseFragment(Lexer lexer) throws Exception {
	//		lexer.next(); // read [
	//		Complex obj = new Complex();
	//		while (!lexer.peek().equals("]")) {
	//			String predicate = lexer.next();
	//			if (lexer.peekType() == LexerTokenType.TOKEN || lexer.peekType() == LexerTokenType.LITERAL) {
	//				obj.predicate(predicate, lexer.next());
	//			} else if (lexer.peek().equals("[")) {
	//				obj.predicate(predicate, importComplex(lexer));
	//			} else
	//				throw new Exception("Not done yet");
	//			if (lexer.peek().equals(";")) 
	//				lexer.next();
	//		}
	//		lexer.next(); // read ]
	//		//return obj;
	//	}
	//
	//	public void importTtl(Section sct, String ttl) throws Exception {
	//		if (!Utilities.noString(ttl)) {
	//			//        System.out.println("import ttl: "+ttl);
	//			Lexer lexer = new Lexer(ttl);
	//			String subject = null;
	//			String predicate = null;
	//			while (!lexer.done()) {
	//				if (subject == null)
	//					subject = lexer.next();
	//				if (predicate == null)
	//					predicate = lexer.next();
	//				if (lexer.peekType() == null) {
	//					throw new Error("Unexpected end of input parsing turtle");
	//				} if (lexer.peekType() == LexerTokenType.TOKEN) {
	//					sct.triple(subject, predicate, lexer.next());
	//				} else if (lexer.peek() == null) {
	//					throw new Error("Unexected - turtle lexer found no token");
	//				} else if (lexer.peek().equals("[")) {
	//					sct.triple(subject, predicate, importComplex(lexer));
	//				} else
	//					throw new Exception("Not done yet");
	//				String n = lexer.next();
	//				if (Utilities.noString(n))
	//					break;
	//				if (n.equals(".")) {
	//					subject = null;
	//					predicate = null;
	//				} else if (n.equals(";")) {
	//					predicate = null;
	//				} else if (!n.equals(","))
	//					throw new Exception("Unexpected token "+n);          
	//			}
	//		}
	//}

	//	private Complex importComplex(Lexer lexer) throws Exception {
	//		lexer.next(); // read [
	//		Complex obj = new Complex();
	//		while (!lexer.peek().equals("]")) {
	//			String predicate = lexer.next();
	//			if (lexer.peekType() == LexerTokenType.TOKEN || lexer.peekType() == LexerTokenType.LITERAL) {
	//				obj.predicate(predicate, lexer.next());
	//			} else if (lexer.peek().equals("[")) {
	//				obj.predicate(predicate, importComplex(lexer));
	//			} else
	//				throw new Exception("Not done yet");
	//			if (lexer.peek().equals(";")) 
	//				lexer.next();
	//		}
	//		lexer.next(); // read ]
	//		return obj;
	//	}

}
