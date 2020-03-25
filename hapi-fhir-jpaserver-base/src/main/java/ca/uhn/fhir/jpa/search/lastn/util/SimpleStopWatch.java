package ca.uhn.fhir.jpa.search.lastn.util;

public class SimpleStopWatch {
    private long myStarted = System.currentTimeMillis();

    public SimpleStopWatch() {

    }

    public long getElapsedTime() {
        return System.currentTimeMillis() - myStarted;
    }

    public void restart() {
        myStarted = System.currentTimeMillis();
    }

}
