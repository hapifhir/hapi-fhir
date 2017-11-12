package ca.uhn.fhir.jpa.cqf.ruler.helpers;

import ca.uhn.fhir.context.FhirContext;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hl7.fhir.dstu3.model.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class XlsxToValueSet {

    // Default indexes and output directory path
    private static boolean valueSet = true;
    private static int startLine = 0;
    private static int oidCol = 0;
    private static int systemCol = 1;
    private static int versionCol = 2;
    private static int codeCol = 3;
    private static int displayCol = 4;

    private static int urlCol = 1;

    private static String outDir = "src/main/resources/valuesets/";

    private static final String validFlags = "-b (line to start conversion)\n-o (oid column index)\n-s (code system column index)\n-v (version column index)\n-c (code column index)\n-d (display column index)\n-u (url column index) only used for CodeSystem conversion";

    private static Map<String, ValueSet> valuesets = new HashMap<>();
    private static Map<String, CodeSystem> codeSystems = new HashMap<>();

    private static void populateOidVs(String oid) {
        ValueSet vs = new ValueSet();
        vs.setId(oid);
        vs.setStatus(Enumerations.PublicationStatus.DRAFT);
        valuesets.put(oid, vs);
    }

    private static void populateOidCs(String oid) {
        CodeSystem cs = new CodeSystem();
        cs.setId(oid);
        cs.setStatus(Enumerations.PublicationStatus.DRAFT);
        cs.setContent(CodeSystem.CodeSystemContentMode.EXAMPLE);
        codeSystems.put(oid, cs);
    }

    private static String getNextCellString(Cell cell) {
        cell.setCellType(CellType.STRING);
        return cell.getStringCellValue();
    }

    // default format: oid ... system ... version ... code ... display
    private static void resolveRowVs(Row row) {
        String oid = getNextCellString(row.getCell(oidCol));

        if (!valuesets.containsKey(oid)) {
            populateOidVs(oid);
        }

        String system = getNextCellString(row.getCell(systemCol));
        String version = getNextCellString(row.getCell(versionCol));
        String code = getNextCellString(row.getCell(codeCol));
        String display = getNextCellString(row.getCell(displayCol));

        ValueSet.ValueSetComposeComponent vscc = valuesets.get(oid).getCompose();
        ValueSet.ConceptSetComponent component = new ValueSet.ConceptSetComponent();
        component.setSystem(system).setVersion(version);
        component.addConcept(new ValueSet.ConceptReferenceComponent().setCode(code).setDisplay(display));
        vscc.addInclude(component);
    }

    // default format: oid ... url ... version ... code ... display
    private static void resolveRowCs(Row row) {
        String oid = getNextCellString(row.getCell(oidCol));

        if (!codeSystems.containsKey(oid)) {
            populateOidCs(oid);
        }

        String url = getNextCellString(row.getCell(urlCol));
        String version = getNextCellString(row.getCell(versionCol));
        String code = getNextCellString(row.getCell(codeCol));
        String display = getNextCellString(row.getCell(displayCol));

        CodeSystem cs = codeSystems.get(oid);
        cs.setUrl(url).setVersion(version);
        cs.getConcept().add(new CodeSystem.ConceptDefinitionComponent().setCode(code).setDisplay(display));
    }

    private static Bundle valuesetBundle() {
        Bundle temp = new Bundle();
        for (String key : valuesets.keySet())
            temp.addEntry(new Bundle.BundleEntryComponent().setResource(valuesets.get(key)));

        return temp;
    }

    private static Bundle codesystemBundle() {
        Bundle temp = new Bundle();
        for (String key : codeSystems.keySet())
            temp.addEntry(new Bundle.BundleEntryComponent().setResource(codeSystems.get(key)));

        return temp;
    }

    // library function use
    public static Bundle convertCs(Workbook workbook, String[] args) {
        resolveArgs(args);
        Iterator<Row> rowIterator = workbook.getSheetAt(0).iterator();

        int currentRow = 0;
        while (currentRow++ != startLine) {
            rowIterator.next();
        }

        while (rowIterator.hasNext()) {
            resolveRowCs(rowIterator.next());
        }

        return codesystemBundle();
    }

    public static Bundle convertCs(String[] args) {
        return convertCs(getWorkbook(args[0]), args);
    }

    public static Bundle convertVs(Workbook workbook, String[] args) {
        resolveArgs(args);
        Iterator<Row> rowIterator = workbook.getSheetAt(0).iterator();

        int currentRow = 0;
        while (currentRow++ != startLine) {
            rowIterator.next();
        }

        while (rowIterator.hasNext()) {
            resolveRowVs(rowIterator.next());
        }

        return valuesetBundle();
    }

    public static Bundle convertVs(String[] args) {
        return convertVs(getWorkbook(args[0]), args);
    }

    public static Workbook getWorkbook(String workbookPath) {
        Workbook workbook = null;
        try {
            FileInputStream spreadsheetStream = new FileInputStream(new File(workbookPath));
            workbook = new XSSFWorkbook(spreadsheetStream);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return workbook;
    }

    private static void resolveArgs(String[] args) {
        for (String arg : args) {
            String[] flagAndValue = arg.split("=");

            switch (flagAndValue[0]) {
                case "-b": startLine = Integer.parseInt(flagAndValue[1]); break;
                case "-o": oidCol = Integer.parseInt(flagAndValue[1]); break;
                case "-s": systemCol = Integer.parseInt(flagAndValue[1]); break;
                case "-v": versionCol = Integer.parseInt(flagAndValue[1]); break;
                case "-c": codeCol = Integer.parseInt(flagAndValue[1]); break;
                case "-d": displayCol = Integer.parseInt(flagAndValue[1]); break;
                case "-u": urlCol = Integer.parseInt(flagAndValue[1]); break;
                case "-outDir": outDir = flagAndValue[1]; break;
                case "-cs": valueSet = false; break;
                default:
                    if (flagAndValue[0].startsWith("-")) {
                        throw new IllegalArgumentException(String.format("Invalid flag: %s\n%s", flagAndValue[0], validFlags));
                    }
                    break;
            }
        }
    }

    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            System.err.println("Path to excel file is required");
            return;
        }

        if (args.length > 1) {
            resolveArgs(args);
        }

        Bundle temp = valueSet ? convertVs(args) : convertCs(args);
        FhirContext context = FhirContext.forDstu3();
        for (Bundle.BundleEntryComponent component : temp.getEntry()) {
            File f = new File(outDir + component.getResource().getId() + ".json");
            if (f.createNewFile()) {
                PrintWriter writer = new PrintWriter(f);
                writer.println(context.newJsonParser().setPrettyPrint(true).encodeResourceToString(component.getResource()));
                writer.println();
                writer.close();
            }
        }
    }
}
