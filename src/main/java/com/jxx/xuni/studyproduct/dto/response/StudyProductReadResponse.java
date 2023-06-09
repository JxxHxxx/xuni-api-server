package com.jxx.xuni.studyproduct.dto.response;

import com.jxx.xuni.common.domain.Category;

public record StudyProductReadResponse(
        String studyProductId,
        String name,
        Category category,
        String creator,
        String thumbnail
) {}