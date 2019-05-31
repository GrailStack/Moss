package org.xujin.moss.client.endpoint;

import org.xujin.moss.client.model.HaloMetricResponse;
import org.springframework.boot.actuate.endpoint.AbstractEndpoint;
import org.springframework.boot.actuate.endpoint.Endpoint;
import org.springframework.boot.actuate.endpoint.PublicMetrics;
import org.springframework.boot.actuate.metrics.Metric;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.util.Assert;

import java.util.*;

/**
 * {@link Endpoint} to expose a collection of {@link PublicMetrics}.
 *
 * @author Dave Syer
 */
@ConfigurationProperties(prefix = "endpoints.metricsInfo")
public class MossMetricsEndpoint extends AbstractEndpoint<HaloMetricResponse> {

    private final List<PublicMetrics> publicMetrics;

    public static final String  THREADS="threads";

    public static final String  NON_HEAP_Used="nonheap.used";

    public static final String  HEAP_USED="heap.used";

    public static final String  HEAP_COMMITTED="heap.committed";

    public static final String  HEAP_INIT="heap.init";

    public static final String  HEAP_MAX="heap";

    public static final String  GC_PS_MARKSWEEP_COUNT="gc.ps_marksweep.count";

    public static final String  GC_PS_MARKSWEEP_TIME="gc.ps_marksweep.time";


    public static final String  GC_PS_SCAVENGE_COUNT="gc.ps_scavenge.count";

    public static final String  GC_PS_SCAVENGE_TIME="gc.ps_scavenge.time";

    public static final String  NONHEAP_COMMITTED="nonheap.committed";

    public static final String  SYSTEMLOAD_AVERAGE="systemload.average";

    public static final String  PROCESSORS="processors";


    /**
     * Create a new {@link org.springframework.boot.actuate.endpoint.MetricsEndpoint} instance.
     * @param publicMetrics the metrics to expose
     */
    public MossMetricsEndpoint(PublicMetrics publicMetrics) {
        this(Collections.singleton(publicMetrics));
    }

    /**
     * Create a new {@link org.springframework.boot.actuate.endpoint.MetricsEndpoint} instance.
     * @param publicMetrics the metrics to expose. The collection will be sorted using the
     * {@link AnnotationAwareOrderComparator}.
     */
    public MossMetricsEndpoint(Collection<PublicMetrics> publicMetrics) {
        super("metricsInfo");
        Assert.notNull(publicMetrics, "PublicMetrics must not be null");
        this.publicMetrics = new ArrayList<PublicMetrics>(publicMetrics);
        AnnotationAwareOrderComparator.sort(this.publicMetrics);
    }

    public void registerPublicMetrics(PublicMetrics metrics) {
        this.publicMetrics.add(metrics);
        AnnotationAwareOrderComparator.sort(this.publicMetrics);
    }

    public void unregisterPublicMetrics(PublicMetrics metrics) {
        this.publicMetrics.remove(metrics);
    }

    @Override
    public HaloMetricResponse invoke() {
        HaloMetricResponse haloMetricResponse=new HaloMetricResponse();
        Map<String, Object> result = new LinkedHashMap<String, Object>();
        List<PublicMetrics> metrics = new ArrayList<PublicMetrics>(this.publicMetrics);
        for (PublicMetrics publicMetric : metrics) {
            try {
                for (Metric<?> metric : publicMetric.metrics()) {
                    if(THREADS.equals(metric.getName())){
                        haloMetricResponse.setJvmThreadslive(String.valueOf(metric.getValue()));
                    }
                    if(NON_HEAP_Used.equals(metric.getName())){
                        haloMetricResponse.setJvmMemoryUsedNonHeap(String.valueOf(metric.getValue()));
                    }
                    if(HEAP_USED.equals(metric.getName())){
                        haloMetricResponse.setJvmMemoryUsedHeap(String.valueOf(metric.getValue()));
                    }
                    if(HEAP_COMMITTED.equals(metric.getName())){
                        haloMetricResponse.setHeapCommitted(String.valueOf(metric.getValue()));
                    }
                    if(HEAP_INIT.equals(metric.getName())){
                        haloMetricResponse.setHeapInit(String.valueOf(metric.getValue()));
                    }
                    if(HEAP_MAX.equals(metric.getName())){
                        haloMetricResponse.setHeapMax(String.valueOf(metric.getValue()));
                    }
                    getGcInfo(haloMetricResponse, metric);
                    if(NONHEAP_COMMITTED.equals(metric.getName())){
                        haloMetricResponse.setNonheapCommitted(String.valueOf(metric.getValue()));
                    }
                    if(SYSTEMLOAD_AVERAGE.equals(metric.getName())){
                        haloMetricResponse.setSystemloadAverage(String.valueOf(metric.getValue()));
                    }
                    if(PROCESSORS.equals(metric.getName())){
                        haloMetricResponse.setProcessors(String.valueOf(metric.getValue()));
                    }



                }
            }
            catch (Exception ex) {
                // Could not evaluate metrics
            }
        }
        return haloMetricResponse;
    }

    /**
     * Get GC Info兼容Java 8和Java 10
     * @param haloMetricResponse
     * @param metric
     */
    private void getGcInfo(HaloMetricResponse haloMetricResponse, Metric<?> metric) {
        if(GC_PS_MARKSWEEP_COUNT.equals(metric.getName())||"gc.g1_old_generation.count".equals(metric.getName())){
            haloMetricResponse.setGcPsMarksweepCount(String.valueOf(metric.getValue()));
        }
        if(GC_PS_MARKSWEEP_TIME.equals(metric.getName())||"gc.g1_old_generation.time".equals(metric.getName())){
            haloMetricResponse.setGcPsMarksweepTime(String.valueOf(metric.getValue()));
        }
        if(GC_PS_SCAVENGE_TIME.equals(metric.getName())||"gc.g1_young_generation.time".equals(metric.getName())){
            haloMetricResponse.setGcPsScavengeTime(String.valueOf(metric.getValue()));
        }
        if(GC_PS_SCAVENGE_COUNT.equals(metric.getName())||"gc.g1_young_generation.count".equals(metric.getName())){
            haloMetricResponse.setGcPsScavengeCount(String.valueOf(metric.getValue()));
        }
    }

}