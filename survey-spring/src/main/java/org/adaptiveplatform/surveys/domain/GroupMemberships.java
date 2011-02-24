package org.adaptiveplatform.surveys.domain;

import com.google.common.collect.Sets;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import org.adaptiveplatform.surveys.dto.UserDto;
import org.hibernate.annotations.CollectionOfElements;

@Embeddable
public class GroupMemberships implements Serializable {

    @CollectionOfElements(targetElement = GroupMember.class)
    @JoinTable(name = "STUDENT_GROUPS_MEMBERS", joinColumns = {@JoinColumn(name = "GROUP_ID")})
    private List<GroupMember> members = new ArrayList<GroupMember>();

    void addMemberWithRole(UserDto user, GroupRole role) {
        members.add(new GroupMember(user, role));
    }

    void removeMember(UserDto user) {
        Iterator<GroupMember> iterator = members.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getUser().isSameAs(user)) {
                iterator.remove();
            }
        }
    }

    Set<GroupRole> getRoleOf(UserDto user) {
        Set<GroupRole> roles = Sets.newHashSet();
        for (GroupMember member : members) {
            if (member.getUser().isSameAs(user)) {
                roles.add(member.getRole());
            }
        }
        return roles;
    }
}
