#!/usr/bin/env -S bash


jar=$(find $(dirname $0) -name mcp-spring-ai-0.0.1-SNAPSHOT.jar  )

java25 -jar "$jar"


