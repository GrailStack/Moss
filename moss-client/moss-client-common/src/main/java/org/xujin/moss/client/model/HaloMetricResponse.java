package org.xujin.moss.client.model;

public class HaloMetricResponse {

    private String jvmThreadslive;
    private String jvmMemoryUsedHeap;
    private String jvmMemoryUsedNonHeap;

    private String heapCommitted;

    private String nonheapCommitted;

    private String heapInit;
    private String heapMax;

    private String gcPsMarksweepCount;

    private String gcPsMarksweepTime;

    private String gcPsScavengeCount;

    private String gcPsScavengeTime;

    private String systemloadAverage;

    private String processors;

    public String getJvmThreadslive() {
        return jvmThreadslive;
    }

    public void setJvmThreadslive(String jvmThreadslive) {
        this.jvmThreadslive = jvmThreadslive;
    }

    public String getJvmMemoryUsedHeap() {
        return jvmMemoryUsedHeap;
    }

    public void setJvmMemoryUsedHeap(String jvmMemoryUsedHeap) {
        this.jvmMemoryUsedHeap = jvmMemoryUsedHeap;
    }

    public String getJvmMemoryUsedNonHeap() {
        return jvmMemoryUsedNonHeap;
    }

    public void setJvmMemoryUsedNonHeap(String jvmMemoryUsedNonHeap) {
        this.jvmMemoryUsedNonHeap = jvmMemoryUsedNonHeap;
    }

    public String getHeapCommitted() {
        return heapCommitted;
    }

    public void setHeapCommitted(String heapCommitted) {
        this.heapCommitted = heapCommitted;
    }

    public String getNonheapCommitted() {
        return nonheapCommitted;
    }

    public void setNonheapCommitted(String nonheapCommitted) {
        this.nonheapCommitted = nonheapCommitted;
    }

    public String getHeapInit() {
        return heapInit;
    }

    public void setHeapInit(String heapInit) {
        this.heapInit = heapInit;
    }

    public String getHeapMax() {
        return heapMax;
    }

    public void setHeapMax(String heapMax) {
        this.heapMax = heapMax;
    }

    public String getGcPsMarksweepCount() {
        return gcPsMarksweepCount;
    }

    public void setGcPsMarksweepCount(String gcPsMarksweepCount) {
        this.gcPsMarksweepCount = gcPsMarksweepCount;
    }

    public String getGcPsMarksweepTime() {
        return gcPsMarksweepTime;
    }

    public void setGcPsMarksweepTime(String gcPsMarksweepTime) {
        this.gcPsMarksweepTime = gcPsMarksweepTime;
    }

    public String getGcPsScavengeCount() {
        return gcPsScavengeCount;
    }

    public void setGcPsScavengeCount(String gcPsScavengeCount) {
        this.gcPsScavengeCount = gcPsScavengeCount;
    }

    public String getGcPsScavengeTime() {
        return gcPsScavengeTime;
    }

    public void setGcPsScavengeTime(String gcPsScavengeTime) {
        this.gcPsScavengeTime = gcPsScavengeTime;
    }

    public String getSystemloadAverage() {
        return systemloadAverage;
    }

    public void setSystemloadAverage(String systemloadAverage) {
        this.systemloadAverage = systemloadAverage;
    }

    public String getProcessors() {
        return processors;
    }

    public void setProcessors(String processors) {
        this.processors = processors;
    }
}
