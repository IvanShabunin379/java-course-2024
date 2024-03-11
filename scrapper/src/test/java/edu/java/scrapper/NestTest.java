package edu.java.scrapper;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class NestTest {
    static String testString;
    @BeforeAll
    public static void setUpString(){
        testString = "string";
    }

    @Test
    public void test(){
        System.out.println(testString);
    }
}

