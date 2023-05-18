package lgrimm.javaknowledge.databasestorage;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;

@Repository
public interface HtmlRepository extends JpaRepository<HtmlEntity, Long> {
}
