package com;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.EnableTransactionManagement;


//定时任务也先不要
//@EnableScheduling
//数据库配置先不要
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@ServletComponentScan
@ComponentScan("com")
@EnableTransactionManagement 
public class Application extends SpringBootServletInitializer{

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public Application() {
		super();
	    setRegisterErrorPageFilter(false);
	}
	
	/**
	 * 这个类的作用与在web.xml中配置负责初始化Spring应用上下文的监听器作用类似
	 */
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(Application.class);
	}
	
	@Bean
	public EmbeddedServletContainerCustomizer containerCustomizer() {

		return new EmbeddedServletContainerCustomizer() {
			@Override
			public void customize(ConfigurableEmbeddedServletContainer container) {

				ErrorPage error401Page = new ErrorPage(HttpStatus.UNAUTHORIZED, "/error/401.html");
				ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, "/error/404.html");
				ErrorPage error500Page = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error/500.html");

				container.addErrorPages(error401Page, error404Page, error500Page);
			}
		};
	}
	
	//必须要这个配置， 否则有AbandonedConnectionCleanupThread 导致内存溢出
    /*@Bean
    protected ServletContextListener listener() {

        return new ServletContextListener() {

            @Override
            public void contextInitialized(ServletContextEvent sce) {
            	logger.debug("Initialising Context...");
            }

            @Override
            public final void contextDestroyed(ServletContextEvent sce) {

            	logger.debug("Destroying Context...");

                try {
                	logger.debug("Calling MySQL AbandonedConnectionCleanupThread shutdown");
                    com.mysql.jdbc.AbandonedConnectionCleanupThread.shutdown();

                } catch (Exception e) {
                	logger.error("Error calling MySQL AbandonedConnectionCleanupThread shutdown {}", e);
                }

                ClassLoader cl = Thread.currentThread().getContextClassLoader();

                Enumeration<Driver> drivers = DriverManager.getDrivers();
                while (drivers.hasMoreElements()) {
                    Driver driver = drivers.nextElement();

                    if (driver.getClass().getClassLoader() == cl) {

                        try {
                        	logger.debug("Deregistering JDBC driver {}", driver);
                            DriverManager.deregisterDriver(driver);

                        } catch (SQLException ex) {
                        	logger.error("Error deregistering JDBC driver {}", driver, ex);
                        }

                    } else {
                    	logger.trace("Not deregistering JDBC driver {} as it does not belong to this webapp's ClassLoader", driver);
                    }
                }
            }
        };
    }*/
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
