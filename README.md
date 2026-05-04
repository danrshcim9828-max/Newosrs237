# NewOSRS237 Framework

Revision-237 RSPS server framework with a working login/session pipeline and deterministic game loop.

## Implemented Runtime
- Java 21 + Gradle build.
- Netty TCP server on port 43594.
- Session state machine: `HANDSHAKE -> LOGIN -> INGAME`.
- Revision gate for 237.
- Login credential packet decoder (`username/password` length-prefixed UTF-8).
- Authentication service contract.
- 600ms game tick executor.
- Cache file indexing at startup.

## Protocol Contract (current)
1. Client sends 2-byte revision.
2. Server replies:
   - `0` revision accepted, move to login state.
   - `6` revision rejected, socket closed.
3. Client sends login packet:
   - `u8 usernameLength`
   - `username bytes`
   - `u8 passwordLength`
   - `password bytes`
4. Server replies:
   - `2` login success (`INGAME`)
   - `3` login denied (socket closed)

## Build & Run
```bash
gradle test
gradle run
```

## Windows Tool Bootstrap
```powershell
./scripts/bootstrap_windows.ps1
# or
./scripts/bootstrap_windows.ps1 -UseChocolatey
```
