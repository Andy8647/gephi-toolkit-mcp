# Gephi Toolkit MCP Server Requirements Document

## 1. Project Overview

### 1.1 Project Goals

#### Primary Goal (Current Phase)
Develop an MCP (Model Context Protocol) Server as a **learning and experimentation project** to understand MCP protocol implementation, Gephi Toolkit integration, and multi-language bridging architecture.

#### Long-term Vision (Future Research Direction)
Explore **LLM-driven intelligent visualization** - leveraging AI to automatically generate optimal graph visualizations based on natural language user intent and automatic data analysis, rather than traditional manual parameter tuning.

### 1.2 Project Positioning & Value Assessment

#### ❌ What This Project is NOT
- **Gephi GUI Replacement**: Cannot provide real-time visual feedback and interactive editing
- **1:1 Feature Replication**: Manually recreating all Gephi features provides limited value
- **Production Visualization Tool**: Lacks the precision control needed for publication-quality graphics

#### ✅ Unique Value Proposition (Future Direction)
**Intelligent Visualization Assistant** - Instead of requiring users to manually configure:
- Node sizes, colors, positions
- Layout algorithm parameters  
- Visual styling decisions

**AI automatically determines optimal visualization strategy based on:**
- User's natural language description of intent
- Automatic analysis of network characteristics
- Best practices for specific visualization goals

Example:
```
User: "Show me the most influential people in this social network"
AI Automatically:
- Calculates degree & betweenness centrality
- Sets node size proportional to influence
- Colors nodes by community membership
- Applies layout that highlights central nodes
- Optimizes parameters to avoid overlaps
```

### 1.3 Technical Foundation
- **Gephi Toolkit**: Java library containing Gephi's core functionality (Graph, Layout, Filters, IO, etc.)
- **Version**: 0.10.1 (stable) or 0.10.2-SNAPSHOT (development)
- **MCP Protocol**: Standard protocol based on JSON-RPC
- **Runtime Environment**: Java JDK 11+, headless mode execution
- **Architecture**: Node.js MCP Server ↔ Java Bridge ↔ Gephi Toolkit

### 1.4 Intelligent Visualization Concept (Future Research)

#### Core Innovation: Intent-Driven Visualization
Traditional workflow: `Data → Manual Analysis → Trial & Error → Visualization`
Proposed workflow: `Data + User Intent → AI Analysis → Optimal Visualization`

#### Intelligent Styling Engine Design
```java
class IntelligentStylist {
    public StyleConfig analyzeAndStyle(Graph graph, String userIntent) {
        // 1. Analyze network characteristics
        NetworkFeatures features = analyzeNetwork(graph);
        
        // 2. Parse user visualization intent
        VisualizationGoal goal = parseIntent(userIntent);
        
        // 3. Generate optimal styling strategy
        return generateOptimalStyle(features, goal);
    }
}
```

#### Intent Categories & Auto-Strategies
| User Intent | Auto-Generated Strategy |
|-------------|------------------------|
| "Find key influencers" | Node size = centrality, Color = community, Highlight layout |
| "Show information flow" | Edge thickness = weight, Directional arrows, Hierarchical layout |
| "Compare groups" | Color = group membership, Separated positioning, Size = intra-group importance |
| "Detect communities" | Color = community ID, Clustered layout, Edge bundling |
| "Timeline evolution" | Color gradient = time, Animation sequence, Fade old connections |

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

### 6.1 Current Implementation (Phase 1) ✅
**Traditional Parameter-Based Approach**
```
1. load_graph("social_network.gexf")
2. calculate_centrality("betweenness")
3. filter_by_degree(min: 5)
4. apply_force_atlas2(iterations: 100)
5. rank_by_centrality("size")
6. export_pdf("result.pdf")
```

### 6.2 Enhanced Analysis Toolkit (Phase 2) 🎯
**Focus on Analysis Over Visualization**
```
User: "Analyze this corporate communication network"
1. load_graph("communications.json")
2. analyze_network_structure()
   → Returns: scale-free network, 3 major communities
3. identify_key_players()
   → Returns: top 10 influential employees with metrics
4. detect_information_bottlenecks()
   → Returns: critical connection points
5. export_analysis_report("corporate_analysis.pdf")
```

### 6.3 Future Intelligent Visualization (Phase 3+) 🔬
**Intent-Driven Automation**
```
User: "Show me who controls information flow in this organization"

Current Approach (Manual):
1. load_graph() 
2. calculate_betweenness_centrality()
3. calculate_degree_centrality() 
4. manually_set_node_sizes()
5. manually_choose_colors()
6. apply_layout()
7. manually_adjust_parameters()
8. export_and_check()
9. repeat 4-8 until satisfied

Intelligent Approach (Proposed):
User: "Show me who controls information flow in this organization"
↓
AI System:
1. Auto-analyzes network → Identifies hierarchical structure
2. Auto-selects strategy → Flow analysis requires betweenness centrality
3. Auto-styles visualization → Size=betweenness, Color=department, Layout=hierarchical
4. Auto-optimizes parameters → Prevents overlaps, ensures readability
5. Generates visualization → "Here are your information bottlenecks"
```

### 6.4 Comparative Value Demonstration
| Approach | User Experience | Time to Insight | Expertise Required |
|----------|-----------------|-----------------|-------------------|
| **Gephi GUI** | Interactive, visual | Fast for experts | High - needs network analysis knowledge |
| **Traditional MCP** | Programmatic, blind | Slow iteration | High - needs to know exact parameters |
| **Intelligent MCP** | Conversational | Fast for everyone | Low - just describe what you want to see |

### 6.5 Real-World Intelligent Scenarios (Research Goals)
```
"Find the most controversial topics in this discussion network"
→ AI: Detects bridge nodes between communities, highlights polarizing content

"Show how this epidemic spread through the contact network"  
→ AI: Time-based coloring, flow visualization, identifies super-spreaders

"Which research collaborations are most productive?"
→ AI: Edge thickness=publications, node size=impact, clustering by field

"Detect fraud patterns in this transaction network"
→ AI: Anomaly detection, suspicious pattern highlighting, risk scoring
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

## 10. Development Priorities (Revised Based on Value Assessment)

### Phase 1 (MVP - Learning Project) ✅ **COMPLETED**
**Goal**: Establish working MCP architecture and basic functionality
1. ✅ Basic graph loading and saving (GEXF, GraphML, CSV, etc.)
2. ✅ Force Atlas 2 layout algorithm
3. ✅ Simple export functionality (PDF, PNG)
4. ✅ Basic network statistics
5. ✅ Java-Node.js bridge architecture
6. ✅ Docker deployment support

### Phase 2 (Foundational Analysis Tools)
**Focus**: Build analysis capabilities that provide actual value over Gephi GUI
1. **Network Analysis Toolkit**
   - Centrality calculations (degree, betweenness, closeness)
   - Community detection algorithms
   - Network metrics (clustering coefficient, density, path lengths)
2. **Data Processing & Format Support**
   - Enhanced CSV support (separate node/edge files)
   - JSON import/export for modern applications
   - Batch processing of multiple files
3. **Basic Intelligence**
   - Automatic parameter selection for common scenarios
   - Data-driven layout optimization

### Phase 3 (Intelligent Visualization Research) 🔬 **EXPERIMENTAL**
**Goal**: Explore AI-driven visualization automation
1. **Intent Parsing Engine**
   - Natural language processing for visualization goals
   - Intent categorization (influence, flow, communities, etc.)
2. **Network Feature Analysis**
   - Automatic detection of network characteristics
   - Scale-aware parameter optimization
   - Quality metrics for layouts
3. **Intelligent Styling System**
   - Auto-assignment of visual properties based on intent
   - Context-aware color/size/position decisions
   - Best practices enforcement

### Phase 4 (Production Intelligence) 🚀 **FUTURE VISION**
**Goal**: Deploy intelligent visualization as a service
1. **Advanced AI Integration**
   - LLM-powered intent understanding
   - Multi-objective optimization
   - Style transfer learning
2. **Workflow Automation**
   - Template-based analysis pipelines
   - Comparative analysis across datasets
   - Automated report generation
3. **Integration Ecosystem**
   - Gephi GUI integration (export optimized starting points)
   - Web-based visualization preview
   - API for embedding in other tools

### Development Philosophy
- **Phase 1**: ✅ Completed - Successful learning project
- **Phase 2**: 🎯 Focus on unique value - What Gephi GUI can't do well
- **Phase 3**: 🔬 Research-oriented - Explore intelligent automation
- **Phase 4**: 🚀 Production-ready intelligent visualization system

### Current Recommendation
**Stay in Phase 2** - Build analysis tools that complement rather than compete with Gephi GUI. Only proceed to Phase 3 if there's clear evidence of user demand for intelligent visualization automation.

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

## 12. Project Summary & Conclusions

### ✅ Current Status (Phase 1 Complete)
This Gephi Toolkit MCP Server has successfully achieved its **primary educational goals**:
- Working MCP protocol implementation
- Functional Java-Node.js bridge architecture  
- Integration with Gephi Toolkit for graph processing
- Docker deployment and comprehensive documentation

### 🤔 Value Assessment & Lessons Learned
Through development and analysis, we discovered that:

**❌ Limited Value Scenarios:**
- 1:1 replication of Gephi GUI features provides minimal benefit
- Blind parameter tuning without visual feedback is frustrating
- Traditional visualization workflows don't leverage AI capabilities

**✅ Potential High-Value Scenarios:**
- Automated analysis and insight generation
- Batch processing and workflow automation
- **Intelligent visualization**: AI-driven styling based on user intent

### 🔮 Future Research Direction
The most promising path forward is **LLM-driven intelligent visualization**:
- Users describe what they want to see in natural language
- AI automatically analyzes data characteristics and applies optimal visualization strategies
- This could solve the fundamental "blind visualization" problem

### 📚 Educational Value Achieved
As a learning project, this delivered:
- Deep understanding of MCP protocol design and implementation
- Experience with multi-language integration (Node.js ↔ Java)
- Knowledge of Gephi Toolkit capabilities and limitations
- Docker containerization and deployment practices
- Graph analysis algorithm integration

### 🎯 Recommendation
**Keep as completed learning project** unless there's specific interest in pursuing the intelligent visualization research direction. The foundation is solid for future experimentation with AI-driven graph visualization automation.

---

*This requirements document captures both the implemented system and the research insights gained during development, providing a complete roadmap for potential future work in intelligent network visualization.*