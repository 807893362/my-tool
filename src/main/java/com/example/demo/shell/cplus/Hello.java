package com.example.demo.shell.cplus;

public class Hello {

    public native void helloFromCPP();

    static{

        System.out.println(System.getProperty("user.dir"));

        System.load(System.getProperty("user.dir") + "\\hello.dll");

    }

}
