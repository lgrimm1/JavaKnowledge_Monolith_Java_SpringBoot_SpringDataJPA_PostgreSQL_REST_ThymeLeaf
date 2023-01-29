package lgrimm1.JavaKnowledge.Txt;

import java.util.*;

public class Entity {

	long id;
	List<String> text;

	public Entity() {
	}

	public Entity(List<String> text) {
		this.text = text;
	}

	public Entity(long id, List<String> text) {
		this.id = id;
		this.text = text;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public List<String> getText() {
		return text;
	}

	public void setText(List<String> text) {
		this.text = text;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Entity entity = (Entity) o;
		return id == entity.id && text.equals(entity.text);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, text);
	}

	@Override
	public String toString() {
		return "Entity{" +
				"id=" + id +
				", text=" + text +
				'}';
	}
}
