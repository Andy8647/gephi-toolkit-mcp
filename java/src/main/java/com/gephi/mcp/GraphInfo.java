package com.gephi.mcp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.gephi.graph.api.Graph;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.Node;
import org.gephi.graph.api.Edge;

import java.util.HashMap;
import java.util.Map;

public class GraphInfo {
    
    public static void main(String[] args) {
        try {
            GraphInfoResult result = getGraphInfo();
            
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            System.out.println(gson.toJson(result));
            
        } catch (Exception e) {
            System.err.println("Error getting graph info: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    public static GraphInfoResult getGraphInfo() throws Exception {
        GephiContext context = GephiContext.getInstance();
        GraphModel graphModel = context.getGraphModel();
        Graph graph = graphModel.getGraph();
        
        if (graph.getNodeCount() == 0) {
            return new GraphInfoResult(0, 0, false, "empty", null);
        }
        
        // Calculate basic statistics
        int nodeCount = graph.getNodeCount();
        int edgeCount = graph.getEdgeCount();
        boolean isDirected = graphModel.isDirected();
        
        // Calculate degree statistics
        Map<String, Object> degreeStats = calculateDegreeStats(graph);
        
        return new GraphInfoResult(
            nodeCount,
            edgeCount,
            isDirected,
            "loaded",
            degreeStats
        );
    }
    
    private static Map<String, Object> calculateDegreeStats(Graph graph) {
        Map<String, Object> stats = new HashMap<>();
        
        int minDegree = Integer.MAX_VALUE;
        int maxDegree = 0;
        int totalDegree = 0;
        
        for (Node node : graph.getNodes()) {
            int degree = graph.getDegree(node);
            totalDegree += degree;
            minDegree = Math.min(minDegree, degree);
            maxDegree = Math.max(maxDegree, degree);
        }
        
        double avgDegree = graph.getNodeCount() > 0 ? (double) totalDegree / graph.getNodeCount() : 0;
        
        stats.put("minDegree", minDegree == Integer.MAX_VALUE ? 0 : minDegree);
        stats.put("maxDegree", maxDegree);
        stats.put("avgDegree", avgDegree);
        stats.put("totalDegree", totalDegree);
        
        return stats;
    }
    
    public static class GraphInfoResult {
        private final int nodeCount;
        private final int edgeCount;
        private final boolean directed;
        private final String status;
        private final Map<String, Object> degreeStats;
        
        public GraphInfoResult(int nodeCount, int edgeCount, boolean directed, String status, Map<String, Object> degreeStats) {
            this.nodeCount = nodeCount;
            this.edgeCount = edgeCount;
            this.directed = directed;
            this.status = status;
            this.degreeStats = degreeStats;
        }
        
        public int getNodeCount() { return nodeCount; }
        public int getEdgeCount() { return edgeCount; }
        public boolean isDirected() { return directed; }
        public String getStatus() { return status; }
        public Map<String, Object> getDegreeStats() { return degreeStats; }
    }
}