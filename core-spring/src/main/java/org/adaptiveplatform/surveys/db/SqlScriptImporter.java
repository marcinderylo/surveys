package org.adaptiveplatform.surveys.db;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.stereotype.Service;

@Service("sqlScriptImporter")
public class SqlScriptImporter {

    @Resource
    private DataSource dataSource;
    @Resource
    private ResourceLoader resourceLoader;

    public void executeScript(String resourcePath) throws IOException,
            SQLException {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();

            org.springframework.core.io.Resource resource = resourceLoader.
                    getResource(ResourceLoader.CLASSPATH_URL_PREFIX
                    + resourcePath);
            String scriptText = slurp(resource.getInputStream());
            connection.createStatement().execute(scriptText);
        } finally {
            JdbcUtils.closeConnection(connection);
        }


    }

    private String slurp(InputStream in) throws IOException {
        StringBuffer out = new StringBuffer();
        byte[] b = new byte[4096];
        for (int n; (n = in.read(b)) != -1;) {
            out.append(new String(b, 0, n));
        }
        return out.toString();
    }
}
