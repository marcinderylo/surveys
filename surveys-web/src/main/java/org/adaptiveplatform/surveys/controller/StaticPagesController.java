package org.adaptiveplatform.surveys.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class StaticPagesController {

	@RequestMapping("/")
	public String defaultPage() {
		return "redirect:/static/surveys-flex.swf";
	}
}
