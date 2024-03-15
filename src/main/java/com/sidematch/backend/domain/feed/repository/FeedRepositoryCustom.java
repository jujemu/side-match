package com.sidematch.backend.domain.feed.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface FeedRepositoryCustom {

    Slice<FeedRepositoryResponse> searchSlice(FeedSearchCondition condition, Pageable pageable);
}
