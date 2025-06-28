# 🌐 Gephi Toolkit MCP Server

> **Transform complex networks into insights with the power of Gephi and Claude**

[![Node.js](https://img.shields.io/badge/Node.js->=16.0.0-green.svg)](https://nodejs.org/)
[![Java](https://img.shields.io/badge/Java-11+-orange.svg)](https://openjdk.org/)
[![MCP](https://img.shields.io/badge/MCP-Compatible-blue.svg)](https://modelcontextprotocol.io/)
[![License](https://img.shields.io/badge/License-ISC-lightgrey.svg)](LICENSE)

A powerful **Model Context Protocol (MCP) server** that bridges [Gephi Toolkit](https://gephi.org/toolkit/) with Claude Desktop, enabling advanced graph analysis and visualization through natural language interactions.

## ✨ Features

- 🔗 **Graph Loading**: Support for GEXF, GraphML, GML, DOT, and CSV formats
- 🎯 **Force Atlas 2 Layout**: Advanced graph layouting with customizable parameters
- 📊 **Graph Analytics**: Real-time statistics and network metrics
- 🖼️ **Export Capabilities**: Generate PNG images and PDF visualizations
- 🔄 **Persistent State**: Maintain graph data across multiple operations
- 🐳 **Docker Ready**: Containerized deployment support
- 🚀 **High Performance**: Java-powered processing with Node.js flexibility

## 🚀 Installation Options

Choose your preferred installation method:

| Method | Pros | Cons | Best For |
|--------|------|------|----------|
| 🖥️ **Local** | Full control, faster development | Requires dependencies | Developers, customization |
| 🐳 **Docker** | Isolated, no dependencies | Slightly slower startup | Production, clean environment |

### 🤔 Which Method Should I Choose?

**Choose Local Installation if:**
- ✅ You're comfortable installing Node.js, Java, and Maven
- ✅ You want the fastest performance
- ✅ You plan to modify or develop the code
- ✅ You're working in a development environment

**Choose Docker Installation if:**
- ✅ You want a quick, isolated setup
- ✅ You don't want to install dependencies on your system
- ✅ You're setting up for production use
- ✅ You're new to development environments
- ✅ You want a "just works" solution

---

## 📦 Option A: Local Installation

### Prerequisites
- **Node.js** 16.0.0 or higher
- **Java** 11 or higher (JDK/JRE)
- **Maven** 3.6+ (for building Java components)
- **Claude Desktop** (for MCP integration)

### Installation Steps

#### 1. Clone & Install
```bash
git clone https://github.com/Andy8647/gephi-toolkit-mcp.git
cd gephi-toolkit-mcp
npm install
```

#### 2. Build Java Components
```bash
npm run build
```

#### 3. Configure Claude Desktop
Add to your `claude_desktop_config.json`:

```json
{
  "mcpServers": {
    "gephi-toolkit": {
      "command": "npx",
      "args": ["gephi-toolkit-mcp"],
      "cwd": "/path/to/gephi-toolkit-mcp"
    }
  }
}
```

---

## 🐳 Option B: Docker Installation

### Prerequisites
- **Docker** (latest version)
- **Claude Desktop** (for MCP integration)

### Installation Steps

#### 1. Clone Repository
```bash
git clone https://github.com/Andy8647/gephi-toolkit-mcp.git
cd gephi-toolkit-mcp
```

#### 2. Build Docker Image
```bash
docker build -t gephi-toolkit-mcp .
```

#### 3. Configure Claude Desktop
Add to your `claude_desktop_config.json`:

```json
{
  "mcpServers": {
    "gephi-toolkit": {
      "command": "docker",
      "args": ["run", "--rm", "-i", "-v", "$(pwd):/workspace", "gephi-toolkit-mcp"]
    }
  }
}
```

> **💡 Tip**: The `-v $(pwd):/workspace` mount allows access to files in your current directory

---

## 🎉 Start Analyzing!

Open Claude Desktop and start working with graphs:

```
Load my network data from networks.gexf, apply Force Atlas 2 layout, 
and export it as a PNG visualization.
```

### 📁 File Path Notes

| Installation | File Access | Example |
|--------------|-------------|---------|
| **Local** | Direct file system access | `./data/network.gexf` |
| **Docker** | Access mounted directories | `/workspace/network.gexf` |

> **Docker Users**: Place your graph files in the directory where you run the docker command, they'll be accessible at `/workspace/` inside the container.

## 🎮 Available Tools

| Tool | Description | Example |
|------|-------------|---------|
| 🔗 `load_graph` | Load graph from file | Load social network data |
| 📊 `get_graph_info` | Get graph statistics | Show node/edge counts |
| 🎯 `apply_force_atlas2` | Apply layout algorithm | Organize network layout |
| 💾 `save_graph` | Export visualization | Generate PNG/PDF output |

## 📁 Project Structure

```
gephi-toolkit-mcp/
├── 🚀 src/
│   └── index.js              # MCP server entry point
├── ☕ java/                  # Java bridge components
│   ├── pom.xml              # Maven configuration
│   └── src/main/java/       # Gephi Toolkit integration
├── 🧪 test/                 # Test scripts & sample data
│   ├── sample.gexf          # Sample graph file
│   └── test_*.sh            # Automated tests
├── 🐳 Dockerfile            # Container configuration
├── 🐳 .dockerignore         # Docker build exclusions
├── 📦 package.json          # Node.js dependencies
└── 📚 docs/                 # Documentation
```

## 🧪 Testing

Run comprehensive tests to verify functionality:

```bash
# Test complete workflow
./test/test_workflow.sh

# Test PNG export specifically
./test/test_png_workflow.sh

# Test exact user scenarios
./test/test_exact_sequence.sh
```

## 🐳 Docker Commands Reference

Quick reference for Docker operations:

```bash
# Build image
docker build -t gephi-toolkit-mcp .

# Run interactively with file access
docker run -i --rm -v $(pwd):/workspace gephi-toolkit-mcp

# Run with specific directory mount
docker run -i --rm -v /path/to/data:/workspace gephi-toolkit-mcp

# Check if image built correctly
echo '{"jsonrpc":"2.0","id":1,"method":"initialize","params":{"protocolVersion":"2024-11-05","capabilities":{},"clientInfo":{"name":"test","version":"1.0"}}}' | docker run -i --rm gephi-toolkit-mcp
```

## 📖 Usage Examples

### Basic Network Analysis

```
1. Load graph: "Load the social network from data/network.gexf"
2. Analyze: "Show me the basic statistics of this network"
3. Layout: "Apply Force Atlas 2 with 500 iterations and gravity 2.0"
4. Export: "Save the visualization as network_analysis.png"
```

### Advanced Workflows

```
Load my citation network, apply a force-directed layout with 
Barnes-Hut optimization, and export both a high-resolution PNG 
and a PDF for publication.
```

## 🔧 Configuration

### Force Atlas 2 Parameters

| Parameter | Default | Description |
|-----------|---------|-------------|
| `iterations` | 100 | Number of layout iterations |
| `gravity` | 1.0 | Attraction strength to center |
| `scalingRatio` | 2.0 | Node repulsion strength |
| `adjustSizes` | false | Prevent node overlap |
| `barnesHutOptimize` | true | Performance optimization |

### Supported Formats

| Input | Output | Description |
|-------|--------|-------------|
| ✅ GEXF | ✅ PNG | Standard graph formats |
| ✅ GraphML | ✅ PDF | Vector graphics |
| ✅ GML | ✅ GEXF | Re-export with layout |
| ✅ DOT | ✅ GraphML | Cross-format conversion |
| ✅ CSV | | Edge/node lists |

## 🤝 Contributing

We welcome contributions! Here's how to get started:

1. 🍴 **Fork** the repository
2. 🌿 **Create** a feature branch: `git checkout -b feature/amazing-feature`
3. ✅ **Test** your changes: `npm run test`
4. 💾 **Commit** with descriptive messages
5. 🚀 **Push** and create a Pull Request

## 🐛 Troubleshooting

### Common Issues

**❌ "No graph loaded" errors**
- Ensure file paths are absolute
- Check file format compatibility
- Verify Java components are built

**❌ Font casting errors in PNG export**
- Update to latest version (fixed in v1.0.0+)
- Rebuild Java components: `npm run build`

**❌ Java process fails**
- Verify Java 11+ is installed
- Check JAVA_HOME environment variable
- Ensure Maven dependencies are resolved

### Debug Mode

Enable verbose logging:

```bash
DEBUG=gephi-toolkit:* npm start
```

## 📚 Documentation

- 📖 [Full API Documentation](docs/)
- 🎯 [Force Atlas 2 Guide](docs/force-atlas-2.md)
- 🔧 [Configuration Reference](docs/configuration.md)
- 🐳 [Docker Deployment](docs/docker.md)

## 📄 License

This project is licensed under the **ISC License** - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- **Gephi Team** for the amazing graph visualization toolkit
- **Anthropic** for the Model Context Protocol
- **Contributors** who make this project possible

## 🔗 Links

- 🌐 **Gephi**: https://gephi.org/
- 📘 **MCP Protocol**: https://modelcontextprotocol.io/
- 🤖 **Claude Desktop**: https://claude.ai/
- 📊 **Force Atlas 2**: https://journals.plos.org/plosone/article?id=10.1371/journal.pone.0098679

---

<div align="center">

**Made with ❤️ for the graph analysis community**

[⭐ Star this repo](https://github.com/Andy8647/gephi-toolkit-mcp) • [🐛 Report Bug](https://github.com/Andy8647/gephi-toolkit-mcp/issues) • [💡 Request Feature](https://github.com/Andy8647/gephi-toolkit-mcp/issues)

</div>