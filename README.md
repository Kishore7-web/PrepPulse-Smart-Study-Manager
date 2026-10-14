# PrepPulse – Smart Study Manager

PrepPulse is a Java 21 + Spring Boot study-planning app intended for a student project and local demonstration. It uses a local H2 SQL database so student data survives restarts. The app does **not** require a paid AI API.

## Innovation features in this version

- **Smart reminders:** per-task reminder choices (1 day, 1 hour, 15 minutes or custom selected offsets), persistent reminder activity, optional browser notifications, and optional SMTP email delivery. Reminder checks run every 60 seconds while the app is running.
- **Smart priority suggestions:** transparent rules rank unfinished tasks using priority, time remaining, estimated effort, and postponement count. The reasons are shown beside recommendations.
- **Smart Rescue Mode:** choose the time you actually have; the planner makes a ranked sequence of study blocks, splits large tasks, inserts short breaks, and reports how many tasks still need time. Comeback Mode offers a 15-minute starting plan.
- **Spaced revision scheduler:** add topics, label difficulty, and receive a review schedule with expanding intervals. Marking a topic reviewed schedules its next review.
- **Focus timer:** 25-minute and 50-minute focus blocks plus 5- and 15-minute breaks. Saved sessions are persisted and used by insights.
- **Workload warning:** compares unfinished estimated minutes with the daily study-time budget.
- **Comeback support:** short recovery plan and a non-judgmental restart message.
- **Study insights:** session minutes today/this week, subject breakdown, postponed-task count, and due revision count.
- **Study preferences:** daily available minutes, email address, global reminders switch, and optional study-partner notification consent.
- Java packages, Spring Data JPA repositories, validation, exception handling, Spring Scheduler, `ExecutorService`/`CompletableFuture`, tests, and REST endpoints.

## Run on Windows (recommended)

1. Install **JDK 21 or newer**. Check it by opening Command Prompt and running `java -version`.
2. Extract the ZIP archive fully. Open the extracted `PrepPulse-Smart-Study-Manager` folder — the folder that directly contains `pom.xml`.
3. Double-click **`run-preppulse.bat`**.
4. On the first run, the launcher uses Maven if already installed. Otherwise it downloads a private Maven copy into the project's `.tools` folder. Maven then downloads the project's Java libraries. Both steps require internet access the first time.
5. Wait until the terminal says Spring Boot has started, then open `http://localhost:8080` in your browser.
6. Keep the terminal window open while using the app. Stop the app with `Ctrl+C`.

You do not need to install Maven globally. Use `diagnose-preppulse.bat` to check your environment and `test-preppulse.bat` to run the automated tests. See `TROUBLESHOOTING.md` if your editor displays many unresolved Spring imports.

## Open in VS Code

Open the project root folder containing `pom.xml`, not only `src`. Install the recommended Java Extension Pack if VS Code prompts you. Run `diagnose-preppulse.bat` once to resolve dependencies, then use **Java: Clean Java Language Server Workspace** from the Command Palette if old red import errors remain.

## Email reminders (optional)

In-app reminder records work without SMTP configuration. Email is disabled by default. To send real emails, set these environment variables before starting the application:

- `PREPPULSE_EMAIL_ENABLED=true`
- `PREPPULSE_EMAIL_HOST` (your email provider's SMTP host)
- `PREPPULSE_EMAIL_PORT` (typically 587)
- `PREPPULSE_EMAIL_USERNAME`
- `PREPPULSE_EMAIL_PASSWORD` (use an app password where the provider requires it; never commit it to source control)
- `PREPPULSE_EMAIL_FROM` (optional sender address)

The student recipient is set in **Study preferences & reminders**. Partner emails are only sent if a partner address is entered and the explicit partner checkbox is enabled. Do not use someone's address without their consent. SMTP may require provider-side app-password/security setup. If delivery fails, the reminder remains recorded with `EMAIL_FAILED` and details are written to the application log; it is not silently treated as a sent email.

Browser notifications are optional and require the user's permission. They are displayed while the dashboard is open; they are not a replacement for background email or OS-level notifications when the browser is closed.

## Data and H2 console

- Database file: `./data/preppulse`
- H2 console: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:file:./data/preppulse`
- User: `sa`
- Password: leave blank

Hibernate updates the schema for this educational starter via `spring.jpa.hibernate.ddl-auto=update`. Keep a backup of the `data` folder before changing entity models.

## API examples

- `GET /api/tasks` — tasks
- `GET /api/stats` — dashboard counts
- `GET /api/recommended` — transparent task ranking
- `POST /api/rescue-plan` with JSON `{"availableMinutes": 60}` — rescue plan
- `POST /api/focus-sessions` with JSON such as `{"durationMinutes":25,"sessionType":"FOCUS"}` — save a session
- `GET /api/reminders/latest-id` — latest reminder ID
- `GET /api/reminders/recent?after=0` — recent reminder activity

## Important implementation notes

This is a local, single-profile college-demo app, not a multi-user hosted service. There is no login/authorization, cloud sync, WhatsApp/SMS delivery, or paid AI integration. Reminders are only processed while the Spring Boot app is running; use SMTP if you need delivery beyond an open dashboard. Run `test-preppulse.bat` and explore the UI locally before your presentation. The source was statically reviewed in this delivery environment, but a complete Maven build could not be executed here because external dependency downloads were unavailable. Do not describe the project as deployed or fully tested until it builds and runs successfully on your laptop.

Smart recommendations and revision intervals are explainable rules, not medical or academic guarantees. Students can edit schedules to suit their real availability.
