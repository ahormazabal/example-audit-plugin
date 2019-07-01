#Example Rundeck auditing listener plugin

This is an example about how to use a an Audit Listener plugin to capture auditing events from rundeck application.
 
## Install 

* build: `./gradlew clean build`
* install: copy the file `build/libs/example-audit-plugin-1.0.0.jar` to libext folder


## Use 

Implement your own `AuditEventListener` to capture events.
Refer to the example found on `src/main/groovy/com/rundeck/plugin/ExampleAuditEventListener.groovy`




