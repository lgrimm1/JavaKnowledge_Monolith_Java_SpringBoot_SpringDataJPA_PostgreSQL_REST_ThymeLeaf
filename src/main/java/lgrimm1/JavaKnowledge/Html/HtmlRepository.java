package lgrimm1.JavaKnowledge.Html;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;

@Repository
public interface HtmlRepository extends JpaRepository<HtmlEntity, Long> {
}
