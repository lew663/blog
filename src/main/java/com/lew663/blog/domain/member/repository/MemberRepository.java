package com.lew663.blog.domain.member.repository;

import com.lew663.blog.domain.member.Member;
import com.lew663.blog.domain.member.Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
    Optional<Member> findByRefreshToken(String refreshToken);
    Optional<Member> findByProviderAndProviderId(Provider provider, String providerId);
}
