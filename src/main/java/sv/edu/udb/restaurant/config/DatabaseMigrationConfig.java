package sv.edu.udb.restaurant.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class DatabaseMigrationConfig {

    @Bean
    ApplicationRunner relaxPublicOrderColumns(
            JdbcTemplate jdbcTemplate,
            @Value("${spring.datasource.url}") String datasourceUrl) {

        return args -> {

            if (!datasourceUrl.startsWith("jdbc:mysql:")) {
                return;
            }

            jdbcTemplate.execute(
                    "alter table orders modify user_id bigint null");
            jdbcTemplate.execute(
                    "alter table orders modify table_id bigint null");
        };
    }
}
