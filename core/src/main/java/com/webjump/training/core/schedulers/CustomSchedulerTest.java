package com.webjump.training.core.schedulers;

import com.webjump.training.core.implementations.GetResolver;
import org.apache.sling.commons.scheduler.ScheduleOptions;
import org.apache.sling.commons.scheduler.Scheduler;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;
import com.webjump.training.core.services.SlingSchedulerConfigurationTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.sling.api.resource.ResourceResolver;

import javax.jcr.Session;

/**
 * @author Anirudh Sharma
 *
 * A Sling Scheduler demo using OSGi R6 annotations
 *
 */
@Component(immediate = true, service = CustomSchedulerTest.class)
@Designate(ocd = SlingSchedulerConfigurationTest.class)
public class CustomSchedulerTest implements Runnable {
    @Reference
    GetResolver getResolver;

    /**
     * Logger
     */
    private static final Logger log = LoggerFactory.getLogger(CustomSchedulerTest.class);

    /**
     * Custom parameter that is to be read from the configuration
     */
    private String customParameter;

    /**
     * Id of the scheduler based on its name
     */
    private int schedulerId;

    /**
     * Scheduler instance injected
     */
    @Reference
    private Scheduler scheduler;

    /**
     * Activate method to initialize stuff
     *
     * @param config
     */
    @Activate
    protected void activate(SlingSchedulerConfigurationTest config) {

        /**
         * Getting the scheduler id
         */
        schedulerId = config.schdulerName().hashCode();

        /**
         * Getting the custom parameter
         */
        customParameter = config.customParameter();
    }

    /**
     * Modifies the scheduler id on modification
     *
     * @param config
     */
    @Modified
    protected void modified(SlingSchedulerConfigurationTest config) {

        /**
         * Removing the scheduler
         */
        removeScheduler();

        /**
         * Updating the scheduler id
         */
        schedulerId = config.schdulerName().hashCode();

        /**
         * Again adding the scheduler
         */
        addScheduler(config);
    }

    /**
     * This method deactivates the scheduler and removes it
     * @param config
     */
    @Deactivate
    protected void deactivate(SlingSchedulerConfigurationTest config) {

        /**
         * Removing the scheduler
         */
        removeScheduler();
    }

    /**
     * This method removes the scheduler
     */
    private void removeScheduler() {

        log.info("Removing scheduler: {}", schedulerId);

        /**
         * Unscheduling/removing the scheduler
         */
        scheduler.unschedule(String.valueOf(schedulerId));
    }

    /**
     * This method adds the scheduler
     *
     * @param config
     */
    private void addScheduler(SlingSchedulerConfigurationTest config) {

        /**
         * Check if the scheduler is enabled
         */
        if(config.enabled()) {

            /**
             * Scheduler option takes the cron expression as a parameter and run accordingly
             */
            ScheduleOptions scheduleOptions = scheduler.EXPR(config.cronExpression());

            /**
             * Adding some parameters
             */
            scheduleOptions.name(config.schdulerName());
            scheduleOptions.canRunConcurrently(false);

            /**
             * Scheduling the job
             */
            scheduler.schedule(this, scheduleOptions);

            log.info("Scheduler added");

        } else {

            log.info("Scheduler is disabled");

        }
    }

    /**
     * Overridden run method to execute Job
     */
    @Override
    public void run() {
        try {
            ResourceResolver serviceResolver = getResolver.getServiceResolver();
            Session session = serviceResolver.adaptTo(Session.class);
            log.error("Custom Scheduler is now running using the passed custom paratmeter, customParameter {}", customParameter);
        } catch (Exception e) {
            log.error("Custom Scheduler is now running using the passed custom paratmeter, e.printStackTrace() {}" + e.getMessage());
        }

    }

}