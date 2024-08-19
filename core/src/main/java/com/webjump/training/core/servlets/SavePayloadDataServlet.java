package com.webjump.training.core.servlets;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.PrintWriter;
import javax.servlet.Servlet;
import com.day.cq.commons.jcr.JcrUtil;
import org.apache.sling.jcr.api.SlingRepository;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.ServletException;
import java.io.IOException;

@Component(service = {
        Servlet.class
}, property = {
        "sling.servlet.methods=post",
        "sling.servlet.paths=/bin/savepayloadyuri"
})
@SuppressWarnings({ "serial", "unused" })
public class SavePayloadDataServlet extends SlingAllMethodsServlet {
    private static final String UPDATE_PATH = "/content/training-sling/us/en/testpage/jcr:content/root/container/container/teste_yuri";
    private static final Logger log = LoggerFactory.getLogger(SavePayloadDataServlet.class);

    @Reference
    private SlingRepository repository;

    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        ResourceResolver resourceResolver = request.getResourceResolver();
        Session session = resourceResolver.adaptTo(Session.class);
        String text = request.getParameter("text");

        PrintWriter out = response.getWriter();
        try {
            Node node = JcrUtil.createPath(UPDATE_PATH, "nt:unstructured", session);
            node.setProperty("text", text);
            session.save();
            out.println("That went well...");
            out.flush();
            out.close();
        } catch (RepositoryException e) {
            out.println("That went not so well...");
            out.flush();
            out.close();
            e.printStackTrace();
        }
    }
}
