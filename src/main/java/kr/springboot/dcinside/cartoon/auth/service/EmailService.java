package kr.springboot.dcinside.cartoon.auth.service;

public interface EmailService {

    void sendAuthMail(String to, String uuid);

}
