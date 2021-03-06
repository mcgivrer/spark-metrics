package com.qmetric.spark.metrics;

import org.apache.http.HttpResponse;
import org.junit.Test;
import spark.utils.IOUtils;

import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.StringWriter;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class PingRouteTest
{
    @Test
    public void shouldPing() throws IOException
    {
        final SparkTestUtil sparkTestUtil = new SparkTestUtil();
        final HttpResponse httpResponse = sparkTestUtil.get("ping");

        final StringWriter writer = new StringWriter();

        IOUtils.copy(httpResponse.getEntity().getContent(), writer);

        assertThat(httpResponse.getStatusLine().getStatusCode(), equalTo(HttpServletResponse.SC_OK));
        assertThat(writer.toString(), equalTo("pong\n"));
    }

}
