package com.kmsoft.community.mapper;

import com.kmsoft.community.model.Question;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface QuestionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Question record);

    int insertSelective(Question record);

    Question selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Question record);

    int updateByPrimaryKeyWithBLOBs(Question record);

    int updateByPrimaryKey(Question record);

    List<Question> selectByPage(Map<String, Object> paramMap);

    int count();

    List<Question> selectByCreator(Map<String, Object> paramMap);

    int countByCreator(String creator);
}