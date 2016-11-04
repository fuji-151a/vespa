// Copyright 2016 Yahoo Inc. Licensed under the terms of the Apache 2.0 license. See LICENSE in the project root.
package com.yahoo.vespa.model.admin.monitoring;

import com.google.common.collect.Sets;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author gjoranv
 */
public class MetricSetTest {

    @Test
    public void internal_metrics_take_precedence() throws Exception {
        final String METRIC_NAME = "metric1";
        MetricSet child =  new MetricSet("set1", Sets.newHashSet(new Metric(METRIC_NAME, "child-output-name", "child-description")));
        MetricSet parent = new MetricSet("set1", Sets.newHashSet(new Metric(METRIC_NAME, "parent-output-name", "parent-description")),
                                         Sets.newHashSet(child));

        Metric combined = parent.getMetrics().get(METRIC_NAME);
        assertEquals("parent-output-name", combined.getOutputName());
        assertEquals("parent-description", combined.getDescription());
    }
}
