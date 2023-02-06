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

	public TxtEntity() {
	}

	public TxtEntity(List<String> content) {
		this.content = content;
	}

	public TxtEntity(long id, List<String> content) {
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
		TxtEntity txtEntity = (TxtEntity) o;
		return id == txtEntity.id && content.equals(txtEntity.content);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, content);
	}

	@Override
	public String toString() {
		return "TxtEntity{" +
				"id=" + id +
				", content=" + content +
				'}';
	}
}
