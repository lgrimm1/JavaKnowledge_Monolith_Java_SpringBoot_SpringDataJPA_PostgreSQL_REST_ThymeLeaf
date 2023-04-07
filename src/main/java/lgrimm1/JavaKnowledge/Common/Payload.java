package lgrimm1.JavaKnowledge.Common;

import java.util.*;

public class Payload {

	public Boolean confirm;
	public String content;
	public Boolean editExistingPage;
	public String files;
	public String message;
	public String searchText;
	public String staticPageLink;
	public String title;
	public List<String> titles;

	public Payload() {
	}

	public Payload(Boolean confirm,
				   String content,
				   Boolean editExistingPage,
				   String files,
				   String message,
				   String searchText,
				   String staticPageLink,
				   String title,
				   List<String> titles) {
		this.confirm = confirm;
		this.content = content;
		this.editExistingPage = editExistingPage;
		this.files = files;
		this.message = message;
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
				Objects.equals(files, payload.files) &&
				Objects.equals(message, payload.message) &&
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
				files,
				message,
				searchText,
				staticPageLink,
				title,
				titles);
	}

	@Override
	public String toString() {
		return "Payload{" +
				"confirm=" + confirm +
				", content=" + content +
				", editExistingPage=" + editExistingPage +
				", files='" + files + '\'' +
				", message='" + message + '\'' +
				", searchText='" + searchText + '\'' +
				", staticPageLink='" + staticPageLink + '\'' +
				", title='" + title + '\'' +
				", titles=" + titles +
				'}';
	}
}
