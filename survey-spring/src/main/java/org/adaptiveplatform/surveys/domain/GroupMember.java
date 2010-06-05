package org.adaptiveplatform.surveys.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.adaptiveplatform.surveys.dto.UserDto;
import org.apache.commons.lang.Validate;

/**
 * @author Marcin Deryło
 */
@Embeddable
public class GroupMember implements Serializable {

        @ManyToOne(optional = false, fetch = FetchType.EAGER)
        @JoinColumn(name = "MEMBER_ID")
        private UserDto user;
        @Enumerated(EnumType.STRING)
        @Column(name = "GROUP_ROLE")
        private GroupRole role;

        protected GroupMember() {
            // To be used only by object persistence framework
        }

        public GroupMember(UserDto user, GroupRole role) {
                Validate.notNull(user, "Member user must be specified");
                Validate.notNull(role,
                        "Member's role in the group must be specified");
                this.user = user;
                this.role = role;
        }

        public GroupRole getRole() {
                return role;
        }

        public UserDto getUser() {
                return user;
        }
}
