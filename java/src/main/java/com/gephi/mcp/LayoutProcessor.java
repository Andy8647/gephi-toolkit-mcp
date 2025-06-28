package com.gephi.mcp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.gephi.graph.api.Graph;
import org.gephi.graph.api.GraphModel;
import org.gephi.layout.plugin.forceAtlas2.ForceAtlas2;
import org.gephi.layout.plugin.forceAtlas2.ForceAtlas2Builder;
import org.gephi.layout.api.LayoutController;

public class LayoutProcessor {
    
    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Usage: LayoutProcessor <algorithm> [parameters...]");
            System.exit(1);
        }
        
        String algorithm = args[0];
        
        try {
            LayoutResult result;
            
            switch (algorithm.toLowerCase()) {
                case "forceatlas2":
                    result = applyForceAtlas2(args);
                    break;
                default:
                    throw new Exception("Unknown layout algorithm: " + algorithm);
            }
            
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            System.out.println(gson.toJson(result));
            
        } catch (Exception e) {
            System.err.println("Error applying layout: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    public static LayoutResult applyForceAtlas2(String[] args) throws Exception {
        GephiContext context = GephiContext.getInstance();
        GraphModel graphModel = context.getGraphModel();
        Graph graph = graphModel.getGraph();
        
        if (graph.getNodeCount() == 0) {
            throw new Exception("No graph loaded. Please load a graph first.");
        }
        
        // Parse parameters
        int iterations = args.length > 1 ? Integer.parseInt(args[1]) : 100;
        boolean adjustSizes = args.length > 2 ? Boolean.parseBoolean(args[2]) : false;
        boolean barnesHutOptimize = args.length > 3 ? Boolean.parseBoolean(args[3]) : true;
        double gravity = args.length > 4 ? Double.parseDouble(args[4]) : 1.0;
        double scalingRatio = args.length > 5 ? Double.parseDouble(args[5]) : 2.0;
        
        // Create and configure ForceAtlas2
        ForceAtlas2Builder layoutBuilder = new ForceAtlas2Builder();
        ForceAtlas2 layout = layoutBuilder.buildLayout();
        
        layout.setGraphModel(graphModel);
        layout.resetPropertiesValues();
        
        // Set parameters
        layout.setAdjustSizes(adjustSizes);
        layout.setBarnesHutOptimize(barnesHutOptimize);
        layout.setGravity(gravity);
        layout.setScalingRatio(scalingRatio);
        
        // Initialize layout
        layout.initAlgo();
        
        // Run layout for specified iterations
        for (int i = 0; i < iterations && layout.canAlgo(); i++) {
            layout.goAlgo();
        }
        
        // Finish layout
        layout.endAlgo();
        
        return new LayoutResult(
            "ForceAtlas2",
            iterations,
            graph.getNodeCount(),
            graph.getEdgeCount(),
            "completed"
        );
    }
    
    public static class LayoutResult {
        private final String algorithm;
        private final int iterations;
        private final int nodeCount;
        private final int edgeCount;
        private final String status;
        
        public LayoutResult(String algorithm, int iterations, int nodeCount, int edgeCount, String status) {
            this.algorithm = algorithm;
            this.iterations = iterations;
            this.nodeCount = nodeCount;
            this.edgeCount = edgeCount;
            this.status = status;
        }
        
        public String getAlgorithm() { return algorithm; }
        public int getIterations() { return iterations; }
        public int getNodeCount() { return nodeCount; }
        public int getEdgeCount() { return edgeCount; }
        public String getStatus() { return status; }
    }
}