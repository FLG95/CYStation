package io.squid.CyStation;


import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class CyStationApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(CyStationApplication.class, args);
    }



    @Override
    public void run(String... args) throws Exception {
        System.out.println("Server started");

    }

}

