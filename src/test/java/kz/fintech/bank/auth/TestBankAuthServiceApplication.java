package kz.fintech.bank.auth;

import org.springframework.boot.SpringApplication;

public class TestBankAuthServiceApplication {

    public static void main(String[] args) {
        SpringApplication.from(BankAuthServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
