#!/usr/bin/env -S bash


jar=$(find $(dirname $0) -name mcp-spring-ai-0.0.1-SNAPSHOT.jar  )

java21 -Dspring.ai.mcp.server.transport=STDIO \
        -Dspring.main.web-application-type=none \
        -Dspring.main.banner-mode=off \
        -Dlogging.pattern.console= \
        -jar "$jar"
