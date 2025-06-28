package com.gephi.mcp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.gephi.graph.api.Graph;
import org.gephi.graph.api.GraphModel;
import org.gephi.io.importer.api.Container;
import org.gephi.io.importer.api.ImportController;
import org.gephi.io.processor.plugin.DefaultProcessor;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class GraphLoader {
    
    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Usage: GraphLoader <filePath> [format]");
            System.exit(1);
        }
        
        String filePath = args[0];
        String format = args.length > 1 ? args[1] : "auto";
        
        try {
            GraphResult result = loadGraph(filePath, format);
            
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            System.out.println(gson.toJson(result));
            
        } catch (Exception e) {
            System.err.println("Error loading graph: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    public static GraphResult loadGraph(String filePath, String format) throws Exception {
        GephiContext context = GephiContext.getInstance();
        
        File file = new File(filePath);
        if (!file.exists()) {
            throw new Exception("File not found: " + filePath);
        }
        
        ImportController importController = context.getImportController();
        
        // Import the file
        Container container;
        try {
            container = importController.importFile(file);
        } catch (Exception e) {
            throw new Exception("Failed to import file: " + e.getMessage());
        }
        
        if (container == null) {
            throw new Exception("Failed to load graph from file");
        }
        
        // Process the container
        container.getLoader().setEdgeDefault(org.gephi.io.importer.api.EdgeDirectionDefault.MIXED);
        
        // Apply to current workspace
        importController.process(container, new DefaultProcessor(), context.getWorkspace());
        
        // Get graph statistics
        GraphModel graphModel = context.getGraphModel();
        Graph graph = graphModel.getGraph();
        
        return new GraphResult(
            graph.getNodeCount(),
            graph.getEdgeCount(),
            graphModel.isDirected(),
            filePath
        );
    }
    
    public static class GraphResult {
        private final int nodeCount;
        private final int edgeCount;
        private final boolean directed;
        private final String source;
        private final String status;
        
        public GraphResult(int nodeCount, int edgeCount, boolean directed, String source) {
            this.nodeCount = nodeCount;
            this.edgeCount = edgeCount;
            this.directed = directed;
            this.source = source;
            this.status = "success";
        }
        
        public int getNodeCount() { return nodeCount; }
        public int getEdgeCount() { return edgeCount; }
        public boolean isDirected() { return directed; }
        public String getSource() { return source; }
        public String getStatus() { return status; }
    }
}