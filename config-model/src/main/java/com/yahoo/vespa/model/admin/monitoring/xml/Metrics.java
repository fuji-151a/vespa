// Copyright 2016 Yahoo Inc. Licensed under the terms of the Apache 2.0 license. See LICENSE in the project root.
package com.yahoo.vespa.model.admin.monitoring.xml;

import com.yahoo.vespa.model.admin.monitoring.MetricsConsumer;

import java.util.Map;

/**
 * Represents the user defined metrics and consumers from services.xml
 * GVL TODO: remove?
 *
 * @author gjoranv
 */
public class Metrics {

    private Map<String, MetricsConsumer> consumers;

}
