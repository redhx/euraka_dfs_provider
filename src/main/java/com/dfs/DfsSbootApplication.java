package com.dfs;

import com.dfs.bean.ConfigBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
@EnableConfigurationProperties({ConfigBean.class})
public class DfsSbootApplication {

	public static void main(String[] args) {
		SpringApplication.run(DfsSbootApplication.class, args);
	}
}
