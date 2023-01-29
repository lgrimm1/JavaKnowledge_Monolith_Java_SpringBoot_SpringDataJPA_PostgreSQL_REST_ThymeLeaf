package lgrimm1.JavaKnowledge.Txt;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

@Service
public class TxtService {

	private final TxtRepository txtRepository;

	@Autowired
	public TxtService(TxtRepository txtRepository) {
		this.txtRepository = txtRepository;
	}
}
