package lgrimm1.JavaKnowledge.Common;

import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class CommonController {

	private final CommonService commonService;

	@Autowired
	public CommonController(CommonService commonService) {
		this.commonService = commonService;
	}
}
