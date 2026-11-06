package com.cefet.servidor;

import org.java_websocket.WebSocket;

public class Cliente {
    String nick;
    WebSocket conexao;

    Cliente(String nick, WebSocket conexao) {
        this.nick = nick;
        this.conexao = conexao;
    }
}
