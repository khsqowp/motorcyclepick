package com.example.motorcyclepick.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.StringTypeHandler;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

// MyBatis 설정을 위한 Configuration 클래스
@Configuration
// MyBatis Mapper 인터페이스 스캔 설정
@MapperScan("com.example.motorcyclepick.repository")
public class MyBatisConfig {

    // SqlSessionFactory 빈 설정
    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        // SqlSessionFactory 생성을 위한 팩토리 빈 객체 생성
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();

        // 데이터소스 설정
        sessionFactory.setDataSource(dataSource);

        // Mapper XML 파일 위치 설정 (classpath:mapper/*.xml)
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources("classpath:mapper/*.xml"));

        // MyBatis 설정
        org.apache.ibatis.session.Configuration configuration =
                new org.apache.ibatis.session.Configuration();

        // 언더스코어 네이밍을 카멜케이스로 자동 매핑 (예: user_name -> userName)
        configuration.setMapUnderscoreToCamelCase(true);

        // NULL 값에 대해서도 setter 호출 허용
        configuration.setCallSettersOnNulls(true);

        // Enum 타입을 문자열로 처리하도록 설정
        configuration.setDefaultEnumTypeHandler(StringTypeHandler.class);

        // MyBatis 타임아웃 설정
        configuration.setDefaultStatementTimeout(Integer.MAX_VALUE);
        configuration.setDefaultExecutorType(org.apache.ibatis.session.ExecutorType.REUSE);

        // 설정 적용
        sessionFactory.setConfiguration(configuration);

        // SqlSessionFactory 객체 생성 및 반환
        return sessionFactory.getObject();
    }
}