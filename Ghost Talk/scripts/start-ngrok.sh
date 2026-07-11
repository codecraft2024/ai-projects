#!/usr/bin/env bash
# Exposes local backend (port 8080) to the internet via ngrok and updates the Android app config.
set -euo pipefail

ROOT="$(cd "$(dirname "$0")" && pwd)"
APP_DIR="$ROOT/app"
LOCAL_PROPS="$APP_DIR/local.properties"
PORT="${1:-8080}"

if ! command -v ngrok >/dev/null 2>&1; then
  echo "ngrok is not installed. Install from https://ngrok.com/download"
  exit 1
fi

# Start ngrok if not already running for this port
if ! curl -sf http://127.0.0.1:4040/api/tunnels >/dev/null 2>&1; then
  echo "Starting ngrok on port $PORT..."
  ngrok http "$PORT" --log=stdout >/tmp/ghosttalk-ngrok.log 2>&1 &
  for _ in $(seq 1 30); do
    curl -sf http://127.0.0.1:4040/api/tunnels >/dev/null 2>&1 && break
    sleep 1
  done
fi

PUBLIC_URL=$(curl -s http://127.0.0.1:4040/api/tunnels | python3 -c "
import sys, json
data = json.load(sys.stdin)
for t in data.get('tunnels', []):
    if t.get('public_url', '').startswith('https://'):
        print(t['public_url'])
        break
")

if [ -z "$PUBLIC_URL" ]; then
  echo "Could not read ngrok public URL. Check ngrok dashboard at http://127.0.0.1:4040"
  exit 1
fi

API_URL="${PUBLIC_URL}/api/v1/"
WS_URL="wss://${PUBLIC_URL#https://}/api/v1/ws"

echo "Public API: $API_URL"

# Update local.properties (preserve sdk.dir)
touch "$LOCAL_PROPS"
if grep -q '^API_BASE_URL=' "$LOCAL_PROPS"; then
  sed -i '' "s|^API_BASE_URL=.*|API_BASE_URL=$API_URL|" "$LOCAL_PROPS"
else
  echo "API_BASE_URL=$API_URL" >> "$LOCAL_PROPS"
fi
if grep -q '^WS_BASE_URL=' "$LOCAL_PROPS"; then
  sed -i '' "s|^WS_BASE_URL=.*|WS_BASE_URL=$WS_URL|" "$LOCAL_PROPS"
else
  echo "WS_BASE_URL=$WS_URL" >> "$LOCAL_PROPS"
fi

echo ""
echo "Updated $LOCAL_PROPS"
echo "Rebuild and install the app: cd app && ./gradlew installDebug"
echo "ngrok dashboard: http://127.0.0.1:4040"
