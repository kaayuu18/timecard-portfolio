package com.example.timecard;

// アプリの起動入口。mainからSpring Bootを開始する。

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TimecardApplication {

	public static void main(String[] args) {
		SpringApplication.run(TimecardApplication.class, args);
	}

}
