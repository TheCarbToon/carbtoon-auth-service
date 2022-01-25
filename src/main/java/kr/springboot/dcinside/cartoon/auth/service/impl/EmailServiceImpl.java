package kr.springboot.dcinside.cartoon.auth.service.impl;

import kr.springboot.dcinside.cartoon.auth.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class EmailServiceImpl implements EmailService {

    private final static String subject = "The Carbtoon 회원가입 인증 메일 입니다.";

    private final static String url = "http://192.168.2.142:9000/auth/email/";

    private final JavaMailSender emailSender;

    @Async
    @Override
    public void sendAuthMail(String to, String uuid) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("carbtoon@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        StringBuilder sb = new StringBuilder();
        sb.append("이메일 인증을 위해 아래의 링크를 눌러 이메일 인증을 완료해 주세요");
        sb.append("\uD83D\uDE00");
        sb.append("\n");
        sb.append(url);
        sb.append(uuid);
        message.setText(sb.toString());
        emailSender.send(message);
    }

}
