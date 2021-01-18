package com.atgui;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author lijian
 * @create 2021-01-15 22:12
 */
public class TestSpringSecurity {
    @Test
    public void testSpringSecurity(){
        // $2a$10$dyIf5fOjCRZs/pYXiBYy8uOiTa1z7I.mpqWlK5B/0icpAKijKCgxe
        // $2a$10$OphM.agzJ55McriN2BzCFeoLZh/z8uL.8dcGx.VCnjLq01vav7qEm

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String s = encoder.encode("123");
        System.out.println(s);
        String s1 = encoder.encode("123");
        System.out.println(s1);

        // 进行判断
        boolean b = encoder.matches("123", "$2a$10$/FoRQCNf9QoyUhUkkupxO.wqLQQW.EFe8sT6KVFXr6TsH8BlFGIUG");
        System.out.println(b);
    }
}
