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
	@Column(name = "title_id", nullable = false, unique = true)
	long titleId;

	public HtmlEntity() {
	}

	public HtmlEntity(List<String> content, long titleId) {
		this.content = content;
		this.titleId = titleId;
	}

	public HtmlEntity(long id, List<String> content, long titleId) {
		this.id = id;
		this.content = content;
		this.titleId = titleId;
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

	public long getTitleId() {
		return titleId;
	}

	public void setTitleId(long titleId) {
		this.titleId = titleId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		HtmlEntity that = (HtmlEntity) o;
		return id == that.id && titleId == that.titleId && content.equals(that.content);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, content, titleId);
	}

	@Override
	public String toString() {
		return "HtmlEntity{" +
				"id=" + id +
				", content=" + content +
				", titleId=" + titleId +
				'}';
	}
}
