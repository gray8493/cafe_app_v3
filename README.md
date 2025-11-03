# Cafe Management App (Workspace README)

This repository contains the Cafe Management application (backend + demo client).

Primary project folder: `cafe-app/demo` (Spring Boot application).

If you want a short, developer-focused README (how to run locally and where to configure keys), use the `cafe-app/README.md` inside the project folder. If you'd like me to replace that file in-place, tell me and I'll overwrite it.

Quick run (dev):

```powershell
cd .\cafe-app\demo
.\mvnw spring-boot:run
```

Python demo client (camera):

```powershell
python python-clients\suggestion_client.py
```

Face++ notes: edit `demo/src/main/resources/application.properties` and set `facepp.api.key` and `facepp.api.secret` to your values. Use `api-us` or `api-cn` endpoint depending on your Face++ app region.

If you want a single README file replaced under `cafe-app/README.md`, confirm and I'll overwrite that file directly with the full rewritten content.