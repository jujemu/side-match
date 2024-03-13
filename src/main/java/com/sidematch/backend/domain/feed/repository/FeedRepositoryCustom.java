package com.sidematch.backend.domain.feed.repository;

import com.sidematch.backend.domain.feed.Feed;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface FeedRepositoryCustom {

    Slice<Feed> searchSlice(FeedSearchCondition condition, Pageable pageable);
}
