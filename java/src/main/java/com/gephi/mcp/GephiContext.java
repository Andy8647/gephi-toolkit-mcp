package com.gephi.mcp;

import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.gephi.io.importer.api.ImportController;
import org.gephi.io.processor.plugin.DefaultProcessor;
import org.gephi.layout.plugin.forceAtlas2.ForceAtlas2;
import org.gephi.layout.plugin.forceAtlas2.ForceAtlas2Builder;
import org.gephi.layout.api.LayoutController;
import org.gephi.io.exporter.api.ExportController;
import org.openide.util.Lookup;

public class GephiContext {
    private static GephiContext instance;
    
    private ProjectController projectController;
    private Workspace workspace;
    private GraphModel graphModel;
    private ImportController importController;
    private ExportController exportController;
    private LayoutController layoutController;
    
    private GephiContext() {
        initialize();
    }
    
    public static synchronized GephiContext getInstance() {
        if (instance == null) {
            instance = new GephiContext();
        }
        return instance;
    }
    
    private void initialize() {
        // Initialize controllers
        projectController = Lookup.getDefault().lookup(ProjectController.class);
        projectController.newProject();
        workspace = projectController.getCurrentWorkspace();
        
        // Get the GraphModel for current workspace
        GraphController graphController = Lookup.getDefault().lookup(GraphController.class);
        graphModel = graphController.getGraphModel();
        
        // Initialize other controllers
        importController = Lookup.getDefault().lookup(ImportController.class);
        exportController = Lookup.getDefault().lookup(ExportController.class);
        layoutController = Lookup.getDefault().lookup(LayoutController.class);
    }
    
    public ProjectController getProjectController() {
        return projectController;
    }
    
    public Workspace getWorkspace() {
        return workspace;
    }
    
    public GraphModel getGraphModel() {
        return graphModel;
    }
    
    public ImportController getImportController() {
        return importController;
    }
    
    public ExportController getExportController() {
        return exportController;
    }
    
    public LayoutController getLayoutController() {
        return layoutController;
    }
    
    public void reset() {
        // Create new project to reset state
        projectController.newProject();
        workspace = projectController.getCurrentWorkspace();
        
        GraphController graphController = Lookup.getDefault().lookup(GraphController.class);
        graphModel = graphController.getGraphModel();
    }
}