package lgrimm.javaknowledge.databasestorage;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;

import java.util.*;

@Repository
public interface TxtRepository extends JpaRepository<TxtEntity, Long> {

	List<TxtEntity> findByContentContainingAllIgnoreCase(String txtPart);
}
