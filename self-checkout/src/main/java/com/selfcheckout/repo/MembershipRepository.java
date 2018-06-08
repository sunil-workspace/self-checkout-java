package com.selfcheckout.repo;

import java.util.List;

import org.springframework.stereotype.Component;

import com.selfcheckout.dto.Member;

@Component
public interface MembershipRepository {
	
	//public List<Member> findAllMembers();
	
	public List<Member> findMember(int memberNumber);
	

}
