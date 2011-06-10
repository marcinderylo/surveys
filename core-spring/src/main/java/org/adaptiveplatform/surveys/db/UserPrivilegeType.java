package org.adaptiveplatform.surveys.db;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.adaptiveplatform.surveys.domain.UserPrivilege;
import org.apache.commons.lang.ObjectUtils;
import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;

/**
 *
 * @author Marcin Deryło
 */
public class UserPrivilegeType implements UserType {

    @Override
    public Object assemble(Serializable cached, Object owner) throws
            HibernateException {
        return cached;
    }

    @Override
    public Object deepCopy(Object value) throws HibernateException {
        return value;
    }

    @Override
    public Serializable disassemble(Object value) throws HibernateException {
        return (Serializable) value;
    }

    @Override
    public boolean equals(Object x, Object y) throws HibernateException {
        if (x == y) {
            return true;
        } else {
            return ObjectUtils.equals(y, y);
        }
    }

    @Override
    public int hashCode(Object x) throws HibernateException {
        assert (x != null);
        return x.hashCode();
    }

    @Override
    public boolean isMutable() {
        return false;
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, Object owner)
            throws HibernateException, SQLException {
        String value = rs.getString(names[0]);
        if (rs.wasNull()) {
            return null;
        }
        return UserPrivilege.getByRole(value);
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index)
            throws HibernateException, SQLException {
        String out = null;
        if (value != null) {
            UserPrivilege privilege = (UserPrivilege) value;
            out = privilege.role;
        }
        st.setString(index, out);
    }

    @Override
    public Object replace(Object original, Object target, Object owner)
            throws HibernateException {
        return original;
    }

    @Override
    public Class<?> returnedClass() {
        return UserPrivilege.class;
    }

    @Override
    public int[] sqlTypes() {
        return new int[]{Types.VARCHAR};
    }
}

