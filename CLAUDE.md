# CLAUDE.md - Development Context for Claude Code

## Project Overview
This project implements a Gephi Toolkit MCP Server. Full requirements and specifications are in [docs/prd.md](./docs/prd.md).

## Current Development Status
- **Phase**: MVP (Phase 1) - **COMPLETE & PRODUCTION READY** ✅
- **Status**: All core functionality implemented, tested, and deployed to GitHub
- **Architecture**: Node.js MCP Server + Java bridge to Gephi Toolkit (persistent service approach)

## ✅ Completed Tasks - MVP Implementation
- [x] Set up basic MCP server structure (`src/index.js`)
- [x] Implement Java bridge for Gephi Toolkit (Maven project in `java/`)
- [x] Create `load_graph` tool (supports GEXF, GraphML, GML, DOT, CSV)
- [x] Add `apply_force_atlas2` layout tool with full parameter support
- [x] Implement `get_graph_info` for statistics
- [x] Add `save_graph` export functionality (GEXF, GraphML, PDF, PNG)
- [x] Maven build configuration with Gephi Toolkit 0.10.1
- [x] Java bridge classes: GephiContext, GraphLoader, LayoutProcessor, GraphInfo, GraphExporter

## ✅ Bug Fixes & Improvements
- [x] **CRITICAL**: Fixed state persistence bug (removed `context.reset()` in GraphLoader.java)
- [x] **CRITICAL**: Fixed PNG export font casting error (Integer → Font object)
- [x] Built and tested Java components: `npm run build-java`
- [x] Comprehensive testing with sample files (4 nodes, 5 edges GEXF)
- [x] Validated Force Atlas 2 layout execution (50-300 iterations)
- [x] Confirmed PNG/GEXF export functionality
- [x] Added error handling for common edge cases
- [x] Performance tested with sample graphs

## ✅ Project Organization & Documentation
- [x] Reorganized file structure (moved test scripts to `test/` directory)
- [x] Created comprehensive README.md with modern design and emojis
- [x] Added test documentation (`test/README.md`)
- [x] Git repository initialization and GitHub deployment
- [x] Docker support (Dockerfile + .dockerignore)
- [x] NPM package configuration for distribution
- [x] Claude Desktop MCP configuration examples

## ✅ Testing & Validation
- [x] Created 4 comprehensive test scripts in `test/` directory
- [x] Validated complete workflow: load → info → layout → export
- [x] Fixed Claude Desktop MCP server configuration (Node.js path issues)
- [x] Confirmed PNG export creates 39-40KB files successfully
- [x] Tested state persistence across multiple operations
- [x] Verified no "No graph loaded" errors in production

## 📋 User Feedback & Findings
- [x] **Styling Issues Identified**: User reported oversized nodes and label fonts in PNG exports
- [x] **Root Cause Analysis**: Hardcoded styling parameters in `GraphExporter.java`
- [x] **Impact Assessment**: 8pt Arial font too large for small networks (4-20 nodes)
- [x] **Documentation Created**: Future styling API design (`docs/future-styling-api.md`)
- [x] **Priority Updated**: Visualization styling controls marked as highest priority for Phase 2

## 🔧 Technical Decisions Made
1. ✅ Java bridge approach: **persistent GephiService** (maintains state between calls)
2. ✅ Session management: **in-memory** with GephiContext singleton
3. ✅ Error handling: JSON responses with detailed error messages
4. ✅ Distribution: NPM package + Docker container options
5. ✅ File structure: Organized test/, src/, java/, docs/ directories

## 🏗️ Architecture Overview
```
Claude Desktop MCP Client
    ↓ stdio/JSON-RPC
Node.js MCP Server (src/index.js)
    ↓ persistent child_process + JSON
GephiService.java (persistent Java service)
    ↓ static method calls
Java Bridge Classes (GraphLoader, LayoutProcessor, GraphExporter, GraphInfo)
    ↓ GephiContext singleton
Gephi Toolkit API (graph processing, layouts, import/export)
```

## 🚀 Deployment Status
- **GitHub Repository**: https://github.com/Andy8647/gephi-toolkit-mcp
- **Git Status**: All code committed and pushed to main branch
- **Distribution Ready**: NPM package configuration complete
- **Docker Ready**: Containerization support available
- **Documentation**: Comprehensive README.md, test documentation, and future API design

## 📊 Test Results Summary
- **State Persistence**: ✅ Fixed - graphs persist across operations
- **PNG Export**: ✅ Working - generates 39-40KB output files (⚠️ styling needs improvement)
- **Layout Application**: ✅ Working - Force Atlas 2 with 50-300 iterations
- **Graph Loading**: ✅ Working - GEXF files load successfully (4 nodes, 5 edges)
- **Claude Desktop Integration**: ✅ Working - MCP server configured correctly
- **User Experience**: ⚠️ Identified - node sizes and label fonts too large for small networks

## Development Notes
- **Gephi Toolkit version**: 0.10.1
- **Java Runtime**: JDK 11+ required
- **Maven**: For Java dependency management and builds
- **Communication**: JSON-RPC between Node.js and persistent Java service
- **Module System**: ES modules configuration in package.json
- **State Management**: GephiContext singleton maintains graph data
- **Error Handling**: Comprehensive JSON error responses with stack traces

## 🎯 Future Enhancements (Phase 2+)

### High Priority
- [ ] **Visualization Styling Controls** 🎨
  - [ ] Configurable node sizes and colors
  - [ ] Adjustable label font sizes and visibility
  - [ ] Edge thickness and color customization
  - [ ] Export resolution settings (width/height)
  - [ ] Background color and transparency options

### Medium Priority  
- [ ] Additional layout algorithms (Fruchterman-Reingold, Circular, etc.)
- [ ] Graph filtering and transformation tools
- [ ] Advanced analytics (centrality measures, community detection)
- [ ] Batch processing capabilities

### Lower Priority
- [ ] Performance optimization for large graphs (>10K nodes)
- [ ] npm package publication
- [ ] Docker Hub image publication

## 🎨 Current Styling Limitations
**Note**: Current PNG exports use hardcoded styling in `GraphExporter.java`:
- **Node label font**: Arial, 8pt (too large for small graphs)
- **Export resolution**: Fixed 1024x768 pixels
- **Node sizes**: Determined by Gephi's default sizing
- **No user control**: All styling parameters are hardcoded

**User Request**: Need configurable node sizes and label fonts for better visualization control.

---
📋 **Full Requirements**: See [docs/prd.md](./docs/prd.md) for complete functional specifications.
📊 **GitHub Repository**: https://github.com/Andy8647/gephi-toolkit-mcp