# Git2- Master Task

## Priority #1: Make Git2- the easiest, reliable Android control/build app

This is the primary project. All other repo cleanup and integration work comes after Git2- is functional, buildable, and useful.

### Current priority stack

1. **Git2-: make the app actually work**
   - Audit every screen and button.
   - Remove placeholder/TODO click handlers.
   - Wire GitHub actions to real GitHub API operations.
   - Wire local/Termux actions where Android permits them.
   - Wire Ollama/local AI generation.
   - Verify navigation and state flow.
   - Verify generated projects actually contain the promised files.
   - Test the real APK, not just compilation.
   - Keep CI producing a downloadable debug APK.

2. **Mandela vs Matrix repository archaeology**
   - Inventory scattered Mandela/Matrix code across repositories.
   - Identify duplicates, obsolete files, useful modules, and missing pieces.
   - Consolidate into a clean project structure without silently deleting functionality.

3. **Swarm Builder**
   - Preserve a standalone Swarm Builder project.
   - Create a separate Mandela-integrated Swarm Builder edition.
   - Keep the standalone version independent of Mandela dependencies.

### Rules

- Do not call a feature complete merely because Gradle compiles it.
- Do not replace working functionality with placeholders.
- Do not delete source until it has been inventoried and its replacement is verified.
- Prefer real end-to-end functionality over cosmetic UI.
- Every major change must leave the repository buildable.
- Keep CI and APK artifacts working while features are repaired.

### Current known issue

The app launches, but parts of the visible UI still contain placeholder click handlers. `app/src/main/java/com/example/ui/MainScreen.kt` previously contained TODO handlers for Termux, GitHub Action, and Ollama. These must be audited and replaced with real wiring.

### Definition of done for Priority #1

- App launches without crashing.
- Main navigation works.
- Every visible button has a deliberate action or is clearly disabled with an explanation.
- GitHub authentication/API flow works with valid credentials.
- Core GitHub operations work end-to-end.
- Local AI/Ollama functionality works when the required local service is available and fails gracefully when unavailable.
- APK builds in CI.
- Unit/instrumentation tests cover the critical flows.
- A fresh APK has been manually smoke-tested.
