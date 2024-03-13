package com.sidematch.backend.domain.feed.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sidematch.backend.domain.feed.Feed;
import com.sidematch.backend.domain.feed.ProjectDomain;
import com.sidematch.backend.domain.feed.controller.FeedSearchType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.List;

import static com.sidematch.backend.domain.feed.QFeed.feed;
import static com.sidematch.backend.domain.feed.controller.FeedSearchType.TITLE;
import static com.sidematch.backend.domain.feed.controller.FeedSearchType.WRITER;
import static com.sidematch.backend.domain.user.QUser.user;

@RequiredArgsConstructor
public class FeedRepositoryCustomImpl implements FeedRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public Slice<Feed> searchSlice(FeedSearchCondition condition, Pageable pageable) {
        ProjectDomain domain = condition.getDomain();
        FeedSearchType type = condition.getType();
        String likeKeyword = "%" + condition.getKeyword() + "%";

        List<Feed> content = queryFactory
                .selectFrom(feed)
                .join(feed.user, user)
                .where(
                        domainEq(domain),
                        keywordEq(type, likeKeyword)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        boolean hasNext = false;
        if (content.size() > pageable.getPageSize()) {
            hasNext = true;
            content.remove(pageable.getPageSize());
        }

        return new SliceImpl<>(content, pageable, hasNext);
    }

    private BooleanExpression domainEq(ProjectDomain domain) {
        return (domain != null) ? feed.projectDomain.eq(domain) : null;
    }

    private BooleanExpression keywordEq(FeedSearchType type, String likeKeyword) {
        if (type == WRITER) {
            return likeKeyword != null ? feed.user.name.like(likeKeyword) : null;
        }
        if (type == TITLE) {
            return likeKeyword != null ? feed.title.like(likeKeyword) : null;
        }

        return null;
    }
}
