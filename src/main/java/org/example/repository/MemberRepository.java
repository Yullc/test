package org.example.repository;

import org.apache.ibatis.annotations.Mapper;
import org.example.vo.Member;


@Mapper
public interface MemberRepository {
    Member findByGithubId(String githubId);
    void save(Member member);
}