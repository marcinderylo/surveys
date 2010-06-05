package org.adaptiveplatform.surveys.service.internal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.adaptiveplatform.surveys.dto.ActivitiesQuery;
import org.adaptiveplatform.surveys.dto.ActivityTypeEnum;
import org.adaptiveplatform.surveys.dto.EvaluationActivityDto;
import org.adaptiveplatform.surveys.dto.UserDto;
import org.adaptiveplatform.surveys.service.EvaluationActivityDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Marcin Deryło
 */
@Repository
@Transactional
public class JdbcEvaluationActivityDao implements EvaluationActivityDao {

    public static final String GET_ALL_ACTIVITIES_QUERY =
            "SELECT * FROM EVALUATION_ACTIVITIES WHERE USER_ID = :userId "
            + "ORDER BY CREATION_DATE DESC";
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcEvaluationActivityDao(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<EvaluationActivityDto> query(ActivitiesQuery query,
            UserDto caller) {
        return jdbcTemplate.query(GET_ALL_ACTIVITIES_QUERY,
                statementPreparator(query, caller), resultRowMapper());
    }

    private PreparedStatementSetter statementPreparator(
            final ActivitiesQuery query,
            final UserDto caller) {
        return new PreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                if (shouldLimitResults(query)) {
                    ps.setMaxRows(query.getMaxNumberOfResults());
                }
                ps.setLong(1, caller.getId());
            }

            private boolean shouldLimitResults(ActivitiesQuery query) {
                return query.getMaxNumberOfResults() > 0;
            }
        };
    }

    private RowMapper<EvaluationActivityDto> resultRowMapper() {
        return new RowMapper<EvaluationActivityDto>() {

            public static final String ACTIVITY_ID_COLUMN = "ACTIVITY_ID";
            public static final String ACTIVITY_NAME_COLUMN =
                    "ACTIVITY_DESCRIPTION";
            public static final String ACTIVITY_TYPE_COLUMN = "ACTIVITY_TYPE";
            public static final String CREATION_DATE_COLUMN = "CREATION_DATE";

            @Override
            public EvaluationActivityDto mapRow(ResultSet rs, int rowNum) throws
                    SQLException {
                EvaluationActivityDto activity = new EvaluationActivityDto();
                activity.setCreationDate(rs.getDate(CREATION_DATE_COLUMN));
                activity.setId(rs.getLong(ACTIVITY_ID_COLUMN));
                activity.setName(rs.getString(ACTIVITY_NAME_COLUMN));
                activity.setType(ActivityTypeEnum.valueOf(
                        rs.getString(ACTIVITY_TYPE_COLUMN)));
                return activity;
            }
        };
    }
}
