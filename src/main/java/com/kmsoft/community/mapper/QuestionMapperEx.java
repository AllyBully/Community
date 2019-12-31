package com.kmsoft.community.mapper;

import com.kmsoft.community.model.Question;

public interface QuestionMapperEx {

    int incView(Question record);

    int incComment(Question record);

}