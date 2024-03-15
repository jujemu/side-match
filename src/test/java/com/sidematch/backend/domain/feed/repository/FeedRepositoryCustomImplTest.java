package com.sidematch.backend.domain.feed.repository;

import com.sidematch.backend.domain.feed.Feed;
import com.sidematch.backend.domain.feed.ProjectDomain;
import com.sidematch.backend.domain.feed.controller.FeedSearchType;
import com.sidematch.backend.domain.user.User;
import com.sidematch.backend.domain.user.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.sidematch.backend.domain.feed.ProjectDomain.문화;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.BDDMockito.given;

@Transactional
@SpringBootTest
class FeedRepositoryCustomImplTest {

    @Autowired
    FeedRepository feedRepository;
    @Autowired
    UserRepository userRepository;
    @Mock
    Pageable pageable;

    @DisplayName("검색 조건을 통해 피드 목록을 조회할 수 있다.")
    @Test
    void searchFeedSlice() throws Exception {
        // given
        User user = User.builder()
                .email("test@test.com")
                .name("test user")
                .build();
        userRepository.save(user);

        // 4 개의 피드를 생성하고 3 개를 조회하여 다음 페이지가 있는 것을 확인한다.
        Feed feed1 = createFeed(user, "test title 1", "test content 1");
        Feed feed2 = createFeed(user, "test title 2", "test content 2");
        Feed feed3 = createFeed(user, "test title 3", "test content 3");
        Feed feed4 = createFeed(user, "test title 4", "test content 4");
        feedRepository.saveAll(List.of(feed1, feed2, feed3, feed4));

        FeedSearchCondition condition = FeedSearchCondition.builder()
                .domain(문화)
                .type(FeedSearchType.TITLE)
                .keyword("test")
                .build();

        given(pageable.getOffset()).willReturn(0L);
        given(pageable.getPageSize()).willReturn(3);

        // when
        Slice<FeedRepositoryResponse> feedRepositoryResponses = feedRepository.searchSlice(condition, pageable);

        // then
        assertThat(feedRepositoryResponses.getSize()).isEqualTo(3);
        assertThat(feedRepositoryResponses.hasNext()).isTrue();
        assertThat(feedRepositoryResponses.getContent()).hasSize(3)
                .extracting("id", "title", "content")
                .containsExactly(
                        tuple(1L, feed1.getTitle(), feed1.getContent()),
                        tuple(2L, feed2.getTitle(), feed2.getContent()),
                        tuple(3L, feed3.getTitle(), feed3.getContent())
                );
        assertThat(feedRepositoryResponses.getContent()).allMatch(response ->
                response.getUserId().equals(user.getId()));
    }

    private Feed createFeed(User user, String title, String content) {
        return Feed.builder()
                .title(title)
                .content(content)
                .user(user)
                .projectDomain(문화)
                .build();
    }
}