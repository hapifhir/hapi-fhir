package ca.uhn.fhir.jpa.dao.r4.corevalidator;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TestResult {

    @SerializedName("errorCount")
    @Expose
    private Integer errorCount;
    @SerializedName("warningCount")
    @Expose
    private Integer warningCount;
    @SerializedName("output")
    @Expose
    private List<String> output = null;

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

}