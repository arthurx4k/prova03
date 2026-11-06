
package com.cefet.servidor;

import java.net.InetSocketAddress;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.util.ArrayList;

public class ServidorChat extends WebSocketServer {

    ArrayList<Cliente> clientes = new ArrayList<>();

    public ServidorChat(int porta) {
        super(new InetSocketAddress(porta));
    }

    @Override
    public void onOpen(WebSocket conexao, ClientHandshake requisicao) {
        System.out.println("Cliente conectado.");
    }

    @Override
    public void onMessage(WebSocket conexao, String mensagem) {

        Cliente remetente = null;

        for (Cliente cliente : clientes) {
            if (cliente.conexao == conexao) {
                remetente = cliente;
                break;
            }
        }

        if (remetente == null) {

            String nick = mensagem.trim();
            if (nick.isEmpty() || nick.contains(" ") || nick.startsWith("@")) {
                conexao.send("ERRO: Nick inválido.");
                conexao.close();
                return;
            }

            for (Cliente cliente : clientes) {
                if (cliente.nick.equalsIgnoreCase(nick)) {
                    conexao.send("ERRO: Nick já utilizado.");
                    conexao.close();
                    return;
                }
            }

            clientes.add(new Cliente(nick, conexao));
            
            for (Cliente cliente : clientes) {
                if (cliente.conexao != conexao) {
                    cliente.conexao.send(nick + " entrou na sala.");
                }
            }

            return;
        }

        if (mensagem.startsWith("@")) {

            int posicao = mensagem.indexOf(" ");

            if (posicao == -1) {
                conexao.send("Formato: @destinatario mensagem");
                return;
            }

            String destinatario = mensagem.substring(1, posicao);
            String texto = mensagem.substring(posicao + 1);

            for (Cliente cliente : clientes) {
                if (cliente.nick.equalsIgnoreCase(destinatario)) {
                    cliente.conexao.send("[Privada] " + remetente.nick + ": " + texto);
                    return;
                }
            }

            conexao.send("Destinatário não conectado.");
        } else {
            broadcast(remetente.nick + ": " + mensagem);
        }
    }

    @Override
    public void onClose(WebSocket conexao, int codigo, String motivo, boolean remoto) {
        for (int i = 0; i < clientes.size(); i++) {
            if (clientes.get(i).conexao == conexao) {
                String nick = clientes.get(i).nick;
                clientes.remove(i);
                broadcast(nick + " saiu da sala.");
            }
        }

        System.out.println("Cliente desconectado.");
    }

    @Override
    public void onError(WebSocket conexao, Exception erro) {
        System.out.println("Erro no servidor: " + erro.getMessage());
    }

    @Override
    public void onStart() {
        System.out.println("Servidor iniciado na porta " + getPort() + ".");
    }
}
