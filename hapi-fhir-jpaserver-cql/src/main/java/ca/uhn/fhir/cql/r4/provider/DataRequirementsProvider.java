package ca.uhn.fhir.cql.r4.provider;

import ca.uhn.fhir.cql.common.helper.TranslatorHelper;
import ca.uhn.fhir.cql.common.provider.LibraryResolutionProvider;
import ca.uhn.fhir.cql.r4.helper.CanonicalHelper;
import ca.uhn.fhir.cql.r4.helper.LibraryHelper;
import com.google.common.base.Strings;
import com.vladsch.flexmark.ext.autolink.AutolinkExtension;
import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughExtension;
import com.vladsch.flexmark.ext.gfm.tasklist.TaskListExtension;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.parser.ParserEmulationProfile;
import com.vladsch.flexmark.util.ast.KeepType;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataSet;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.cqframework.cql.cql2elm.CqlTranslator;
import org.cqframework.cql.cql2elm.LibraryManager;
import org.cqframework.cql.cql2elm.ModelManager;
import org.cqframework.cql.elm.execution.CodeDef;
import org.cqframework.cql.elm.execution.CodeSystemDef;
import org.cqframework.cql.elm.execution.ExpressionDef;
import org.cqframework.cql.elm.execution.FunctionDef;
import org.cqframework.cql.elm.execution.IncludeDef;
import org.cqframework.cql.elm.execution.Library;
import org.cqframework.cql.elm.execution.ValueSetDef;
import org.cqframework.cql.elm.execution.VersionedIdentifier;
import org.cqframework.cql.tools.formatter.CqlFormatterVisitor;
import org.cqframework.cql.tools.formatter.CqlFormatterVisitor.FormatResult;
import org.hl7.elm.r1.ValueSetRef;
import org.hl7.fhir.r4.model.Attachment;
import org.hl7.fhir.r4.model.Base64BinaryType;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.DataRequirement;
import org.hl7.fhir.r4.model.Expression;
import org.hl7.fhir.r4.model.MarkdownType;
import org.hl7.fhir.r4.model.Measure;
import org.hl7.fhir.r4.model.ParameterDefinition;
import org.hl7.fhir.r4.model.RelatedArtifact;
import org.hl7.fhir.r4.model.StringType;
import org.opencds.cqf.cql.engine.execution.LibraryLoader;
import org.opencds.cqf.tooling.measure.r4.CodeTerminologyRef;
import org.opencds.cqf.tooling.measure.r4.CqfMeasure;
import org.opencds.cqf.tooling.measure.r4.TerminologyRef;
import org.opencds.cqf.tooling.measure.r4.TerminologyRef.TerminologyRefType;
import org.opencds.cqf.tooling.measure.r4.VersionedTerminologyRef;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class DataRequirementsProvider {

    // For creating the CQF measure we need to:
    // 1. Find the Primary Library Resource
    // 2. Load the Primary Library as ELM. This will recursively load the dependent
    // libraries as ELM by Name
    // 3. Load the Library Dependencies as Resources
    // 4. Update the Data Requirements on the Resources accordingly
    // Since the Library Loader only exposes the loaded libraries as ELM, we
    // actually have to load them twice.
    // Once via the loader, Once manually
    public CqfMeasure createCqfMeasure(Measure measure,
            LibraryResolutionProvider<org.hl7.fhir.r4.model.Library> libraryResourceProvider) {
        Map<VersionedIdentifier, Pair<Library, org.hl7.fhir.r4.model.Library>> libraryMap = this
                .createLibraryMap(measure, libraryResourceProvider);
        return this.createCqfMeasure(measure, libraryMap);
    }

    private Map<VersionedIdentifier, Pair<Library, org.hl7.fhir.r4.model.Library>> createLibraryMap(Measure measure,
            LibraryResolutionProvider<org.hl7.fhir.r4.model.Library> libraryResourceProvider) {
        LibraryLoader libraryLoader = LibraryHelper.createLibraryLoader(libraryResourceProvider);
        List<Library> libraries = LibraryHelper.loadLibraries(measure, libraryLoader, libraryResourceProvider);
        Map<VersionedIdentifier, Pair<Library, org.hl7.fhir.r4.model.Library>> libraryMap = new HashMap<>();

        for (Library library : libraries) {
            VersionedIdentifier vi = library.getIdentifier();
            org.hl7.fhir.r4.model.Library libraryResource = libraryResourceProvider.resolveLibraryByName(vi.getId(),
                    vi.getVersion());
            libraryMap.put(vi, Pair.of(library, libraryResource));
        }

        return libraryMap;
    }

    private CqfMeasure createCqfMeasure(Measure measure,
            Map<VersionedIdentifier, Pair<Library, org.hl7.fhir.r4.model.Library>> libraryMap) {
        // Ensure All Data Requirements for all referenced libraries
        org.hl7.fhir.r4.model.Library moduleDefinition = this.getDataRequirements(measure,
                libraryMap.values().stream().map(Pair::getRight).filter(Objects::nonNull).collect(Collectors.toList()));

        CqfMeasure cqfMeasure = new CqfMeasure(measure);
        moduleDefinition.getRelatedArtifact().forEach(cqfMeasure::addRelatedArtifact);
        cqfMeasure.setDataRequirement(moduleDefinition.getDataRequirement());
        cqfMeasure.setParameter(moduleDefinition.getParameter());

        ArrayList<RelatedArtifact> citations = new ArrayList<>();
        for (RelatedArtifact citation : cqfMeasure.getRelatedArtifact()) {
            if (citation.hasType() && Objects.equals(citation.getType().toCode(), "citation")
                    && citation.hasCitation()) {
                citations.add(citation);
            }
        }

        ArrayList<Measure.MeasureGroupComponent> populationStatements = new ArrayList<>();
        for (Measure.MeasureGroupComponent group : measure.getGroup()) {
            populationStatements.add(group.copy());
        }
        List<Measure.MeasureGroupPopulationComponent> definitionStatements = new ArrayList<>();
        List<Measure.MeasureGroupPopulationComponent> functionStatements = new ArrayList<>();
        List<Measure.MeasureGroupPopulationComponent> supplementalDataElements = new ArrayList<>();
        List<TerminologyRef> terminology = new ArrayList<>();
        List<TerminologyRef> codes = new ArrayList<>();
        List<TerminologyRef> codeSystems = new ArrayList<>();
        List<TerminologyRef> valueSets = new ArrayList<>();
        List<StringType> dataCriteria = new ArrayList<>();

        String primaryLibraryId = CanonicalHelper.getId(measure.getLibrary().get(0));
        Library primaryLibrary = libraryMap.values().stream().filter(x -> x.getRight() != null)
                .filter(x -> x.getRight().getIdElement() != null
                        && x.getRight().getIdElement().getIdPart().equals(primaryLibraryId))
                .findFirst().get().getLeft();

        for (Map.Entry<VersionedIdentifier, Pair<Library, org.hl7.fhir.r4.model.Library>> libraryEntry : libraryMap
                .entrySet()) {
            Library library = libraryEntry.getValue().getLeft();
            org.hl7.fhir.r4.model.Library libraryResource = libraryEntry.getValue().getRight();
            String libraryNamespace = "";
            if (primaryLibrary.getIncludes() != null) {
                for (IncludeDef include : primaryLibrary.getIncludes().getDef()) {
                    if (library.getIdentifier().getId().equalsIgnoreCase(include.getPath())) {
                        libraryNamespace = include.getLocalIdentifier() + ".";
                    }
                }
            }

            if (library.getCodeSystems() != null && library.getCodeSystems().getDef() != null) {
                for (CodeSystemDef codeSystem : library.getCodeSystems().getDef()) {
                    String codeId = codeSystem.getId().replace("urn:oid:", "");
                    String name = codeSystem.getName();
                    String version = codeSystem.getVersion();

                    TerminologyRef term = new VersionedTerminologyRef(TerminologyRefType.CODESYSTEM, name, codeId,
                            version);
                    Boolean exists = false;
                    for (TerminologyRef t : codeSystems) {
                        if (t.getDefinition().equalsIgnoreCase(term.getDefinition())) {
                            exists = true;
                        }
                    }
                    if (!exists) {
                        codeSystems.add(term);
                    }
                }
            }

            if (library.getCodes() != null && library.getCodes().getDef() != null) {
                for (CodeDef code : library.getCodes().getDef()) {
                    String codeId = code.getId();
                    String name = code.getName();
                    String codeSystemName = code.getCodeSystem().getName();
                    String displayName = code.getDisplay();
                    String codeSystemId = null;

                    for (TerminologyRef rf : codeSystems) {
                        if (rf.getName().equals(codeSystemName)) {
                            codeSystemId = rf.getId();
                            break;
                        }
                    }

                    TerminologyRef term = new CodeTerminologyRef(name, codeId, codeSystemName, codeSystemId,
                            displayName);
                    Boolean exists = false;
                    for (TerminologyRef t : codes) {
                        if (t.getDefinition().equalsIgnoreCase(term.getDefinition())) {
                            exists = true;
                        }
                    }
                    if (!exists) {
                        codes.add(term);
                    }
                }
            }

            if (library.getValueSets() != null && library.getValueSets().getDef() != null) {
                for (ValueSetDef valueSet : library.getValueSets().getDef()) {
                    String valueSetId = valueSet.getId().replace("urn:oid:", "");
                    String name = valueSet.getName();

                    TerminologyRef term = new VersionedTerminologyRef(TerminologyRefType.VALUESET, name, valueSetId);
                    Boolean exists = false;
                    for (TerminologyRef t : valueSets) {
                        if (t.getDefinition().equalsIgnoreCase(term.getDefinition())) {
                            exists = true;
                        }
                    }
                    if (!exists) {
                        valueSets.add(term);
                    }

                    for (DataRequirement data : cqfMeasure.getDataRequirement()) {
                        String type = data.getType();

                        for (DataRequirement.DataRequirementCodeFilterComponent filter : data.getCodeFilter()) {
                            if (filter.hasValueSet() && filter.getValueSet().equalsIgnoreCase(valueSet.getId())) {
                                StringType dataElement = new StringType();
                                dataElement.setValueAsString(
                                        "\"" + type + ": " + name + "\" using \"" + name + " (" + valueSetId + ")");
                                exists = false;
                                for (StringType string : dataCriteria) {
                                    if (string.getValueAsString().equalsIgnoreCase(dataElement.getValueAsString())) {
                                        exists = true;
                                    }
                                }
                                if (!exists) {
                                    dataCriteria.add(dataElement);
                                }
                            }
                        }
                    }
                }
            }

            // Don't try to parse statements for libraries that don't have resources (such
            // as an embedded FHIRHelpers)
            if (libraryResource == null) {
                continue;
            }

            String cql = "";
            for (Attachment attachment : libraryResource.getContent()) {
                cqfMeasure.addContent(attachment);
                if (attachment.getContentType().equalsIgnoreCase("text/cql")) {
                    cql = new String(attachment.getData());
                }
            }

            String[] cqlLines = cql.replaceAll("[\r]", "").split("[\n]");

            if (library.getStatements() != null) {
                for (ExpressionDef statement : library.getStatements().getDef()) {
                    String[] location = statement.getLocator().split("-");
                    String statementText = "";
                    String signature = "";
                    int start = Integer.parseInt(location[0].split(":")[0]);
                    int end = Integer.parseInt(location[1].split(":")[0]);
                    for (int i = start - 1; i < end; i++) {
                        if (cqlLines[i].contains("define function \"" + statement.getName() + "\"(")) {
                            signature = cqlLines[i].substring(cqlLines[i].indexOf("("), cqlLines[i].indexOf(")") + 1);
                        }
                        if (!cqlLines[i].contains("define \"" + statement.getName() + "\":")
                                && !cqlLines[i].contains("define function \"" + statement.getName() + "\"(")) {
                            statementText = statementText
                                    .concat((statementText.length() > 0 ? "\r\n" : "") + cqlLines[i]);
                        }
                    }
                    if (statementText.startsWith("context")) {
                        continue;
                    }
                    Measure.MeasureGroupPopulationComponent def = new Measure.MeasureGroupPopulationComponent();
                    def.setCriteria(new Expression().setName(libraryNamespace + statement.getName() + signature)
                            .setExpression(statementText));
                    if (statement.getClass() == FunctionDef.class) {
                        functionStatements.add(def);
                    } else {
                        definitionStatements.add(def);
                    }

                    for (Measure.MeasureGroupComponent group : populationStatements) {
                        for (Measure.MeasureGroupPopulationComponent population : group.getPopulation()) {
                            if (population.hasCriteria() && population.getCriteria().hasExpression()
                                    && population.getCriteria().getExpression().equalsIgnoreCase(statement.getName())) {
                                String code = population.getCode().getCodingFirstRep().getCode();
                                String display = HQMFProvider.measurePopulationValueSetMap.get(code).displayName;
                                population.setCriteria(new Expression().setName(display).setExpression(statementText));
                            }
                        }

                        for (Measure.MeasureGroupStratifierComponent mgsc : group.getStratifier()) {
                            if (mgsc.hasCriteria() && mgsc.getCriteria().hasExpression()
                                    && mgsc.getCriteria().getExpression().equalsIgnoreCase(statement.getName())) {
                                mgsc.setCriteria(new Expression().setExpression(statementText));
                            }
                        }
                    }

                    for (Measure.MeasureSupplementalDataComponent dataComponent : cqfMeasure.getSupplementalData()) {
                        if (dataComponent.hasCriteria() && dataComponent.getCriteria().hasExpression() && dataComponent
                                .getCriteria().getExpression().equalsIgnoreCase(def.getCriteria().getName())) {
                            supplementalDataElements.add(def);
                        }
                    }
                }
            }
        }

        Comparator<StringType> stringTypeComparator = (item, t1) -> {
            String s1 = item.asStringValue();
            String s2 = t1.asStringValue();
            return s1.compareToIgnoreCase(s2);
        };

        Comparator<TerminologyRef> terminologyRefComparator = (item, t1) -> {
            String s1 = item.getDefinition();
            String s2 = t1.getDefinition();
            return s1.compareToIgnoreCase(s2);
        };
        Comparator<Measure.MeasureGroupPopulationComponent> populationComparator = (item, t1) -> {
            String s1 = item.getCriteria().getName();
            String s2 = t1.getCriteria().getName();
            return s1.compareToIgnoreCase(s2);
        };

        definitionStatements.sort(populationComparator);
        functionStatements.sort(populationComparator);
        supplementalDataElements.sort(populationComparator);
        codeSystems.sort(terminologyRefComparator);
        codes.sort(terminologyRefComparator);
        valueSets.sort(terminologyRefComparator);
        dataCriteria.sort(stringTypeComparator);

        terminology.addAll(codeSystems);
        terminology.addAll(codes);
        terminology.addAll(valueSets);

        cqfMeasure.setPopulationStatements(populationStatements);
        cqfMeasure.setDefinitionStatements(definitionStatements);
        cqfMeasure.setFunctionStatements(functionStatements);
        cqfMeasure.setSupplementalDataElements(supplementalDataElements);
        cqfMeasure.setTerminology(terminology);
        cqfMeasure.setDataCriteria(dataCriteria);
        cqfMeasure.setLibraries(
                libraryMap.values().stream().map(Pair::getRight).filter(Objects::nonNull).collect(Collectors.toList()));
        cqfMeasure.setCitations(citations);

        Map<String, List<Triple<Integer, String, String>>> criteriaMap = new HashMap<>();
        // Index all usages of criteria
        for (int i = 0; i < cqfMeasure.getGroup().size(); i++) {
            Measure.MeasureGroupComponent mgc = cqfMeasure.getGroup().get(i);
            for (int j = 0; j < mgc.getPopulation().size(); j++) {
                Measure.MeasureGroupPopulationComponent mgpc = mgc.getPopulation().get(j);
                String criteria = mgpc.getCriteria().getExpression();
                if (criteria != null && !criteria.isEmpty()) {
                    if (!criteriaMap.containsKey(criteria)) {
                        criteriaMap.put(criteria, new ArrayList<>());
                    }

                    criteriaMap.get(criteria)
                            .add(Triple.of(i, mgpc.getCode().getCodingFirstRep().getCode(), mgpc.getDescription()));
                }
            }
        }

        // Find shared usages
        for (Entry<String, List<Triple<Integer, String, String>>> entry : criteriaMap.entrySet()) {
            String criteria = entry.getKey();
            if (cqfMeasure.getGroup().size() == 1
                    || entry.getValue().stream().map(Triple::getLeft).distinct().count() > 1) {
                String code = entry.getValue().get(0).getMiddle();
                String display = HQMFProvider.measurePopulationValueSetMap.get(code).displayName;
                cqfMeasure.addSharedPopulationCritiera(criteria, display, entry.getValue().get(0).getRight());
            }
        }

        // If there's only one group every criteria was shared. Kill the group.
        if (cqfMeasure.getGroup().size() == 1) {
            cqfMeasure.getGroup().clear();
        }
        // Otherwise, remove the shared components.
        else {
            for (int i = 0; i < cqfMeasure.getGroup().size(); i++) {
                Measure.MeasureGroupComponent mgc = cqfMeasure.getGroup().get(i);
                List<Measure.MeasureGroupPopulationComponent> newMgpc = new ArrayList<>();
                for (int j = 0; j < mgc.getPopulation().size(); j++) {
                    Measure.MeasureGroupPopulationComponent mgpc = mgc.getPopulation().get(j);
                    if (mgpc.hasCriteria() && mgpc.getCriteria().hasExpression() && !cqfMeasure
                            .getSharedPopulationCritieria().containsKey(mgpc.getCriteria().getExpression())) {
                        String code = mgpc.getCode().getCodingFirstRep().getCode();
                        String display = HQMFProvider.measurePopulationValueSetMap.get(code).displayName;
                        mgpc.getCriteria().setName(display);
                        newMgpc.add(mgpc);
                    }
                }

                mgc.setPopulation(newMgpc);
            }
        }

        return processMarkDown(cqfMeasure);
    }

    private CqfMeasure processMarkDown(CqfMeasure measure) {

        MutableDataSet options = new MutableDataSet();

        options.setFrom(ParserEmulationProfile.GITHUB_DOC);
        options.set(Parser.EXTENSIONS, Arrays.asList(AutolinkExtension.create(),
                // AnchorLinkExtension.create(),
                // EmojiExtension.create(),
                StrikethroughExtension.create(), TablesExtension.create(), TaskListExtension.create()));

        // uncomment and define location of emoji images from
        // https://github.com/arvida/emoji-cheat-sheet.com
        // options.set(EmojiExtension.ROOT_IMAGE_PATH, "");

        // Uncomment if GFM anchor links are desired in headings
        // options.set(AnchorLinkExtension.ANCHORLINKS_SET_ID, false);
        // options.set(AnchorLinkExtension.ANCHORLINKS_ANCHOR_CLASS, "anchor");
        // options.set(AnchorLinkExtension.ANCHORLINKS_SET_NAME, true);
        // options.set(AnchorLinkExtension.ANCHORLINKS_TEXT_PREFIX, "<span
        // class=\"octicon octicon-link\"></span>");

        // References compatibility
        options.set(Parser.REFERENCES_KEEP, KeepType.LAST);

        // Set GFM table parsing options
        options.set(TablesExtension.COLUMN_SPANS, false).set(TablesExtension.MIN_HEADER_ROWS, 1)
                .set(TablesExtension.MAX_HEADER_ROWS, 1).set(TablesExtension.APPEND_MISSING_COLUMNS, true)
                .set(TablesExtension.DISCARD_EXTRA_COLUMNS, true).set(TablesExtension.WITH_CAPTION, false)
                .set(TablesExtension.HEADER_SEPARATOR_COLUMN_MATCH, true);

        // Setup List Options for GitHub profile which is kramdown for documents
        options.setFrom(ParserEmulationProfile.GITHUB_DOC);

        options.set(HtmlRenderer.SOFT_BREAK, "<br />\n");

        Parser parser = Parser.builder(options).build();
        HtmlRenderer renderer = HtmlRenderer.builder(options).build();

        measure.setDescription(markdownToHtml(parser, renderer, measure.getDescription()));
        measure.setPurpose(markdownToHtml(parser, renderer, measure.getPurpose()));
        // measure.setCopyright(markdownToHtml(parser, renderer,
        // measure.getCopyright()));
        measure.setRationale(markdownToHtml(parser, renderer, measure.getRationale()));
        measure.setClinicalRecommendationStatement(
                markdownToHtml(parser, renderer, measure.getClinicalRecommendationStatement()));
        measure.setGuidance(markdownToHtml(parser, renderer, measure.getGuidance()));

        measure.setDefinition(
                measure.getDefinition().stream().map(x -> markdownToHtml(parser, renderer, x.getValueAsString()))
                        .map(MarkdownType::new).collect(Collectors.toList()));

        return measure;
    }

    private String markdownToHtml(Parser parser, HtmlRenderer renderer, String markdown) {
        if (Strings.isNullOrEmpty(markdown)) {
            return null;
        }

        Node document = parser.parse(markdown);
        return renderer.render(document);
    }

    public org.hl7.fhir.r4.model.Library getDataRequirements(Measure measure,
            LibraryResolutionProvider<org.hl7.fhir.r4.model.Library> libraryResourceProvider) {
        Map<VersionedIdentifier, Pair<Library, org.hl7.fhir.r4.model.Library>> libraryMap = this
                .createLibraryMap(measure, libraryResourceProvider);
        return this.getDataRequirements(measure,
                libraryMap.values().stream().map(Pair::getRight).filter(Objects::nonNull).collect(Collectors.toList()));
    }

    private org.hl7.fhir.r4.model.Library getDataRequirements(Measure measure,
            Collection<org.hl7.fhir.r4.model.Library> libraries) {
        List<DataRequirement> reqs = new ArrayList<>();
        List<RelatedArtifact> dependencies = new ArrayList<>();
        List<ParameterDefinition> parameters = new ArrayList<>();

        for (org.hl7.fhir.r4.model.Library library : libraries) {
            for (RelatedArtifact dependency : library.getRelatedArtifact()) {
                if (dependency.getType().toCode().equals("depends-on")) {
                    dependencies.add(dependency);
                }
            }

            reqs.addAll(library.getDataRequirement());
            parameters.addAll(library.getParameter());
        }

        List<Coding> typeCoding = new ArrayList<>();
        typeCoding.add(new Coding().setCode("module-definition"));
        org.hl7.fhir.r4.model.Library library = new org.hl7.fhir.r4.model.Library()
                .setType(new CodeableConcept().setCoding(typeCoding));

        if (!dependencies.isEmpty()) {
            library.setRelatedArtifact(dependencies);
        }

        if (!reqs.isEmpty()) {
            library.setDataRequirement(reqs);
        }

        if (!parameters.isEmpty()) {
            library.setParameter(parameters);
        }

        return library;
    }

    public CqlTranslator getTranslator(org.hl7.fhir.r4.model.Library library, LibraryManager libraryManager,
            ModelManager modelManager) {
        Attachment cql = null;
        for (Attachment a : library.getContent()) {
            if (a.getContentType().equals("text/cql")) {
                cql = a;
                break;
            }
        }

        if (cql == null) {
            return null;
        }

        return TranslatorHelper.getTranslator(
                new ByteArrayInputStream(Base64.getDecoder().decode(cql.getDataElement().getValueAsString())),
                libraryManager, modelManager);
    }

    public void formatCql(org.hl7.fhir.r4.model.Library library) {
        for (Attachment att : library.getContent()) {
            if (att.getContentType().equals("text/cql")) {
                try {
                    FormatResult fr = CqlFormatterVisitor.getFormattedOutput(new ByteArrayInputStream(
                            Base64.getDecoder().decode(att.getDataElement().getValueAsString())));

                    // Only update the content if it's valid CQL.
                    if (fr.getErrors().size() == 0) {
                        Base64BinaryType bt = new Base64BinaryType(
                                new String(Base64.getEncoder().encode(fr.getOutput().getBytes())));
                        att.setDataElement(bt);
                    }
                } catch (IOException e) {
                    // Intentionally empty for now
                }
            }
        }
    }

    public void ensureElm(org.hl7.fhir.r4.model.Library library, CqlTranslator translator) {

        library.getContent().removeIf(a -> a.getContentType().equals("application/elm+xml"));
        String xml = translator.toXml();
        Attachment elm = new Attachment();
        elm.setContentType("application/elm+xml");
        elm.setData(xml.getBytes());
        library.getContent().add(elm);
    }

    public void ensureRelatedArtifacts(org.hl7.fhir.r4.model.Library library, CqlTranslator translator,
            LibraryResolutionProvider<org.hl7.fhir.r4.model.Library> libraryResourceProvider) {
        library.getRelatedArtifact().clear();
        org.hl7.elm.r1.Library elm = translator.toELM();
        if (elm.getIncludes() != null && !elm.getIncludes().getDef().isEmpty()) {
            for (org.hl7.elm.r1.IncludeDef def : elm.getIncludes().getDef()) {
                org.hl7.fhir.r4.model.Library relatedLibrary = libraryResourceProvider
                        .resolveLibraryByName(def.getPath(), def.getVersion());
                library.addRelatedArtifact(new RelatedArtifact().setType(RelatedArtifact.RelatedArtifactType.DEPENDSON)
                        .setResource(relatedLibrary.getIdElement().getResourceType() + "/"
                                + relatedLibrary.getIdElement().getIdPart()));
            }
        }

        if (elm.getUsings() != null && !elm.getUsings().getDef().isEmpty()) {
            for (org.hl7.elm.r1.UsingDef def : elm.getUsings().getDef()) {
                String uri = def.getUri();
                String version = def.getVersion();
                if (version != null && !version.isEmpty()) {
                    uri = uri + "|" + version;
                }
                library.addRelatedArtifact(
                        new RelatedArtifact().setType(RelatedArtifact.RelatedArtifactType.DEPENDSON).setUrl(uri));
            }
        }
    }

    public void ensureDataRequirements(org.hl7.fhir.r4.model.Library library, CqlTranslator translator) {
        library.getDataRequirement().clear();

        List<DataRequirement> reqs = new ArrayList<>();

        for (org.hl7.elm.r1.Retrieve retrieve : translator.toRetrieves()) {
            DataRequirement dataReq = new DataRequirement();
            dataReq.setType(retrieve.getDataType().getLocalPart());
            if (retrieve.getCodeProperty() != null) {
                DataRequirement.DataRequirementCodeFilterComponent codeFilter = new DataRequirement.DataRequirementCodeFilterComponent();
                codeFilter.setPath(retrieve.getCodeProperty());
                if (retrieve.getCodes() instanceof ValueSetRef) {
                    codeFilter.setValueSet(getValueSetId(((ValueSetRef) retrieve.getCodes()).getName(), translator));
                }
                dataReq.setCodeFilter(Collections.singletonList(codeFilter));
            }
            // TODO - Date filters - we want to populate this with a $data-requirements
            // request as there isn't a good way through elm analysis
            reqs.add(dataReq);
        }

        //
        // org.hl7.elm.r1.Library elm = translator.toELM();
        // Codes codes = elm.getCodes();
        // for (CodeDef cd : codes.getDef()) {
        // cd.
        // }

        library.setDataRequirement(reqs);
    }

    public String getValueSetId(String valueSetName, CqlTranslator translator) {
        org.hl7.elm.r1.Library.ValueSets valueSets = translator.toELM().getValueSets();
        if (valueSets != null) {
            for (org.hl7.elm.r1.ValueSetDef def : valueSets.getDef()) {
                if (def.getName().equals(valueSetName)) {
                    return def.getId();
                }
            }
        }

        return valueSetName;
    }

}
