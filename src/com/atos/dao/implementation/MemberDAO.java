package com.atos.dao.implementation;

import com.atos.dao.Dao;
import com.atos.exception.MemberNotFoundException;
import com.atos.model.Member;

import java.util.HashMap;
import java.util.Map;

public class MemberDAO implements Dao<Member> {

    private Map<Integer, Member> members = new HashMap<>();

    public Member getByName(String name) throws MemberNotFoundException {
        return members.values()
                .stream()
                .filter(member -> name.equals(member.getName()))
                .findAny().orElseThrow(()->new MemberNotFoundException(name));
    }

    public Member get(Integer id) throws MemberNotFoundException {
        Member member = members.get(id);
        if(member==null){
            throw new MemberNotFoundException(id.toString());
        }
        return member;
    }

    public Map<Integer, Member> getAll() {
        return members;
    }

    public void save(Member newMember) {
        members.put(newMember.getId(), newMember);
    }

    public void delete(Integer id) {
        members.remove(id);
    }
}
