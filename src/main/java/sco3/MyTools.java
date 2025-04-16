package sco3;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

@Service
public class MyTools {
	@Tool(description = "add two numbers")
	public Integer add(@ToolParam(description = "first number") int a,
			@ToolParam(description = "second number") int b) {

		return a + b;
	}

}
