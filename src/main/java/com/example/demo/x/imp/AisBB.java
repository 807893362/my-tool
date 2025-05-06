package com.example.demo.x.imp;

import com.example.demo.x.AbstctMthreod;
import com.example.demo.x.model.AB;
import org.springframework.stereotype.Service;

@Service
public class AisBB extends AbstctMthreod {
    @Override
    public AB A() {
        System.err.println("BB");
        return new AB("BB");
    }
}
