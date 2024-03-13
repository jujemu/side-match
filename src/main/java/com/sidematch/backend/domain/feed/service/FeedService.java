package com.sidematch.backend.domain.feed.service;


import com.sidematch.backend.domain.feed.Feed;
import com.sidematch.backend.domain.feed.controller.dto.FeedSearchRequest;
import com.sidematch.backend.domain.feed.controller.dto.FeedSliceResponse;
import com.sidematch.backend.domain.feed.repository.FeedRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Getter
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class FeedService {

    private final FeedRepository feedRepository;

    /**
     * 유저가 로그인을 한 경우라면 유저가 표시한 좋아요 여부를 같이 응답
     * 아니라면 도메인, 작성자 혹은 피드 제목을 기준으로 필터된 값을 슬라이스하여 응답
     */
    public FeedSliceResponse getSliceFeed(FeedSearchRequest request, Pageable pageable) {
        Slice<Feed> feeds = feedRepository.searchSlice(request.toConditionEntity(), pageable);
        return FeedSliceResponse.of(feeds);
    }
}
