package com.cefet.cliente;

import java.net.URI;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

public class ClienteChat extends WebSocketClient {
    public ClienteChat(URI endereco) {
        super(endereco);
    }

    @Override
    public void onOpen(ServerHandshake resposta) {
        System.out.println("Conectado ao servidor!");        
    }

    @Override
    public void onMessage(String mensagem) {
        System.out.println(mensagem);
    }

    @Override
    public void onClose(int codigo, String motivo, boolean remoto) {
        System.out.println("Conexão encerrada.");
    }

    @Override
    public void onError(Exception erro) {
        System.out.println("Erro na comunicação: " + erro.getMessage());
    }
}
