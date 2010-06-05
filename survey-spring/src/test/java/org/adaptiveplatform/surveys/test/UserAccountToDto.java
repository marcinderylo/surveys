package org.adaptiveplatform.surveys.test;

import org.adaptiveplatform.surveys.domain.UserAccount;
import org.adaptiveplatform.surveys.domain.UserPrivilege;
import org.adaptiveplatform.surveys.dto.UserDto;

import com.google.common.base.Function;

/**
 *
 * @author Marcin Deryło
 */
public final class UserAccountToDto implements Function<UserAccount, UserDto> {

    public static final UserAccountToDto INSTANCE = new UserAccountToDto();

    private UserAccountToDto() {
    }

    @Override
    public UserDto apply(UserAccount from) {
        UserDto user = new UserDto();
        user.setId(from.getId());
        user.setName(from.getEmail());
        for (UserPrivilege priviledge : from.getPrivileges()) {
            user.getRoles().add(priviledge.role);
        }
        return user;
    }
}
