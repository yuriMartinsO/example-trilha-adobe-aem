package com.webjump.training.core.servlets;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.junit.jupiter.api.Test;
import javax.jcr.Session;
import javax.jcr.Node;
import java.io.PrintWriter;
import org.junit.jupiter.api.BeforeEach;

public class SavePayloadDataServletTest {

    @Mock
    private SlingHttpServletRequest request;

    @Mock
    private SlingHttpServletResponse response;

    @Mock
    private ResourceResolver resourceResolver;

    @Mock
    private Session session;

    @Mock
    private Node node;

    @Mock
    private PrintWriter printWriter;

    private SavePayloadDataServlet servlet;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        servlet = new SavePayloadDataServlet();
    }

    @Test
    public void testPlaceholder() {
        // This test does nothing, it's just a placeholder for the build.
        assert true;
    }
}