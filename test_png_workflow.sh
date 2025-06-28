#!/bin/bash

echo "Testing PNG export after Force Atlas 2 layout..."

# Start the MCP server in background
node src/index.js &
SERVER_PID=$!

# Give server time to start
sleep 2

# Test sequence: load graph -> apply layout -> save as PNG
{
    echo '{"jsonrpc": "2.0", "id": 1, "method": "tools/call", "params": {"name": "load_graph", "arguments": {"file_path": "test/sample.gexf"}}}'
    echo '{"jsonrpc": "2.0", "id": 2, "method": "tools/call", "params": {"name": "apply_force_atlas2", "arguments": {"iterations": 100}}}'
    echo '{"jsonrpc": "2.0", "id": 3, "method": "tools/call", "params": {"name": "save_graph", "arguments": {"file_path": "test/output_with_layout.png", "format": "png"}}}'
} | node src/index.js

# Clean up
kill $SERVER_PID 2>/dev/null

echo "PNG export test completed!"