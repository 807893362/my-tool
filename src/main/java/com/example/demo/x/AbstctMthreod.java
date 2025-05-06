package com.example.demo.x;

import com.example.demo.x.model.AB;
import org.springframework.stereotype.Service;

public abstract class AbstctMthreod implements IAbstct{


    public abstract AB A();


    public void AB(){
        System.err.println("AB");
    }


    @Override
    public void doIt() {
        A();
    }

}
