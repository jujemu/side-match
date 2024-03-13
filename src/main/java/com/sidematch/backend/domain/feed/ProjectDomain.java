package com.sidematch.backend.domain.feed;

import java.util.Arrays;

public enum ProjectDomain {
    전체, 건강, 교육, 여행, 음식, 사회, 과학, 기술, 환경, 문화, 경제, 기타;

    public static ProjectDomain of(String projectDomain) {
        return ProjectDomain.valueOf(projectDomain);
    }
}
