package lgrimm1.javaknowledge.databasestorage;

import jakarta.persistence.*;

import java.util.*;

@Entity
@Table(name = "title_table")
public class TitleEntity {

	@Id
	@SequenceGenerator(name = "seq_title", sequenceName = "seq_title", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_title")
	@Column(name = "id")
	long id;
	@Column(name = "title", nullable = false, unique = true)
	String title;
	@Column(name = "txt_id", nullable = false, unique = true)
	long txtId;
	@Column(name = "html_id", nullable = false, unique = true)
	long htmlId;

	public TitleEntity() {
	}

	public TitleEntity(String title, long txtId, long htmlId) {
		this.title = title;
		this.txtId = txtId;
		this.htmlId = htmlId;
	}

	public TitleEntity(long id, String title, long txtId, long htmlId) {
		this.id = id;
		this.title = title;
		this.txtId = txtId;
		this.htmlId = htmlId;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public long getTxtId() {
		return txtId;
	}

	public void setTxtId(long txtId) {
		this.txtId = txtId;
	}

	public long getHtmlId() {
		return htmlId;
	}

	public void setHtmlId(long htmlId) {
		this.htmlId = htmlId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		TitleEntity that = (TitleEntity) o;
		return id == that.id &&
				txtId == that.txtId &&
				htmlId == that.htmlId &&
				title.equals(that.title);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, title, txtId, htmlId);
	}

	@Override
	public String toString() {
		return "TitleEntity{" +
				"id=" + id +
				", title='" + title + '\'' +
				", txtId=" + txtId +
				", htmlId=" + htmlId +
				'}';
	}
}
