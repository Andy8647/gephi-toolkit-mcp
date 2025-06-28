# CLAUDE.md - Development Context for Claude Code

## Project Overview
This project implements a Gephi Toolkit MCP Server. Full requirements and specifications are in [docs/prd.md](./docs/prd.md).

## Current Development Status
- **Phase**: MVP (Phase 1) - **FOUNDATION COMPLETE** ✅
- **Priority**: Basic graph loading and Force Atlas 2 layout
- **Architecture**: Node.js MCP Server + Java bridge to Gephi Toolkit (child_process approach)

## ✅ Completed Tasks
- [x] Set up basic MCP server structure (`src/index.js`)
- [x] Implement Java bridge for Gephi Toolkit (Maven project in `java/`)
- [x] Create `load_graph` tool (supports GEXF, GraphML, GML, DOT, CSV)
- [x] Add `apply_force_atlas2` layout tool with full parameter support
- [x] Implement `get_graph_info` for statistics
- [x] Add `save_graph` export functionality (GEXF, GraphML, PDF, PNG)
- [x] Maven build configuration with Gephi Toolkit 0.10.1
- [x] Java bridge classes: GephiContext, GraphLoader, LayoutProcessor, GraphInfo, GraphExporter

## 🎯 Next Tasks (Testing & Refinement)
- [ ] Build Java components: `npm run build-java`
- [ ] Test graph loading with sample files
- [ ] Test Force Atlas 2 layout execution
- [ ] Validate export functionality
- [ ] Add error handling for edge cases
- [ ] Performance testing with larger graphs
- [ ] Add more layout algorithms (Phase 2)

## 🔧 Technical Decisions Made
1. ✅ Java bridge approach: **child_process** (clean separation, easier debugging)
2. ✅ Session management: **in-memory** with GephiContext singleton
3. ✅ Error handling: JSON responses with detailed error messages

## 🏗️ Architecture Overview
```
Node.js MCP Server (src/index.js)
    ↓ child_process spawn
Java Bridge Classes (java/src/main/java/com/gephi/mcp/)
    ↓ Gephi Toolkit API
Gephi Core (graph processing, layouts, import/export)
```

## Development Notes
- Gephi Toolkit version: 0.10.1
- Requires Java JDK 11+
- Uses Maven for Java dependency management
- JSON communication between Node.js and Java processes
- ES modules configuration in package.json

---
📋 **Full Requirements**: See [docs/prd.md](./docs/prd.md) for complete functional specifications.