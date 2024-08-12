package com.webjump.training.core.implementations;

import java.util.HashMap;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(service = GetResolver.class)
public class GetResolverImpl implements GetResolver {
    @Reference
    ResourceResolverFactory resolverFactory;

    public ResourceResolver getServiceResolver() {
        System.out.println("#### Trying to get service resource resolver ....  in my bundle");
        HashMap < String, Object > param = new HashMap < String, Object > ();
        param.put(ResourceResolverFactory.SUBSERVICE, "getresourceresolver");
        ResourceResolver resolver = null;
        try {
            resolver = resolverFactory.getServiceResourceResolver(param);
        } catch (LoginException e) {
            System.out.println("Login Exception " + e.getMessage());
        }

        return resolver;
    }
}
