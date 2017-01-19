package ca.uhn.fhir.narrative.template;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.antlr.runtime.ANTLRFileStream;
import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.CommonTreeNodeStream;

import ca.uhn.fhir.narrative.template.filters.Filter;
import ca.uhn.fhir.narrative.template.nodes.LNode;
import ca.uhn.fhir.narrative.template.nodes.LiquidWalker;
import ca.uhn.fhir.narrative.template.parser.Flavor;
import ca.uhn.fhir.narrative.template.parser.LiquidLexer;
import ca.uhn.fhir.narrative.template.parser.LiquidParser;
import ca.uhn.fhir.narrative.template.tags.Tag;

/**
 * The main class of this library. Use one of its static
 * <code>parse(...)</code> to get a hold of a reference.
 * <p/>
 * Also see: https://github.com/Shopify/liquid
 */
public class Template {

    /**
     * The root of the AST denoting the Liquid input source.
     */
    private final CommonTree root;

    /**
     * This instance's tags.
     */
    private final Map<String, Tag> tags;

    /**
     * This instance's filters.
     */
    private final Map<String, Filter> filters;

    private final Flavor flavor;

    /**
     * Creates a new Template instance from a given input.
     *  @param input
     *         the file holding the Liquid source.
     * @param tags
     *         the tags this instance will make use of.
     * @param filters
     *         the filters this instance will make use of.
     */
    private Template(String input, Map<String, Tag> tags, Map<String, Filter> filters) {
        this(input, tags, filters, Flavor.LIQUID);
    }

    private Template(String input, Map<String, Tag> tags, Map<String, Filter> filters, Flavor flavor) {

        this.tags = tags;
        this.filters = filters;
        this.flavor = flavor;

        LiquidLexer lexer = new LiquidLexer(new ANTLRStringStream(input));
        LiquidParser parser = new LiquidParser(flavor, new CommonTokenStream(lexer));

        try {
            root = (CommonTree) parser.parse().getTree();
        }
        catch (RecognitionException e) {
            throw new RuntimeException("could not parse input: " + input, e);
        }
    }

    /**
     * Creates a new Template instance from a given file.
     *
     * @param file
     *         the file holding the Liquid source.
     */
    private Template(File file, Map<String, Tag> tags, Map<String, Filter> filters) throws IOException {
        this(file, tags, filters, Flavor.LIQUID);
    }

    private Template(File file, Map<String, Tag> tags, Map<String, Filter> filters, Flavor flavor) throws IOException {

        this.tags = tags;
        this.filters = filters;
        this.flavor = flavor;

        try {
            LiquidLexer lexer = new LiquidLexer(new ANTLRFileStream(file.getAbsolutePath()));
            LiquidParser parser = new LiquidParser(flavor, new CommonTokenStream(lexer));
            root = (CommonTree) parser.parse().getTree();
        }
        catch (RecognitionException e) {
            throw new RuntimeException("could not parse input from " + file, e);
        }
    }

    /**
     * Returns the root of the AST of the parsed input.
     *
     * @return the root of the AST of the parsed input.
     */
    public CommonTree getAST() {
        return root;
    }

    /**
     * Returns a new Template instance from a given input string.
     *
     * @param input
     *         the input string holding the Liquid source.
     *
     * @return a new Template instance from a given input string.
     */
    public static Template parse(String input) {
        return new Template(input, Tag.getTags(), Filter.getFilters());
    }

    /**
     * Returns a new Template instance from a given input file.
     *
     * @param file
     *         the input file holding the Liquid source.
     *
     * @return a new Template instance from a given input file.
     */
    public static Template parse(File file) throws IOException {
        return parse(file, Flavor.LIQUID);
    }

    public static Template parse(File file, Flavor flavor) throws IOException {
        return new Template(file, Tag.getTags(), Filter.getFilters(), flavor);
    }

    public Template with(Tag tag) {
        this.tags.put(tag.name, tag);
        return this;
    }

    public Template with(Filter filter) {
        this.filters.put(filter.name, filter);
        return this;
    }

   

   
    /**
     * Renders the template.
     *
     * @param context
     *         a Map denoting the (possibly nested)
     *         variables that can be used in this
     *         Template.
     *
     * @return a string denoting the rendered template.
     */
    public String render(Map<String, Object> context) {

        LiquidWalker walker = new LiquidWalker(new CommonTreeNodeStream(root), this.tags, this.filters, this.flavor);

        try {
            LNode node = walker.walk();
            Object rendered = node.render(context);
            return rendered == null ? "" : String.valueOf(rendered);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns a string representation of the AST of the parsed
     * input source.
     *
     * @return a string representation of the AST of the parsed
     *         input source.
     */
    public String toStringAST() {

        StringBuilder builder = new StringBuilder();

        walk(root, builder);

        return builder.toString();
    }

    /**
     * Walks a (sub) tree of the root of the input source and builds
     * a string representation of the structure of the AST.
     * <p/>
     * Note that line breaks and multiple white space characters are
     * trimmed to a single white space character.
     *
     * @param tree
     *         the (sub) tree.
     * @param builder
     *         the StringBuilder to fill.
     */
    @SuppressWarnings("unchecked")
    private void walk(CommonTree tree, StringBuilder builder) {

        List<CommonTree> firstStack = new ArrayList<CommonTree>();
        firstStack.add(tree);

        List<List<CommonTree>> childListStack = new ArrayList<List<CommonTree>>();
        childListStack.add(firstStack);

        while (!childListStack.isEmpty()) {

            List<CommonTree> childStack = childListStack.get(childListStack.size() - 1);

            if (childStack.isEmpty()) {
                childListStack.remove(childListStack.size() - 1);
            }
            else {
                tree = childStack.remove(0);

                String indent = "";

                for (int i = 0; i < childListStack.size() - 1; i++) {
                    indent += (childListStack.get(i).size() > 0) ? "|  " : "   ";
                }

                String tokenName = LiquidParser.tokenNames[tree.getType()];
                String tokenText = tree.getText().replaceAll("\\s+", " ").trim();

                builder.append(indent)
                        .append(childStack.isEmpty() ? "'- " : "|- ")
                        .append(tokenName)
                        .append(!tokenName.equals(tokenText) ? "='" + tokenText + "'" : "")
                        .append("\n");

                if (tree.getChildCount() > 0) {
                    childListStack.add(new ArrayList<CommonTree>((List<CommonTree>) tree.getChildren()));
                }
            }
        }
    }
}
