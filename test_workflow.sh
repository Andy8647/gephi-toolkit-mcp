#!/bin/bash

# Test script to demonstrate persistent state in single session
echo "Testing Gephi Toolkit MCP with persistent Java service..."

# Start the MCP server and pipe commands to it
(
  echo '{"jsonrpc": "2.0", "id": 1, "method": "tools/call", "params": {"name": "load_graph", "arguments": {"filePath": "/Users/andy/Documents/GitHub/gephi-toolkit-mcp/test/sample.gexf", "format": "gexf"}}}'
  sleep 3
  echo '{"jsonrpc": "2.0", "id": 2, "method": "tools/call", "params": {"name": "get_graph_info", "arguments": {}}}'
  sleep 2
  echo '{"jsonrpc": "2.0", "id": 3, "method": "tools/call", "params": {"name": "apply_force_atlas2", "arguments": {"iterations": 50}}}'
  sleep 3
  echo '{"jsonrpc": "2.0", "id": 4, "method": "tools/call", "params": {"name": "save_graph", "arguments": {"filePath": "/Users/andy/Documents/GitHub/gephi-toolkit-mcp/test/output_with_layout.png", "format": "png"}}}'
  sleep 2
) | node src/index.js