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
	@Column(name = "source_text", nullable = false)
	List<String> sourceText;

	public TxtEntity() {
	}

	public TxtEntity(List<String> sourceText) {
		this.sourceText = sourceText;
	}

	public TxtEntity(long id, List<String> sourceText) {
		this.id = id;
		this.sourceText = sourceText;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public List<String> getSourceText() {
		return sourceText;
	}

	public void setSourceText(List<String> sourceText) {
		this.sourceText = sourceText;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		TxtEntity txtEntity = (TxtEntity) o;
		return id == txtEntity.id && sourceText.equals(txtEntity.sourceText);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, sourceText);
	}

	@Override
	public String toString() {
		return "TxtEntity{" +
				"id=" + id +
				", sourceText=" + sourceText +
				'}';
	}
}
