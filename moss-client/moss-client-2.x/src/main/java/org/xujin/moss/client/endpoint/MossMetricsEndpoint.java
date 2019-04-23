package org.xujin.moss.client.endpoint;

import org.xujin.moss.client.model.HaloMetricResponse;
import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Statistic;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.composite.CompositeMeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.InvalidEndpointRequestException;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryUsage;
import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * 获取用于展示数据的端点
 * @author xujin
 */
@Endpoint(id = "metricsInfo")
public class MossMetricsEndpoint {

    @Autowired
    private MeterRegistry registry;

    private void collectNames(Set<String> names, MeterRegistry registry) {
        if (registry instanceof CompositeMeterRegistry) {
            ((CompositeMeterRegistry) registry).getRegistries()
                    .forEach((member) -> collectNames(names, member));
        }
        else {
            registry.getMeters().stream().map(this::getName).forEach(names::add);
        }
    }

    private String getName(Meter meter) {
        return meter.getId().getName();
    }

    @ReadOperation
    public HaloMetricResponse HaloMetric() {
        HaloMetricResponse haloMetricResponse=new HaloMetricResponse();
        MetricResponse jvmThreadsLive=metric("jvm.threads.live",null);
        haloMetricResponse.setJvmThreadslive(String.valueOf(jvmThreadsLive.getMeasurements().get(0).getValue()));
        MetricResponse jvmNemoryUsedHeap=metric("jvm.memory.used", Arrays.asList("area:heap") );
        haloMetricResponse.setJvmMemoryUsedHeap(String.valueOf(jvmNemoryUsedHeap.getMeasurements().get(0).getValue()));
        MetricResponse jvmNemoryUsedNonHeap=metric("jvm.memory.used", Arrays.asList("area:nonheap") );
        haloMetricResponse.setJvmMemoryUsedNonHeap(String.valueOf(jvmNemoryUsedNonHeap.getMeasurements().get(0).getValue()));

        MetricResponse systemLoadAverage=metric("system.load.average.1m", null );
        haloMetricResponse.setSystemloadAverage(String.valueOf(systemLoadAverage.getMeasurements().get(0).getValue()));

        MetricResponse heapCommitted=metric("jvm.memory.committed", Arrays.asList("area:heap") );
        haloMetricResponse.setHeapCommitted(String.valueOf(heapCommitted.getMeasurements().get(0).getValue()));
        MetricResponse nonheapCommitted=metric("jvm.memory.committed", Arrays.asList("area:nonheap") );
        haloMetricResponse.setNonheapCommitted(String.valueOf(nonheapCommitted.getMeasurements().get(0).getValue()));

        MetricResponse heapMax=metric("jvm.memory.max", Arrays.asList("area:heap") );
        haloMetricResponse.setHeapMax(String.valueOf(heapMax.getMeasurements().get(0).getValue()));


        getGcinfo(haloMetricResponse);
        MemoryUsage memoryUsage = ManagementFactory.getMemoryMXBean()
                .getHeapMemoryUsage();
        haloMetricResponse.setHeapInit(String.valueOf(memoryUsage.getInit()));
        Runtime runtime = Runtime.getRuntime();
        haloMetricResponse.setProcessors(String.valueOf(runtime.availableProcessors()));
        return haloMetricResponse;

    }

    /**
     * 兼容Java 8和Java 10获取GC信息
     * @param haloMetricResponse
     */
    private void getGcinfo(HaloMetricResponse haloMetricResponse) {
        List<GarbageCollectorMXBean> garbageCollectorMxBeans = ManagementFactory.getGarbageCollectorMXBeans();
        for (GarbageCollectorMXBean garbageCollectorMXBean : garbageCollectorMxBeans) {
            String name = beautifyGcName(garbageCollectorMXBean.getName());
            String gcCount="gc." + name + ".count";
            String gcTime="gc." + name + ".time";
            if(gcCount.equals("gc.ps_scavenge.count")||gcCount.equals("gc.g1_young_generation.count")){
                haloMetricResponse.setGcPsScavengeCount(String.valueOf(garbageCollectorMXBean.getCollectionCount()));
            }
            if(gcTime.equals("gc.ps_scavenge.time")||gcTime.equals("gc.g1_young_generation.time")){
                 haloMetricResponse.setGcPsScavengeTime(String.valueOf(garbageCollectorMXBean.getCollectionTime()));
            }
            if(gcCount.equals("gc.ps_marksweep.count")||gcCount.equals("gc.g1_old_generation.count")){
                haloMetricResponse.setGcPsMarksweepCount(String.valueOf(garbageCollectorMXBean.getCollectionCount()));
            }
            if(gcTime.equals("gc.ps_marksweep.time")||gcTime.equals("gc.g1_old_generation.time")){
                haloMetricResponse.setGcPsMarksweepTime(String.valueOf(garbageCollectorMXBean.getCollectionTime()));
            }
        }
    }

    private String beautifyGcName(String name) {
        return StringUtils.replace(name, " ", "_").toLowerCase();
    }

    public MetricResponse metric(String requiredMetricName, @Nullable List<String> tag) {
        List<Tag> tags = parseTags(tag);
        Collection<Meter> meters = findFirstMatchingMeters(this.registry,
                requiredMetricName, tags);
        if (meters.isEmpty()) {
            return null;
        }
        Map<Statistic, Double> samples = getSamples(meters);
        Map<String, Set<String>> availableTags = getAvailableTags(meters);
        tags.forEach((t) -> availableTags.remove(t.getKey()));
        Meter.Id meterId = meters.iterator().next().getId();
        return new MetricResponse(requiredMetricName, meterId.getDescription(),
                meterId.getBaseUnit(), asList(samples, Sample::new),
                asList(availableTags, AvailableTag::new));


    }

    private List<Tag> parseTags(List<String> tags) {
        if (tags == null) {
            return Collections.emptyList();
        }
        return tags.stream().map(this::parseTag).collect(Collectors.toList());
    }

    private Tag parseTag(String tag) {
        String[] parts = tag.split(":", 2);
        if (parts.length != 2) {
            throw new InvalidEndpointRequestException(
                    "Each tag parameter must be in the form 'key:value' but was: " + tag,
                    "Each tag parameter must be in the form 'key:value'");
        }
        return Tag.of(parts[0], parts[1]);
    }

    private Collection<Meter> findFirstMatchingMeters(MeterRegistry registry, String name,
                                                      Iterable<Tag> tags) {
        if (registry instanceof CompositeMeterRegistry) {
            return findFirstMatchingMeters((CompositeMeterRegistry) registry, name, tags);
        }
        return registry.find(name).tags(tags).meters();
    }

    private Collection<Meter> findFirstMatchingMeters(CompositeMeterRegistry composite,
                                                      String name, Iterable<Tag> tags) {
        return composite.getRegistries().stream()
                .map((registry) -> findFirstMatchingMeters(registry, name, tags))
                .filter((matching) -> !matching.isEmpty()).findFirst()
                .orElse(Collections.emptyList());
    }

    private Map<Statistic, Double> getSamples(Collection<Meter> meters) {
        Map<Statistic, Double> samples = new LinkedHashMap<>();
        meters.forEach((meter) -> mergeMeasurements(samples, meter));
        return samples;
    }

    private void mergeMeasurements(Map<Statistic, Double> samples, Meter meter) {
        meter.measure().forEach((measurement) -> samples.merge(measurement.getStatistic(),
                measurement.getValue(), mergeFunction(measurement.getStatistic())));
    }

    private BiFunction<Double, Double, Double> mergeFunction(Statistic statistic) {
        return Statistic.MAX.equals(statistic) ? Double::max : Double::sum;
    }

    private Map<String, Set<String>> getAvailableTags(Collection<Meter> meters) {
        Map<String, Set<String>> availableTags = new HashMap<>();
        meters.forEach((meter) -> mergeAvailableTags(availableTags, meter));
        return availableTags;
    }

    private void mergeAvailableTags(Map<String, Set<String>> availableTags, Meter meter) {
        meter.getId().getTags().forEach((tag) -> {
            Set<String> value = Collections.singleton(tag.getValue());
            availableTags.merge(tag.getKey(), value, this::merge);
        });
    }

    private <T> Set<T> merge(Set<T> set1, Set<T> set2) {
        Set<T> result = new HashSet<>(set1.size() + set2.size());
        result.addAll(set1);
        result.addAll(set2);
        return result;
    }

    private <K, V, T> List<T> asList(Map<K, V> map, BiFunction<K, V, T> mapper) {
        return map.entrySet().stream()
                .map((entry) -> mapper.apply(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    /**
     * Response payload for a metric name listing.
     */
    public static final class ListNamesResponse {

        private final Set<String> names;

        ListNamesResponse(Set<String> names) {
            this.names = names;
        }

        public Set<String> getNames() {
            return this.names;
        }

    }

    /**
     * Response payload for a metric name selector.
     */
    public static final class MetricResponse {

        private final String name;

        private final String description;

        private final String baseUnit;

        private final List<Sample> measurements;

        private final List<AvailableTag> availableTags;

        MetricResponse(String name, String description, String baseUnit,
                       List<Sample> measurements, List<AvailableTag> availableTags) {
            this.name = name;
            this.description = description;
            this.baseUnit = baseUnit;
            this.measurements = measurements;
            this.availableTags = availableTags;
        }

        public String getName() {
            return this.name;
        }

        public String getDescription() {
            return this.description;
        }

        public String getBaseUnit() {
            return this.baseUnit;
        }

        public List<Sample> getMeasurements() {
            return this.measurements;
        }

        public List<AvailableTag> getAvailableTags() {
            return this.availableTags;
        }

    }

    /**
     * A set of tags for further dimensional drilldown and their potential values.
     */
    public static final class AvailableTag {

        private final String tag;

        private final Set<String> values;

        AvailableTag(String tag, Set<String> values) {
            this.tag = tag;
            this.values = values;
        }

        public String getTag() {
            return this.tag;
        }

        public Set<String> getValues() {
            return this.values;
        }

    }

    /**
     * A measurement sample combining a {@link Statistic statistic} and a value.
     */
    public static final class Sample {

        private final Statistic statistic;

        private final Double value;

        Sample(Statistic statistic, Double value) {
            this.statistic = statistic;
            this.value = value;
        }

        public Statistic getStatistic() {
            return this.statistic;
        }

        public Double getValue() {
            return this.value;
        }

        @Override
        public String toString() {
            return "MeasurementSample{" + "statistic=" + this.statistic + ", value="
                    + this.value + '}';
        }

    }



}