package lgrimm1.JavaKnowledge.Common;

import lgrimm1.JavaKnowledge.Html.*;
import lgrimm1.JavaKnowledge.Txt.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

@Service
public class CommonService {

	private final TxtRepository txtRepository;
	private final HtmlRepository htmlRepository;

	@Autowired
	public CommonService(TxtRepository txtRepository, HtmlRepository htmlRepository) {
		this.txtRepository = txtRepository;
		this.htmlRepository = htmlRepository;
	}
}
