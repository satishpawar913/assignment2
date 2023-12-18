package com.example.demo.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.demo.repository.UserRepo;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserServiceImpl implements UserService {

	private final UserRepo userRepo;
	private final WebClient webClient;

	private final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	public UserServiceImpl(UserRepo userRepo, WebClient webClient) {
		this.userRepo = userRepo;
		this.webClient = webClient;
	}

	private static final String POST_ENDPOINT = "/posts";

	public Mono<List<Map<String, Object>>> getAllPosts() {
		return webClient.get().uri(POST_ENDPOINT).retrieve()
				.bodyToMono(new ParameterizedTypeReference<List<Map<String, Object>>>() {
				}).doOnSuccess(response -> log.info("Retrieved all posts successfully"));
	}

	public Mono<Map<String, Object>> getPostById(Integer postId) {
		return webClient.get().uri(POST_ENDPOINT + "/{id}", postId).retrieve()
				.bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
				}).doOnSuccess(response -> log.info("Retrieved post by ID successfully. Post: {}", response));
	}

	public Mono<Map<String, Object>> savePost(Map<String, Object> post) {
		return webClient.post().uri(POST_ENDPOINT).bodyValue(post).retrieve()
				.bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
				}).doOnSuccess(response -> log.info("Post saved successfully. Post: {}", response));
	}

	public Mono<Map<String, Object>> updatePost(Integer postId, Map<String, Object> updatedPost) {
		return webClient.put().uri(POST_ENDPOINT + "/{id}", postId).bodyValue(updatedPost).retrieve()
				.bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
				}).doOnSuccess(response -> log.info("Post updated successfully. Post: {}", response));
	}

	public Mono<Void> deletePost(Integer postId) {
		return webClient.delete().uri(POST_ENDPOINT + "/{id}", postId).retrieve().toEntity(Void.class)
				.doOnSuccess(responseEntity -> {
					HttpStatus statusCode = (HttpStatus) responseEntity.getStatusCode();
					log.info("Post deleted successfully. Status code: {}", statusCode);
					log.info("Post ID: {}", postId);
				}).then();
	}

}
