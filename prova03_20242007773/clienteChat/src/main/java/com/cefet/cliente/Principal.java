package com.cefet.cliente;

import java.net.URI;
import java.util.Scanner;
import org.java_websocket.exceptions.WebsocketNotConnectedException;

public class Principal {
    public static void main(String[] args) {
        ClienteChat cliente = new ClienteChat(URI.create("ws://localhost:8080"));

        try (Scanner teclado = new Scanner(System.in)) {
            System.out.print("Informe seu nick: ");
            if (!teclado.hasNextLine()) {
                return;
            }

            String nick = teclado.nextLine();

            if (!cliente.connectBlocking()) {
                System.out.println("Não foi possível conectar ao servidor.");
                return;
            }

            cliente.send(nick);     
            
            System.out.println("Digite mensagens ou sair para encerrar.");

            while (cliente.isOpen() && teclado.hasNextLine()) {
                String mensagem = teclado.nextLine();
                if (mensagem.equalsIgnoreCase("sair")) {
                    break;
                }
                cliente.send(mensagem);
            }
        } catch (WebsocketNotConnectedException erro) {
            System.out.println("A conexão foi encerrada antes do envio.");
        } catch (InterruptedException erro) {
            Thread.currentThread().interrupt();
            System.out.println("A conexão foi interrompida.");
        } finally {
            cliente.close();
        }
    }
}
