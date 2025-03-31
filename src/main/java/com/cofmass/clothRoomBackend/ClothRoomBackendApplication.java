package com.cofmass.clothRoomBackend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@SpringBootApplication
@MapperScan("com.cofmass.clothRoomBackend.mapper")
public class ClothRoomBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClothRoomBackendApplication.class, args);
        System.out.println("====================启动成功====================");
    }

}
