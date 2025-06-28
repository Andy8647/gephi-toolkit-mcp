package com.gephi.mcp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.gephi.graph.api.Graph;
import org.gephi.graph.api.GraphModel;
import org.gephi.io.exporter.api.ExportController;
import org.gephi.io.exporter.plugin.ExporterGEXF;
import org.gephi.io.exporter.plugin.ExporterGraphML;
import org.gephi.preview.api.PreviewController;
import org.gephi.preview.api.PreviewModel;
import org.gephi.preview.api.PreviewProperty;
import org.gephi.io.exporter.preview.PDFExporter;
import org.gephi.io.exporter.preview.PNGExporter;
import org.openide.util.Lookup;

import java.io.File;

public class GraphExporter {
    
    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Usage: GraphExporter <filePath> <format>");
            System.exit(1);
        }
        
        String filePath = args[0];
        String format = args[1].toLowerCase();
        
        try {
            ExportResult result = exportGraph(filePath, format);
            
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            System.out.println(gson.toJson(result));
            
        } catch (Exception e) {
            System.err.println("Error exporting graph: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    public static ExportResult exportGraph(String filePath, String format) throws Exception {
        GephiContext context = GephiContext.getInstance();
        GraphModel graphModel = context.getGraphModel();
        Graph graph = graphModel.getGraph();
        
        if (graph.getNodeCount() == 0) {
            throw new Exception("No graph loaded. Please load a graph first.");
        }
        
        File file = new File(filePath);
        ExportController exportController = context.getExportController();
        
        switch (format) {
            case "gexf":
                exportGEXF(exportController, graphModel, file);
                break;
            case "graphml":
                exportGraphML(exportController, graphModel, file);
                break;
            case "pdf":
                exportPDF(context, file);
                break;
            case "png":
                exportPNG(context, file);
                break;
            default:
                throw new Exception("Unsupported export format: " + format);
        }
        
        return new ExportResult(
            filePath,
            format,
            graph.getNodeCount(),
            graph.getEdgeCount(),
            file.length(),
            "success"
        );
    }
    
    private static void exportGEXF(ExportController exportController, GraphModel graphModel, File file) throws Exception {
        ExporterGEXF exporter = new ExporterGEXF();
        exporter.setExportVisible(true);
        exporter.setWorkspace(GephiContext.getInstance().getWorkspace());
        exportController.exportFile(file, exporter);
    }
    
    private static void exportGraphML(ExportController exportController, GraphModel graphModel, File file) throws Exception {
        ExporterGraphML exporter = new ExporterGraphML();
        exporter.setExportVisible(true);
        exporter.setWorkspace(GephiContext.getInstance().getWorkspace());
        exportController.exportFile(file, exporter);
    }
    
    private static void exportPDF(GephiContext context, File file) throws Exception {
        // Configure preview
        PreviewController previewController = Lookup.getDefault().lookup(PreviewController.class);
        PreviewModel previewModel = previewController.getModel();
        
        previewModel.getProperties().putValue(PreviewProperty.SHOW_NODE_LABELS, Boolean.TRUE);
        previewModel.getProperties().putValue(PreviewProperty.SHOW_EDGE_LABELS, Boolean.FALSE);
        previewModel.getProperties().putValue(PreviewProperty.NODE_LABEL_FONT, new java.awt.Font("Arial", java.awt.Font.PLAIN, 8));
        
        // Export PDF
        ExportController exportController = context.getExportController();
        PDFExporter pdfExporter = (PDFExporter) exportController.getExporter("pdf");
        pdfExporter.setWorkspace(context.getWorkspace());
        exportController.exportFile(file, pdfExporter);
    }
    
    private static void exportPNG(GephiContext context, File file) throws Exception {
        // Configure preview
        PreviewController previewController = Lookup.getDefault().lookup(PreviewController.class);
        PreviewModel previewModel = previewController.getModel();
        
        previewModel.getProperties().putValue(PreviewProperty.SHOW_NODE_LABELS, Boolean.TRUE);
        previewModel.getProperties().putValue(PreviewProperty.SHOW_EDGE_LABELS, Boolean.FALSE);
        previewModel.getProperties().putValue(PreviewProperty.NODE_LABEL_FONT, new java.awt.Font("Arial", java.awt.Font.PLAIN, 8));
        
        // Export PNG
        ExportController exportController = context.getExportController();
        PNGExporter pngExporter = (PNGExporter) exportController.getExporter("png");
        pngExporter.setWorkspace(context.getWorkspace());
        pngExporter.setWidth(1024);
        pngExporter.setHeight(768);
        exportController.exportFile(file, pngExporter);
    }
    
    public static class ExportResult {
        private final String filePath;
        private final String format;
        private final int nodeCount;
        private final int edgeCount;
        private final long fileSize;
        private final String status;
        
        public ExportResult(String filePath, String format, int nodeCount, int edgeCount, long fileSize, String status) {
            this.filePath = filePath;
            this.format = format;
            this.nodeCount = nodeCount;
            this.edgeCount = edgeCount;
            this.fileSize = fileSize;
            this.status = status;
        }
        
        public String getFilePath() { return filePath; }
        public String getFormat() { return format; }
        public int getNodeCount() { return nodeCount; }
        public int getEdgeCount() { return edgeCount; }
        public long getFileSize() { return fileSize; }
        public String getStatus() { return status; }
    }
}