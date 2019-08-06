package com.example;

import com.dtolabs.rundeck.core.audit.AuditEvent;
import com.dtolabs.rundeck.core.plugins.Plugin;
import com.dtolabs.rundeck.plugins.ServiceNameConstants;
import com.dtolabs.rundeck.plugins.audit.AuditEventListener;
import com.dtolabs.rundeck.plugins.descriptions.PluginDescription;
import com.dtolabs.rundeck.plugins.descriptions.PluginProperty;

import java.io.*;
import java.util.Date;
import java.util.List;

/**
 * Example listener for audit events.
 * <p>
 * Use this kind of plugin to take actions when certain auditing events are triggered.
 *
 * @author Alberto Hormazabal
 */
@Plugin(
    name = ExampleAuditListener.PROVIDER_NAME,
    service = ServiceNameConstants.AuditEventListener)
@PluginDescription(
    title = "Example Audit Plugin",
    description = "Example plugin for implementing an auditing listener")
public class ExampleAuditListener implements AuditEventListener {

  public static final String PROVIDER_NAME = "ExampleAuditListener";

  /*
  Plugin Configuration

  Using the @PluginProperty annotation you can define how to populate plugin fields from properties
  at the framework.properties file.

  Properties will be read from properties named with the format 'framework.plugin.AuditEventListener.MyProviderName.MyPropertyName'.

  For example the base property key for this example is:
  'framework.plugin.AuditEventListener.ExampleAuditListener'

   */


  // will look for the 'framework.plugin.AuditEventListener.ExampleAuditListener.path' property at framework.properties.
  @PluginProperty(required = true)
  private String path;

  // will look for the 'framework.plugin.AuditEventListener.ExampleAuditListener.filename' property at framework.properties.
  @PluginProperty(
      name = "filename",
      defaultValue = "audit.log"
  )
  private String nameFile;

  /** The PrintWriter we will use to write to a file */
  private PrintWriter output = null;

  /**
   * This method is called right after the plugin has been initialized and configured.
   * Use this method to perform instance initialization.
   */
  @Override
  public void init() {
    System.out.println("  !!! EXAMPLE AUDIT PLUGIN INITIALIZATION: " + this.toString());
    System.out.println("  !!! PATH: " + path);
    System.out.println("  !!! Filename: " + nameFile);
    System.out.println();

    File outputFile = new File(path, nameFile);
    try {
      OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(outputFile, true));
      this.output = new PrintWriter(outputStream, true);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      throw new RuntimeException(e.getMessage(), e);
    }

    // Write a header to the file.
    output.format("[%-30s] %-15s %-15s %-10s %s%n",
        "DATE",
        "USER",
        "ACTION",
        "RESOURCE",
        "ID");
  }

  /**
   * This method is called when any kind of event is triggered.
   * If a triggered event also matches any of the below methods, this method will
   * be also called BEFORE said specific method.
   *
   * @param event
   */
  public void onEvent(AuditEvent event) {


    String user = event.getUsername(); // Gets the username which generated the event.
    Date ts = event.getTimestamp(); // Timestamp at which the event was generated.
    List<String> roles = event.getUserRoles(); // List with the auth roles of the user.

    AuditEvent.ResourceType resourceType = event.getResourceType();
    // Type of resource which generated the event. (IE: user, project).

    if (AuditEvent.ResourceType.project.equals(resourceType)) {

      // Get the project name
      String projectName = event.getResourceName();

      System.out.format("Event [%s] triggered on project [%s]: %s%n",
          event.getAction().toString(), projectName, event.toString());
    }

    System.out.println("\nExample Plugin On-event!!!" + event);

    // Write to file.
    output.format("[%-30s] %-15s %-15s %-10s %s%n",
        event.getTimestamp(),
        event.getUsername(),
        event.getAction(),
        event.getResourceType(),
        event.getResourceName());

  }

    /*
    The following methods are quick access callbacks to capture specific events.
    These method are called whenever an specific event is triggered. Note that the
    main onEvent() method above will also be called ALWAYS BEFORE any of the following
    callbacks.
     */


  /**
   * This method is called when an event with Action "login_success" and Resource Type "user"
   * is triggered.
   */
  public void onLoginSuccess(AuditEvent event) {

    System.out.println("\nExample Plugin onLoginSuccess!!!" + event);

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
//        System.out.println("\nExample Plugin onLoginFailed!!!" + event)
//    }


  /**
   * This method is called when an event with Action "logout" and Resource Type "user"
   * is triggered.
   */
  public void onLogout(AuditEvent event) {

    System.out.println("\nExample Plugin User onLogout: " + event.getUsername());

  }


  /**
   * This method is called when an event with Action "view" and Resource Type "project".
   * is triggered.
   * This happens when a user switches requests a project homepage. (for example by switching
   * project using the menu).
   */
  public void onProjectView(AuditEvent event) {

    System.out.println("\nExample Plugin onProjectView!!!" + event.getResourceName() + " event:" + event);


  }
}


