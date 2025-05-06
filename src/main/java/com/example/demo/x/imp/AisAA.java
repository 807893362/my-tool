package com.example.demo.x.imp;

import com.example.demo.x.AbstctMthreod;
import com.example.demo.x.model.AB;
import org.springframework.stereotype.Service;

@Service
public class AisAA extends AbstctMthreod {
    @Override
    public AB A() {
        System.err.println("AA");
        return new AB("AA");
    }
}
