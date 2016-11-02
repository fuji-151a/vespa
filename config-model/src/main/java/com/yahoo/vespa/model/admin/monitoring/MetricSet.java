// Copyright 2016 Yahoo Inc. Licensed under the terms of the Apache 2.0 license. See LICENSE in the project root.
package com.yahoo.vespa.model.admin.monitoring;

import java.util.Set;

/**
 * @author gjoranv
 */
public interface MetricSet {

    String getId();

    Set<Metric> getMetrics();
}
