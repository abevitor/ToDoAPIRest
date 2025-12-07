package com.example.demo.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    public void enviarEmail(String para, String assunto, String mensagem) {
        SimpleMailMessage msg = new SimpleMailMessage();

        msg.setTo(para);
        msg.setSubject(assunto);
        msg.setText(mensagem);

        emailSender.send(msg);
    }
    
}
