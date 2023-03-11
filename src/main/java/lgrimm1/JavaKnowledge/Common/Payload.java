package lgrimm1.JavaKnowledge.Common;

import java.util.*;

public class Payload {

	private Boolean confirm;
	private List<String> content;
	private Boolean editExistingPage;
	private String fileName;
	private String files;
	private String message;
	private List<String> references;
	private String searchText;
	private String staticPageLink;
	private String title;
	private List<String> titles;
/*
	private final Map<String, Object> model = new HashMap<>();
*/

	public Payload() {
	}

	public Payload(String searchText) {
/*
		model.put("search_text", searchText);
*/
		this.searchText = searchText;
	}

	public Payload(String searchText, List<String> titles) {
/*
		model.put("search_text", searchText);
		model.put("titles", titles);
*/
		this.searchText = searchText;
		this.titles = titles;
	}

	public Payload(List<String> titles, String message) {
/*
		model.put("titles", titles);
		model.put("message", message);
*/
		this.titles = titles;
		this.message = message;
	}


	public Payload(List<String> references, String staticPageLink, String title) {
/*
		model.put("references", references);
		model.put("static_page_link", staticPageLink);
		model.put("title", title);
*/
		this.references = references;
		this.staticPageLink = staticPageLink;
		this.title = title;
	}

	public Payload(Boolean confirm, String files, String message, List<String> titles) {
/*
		model.put("confirm", confirm);
		model.put("files", files);
		model.put("message", message);
		model.put("titles", titles);
*/
		this.confirm = confirm;
		this.files = files;
		this.message = message;
		this.titles = titles;
	}

	public Payload(List<String> content, Boolean editExistingPage, String fileName, String message, String title) {
/*
		model.put("content", content);
		model.put("edit_existing_page", editExistingPage);
		model.put("file_name", fileName);
		model.put("message", message);
		model.put("title", title);
*/
		this.content = content;
		this.editExistingPage = editExistingPage;
		this.fileName = fileName;
		this.message = message;
		this.title = title;
	}

	public Payload(Boolean confirm,
				   List<String> content,
				   Boolean editExistingPage,
				   String fileName,
				   String files,
				   String message,
				   List<String> references,
				   String searchText,
				   String staticPageLink,
				   String title,
				   List<String> titles) {
/*
		model.put("confirm", confirm);
		model.put("content", content);
		model.put("edit_existing_page", editExistingPage);
		model.put("file_name", fileName);
		model.put("files", files);
		model.put("message", message);
		model.put("references", references);
		model.put("search_text", searchText);
		model.put("static_page_link", staticPageLink);
		model.put("title", title);
		model.put("titles", titles);
*/
		this.confirm = confirm;
		this.content = content;
		this.editExistingPage = editExistingPage;
		this.fileName = fileName;
		this.files = files;
		this.message = message;
		this.references = references;
		this.searchText = searchText;
		this.staticPageLink = staticPageLink;
		this.title = title;
		this.titles = titles;
	}

	public Boolean getConfirm() {
		return confirm;
	}

	public void setConfirm(Boolean confirm) {
		this.confirm = confirm;
	}

	public List<String> getContent() {
		return content;
	}

	public void setContent(List<String> content) {
		this.content = content;
	}

	public Boolean getEditExistingPage() {
		return editExistingPage;
	}

	public void setEditExistingPage(Boolean editExistingPage) {
		this.editExistingPage = editExistingPage;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFiles() {
		return files;
	}

	public void setFiles(String files) {
		this.files = files;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<String> getReferences() {
		return references;
	}

	public void setReferences(List<String> references) {
		this.references = references;
	}

	public String getSearchText() {
		return searchText;
	}

	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}

	public String getStaticPageLink() {
		return staticPageLink;
	}

	public void setStaticPageLink(String staticPageLink) {
		this.staticPageLink = staticPageLink;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<String> getTitles() {
		return titles;
	}

	public void setTitles(List<String> titles) {
		this.titles = titles;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Payload payload = (Payload) o;
		return Objects.equals(confirm, payload.confirm) &&
				Objects.equals(content, payload.content) &&
				Objects.equals(editExistingPage, payload.editExistingPage) &&
				Objects.equals(fileName, payload.fileName) &&
				Objects.equals(files, payload.files) &&
				Objects.equals(message, payload.message) &&
				Objects.equals(references, payload.references) &&
				Objects.equals(searchText, payload.searchText) &&
				Objects.equals(staticPageLink, payload.staticPageLink) &&
				Objects.equals(title, payload.title) &&
				Objects.equals(titles, payload.titles);
	}

	@Override
	public int hashCode() {
		return Objects.hash(confirm,
				content,
				editExistingPage,
				fileName,
				files,
				message,
				references,
				searchText,
				staticPageLink,
				title,
				titles);
	}
}
