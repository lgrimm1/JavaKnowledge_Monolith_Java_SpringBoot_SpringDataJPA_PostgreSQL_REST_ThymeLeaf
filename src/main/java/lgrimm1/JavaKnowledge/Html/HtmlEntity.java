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
	@Column(name = "html_text", nullable = false)
	List<String> htmlText;

	public HtmlEntity() {
	}

	public HtmlEntity(List<String> htmlText) {
		this.htmlText = htmlText;
	}

	public HtmlEntity(long id, List<String> htmlText) {
		this.id = id;
		this.htmlText = htmlText;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public List<String> getHtmlText() {
		return htmlText;
	}

	public void setHtmlText(List<String> htmlText) {
		this.htmlText = htmlText;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		HtmlEntity that = (HtmlEntity) o;
		return id == that.id && htmlText.equals(that.htmlText);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, htmlText);
	}

	@Override
	public String toString() {
		return "HtmlEntity{" +
				"id=" + id +
				", htmlText=" + htmlText +
				'}';
	}
}
