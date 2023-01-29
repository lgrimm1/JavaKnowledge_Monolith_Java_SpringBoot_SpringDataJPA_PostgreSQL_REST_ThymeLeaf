package lgrimm1.JavaKnowledge.Html;

import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1/html", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class HtmlController {

	private final HtmlService htmlService;

	@Autowired
	public HtmlController(HtmlService htmlService) {
		this.htmlService = htmlService;
	}
}
