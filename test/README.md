# Test Scripts

This directory contains test scripts and sample data for the Gephi Toolkit MCP Server.

## Files

### Sample Data
- `sample.gexf` - Sample GEXF graph file with 4 nodes and 5 edges for testing

### Test Scripts
- `test_workflow.sh` - Complete workflow test (load → info → layout → export)
- `test_exact_sequence.sh` - Tests the exact sequence from user requirements
- `test_png_workflow.sh` - Tests PNG export specifically
- `test_png_simple.sh` - Simplified PNG export test

## Running Tests

Make sure the Java components are built first:

```bash
npm run build-java
```

Then run any test script:

```bash
# Run the main workflow test
./test/test_workflow.sh

# Test PNG export specifically
./test/test_png_workflow.sh
```

## Expected Results

All tests should complete without "No graph loaded" errors and generate output files in `/Users/andy/Downloads/` (or equivalent for your system).

The complete workflow should:
1. Load the sample graph (4 nodes, 5 edges)
2. Show graph info with correct counts
3. Apply Force Atlas 2 layout successfully
4. Export to PNG/GEXF format