package com.rundeck.plugin

import com.dtolabs.rundeck.core.audit.AuditEvent
import com.dtolabs.rundeck.core.common.Framework
import com.dtolabs.rundeck.core.plugins.Plugin
import com.dtolabs.rundeck.core.utils.PropertyLookupException
import com.dtolabs.rundeck.plugins.ServiceNameConstants
import com.dtolabs.rundeck.plugins.audit.AuditEventListener
import com.dtolabs.rundeck.plugins.descriptions.PluginDescription

/**
 *
 * Example listener for audit events.
 *
 * Use this kind of plugin to take actions when certain auditing events are triggered.
 *
 *
 * @author Alberto Hormazabal
 */
@Plugin(name = ExampleAuditEventListener.PROVIDER_NAME, service = ServiceNameConstants.AuditEventListener)
@PluginDescription(title = ExampleAuditEventListener.PROVIDER_TITLE, description = ExampleAuditEventListener.PROVIDER_DESCRIPTION)
class ExampleAuditEventListener implements AuditEventListener {

    // The name for this provider
    public static final String PROVIDER_NAME = "example-audit-listener"

    // The title to be displayed by this provider
    public static final String PROVIDER_TITLE = "Example Audit Plugin"

    // Provider description.
    public static final String PROVIDER_DESCRIPTION = "Example plugin for implementing an auditing listener"

    // Example config key.
    public static final String EXAMPLE_CONFIG = "example-audit-plugin.config.entry"


    /**
     * This method is called when any kind of event is triggered.
     * If a triggered event also matches any of the below methods, this method will
     * be also called BEFORE said specific method.
     * @param event
     */
    @Override
    void onEvent(AuditEvent event) {


        String user = event.username // Gets the username which generated the event.
        String ts = event.timestamp // Timestamp at which the event was generated.
        String roles = event.userRoles // List with the auth roles of the user.

        AuditEvent.ResourceType resourceType = event.resourceType;
        // Type of resource which generated the event. (IE: user, project).

        if (AuditEvent.ResourceType.project.equals(resourceType)) {

            // Get the project name
            String projectName = event.resourceName

            System.out.format("Event [%s] triggered on project [%s]: %s%n",
                    event.action.toString(), projectName, event.toString())
        }

        // Get a configuration value from framework.properties
        try {
            String configValue = event.getFramework().getProperty(EXAMPLE_CONFIG)
        }
        catch (PropertyLookupException e) {
            System.err.println("Example Plugin error getting property: " + e.getMessage())
        }

        System.out.println("Example Plugin On-event!!!" + event)
    }

    /*
    The following methods are quick access callbacks to capture specific events.
    These method are called whenever an specific event is triggered. Note that the
    main onEvent() method above will also be called ALWAYS BEFORE any of the following
    callbacks.
     */


    @Override
    /**
     * This method is called when an event with Action "login_success" and Resource Type "user"
     * is triggered.
     */
    void onLoginSuccess(AuditEvent event) {
        System.out.println("Example Plugin onLoginSuccess!!!" + event)
    }


    /*
    It is not necessary to implement all methods, you can only implement the ones
    you need.
     */

//    /**
//     * This method is called when an event with Action "login_failed" and Resource Type "user"
//     * is triggered.
//     */
//    @Override
//    void onLoginFailed(AuditEvent event) {
//        System.out.println("Example Plugin onLoginFailed!!!" + event)
//    }


    /**
     * This method is called when an event with Action "logout" and Resource Type "user"
     * is triggered.
     */
    @Override
    void onLogout(AuditEvent event) {
        System.out.println("Example Plugin User onLogout: " + event.username)
    }


    /**
     * This method is called when an event with Action "view" and Resource Type "project".
     * is triggered.
     * This happens when a user switches requests a project homepage. (for example by switching
     * project using the menu).
     */
    @Override
    void onProjectView(AuditEvent event) {
        System.out.println("Example Plugin onProjectView!!!" + event.resourceName + " event:" + event)
    }
}


