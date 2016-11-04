// Copyright 2016 Yahoo Inc. Licensed under the terms of the Apache 2.0 license. See LICENSE in the project root.
package com.yahoo.vespa.model.admin.monitoring;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This class sets up the default metrics and the default 'vespa' metrics consumer.
 *
 * TODO: remove for Vespa 7 or when the 'metric-consumers' element in 'admin' has been removed.
 *
 * @author <a href="mailto:trygve@yahoo-inc.com">Trygve Bolsø Berdal</a>
 * @author gjoranv
 */
@SuppressWarnings("UnusedDeclaration") // All public apis are used by model amenders
public class DefaultMetricConsumers {

    private final MetricSet vespaMetricSet;

    public DefaultMetricConsumers(MetricSet vespaMetricSet) {
        this.vespaMetricSet = vespaMetricSet;
    }

    /**
     * Populates a map of with consumer as key and metrics for that consumer as value. The metrics
     * are to be forwarded to consumers.
     *
     * @return A map of default metric consumers and default metrics for that consumer.
     */
    @SuppressWarnings("UnusedDeclaration")
    public Map<String, MetricsConsumer> getDefaultMetricConsumers() {
        Map<String, MetricsConsumer> metricsConsumers = new LinkedHashMap<>();
        metricsConsumers.put("yamas", getVespaConsumer());
        return metricsConsumers;
    }

    private MetricsConsumer getVespaConsumer(){
        return new MetricsConsumer("yamas", vespaMetricSet);
    }

}
