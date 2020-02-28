package ca.uhn.fhir.jpa.dao.r4.corevalidator;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TestResult {

    @SerializedName("errorCount")
    @Expose
    private Integer errorCount = 0;
    @SerializedName("warningCount")
    @Expose
    private Integer warningCount = 0;
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