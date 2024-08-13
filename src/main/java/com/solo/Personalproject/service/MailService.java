package com.solo.Personalproject.service;

import jakarta.mail.Message;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender emailSender; //MailConfig @Bean 객체
    public final String ePw = createKey();

    private MimeMessage createMessage(String to)throws  Exception{
        System.out.println("보내는 대상 : "+to);
        System.out.println("인증 번호 : "+ePw);
        MimeMessage message = emailSender.createMimeMessage();

        message.addRecipients(Message.RecipientType.TO, to);//보내는 대상
        message.setSubject("회원 가입 인증코드"); //제목

        String msgg="";
        msgg = "<div style='margin: 20px; padding: 20px; border: 2px solid #87CEEB; border-radius: 10px; background-color: #E0F7FA;'>";
        msgg += "<div style='text-align: center;'>";
        msgg += "</div>";
        msgg += "<h1 style='color: #0073e6; font-family: Arial, sans-serif; text-align: center;'>안녕하세요. VapeWorld입니다.</h1>";
        msgg += "<br>";
        msgg += "<p style='font-family: Arial, sans-serif; color: #333; text-align: center;'>아래 코드를 복사해 입력해주세요.</p>";
        msgg += "<br>";
        msgg += "<p style='font-family: Arial, sans-serif; color: #333; text-align: center;'>감사합니다</p>";
        msgg += "<br>";
        msgg += "<div align='center' style='border: 1px solid #0073e6; padding: 10px; font-family: Verdana, sans-serif; background-color: #f0f8ff; border-radius: 5px;'>";
        msgg += "<h3 style='color: #0073e6;'>회원가입 인증 코드입니다.</h3>";
        msgg += "<div style='font-size: 130%; color: #0073e6;'>";
        msgg += "CODE: <strong>" + ePw + "</strong></div><br/>";
        msgg += "</div>";
        msgg += "</div>";


        message.setText(msgg, "utf-8","html"); //내용
        message.setFrom(new InternetAddress("z9096@naver.com","인증")); //properties에 입력한 이메일


        return message;
    }

    public static String createKey(){
        StringBuffer key = new StringBuffer();
        Random r = new Random();

        for(int i = 0;i<8;i++){
            int index = r.nextInt(3);
            switch (index){
                case 0:
                    key.append((char)((int)r.nextInt(26)+97));//a~z
                    break;
                case 1:
                    key.append((char)((int)(r.nextInt(26))+65));//A~Z
                    break;
                case 2:
                    key.append(r.nextInt(10));//0~9
                    break;
            }
        }
        return key.toString();
    }

    public String sendSimpleMessage(String to) throws Exception{
        MimeMessage message = createMessage(to);
        try{
            emailSender.send(message);
        }catch (MailException es){
            es.printStackTrace();
            throw  new IllegalArgumentException();
        }
        return ePw;
    }

}
