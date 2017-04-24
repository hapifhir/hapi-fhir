package org.hl7.fhir.dstu3.utils;

// remember group resolution
// trace - account for which wasn't transformed in the source

import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.hl7.fhir.dstu3.conformance.ProfileUtilities;
import org.hl7.fhir.dstu3.conformance.ProfileUtilities.ProfileKnowledgeProvider;
import org.hl7.fhir.dstu3.context.IWorkerContext;
import org.hl7.fhir.dstu3.context.IWorkerContext.ValidationResult;
import org.hl7.fhir.dstu3.elementmodel.Element;
import org.hl7.fhir.dstu3.elementmodel.Property;
import org.hl7.fhir.dstu3.model.Base;
import org.hl7.fhir.dstu3.model.BooleanType;
import org.hl7.fhir.dstu3.model.CodeType;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.ConceptMap;
import org.hl7.fhir.dstu3.model.ConceptMap.ConceptMapGroupComponent;
import org.hl7.fhir.dstu3.model.ConceptMap.SourceElementComponent;
import org.hl7.fhir.dstu3.model.ConceptMap.TargetElementComponent;
import org.hl7.fhir.dstu3.model.Constants;
import org.hl7.fhir.dstu3.model.ContactDetail;
import org.hl7.fhir.dstu3.model.ContactPoint;
import org.hl7.fhir.dstu3.model.DecimalType;
import org.hl7.fhir.dstu3.model.ElementDefinition;
import org.hl7.fhir.dstu3.model.ElementDefinition.ElementDefinitionMappingComponent;
import org.hl7.fhir.dstu3.model.ElementDefinition.TypeRefComponent;
import org.hl7.fhir.dstu3.model.Enumeration;
import org.hl7.fhir.dstu3.model.Enumerations.ConceptMapEquivalence;
import org.hl7.fhir.dstu3.model.Enumerations.PublicationStatus;
import org.hl7.fhir.dstu3.model.ExpressionNode;
import org.hl7.fhir.dstu3.model.ExpressionNode.CollectionStatus;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.IntegerType;
import org.hl7.fhir.dstu3.model.Narrative.NarrativeStatus;
import org.hl7.fhir.dstu3.model.PrimitiveType;
import org.hl7.fhir.dstu3.model.Reference;
import org.hl7.fhir.dstu3.model.Resource;
import org.hl7.fhir.dstu3.model.ResourceFactory;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.dstu3.model.StructureDefinition;
import org.hl7.fhir.dstu3.model.StructureDefinition.StructureDefinitionMappingComponent;
import org.hl7.fhir.dstu3.model.StructureDefinition.TypeDerivationRule;
import org.hl7.fhir.dstu3.model.StructureMap;
import org.hl7.fhir.dstu3.model.StructureMap.StructureMapContextType;
import org.hl7.fhir.dstu3.model.StructureMap.StructureMapGroupComponent;
import org.hl7.fhir.dstu3.model.StructureMap.StructureMapGroupInputComponent;
import org.hl7.fhir.dstu3.model.StructureMap.StructureMapGroupRuleComponent;
import org.hl7.fhir.dstu3.model.StructureMap.StructureMapGroupRuleDependentComponent;
import org.hl7.fhir.dstu3.model.StructureMap.StructureMapGroupRuleSourceComponent;
import org.hl7.fhir.dstu3.model.StructureMap.StructureMapGroupRuleTargetComponent;
import org.hl7.fhir.dstu3.model.StructureMap.StructureMapGroupRuleTargetParameterComponent;
import org.hl7.fhir.dstu3.model.StructureMap.StructureMapGroupTypeMode;
import org.hl7.fhir.dstu3.model.StructureMap.StructureMapInputMode;
import org.hl7.fhir.dstu3.model.StructureMap.StructureMapSourceListMode;
import org.hl7.fhir.dstu3.model.StructureMap.StructureMapTargetListMode;
import org.hl7.fhir.dstu3.model.StructureMap.StructureMapModelMode;
import org.hl7.fhir.dstu3.model.StructureMap.StructureMapStructureComponent;
import org.hl7.fhir.dstu3.model.StructureMap.StructureMapTransform;
import org.hl7.fhir.dstu3.model.Type;
import org.hl7.fhir.dstu3.model.TypeDetails;
import org.hl7.fhir.dstu3.model.TypeDetails.ProfiledType;
import org.hl7.fhir.dstu3.model.UriType;
import org.hl7.fhir.dstu3.model.ValueSet;
import org.hl7.fhir.dstu3.model.ValueSet.ValueSetExpansionContainsComponent;
import org.hl7.fhir.dstu3.terminologies.ValueSetExpander.ValueSetExpansionOutcome;
import org.hl7.fhir.dstu3.utils.FHIRLexer.FHIRLexerException;
import org.hl7.fhir.dstu3.utils.FHIRPathEngine.IEvaluationContext;
import org.hl7.fhir.exceptions.DefinitionException;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.exceptions.PathEngineException;
import org.hl7.fhir.utilities.CommaSeparatedStringBuilder;
import org.hl7.fhir.utilities.TextFile;
import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.utilities.xhtml.NodeType;
import org.hl7.fhir.utilities.xhtml.XhtmlNode;

/**
 * Services in this class:
 * 
 * string render(map) - take a structure and convert it to text
 * map parse(text) - take a text representation and parse it 
 * getTargetType(map) - return the definition for the type to create to hand in 
 * transform(appInfo, source, map, target) - transform from source to target following the map
 * analyse(appInfo, map) - generate profiles and other analysis artifacts for the targets of the transform
 * map generateMapFromMappings(StructureDefinition) - build a mapping from a structure definition with loigcal mappings
 *  
 * @author Grahame Grieve
 *
 */
public class StructureMapUtilities {

	public class ResolvedGroup {
    public StructureMapGroupComponent target;
    public StructureMap targetMap;
  }
  public static final String MAP_WHERE_CHECK = "map.where.check";
	public static final String MAP_WHERE_EXPRESSION = "map.where.expression";
	public static final String MAP_SEARCH_EXPRESSION = "map.search.expression";
	public static final String MAP_EXPRESSION = "map.transform.expression";
  private static final boolean RENDER_MULTIPLE_TARGETS_ONELINE = true;
  private static final String AUTO_VAR_NAME = "vvv";

	public interface ITransformerServices {
		//    public boolean validateByValueSet(Coding code, String valuesetId);
	  public void log(String message); // log internal progress
	  public Base createType(Object appInfo, String name) throws FHIRException;
    public Base createResource(Object appInfo, Base res); // an already created resource is provided; this is to identify/store it
		public Coding translate(Object appInfo, Coding source, String conceptMapUrl) throws FHIRException;
		//    public Coding translate(Coding code)
		//    ValueSet validation operation
		//    Translation operation
		//    Lookup another tree of data
		//    Create an instance tree
		//    Return the correct string format to refer to a tree (input or output)
    public Base resolveReference(Object appContext, String url);
    public List<Base> performSearch(Object appContext, String url);
	}

	private class FFHIRPathHostServices implements IEvaluationContext{

    public Base resolveConstant(Object appContext, String name) throws PathEngineException {
      Variables vars = (Variables) appContext;
      Base res = vars.get(VariableMode.INPUT, name);
      if (res == null)
        res = vars.get(VariableMode.OUTPUT, name);
      return res;
    }

    @Override
    public TypeDetails resolveConstantType(Object appContext, String name) throws PathEngineException {
      if (!(appContext instanceof VariablesForProfiling)) 
        throw new Error("Internal Logic Error (wrong type '"+appContext.getClass().getName()+"' in resolveConstantType)");
      VariablesForProfiling vars = (VariablesForProfiling) appContext;
      VariableForProfiling v = vars.get(null, name);
      if (v == null)
        throw new PathEngineException("Unknown variable '"+name+"' from variables "+vars.summary());
      return v.property.types;
    }

    @Override
    public boolean log(String argument, List<Base> focus) {
      throw new Error("Not Implemented Yet");
    }

    @Override
    public FunctionDetails resolveFunction(String functionName) {
      return null; // throw new Error("Not Implemented Yet");
    }

    @Override
    public TypeDetails checkFunction(Object appContext, String functionName, List<TypeDetails> parameters) throws PathEngineException {
      throw new Error("Not Implemented Yet");
    }

    @Override
    public List<Base> executeFunction(Object appContext, String functionName, List<List<Base>> parameters) {
      throw new Error("Not Implemented Yet");
    }

    @Override
    public Base resolveReference(Object appContext, String url) {
      if (services == null)
        return null;
      return services.resolveReference(appContext, url);
    }
	  
	}
	private IWorkerContext worker;
	private FHIRPathEngine fpe;
	private Map<String, StructureMap> library;
	private ITransformerServices services;
  private ProfileKnowledgeProvider pkp;
  private Map<String, Integer> ids = new HashMap<String, Integer>(); 

	public StructureMapUtilities(IWorkerContext worker, Map<String, StructureMap> library, ITransformerServices services, ProfileKnowledgeProvider pkp) {
		super();
		this.worker = worker;
		this.library = library;
		this.services = services;
		this.pkp = pkp;
		fpe = new FHIRPathEngine(worker);
		fpe.setHostServices(new FFHIRPathHostServices());
	}

	public StructureMapUtilities(IWorkerContext worker, Map<String, StructureMap> library, ITransformerServices services) {
		super();
		this.worker = worker;
		this.library = library;
		this.services = services;
		fpe = new FHIRPathEngine(worker);
    fpe.setHostServices(new FFHIRPathHostServices());
	}

  public StructureMapUtilities(IWorkerContext worker, Map<String, StructureMap> library) {
    super();
    this.worker = worker;
    this.library = library;
    fpe = new FHIRPathEngine(worker);
    fpe.setHostServices(new FFHIRPathHostServices());
  }

  public StructureMapUtilities(IWorkerContext worker) {
    super();
    this.worker = worker;
    fpe = new FHIRPathEngine(worker);
    fpe.setHostServices(new FFHIRPathHostServices());
  }

  public StructureMapUtilities(IWorkerContext worker, ITransformerServices services) {
    super();
    this.worker = worker;
    this.library = new HashMap<String, StructureMap>();
    for (org.hl7.fhir.dstu3.model.MetadataResource bc : worker.allConformanceResources()) {
      if (bc instanceof StructureMap)
        library.put(bc.getUrl(), (StructureMap) bc);
    }
    this.services = services;
    fpe = new FHIRPathEngine(worker);
    fpe.setHostServices(new FFHIRPathHostServices());
  }

	public static String render(StructureMap map) {
		StringBuilder b = new StringBuilder();
		b.append("map \"");
		b.append(map.getUrl());
		b.append("\" = \"");
		b.append(Utilities.escapeJava(map.getName()));
		b.append("\"\r\n\r\n");

		renderConceptMaps(b, map);
		renderUses(b, map);
		renderImports(b, map);
		for (StructureMapGroupComponent g : map.getGroup())
			renderGroup(b, g);
		return b.toString();
	}

	private static void renderConceptMaps(StringBuilder b, StructureMap map) {
    for (Resource r : map.getContained()) {
      if (r instanceof ConceptMap) {
        produceConceptMap(b, (ConceptMap) r);
      }
    }
  }

  private static void produceConceptMap(StringBuilder b, ConceptMap cm) {
    b.append("conceptmap \"");
    b.append(cm.getId());
    b.append("\" {\r\n");
    Map<String, String> prefixesSrc = new HashMap<String, String>();
    Map<String, String> prefixesTgt = new HashMap<String, String>();
    char prefix = 's';
    for (ConceptMapGroupComponent cg : cm.getGroup()) {
      if (!prefixesSrc.containsKey(cg.getSource())) {
        prefixesSrc.put(cg.getSource(), String.valueOf(prefix));
        b.append("  prefix ");
        b.append(prefix);
        b.append(" = \"");
        b.append(cg.getSource());
        b.append("\"\r\n");
        prefix++;
      }
      if (!prefixesTgt.containsKey(cg.getTarget())) {
        prefixesTgt.put(cg.getTarget(), String.valueOf(prefix));
        b.append("  prefix ");
        b.append(prefix);
        b.append(" = \"");
        b.append(cg.getTarget());
        b.append("\"\r\n");
        prefix++;
      }
    }
    b.append("\r\n");
    for (ConceptMapGroupComponent cg : cm.getGroup()) {
      for (SourceElementComponent ce : cg.getElement()) {
        b.append("  ");
        b.append(prefixesSrc.get(cg.getSource()));
        b.append(":");
        b.append(ce.getCode());
        b.append(" ");
        b.append(getChar(ce.getTargetFirstRep().getEquivalence()));
        b.append(" ");
        b.append(prefixesTgt.get(cg.getTarget()));
        b.append(":");
        b.append(ce.getTargetFirstRep().getCode());
        b.append("\r\n");
      }
    }
    b.append("}\r\n\r\n");
  }

  private static Object getChar(ConceptMapEquivalence equivalence) {
    switch (equivalence) {
    case RELATEDTO: return "-";
    case EQUAL: return "=";
    case EQUIVALENT: return "==";
    case DISJOINT: return "!=";
    case UNMATCHED: return "--";
    case WIDER: return "<=";
    case SUBSUMES: return "<-";
    case NARROWER: return ">=";
    case SPECIALIZES: return ">-";
    case INEXACT: return "~";
    default: return "??";
    }
  }

  private static void renderUses(StringBuilder b, StructureMap map) {
		for (StructureMapStructureComponent s : map.getStructure()) {
			b.append("uses \"");
			b.append(s.getUrl());
      b.append("\" ");
      if (s.hasAlias()) {
        b.append("alias ");
        b.append(s.getAlias());
        b.append(" ");
      }
      b.append("as ");
			b.append(s.getMode().toCode());
			b.append("\r\n");
			renderDoco(b, s.getDocumentation());
		}
		if (map.hasStructure())
			b.append("\r\n");
	}

	private static void renderImports(StringBuilder b, StructureMap map) {
		for (UriType s : map.getImport()) {
			b.append("imports \"");
			b.append(s.getValue());
			b.append("\"\r\n");
		}
		if (map.hasImport())
			b.append("\r\n");
	}

  public static String groupToString(StructureMapGroupComponent g) {
    StringBuilder b = new StringBuilder();
    renderGroup(b, g);
    return b.toString();
  }
  
  private static void renderGroup(StringBuilder b, StructureMapGroupComponent g) {
		b.append("group ");
    switch (g.getTypeMode()) {
    case TYPES: b.append("for types");
    case TYPEANDTYPES: b.append("for type+types ");
    default: // NONE, NULL
    }
      b.append("for types ");
		b.append(g.getName());
		if (g.hasExtends()) {
			b.append(" extends ");
			b.append(g.getExtends());
		}
		if (g.hasDocumentation()) 
			renderDoco(b, g.getDocumentation());
		b.append("\r\n");
		for (StructureMapGroupInputComponent gi : g.getInput()) {
			b.append("  input ");
			b.append(gi.getName());
			if (gi.hasType()) {
				b.append(" : ");
				b.append(gi.getType());
			}
			b.append(" as ");
			b.append(gi.getMode().toCode());
			b.append("\r\n");
		}
		if (g.hasInput())
			b.append("\r\n");
		for (StructureMapGroupRuleComponent r : g.getRule()) {
			renderRule(b, r, 2);
		}
		b.append("\r\nendgroup\r\n");
	}

  public static String ruleToString(StructureMapGroupRuleComponent r) {
    StringBuilder b = new StringBuilder();
    renderRule(b, r, 0);
    return b.toString();
  }
  
	private static void renderRule(StringBuilder b, StructureMapGroupRuleComponent r, int indent) {
		for (int i = 0; i < indent; i++)
			b.append(' ');
		b.append(r.getName());
		b.append(" : for ");
		boolean canBeAbbreviated = checkisSimple(r);
		
		boolean first = true;
		for (StructureMapGroupRuleSourceComponent rs : r.getSource()) {
			if (first)
				first = false;
			else
				b.append(", ");
			renderSource(b, rs, canBeAbbreviated);
		}
		if (r.getTarget().size() > 1) {
			b.append(" make ");
			first = true;
			for (StructureMapGroupRuleTargetComponent rt : r.getTarget()) {
				if (first)
					first = false;
				else
					b.append(", ");
				if (RENDER_MULTIPLE_TARGETS_ONELINE)
	        b.append(' ');
				else {
				b.append("\r\n");
				for (int i = 0; i < indent+4; i++)
					b.append(' ');
				}
				renderTarget(b, rt, false);
			}
		} else if (r.hasTarget()) { 
			b.append(" make ");
			renderTarget(b, r.getTarget().get(0), canBeAbbreviated);
		}
		if (!canBeAbbreviated) {
		  if (r.hasRule()) {
		    b.append(" then {\r\n");
		    renderDoco(b, r.getDocumentation());
		    for (StructureMapGroupRuleComponent ir : r.getRule()) {
		      renderRule(b, ir, indent+2);
		    }
		    for (int i = 0; i < indent; i++)
		      b.append(' ');
		    b.append("}\r\n");
		  } else {
		    if (r.hasDependent()) {
		      b.append(" then ");
		      first = true;
		      for (StructureMapGroupRuleDependentComponent rd : r.getDependent()) {
		        if (first)
		          first = false;
		        else
		          b.append(", ");
		        b.append(rd.getName());
		        b.append("(");
		        boolean ifirst = true;
		        for (StringType rdp : rd.getVariable()) {
		          if (ifirst)
		            ifirst = false;
		          else
		            b.append(", ");
		          b.append(rdp.asStringValue());
		        }
		        b.append(")");
		      }
		    }
		  }
		}
    renderDoco(b, r.getDocumentation());
    b.append("\r\n");
	}

  private static boolean checkisSimple(StructureMapGroupRuleComponent r) {
    return 
          (r.getSource().size() == 1 && r.getSourceFirstRep().hasElement() && r.getSourceFirstRep().hasVariable()) && 
          (r.getTarget().size() == 1 && r.getTargetFirstRep().hasVariable() && (r.getTargetFirstRep().getTransform() == null || r.getTargetFirstRep().getTransform() == StructureMapTransform.CREATE) && r.getTargetFirstRep().getParameter().size() == 0) &&
          (r.getDependent().size() == 0);
  }

  public static String sourceToString(StructureMapGroupRuleSourceComponent r) {
    StringBuilder b = new StringBuilder();
    renderSource(b, r, false);
    return b.toString();
  }
  
	private static void renderSource(StringBuilder b, StructureMapGroupRuleSourceComponent rs, boolean abbreviate) {
		b.append(rs.getContext());
		if (rs.getContext().equals("@search")) {
      b.append('(');
      b.append(rs.getElement());
      b.append(')');
		} else if (rs.hasElement()) {
			b.append('.');
			b.append(rs.getElement());
		}
		if (rs.hasType()) {
      b.append(" : ");
      b.append(rs.getType());
      if (rs.hasMin()) {
        b.append(" ");
        b.append(rs.getMin());
        b.append("..");
        b.append(rs.getMax());
      }
		}
		
		if (rs.hasListMode()) {
			b.append(" ");
				b.append(rs.getListMode().toCode());
		}
		if (rs.hasDefaultValue()) {
		  b.append(" default ");
		  assert rs.getDefaultValue() instanceof StringType;
		  b.append("\""+Utilities.escapeJson(((StringType) rs.getDefaultValue()).asStringValue())+"\"");
		}
		if (!abbreviate && rs.hasVariable()) {
			b.append(" as ");
			b.append(rs.getVariable());
		}
		if (rs.hasCondition())  {
			b.append(" where ");
			b.append(rs.getCondition());
		}
		if (rs.hasCheck())  {
			b.append(" check ");
			b.append(rs.getCheck());
		}
	}

  public static String targetToString(StructureMapGroupRuleTargetComponent rt) {
    StringBuilder b = new StringBuilder();
    renderTarget(b, rt, false);
    return b.toString();
  }
	
	private static void renderTarget(StringBuilder b, StructureMapGroupRuleTargetComponent rt, boolean abbreviate) {
	  if (rt.hasContext()) {
	    if (rt.getContextType() == StructureMapContextType.TYPE)
	      b.append("@");
		b.append(rt.getContext());
		if (rt.hasElement())  {
			b.append('.');
			b.append(rt.getElement());
		}
	  }
		if (!abbreviate && rt.hasTransform()) {
	    if (rt.hasContext()) 
			b.append(" = ");
			if (rt.getTransform() == StructureMapTransform.COPY && rt.getParameter().size() == 1) {
				renderTransformParam(b, rt.getParameter().get(0));
      } else if (rt.getTransform() == StructureMapTransform.EVALUATE && rt.getParameter().size() == 1) {
        b.append("(");
        b.append("\""+((StringType) rt.getParameter().get(0).getValue()).asStringValue()+"\"");
        b.append(")");
			} else if (rt.getTransform() == StructureMapTransform.EVALUATE && rt.getParameter().size() == 2) {
				b.append(rt.getTransform().toCode());
				b.append("(");
				b.append(((IdType) rt.getParameter().get(0).getValue()).asStringValue());
				b.append("\""+((StringType) rt.getParameter().get(1).getValue()).asStringValue()+"\"");
				b.append(")");
			} else {
				b.append(rt.getTransform().toCode());
				b.append("(");
				boolean first = true;
				for (StructureMapGroupRuleTargetParameterComponent rtp : rt.getParameter()) {
					if (first)
						first = false;
					else
						b.append(", ");
					renderTransformParam(b, rtp);
				}
				b.append(")");
			}
		}
		if (!abbreviate && rt.hasVariable()) {
			b.append(" as ");
			b.append(rt.getVariable());
		}
		for (Enumeration<StructureMapTargetListMode> lm : rt.getListMode()) {
			b.append(" ");
			b.append(lm.getValue().toCode());
			if (lm.getValue() == StructureMapTargetListMode.SHARE) {
				b.append(" ");
				b.append(rt.getListRuleId());
			}
		}
	}

  public static String paramToString(StructureMapGroupRuleTargetParameterComponent rtp) {
    StringBuilder b = new StringBuilder();
    renderTransformParam(b, rtp);
    return b.toString();
  }
  	
	private static void renderTransformParam(StringBuilder b, StructureMapGroupRuleTargetParameterComponent rtp) {
	  try {
		if (rtp.hasValueBooleanType())
			b.append(rtp.getValueBooleanType().asStringValue());
		else if (rtp.hasValueDecimalType())
			b.append(rtp.getValueDecimalType().asStringValue());
		else if (rtp.hasValueIdType())
			b.append(rtp.getValueIdType().asStringValue());
		else if (rtp.hasValueDecimalType())
			b.append(rtp.getValueDecimalType().asStringValue());
		else if (rtp.hasValueIntegerType())
			b.append(rtp.getValueIntegerType().asStringValue());
		else 
	      b.append("\""+Utilities.escapeJava(rtp.getValueStringType().asStringValue())+"\"");
	  } catch (FHIRException e) {
	    e.printStackTrace();
	    b.append("error!");
	  }
	}

	private static void renderDoco(StringBuilder b, String doco) {
		if (Utilities.noString(doco))
			return;
		b.append(" // ");
		b.append(doco.replace("\r\n", " ").replace("\r", " ").replace("\n", " "));
	}

	public StructureMap parse(String text) throws FHIRException {
		FHIRLexer lexer = new FHIRLexer(text);
		if (lexer.done())
			throw lexer.error("Map Input cannot be empty");
		lexer.skipComments();
		lexer.token("map");
		StructureMap result = new StructureMap();
		result.setUrl(lexer.readConstant("url"));
		lexer.token("=");
		result.setName(lexer.readConstant("name"));
		lexer.skipComments();

		while (lexer.hasToken("conceptmap"))
			parseConceptMap(result, lexer);

		while (lexer.hasToken("uses"))
			parseUses(result, lexer);
		while (lexer.hasToken("imports"))
			parseImports(result, lexer);

		parseGroup(result, lexer);

		while (!lexer.done()) {
			parseGroup(result, lexer);    
		}

		return result;
	}

	private void parseConceptMap(StructureMap result, FHIRLexer lexer) throws FHIRLexerException {
		lexer.token("conceptmap");
		ConceptMap map = new ConceptMap();
		String id = lexer.readConstant("map id");
		if (!id.startsWith("#"))
			lexer.error("Concept Map identifier must start with #");
		map.setId(id);
		map.setStatus(PublicationStatus.DRAFT); // todo: how to add this to the text format
		result.getContained().add(map);
		lexer.token("{");
		lexer.skipComments();
		//	  lexer.token("source");
		//	  map.setSource(new UriType(lexer.readConstant("source")));
		//	  lexer.token("target");
		//	  map.setSource(new UriType(lexer.readConstant("target")));
		Map<String, String> prefixes = new HashMap<String, String>();
		while (lexer.hasToken("prefix")) {
			lexer.token("prefix");
			String n = lexer.take();
			lexer.token("=");
			String v = lexer.readConstant("prefix url");
			prefixes.put(n, v);
		}
		while (!lexer.hasToken("}")) {
		  String srcs = readPrefix(prefixes, lexer);
			lexer.token(":");
      String sc = lexer.getCurrent().startsWith("\"") ? lexer.readConstant("code") : lexer.take();
		  ConceptMapEquivalence eq = readEquivalence(lexer);
		  String tgts = (eq != ConceptMapEquivalence.UNMATCHED) ? readPrefix(prefixes, lexer) : "";
		  ConceptMapGroupComponent g = getGroup(map, srcs, tgts);
			SourceElementComponent e = g.addElement();
			e.setCode(sc);
      if (e.getCode().startsWith("\""))
        e.setCode(lexer.processConstant(e.getCode()));
			TargetElementComponent tgt = e.addTarget();
			if (eq != ConceptMapEquivalence.EQUIVALENT)
			  tgt.setEquivalence(eq);
			if (tgt.getEquivalence() != ConceptMapEquivalence.UNMATCHED) {
				lexer.token(":");
				tgt.setCode(lexer.take());
				if (tgt.getCode().startsWith("\""))
				  tgt.setCode(lexer.processConstant(tgt.getCode()));
			}
			if (lexer.hasComment())
				tgt.setComment(lexer.take().substring(2).trim());
		}
		lexer.token("}");
	}


	private ConceptMapGroupComponent getGroup(ConceptMap map, String srcs, String tgts) {
	  for (ConceptMapGroupComponent grp : map.getGroup()) {
	    if (grp.getSource().equals(srcs)) 
	      if ((tgts == null && !grp.hasTarget()) || (tgts != null && tgts.equals(grp.getTarget())))
	      return grp;
	  }
	  ConceptMapGroupComponent grp = map.addGroup(); 
    grp.setSource(srcs);
    grp.setTarget(tgts);
    return grp;
  }


	private String readPrefix(Map<String, String> prefixes, FHIRLexer lexer) throws FHIRLexerException {
		String prefix = lexer.take();
		if (!prefixes.containsKey(prefix))
			throw lexer.error("Unknown prefix '"+prefix+"'");
		return prefixes.get(prefix);
	}


	private ConceptMapEquivalence readEquivalence(FHIRLexer lexer) throws FHIRLexerException {
		String token = lexer.take();
    if (token.equals("-"))
      return ConceptMapEquivalence.RELATEDTO;
    if (token.equals("="))
      return ConceptMapEquivalence.EQUAL;
		if (token.equals("=="))
			return ConceptMapEquivalence.EQUIVALENT;
		if (token.equals("!="))
			return ConceptMapEquivalence.DISJOINT;
		if (token.equals("--"))
			return ConceptMapEquivalence.UNMATCHED;
		if (token.equals("<="))
			return ConceptMapEquivalence.WIDER;
		if (token.equals("<-"))
			return ConceptMapEquivalence.SUBSUMES;
		if (token.equals(">="))
			return ConceptMapEquivalence.NARROWER;
		if (token.equals(">-"))
			return ConceptMapEquivalence.SPECIALIZES;
		if (token.equals("~"))
			return ConceptMapEquivalence.INEXACT;
		throw lexer.error("Unknown equivalence token '"+token+"'");
	}


	private void parseUses(StructureMap result, FHIRLexer lexer) throws FHIRException {
		lexer.token("uses");
		StructureMapStructureComponent st = result.addStructure();
		st.setUrl(lexer.readConstant("url"));
		if (lexer.hasToken("alias")) {
	    lexer.token("alias");
		  st.setAlias(lexer.take());
		}
		lexer.token("as");
		st.setMode(StructureMapModelMode.fromCode(lexer.take()));
		lexer.skipToken(";");
		if (lexer.hasComment()) {
			st.setDocumentation(lexer.take().substring(2).trim());
		}
		lexer.skipComments();
	}

	private void parseImports(StructureMap result, FHIRLexer lexer) throws FHIRException {
		lexer.token("imports");
		result.addImport(lexer.readConstant("url"));
		lexer.skipToken(";");
		if (lexer.hasComment()) {
			lexer.next();
		}
		lexer.skipComments();
	}

	private void parseGroup(StructureMap result, FHIRLexer lexer) throws FHIRException {
		lexer.token("group");
		StructureMapGroupComponent group = result.addGroup();
		if (lexer.hasToken("for")) {
		  lexer.token("for");
		  if ("type".equals(lexer.getCurrent())) {
        lexer.token("type");
        lexer.token("+");
        lexer.token("types");
        group.setTypeMode(StructureMapGroupTypeMode.TYPEANDTYPES);
		  } else {
		    lexer.token("types");
        group.setTypeMode(StructureMapGroupTypeMode.TYPES);
		  }
		} else
		  group.setTypeMode(StructureMapGroupTypeMode.NONE);
		group.setName(lexer.take());
		if (lexer.hasToken("extends")) {
			lexer.next();
			group.setExtends(lexer.take());
		}
		lexer.skipComments();
		while (lexer.hasToken("input")) 
			parseInput(group, lexer);
		while (!lexer.hasToken("endgroup")) {
			if (lexer.done())
				throw lexer.error("premature termination expecting 'endgroup'");
			parseRule(result, group.getRule(), lexer);
		}
		lexer.next();
		lexer.skipComments();
	}

	private void parseInput(StructureMapGroupComponent group, FHIRLexer lexer) throws FHIRException {
		lexer.token("input");
		StructureMapGroupInputComponent input = group.addInput();
		input.setName(lexer.take());
		if (lexer.hasToken(":")) {
			lexer.token(":");
			input.setType(lexer.take());
		}
		lexer.token("as");
		input.setMode(StructureMapInputMode.fromCode(lexer.take()));
		if (lexer.hasComment()) {
			input.setDocumentation(lexer.take().substring(2).trim());
		}
		lexer.skipToken(";");
		lexer.skipComments();
	}

	private void parseRule(StructureMap map, List<StructureMapGroupRuleComponent> list, FHIRLexer lexer) throws FHIRException {
		StructureMapGroupRuleComponent rule = new StructureMapGroupRuleComponent(); 
		list.add(rule);
		rule.setName(lexer.takeDottedToken());
		lexer.token(":");
		lexer.token("for");
		boolean done = false;
		while (!done) {
			parseSource(rule, lexer);
			done = !lexer.hasToken(",");
			if (!done)
				lexer.next();
		}
		if (lexer.hasToken("make")) {
			lexer.token("make");
			done = false;
			while (!done) {
				parseTarget(rule, lexer);
				done = !lexer.hasToken(",");
				if (!done)
					lexer.next();
			}
		}
		if (lexer.hasToken("then")) {
			lexer.token("then");
			if (lexer.hasToken("{")) {
				lexer.token("{");
				if (lexer.hasComment()) {
					rule.setDocumentation(lexer.take().substring(2).trim());
				}
				lexer.skipComments();
				while (!lexer.hasToken("}")) {
					if (lexer.done())
						throw lexer.error("premature termination expecting '}' in nested group");
					parseRule(map, rule.getRule(), lexer);
				}      
				lexer.token("}");
			} else {
				done = false;
				while (!done) {
					parseRuleReference(rule, lexer);
					done = !lexer.hasToken(",");
					if (!done)
						lexer.next();
				}
			}
		} else if (lexer.hasComment()) {
			rule.setDocumentation(lexer.take().substring(2).trim());
		}
		if (isSimpleSyntax(rule)) {
		  rule.getSourceFirstRep().setVariable(AUTO_VAR_NAME);
		  rule.getTargetFirstRep().setVariable(AUTO_VAR_NAME);
		  rule.getTargetFirstRep().setTransform(StructureMapTransform.CREATE); // with no parameter - e.g. imply what is to be created
		  // no dependencies - imply what is to be done based on types
		}
		lexer.skipComments();
	}

	private boolean isSimpleSyntax(StructureMapGroupRuleComponent rule) {
    return 
        (rule.getSource().size() == 1 && rule.getSourceFirstRep().hasContext() && rule.getSourceFirstRep().hasElement() && !rule.getSourceFirstRep().hasVariable()) &&
        (rule.getTarget().size() == 1 && rule.getTargetFirstRep().hasContext() && rule.getTargetFirstRep().hasElement() && !rule.getTargetFirstRep().hasVariable() && !rule.getTargetFirstRep().hasParameter()) &&
        (rule.getDependent().size() == 0 && rule.getRule().size() == 0);
  }

  private void parseRuleReference(StructureMapGroupRuleComponent rule, FHIRLexer lexer) throws FHIRLexerException {
		StructureMapGroupRuleDependentComponent ref = rule.addDependent();
		ref.setName(lexer.take());
		lexer.token("(");
		boolean done = false;
		while (!done) {
			ref.addVariable(lexer.take());
			done = !lexer.hasToken(",");
			if (!done)
				lexer.next();
		}
		lexer.token(")");
	}

	private void parseSource(StructureMapGroupRuleComponent rule, FHIRLexer lexer) throws FHIRException {
		StructureMapGroupRuleSourceComponent source = rule.addSource();
		source.setContext(lexer.take());
		if (source.getContext().equals("search") && lexer.hasToken("(")) {
	    source.setContext("@search");
      lexer.take();
      ExpressionNode node = fpe.parse(lexer);
      source.setUserData(MAP_SEARCH_EXPRESSION, node);
      source.setElement(node.toString());
      lexer.token(")");
		} else if (lexer.hasToken(".")) {
			lexer.token(".");
			source.setElement(lexer.take());
		}
		if (lexer.hasToken(":")) {
		  // type and cardinality
		  lexer.token(":");
		  source.setType(lexer.takeDottedToken());
		  if (!lexer.hasToken("as", "first", "last", "not_first", "not_last", "only_one", "default")) {
		    source.setMin(lexer.takeInt());
		    lexer.token("..");
		    source.setMax(lexer.take());
		  }
		}
		if (lexer.hasToken("default")) {
		  lexer.token("default");
		  source.setDefaultValue(new StringType(lexer.readConstant("default value")));
		}
		if (Utilities.existsInList(lexer.getCurrent(), "first", "last", "not_first", "not_last", "only_one"))
			source.setListMode(StructureMapSourceListMode.fromCode(lexer.take()));

		if (lexer.hasToken("as")) {
			lexer.take();
			source.setVariable(lexer.take());
		}
		if (lexer.hasToken("where")) {
			lexer.take();
			ExpressionNode node = fpe.parse(lexer);
			source.setUserData(MAP_WHERE_EXPRESSION, node);
			source.setCondition(node.toString());
		}
		if (lexer.hasToken("check")) {
			lexer.take();
			ExpressionNode node = fpe.parse(lexer);
			source.setUserData(MAP_WHERE_CHECK, node);
			source.setCheck(node.toString());
		}
	}

	private void parseTarget(StructureMapGroupRuleComponent rule, FHIRLexer lexer) throws FHIRException {
		StructureMapGroupRuleTargetComponent target = rule.addTarget();
		String start = lexer.take();
		if (lexer.hasToken(".")) {
	    target.setContext(start);
	    target.setContextType(StructureMapContextType.VARIABLE);
	    start = null;
			lexer.token(".");
			target.setElement(lexer.take());
		}
		String name;
		boolean isConstant = false;
		if (lexer.hasToken("=")) {
		  if (start != null)
	      target.setContext(start);
			lexer.token("=");
			isConstant = lexer.isConstant(true);
			name = lexer.take();
		} else 
		  name = start;
		
		if ("(".equals(name)) {
		  // inline fluentpath expression
      target.setTransform(StructureMapTransform.EVALUATE);
      ExpressionNode node = fpe.parse(lexer);
      target.setUserData(MAP_EXPRESSION, node);
      target.addParameter().setValue(new StringType(node.toString()));
      lexer.token(")");
		} else if (lexer.hasToken("(")) {
				target.setTransform(StructureMapTransform.fromCode(name));
				lexer.token("(");
				if (target.getTransform() == StructureMapTransform.EVALUATE) {
					parseParameter(target, lexer);
					lexer.token(",");
					ExpressionNode node = fpe.parse(lexer);
					target.setUserData(MAP_EXPRESSION, node);
					target.addParameter().setValue(new StringType(node.toString()));
				} else { 
					while (!lexer.hasToken(")")) {
						parseParameter(target, lexer);
						if (!lexer.hasToken(")"))
							lexer.token(",");
					}       
				}
				lexer.token(")");
		} else if (name != null) {
				target.setTransform(StructureMapTransform.COPY);
				if (!isConstant) {
					String id = name;
					while (lexer.hasToken(".")) {
						id = id + lexer.take() + lexer.take();
					}
					target.addParameter().setValue(new IdType(id));
				}
				else 
					target.addParameter().setValue(readConstant(name, lexer));
			}
		if (lexer.hasToken("as")) {
			lexer.take();
			target.setVariable(lexer.take());
		}
		while (Utilities.existsInList(lexer.getCurrent(), "first", "last", "share", "collate")) {
			if (lexer.getCurrent().equals("share")) {
				target.addListMode(StructureMapTargetListMode.SHARE);
				lexer.next();
				target.setListRuleId(lexer.take());
			} else if (lexer.getCurrent().equals("first")) 
				target.addListMode(StructureMapTargetListMode.FIRST);
			else
				target.addListMode(StructureMapTargetListMode.LAST);
			lexer.next();
		}
	}


	private void parseParameter(StructureMapGroupRuleTargetComponent target, FHIRLexer lexer) throws FHIRLexerException {
		if (!lexer.isConstant(true)) {
			target.addParameter().setValue(new IdType(lexer.take()));
		} else if (lexer.isStringConstant())
			target.addParameter().setValue(new StringType(lexer.readConstant("??")));
		else {
			target.addParameter().setValue(readConstant(lexer.take(), lexer));
		}
	}

	private Type readConstant(String s, FHIRLexer lexer) throws FHIRLexerException {
		if (Utilities.isInteger(s))
			return new IntegerType(s);
		else if (Utilities.isDecimal(s))
			return new DecimalType(s);
		else if (Utilities.existsInList(s, "true", "false"))
			return new BooleanType(s.equals("true"));
		else 
			return new StringType(lexer.processConstant(s));        
	}

	public StructureDefinition getTargetType(StructureMap map) throws FHIRException {
	  boolean found = false;
	  StructureDefinition res = null;
	  for (StructureMapStructureComponent uses : map.getStructure()) {
	    if (uses.getMode() == StructureMapModelMode.TARGET) {
	      if (found)
	        throw new FHIRException("Multiple targets found in map "+map.getUrl());
	      found = true;
	      res = worker.fetchResource(StructureDefinition.class, uses.getUrl());
	      if (res == null)
	        throw new FHIRException("Unable to find "+uses.getUrl()+" referenced from map "+map.getUrl());      
	    }
	  }
	  if (res == null)
      throw new FHIRException("No targets found in map "+map.getUrl());
	  return res;
	}

	public enum VariableMode {
		INPUT, OUTPUT
	}

	public class Variable {
		private VariableMode mode;
		private String name;
		private Base object;
		public Variable(VariableMode mode, String name, Base object) {
			super();
			this.mode = mode;
			this.name = name;
			this.object = object;
		}
		public VariableMode getMode() {
			return mode;
		}
		public String getName() {
			return name;
		}
		public Base getObject() {
			return object;
		}
    public String summary() {
      return name+": "+object.fhirType();
    }
	}

	public class Variables {
		private List<Variable> list = new ArrayList<Variable>();

		public void add(VariableMode mode, String name, Base object) {
			Variable vv = null;
			for (Variable v : list) 
				if ((v.mode == mode) && v.getName().equals(name))
					vv = v;
			if (vv != null)
				list.remove(vv);
			list.add(new Variable(mode, name, object));
		}

		public Variables copy() {
			Variables result = new Variables();
			result.list.addAll(list);
			return result;
		}

		public Base get(VariableMode mode, String name) {
			for (Variable v : list) 
				if ((v.mode == mode) && v.getName().equals(name))
					return v.getObject();
			return null;
		}

    public String summary() {
      CommaSeparatedStringBuilder s = new CommaSeparatedStringBuilder();
      CommaSeparatedStringBuilder t = new CommaSeparatedStringBuilder();
      for (Variable v : list)
        if (v.mode == VariableMode.INPUT)
          s.append(v.summary());
        else
          t.append(v.summary());
      return "source variables ["+s.toString()+"], target variables ["+t.toString()+"]";
    }
	}

	public class TransformContext {
		private Object appInfo;

		public TransformContext(Object appInfo) {
			super();
			this.appInfo = appInfo;
		}

		public Object getAppInfo() {
			return appInfo;
		}

	}

	private void log(String cnt) {
	  if (services != null)
	    services.log(cnt);
	}

	/**
	 * Given an item, return all the children that conform to the pattern described in name
	 * 
	 * Possible patterns:
	 *  - a simple name (which may be the base of a name with [] e.g. value[x])
	 *  - a name with a type replacement e.g. valueCodeableConcept
	 *  - * which means all children
	 *  - ** which means all descendents
	 *  
	 * @param item
	 * @param name
	 * @param result
	 * @throws FHIRException 
	 */
	protected void getChildrenByName(Base item, String name, List<Base> result) throws FHIRException {
		for (Base v : item.listChildrenByName(name, true))
			if (v != null)
				result.add(v);
	}

	public void transform(Object appInfo, Base source, StructureMap map, Base target) throws FHIRException {
		TransformContext context = new TransformContext(appInfo);
    log("Start Transform "+map.getUrl());
    StructureMapGroupComponent g = map.getGroup().get(0);

		Variables vars = new Variables();
		vars.add(VariableMode.INPUT, getInputName(g, StructureMapInputMode.SOURCE, "source"), source);
		vars.add(VariableMode.OUTPUT, getInputName(g, StructureMapInputMode.TARGET, "target"), target);

    executeGroup("", context, map, vars, g);
    if (target instanceof Element)
      ((Element) target).sort();
	}

	private String getInputName(StructureMapGroupComponent g, StructureMapInputMode mode, String def) throws DefinitionException {
	  String name = null;
    for (StructureMapGroupInputComponent inp : g.getInput()) {
      if (inp.getMode() == mode)
        if (name != null)
          throw new DefinitionException("This engine does not support multiple source inputs");
        else
          name = inp.getName();
    }
    return name == null ? def : name;
	}

	private void executeGroup(String indent, TransformContext context, StructureMap map, Variables vars, StructureMapGroupComponent group) throws FHIRException {
		log(indent+"Group : "+group.getName());
    // todo: check inputs
		if (group.hasExtends()) {
		  ResolvedGroup rg = resolveGroupReference(map, group, group.getExtends());
		  executeGroup(indent+" ", context, rg.targetMap, vars, rg.target); 
		}
		  
		for (StructureMapGroupRuleComponent r : group.getRule()) {
			executeRule(indent+"  ", context, map, vars, group, r);
		}
	}

	private void executeRule(String indent, TransformContext context, StructureMap map, Variables vars, StructureMapGroupComponent group, StructureMapGroupRuleComponent rule) throws FHIRException {
		log(indent+"rule : "+rule.getName());
		if (rule.getName().contains("CarePlan.participant-unlink"))
		  System.out.println("debug");
		Variables srcVars = vars.copy();
		if (rule.getSource().size() != 1)
			throw new FHIRException("Rule \""+rule.getName()+"\": not handled yet");
		List<Variables> source = processSource(rule.getName(), context, srcVars, rule.getSource().get(0));
		if (source != null) {
			for (Variables v : source) {
				for (StructureMapGroupRuleTargetComponent t : rule.getTarget()) {
					processTarget(rule.getName(), context, v, map, group, t, rule.getSource().size() == 1 ? rule.getSourceFirstRep().getVariable() : null);
				}
				if (rule.hasRule()) {
					for (StructureMapGroupRuleComponent childrule : rule.getRule()) {
						executeRule(indent +"  ", context, map, v, group, childrule);
					}
				} else if (rule.hasDependent()) {
					for (StructureMapGroupRuleDependentComponent dependent : rule.getDependent()) {
						executeDependency(indent+"  ", context, map, v, group, dependent);
					}
				} else if (rule.getSource().size() == 1 && rule.getSourceFirstRep().hasVariable() && rule.getTarget().size() == 1 && rule.getTargetFirstRep().hasVariable() && rule.getTargetFirstRep().getTransform() == StructureMapTransform.CREATE && !rule.getTargetFirstRep().hasParameter()) {
				  // simple inferred, map by type
				  Base src = v.get(VariableMode.INPUT, rule.getSourceFirstRep().getVariable());
				  Base tgt = v.get(VariableMode.OUTPUT, rule.getTargetFirstRep().getVariable());
				  String srcType = src.fhirType();
				  String tgtType = tgt.fhirType();
				  ResolvedGroup defGroup = resolveGroupByTypes(map, rule.getName(), group, srcType, tgtType);
			    Variables vdef = new Variables();
          vdef.add(VariableMode.INPUT, defGroup.target.getInput().get(0).getName(), src);
          vdef.add(VariableMode.OUTPUT, defGroup.target.getInput().get(1).getName(), tgt);
				  executeGroup(indent+"  ", context, defGroup.targetMap, vdef, defGroup.target);
				}
			}
		}
	}

	private void executeDependency(String indent, TransformContext context, StructureMap map, Variables vin, StructureMapGroupComponent group, StructureMapGroupRuleDependentComponent dependent) throws FHIRException {
	  ResolvedGroup rg = resolveGroupReference(map, group, dependent.getName());

		if (rg.target.getInput().size() != dependent.getVariable().size()) {
			throw new FHIRException("Rule '"+dependent.getName()+"' has "+Integer.toString(rg.target.getInput().size())+" but the invocation has "+Integer.toString(dependent.getVariable().size())+" variables");
		}
		Variables v = new Variables();
		for (int i = 0; i < rg.target.getInput().size(); i++) {
			StructureMapGroupInputComponent input = rg.target.getInput().get(i);
			StringType rdp = dependent.getVariable().get(i);
      String var = rdp.asStringValue();
			VariableMode mode = input.getMode() == StructureMapInputMode.SOURCE ? VariableMode.INPUT :   VariableMode.OUTPUT; 
			Base vv = vin.get(mode, var);
      if (vv == null && mode == VariableMode.INPUT) //* once source, always source. but target can be treated as source at user convenient
        vv = vin.get(VariableMode.OUTPUT, var);
			if (vv == null)
				throw new FHIRException("Rule '"+dependent.getName()+"' "+mode.toString()+" variable '"+input.getName()+"' named as '"+var+"' has no value");
			v.add(mode, input.getName(), vv);    	
		}
		executeGroup(indent+"  ", context, rg.targetMap, v, rg.target);
	}

  private String determineTypeFromSourceType(StructureMap map, StructureMapGroupComponent source, Base base, String[] types) throws FHIRException {
    String type = base.fhirType();
    String kn = "type^"+type;
    if (source.hasUserData(kn))
      return source.getUserString(kn);
    
    ResolvedGroup res = new ResolvedGroup();
    res.targetMap = null;
    res.target = null;
    for (StructureMapGroupComponent grp : map.getGroup()) {
      if (matchesByType(map, grp, type)) {
        if (res.targetMap == null) {
          res.targetMap = map;
          res.target = grp;
        } else 
          throw new FHIRException("Multiple possible matches looking for default rule for '"+type+"'");
      }
    }
    if (res.targetMap != null) {
      String result = getActualType(res.targetMap, res.target.getInput().get(1).getType());
      source.setUserData(kn, result);
      return result;
    }

    for (UriType imp : map.getImport()) {
      List<StructureMap> impMapList = findMatchingMaps(imp.getValue());
      if (impMapList.size() == 0)
        throw new FHIRException("Unable to find map(s) for "+imp.getValue());
      for (StructureMap impMap : impMapList) {
        if (!impMap.getUrl().equals(map.getUrl())) {
          for (StructureMapGroupComponent grp : impMap.getGroup()) {
            if (matchesByType(impMap, grp, type)) {
              if (res.targetMap == null) {
                res.targetMap = impMap;
                res.target = grp;
              } else 
                throw new FHIRException("Multiple possible matches for default rule for '"+type+"' in "+res.targetMap.getUrl()+" ("+res.target.getName()+") and "+impMap.getUrl()+" ("+grp.getName()+")");
            }
          }
        }
      }
    }
    if (res.target == null)
      throw new FHIRException("No matches found for default rule for '"+type+"' from "+map.getUrl());
    String result = getActualType(res.targetMap, res.target.getInput().get(1).getType()); // should be .getType, but R2...
    source.setUserData(kn, result);
    return result;
  }

  private List<StructureMap> findMatchingMaps(String value) {
    List<StructureMap> res = new ArrayList<StructureMap>();
    if (value.contains("*")) {
      for (StructureMap sm : library.values()) {
        if (urlMatches(value, sm.getUrl())) {
          res.add(sm); 
        }
      }
    } else {
      StructureMap sm = library.get(value);
      if (sm != null)
        res.add(sm); 
    }
    Set<String> check = new HashSet<String>();
    for (StructureMap sm : res) {
      if (check.contains(sm.getUrl()))
        throw new Error("duplicate");
      else
        check.add(sm.getUrl());
    }
    return res;
  }

  private boolean urlMatches(String mask, String url) {
    return url.length() > mask.length() && url.startsWith(mask.substring(0, mask.indexOf("*"))) && url.endsWith(mask.substring(mask.indexOf("*")+1)) ;
  }

  private ResolvedGroup resolveGroupByTypes(StructureMap map, String ruleid, StructureMapGroupComponent source, String srcType, String tgtType) throws FHIRException {
    String kn = "types^"+srcType+":"+tgtType;
    if (source.hasUserData(kn))
      return (ResolvedGroup) source.getUserData(kn);
    
    ResolvedGroup res = new ResolvedGroup();
    res.targetMap = null;
    res.target = null;
    for (StructureMapGroupComponent grp : map.getGroup()) {
      if (matchesByType(map, grp, srcType, tgtType)) {
        if (res.targetMap == null) {
          res.targetMap = map;
          res.target = grp;
        } else 
          throw new FHIRException("Multiple possible matches looking for rule for '"+srcType+"/"+tgtType+"', from rule '"+ruleid+"'");
      }
    }
    if (res.targetMap != null) {
      source.setUserData(kn, res);
      return res;
    }

    for (UriType imp : map.getImport()) {
      List<StructureMap> impMapList = findMatchingMaps(imp.getValue());
      if (impMapList.size() == 0)
        throw new FHIRException("Unable to find map(s) for "+imp.getValue());
      for (StructureMap impMap : impMapList) {
        if (!impMap.getUrl().equals(map.getUrl())) {
          for (StructureMapGroupComponent grp : impMap.getGroup()) {
            if (matchesByType(impMap, grp, srcType, tgtType)) {
              if (res.targetMap == null) {
                res.targetMap = impMap;
                res.target = grp;
              } else 
                throw new FHIRException("Multiple possible matches for rule for '"+srcType+"/"+tgtType+"' in "+res.targetMap.getUrl()+" and "+impMap.getUrl()+", from rule '"+ruleid+"'");
            }
          }
        }
      }
    }
    if (res.target == null)
      throw new FHIRException("No matches found for rule for '"+srcType+"/"+tgtType+"' from "+map.getUrl()+", from rule '"+ruleid+"'");
    source.setUserData(kn, res);
    return res;
  }


  private boolean matchesByType(StructureMap map, StructureMapGroupComponent grp, String type) throws FHIRException {
    if (grp.getTypeMode() != StructureMapGroupTypeMode.TYPEANDTYPES)
      return false;
    if (grp.getInput().size() != 2 || grp.getInput().get(0).getMode() != StructureMapInputMode.SOURCE || grp.getInput().get(1).getMode() != StructureMapInputMode.TARGET)
      return false;
    return matchesType(map, type, grp.getInput().get(0).getType());
  }

  private boolean matchesByType(StructureMap map, StructureMapGroupComponent grp, String srcType, String tgtType) throws FHIRException {
    if (grp.getTypeMode() == StructureMapGroupTypeMode.NONE)
      return false;
    if (grp.getInput().size() != 2 || grp.getInput().get(0).getMode() != StructureMapInputMode.SOURCE || grp.getInput().get(1).getMode() != StructureMapInputMode.TARGET)
      return false;
    if (!grp.getInput().get(0).hasType() || !grp.getInput().get(1).hasType())
      return false;
    return matchesType(map, srcType, grp.getInput().get(0).getType()) && matchesType(map, tgtType, grp.getInput().get(1).getType());
  }

  private boolean matchesType(StructureMap map, String actualType, String statedType) throws FHIRException {
    // check the aliases
    for (StructureMapStructureComponent imp : map.getStructure()) {
      if (imp.hasAlias() && statedType.equals(imp.getAlias())) {
        StructureDefinition sd = worker.fetchResource(StructureDefinition.class, imp.getUrl());
        if (sd != null)
          statedType = sd.getType();
        break;
      }
    }
    
    return actualType.equals(statedType);
  }

  private String getActualType(StructureMap map, String statedType) throws FHIRException {
    // check the aliases
    for (StructureMapStructureComponent imp : map.getStructure()) {
      if (imp.hasAlias() && statedType.equals(imp.getAlias())) {
        StructureDefinition sd = worker.fetchResource(StructureDefinition.class, imp.getUrl());
        if (sd == null)
          throw new FHIRException("Unable to resolve structure "+imp.getUrl());
        return sd.getId(); // should be sd.getType(), but R2...
      }
    }
    return statedType;
  }


  private ResolvedGroup resolveGroupReference(StructureMap map, StructureMapGroupComponent source, String name) throws FHIRException {
    String kn = "ref^"+name;
    if (source.hasUserData(kn))
      return (ResolvedGroup) source.getUserData(kn);
    
	  ResolvedGroup res = new ResolvedGroup();
    res.targetMap = null;
    res.target = null;
    for (StructureMapGroupComponent grp : map.getGroup()) {
      if (grp.getName().equals(name)) {
        if (res.targetMap == null) {
          res.targetMap = map;
          res.target = grp;
        } else 
          throw new FHIRException("Multiple possible matches for rule '"+name+"'");
      }
    }
    if (res.targetMap != null) {
      source.setUserData(kn, res);
      return res;
    }

    for (UriType imp : map.getImport()) {
      List<StructureMap> impMapList = findMatchingMaps(imp.getValue());
      if (impMapList.size() == 0)
        throw new FHIRException("Unable to find map(s) for "+imp.getValue());
      for (StructureMap impMap : impMapList) {
        if (!impMap.getUrl().equals(map.getUrl())) {
          for (StructureMapGroupComponent grp : impMap.getGroup()) {
            if (grp.getName().equals(name)) {
              if (res.targetMap == null) {
                res.targetMap = impMap;
                res.target = grp;
              } else 
                throw new FHIRException("Multiple possible matches for rule '"+name+"' in "+res.targetMap.getUrl()+" and "+impMap.getUrl());
            }
          }
        }
      }
    }
    if (res.target == null)
      throw new FHIRException("No matches found for rule '"+name+"'. Reference found in "+map.getUrl());
    source.setUserData(kn, res);
    return res;
  }

  private List<Variables> processSource(String ruleId, TransformContext context, Variables vars, StructureMapGroupRuleSourceComponent src) throws FHIRException {
    List<Base> items;
    if (src.getContext().equals("@search")) {
      ExpressionNode expr = (ExpressionNode) src.getUserData(MAP_SEARCH_EXPRESSION);
      if (expr == null) {
        expr = fpe.parse(src.getElement());
        src.setUserData(MAP_SEARCH_EXPRESSION, expr);
      }
      String search = fpe.evaluateToString(vars, null, new StringType(), expr); // string is a holder of nothing to ensure that variables are processed correctly 
      items = services.performSearch(context.appInfo, search);
    } else {
      items = new ArrayList<Base>();
      Base b = vars.get(VariableMode.INPUT, src.getContext());
      if (b == null)
        throw new FHIRException("Unknown input variable "+src.getContext());

      if (!src.hasElement()) 
        items.add(b);
      else { 
        getChildrenByName(b, src.getElement(), items);
        if (items.size() == 0 && src.hasDefaultValue())
          items.add(src.getDefaultValue());
      }
    }
    
		if (src.hasType()) {
	    List<Base> remove = new ArrayList<Base>();
	    for (Base item : items) {
	      if (item != null && !isType(item, src.getType())) {
	        remove.add(item);
	      }
	    }
	    items.removeAll(remove);
		}

    if (src.hasCondition()) {
      ExpressionNode expr = (ExpressionNode) src.getUserData(MAP_WHERE_EXPRESSION);
      if (expr == null) {
        expr = fpe.parse(src.getCondition());
        //        fpe.check(context.appInfo, ??, ??, expr)
        src.setUserData(MAP_WHERE_EXPRESSION, expr);
      }
      List<Base> remove = new ArrayList<Base>();
      for (Base item : items) {
        if (!fpe.evaluateToBoolean(vars, null, item, expr))
          remove.add(item);
      }
      items.removeAll(remove);
    }

    if (src.hasCheck()) {
      ExpressionNode expr = (ExpressionNode) src.getUserData(MAP_WHERE_CHECK);
      if (expr == null) {
        expr = fpe.parse(src.getCheck());
        //        fpe.check(context.appInfo, ??, ??, expr)
        src.setUserData(MAP_WHERE_CHECK, expr);
      }
      List<Base> remove = new ArrayList<Base>();
      for (Base item : items) {
        if (!fpe.evaluateToBoolean(vars, null, item, expr))
          throw new FHIRException("Rule \""+ruleId+"\": Check condition failed");
      }
    } 

		
		if (src.hasListMode() && !items.isEmpty()) {
		  switch (src.getListMode()) {
		  case FIRST:
		    Base bt = items.get(0);
        items.clear();
        items.add(bt);
		    break;
		  case NOTFIRST: 
        if (items.size() > 0)
          items.remove(0);
        break;
		  case LAST:
        bt = items.get(items.size()-1);
        items.clear();
        items.add(bt);
        break;
		  case NOTLAST: 
        if (items.size() > 0)
          items.remove(items.size()-1);
        break;
		  case ONLYONE:
		    if (items.size() > 1)
          throw new FHIRException("Rule \""+ruleId+"\": Check condition failed: the collection has more than one item");
        break;
      case NULL:
		  }
		}
		List<Variables> result = new ArrayList<Variables>();
		for (Base r : items) {
			Variables v = vars.copy();
			if (src.hasVariable())
				v.add(VariableMode.INPUT, src.getVariable(), r);
			result.add(v); 
		}
		return result;
	}


	private boolean isType(Base item, String type) {
    if (type.equals(item.fhirType()))
      return true;
    return false;
  }

  private void processTarget(String ruleId, TransformContext context, Variables vars, StructureMap map, StructureMapGroupComponent group, StructureMapGroupRuleTargetComponent tgt, String srcVar) throws FHIRException {
	  Base dest = null;
	  if (tgt.hasContext()) {
  		dest = vars.get(VariableMode.OUTPUT, tgt.getContext());
		if (dest == null)
			throw new FHIRException("Rule \""+ruleId+"\": target context not known: "+tgt.getContext());
		if (!tgt.hasElement())
			throw new FHIRException("Rule \""+ruleId+"\": Not supported yet");
	  }
		Base v = null;
		if (tgt.hasTransform()) {
			v = runTransform(ruleId, context, map, group, tgt, vars, dest, tgt.getElement(), srcVar);
			if (v != null && dest != null)
				v = dest.setProperty(tgt.getElement().hashCode(), tgt.getElement(), v); // reset v because some implementations may have to rewrite v when setting the value
		} else if (dest != null) 
			v = dest.makeProperty(tgt.getElement().hashCode(), tgt.getElement());
		if (tgt.hasVariable() && v != null)
			vars.add(VariableMode.OUTPUT, tgt.getVariable(), v);
	}

	private Base runTransform(String ruleId, TransformContext context, StructureMap map, StructureMapGroupComponent group, StructureMapGroupRuleTargetComponent tgt, Variables vars, Base dest, String element, String srcVar) throws FHIRException {
	  try {
	    switch (tgt.getTransform()) {
	    case CREATE :
	      String tn;
	      if (tgt.getParameter().isEmpty()) {
	        // we have to work out the type. First, we see if there is a single type for the target. If there is, we use that
	        String[] types = dest.getTypesForProperty(element.hashCode(), element);
	        if (types.length == 1 && !"*".equals(types[0]) && !types[0].equals("Resource"))
	          tn = types[0];
	        else if (srcVar != null) {
	          tn = determineTypeFromSourceType(map, group, vars.get(VariableMode.INPUT, srcVar), types);
	        } else
	          throw new Error("Cannot determine type implicitly because there is no single input variable");
	      } else
	        tn = getParamStringNoNull(vars, tgt.getParameter().get(0), tgt.toString());
	      Base res = services != null ? services.createType(context.getAppInfo(), tn) : ResourceFactory.createResourceOrType(tn);
	      if (res.isResource() && !res.fhirType().equals("Parameters")) {
//	        res.setIdBase(tgt.getParameter().size() > 1 ? getParamString(vars, tgt.getParameter().get(0)) : UUID.randomUUID().toString().toLowerCase());
	        if (services != null) 
	          res = services.createResource(context.getAppInfo(), res);
	      }
	      if (tgt.hasUserData("profile"))
	        res.setUserData("profile", tgt.getUserData("profile"));
	      return res;
	    case COPY : 
	      return getParam(vars, tgt.getParameter().get(0));
	    case EVALUATE :
	      ExpressionNode expr = (ExpressionNode) tgt.getUserData(MAP_EXPRESSION);
	      if (expr == null) {
	        expr = fpe.parse(getParamStringNoNull(vars, tgt.getParameter().get(1), tgt.toString()));
	        tgt.setUserData(MAP_WHERE_EXPRESSION, expr);
	      }
	      List<Base> v = fpe.evaluate(vars, null, tgt.getParameter().size() == 2 ? getParam(vars, tgt.getParameter().get(0)) : new BooleanType(false), expr);
	      if (v.size() == 0)
	        return null;
	      else if (v.size() != 1)
	        throw new FHIRException("Rule \""+ruleId+"\": Evaluation of "+expr.toString()+" returned "+Integer.toString(v.size())+" objects");
	      else
	        return v.get(0);

	    case TRUNCATE : 
	      String src = getParamString(vars, tgt.getParameter().get(0));
	      String len = getParamStringNoNull(vars, tgt.getParameter().get(1), tgt.toString());
	      if (Utilities.isInteger(len)) {
	        int l = Integer.parseInt(len);
	        if (src.length() > l)
	          src = src.substring(0, l);
	      }
	      return new StringType(src);
	    case ESCAPE : 
	      throw new Error("Rule \""+ruleId+"\": Transform "+tgt.getTransform().toCode()+" not supported yet");
	    case CAST :
	      throw new Error("Rule \""+ruleId+"\": Transform "+tgt.getTransform().toCode()+" not supported yet");
	    case APPEND : 
	      throw new Error("Rule \""+ruleId+"\": Transform "+tgt.getTransform().toCode()+" not supported yet");
	    case TRANSLATE : 
	      return translate(context, map, vars, tgt.getParameter());
	    case REFERENCE :
	      Base b = getParam(vars, tgt.getParameter().get(0));
	      if (b == null)
	        throw new FHIRException("Rule \""+ruleId+"\": Unable to find parameter "+((IdType) tgt.getParameter().get(0).getValue()).asStringValue());
	      if (!b.isResource())
	        throw new FHIRException("Rule \""+ruleId+"\": Transform engine cannot point at an element of type "+b.fhirType());
	      else {
	        String id = b.getIdBase();
	        if (id == null) {
	          id = UUID.randomUUID().toString().toLowerCase();
	          b.setIdBase(id);
	        }
	        return new Reference().setReference(b.fhirType()+"/"+id);
	      }
	    case DATEOP :
	      throw new Error("Rule \""+ruleId+"\": Transform "+tgt.getTransform().toCode()+" not supported yet");
	    case UUID :
	      return new IdType(UUID.randomUUID().toString());
	    case POINTER :
	      b = getParam(vars, tgt.getParameter().get(0));
	      if (b instanceof Resource)
	        return new UriType("urn:uuid:"+((Resource) b).getId());
	      else
	        throw new FHIRException("Rule \""+ruleId+"\": Transform engine cannot point at an element of type "+b.fhirType());
	    case CC:
	      CodeableConcept cc = new CodeableConcept();
	      cc.addCoding(buildCoding(getParamStringNoNull(vars, tgt.getParameter().get(0), tgt.toString()), getParamStringNoNull(vars, tgt.getParameter().get(1), tgt.toString())));
	      return cc;
	    case C: 
	      Coding c = buildCoding(getParamStringNoNull(vars, tgt.getParameter().get(0), tgt.toString()), getParamStringNoNull(vars, tgt.getParameter().get(1), tgt.toString()));
	      return c;
	    default:
	      throw new Error("Rule \""+ruleId+"\": Transform Unknown: "+tgt.getTransform().toCode());
	    }
	  } catch (Exception e) {
	    throw new FHIRException("Exception executing transform "+tgt.toString()+" on Rule \""+ruleId+"\": "+e.getMessage(), e);
	  }
	}


  private Coding buildCoding(String uri, String code) throws FHIRException {
	  // if we can get this as a valueSet, we will
	  String system = null;
	  String display = null;
	  ValueSet vs = Utilities.noString(uri) ? null : worker.fetchResourceWithException(ValueSet.class, uri);
	  if (vs != null) {
	    ValueSetExpansionOutcome vse = worker.expandVS(vs, true, false);
	    if (vse.getError() != null)
	      throw new FHIRException(vse.getError());
	    CommaSeparatedStringBuilder b = new CommaSeparatedStringBuilder();
	    for (ValueSetExpansionContainsComponent t : vse.getValueset().getExpansion().getContains()) {
	      if (t.hasCode())
	        b.append(t.getCode());
	      if (code.equals(t.getCode()) && t.hasSystem()) {
	        system = t.getSystem();
          display = t.getDisplay();
	        break;
	      }
        if (code.equalsIgnoreCase(t.getDisplay()) && t.hasSystem()) {
          system = t.getSystem();
          display = t.getDisplay();
          break;
        }
	    }
	    if (system == null)
	      throw new FHIRException("The code '"+code+"' is not in the value set '"+uri+"' (valid codes: "+b.toString()+"; also checked displays)");
	  } else
	    system = uri;
	  ValidationResult vr = worker.validateCode(system, code, null);
	  if (vr != null && vr.getDisplay() != null)
	    display = vr.getDisplay();
   return new Coding().setSystem(system).setCode(code).setDisplay(display);
  }


  private String getParamStringNoNull(Variables vars, StructureMapGroupRuleTargetParameterComponent parameter, String message) throws FHIRException {
    Base b = getParam(vars, parameter);
    if (b == null)
      throw new FHIRException("Unable to find a value for "+parameter.toString()+". Context: "+message);
    if (!b.hasPrimitiveValue())
      throw new FHIRException("Found a value for "+parameter.toString()+", but it has a type of "+b.fhirType()+" and cannot be treated as a string. Context: "+message);
    return b.primitiveValue();
  }

  private String getParamString(Variables vars, StructureMapGroupRuleTargetParameterComponent parameter) throws DefinitionException {
		Base b = getParam(vars, parameter);
		if (b == null || !b.hasPrimitiveValue())
			return null;
		return b.primitiveValue();
	}


	private Base getParam(Variables vars, StructureMapGroupRuleTargetParameterComponent parameter) throws DefinitionException {
		Type p = parameter.getValue();
		if (!(p instanceof IdType))
			return p;
		else { 
			String n = ((IdType) p).asStringValue();
      Base b = vars.get(VariableMode.INPUT, n);
			if (b == null)
				b = vars.get(VariableMode.OUTPUT, n);
			if (b == null)
        throw new DefinitionException("Variable "+n+" not found ("+vars.summary()+")");
			return b;
		}
	}


	private Base translate(TransformContext context, StructureMap map, Variables vars, List<StructureMapGroupRuleTargetParameterComponent> parameter) throws FHIRException {
		Base src = getParam(vars, parameter.get(0));
		String id = getParamString(vars, parameter.get(1));
		String fld = parameter.size() > 2 ? getParamString(vars, parameter.get(2)) : null;
		return translate(context, map, src, id, fld);
	}

	private class SourceElementComponentWrapper {
	  private ConceptMapGroupComponent group;
    private SourceElementComponent comp;
    public SourceElementComponentWrapper(ConceptMapGroupComponent group, SourceElementComponent comp) {
      super();
      this.group = group;
      this.comp = comp;
    }
	}
	public Base translate(TransformContext context, StructureMap map, Base source, String conceptMapUrl, String fieldToReturn) throws FHIRException {
		Coding src = new Coding();
		if (source.isPrimitive()) {
			src.setCode(source.primitiveValue());
		} else if ("Coding".equals(source.fhirType())) {
			Base[] b = source.getProperty("system".hashCode(), "system", true);
			if (b.length == 1)
				src.setSystem(b[0].primitiveValue());
			b = source.getProperty("code".hashCode(), "code", true);
			if (b.length == 1)
				src.setCode(b[0].primitiveValue());
		} else if ("CE".equals(source.fhirType())) {
			Base[] b = source.getProperty("codeSystem".hashCode(), "codeSystem", true);
			if (b.length == 1)
				src.setSystem(b[0].primitiveValue());
			b = source.getProperty("code".hashCode(), "code", true);
			if (b.length == 1)
				src.setCode(b[0].primitiveValue());
		} else
			throw new FHIRException("Unable to translate source "+source.fhirType());

		String su = conceptMapUrl;
		if (conceptMapUrl.equals("http://hl7.org/fhir/ConceptMap/special-oid2uri")) {
			String uri = worker.oid2Uri(src.getCode());
			if (uri == null)
				uri = "urn:oid:"+src.getCode();
			if ("uri".equals(fieldToReturn))
				return new UriType(uri);
			else
				throw new FHIRException("Error in return code");
		} else {
			ConceptMap cmap = null;
			if (conceptMapUrl.startsWith("#")) {
				for (Resource r : map.getContained()) {
					if (r instanceof ConceptMap && ((ConceptMap) r).getId().equals(conceptMapUrl.substring(1))) {
						cmap = (ConceptMap) r;
						su = map.getUrl()+conceptMapUrl;
					}
				}
				if (cmap == null)
		      throw new FHIRException("Unable to translate - cannot find map "+conceptMapUrl);
			} else
				cmap = worker.fetchResource(ConceptMap.class, conceptMapUrl);
			Coding outcome = null;
			boolean done = false;
			String message = null;
			if (cmap == null) {
				if (services == null) 
					message = "No map found for "+conceptMapUrl;
				else {
					outcome = services.translate(context.appInfo, src, conceptMapUrl);
					done = true;
				}
			} else {
			  List<SourceElementComponentWrapper> list = new ArrayList<SourceElementComponentWrapper>();
			  for (ConceptMapGroupComponent g : cmap.getGroup()) {
			    for (SourceElementComponent e : g.getElement()) {
						if (!src.hasSystem() && src.getCode().equals(e.getCode())) 
			        list.add(new SourceElementComponentWrapper(g, e));
			      else if (src.hasSystem() && src.getSystem().equals(g.getSource()) && src.getCode().equals(e.getCode()))
			        list.add(new SourceElementComponentWrapper(g, e));
			    }
				}
				if (list.size() == 0)
					done = true;
				else if (list.get(0).comp.getTarget().size() == 0)
					message = "Concept map "+su+" found no translation for "+src.getCode();
				else {
					for (TargetElementComponent tgt : list.get(0).comp.getTarget()) {
						if (tgt.getEquivalence() == null || EnumSet.of( ConceptMapEquivalence.EQUAL , ConceptMapEquivalence.RELATEDTO , ConceptMapEquivalence.EQUIVALENT, ConceptMapEquivalence.WIDER).contains(tgt.getEquivalence())) {
							if (done) {
								message = "Concept map "+su+" found multiple matches for "+src.getCode();
								done = false;
							} else {
								done = true;
								outcome = new Coding().setCode(tgt.getCode()).setSystem(list.get(0).group.getTarget());
							}
						} else if (tgt.getEquivalence() == ConceptMapEquivalence.UNMATCHED) {
							done = true;
						}
					}
					if (!done)
						message = "Concept map "+su+" found no usable translation for "+src.getCode();
				}
			}
			if (!done) 
				throw new FHIRException(message);
			if (outcome == null)
				return null;
			if ("code".equals(fieldToReturn))
				return new CodeType(outcome.getCode());
			else
				return outcome; 
		}
	}


	public Map<String, StructureMap> getLibrary() {
	  return library;
	}

	public class PropertyWithType {
    private String path;
    private Property baseProperty;
    private Property profileProperty;
	  private TypeDetails types;
    public PropertyWithType(String path, Property baseProperty, Property profileProperty, TypeDetails types) {
      super();
      this.baseProperty = baseProperty;
      this.profileProperty = profileProperty;
      this.path = path;
      this.types = types;
    }

    public TypeDetails getTypes() {
      return types;
    }
    public String getPath() {
      return path;
    }

    public Property getBaseProperty() {
      return baseProperty;
    }

    public void setBaseProperty(Property baseProperty) {
      this.baseProperty = baseProperty;
    }

    public Property getProfileProperty() {
      return profileProperty;
    }

    public void setProfileProperty(Property profileProperty) {
      this.profileProperty = profileProperty;
    }

    public String summary() {
      return path;
    }
    
	}
	
	public class VariableForProfiling {
	    private VariableMode mode;
	    private String name;
	    private PropertyWithType property;
	    
	    public VariableForProfiling(VariableMode mode, String name, PropertyWithType property) {
	      super();
	      this.mode = mode;
	      this.name = name;
	      this.property = property;
	    }
	    public VariableMode getMode() {
	      return mode;
	    }
	    public String getName() {
	      return name;
	    }
      public PropertyWithType getProperty() {
        return property;
      }
      public String summary() {
        return name+": "+property.summary();
      }      
	  }

  public class VariablesForProfiling {
    private List<VariableForProfiling> list = new ArrayList<VariableForProfiling>();
    private boolean optional;
    private boolean repeating;

    public VariablesForProfiling(boolean optional, boolean repeating) {
      this.optional = optional;
      this.repeating = repeating;
    }

    public void add(VariableMode mode, String name, String path, Property property, TypeDetails types) {
      add(mode, name, new PropertyWithType(path, property, null, types));
    }
    
    public void add(VariableMode mode, String name, String path, Property baseProperty, Property profileProperty, TypeDetails types) {
      add(mode, name, new PropertyWithType(path, baseProperty, profileProperty, types));
    }
    
    public void add(VariableMode mode, String name, PropertyWithType property) {
      VariableForProfiling vv = null;
      for (VariableForProfiling v : list) 
        if ((v.mode == mode) && v.getName().equals(name))
          vv = v;
      if (vv != null)
        list.remove(vv);
      list.add(new VariableForProfiling(mode, name, property));
    }

    public VariablesForProfiling copy(boolean optional, boolean repeating) {
      VariablesForProfiling result = new VariablesForProfiling(optional, repeating);
      result.list.addAll(list);
      return result;
    }

    public VariablesForProfiling copy() {
      VariablesForProfiling result = new VariablesForProfiling(optional, repeating);
      result.list.addAll(list);
      return result;
    }

    public VariableForProfiling get(VariableMode mode, String name) {
      if (mode == null) {
        for (VariableForProfiling v : list) 
          if ((v.mode == VariableMode.OUTPUT) && v.getName().equals(name))
            return v;
        for (VariableForProfiling v : list) 
          if ((v.mode == VariableMode.INPUT) && v.getName().equals(name))
            return v;
      }
      for (VariableForProfiling v : list) 
        if ((v.mode == mode) && v.getName().equals(name))
          return v;
      return null;
    }

    public String summary() {
      CommaSeparatedStringBuilder s = new CommaSeparatedStringBuilder();
      CommaSeparatedStringBuilder t = new CommaSeparatedStringBuilder();
      for (VariableForProfiling v : list)
        if (v.mode == VariableMode.INPUT)
          s.append(v.summary());
        else
          t.append(v.summary());
      return "source variables ["+s.toString()+"], target variables ["+t.toString()+"]";
    }
  }

  public class StructureMapAnalysis {
    private List<StructureDefinition> profiles = new ArrayList<StructureDefinition>();
    private XhtmlNode summary;
    public List<StructureDefinition> getProfiles() {
      return profiles;
    }
    public XhtmlNode getSummary() {
      return summary;
    }
    
  }

	/**
	 * Given a structure map, return a set of analyses on it. 
	 * 
	 * Returned:
	 *   - a list or profiles for what it will create. First profile is the target
	 *   - a table with a summary (in xhtml) for easy human undertanding of the mapping
	 *   
	 * 
	 * @param appInfo
	 * @param map
	 * @return
	 * @throws Exception
	 */
  public StructureMapAnalysis analyse(Object appInfo, StructureMap map) throws Exception {
    ids.clear();
    StructureMapAnalysis result = new StructureMapAnalysis(); 
    TransformContext context = new TransformContext(appInfo);
    VariablesForProfiling vars = new VariablesForProfiling(false, false);
    StructureMapGroupComponent start = map.getGroup().get(0);
    for (StructureMapGroupInputComponent t : start.getInput()) {
      PropertyWithType ti = resolveType(map, t.getType(), t.getMode());
      if (t.getMode() == StructureMapInputMode.SOURCE)
       vars.add(VariableMode.INPUT, t.getName(), ti);
      else 
        vars.add(VariableMode.OUTPUT, t.getName(), createProfile(map, result.profiles, ti, start.getName(), start));
    }

    result.summary = new XhtmlNode(NodeType.Element, "table").setAttribute("class", "grid");
    XhtmlNode tr = result.summary.addTag("tr");
    tr.addTag("td").addTag("b").addText("Source");
    tr.addTag("td").addTag("b").addText("Target");
    
    log("Start Profiling Transform "+map.getUrl());
    analyseGroup("", context, map, vars, start, result);
    ProfileUtilities pu = new ProfileUtilities(worker, null, pkp);
    for (StructureDefinition sd : result.getProfiles())
      pu.cleanUpDifferential(sd);
    return result;
  }


  private void analyseGroup(String indent, TransformContext context, StructureMap map, VariablesForProfiling vars, StructureMapGroupComponent group, StructureMapAnalysis result) throws Exception {
    log(indent+"Analyse Group : "+group.getName());
    // todo: extends
    // todo: check inputs
    XhtmlNode tr = result.summary.addTag("tr").setAttribute("class", "diff-title");
    XhtmlNode xs = tr.addTag("td");
    XhtmlNode xt = tr.addTag("td");
    for (StructureMapGroupInputComponent inp : group.getInput()) {
      if (inp.getMode() == StructureMapInputMode.SOURCE) 
        noteInput(vars, inp, VariableMode.INPUT, xs);
      if (inp.getMode() == StructureMapInputMode.TARGET) 
        noteInput(vars, inp, VariableMode.OUTPUT, xt);
    }
    for (StructureMapGroupRuleComponent r : group.getRule()) {
      analyseRule(indent+"  ", context, map, vars, group, r, result);
    }    
  }


  private void noteInput(VariablesForProfiling vars, StructureMapGroupInputComponent inp, VariableMode mode, XhtmlNode xs) {
    VariableForProfiling v = vars.get(mode, inp.getName());
    if (v != null)
      xs.addText("Input: "+v.property.getPath());
  }

  private void analyseRule(String indent, TransformContext context, StructureMap map, VariablesForProfiling vars, StructureMapGroupComponent group, StructureMapGroupRuleComponent rule, StructureMapAnalysis result) throws Exception {
    log(indent+"Analyse rule : "+rule.getName());
    XhtmlNode tr = result.summary.addTag("tr");
    XhtmlNode xs = tr.addTag("td");
    XhtmlNode xt = tr.addTag("td");

    VariablesForProfiling srcVars = vars.copy();
    if (rule.getSource().size() != 1)
      throw new Exception("Rule \""+rule.getName()+"\": not handled yet");
    VariablesForProfiling source = analyseSource(rule.getName(), context, srcVars, rule.getSourceFirstRep(), xs);

    TargetWriter tw = new TargetWriter();
      for (StructureMapGroupRuleTargetComponent t : rule.getTarget()) {
      analyseTarget(rule.getName(), context, source, map, t, rule.getSourceFirstRep().getVariable(), tw, result.profiles, rule.getName());
      }
    tw.commit(xt);

          for (StructureMapGroupRuleComponent childrule : rule.getRule()) {
      analyseRule(indent+"  ", context, map, source, group, childrule, result);
          }
//    for (StructureMapGroupRuleDependentComponent dependent : rule.getDependent()) {
//      executeDependency(indent+"  ", context, map, v, group, dependent); // do we need group here?
//    }
          }

  public class StringPair {
    private String var;
    private String desc;
    public StringPair(String var, String desc) {
      super();
      this.var = var;
      this.desc = desc;
        }
    public String getVar() {
      return var;
      }
    public String getDesc() {
      return desc;
    }
  }
  public class TargetWriter {
    private Map<String, String> newResources = new HashMap<String, String>();
    private List<StringPair> assignments = new ArrayList<StringPair>();
    private List<StringPair> keyProps = new ArrayList<StringPair>();
    private CommaSeparatedStringBuilder txt = new CommaSeparatedStringBuilder();

    public void newResource(String var, String name) {
      newResources.put(var, name);
      txt.append("new "+name);
    }

    public void valueAssignment(String context, String desc) {
      assignments.add(new StringPair(context, desc));      
      txt.append(desc);
        }

    public void keyAssignment(String context, String desc) {
      keyProps.add(new StringPair(context, desc));      
      txt.append(desc);
    }
    public void commit(XhtmlNode xt) {
      if (newResources.size() == 1 && assignments.size() == 1 && newResources.containsKey(assignments.get(0).getVar()) && keyProps.size() == 1 && newResources.containsKey(keyProps.get(0).getVar()) ) {
        xt.addText("new "+assignments.get(0).desc+" ("+keyProps.get(0).desc.substring(keyProps.get(0).desc.indexOf(".")+1)+")");
      } else if (newResources.size() == 1 && assignments.size() == 1 && newResources.containsKey(assignments.get(0).getVar()) && keyProps.size() == 0) {
        xt.addText("new "+assignments.get(0).desc);
      } else {
        xt.addText(txt.toString());        
    }
    }
  }

  private VariablesForProfiling analyseSource(String ruleId, TransformContext context, VariablesForProfiling vars, StructureMapGroupRuleSourceComponent src, XhtmlNode td) throws Exception {
    VariableForProfiling var = vars.get(VariableMode.INPUT, src.getContext());
    if (var == null)
      throw new FHIRException("Rule \""+ruleId+"\": Unknown input variable "+src.getContext());
    PropertyWithType prop = var.getProperty();

    boolean optional = false;
    boolean repeating = false;

    if (src.hasCondition()) {
      optional = true;
    }

    if (src.hasElement()) {
      Property element = prop.getBaseProperty().getChild(prop.types.getType(), src.getElement());
      if (element == null)
        throw new Exception("Rule \""+ruleId+"\": Unknown element name "+src.getElement());
      if (element.getDefinition().getMin() == 0)
        optional = true;
      if (element.getDefinition().getMax().equals("*"))
        repeating = true;
      VariablesForProfiling result = vars.copy(optional, repeating);
      TypeDetails type = new TypeDetails(CollectionStatus.SINGLETON);
      for (TypeRefComponent tr : element.getDefinition().getType()) {
        if (!tr.hasCode())
          throw new Error("Rule \""+ruleId+"\": Element has no type");
        ProfiledType pt = new ProfiledType(tr.getCode());
        if (tr.hasProfile())
          pt.addProfile(tr.getProfile());
        if (element.getDefinition().hasBinding())
          pt.addBinding(element.getDefinition().getBinding());
        type.addType(pt);
    } 
      td.addText(prop.getPath()+"."+src.getElement()); 
      if (src.hasVariable())
        result.add(VariableMode.INPUT, src.getVariable(), new PropertyWithType(prop.getPath()+"."+src.getElement(), element, null, type));
    return result;
    } else {
      td.addText(prop.getPath()); // ditto!
      return vars.copy(optional, repeating);
    }
  }


  private void analyseTarget(String ruleId, TransformContext context, VariablesForProfiling vars, StructureMap map, StructureMapGroupRuleTargetComponent tgt, String tv, TargetWriter tw, List<StructureDefinition> profiles, String sliceName) throws Exception {
    VariableForProfiling var = null;
    if (tgt.hasContext()) {
      var = vars.get(VariableMode.OUTPUT, tgt.getContext());
      if (var == null)
        throw new Exception("Rule \""+ruleId+"\": target context not known: "+tgt.getContext());
      if (!tgt.hasElement())
        throw new Exception("Rule \""+ruleId+"\": Not supported yet");
    }

    
    TypeDetails type = null;
    if (tgt.hasTransform()) {
      type = analyseTransform(context, map, tgt, var, vars);
        // profiling: dest.setProperty(tgt.getElement().hashCode(), tgt.getElement(), v);
    } else {
      Property vp = var.property.baseProperty.getChild(tgt.getElement(),  tgt.getElement());
      if (vp == null)
        throw new Exception("Unknown Property "+tgt.getElement()+" on "+var.property.path);
      
      type = new TypeDetails(CollectionStatus.SINGLETON, vp.getType(tgt.getElement()));
    }

    if (tgt.getTransform() == StructureMapTransform.CREATE) {
      String s = getParamString(vars, tgt.getParameter().get(0));
      if (worker.getResourceNames().contains(s))
        tw.newResource(tgt.getVariable(), s);
    } else { 
      boolean mapsSrc = false;
      for (StructureMapGroupRuleTargetParameterComponent p : tgt.getParameter()) {
        Type pr = p.getValue();
        if (pr instanceof IdType && ((IdType) pr).asStringValue().equals(tv)) 
          mapsSrc = true;
      }
      if (mapsSrc) { 
        if (var == null)
          throw new Error("Rule \""+ruleId+"\": Attempt to assign with no context");
        tw.valueAssignment(tgt.getContext(), var.property.getPath()+"."+tgt.getElement()+getTransformSuffix(tgt.getTransform()));
      } else if (tgt.hasContext()) {
        if (isSignificantElement(var.property, tgt.getElement())) {
          String td = describeTransform(tgt);
          if (td != null)
            tw.keyAssignment(tgt.getContext(), var.property.getPath()+"."+tgt.getElement()+" = "+td);
        }
      }
    }
    Type fixed = generateFixedValue(tgt);
    
    PropertyWithType prop = updateProfile(var, tgt.getElement(), type, map, profiles, sliceName, fixed, tgt);
    if (tgt.hasVariable())
      if (tgt.hasElement())
        vars.add(VariableMode.OUTPUT, tgt.getVariable(), prop); 
      else
        vars.add(VariableMode.OUTPUT, tgt.getVariable(), prop); 
  }
  
  private Type generateFixedValue(StructureMapGroupRuleTargetComponent tgt) {
    if (!allParametersFixed(tgt))
      return null;
    if (!tgt.hasTransform())
      return null;
    switch (tgt.getTransform()) {
    case COPY: return tgt.getParameter().get(0).getValue(); 
    case TRUNCATE: return null; 
    //case ESCAPE: 
    //case CAST: 
    //case APPEND: 
    case TRANSLATE: return null; 
  //case DATEOP, 
  //case UUID, 
  //case POINTER, 
  //case EVALUATE, 
    case CC: 
      CodeableConcept cc = new CodeableConcept();
      cc.addCoding(buildCoding(tgt.getParameter().get(0).getValue(), tgt.getParameter().get(1).getValue()));
      return cc;
    case C: 
      return buildCoding(tgt.getParameter().get(0).getValue(), tgt.getParameter().get(1).getValue());
    case QTY: return null; 
  //case ID, 
  //case CP, 
    default:
      return null;
    }
  }

  @SuppressWarnings("rawtypes")
  private Coding buildCoding(Type value1, Type value2) {
    return new Coding().setSystem(((PrimitiveType) value1).asStringValue()).setCode(((PrimitiveType) value2).asStringValue()) ;
  }

  private boolean allParametersFixed(StructureMapGroupRuleTargetComponent tgt) {
    for (StructureMapGroupRuleTargetParameterComponent p : tgt.getParameter()) {
      Type pr = p.getValue();
      if (pr instanceof IdType)
        return false;
    }
    return true;
  }

  private String describeTransform(StructureMapGroupRuleTargetComponent tgt) throws FHIRException {
    switch (tgt.getTransform()) {
    case COPY: return null; 
    case TRUNCATE: return null; 
    //case ESCAPE: 
    //case CAST: 
    //case APPEND: 
    case TRANSLATE: return null; 
  //case DATEOP, 
  //case UUID, 
  //case POINTER, 
  //case EVALUATE, 
    case CC: return describeTransformCCorC(tgt); 
    case C: return describeTransformCCorC(tgt); 
    case QTY: return null; 
  //case ID, 
  //case CP, 
    default:
      return null;
    }
  }

  @SuppressWarnings("rawtypes")
  private String describeTransformCCorC(StructureMapGroupRuleTargetComponent tgt) throws FHIRException {
    if (tgt.getParameter().size() < 2)
      return null;
    Type p1 = tgt.getParameter().get(0).getValue();
    Type p2 = tgt.getParameter().get(1).getValue();
    if (p1 instanceof IdType || p2 instanceof IdType)
      return null;
    if (!(p1 instanceof PrimitiveType) || !(p2 instanceof PrimitiveType))
      return null;
    String uri = ((PrimitiveType) p1).asStringValue();
    String code = ((PrimitiveType) p2).asStringValue();
    if (Utilities.noString(uri))
      throw new FHIRException("Describe Transform, but the uri is blank");
    if (Utilities.noString(code))
      throw new FHIRException("Describe Transform, but the code is blank");
    Coding c = buildCoding(uri, code);
    return NarrativeGenerator.describeSystem(c.getSystem())+"#"+c.getCode()+(c.hasDisplay() ? "("+c.getDisplay()+")" : "");
  }


  private boolean isSignificantElement(PropertyWithType property, String element) {
    if ("Observation".equals(property.getPath()))
      return "code".equals(element);
    else if ("Bundle".equals(property.getPath()))
      return "type".equals(element);
    else
      return false;
  }

  private String getTransformSuffix(StructureMapTransform transform) {
    switch (transform) {
    case COPY: return ""; 
    case TRUNCATE: return " (truncated)"; 
    //case ESCAPE: 
    //case CAST: 
    //case APPEND: 
    case TRANSLATE: return " (translated)"; 
  //case DATEOP, 
  //case UUID, 
  //case POINTER, 
  //case EVALUATE, 
    case CC: return " (--> CodeableConcept)"; 
    case C: return " (--> Coding)"; 
    case QTY: return " (--> Quantity)"; 
  //case ID, 
  //case CP, 
    default:
      return " {??)";
    }
  }

  private PropertyWithType updateProfile(VariableForProfiling var, String element, TypeDetails type, StructureMap map, List<StructureDefinition> profiles, String sliceName, Type fixed, StructureMapGroupRuleTargetComponent tgt) throws FHIRException {
    if (var == null) {
      assert (Utilities.noString(element));
      // 1. start the new structure definition
      StructureDefinition sdn = worker.fetchResource(StructureDefinition.class, type.getType());
      if (sdn == null)
        throw new FHIRException("Unable to find definition for "+type.getType());
      ElementDefinition edn = sdn.getSnapshot().getElementFirstRep();
      PropertyWithType pn = createProfile(map, profiles, new PropertyWithType(sdn.getId(), new Property(worker, edn, sdn), null, type), sliceName, tgt);

//      // 2. hook it into the base bundle
//      if (type.getType().startsWith("http://hl7.org/fhir/StructureDefinition/") && worker.getResourceNames().contains(type.getType().substring(40))) {
//        StructureDefinition sd = var.getProperty().profileProperty.getStructure();
//        ElementDefinition ed = sd.getDifferential().addElement();
//        ed.setPath("Bundle.entry");
//        ed.setName(sliceName);
//        ed.setMax("1"); // well, it is for now...
//        ed = sd.getDifferential().addElement();
//        ed.setPath("Bundle.entry.fullUrl");
//        ed.setMin(1);
//        ed = sd.getDifferential().addElement();
//        ed.setPath("Bundle.entry.resource");
//        ed.setMin(1);
//        ed.addType().setCode(pn.getProfileProperty().getStructure().getType()).setProfile(pn.getProfileProperty().getStructure().getUrl());
//      }
      return pn; 
    } else {
      assert (!Utilities.noString(element));
      Property pvb = var.getProperty().getBaseProperty();
      Property pvd = var.getProperty().getProfileProperty();
      Property pc = pvb.getChild(element, var.property.types);
      if (pc == null)
        throw new DefinitionException("Unable to find a definition for "+pvb.getDefinition().getPath()+"."+element);
      
      // the profile structure definition (derived)
      StructureDefinition sd = var.getProperty().profileProperty.getStructure();
      ElementDefinition ednew = sd.getDifferential().addElement();
      ednew.setPath(var.getProperty().profileProperty.getDefinition().getPath()+"."+pc.getName());
      ednew.setUserData("slice-name", sliceName);
      ednew.setFixed(fixed);
      for (ProfiledType pt : type.getProfiledTypes()) {
        if (pt.hasBindings())
          ednew.setBinding(pt.getBindings().get(0));
        if (pt.getUri().startsWith("http://hl7.org/fhir/StructureDefinition/")) {
          String t = pt.getUri().substring(40);
          t = checkType(t, pc, pt.getProfiles());
          if (t != null) {
            if (pt.hasProfiles()) {
              for (String p : pt.getProfiles())
                if (t.equals("Reference"))
                  ednew.addType().setCode(t).setTargetProfile(p);
                else
                  ednew.addType().setCode(t).setProfile(p);
            } else 
            ednew.addType().setCode(t);
      }
        }
      }
      
      return new PropertyWithType(var.property.path+"."+element, pc, new Property(worker, ednew, sd), type);
    }
  }
  


  private String checkType(String t, Property pvb, List<String> profiles) throws FHIRException {
    if (pvb.getDefinition().getType().size() == 1 && isCompatibleType(t, pvb.getDefinition().getType().get(0).getCode()) && profilesMatch(profiles, pvb.getDefinition().getType().get(0).getProfile())) 
      return null;
    for (TypeRefComponent tr : pvb.getDefinition().getType()) {
      if (isCompatibleType(t, tr.getCode()))
        return tr.getCode(); // note what is returned - the base type, not the inferred mapping type
    }
    throw new FHIRException("The type "+t+" is not compatible with the allowed types for "+pvb.getDefinition().getPath());
  }

  private boolean profilesMatch(List<String> profiles, String profile) {
    return profiles == null || profiles.size() == 0 || (profiles.size() == 1 && profiles.get(0).equals(profile));
  }

  private boolean isCompatibleType(String t, String code) {
    if (t.equals(code))
      return true;
    if (t.equals("string")) {
      StructureDefinition sd = worker.fetchResource(StructureDefinition.class, "http://hl7.org/fhir/StructureDefinition/"+code);
      if (sd != null && sd.getBaseDefinition().equals("http://hl7.org/fhir/StructureDefinition/string"))
        return true;
    }
    return false;
  }

  private TypeDetails analyseTransform(TransformContext context, StructureMap map, StructureMapGroupRuleTargetComponent tgt, VariableForProfiling var, VariablesForProfiling vars) throws FHIRException {
    switch (tgt.getTransform()) {
    case CREATE :
      String p = getParamString(vars, tgt.getParameter().get(0));
      return new TypeDetails(CollectionStatus.SINGLETON, p);
    case COPY : 
      return getParam(vars, tgt.getParameter().get(0));
    case EVALUATE :
      ExpressionNode expr = (ExpressionNode) tgt.getUserData(MAP_EXPRESSION);
      if (expr == null) {
        expr = fpe.parse(getParamString(vars, tgt.getParameter().get(tgt.getParameter().size()-1)));
        tgt.setUserData(MAP_WHERE_EXPRESSION, expr);
      }
      return fpe.check(vars, null, expr);

////case TRUNCATE : 
////  String src = getParamString(vars, tgt.getParameter().get(0));
////  String len = getParamString(vars, tgt.getParameter().get(1));
////  if (Utilities.isInteger(len)) {
////    int l = Integer.parseInt(len);
////    if (src.length() > l)
////      src = src.substring(0, l);
////  }
////  return new StringType(src);
////case ESCAPE : 
////  throw new Error("Transform "+tgt.getTransform().toCode()+" not supported yet");
////case CAST :
////  throw new Error("Transform "+tgt.getTransform().toCode()+" not supported yet");
////case APPEND : 
////  throw new Error("Transform "+tgt.getTransform().toCode()+" not supported yet");
    case TRANSLATE : 
      return new TypeDetails(CollectionStatus.SINGLETON, "CodeableConcept");
   case CC:
     ProfiledType res = new ProfiledType("CodeableConcept");
     if (tgt.getParameter().size() >= 2  && isParamId(vars, tgt.getParameter().get(1))) {
       TypeDetails td = vars.get(null, getParamId(vars, tgt.getParameter().get(1))).property.types;
       if (td != null && td.hasBinding())
         // todo: do we need to check that there's no implicit translation her? I don't think we do...
         res.addBinding(td.getBinding());
     }
     return new TypeDetails(CollectionStatus.SINGLETON, res);
   case C:
     return new TypeDetails(CollectionStatus.SINGLETON, "Coding");
   case QTY:
     return new TypeDetails(CollectionStatus.SINGLETON, "Quantity");
   case REFERENCE :
      VariableForProfiling vrs = vars.get(VariableMode.OUTPUT, getParamId(vars, tgt.getParameterFirstRep()));
      if (vrs == null)
        throw new FHIRException("Unable to resolve variable \""+getParamId(vars, tgt.getParameterFirstRep())+"\"");
      String profile = vrs.property.getProfileProperty().getStructure().getUrl();
     TypeDetails td = new TypeDetails(CollectionStatus.SINGLETON);
     td.addType("Reference", profile);
     return td;  
////case DATEOP :
////  throw new Error("Transform "+tgt.getTransform().toCode()+" not supported yet");
////case UUID :
////  return new IdType(UUID.randomUUID().toString());
////case POINTER :
////  Base b = getParam(vars, tgt.getParameter().get(0));
////  if (b instanceof Resource)
////    return new UriType("urn:uuid:"+((Resource) b).getId());
////  else
////    throw new FHIRException("Transform engine cannot point at an element of type "+b.fhirType());
    default:
      throw new Error("Transform Unknown or not handled yet: "+tgt.getTransform().toCode());
    }
  }
  private String getParamString(VariablesForProfiling vars, StructureMapGroupRuleTargetParameterComponent parameter) {
    Type p = parameter.getValue();
    if (p == null || p instanceof IdType)
      return null;
    if (!p.hasPrimitiveValue())
      return null;
    return p.primitiveValue();
  }

  private String getParamId(VariablesForProfiling vars, StructureMapGroupRuleTargetParameterComponent parameter) {
    Type p = parameter.getValue();
    if (p == null || !(p instanceof IdType))
      return null;
    return p.primitiveValue();
  }

  private boolean isParamId(VariablesForProfiling vars, StructureMapGroupRuleTargetParameterComponent parameter) {
    Type p = parameter.getValue();
    if (p == null || !(p instanceof IdType))
      return false;
    return vars.get(null, p.primitiveValue()) != null;
  }

  private TypeDetails getParam(VariablesForProfiling vars, StructureMapGroupRuleTargetParameterComponent parameter) throws DefinitionException {
    Type p = parameter.getValue();
    if (!(p instanceof IdType))
      return new TypeDetails(CollectionStatus.SINGLETON, "http://hl7.org/fhir/StructureDefinition/"+p.fhirType());
    else { 
      String n = ((IdType) p).asStringValue();
      VariableForProfiling b = vars.get(VariableMode.INPUT, n);
      if (b == null)
        b = vars.get(VariableMode.OUTPUT, n);
      if (b == null)
        throw new DefinitionException("Variable "+n+" not found ("+vars.summary()+")");
      return b.getProperty().getTypes();
    }
  }

  private PropertyWithType createProfile(StructureMap map, List<StructureDefinition> profiles, PropertyWithType prop, String sliceName, Base ctxt) throws DefinitionException {
    if (prop.getBaseProperty().getDefinition().getPath().contains(".")) 
      throw new DefinitionException("Unable to process entry point");

    String type = prop.getBaseProperty().getDefinition().getPath();
    String suffix = "";
    if (ids.containsKey(type)) {
      int id = ids.get(type);
      id++;
      ids.put(type, id);
      suffix = "-"+Integer.toString(id);
    } else
      ids.put(type, 0);
    
    StructureDefinition profile = new StructureDefinition();
    profiles.add(profile);
    profile.setDerivation(TypeDerivationRule.CONSTRAINT);
    profile.setType(type);
    profile.setBaseDefinition(prop.getBaseProperty().getStructure().getUrl());
    profile.setName("Profile for "+profile.getType()+" for "+sliceName);
    profile.setUrl(map.getUrl().replace("StructureMap", "StructureDefinition")+"-"+profile.getType()+suffix);
    ctxt.setUserData("profile", profile.getUrl()); // then we can easily assign this profile url for validation later when we actually transform
    profile.setId(map.getId()+"-"+profile.getType()+suffix);
    profile.setStatus(map.getStatus());
    profile.setExperimental(map.getExperimental());
    profile.setDescription("Generated automatically from the mapping by the Java Reference Implementation");
    for (ContactDetail c : map.getContact()) {
      ContactDetail p = profile.addContact();
      p.setName(c.getName());
      for (ContactPoint cc : c.getTelecom()) 
        p.addTelecom(cc);
    }
    profile.setDate(map.getDate());
    profile.setCopyright(map.getCopyright());
    profile.setFhirVersion(Constants.VERSION);
    profile.setKind(prop.getBaseProperty().getStructure().getKind());
    profile.setAbstract(false);
    ElementDefinition ed = profile.getDifferential().addElement();
    ed.setPath(profile.getType());
    prop.profileProperty = new Property(worker, ed, profile);
    return prop;
  }

  private PropertyWithType resolveType(StructureMap map, String type, StructureMapInputMode mode) throws Exception {
    for (StructureMapStructureComponent imp : map.getStructure()) {
      if ((imp.getMode() == StructureMapModelMode.SOURCE && mode == StructureMapInputMode.SOURCE) || 
          (imp.getMode() == StructureMapModelMode.TARGET && mode == StructureMapInputMode.TARGET)) {
        StructureDefinition sd = worker.fetchResource(StructureDefinition.class, imp.getUrl());
        if (sd == null)
          throw new Exception("Import "+imp.getUrl()+" cannot be resolved");
        if (sd.getId().equals(type)) {
          return new PropertyWithType(sd.getType(), new Property(worker, sd.getSnapshot().getElement().get(0), sd), null, new TypeDetails(CollectionStatus.SINGLETON, sd.getUrl()));
        }
      }
    }
    throw new Exception("Unable to find structure definition for "+type+" in imports");
  }


  public StructureMap generateMapFromMappings(StructureDefinition sd) throws IOException, FHIRException {
    String id = getLogicalMappingId(sd);
    if (id == null) 
        return null;
    String prefix = ToolingExtensions.readStringExtension(sd,  ToolingExtensions.EXT_MAPPING_PREFIX);
    String suffix = ToolingExtensions.readStringExtension(sd,  ToolingExtensions.EXT_MAPPING_SUFFIX);
    if (prefix == null || suffix == null)
      return null;
    // we build this by text. Any element that has a mapping, we put it's mappings inside it....
    StringBuilder b = new StringBuilder();
    b.append(prefix);

    ElementDefinition root = sd.getSnapshot().getElementFirstRep();
    String m = getMapping(root, id);
    if (m != null)
      b.append(m+"\r\n");
    addChildMappings(b, id, "", sd, root, false);
    b.append("\r\n");
    b.append(suffix);
    b.append("\r\n");
    TextFile.stringToFile(b.toString(), "c:\\temp\\test.map");
    StructureMap map = parse(b.toString());
    map.setId(tail(map.getUrl()));
    if (!map.hasStatus())
      map.setStatus(PublicationStatus.DRAFT);
    map.getText().setStatus(NarrativeStatus.GENERATED);
    map.getText().setDiv(new XhtmlNode(NodeType.Element, "div"));
    map.getText().getDiv().addTag("pre").addText(render(map));
    return map;
  }


  private String tail(String url) {
    return url.substring(url.lastIndexOf("/")+1);
  }


  private void addChildMappings(StringBuilder b, String id, String indent, StructureDefinition sd, ElementDefinition ed, boolean inner) throws DefinitionException {
    boolean first = true;
    List<ElementDefinition> children = ProfileUtilities.getChildMap(sd, ed);
    for (ElementDefinition child : children) {
      if (first && inner) {
        b.append(" then {\r\n");
        first = false;
      }
      String map = getMapping(child, id);
      if (map != null) {
        b.append(indent+"  "+child.getPath()+": "+map);
        addChildMappings(b, id, indent+"  ", sd, child, true);
        b.append("\r\n");
      }
    }
    if (!first && inner)
      b.append(indent+"}");
    
  }


  private String getMapping(ElementDefinition ed, String id) {
    for (ElementDefinitionMappingComponent map : ed.getMapping())
      if (id.equals(map.getIdentity()))
        return map.getMap();
    return null;
  }


  private String getLogicalMappingId(StructureDefinition sd) {
    String id = null;
    for (StructureDefinitionMappingComponent map : sd.getMapping()) {
      if ("http://hl7.org/fhir/logical".equals(map.getUri()))
        return map.getIdentity();
    }
    return null;
  }
	
}
