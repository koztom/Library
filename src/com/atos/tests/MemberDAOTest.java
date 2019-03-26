package com.atos.tests;

import com.atos.dao.implementation.MemberDAO;
import com.atos.exception.MemberNotFoundException;
import com.atos.model.Member;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

public class MemberDAOTest {
    private MemberDAO dao;
    private static final Member MEMBER = new Member("John Traven");

    @BeforeEach
    public void setUp() {
        dao = new MemberDAO();
        dao.save(MEMBER);

        Map<Integer,Member> members = dao.getAll();
        assertEquals(MEMBER, members.get(MEMBER.getId()));
    }

    @Nested
    public class NonExistingMember {

        @Test
        public void addingShouldBeSuccess() throws Exception {
            try (Stream<Map.Entry<Integer, Member>> allMembers = dao.getAll().entrySet().stream()) {
                assumeTrue(allMembers.count() == 1);
            }

            final Member nonExistingMember = new Member( "David Jones");
            dao.save(nonExistingMember);
            assertTrue(dao.getAll().containsKey(nonExistingMember.getId()));
            assertMemberCountIs(2);
            assertEquals(nonExistingMember, dao.get(nonExistingMember.getId()));
        }


        @Test
        public void getShouldThrowMemberNotFoundException(){
            Assertions.assertThrows(MemberNotFoundException.class, () -> dao.get(getNonExistingMemberId()));

        }
    }

    @Nested
    public class ExistingMember {

        @Test
        public void savingSecondTimeShouldntAddToMembers() throws Exception {
            dao.save(MEMBER);
            assertMemberCountIs(1);
            assertEquals(MEMBER, dao.get(MEMBER.getId()));
        }

        @Test
        public void getShouldReturnTheMember() throws MemberNotFoundException {
            Member retrievedMember = dao.get(MEMBER.getId());
            assertEquals(MEMBER, retrievedMember);
        }
    }

    @Nested
    public class FewMembersToFind {
        private final Member firstMemberToAdd = new Member( "Adrien Meils");
        private final Member secondMemberToAdd = new Member( "Adrien Roche");

        @BeforeEach
        public void setUp() {
            dao.save(MEMBER);
            dao.save(firstMemberToAdd);
            dao.save(secondMemberToAdd);
            Map<Integer,Member> members = dao.getAll();
            assertEquals(MEMBER, members.get(MEMBER.getId()));
            assertEquals(firstMemberToAdd, members.get(firstMemberToAdd.getId()));
            assertEquals(secondMemberToAdd, members.get(secondMemberToAdd.getId()));
        }

        @Test
        public void shouldThrowExceptionAsThereAreNoParameters()  {
            Assertions.assertThrows(MemberNotFoundException.class, () -> dao.getByName(""));
        }

        @Test
        public void shouldReturnMembersFromGivenName()  {
            Member found = null;
            try {
                found = dao.getByName("Adrien Roche");
            } catch (MemberNotFoundException noMemberFound) {
                noMemberFound.printStackTrace();
            }
            assertEquals(found,secondMemberToAdd);
        }

        @Test
        public void shouldNotReturnAnyMember()  {
            Assertions.assertThrows(MemberNotFoundException.class, () -> dao.getByName("Adrien"));
        }
    }

    private int getNonExistingMemberId() {
        return 999;
    }

    private void assertMemberCountIs(int count) {
        try (Stream<Map.Entry<Integer, Member>> allMembers = dao.getAll().entrySet().stream()) {
            assertEquals(count, allMembers.count());
        }
    }
}