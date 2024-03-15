package com.sidematch.backend.domain.feed.controller.dto;

import com.sidematch.backend.domain.feed.repository.FeedRepositoryResponse;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Slice;

import java.util.List;

@Data
@NoArgsConstructor
public class FeedSliceServiceResponse {

    private List<FeedSearchResponse> feedSearchResponses;
    private int size;
    private Boolean hasNextSlice;

    private FeedSliceServiceResponse(List<FeedSearchResponse> feedSearchResponses, int size, Boolean hasNextSlice) {
        this.feedSearchResponses = feedSearchResponses;
        this.size = size;
        this.hasNextSlice = hasNextSlice;
    }

    public static FeedSliceServiceResponse of(Slice<FeedRepositoryResponse> repositoryResponses) {
        List<FeedSearchResponse> responses = repositoryResponses.stream()
                .map(FeedSearchResponse::of)
                .toList();

        return new FeedSliceServiceResponse(
                responses,
                repositoryResponses.getSize(),
                repositoryResponses.hasNext()
        );
    }

}
