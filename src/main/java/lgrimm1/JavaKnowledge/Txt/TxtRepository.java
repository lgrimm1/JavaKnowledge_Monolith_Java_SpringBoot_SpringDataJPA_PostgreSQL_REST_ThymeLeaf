package lgrimm1.JavaKnowledge.Txt;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.*;

@Repository
public interface TxtRepository extends JpaRepository<TxtEntity, Long> {
}
