package lgrimm1.JavaKnowledge.Txt;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;

import java.util.*;

@Repository
public interface TxtRepository extends JpaRepository<TxtEntity, Long> {

//	@Query("SELECT s FROM TxtEntity s WHERE UPPER(?1) IN UPPER(s.content)")
//	@Query(value = "SELECT * FROM TxtEntity t WHERE ANY(t.content) LIKE :txtPart", nativeQuery = true)
//	@Query(value = "SELECT * FROM TxtEntity t WHERE ANY(t.content) LIKE ?1", nativeQuery = true)
	@Query(value = "SELECT * FROM TxtEntity t WHERE UPPER(ANY(t.content)) LIKE UPPER(?1)", nativeQuery = true)
	List<TxtEntity> findEntityByItsPartiallyContainedText(String txtPart);
}
