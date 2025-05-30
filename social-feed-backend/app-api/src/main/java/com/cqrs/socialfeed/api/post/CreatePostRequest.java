package com.cqrs.socialfeed.api.post;

import java.util.List;

public record CreatePostRequest(String content, List<String> imageUrls) {}