# PrepPulse troubleshooting (Windows)

## Important: open the correct folder

In VS Code, choose **File → Open Folder** and select the extracted `PrepPulse-Smart-Study-Manager` folder — the folder that directly contains `pom.xml`, `maven.bat`, `run-preppulse.bat`, and `src`.
Do not open only the `src` or `src/main/java` folder.

## Hundreds of red errors like `package org.springframework... does not exist`

These often mean the Maven dependencies have not been downloaded/imported yet, not that every Java file is broken.

1. Close old editor error tabs.
2. Double-click `diagnose-preppulse.bat` once and let Maven setup finish. An internet connection is needed the first time.
3. Then double-click `run-preppulse.bat`.
4. In VS Code, open the Command Palette (`Ctrl+Shift+P`) and run **Java: Clean Java Language Server Workspace**, then allow the workspace to reload.

Do not create replacement classes for Spring imports. Maven downloads those libraries for the project.

## If the app does not start

Use `test-preppulse.bat` and read the **first** `[ERROR]` message. Common cases:

- `JAVA_HOME` / unsupported class version: install JDK 21 or newer and reopen Command Prompt.
- Could not resolve dependencies / connection timed out: check internet access and run the test script again.
- Port 8080 already in use: close the other app using port 8080, then retry.
- Browser says the page cannot be reached: keep the run terminal open and wait until Spring Boot says it has started on port 8080.

The first successful build needs internet access to fetch Java dependencies. Afterwards Maven keeps them in its local cache.

## Resetting demo data

Stop PrepPulse first. Back up the `data` folder, then remove the H2 database files inside it only if you intentionally want to reset all saved tasks, settings, topics, reminders, and focus sessions. On next startup the demo data will be seeded again.
