package com.ironhack.midtermproject.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PwdUtils {

    public static void main(String[] args) throws JsonProcessingException {

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        System.out.println(passwordEncoder.encode("123"));
        System.out.println(passwordEncoder.encode("admin"));
        System.out.println(passwordEncoder.encode("secretkey"));
        System.out.println(passwordEncoder.encode("hashedkey"));

        System.out.println(passwordEncoder.encode("p4ssword"));
        System.out.println(passwordEncoder.encode("adm1n"));
        System.out.println(passwordEncoder.encode("secr3tKey"));
        System.out.println(passwordEncoder.encode("hash3dKey"));

    }

}

