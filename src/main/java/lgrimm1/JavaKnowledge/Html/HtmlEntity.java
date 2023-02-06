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

	public HtmlEntity() {
	}

	public HtmlEntity(List<String> content) {
		this.content = content;
	}

	public HtmlEntity(long id, List<String> content) {
		this.id = id;
		this.content = content;
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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		HtmlEntity that = (HtmlEntity) o;
		return id == that.id && content.equals(that.content);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, content);
	}

	@Override
	public String toString() {
		return "HtmlEntity{" +
				"id=" + id +
				", content=" + content +
				'}';
	}
}
