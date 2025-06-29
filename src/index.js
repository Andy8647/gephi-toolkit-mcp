#!/usr/bin/env node

import { Server } from "@modelcontextprotocol/sdk/server/index.js";
import { StdioServerTransport } from "@modelcontextprotocol/sdk/server/stdio.js";
import { spawn } from "child_process";
import path from "path";
import { fileURLToPath } from "url";

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

class GephiMCPServer {
  constructor() {
    this.server = new Server(
      {
        name: "gephi-toolkit-mcp",
        version: "1.0.0",
      },
      {
        capabilities: {
          tools: {},
        },
      }
    );

    this.javaService = null;
    this.setupToolHandlers();
    this.setupErrorHandling();
  }

  setupToolHandlers() {
    this.server.registerTool("load_graph", {
      description: "Load a graph from file (GEXF, GraphML, GML, DOT, CSV)",
      inputSchema: {
        type: "object",
        properties: {
          filePath: {
            type: "string",
            description: "Path to the graph file"
          },
          format: {
            type: "string",
            enum: ["gexf", "graphml", "gml", "dot", "csv"],
            description: "Graph file format"
          }
        },
        required: ["filePath"]
      }
    }, async (args) => {
      return await this.loadGraph(args.filePath, args.format);
    });

    this.server.registerTool("apply_force_atlas2", {
      description: "Apply Force Atlas 2 layout algorithm",
      inputSchema: {
        type: "object",
        properties: {
          iterations: {
            type: "number",
            description: "Number of iterations to run",
            default: 100
          },
          adjustSizes: {
            type: "boolean",
            description: "Adjust node sizes based on degree",
            default: false
          },
          barnesHutOptimize: {
            type: "boolean",
            description: "Use Barnes-Hut optimization",
            default: true
          },
          gravity: {
            type: "number",
            description: "Gravity parameter",
            default: 1.0
          },
          scalingRatio: {
            type: "number",
            description: "Scaling ratio",
            default: 2.0
          }
        }
      }
    }, async (args) => {
      return await this.applyForceAtlas2(args);
    });

    this.server.registerTool("get_graph_info", {
      description: "Get basic information about the loaded graph",
      inputSchema: {
        type: "object",
        properties: {},
        additionalProperties: false
      }
    }, async () => {
      return await this.getGraphInfo();
    });

    this.server.registerTool("save_graph", {
      description: "Save the current graph to file",
      inputSchema: {
        type: "object",
        properties: {
          filePath: {
            type: "string",
            description: "Path where to save the graph"
          },
          format: {
            type: "string",
            enum: ["gexf", "graphml", "pdf", "png"],
            description: "Output format"
          }
        },
        required: ["filePath", "format"]
      }
    }, async (args) => {
      return await this.saveGraph(args.filePath, args.format);
    });
  }

  async startJavaService() {
    if (this.javaService && !this.javaService.killed) {
      return;
    }

    const javaPath = path.join(__dirname, "..", "java");
    const classpath = path.join(javaPath, "target", "classes") + ":" + path.join(javaPath, "target", "*");

    this.javaService = spawn("java", [
      "-cp", classpath,
      "com.gephi.mcp.GephiService"
    ], {
      stdio: ["pipe", "pipe", "pipe"],
      env: { ...process.env, PATH: "/opt/homebrew/opt/openjdk@11/bin:" + process.env.PATH }
    });

    this.javaService.stderr.on("data", (data) => {
      console.error("Java service error:", data.toString());
    });

    this.javaService.on("close", (code) => {
      console.log("Java service exited with code:", code);
      this.javaService = null;
    });

    // Wait for service to start
    await new Promise(resolve => setTimeout(resolve, 1000));
  }

  async callJavaService(operation, params = {}) {
    await this.startJavaService();

    return new Promise((resolve, reject) => {
      const request = JSON.stringify({ operation, params });

      let responseReceived = false;
      const timeout = setTimeout(() => {
        if (!responseReceived) {
          reject(new Error("Java service timeout"));
        }
      }, 10000);

      const onData = (data) => {
        if (responseReceived) return;
        responseReceived = true;
        clearTimeout(timeout);

        try {
          const response = JSON.parse(data.toString().trim());
          resolve(response);
        } catch (e) {
          reject(new Error("Invalid JSON response from Java service"));
        }
      };

      this.javaService.stdout.once("data", onData);
      this.javaService.stdin.write(request + "\n");
    });
  }

  async callJavaProcess(className, args = []) {
    return new Promise((resolve, reject) => {
      const javaPath = path.join(__dirname, "..", "java");
      const classpath = path.join(javaPath, "target", "classes") + ":" + path.join(javaPath, "target", "*");

      const javaProcess = spawn("java", [
        "-cp", classpath,
        className,
        ...args
      ], {
        stdio: ["pipe", "pipe", "pipe"],
        env: { ...process.env, PATH: "/opt/homebrew/opt/openjdk@11/bin:" + process.env.PATH }
      });

      let stdout = "";
      let stderr = "";

      javaProcess.stdout.on("data", (data) => {
        stdout += data.toString();
      });

      javaProcess.stderr.on("data", (data) => {
        stderr += data.toString();
      });

      javaProcess.on("close", (code) => {
        if (code === 0) {
          try {
            const result = JSON.parse(stdout);
            resolve(result);
          } catch (e) {
            resolve({ output: stdout });
          }
        } else {
          reject(new Error(`Java process failed with code ${code}: ${stderr}`));
        }
      });

      process.on("error", (error) => {
        reject(new Error(`Failed to start Java process: ${error.message}`));
      });
    });
  }

  async loadGraph(filePath, format) {
    const result = await this.callJavaService("load_graph", { filePath, format: format || "auto" });

    if (result.success) {
      return {
        content: [
          {
            type: "text",
            text: `Graph loaded successfully: ${result.nodeCount} nodes, ${result.edgeCount} edges`
          }
        ]
      };
    } else {
      throw new Error(result.error || result.message);
    }
  }

  async applyForceAtlas2(options = {}) {
    const result = await this.callJavaService("apply_force_atlas2", options);

    if (result.success) {
      return {
        content: [
          {
            type: "text",
            text: `Force Atlas 2 layout applied: ${result.iterations} iterations completed`
          }
        ]
      };
    } else {
      throw new Error(result.error || result.message);
    }
  }

  async getGraphInfo() {
    const result = await this.callJavaService("get_graph_info");

    return {
      content: [
        {
          type: "text",
          text: JSON.stringify({
            nodeCount: result.nodeCount,
            edgeCount: result.edgeCount,
            directed: result.directed,
            status: result.status
          }, null, 2)
        }
      ]
    };
  }

  async saveGraph(filePath, format) {
    const result = await this.callJavaService("save_graph", { filePath, format });

    if (result.success) {
      return {
        content: [
          {
            type: "text",
            text: `Graph saved successfully to ${filePath}`
          }
        ]
      };
    } else {
      throw new Error(result.error || result.message);
    }
  }

  setupErrorHandling() {
    this.server.onerror = (error) => {
      console.error("[MCP Error]", error);
    };

    process.on("SIGINT", async () => {
      if (this.javaService) {
        this.javaService.kill();
      }
      await this.server.close();
      process.exit(0);
    });
  }

  async run() {
    const transport = new StdioServerTransport();
    await this.server.connect(transport);
    console.error("Gephi Toolkit MCP Server running on stdio");
  }
}

const server = new GephiMCPServer();
server.run().catch(console.error);