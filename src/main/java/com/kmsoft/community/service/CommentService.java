package com.kmsoft.community.service;

import com.kmsoft.community.dto.CommentDTO;
import com.kmsoft.community.model.Comment;

import java.util.List;

public interface CommentService {

    int addComment(Comment comment);

    List<CommentDTO> listByQuestionId(Long id);
}
