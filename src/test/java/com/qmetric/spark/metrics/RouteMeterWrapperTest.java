package com.qmetric.spark.metrics;

import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.junit.Before;
import org.junit.Test;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

import java.util.SortedMap;

import static com.qmetric.spark.metrics.SparkConstants.PORT;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class RouteMeterWrapperTest
{
    private SparkTestUtil sparkTestUtil;

    private MetricRegistry metricRegistry;

    @Before
    public void init()
    {
        metricRegistry = new MetricRegistry();
        Spark.get(new RouteMeterWrapper("/meter-wrapper", metricRegistry, new Route("/meter-wrapper")
        {
            @Override public Object handle(final Request request, final Response response)
            {
                return "";
            }
        }));

        sparkTestUtil = new SparkTestUtil(PORT);
    }

    @Test
    public void shouldMeterRequest()
    {
        for (int i = 0; i < 10; i++)
        {
            final HttpResponse httpResponse = sparkTestUtil.get("meter-wrapper");
            EntityUtils.consumeQuietly(httpResponse.getEntity());
        }
        final SortedMap<String, Meter> meters = metricRegistry.getMeters();

        for (String s : meters.keySet())
        {
            assertThat(meters.get(s).getCount(), is(10l));
        }
    }
}