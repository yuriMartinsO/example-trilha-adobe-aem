package com.webjump.training.core.schedulers;

import com.webjump.training.core.implementations.GetSession;
import com.webjump.training.core.services.SlingSchedulerConfigurationForUpdatingContentField;
import org.apache.sling.commons.scheduler.Scheduler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.slf4j.Logger;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import static org.mockito.Mockito.*;

public class UpdateContentFieldSchedulerTest {
    @Mock
    private GetSession getSession;

    @Mock
    private Scheduler scheduler;

    @Mock
    private Session session;

    @Mock
    private Logger log;

    @Mock
    private SlingSchedulerConfigurationForUpdatingContentField config;

    @InjectMocks
    private UpdateContentFieldScheduler updateContentFieldScheduler;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(getSession.execute()).thenReturn(session);
    }

    @Test
    public void testRunLoggWithSuccess() throws RepositoryException {
        when(getSession.execute()).thenReturn(null);
        updateContentFieldScheduler.run();

        verify(log).error(contains("Custom Scheduler has an error"));
    }
}
