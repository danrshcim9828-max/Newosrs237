# Rev237 Framework Design (Implementation-Backed)

## 1. Runtime model
- **One process, multiple subsystems**: network ingress, game tick scheduler, cache services.
- **Strict tick model**: 600ms scheduler in `GameEngine` to align with Old School timing semantics.
- **Revision gate first**: login channel validates rev237 before any stateful decode.

## 2. Network stack
- Netty NIO acceptor with isolated boss/worker groups.
- Login pipeline starts with `LoginHandshakeHandler`.
- First packet must encode client revision as unsigned short.
- Response byte contract:
  - `0`: accepted.
  - `6`: rejected (revision mismatch).

This keeps the handshake deterministic and directly testable.

## 3. Protocol strategy for RSProt237
Use RSProt as the authoritative packet schema/codec layer:
1. Insert RSProt decoder after handshake acceptance.
2. Route decoded packets into typed command handlers.
3. Emit outbound messages through RSProt encoders only.

Operational rule: packet ids and field serialization are maintained in one RSProt contract module so server behavior remains revision-locked.

## 4. Cache + data strategy (RuneLite/RsProz)
- `CacheIndex` currently validates cache availability and file cardinality.
- RuneLite/RsProz integration path:
  1. Export definitions (objs/npcs/sequences/varbits).
  2. Export map + XTEA region metadata.
  3. Load outputs into immutable runtime stores at startup.

Design target: no dynamic cache mutation during runtime ticks.

## 5. Game engine model
- Fixed rate scheduler with monotonic tick counter.
- Tick services run by phase:
  1. inbound commands applied.
  2. simulation update.
  3. outbound synchronization.

`GameEngine` logs every 100 ticks for liveness and uptime tracking.

## 6. Production hardening track
- Add RSA/ISAAC login cryptography and account auth adapter.
- Add session state machine (`HANDSHAKE -> LOGIN -> INGAME`).
- Add packet flood limits and per-IP rate policies.
- Add persistence adapters (accounts, inventory, position).
- Add metrics export (Prometheus/OpenTelemetry).

## 7. Build + operations
- Java toolchain pinned to 21 via Gradle toolchains.
- Windows bootstrap script installs JDK/Python/Gradle with Winget/Chocolatey.
- Python environment validator reports required executables and versions.

## 8. Definition of done for "full framework"
This baseline is considered framework-complete for initial rev237 bring-up when:
- Process boots with config generation.
- Tick loop runs continuously.
- Login socket binds and validates rev237.
- Cache path is inspected at startup.
- Automated tests validate revision handshake behavior.
