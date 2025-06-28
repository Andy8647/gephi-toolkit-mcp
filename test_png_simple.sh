#!/bin/bash

echo "Testing PNG export after Force Atlas 2 layout..."

# Test sequence: load graph -> apply layout -> save as PNG
{
    echo '{"jsonrpc": "2.0", "id": 1, "method": "tools/call", "params": {"name": "load_graph", "arguments": {"file_path": "test/sample.gexf"}}}'
    sleep 1
    echo '{"jsonrpc": "2.0", "id": 2, "method": "tools/call", "params": {"name": "apply_force_atlas2", "arguments": {"iterations": 100}}}'
    sleep 1
    echo '{"jsonrpc": "2.0", "id": 3, "method": "tools/call", "params": {"name": "save_graph", "arguments": {"file_path": "test/output_with_layout.png", "format": "png"}}}'
    sleep 1
} | node src/index.js

echo "PNG export test completed!"