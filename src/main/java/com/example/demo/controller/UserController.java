package com.example.demo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.entity.Post;
import com.example.demo.service.UserService;

import jakarta.validation.Valid;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
@RequestMapping("/dummyapi")
public class UserController {

	private final UserService userService;

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/list")
	public String getAllPosts(Model model) {
		List<Map<String, Object>> posts = userService.getAllPosts().block();
		model.addAttribute("posts", posts);
		return "list";
	}

	@GetMapping("/create")
	public String createPostForm(Model model) {
		model.addAttribute("post", new Post());
		return "create";
	}

	@GetMapping("/view/{id}")
	public String viewPost(@PathVariable Integer id, Model model) {
		Map<String, Object> post = userService.getPostById(id).block();
		model.addAttribute("post", post);
		return "view";
	}

	@PostMapping("/save")
	public String savePost(@Valid @ModelAttribute("post") Post post, BindingResult bindingResult,
			RedirectAttributes redirectAttributes) {
		if (bindingResult.hasErrors()) {
			return "create";
		}
		userService.savePost(Map.of("title", post.getTitle(), "body", post.getBody(),"userId",post.getUserId())).subscribe();

		return "redirect:/dummyapi/list";
	}

	@GetMapping("/edit/{id}")
	public String editPost(@PathVariable Integer id, Model model) {
		Map<String, Object> postMap = userService.getPostById(id).block();
		Post post = new Post();
		post.setId(id); // Set the id
		post.setTitle((String) postMap.get("title"));
		post.setBody((String) postMap.get("body"));

		model.addAttribute("post", post);
		return "edit";
	}

	@PostMapping("/update/{id}")
	public String updatePost(@PathVariable Integer id, @Valid @ModelAttribute("post") Post updatedPost,
			BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		if (bindingResult.hasErrors()) {
			return "edit";
		}
		userService.updatePost(id, Map.of("title", updatedPost.getTitle(), "body", updatedPost.getBody())).subscribe();

		return "redirect:/dummyapi/list";
	}

	@GetMapping("/delete/{id}")
	public String deletePost(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
		userService.deletePost(id).subscribe();

		return "redirect:/dummyapi/list";
	}

}
