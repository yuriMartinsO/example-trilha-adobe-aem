package com.webjump.training.core.implementations;

import javax.jcr.Session;
import java.util.HashMap;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 *  Class for getting the session
 */
@Component(service = GetSession.class)
public class GetSessionImplementation implements GetSession {
    /**
     * RecourceResolverFactory to get the session
     */
    @Reference
    ResourceResolverFactory resolverFactory;

    /**
     * Execute
     *
     * @return Session
     */
    public Session execute() {
        HashMap < String, Object > param = new HashMap < String, Object > ();
        param.put(ResourceResolverFactory.SUBSERVICE, "getresourceresolver");
        ResourceResolver resolver = null;
        try {
            resolver = resolverFactory.getServiceResourceResolver(param);
        } catch (LoginException e) {
            throw new RuntimeException(e);
        }
        return resolver.adaptTo(Session.class);
    }
}
