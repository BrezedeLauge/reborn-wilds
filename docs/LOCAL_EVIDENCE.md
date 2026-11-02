# Local development evidence

The public repository includes source, tests, provenance inventories and the current [validation summary](VALIDATION.md). Raw development reports, launcher logs, game worlds, account files, captures and caches remain local and are excluded by `.gitignore`. They can contain machine paths or account information and are not required to build the mod.

Some historical development documents refer to `reports/`, `build/`, `dist/` or `run-*` paths. These identify local evidence and are not downloadable release artifacts. Historical test counts are specific to their original snapshots; they must not be added together or treated as validation of later changes. Source snapshots are distinguished from published binary releases.

The source-controlled tests and showcase tools can recreate checks in an appropriate Java 21 / NeoForge development environment. Showcase helpers operate only in explicitly marked development copies. A staged test scene is not proof of natural world generation, and successful compilation is not visual acceptance.
