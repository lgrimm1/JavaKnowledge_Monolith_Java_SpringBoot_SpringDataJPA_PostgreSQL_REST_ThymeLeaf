package lgrimm1.JavaKnowledge.Html;

import jakarta.persistence.*;

import java.util.*;

@Entity
@Table(name = "html")
public class HtmlEntity {

	@Id
	@SequenceGenerator(name = "seq_html", sequenceName = "seq_html", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_html")
	@Column(name = "id")
	long id;
	@Column(name = "content", nullable = false)
	List<String> content;
	@Column(name = "title_references", nullable = false)
	List<String> titleReferences;

	public HtmlEntity() {
	}

	public HtmlEntity(List<String> content, List<String> titleReferences) {
		this.content = content;
		this.titleReferences = titleReferences;
	}

	public HtmlEntity(long id, List<String> content, List<String> titleReferences) {
		this.id = id;
		this.content = content;
		this.titleReferences = titleReferences;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public List<String> getContent() {
		return content;
	}

	public void setContent(List<String> content) {
		this.content = content;
	}

	public List<String> getTitleReferences() {
		return titleReferences;
	}

	public void setTitleReferences(List<String> titleReferences) {
		this.titleReferences = titleReferences;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		HtmlEntity that = (HtmlEntity) o;
		return id == that.id && content.equals(that.content) && titleReferences.equals(that.titleReferences);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, content, titleReferences);
	}

	@Override
	public String toString() {
		return "HtmlEntity{" +
				"id=" + id +
				", content=" + content +
				", titleReferences=" + titleReferences +
				'}';
	}
}
