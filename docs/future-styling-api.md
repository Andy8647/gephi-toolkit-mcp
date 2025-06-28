# Future Styling API Design

## Enhanced `save_graph` Tool with Styling Options

### Current Usage
```json
{
  "name": "save_graph",
  "arguments": {
    "filePath": "/path/to/output.png",
    "format": "png"
  }
}
```

### Future Enhanced Usage
```json
{
  "name": "save_graph",
  "arguments": {
    "filePath": "/path/to/output.png",
    "format": "png",
    "styling": {
      "nodes": {
        "size": "small",           // "tiny", "small", "medium", "large", "huge"
        "sizeMultiplier": 0.5,     // Custom size scaling factor
        "color": "auto",           // "auto", "#FF0000", "degree-based"
        "showLabels": true,
        "labelFont": {
          "family": "Arial",
          "size": 6,               // Font size in points
          "style": "plain"         // "plain", "bold", "italic"
        }
      },
      "edges": {
        "thickness": "thin",       // "hair", "thin", "medium", "thick"
        "color": "#CCCCCC",
        "showLabels": false
      },
      "canvas": {
        "width": 1920,            // Export width in pixels
        "height": 1080,           // Export height in pixels
        "backgroundColor": "#FFFFFF",
        "transparency": false
      },
      "layout": {
        "zoom": 1.0,              // Zoom level for export
        "centerX": 0.5,           // Center position (0-1)
        "centerY": 0.5
      }
    }
  }
}
```

## Gephi Toolkit Properties That Could Be Exposed

### Node Styling
- `PreviewProperty.NODE_SIZE` - Node size scaling
- `PreviewProperty.NODE_COLOR` - Node colors
- `PreviewProperty.SHOW_NODE_LABELS` - Label visibility
- `PreviewProperty.NODE_LABEL_FONT` - Font family, size, style
- `PreviewProperty.NODE_LABEL_COLOR` - Label text color
- `PreviewProperty.NODE_LABEL_SHOW_BOX` - Label background box
- `PreviewProperty.NODE_OPACITY` - Node transparency

### Edge Styling  
- `PreviewProperty.EDGE_THICKNESS` - Edge line thickness
- `PreviewProperty.EDGE_COLOR` - Edge colors
- `PreviewProperty.SHOW_EDGE_LABELS` - Edge label visibility
- `PreviewProperty.EDGE_OPACITY` - Edge transparency
- `PreviewProperty.EDGE_CURVED` - Curved vs straight edges

### Canvas/Export Settings
- `PNGExporter.setWidth()` - Image width
- `PNGExporter.setHeight()` - Image height
- `PreviewProperty.BACKGROUND_COLOR` - Canvas background
- `PreviewProperty.MARGIN` - Margin around graph

## Implementation Strategy

1. **Phase 2A**: Add basic styling options (node size, label font size)
2. **Phase 2B**: Add color controls and edge styling
3. **Phase 2C**: Add advanced layout and export options

## Example Use Cases

### Small Network (4-20 nodes)
```json
"styling": {
  "nodes": {
    "size": "medium",
    "labelFont": {"size": 10}
  },
  "canvas": {"width": 800, "height": 600}
}
```

### Large Network (1000+ nodes)  
```json
"styling": {
  "nodes": {
    "size": "tiny",
    "showLabels": false
  },
  "edges": {"thickness": "hair"},
  "canvas": {"width": 2400, "height": 1800}
}
```

### Publication Quality
```json
"styling": {
  "nodes": {
    "size": "small",
    "labelFont": {"family": "Times", "size": 8}
  },
  "canvas": {
    "width": 3000,
    "height": 2000,
    "backgroundColor": "#FFFFFF"
  }
}
```