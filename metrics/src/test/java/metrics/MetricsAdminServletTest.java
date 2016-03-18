package metrics;

import org.junit.Before;
import org.junit.Test;

import javax.servlet.annotation.WebServlet;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

/**
 * Unit tests for the {@link MetricsAdminServlet} class.
 */
public class MetricsAdminServletTest {

    private MetricsAdminServlet listener;

    @Before
    public void setup() {
        listener = new MetricsAdminServlet();
    }

    @Test
    public void shouldBeMappedToCorrectUrl() {
        WebServlet annotation = listener.getClass().getAnnotation(WebServlet.class);
        assertThat(annotation.value().length, equalTo(1));
        assertThat(annotation.value()[0], equalTo("/internal/metrics/*"));
    }
}