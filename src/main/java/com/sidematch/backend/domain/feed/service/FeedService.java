package com.sidematch.backend.domain.feed.service;


import com.sidematch.backend.config.jwt.TokenProvider;
import com.sidematch.backend.domain.feed.Feed;
import com.sidematch.backend.domain.feed.ProjectDomain;
import com.sidematch.backend.domain.feed.controller.FeedSearchRequest;
import com.sidematch.backend.domain.feed.controller.FeedSearchResponse;
import com.sidematch.backend.domain.feed.controller.FeedSearchType;
import com.sidematch.backend.domain.feed.controller.FeedSliceResponse;
import com.sidematch.backend.domain.feed.repository.FeedRepository;
import com.sidematch.backend.domain.feed.repository.FeedRepositoryCustom;
import com.sidematch.backend.domain.user.User;
import com.sidematch.backend.domain.user.repository.UserRepository;
import jakarta.persistence.EntityManager;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Getter
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class FeedService {

    private final EntityManager em;
    private final UserRepository userRepository;
    private final FeedRepositoryCustom feedRepositoryCustom;
    private final FeedRepository feedRepository;
    private final TokenProvider tokenProvider;

    /**
     * 유저가 로그인을 한 경우라면 유저가 표시한 좋아요 여부를 같이 응답
     * 아니라면 도메인, 작성자 혹은 피드 제목을 기준으로 필터된 값을 슬라이스하여 응답
     */
    public FeedSliceResponse getSliceFeed(FeedSearchRequest request, Pageable pageable) {
        List<Feed> feeds = feedRepositoryCustom.findFeedListByFeedRequest(request, pageable);
        List<FeedSearchResponse> responseFeeds = new ArrayList<>();
        for (Feed feed : feeds) {
            FeedSearchResponse feedSearchResponse = FeedSearchResponse.builder().user(feed.getUser()).feed(feed).build();
            boolean isLiked = false;
            feedSearchResponse.setLiked(isLiked);
            responseFeeds.add(feedSearchResponse);
        }

        FeedSliceResponse response = new FeedSliceResponse();
        response.setFeedSearchResponses(responseFeeds);
        response.setSize(feeds.size());
        response.setHasNextSlice(hasNextFeedSlice(request, pageable));

        return response;
    }

    // todo: refactoring, hasNextSlice할 필요없이 하나의 query로 카운트 할 수 있음.
    private boolean hasNextFeedSlice(FeedSearchRequest request, Pageable pageable) {

        if (request.getSearchType() != null) {
            if (request.getDomain() == ProjectDomain.전체) {
                String jpql = (request.getSearchType() == FeedSearchType.TITLE)
                        ? "select count(cnt) from (select f.id AS cnt from Feed f where f.title like CONCAT('%', :searchValue, '%') GROUP BY f.id order by f.id DESC)"
                        : "select count(cnt) from (select f.id AS cnt from Feed f join f.user u on u.name like CONCAT('%', :searchValue, '%') GROUP BY f.id order by f.id DESC)";

                try {
                    Long countQuery = em.createQuery(jpql, Long.class)
                            .setParameter("searchValue", request.getSearchValue())
                            .getSingleResult();
                    return countQuery > ((pageable.getPageNumber() + 1L) * pageable.getPageSize());
                } catch (Exception e) {
                    return false;
                }
            } else {
                String jpql = (request.getSearchType() == FeedSearchType.TITLE)
                        ? "select count(cnt) from (select f.id AS cnt from Feed f where f.title like CONCAT('%', :searchValue, '%') and f.projectDomain = :domain GROUP BY f.id order by f.id DESC)"
                        : "select count(cnt) from (select f.id AS cnt from Feed f join f.user u on u.name like CONCAT('%', :searchValue, '%') and f.projectDomain = :domain GROUP BY f.id order by f.id DESC)";

                try {
                    Long countQuery = em.createQuery(jpql, Long.class)
                            .setParameter("domain", request.getDomain())
                            .setParameter("searchValue", request.getSearchValue())
                            .getSingleResult();
                    return countQuery > ((pageable.getPageNumber() + 1L) * pageable.getPageSize());
                } catch (Exception e) {
                    return false;
                }
            }
        } else {
            if (request.getDomain() == ProjectDomain.전체) {
                String jpql = "select count(cnt) from (select f.id AS cnt from Feed f GROUP BY f.id)";

                try {
                    Long countQuery = em.createQuery(jpql, Long.class)
                            .getSingleResult();
                    //                log.info("카운트 쿼리 " + Long.toString(countQuery));
                    return countQuery > ((pageable.getPageNumber() + 1L) * pageable.getPageSize());
                } catch (Exception e) {
                    return false;
                }
            } else {
                String jpql = "select count(cnt) from (select f.id AS cnt from Feed f WHERE f.projectDomain = :domain GROUP BY f.id)";

                try {
                    Long countQuery = em.createQuery(jpql, Long.class)
                            .setParameter("domain", request.getDomain())
                            .getSingleResult();
                    return countQuery > ((pageable.getPageNumber() + 1L) * pageable.getPageSize());
                } catch (Exception e) {
                    return false;
                }
            }
        }
    }
}
