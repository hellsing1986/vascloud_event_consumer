package vn.vnpt.endpoint.consumer.operamini.metrics;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.SharedMetricRegistries;
import com.codahale.metrics.servlets.MetricsServlet;

public class MetricsServletContextListener extends MetricsServlet.ContextListener {
    public static final MetricRegistry METRIC_REGISTRY = SharedMetricRegistries.getOrCreate("default");
    @Override
    protected MetricRegistry getMetricRegistry() {
        return METRIC_REGISTRY;
    }
}
