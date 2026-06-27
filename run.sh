#!/usr/bin/env -S bash


jar=$(find $(dirname $0) -name mcp-plantuml-springboot\*.jar  | grep -v "plain.jar")

java25 -jar "$jar"


