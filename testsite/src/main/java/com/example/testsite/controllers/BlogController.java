package com.example.testsite.controllers;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.testsite.models.Post;
import com.example.testsite.repo.PostRepository;

@Controller
public class BlogController {

	@Autowired
	private PostRepository postRepository;
	@GetMapping("/blog")
	public String blogMain(Model model) {
		Iterable<Post> posts = postRepository.findAll();
		model.addAttribute("posts", posts);
		return "blog-main";
	}
	
	@GetMapping("/blog/add")
	public String blogAdd(Model model) {
		return "blog-add";
	}
	
	@PostMapping("/blog/add")
	public String blogPostAdd(@RequestParam String title,@RequestParam String anons, @RequestParam String full_text, Model model) {
		Post post = new Post(title,anons,full_text);
		postRepository.save(post);
		return "redirect:/blog";
	}
	
	@GetMapping("/blog/{id}")
	public String blogDetails(@PathVariable(value = "id") long postId ,Model model) {
		if(!postRepository.existsById(postId)) {
			return "redirect:/blog";
		}
		Optional<Post> post = postRepository.findById(postId);
		ArrayList<Post> resalt = new ArrayList<>();
		post.ifPresent(resalt :: add);
		model.addAttribute("post", resalt);
		return "blog-details";
	}
	
	@GetMapping("/blog/{id}/edit")
	public String blogEdit(@PathVariable(value = "id") long editId, Model model) {
		if(!postRepository.existsById(editId)) {
			return "redirect:/blog";
		}
		
		Optional<Post> post = postRepository.findById(editId);
		ArrayList<Post> resalt = new ArrayList<>();
		post.ifPresent(resalt :: add);
		model.addAttribute("post", resalt);
		return "blog-edit";
	}
	
	@PostMapping("/blog/{id}/edit")
	public String blogPostApdate(@PathVariable(value = "id") long editId, @RequestParam String title,@RequestParam String anons, @RequestParam String full_text, Model model) {
		Post post = postRepository.findById(editId).orElseThrow();
		post.setTitle(title);
		post.setFull_text(full_text);
		post.setAnons(anons);
		postRepository.save(post);  
		return "redirect:/blog";
	}
	
	@PostMapping("/blog/{id}/remove")
	public String blogPostDelete(@PathVariable(value = "id") long editId, Model model) {
		Post post = postRepository.findById(editId).orElseThrow();
		postRepository.delete(post);
		return "redirect:/blog";
	}
	
}







