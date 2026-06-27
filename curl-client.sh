#!/usr/bin/env bash

set -euo pipefail

URL="${MCP_URL:-http://localhost:8080/mcp}"
TMPDIR=$(mktemp -d)
trap 'rm -rf "$TMPDIR"' EXIT

echo "--- Initializing ---"
curl -s -D "$TMPDIR/headers.txt" -X POST \
  -H "Content-Type: application/json" \
  -H "Accept: application/json, text/event-stream" \
  -d '{
    "jsonrpc": "2.0",
    "method": "initialize",
    "params": {
      "protocolVersion": "2024-11-05",
      "capabilities": {},
      "clientInfo": { "name": "curl-test", "version": "1.0.0" }
    },
    "id": 1
  }' "$URL" > "$TMPDIR/init.json"

echo "Init Response:"
cat "$TMPDIR/init.json"

SESSION_ID=$(grep -i "mcp-session-id" "$TMPDIR/headers.txt" | awk '{print $2}' | tr -d '\r' || true)
if [[ -z "$SESSION_ID" ]]; then
  echo "Error: Mcp-Session-Id not found in headers" >&2
  cat "$TMPDIR/headers.txt"
  exit 1
fi
echo "Session ID: $SESSION_ID"

echo "--- Sending Initialized Notification ---"
curl -s -X POST \
  -H "Content-Type: application/json" \
  -H "Accept: application/json, text/event-stream" \
  -H "Mcp-Session-Id: $SESSION_ID" \
  -d '{
    "jsonrpc": "2.0",
    "method": "notifications/initialized"
  }' "$URL"

echo -e "\n--- Calling renderDiagram ---"
RESPONSE=$(curl -s -X POST \
  -H "Content-Type: application/json" \
  -H "Accept: application/json, text/event-stream" \
  -H "Mcp-Session-Id: $SESSION_ID" \
  -d '{
    "jsonrpc": "2.0",
    "id": 2,
    "method": "tools/call",
    "params": {
      "name": "renderDiagram",
      "arguments": {
        "source": "@startuml\nUser -> AI : Hello\n@enduml"
      }
    }
  }' "$URL")

SSE_DATA=$(echo "$RESPONSE" | grep '^data:' | sed 's/^data: *//')
echo "$SSE_DATA" | jq .

BASE64_CONTENT=$(echo "$SSE_DATA" | jq -r '.result.content[0].data // empty')
if [[ -z "$BASE64_CONTENT" ]]; then
  echo "Error: No base64 content in response"
  exit 1
fi

SVG=$(echo "$BASE64_CONTENT" | base64 -d)
if ! echo "$SVG" | head -1 | grep -q '<svg'; then
  echo "Error: Output is not valid SVG"
  exit 1
fi

echo -e "\nIntegration test passed"
