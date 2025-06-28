#!/bin/bash

echo "Testing PNG export after Force Atlas 2 layout..."

# Get the project root directory (parent of test directory)
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(dirname "$SCRIPT_DIR")"

# Change to project root
cd "$PROJECT_ROOT"

# Test sequence: load graph -> apply layout -> save as PNG
{
    echo '{"jsonrpc": "2.0", "id": 1, "method": "tools/call", "params": {"name": "load_graph", "arguments": {"filePath": "'$PROJECT_ROOT'/test/sample.gexf", "format": "gexf"}}}'
    sleep 1
    echo '{"jsonrpc": "2.0", "id": 2, "method": "tools/call", "params": {"name": "apply_force_atlas2", "arguments": {"iterations": 100}}}'
    sleep 1
    echo '{"jsonrpc": "2.0", "id": 3, "method": "tools/call", "params": {"name": "save_graph", "arguments": {"filePath": "'$PROJECT_ROOT'/test/output_with_layout.png", "format": "png"}}}'
    sleep 1
} | node src/index.js

echo "PNG export test completed!"