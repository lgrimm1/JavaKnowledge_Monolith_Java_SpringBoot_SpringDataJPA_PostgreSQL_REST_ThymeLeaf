package lgrimm1.JavaKnowledge.Txt;

import jakarta.persistence.*;

import java.util.*;

@Entity
@Table(name = "txt")
public class TxtEntity {

	@Id
	@SequenceGenerator(name = "seq_txt", sequenceName = "seq_txt", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_txt")
	@Column(name = "id")
	long id;
	@Column(name = "content", nullable = false)
	List<String> content;
	@Column(name = "title_id", nullable = false, unique = true)
	long titleId;

	public TxtEntity() {
	}

	public TxtEntity(List<String> content, long titleId) {
		this.content = content;
		this.titleId = titleId;
	}

	public TxtEntity(long id, List<String> content, long titleId) {
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
		TxtEntity txtEntity = (TxtEntity) o;
		return id == txtEntity.id && titleId == txtEntity.titleId && content.equals(txtEntity.content);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, content, titleId);
	}

	@Override
	public String toString() {
		return "TxtEntity{}";
	}
}
