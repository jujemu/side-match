package com.sidematch.backend.domain.feed.controller;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class FeedSliceResponse {

    private List<FeedSearchResponse> feedSearchResponses;
    private int size;
    private Boolean hasNextSlice;

}
