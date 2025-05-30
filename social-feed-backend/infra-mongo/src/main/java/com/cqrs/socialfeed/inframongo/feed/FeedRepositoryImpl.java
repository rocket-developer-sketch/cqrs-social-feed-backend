package com.cqrs.socialfeed.inframongo.feed;

import com.cqrs.socialfeed.domain.feed.Feed;
import com.cqrs.socialfeed.domain.feed.FeedRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class FeedRepositoryImpl implements FeedRepository {

    private final MongoTemplate mongoTemplate;

    public FeedRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void save(Feed feed) {
        FeedDocument document = new FeedDocument(
                feed.getUserId(),
                feed.getPostId(),
                feed.getPostAuthorId(),
                feed.getCreatedAt(),
                feed.getPushedAt()
        );
        mongoTemplate.save(document);
    }

    @Override
    public List<Feed> findFeedsByUserId(Long receiverId, Long cursorPostId, int size) {
        Query query = new Query(Criteria.where("userId").is(receiverId));
        if (cursorPostId != null) {
            query.addCriteria(Criteria.where("postId").lt(cursorPostId));
        }
        query.with(Sort.by(Sort.Direction.DESC, "postId")); // pushedAt 전파 시점으로 정렬 되면 좋을텐데
        query.limit(size);

        return mongoTemplate.find(query, FeedDocument.class).stream()
                .map(doc -> new Feed(
                        doc.getUserId(),
                        doc.getPostId(),
                        doc.getPostAuthorId(),
                        doc.getCreatedAt(),
                        doc.getPushedAt()
                ))
                .collect(Collectors.toList());
    }
}
