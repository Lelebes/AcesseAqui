package service;

import java.io.*;
import java.net.Socket;

public class Cliente {

    public static boolean enviarMensagem(String endereco, int porta, String mensagem) {
        try (Socket socket = new Socket(endereco, porta);
             PrintWriter escritor = new PrintWriter(socket.getOutputStream(), true)) {
            escritor.println(mensagem);
            System.out.println("Mensagem enviada com sucesso!");
            return true;
        } catch (IOException e) {
            System.err.println("Erro ao enviar mensagem: " + e.getMessage());
            return false;
        }
    }
}