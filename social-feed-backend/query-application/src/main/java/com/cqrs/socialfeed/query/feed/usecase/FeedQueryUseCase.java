package com.cqrs.socialfeed.query.feed.usecase;

import com.cqrs.socialfeed.query.feed.request.GetFeedsByUserIdRequest;
import com.cqrs.socialfeed.query.feed.response.FeedResponse;

import java.util.List;

public interface FeedQueryUseCase {
    List<FeedResponse> getFeeds(GetFeedsByUserIdRequest request);
}
