package lgrimm1.JavaKnowledge.Common;

import lgrimm1.JavaKnowledge.Html.*;
import lgrimm1.JavaKnowledge.Process.*;
import lgrimm1.JavaKnowledge.Title.*;
import lgrimm1.JavaKnowledge.Txt.*;
import org.springframework.web.servlet.*;

import java.util.*;

public class BrowsingService {
	public static ModelAndView searchPages(String initialView,
										   Payload payload,
										   ProcessRecords processRecords,
										   TitleRepository titleRepository,
										   TxtRepository txtRepository) {
		if (payload == null || payload.getSearchText() == null || payload.getSearchText().isBlank()) {
			Payload payload2 = new Payload(
					null,
					null,
					null,
					null,
					null,
					"<all titles>",
					null,
					null,
					processRecords.getAllTitles(titleRepository)
			);
			return new ModelAndView(initialView, "payload", payload2);
		}
		List<String> titles = processRecords.searchBySearchText(
						payload.getSearchText(),
						titleRepository,
						txtRepository)
				.stream()
				.sorted()
				.toList();
		Payload payload2 = new Payload(
				null,
				null,
				null,
				null,
				null,
				payload.getSearchText(),
				null,
				null,
				titles
		);
		return new ModelAndView(initialView, "payload", payload2);
	}

	public static ModelAndView getPage(String initialView,
									   Payload payload,
									   ProcessRecords processRecords,
									   TitleRepository titleRepository,
									   HtmlRepository htmlRepository) {
		if (payload == null ||
				payload.getTitles() == null ||
				payload.getTitles().size() != 1 ||
				payload.getTitles().get(0) == null ||
				payload.getTitles().get(0).isBlank()) {
			Payload payload2 = new Payload(
					null,
					null,
					null,
					null,
					null,
					"<all titles>",
					null,
					null,
					processRecords.getAllTitles(titleRepository)
			);
			return new ModelAndView("list", "payload", payload2);
		}
		List<String> titles = payload.getTitles();
		Optional<TitleEntity> optionalTitleEntity = titleRepository.findByTitle(titles.get(0));
		if (optionalTitleEntity.isEmpty()) {
			Payload payload2 = new Payload(
					null,
					null,
					null,
					null,
					null,
					"<all titles>",
					null,
					null,
					processRecords.getAllTitles(titleRepository)
			);
			return new ModelAndView("list", "payload", payload2);
		}
		Optional<HtmlEntity> optionalHtmlEntity = htmlRepository.findById(optionalTitleEntity.get().getHtmlId());
		if (optionalHtmlEntity.isEmpty()) {
			Payload payload2 = new Payload(
					null,
					null,
					null,
					null,
					null,
					"<all titles>",
					null,
					null,
					processRecords.getAllTitles(titleRepository)
			);
			return new ModelAndView("list", "payload", payload2);
		}
		Payload payload2 = new Payload(
				null,
				null,
				null,
				null,
				null,
				null,
				optionalTitleEntity.get().getFilename() + ".html",
				null,
				optionalHtmlEntity.get().getTitleReferences()
		);
		return new ModelAndView(initialView, "payload", payload2);
	}
}
