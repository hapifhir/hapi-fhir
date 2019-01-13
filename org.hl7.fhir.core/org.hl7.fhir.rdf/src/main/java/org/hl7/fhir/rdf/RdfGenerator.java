package org.hl7.fhir.rdf;

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

import org.hl7.fhir.dstu3.formats.TurtleLexer;
import org.hl7.fhir.dstu3.formats.TurtleLexer.TurtleTokenType;
import org.hl7.fhir.utilities.Utilities;

public class RdfGenerator {

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

		public boolean write(LineOutputStreamWriter writer, int indent) throws Exception {
			if (predicates.isEmpty()) 
				return false;
			if (predicates.size() == 1 && predicates.get(0).object instanceof StringType && Utilities.noString(predicates.get(0).comment)) {
				writer.write(" "+predicates.get(0).predicate+" "+((StringType) predicates.get(0).object).value);
				return false;
			}
			String left = Utilities.padLeft("", ' ', indent);
			int i = 0;
			for (Predicate po : predicates) {
				writer.write("\r\n");
				if (po.getObject() instanceof StringType)
					writer.write(left+" "+po.getPredicate()+" "+((StringType) po.getObject()).value);
				else {
					writer.write(left+" "+po.getPredicate()+" [");
					if (((Complex) po.getObject()).write(writer, indent+2))
						writer.write(left+" ]");
					else
						writer.write(" ]");
				}
				i++;
				if (i < predicates.size())
					writer.write(";");
				if (!Utilities.noString(po.comment)) 
					writer.write(" # "+escape(po.comment, false));
			}
			return true;      
		}

    public boolean write(StringBuilder b, int indent) throws Exception {
      if (predicates.isEmpty()) 
        return false;
      if (predicates.size() == 1 && predicates.get(0).object instanceof StringType && Utilities.noString(predicates.get(0).comment)) {
        b.append(" "+predicates.get(0).makelink()+" "+Utilities.escapeXml(((StringType) predicates.get(0).object).value));
        return false;
      }
      String left = Utilities.padLeft("", ' ', indent);
      int i = 0;
      for (Predicate po : predicates) {
        b.append("\r\n");
        if (po.getObject() instanceof StringType)
          b.append(left+" "+po.makelink()+" "+Utilities.escapeXml(((StringType) po.getObject()).value));
        else {
          b.append(left+" "+po.makelink()+" [");
          if (((Complex) po.getObject()).write(b, indent+2))
            b.append(left+" ]");
          else
            b.append(" ]");
        }
        i++;
        if (i < predicates.size())
          b.append(";");
        if (!Utilities.noString(po.comment)) 
          b.append(" # "+Utilities.escapeXml(escape(po.comment, false)));
      }
      return true;      
    }

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
			Predicate p = new Predicate();
			p.predicate = predicate;
			predicateSet.add(predicate);
			if (object instanceof StringType)
				objectSet.add(((StringType) object).value);
			p.object = object;
			predicates.add(p);
			return this;
		}

    public Complex linkedPredicate(String predicate, Triple object, String link) {
      Predicate p = new Predicate();
      p.predicate = predicate;
      p.link = link;
      predicateSet.add(predicate);
      if (object instanceof StringType)
        objectSet.add(((StringType) object).value);
      p.object = object;
      predicates.add(p);
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
			RdfGenerator.this.prefix(code, url);
		}
	}

	private class Predicate {
		protected String predicate;
		protected String link;
		protected Triple object;
		protected String comment;

		public String getPredicate() {
			return predicate;
		}
		public String makelink() {
      if (link == null)
        return predicate;
      else
        return "<a href=\""+link+"\">"+predicate+"</a>";
    }
		
    public Triple getObject() {
			return object;
		}
		public String getComment() {
			return comment;
		}
	}

	public class Subject extends Complex {
		private String id;

		public Predicate predicate(String predicate, Triple object, String comment) {
			Predicate p = new Predicate();
			p.predicate = predicate;
			predicateSet.add(predicate);
			if (object instanceof StringType)
				objectSet.add(((StringType) object).value);
			p.object = object;
			predicates.add(p);
			p.comment = comment; 
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

		public void importTtl(String ttl) throws Exception {
			if (!Utilities.noString(ttl)) {
				//        System.out.println("import ttl: "+ttl);
				TurtleLexer lexer = new TurtleLexer(ttl);
				String subject = null;
				String predicate = null;
				while (!lexer.done()) {
					if (subject == null)
						subject = lexer.next();
					if (predicate == null)
						predicate = lexer.next();
					if (lexer.peekType() == null) {
						throw new Error("Unexpected end of input parsing turtle");
					} if (lexer.peekType() == TurtleTokenType.TOKEN) {
						triple(subject, predicate, lexer.next());
					} else if (lexer.peek() == null) {
						throw new Error("Unexected - turtle lexer found no token");
					} else if (lexer.peek().equals("[")) {
						triple(subject, predicate, importComplex(lexer));
					} else
						throw new Exception("Not done yet");
					String n = lexer.next();
					if (Utilities.noString(n))
						break;
					if (n.equals(".")) {
						subject = null;
						predicate = null;
					} else if (n.equals(";")) {
						predicate = null;
					} else if (!n.equals(","))
						throw new Exception("Unexpected token "+n);          
				}
			}
		}

		private Complex importComplex(TurtleLexer lexer) throws Exception {
			lexer.next(); // read [
			Complex obj = new Complex();
			while (!lexer.peek().equals("]")) {
				String predicate = lexer.next();
				if (lexer.peekType() == TurtleTokenType.TOKEN || lexer.peekType() == TurtleTokenType.LITERAL) {
					obj.predicate(predicate, lexer.next());
				} else if (lexer.peek().equals("[")) {
					obj.predicate(predicate, importComplex(lexer));
				} else
					throw new Exception("Not done yet");
				if (lexer.peek().equals(";")) 
					lexer.next();
			}
			lexer.next(); // read ]
			return obj;
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
	}

	private List<Section> sections = new ArrayList<Section>();
	protected Set<String> subjectSet = new HashSet<String>();
	protected Set<String> predicateSet = new HashSet<String>();
	protected Set<String> objectSet = new HashSet<String>();
	private OutputStream destination;
	protected Map<String, String> prefixes = new HashMap<String, String>();


	public RdfGenerator(OutputStream destination) {
		super();
		this.destination = destination;
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


	public void prefix(String code, String url) {
		if (!prefixes.containsKey(code)) 
			prefixes.put(code, url);
		else if (!prefixes.get(code).equals(url))
			throw new Error("The prefix "+code+" is already assigned to "+prefixes.get(code)+" so cannot be set to "+url);
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

	//  protected PredicateObject predicateObj(String predicate, TripleObject object) {
	//    PredicateObject obj = new PredicateObject();
	//    obj.predicate = predicate;
	//    predicates.add(predicate);
	//    obj.object = object;
	//    return obj;
	//  }
	//
	//  protected PredicateObject predicate(String predicate, String object) {
	//    PredicateObject obj = new PredicateObject();
	//    obj.predicate = predicate;
	//    predicates.add(predicate);
	//    obj.object = new StringObject(object);
	//    return obj;
	//  }
	//
	//  protected PredicateObject predicate(String predicate, String object, String comment) {
	//    PredicateObject obj = new PredicateObject();
	//    obj.predicate = predicate;
	//    predicates.add(predicate);
	//    obj.object = new StringObject(object);
	//    obj.comment = comment;
	//    return obj;
	//  }
	//
	protected Complex complex() {
		return new Complex();
	}
	//
	//  protected TripleObject complex(PredicateObject predicate1, PredicateObject predicate2) {
	//    ComplexObject obj = new ComplexObject();
	//    obj.predicates.add(predicate1);
	//    obj.predicates.add(predicate2);
	//    return obj;
	//  }
	//
	//  protected TripleObject complex(PredicateObject predicate1, PredicateObject predicate2, PredicateObject predicate3) {
	//    ComplexObject obj = new ComplexObject();
	//    obj.predicates.add(predicate1);
	//    obj.predicates.add(predicate2);
	//    obj.predicates.add(predicate3);
	//    return obj;
	//  }
	//
	//  protected void triple(String section, String subject, String predicate, String object) {
	//    triple(section, subject, predicate, new StringObject(object), null);
	//  }
	//  
	//  protected void triple(String section, String subject, String predicate, TripleObject object) {
	//    triple(section, subject, predicate, object, null);
	//  }
	//  
	//  protected void triple(String section, String subject, String predicate, String object, String comment) {
	//    triple(section, subject, predicate, new StringObject(object), comment);
	//  }
	//  
	//  protected void primaryTriple(String section, String subject, String predicate, String object) {
	//    Section s = sections.get(sections.size()-1); 
	//    if (s.primary != null)
	//      throw new Error("multiple primary objects");
	//    s.primary = triple(section, null, subject, predicate, new StringObject(object), null);
	//  }
	//  
	//  protected Triple triple(String section, Integer order, String subject, String predicate, TripleObject object, String comment) {
	//    if (!hasSection(section))
	//      throw new Error("use undefined section "+section);
	//    checkPrefix(subject);
	//    checkPrefix(predicate);
	//    checkPrefix(object);
	//    predicates.add(predicate);
	//    Triple t = new Triple(section, order, subject, predicate, object, comment == null ? "" : " # "+comment.replace("\r\n", " ").replace("\r", " ").replace("\n", " "));
	//    triples.add(t);
	//    return t;
	//  }

	private void checkPrefix(Triple object) {
		if (object instanceof StringType)
			checkPrefix(((StringType) object).value);
		else {
			Complex obj = (Complex) object;
			for (Predicate po : obj.predicates) {
				checkPrefix(po.getPredicate());
				checkPrefix(po.getObject());
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

	protected class LineOutputStreamWriter extends OutputStreamWriter {
		private LineOutputStreamWriter(OutputStream out) throws UnsupportedEncodingException {
			super(out, "UTF-8");
		}

		private void ln() throws Exception {
			write("\r\n");
		}

		private void ln(String s) throws Exception {
			write(s);
			write("\r\n");
		}

	}


	public void commit(boolean header) throws Exception {
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
    b.append("<pre class\"rdf\">\r\n");
    commitPrefixes(b);
    for (Section s : sections) {
      commitSection(b, s);
    }
    b.append("</pre>\r\n");
    b.append("\r\n");
    return b.toString();
  }

  private void commitPrefixes(LineOutputStreamWriter writer, boolean header) throws Exception {
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

	private void commitSection(LineOutputStreamWriter writer, Section section) throws Exception {
		writer.ln("# - "+section.name+" "+Utilities.padLeft("", '-', 75-section.name.length()));
		writer.ln();
		for (Subject sbj : section.subjects) {
			writer.write(sbj.id);
			writer.write(" ");
			int i = 0;

			for (Predicate p : sbj.predicates) {
				writer.write(p.getPredicate());
				writer.write(" ");
				if (p.getObject() instanceof StringType)
					writer.write(((StringType) p.getObject()).value);
				else {
					writer.write("[");
					if (((Complex) p.getObject()).write(writer, 4))
						writer.write("\r\n  ]");
					else
						writer.write("]");
				}
				String comment = p.comment == null? "" : " # "+p.comment;
				i++;
				if (i < sbj.predicates.size())
					writer.write(";"+comment+"\r\n  ");
				else
					writer.write("."+comment+"\r\n\r\n");
			}
		}
	}

  private void commitSection(StringBuilder b, Section section) throws Exception {
    b.append("# - "+section.name+" "+Utilities.padLeft("", '-', 75-section.name.length())+"\r\n");
    b.append("\r\n");
    for (Subject sbj : section.subjects) {
      b.append(sbj.id);
      b.append(" ");
      int i = 0;

      for (Predicate p : sbj.predicates) {
        b.append(p.makelink());
        b.append(" ");
        if (p.getObject() instanceof StringType)
          b.append(Utilities.escapeXml(((StringType) p.getObject()).value));
        else {
          b.append("[");
          if (((Complex) p.getObject()).write(b, 4))
            b.append("\r\n  ]");
          else
            b.append("]");
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

	//  private void coomitTriple(LineOutputStreamWriter writer, Triple t) throws Exception, IOException {
	//    boolean follow = false;
	//    if (lastSubject != null) {
	//      follow = lastSubject.equals(t.getSubject());
	//      String c = follow ? ";" : ".";
	//      writer.ln(c+lastComment);
	//      if (!follow) 
	//        writer.ln();
	//    }
	//    String left = follow ? Utilities.padLeft("", ' ', 2) : t.getSubject();
	//    lastComment = t.getComment();
	//    lastSubject = t.getSubject();
	//  }


}


