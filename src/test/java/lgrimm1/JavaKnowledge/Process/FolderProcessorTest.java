package lgrimm1.JavaKnowledge.Process;

import org.junit.jupiter.api.*;

import java.util.*;

class FolderProcessorTest {
	Extractors extractors = new Extractors();
	PageProcessor pageProcessor = new PageProcessor(extractors);
	FileOperations fileOperations = new FileOperations();
	FolderProcessor folderProcessor = new FolderProcessor(pageProcessor, fileOperations);

	@Test
	void folderProcessor() {
		//TODO folderProcessor test
	}

	@Test
	void getResourcesPath() {
		String resourcePath = folderProcessor.getResourcesPath();

		Assertions.assertTrue(resourcePath.contains("src") &&
						resourcePath.contains("main") &&
						resourcePath.contains("resources"));
	}

	@Test
	void stringListToString() {
		List<String> list = new ArrayList<>(Arrays.asList("AAA", "BBB", "CCC", "DDD", "EEE"));

		Assertions.assertEquals("AAA\nBBB\nCCC\nDDD\nEEE",
				folderProcessor.stringListToString(list, "\n"));
	}
}
