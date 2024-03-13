package com.sidematch.backend.domain.feed.controller.dto;

import com.sidematch.backend.domain.feed.Feed;
import com.sidematch.backend.domain.feed.controller.dto.FeedSearchResponse;
import com.sidematch.backend.domain.user.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Slice;

import java.util.List;

@Data
@NoArgsConstructor
public class FeedSliceResponse {

    private List<FeedSearchResponse> feedSearchResponses;
    private int size;
    private Boolean hasNextSlice;

    private FeedSliceResponse(List<FeedSearchResponse> feedSearchResponses, int size, Boolean hasNextSlice) {
        this.feedSearchResponses = feedSearchResponses;
        this.size = size;
        this.hasNextSlice = hasNextSlice;
    }

    public static FeedSliceResponse of(Slice<Feed> feeds) {
        List<FeedSearchResponse> responses = feeds.stream()
                .map(FeedSearchResponse::of)
                .toList();

        return new FeedSliceResponse(
                responses,
                feeds.getSize(),
                feeds.hasNext()
        );
    }

}
