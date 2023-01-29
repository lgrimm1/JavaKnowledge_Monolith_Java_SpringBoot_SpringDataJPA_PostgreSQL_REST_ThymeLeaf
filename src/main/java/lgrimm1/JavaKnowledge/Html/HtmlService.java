package lgrimm1.JavaKnowledge.Html;

import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

@Service
public class HtmlService {

	private final HtmlRepository htmlRepository;

	@Autowired
	public HtmlService(HtmlRepository htmlRepository) {
		this.htmlRepository = htmlRepository;
	}
}
