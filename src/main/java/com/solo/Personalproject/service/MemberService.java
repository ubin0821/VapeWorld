package com.solo.Personalproject.service;

import com.solo.Personalproject.entity.Member;
import com.solo.Personalproject.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService implements UserDetailsService  {
    private final MemberRepository memberRepository;

   public Member saveMember(Member member) {
       validateDuplicateMember(member);
       return memberRepository.save(member);
   }
   private void validateDuplicateMember(Member member) {
       Member findMember = memberRepository.findByEmail(member.getEmail());
       if (findMember != null) {
           throw  new IllegalStateException("이미 가입된 이메일입니다.");
       }
       findMember = memberRepository.findByTel(member.getTel());
       if (findMember != null) {
           throw new IllegalStateException("이미 가입된 전화번호입니다.");
       }
   }
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email);

        if(member == null){
            throw new UsernameNotFoundException(email);
        }
        //빌더패턴
        return User.builder().username(member.getEmail())
                .password(member.getPassword())
                .roles(member.getRole().toString())
                .build();
    }
}
