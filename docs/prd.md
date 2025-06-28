# Gephi Toolkit MCP Server Requirements Document

## 1. Project Overview

### 1.1 Project Goals
Develop an MCP (Model Context Protocol) Server based on Gephi Toolkit that enables AI assistants to perform network graph analysis, visualization, and manipulation through standardized interfaces.

### 1.2 Technical Foundation
- **Gephi Toolkit**: Java library containing Gephi's core functionality (Graph, Layout, Filters, IO, etc.)
- **Version**: 0.10.1 (stable) or 0.10.2-SNAPSHOT (development)
- **MCP Protocol**: Standard protocol based on JSON-RPC
- **Runtime Environment**: Java JDK 11+, headless mode execution

## 2. Core Functional Modules

### 2.1 Graph Management
**Resources:**
- `graphs` - List all graphs in current workspace
- `graph/{graphId}` - Get detailed information of specific graph
- `graph/{graphId}/stats` - Get graph statistics (node count, edge count, etc.)

**Tools:**
- `create_graph` - Create new empty graph
- `load_graph` - Load graph from file (GEXF, GraphML, GML, DOT, CSV)
- `save_graph` - Save graph to file
- `clear_graph` - Clear current graph
- `get_graph_info` - Get basic graph information

### 2.2 Nodes & Edges Operations
**Tools:**
- `add_node` - Add single node
- `add_nodes` - Batch add nodes
- `remove_node` - Remove node
- `add_edge` - Add edge
- `remove_edge` - Remove edge
- `set_node_attribute` - Set node attributes
- `set_edge_attribute` - Set edge attributes
- `get_neighbors` - Get node neighbors

### 2.3 Layout Algorithms
**Tools:**
- `apply_force_atlas2` - Apply Force Atlas 2 layout
- `apply_yifan_hu` - Apply Yifan Hu layout
- `apply_fruchterman_reingold` - Apply Fruchterman Reingold layout
- `apply_circular` - Apply circular layout
- `apply_random` - Apply random layout
- `stop_layout` - Stop current layout
- `layout_step` - Execute single layout step

### 2.4 Network Analysis
**Tools:**
- `calculate_centrality` - Calculate centrality metrics (degree, betweenness, closeness)
- `detect_communities` - Community detection
- `calculate_clustering` - Calculate clustering coefficient
- `shortest_path` - Calculate shortest path
- `calculate_density` - Calculate network density
- `graph_metrics` - Get global network metrics

### 2.5 Filters
**Tools:**
- `filter_by_degree` - Filter nodes by degree
- `filter_by_attribute` - Filter by attribute
- `filter_by_edge_weight` - Filter by edge weight
- `remove_filter` - Remove filter
- `apply_topology_filter` - Apply topology filter

### 2.6 Visualization Settings
**Tools:**
- `set_node_color` - Set node color
- `set_node_size` - Set node size
- `set_edge_color` - Set edge color
- `set_edge_thickness` - Set edge thickness
- `rank_by_degree` - Rank by degree for color/size
- `rank_by_centrality` - Rank by centrality for color/size

### 2.7 Import/Export
**Tools:**
- `import_csv` - Import graph data from CSV
- `export_pdf` - Export as PDF
- `export_png` - Export as PNG
- `export_svg` - Export as SVG
- `export_gexf` - Export as GEXF format
- `export_graphml` - Export as GraphML format

## 3. Data Models

### 3.1 Graph Object
```json
{
  "id": "graph_1",
  "name": "Social Network",
  "nodeCount": 150,
  "edgeCount": 300,
  "directed": true,
  "type": "mixed",
  "lastModified": "2025-06-28T10:30:00Z"
}
```

### 3.2 Node Object
```json
{
  "id": "node_1",
  "label": "User A",
  "attributes": {
    "age": 25,
    "location": "Toronto",
    "degree": 15
  },
  "position": {
    "x": 100.5,
    "y": 200.3
  },
  "visual": {
    "color": "#FF5733",
    "size": 10
  }
}
```

### 3.3 Edge Object
```json
{
  "id": "edge_1",
  "source": "node_1",
  "target": "node_2", 
  "weight": 1.5,
  "type": "friendship",
  "directed": true,
  "attributes": {
    "created": "2024-01-15"
  }
}
```

## 4. Technical Implementation Details

### 4.1 Dependency Configuration
```xml
<dependency>
    <groupId>org.gephi</groupId>
    <artifactId>gephi-toolkit</artifactId>
    <version>0.10.1</version>
</dependency>
```

### 4.2 Core Java Class Design
```java
// Main controller class
public class GephiMCPServer {
    private ProjectController projectController;
    private GraphModel graphModel;
    private ImportController importController;
    private ExportController exportController;
    private FilterController filterController;
    private RankingController rankingController;
    private PreviewController previewController;
}
```

### 4.3 Key API Usage Patterns
Based on Gephi Toolkit standard patterns:
1. Initialize project and workspace
2. Get various controllers
3. Execute graph operations
4. Apply layouts and filters
5. Export results

## 5. MCP Server Architecture

### 5.1 Server Entry Point
```javascript
// Node.js MCP Server main entry
const server = new Server({
  name: "gephi-toolkit-mcp",
  version: "1.0.0"
}, {
  capabilities: {
    resources: {},
    tools: {}
  }
});
```

### 5.2 Java Bridge
Use Node.js `child_process` to call Java programs, or use GraalVM native images.

### 5.3 Session Management
- Each session maintains independent Gephi project
- Support multiple graphs simultaneously
- Provide undo/redo functionality

## 6. Usage Scenario Examples

### 6.1 Basic Network Analysis Workflow
```
1. load_graph("social_network.gexf")
2. calculate_centrality("betweenness")
3. filter_by_degree(min: 5)
4. apply_force_atlas2(iterations: 100)
5. rank_by_centrality("size")
6. export_pdf("result.pdf")
```

### 6.2 Social Network Analysis
```
1. import_csv("users.csv", "connections.csv")
2. detect_communities()
3. set_node_color(by: "community")
4. apply_yifan_hu()
5. export_png("communities.png")
```

## 7. Error Handling

### 7.1 Common Error Types
- File not found or invalid format
- Empty graph or non-existent nodes
- Invalid layout algorithm parameters
- Memory insufficient

### 7.2 Error Response Format
```json
{
  "error": {
    "code": "GRAPH_NOT_FOUND",
    "message": "Graph with ID 'graph_1' not found",
    "details": {
      "graphId": "graph_1",
      "availableGraphs": ["graph_2", "graph_3"]
    }
  }
}
```

## 8. Performance Considerations

### 8.1 Memory Management
- Chunked processing for large graphs
- Automatic garbage collection
- Memory usage monitoring

### 8.2 Asynchronous Processing
- Long-running layout algorithms execute asynchronously
- Progress reporting mechanism
- Task cancellation functionality

## 9. Extended Features

### 9.1 Plugin Support
Support Gephi-compatible plugins such as:
- OpenOrd layout
- Custom statistical algorithms
- Special format importers

### 9.2 Batch Processing Mode
- Batch processing of multiple graph files
- Templated workflows
- Automated report generation

## 10. Development Priorities

### Phase 1 (MVP)
1. Basic graph loading and saving
2. Basic layout algorithms (Force Atlas 2, Yifan Hu)
3. Simple export functionality (PDF, PNG)
4. Basic network statistics

### Phase 2 (Enhancement)
1. Filter functionality
2. Visualization customization
3. More layout algorithms
4. CSV import functionality

### Phase 3 (Advanced)
1. Community detection
2. Dynamic network support
3. Plugin system
4. Batch processing functionality

## 11. Testing Strategy

### 11.1 Unit Testing
- Functional testing for each MCP tool
- Boundary condition testing
- Error handling testing

### 11.2 Integration Testing
- Complete workflow testing
- Large dataset performance testing
- Multi-concurrent session testing

### 11.3 Sample Datasets
- Small-scale test graphs (<100 nodes)
- Medium-scale graphs (1000-10000 nodes)
- Real datasets (social networks, citation networks, etc.)

---

This requirements document provides a complete roadmap for developing the Gephi Toolkit MCP Server and can be used directly to begin actual development work.