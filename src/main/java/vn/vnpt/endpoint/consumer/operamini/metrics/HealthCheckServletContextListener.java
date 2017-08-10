package vn.vnpt.endpoint.consumer.operamini.metrics;

import com.codahale.metrics.health.HealthCheckRegistry;
import com.codahale.metrics.health.SharedHealthCheckRegistries;
import com.codahale.metrics.servlets.HealthCheckServlet;

public class HealthCheckServletContextListener extends HealthCheckServlet.ContextListener {
    public static final HealthCheckRegistry HEALTH_CHECK_REGISTRY = SharedHealthCheckRegistries.getOrCreate("default");
    @Override
    protected HealthCheckRegistry getHealthCheckRegistry() {
        return HEALTH_CHECK_REGISTRY;
    }
}
