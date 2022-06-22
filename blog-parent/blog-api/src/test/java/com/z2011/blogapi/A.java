package com.z2011.blogapi;

import lombok.Data;
import org.springframework.stereotype.Component;
@Data
@Component
public class A {
    private String name="abc";
    private B b=new B();
}