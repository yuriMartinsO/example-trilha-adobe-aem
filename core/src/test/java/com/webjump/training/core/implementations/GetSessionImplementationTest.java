package com.webjump.training.core.implementations;

import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.jcr.Session;
import java.util.HashMap;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class GetSessionImplementationTest {
    @Mock
    private ResourceResolverFactory resolverFactory;

    @Mock
    private ResourceResolver resourceResolver;

    @Mock
    private Session session;

    @InjectMocks
    private GetSessionImplementation getSessionImplementation;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testExecuteSuccess() throws LoginException {
        HashMap<String, Object> param = new HashMap<>();
        param.put(ResourceResolverFactory.SUBSERVICE, "getresourceresolver");
        when(resolverFactory.getServiceResourceResolver(param)).thenReturn(resourceResolver);
        when(resourceResolver.adaptTo(Session.class)).thenReturn(session);

        Session result = getSessionImplementation.execute();

        assertEquals(session, result);
        verify(resolverFactory).getServiceResourceResolver(param);
        verify(resourceResolver).adaptTo(Session.class);
    }

    @Test
    public void testExecuteMethodThrowsException() throws LoginException {
        HashMap<String, Object> param = new HashMap<>();
        param.put(ResourceResolverFactory.SUBSERVICE, "getresourceresolver");
        when(resolverFactory.getServiceResourceResolver(param)).thenThrow(new LoginException("Failed!"));

        RuntimeException result = assertThrows(
                RuntimeException.class, () -> getSessionImplementation.execute()
        );

        assertEquals("Failed!", result.getCause().getMessage());
        verify(resolverFactory).getServiceResourceResolver(param);
    }
}
