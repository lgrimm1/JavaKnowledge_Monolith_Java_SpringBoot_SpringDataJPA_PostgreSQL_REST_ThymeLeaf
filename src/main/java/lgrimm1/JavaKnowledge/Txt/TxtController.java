package lgrimm1.JavaKnowledge.Txt;

import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1/txt", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class TxtController {

	private final TxtService txtService;

	@Autowired
	public TxtController(TxtService txtService) {
		this.txtService = txtService;
	}
}
