package sco3;

import java.io.ByteArrayOutputStream;
import java.util.Base64;

import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import io.modelcontextprotocol.spec.McpSchema.ImageContent;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;

@Component
public class MyTools {
	@McpTool(description = "Render PlantUML into SVG image (base64 encoded)")
	public CallToolResult renderDiagram(@ToolParam(description = "PlantUML source") String source) throws Exception {

		SourceStringReader reader = new SourceStringReader(source);
		ByteArrayOutputStream os = new ByteArrayOutputStream();

		reader.generateImage(os, new FileFormatOption(FileFormat.SVG));
		byte[] imageBytes = os.toByteArray();

		String base64 = Base64.getEncoder().encodeToString(imageBytes);

		ImageContent content = ImageContent.builder(base64, "image/svg+xml").build();

		return CallToolResult.builder().addContent(content).build();
	}
}
