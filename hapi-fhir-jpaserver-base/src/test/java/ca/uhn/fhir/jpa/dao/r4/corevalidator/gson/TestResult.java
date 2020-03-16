package ca.uhn.fhir.jpa.dao.r4.corevalidator.gson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import net.sf.saxon.value.IntegerRange;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TestResult {

    public static final int NO_WARNING = Integer.MIN_VALUE;

    @SerializedName("errorCount")
    @Expose
    private Integer errorCount = 0;
    @SerializedName("warningCount")
    @Expose
    private Integer warningCount = NO_WARNING;
    @SerializedName("output")
    @Expose
    private List<String> output = new ArrayList<>();

    public Integer getErrorCount() {
        return errorCount;
    }

    public TestResult setErrorCount(Integer errorCount) {
        this.errorCount = errorCount;
        return this;
    }

    public Integer getWarningCount() {
        return warningCount;
    }

    public TestResult setWarningCount(Integer warningCount) {
        this.warningCount = warningCount;
        return this;
    }

    public List<String> getOutput() {
        return output;
    }

    public TestResult setOutput(List<String> output) {
        this.output = output;
        return this;
    }

    @Override
    public String toString() {
        return "TestResult {" +
                "\nerrorCount=" + errorCount +
                (warningCount == NO_WARNING ? "" : "\nwarningCount=" + warningCount) +
                "\noutput=\n" + output.stream().collect(Collectors.joining("\n")) +
                "\n}";
    }
}