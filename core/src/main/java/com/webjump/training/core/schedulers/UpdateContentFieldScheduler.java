package com.webjump.training.core.schedulers;

import com.day.cq.commons.jcr.JcrUtil;
import com.webjump.training.core.implementations.GetSession;
import com.webjump.training.core.services.SlingSchedulerConfigurationForUpdatingContentField;
import org.apache.sling.commons.scheduler.ScheduleOptions;
import org.apache.sling.commons.scheduler.Scheduler;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Date;
import javax.jcr.Node;
import javax.jcr.Session;

/**
 * Custom scheduler for update a single field
 */
@Component(immediate = true, service = UpdateContentFieldScheduler.class)
@Designate(ocd = SlingSchedulerConfigurationForUpdatingContentField.class)
public class UpdateContentFieldScheduler implements Runnable {
    /**
     * Class for getting the session
     */
    @Reference
    GetSession getSession;

    /**
     *  Path to update the content field
     */
    private static final String UPDATE_PATH = "/content/training-sling/us/en/jcr:content/root/container/container/teste_yuri";

    /**
     *  Logger
     */
    private Logger logger = LoggerFactory.getLogger(UpdateContentFieldScheduler.class);

    /**
     *  Custom parameter
     */
    private String customParameter;

    /**
     *  Scheduler id
     */
    private int schedulerId;

    /**
     *  Scheduler object
     */
    @Reference
    private Scheduler scheduler;

    /**
     * Activate method to initialize stuff
     *
     *  @param config
     */
    @Activate
    protected void activate(SlingSchedulerConfigurationForUpdatingContentField config) {

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
    protected void modified(SlingSchedulerConfigurationForUpdatingContentField config) {

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
     *
     * @param config
     */
    @Deactivate
    protected void deactivate(SlingSchedulerConfigurationForUpdatingContentField config) {

        /**
         * Removing the scheduler
         */
        removeScheduler();
    }

    /**
     * This method removes the scheduler
     */
    private void removeScheduler() {

        logger.info("Removing scheduler: {}", schedulerId);

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
    private void addScheduler(SlingSchedulerConfigurationForUpdatingContentField config) {

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

            logger.info("Scheduler added");

        } else {

            logger.info("Scheduler is disabled");

        }
    }

    /**
     * Overridden run method to execute Job
     */
    @Override
    public void run() {
        try {
            Session session = getSession.execute();

            Date date = new Date();
            long secs = date.getTime()/1000;
            String text = "Changed value: " + secs;

            Node node = JcrUtil.createPath(UPDATE_PATH, "nt:unstructured", session);
            node.setProperty("text", text);

            session.save();
            session.logout();
            logger.info("Custom Scheduler is running correctly!!!");
        } catch (Exception e) {
            logger.error("Custom Scheduler has an error, e.getMessage() " + e.getMessage());
        }
    }
}