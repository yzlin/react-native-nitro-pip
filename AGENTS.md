# AGENTS.md

## Scope

- Repo: React Native Nitro module (library + example app)
- Monorepo: root package + `example/` (Bun workspaces)
- Primary languages: TypeScript, Kotlin, Swift, C++ (Nitro)

## Quick Start

- Node version: see `.nvmrc` (v22.20.0)
- Package manager: Bun (`packageManager` in `package.json`)
- Install deps: `bun install`

## Commands

### Library (root)

- Build library: `bun run prepare` (runs `bob build`)
- Generate Nitro bindings: `bun run nitrogen`
  - Required when changing `*.nitro.ts`
  - Required on first setup (generated files are not committed)
- Typecheck: `bun run typecheck`
- Lint: `bun run lint`
- Lint + fix: `bun run lint --fix`
- Tests (Jest): `bun run test`
- Clean build output: `bun run clean`

### Example app

- Start Metro (dev client): `bun run example start`
- Run Android: `bun run example android`
- Run iOS: `bun run example ios`

### CI parity (useful for local verification)

- Lint: `bun run lint`
- Typecheck: `bun run typecheck`
- Tests with coverage: `bun run test --maxWorkers=2 --coverage`
- Build library: `bun run prepare`

### Single test / focused runs (Jest)

- Run one file: `bun run test --runTestsByPath path/to/file.test.tsx`
- Run by name: `bun run test -t "test name pattern"`

## Tooling

- TypeScript: strict mode enabled (`tsconfig.json`)
- ESLint: Flat config with `@react-native` + Prettier (`eslint.config.mjs`)
- Prettier: configured in `package.json`
- Biome: enabled with `ultracite` presets (`biome.jsonc`)
- Hooks: `lefthook.yml` runs ESLint + TSC on commit, commitlint on commit-msg
- Jest: config in `package.json` (React Native preset; ignores `example/node_modules` and `lib/`)

## Code Style

### Formatting

- Indentation: 2 spaces (no tabs) (`.editorconfig`)
- Quotes: single quotes (`package.json` Prettier config)
- Trailing commas: ES5 style (`package.json` Prettier config)
- Line endings: LF (`.editorconfig`)
- Trim trailing whitespace + final newline (`.editorconfig`)

### Imports

- Use ES modules; prefer `import type` for type-only imports (`src/index.tsx`)
- Follow Biome organizeImports grouping if running format (`biome.jsonc`)

### Naming

- TypeScript: camelCase for functions/vars, PascalCase for components/interfaces
- Kotlin/Swift: PascalCase types, camelCase methods/params
- File naming: standard React Native + Nitro conventions (`*.nitro.ts`, `index.tsx`)

### Types

- Strict TS: `strict: true`, `noUnusedLocals`, `noUnusedParameters`
- Prefer `interface` over `type` for object shapes (Biome rule)
- Avoid `as any` / `@ts-ignore` / `@ts-expect-error`

### Error handling

- JS/TS: no custom pattern defined; follow standard try/catch only when needed
- Native: minimal error handling in handwritten files; generated Nitro bindings throw/propagate errors

### React Native conventions

- React 17+ JSX runtime: no `React` import required (`eslint.config.mjs`)
- Example app uses `StyleSheet.create` for styles (`example/src/App.tsx`)

## Generated Code

- `nitrogen/generated/**` is generated; do not edit manually
- Run `bun run nitrogen` after changes to `*.nitro.ts`

## Structure Guide

- `src/` TypeScript API surface
- `android/` Kotlin + C++ implementation
- `ios/` Swift implementation
- `example/` Expo-based example app
- `lib/` build output (do not edit)

## Commit & PR conventions

- Conventional Commits enforced by commitlint (`lefthook.yml`, `CONTRIBUTING.md`)
- Types: `feat|fix|refactor|docs|test|chore`

## Do/Don’t

- Do keep changes minimal and follow existing patterns
- Do update tests when behavior changes (tests currently minimal)
- Don’t edit generated Nitro files
- Don’t use npm for development (Bun workspace only)

## External references (context only)

- Nitro docs: https://nitro.margelo.com/
- Builder Bob docs: https://callstack.github.io/react-native-builder-bob/

## Landing the Plane (Session Completion)

**When ending a work session**, you MUST complete ALL steps below. Work is NOT complete until `git push` succeeds.

**MANDATORY WORKFLOW:**

1. **File issues for remaining work** - Create issues for anything that needs follow-up
2. **Run quality gates** (if code changed) - Tests, linters, builds
3. **Update issue status** - Close finished work, update in-progress items
4. **PUSH TO REMOTE** - This is MANDATORY:
   ```bash
   git pull --rebase
   bd sync
   git push
   git status  # MUST show "up to date with origin"
   ```
5. **Clean up** - Clear stashes, prune remote branches
6. **Verify** - All changes committed AND pushed
7. **Hand off** - Provide context for next session

**CRITICAL RULES:**
- Work is NOT complete until `git push` succeeds
- NEVER stop before pushing - that leaves work stranded locally
- NEVER say "ready to push when you are" - YOU must push
- If push fails, resolve and retry until it succeeds
