# MCP Client Configuration Examples

This directory contains example configuration files for integrating SynergyBridge MCP with various MCP clients.

## Available Configurations

| Client | File | Description |
|--------|------|-------------|
| Claude Desktop | `claude-desktop.json` | Configuration for Anthropic's Claude Desktop app |
| Goose | `goose-config.yaml` | Configuration for Block's Goose AI agent |
| Cursor | `cursor-settings.json` | Settings for Cursor IDE MCP integration |
| Generic | `generic-mcp.json` | Generic MCP configuration for custom clients |

## Server Endpoints

SynergyBridge MCP provides the following endpoints:

| Protocol | URL | Description |
|----------|-----|-------------|
| HTTP (Streamable) | `http://localhost:8080/mcp` | Primary MCP endpoint |
| HTTP/SSE | `http://localhost:8080/mcp/sse` | Server-Sent Events endpoint |

## Quick Start

1. Start SynergyBridge MCP server:
   ```bash
   ./gradlew quarkusDev
   ```

2. Copy the appropriate configuration file to your client's config directory

3. Restart your MCP client

4. Enjoy the authentic enterprise experience!

## Notes

- The server uses port 8080 by default
- Tools have intentional delays (2-12 seconds) for authenticity
- 10% of requests will receive enterprise-grade error messages (this is a feature!)
