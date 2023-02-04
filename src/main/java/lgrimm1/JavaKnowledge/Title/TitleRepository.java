package lgrimm1.JavaKnowledge.Title;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.*;

import java.util.*;

@Repository
public interface TitleRepository extends JpaRepository<TitleEntity, Long> {

	Optional<TitleEntity> findByTitle(String title);

	List<TitleEntity> findByTitleContainingAllIgnoreCase(String titlePart);

	Optional<TitleEntity> findByFilename(String filename);
}
