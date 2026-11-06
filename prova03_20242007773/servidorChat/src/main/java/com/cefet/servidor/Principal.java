package com.cefet.servidor;

public class Principal {
    public static void main(String[] args) {
        ServidorChat servidor = new ServidorChat(8080);
        servidor.start();
    }
}
