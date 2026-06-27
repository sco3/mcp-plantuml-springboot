package sco3;

import static java.util.Base64.getEncoder;
import static net.sourceforge.plantuml.FileFormat.SVG;

import java.io.ByteArrayOutputStream;

import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import io.modelcontextprotocol.spec.McpSchema.ImageContent;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;

@Component
public class PlantUmlTool {
	private static final String MIME_SVG = "image/svg+xml";

	@McpTool(description = "Render PlantUML into SVG image (base64 encoded)")
	public CallToolResult renderDiagram( //
			@ToolParam(description = "PlantUML source") //
			String source //
	) throws Exception {
		ByteArrayOutputStream os = new ByteArrayOutputStream();

		new SourceStringReader(source) //
				.outputImage(os, new FileFormatOption(SVG));

		String b64 = getEncoder().encodeToString(os.toByteArray());

		ImageContent content = ImageContent.builder(b64, MIME_SVG).build();

		return CallToolResult.builder().addContent(content).build();
	}
}
