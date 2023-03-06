package lgrimm1.JavaKnowledge.Common;

import java.util.*;

public class ListPayload {
	private final List<String> titles;
	private final List<String> files;
	private final Boolean confirm;
	private final String message;

	public ListPayload(List<String> titles, List<String> files, Boolean confirm, String message) {
		this.titles = titles;
		this.files = files;
		this.confirm = confirm;
		this.message = message;
	}

	public List<String> getTitles() {
		return titles;
	}

	public List<String> getFiles() {
		return files;
	}

	public Boolean getConfirm() {
		return confirm;
	}

	public String getMessage() {
		return message;
	}
}
