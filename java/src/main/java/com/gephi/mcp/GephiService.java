package com.gephi.mcp;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 * Persistent Gephi service that maintains graph state across operations
 */
public class GephiService {
    private static Gson gson = new Gson();
    
    public static void main(String[] args) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
             PrintWriter writer = new PrintWriter(System.out, true)) {
            
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    JsonObject request = JsonParser.parseString(line).getAsJsonObject();
                    String operation = request.get("operation").getAsString();
                    JsonObject params = request.has("params") ? request.getAsJsonObject("params") : new JsonObject();
                    
                    JsonObject response = processOperation(operation, params);
                    writer.println(gson.toJson(response));
                    writer.flush();
                    
                } catch (Exception e) {
                    JsonObject errorResponse = new JsonObject();
                    errorResponse.addProperty("success", false);
                    errorResponse.addProperty("error", e.getMessage());
                    writer.println(gson.toJson(errorResponse));
                    writer.flush();
                }
            }
        } catch (Exception e) {
            System.err.println("Service error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static JsonObject processOperation(String operation, JsonObject params) {
        JsonObject response = new JsonObject();
        
        try {
            switch (operation) {
                case "load_graph":
                    String filePath = params.get("filePath").getAsString();
                    String format = params.has("format") ? params.get("format").getAsString() : "auto";
                    
                    GraphLoader.GraphResult result = GraphLoader.loadGraph(filePath, format);
                    response.addProperty("success", true);
                    response.addProperty("message", "Graph loaded successfully");
                    response.addProperty("nodeCount", result.getNodeCount());
                    response.addProperty("edgeCount", result.getEdgeCount());
                    break;
                    
                case "get_graph_info":
                    GraphInfo.GraphInfoResult infoResult = GraphInfo.getGraphInfo();
                    response.addProperty("success", true);
                    response.addProperty("nodeCount", infoResult.getNodeCount());
                    response.addProperty("edgeCount", infoResult.getEdgeCount());
                    response.addProperty("directed", infoResult.isDirected());
                    response.addProperty("status", infoResult.getStatus());
                    break;
                    
                case "apply_force_atlas2":
                    // Build args array for LayoutProcessor
                    String[] layoutArgs = buildLayoutArgs(params);
                    LayoutProcessor.LayoutResult layoutResult = LayoutProcessor.applyForceAtlas2(layoutArgs);
                    response.addProperty("success", true);
                    response.addProperty("message", "Layout applied successfully");
                    response.addProperty("iterations", layoutResult.getIterations());
                    break;
                    
                case "save_graph":
                    String outputPath = params.get("filePath").getAsString();
                    String outputFormat = params.get("format").getAsString();
                    
                    GraphExporter.ExportResult exportResult = GraphExporter.exportGraph(outputPath, outputFormat);
                    response.addProperty("success", true);
                    response.addProperty("message", "Graph exported successfully");
                    break;
                    
                case "ping":
                    response.addProperty("success", true);
                    response.addProperty("message", "Service is running");
                    break;
                    
                default:
                    response.addProperty("success", false);
                    response.addProperty("error", "Unknown operation: " + operation);
            }
        } catch (Exception e) {
            response.addProperty("success", false);
            response.addProperty("error", e.getMessage());
        }
        
        return response;
    }
    
    private static String[] buildLayoutArgs(JsonObject params) {
        // First arg is unused (was originally algorithm name)
        String iterations = String.valueOf(params.has("iterations") ? params.get("iterations").getAsInt() : 100);
        String adjustSizes = String.valueOf(params.has("adjustSizes") ? params.get("adjustSizes").getAsBoolean() : false);
        String barnesHut = String.valueOf(params.has("barnesHutOptimize") ? params.get("barnesHutOptimize").getAsBoolean() : true);
        String gravity = String.valueOf(params.has("gravity") ? params.get("gravity").getAsDouble() : 1.0);
        String scaling = String.valueOf(params.has("scalingRatio") ? params.get("scalingRatio").getAsDouble() : 2.0);
        
        return new String[]{"forceatlas2", iterations, adjustSizes, barnesHut, gravity, scaling};
    }
}