package lgrimm1.javaknowledge.common;

import java.util.*;

public class Payload {
	public String templateTitle;
	public Boolean confirm;
	public String content;
	public Boolean editExistingPage;
	public String message;
	public String searchText;
	public String htmlContent;
	public String title;
	public List<String> titles;

	public Payload() {
	}

	public Payload(String templateTitle,
				   Boolean confirm,
				   String content,
				   Boolean editExistingPage,
				   String message,
				   String searchText,
				   String htmlContent,
				   String title,
				   List<String> titles) {
		this.templateTitle = templateTitle;
		this.confirm = confirm;
		this.content = content;
		this.editExistingPage = editExistingPage;
		this.message = message;
		this.searchText = searchText;
		this.htmlContent = htmlContent;
		this.title = title;
		this.titles = titles;
	}

	public String getTemplateTitle() {
		return templateTitle;
	}

	public void setTemplateTitle(String templateTitle) {
		this.templateTitle = templateTitle;
	}

	public Boolean getConfirm() {
		return confirm;
	}

	public void setConfirm(Boolean confirm) {
		this.confirm = confirm;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Boolean getEditExistingPage() {
		return editExistingPage;
	}

	public void setEditExistingPage(Boolean editExistingPage) {
		this.editExistingPage = editExistingPage;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getSearchText() {
		return searchText;
	}

	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}

	public String getHtmlContent() {
		return htmlContent;
	}

	public void setHtmlContent(String htmlContent) {
		this.htmlContent = htmlContent;
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
		return Objects.equals(templateTitle, payload.templateTitle) && Objects.equals(confirm, payload.confirm) && Objects.equals(content, payload.content) && Objects.equals(editExistingPage, payload.editExistingPage) && Objects.equals(message, payload.message) && Objects.equals(searchText, payload.searchText) && Objects.equals(htmlContent, payload.htmlContent) && Objects.equals(title, payload.title) && Objects.equals(titles, payload.titles);
	}

	@Override
	public int hashCode() {
		return Objects.hash(templateTitle, confirm, content, editExistingPage, message, searchText, htmlContent, title, titles);
	}

	@Override
	public String toString() {
		return "Payload{" +
				"templateTitle='" + templateTitle + '\'' +
				", confirm=" + confirm +
				", content='" + content + '\'' +
				", editExistingPage=" + editExistingPage +
				", message='" + message + '\'' +
				", searchText='" + searchText + '\'' +
				", htmlContent='" + htmlContent + '\'' +
				", title='" + title + '\'' +
				", titles=" + titles +
				'}';
	}
}
