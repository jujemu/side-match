package com.sidematch.backend.domain.feed.repository;

import com.sidematch.backend.domain.feed.Feed;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedRepository extends JpaRepository<Feed, Long> {
}
