package br.com.projeto.jpa.padrao.config;

import java.io.PrintWriter;
import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class CPConfiguration {

	@Bean
	@Profile("prod")
	@ConfigurationProperties("spring.datasource")
	public DataSource customDatasource() {
		Properties props = new Properties();

        props.setProperty("dataSourceClassName", "org.postgresql.ds.PGSimpleDataSource");
        props.setProperty("dataSource.user", "postgres");
        props.setProperty("dataSource.password", "1234");
        props.setProperty("dataSource.databaseName", "escola");
        props.put("dataSource.logWriter", new PrintWriter(System.out));

        HikariConfig config = new HikariConfig(props);
        HikariDataSource ds = new HikariDataSource(config);
        return ds;
	}
	
	@Bean
	@Profile("test")
	@ConfigurationProperties("spring.datasource")
	public DataSource customDatasourceTest() {
		
		 HikariDataSource dataSource = new HikariDataSource();
		 dataSource.setDriverClassName("org.h2.Driver");
		 dataSource.setJdbcUrl("jdbc:h2:~/testdb");
		 dataSource.setUsername("sa");
		 dataSource.setPassword("");
		 return dataSource;
	}
}
