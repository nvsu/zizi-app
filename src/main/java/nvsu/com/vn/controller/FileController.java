package nvsu.com.vn.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class FileController {

	@RequestMapping("/test")
	public @ResponseBody String greeting() {
		return "Hello, World";
	}
}
