// Copyright 2016 Yahoo Inc. Licensed under the terms of the Apache 2.0 license. See LICENSE in the project root.
package com.yahoo.vespa.model.admin.monitoring;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Models a metric set containing a set of metrics and child metric sets.
 *
 * @author gjoranv
 */
public class MetricSet {

    private final String id;
    private final Map<String, Metric> metrics;
    private final Set<MetricSet> children;


    public MetricSet(String id, Set<Metric> metrics, Set<MetricSet> children) {
        Objects.requireNonNull(id, "Id cannot be null or empty.");

        this.id = id;
        this.metrics = toMapByName(metrics);
        this.children = children;
    }

    public MetricSet(String id, Set<Metric> metrics) {
        this(id, metrics, Collections.emptySet());
    }

    public final String getId() { return id; }

    /**
     * Returns all metrics in this set, including all metrics in any contained metric sets.
     * <br>
     * Joins this set's metrics with its child sets into a named flat map of metrics.
     * In the case of duplicate metrics, the metrics directly defined in this set
     * takes precedence with respect to output name, description and dimension value
     * (even if they are empty), while new dimensions from the children will be added.
     *
     * @return All metrics contained in this set.
     */
    public final Map<String, Metric> getMetrics() {
        return flatten(metrics, children);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MetricSet)) return false;

        MetricSet that = (MetricSet) o;

        return Objects.equals(id, that.id);

    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    private Map<String, Metric> flatten(Map<String, Metric> metrics, Set<MetricSet> children) {
        Map<String, Metric> joinedMetrics = new LinkedHashMap<>(metrics);

        for (MetricSet metricSet : children) {
            metricSet.getMetrics().forEach(
                    (name, metric) -> {
                        if (joinedMetrics.containsKey(name))
                            joinedMetrics.put(name, createCombinedMetric(joinedMetrics.get(name), metric));
                        else
                            joinedMetrics.put(name, metric);
                    });
        }
        return joinedMetrics;
    }

    private static Metric createCombinedMetric(Metric existing, Metric new1) {
        Map<String, String> dimensions = new LinkedHashMap<>(existing.getDimensions());
        new1.getDimensions().forEach(
                (k, v) -> {
                    if (!dimensions.containsKey(k)) dimensions.put(k, v);
                });

        return new Metric(existing.getName(), existing.getOutputName(), existing.getDescription(), dimensions);
    }

    private Map<String, Metric> toMapByName(Collection<Metric> metrics) {
        Map<String, Metric> metricMap = new LinkedHashMap<>();
        for (Metric metric : metrics) {
            metricMap.put(metric.getName(), metric);
        }
        return metricMap;
    }

}
