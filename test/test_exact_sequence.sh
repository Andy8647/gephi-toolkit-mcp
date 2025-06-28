#!/bin/bash

echo "Testing exact user sequence to reproduce bug..."

# Get the project root directory (parent of test directory)
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(dirname "$SCRIPT_DIR")"

# Change to project root
cd "$PROJECT_ROOT"

# Start the MCP server and pipe commands to it
(
  echo '{"jsonrpc": "2.0", "id": 1, "method": "tools/call", "params": {"name": "load_graph", "arguments": {"filePath": "/Users/andy/Downloads/sample.gexf", "format": "gexf"}}}'
  sleep 3
  echo '{"jsonrpc": "2.0", "id": 2, "method": "tools/call", "params": {"name": "get_graph_info", "arguments": {}}}'
  sleep 2
  echo '{"jsonrpc": "2.0", "id": 3, "method": "tools/call", "params": {"name": "apply_force_atlas2", "arguments": {"iterations": 300, "gravity": 1.0}}}'
  sleep 3
  echo '{"jsonrpc": "2.0", "id": 4, "method": "tools/call", "params": {"name": "save_graph", "arguments": {"filePath": "/Users/andy/Downloads/test_output.png", "format": "png"}}}'
  sleep 2
) | node src/index.js