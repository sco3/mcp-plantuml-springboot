package sco3;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import io.modelcontextprotocol.spec.McpSchema.Content;

class PlantUmlToolTest {

	private final PlantUmlTool tool = new PlantUmlTool();

	@Test
	void renderDiagram_returnsSvgBase64Content() throws Exception {
		String plantuml = "@startuml\nAlice -> Bob : hello\n@enduml";

		CallToolResult result = tool.renderDiagram(plantuml);

		assertNotNull(result);
		assertNotNull(result.content());
		assertEquals(1, result.content().size());

		Content content = result.content().getFirst();
		assertNotNull(content);
	}

	@Test
	void renderDiagram_emptySource_stillReturnsResult() throws Exception {
		CallToolResult result = tool.renderDiagram("");
		assertNotNull(result);
		assertNotNull(result.content());
		assertEquals(1, result.content().size());
	}

	@Test
	void renderDiagram_emptySource_throwsException() {
		Exception exception = assertThrows(Exception.class, () -> {
			tool.renderDiagram(null);
		});

		assertTrue(exception instanceof NullPointerException);
	}
}
