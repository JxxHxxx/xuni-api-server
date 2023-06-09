package com.jxx.xuni.studyproduct.dto.response;

import com.jxx.xuni.common.domain.Category;
import com.jxx.xuni.studyproduct.domain.Content;

import java.util.List;

public record StudyProductContentReadResponse(
        String name,
        Category category,
        String creator,
        String thumbnail,
        List<Content> contents
)
{ }
