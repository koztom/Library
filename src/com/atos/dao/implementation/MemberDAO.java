package com.atos.dao.implementation;

import com.atos.dao.Dao;
import com.atos.model.Member;

import java.util.*;

public class MemberDAO implements Dao<Member> {

    private Map<Integer, Member> members = new HashMap<>();

    public Member getByName(String name) {
        return members.values()
                .stream()
                .filter(member -> name.equals(member.getName()))
                .findAny()
                .orElse(null);
    }

    public Member get(Integer id) {
        return members.get(id);
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
