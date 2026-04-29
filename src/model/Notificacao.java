package model;

import java.time.LocalDateTime;

public class Notificacao implements java.io.Serializable {
    private String descricao;
    private String localizacao;
    private String imagemCaminho; // Caminho da imagem
    private Usuario usuario;
    private LocalDateTime dataHora;

    // Construtor
    public Notificacao(String descricao, String localizacao, String imagemCaminho, Usuario usuario, LocalDateTime dataHora) {
        this.descricao = descricao;
        this.localizacao = localizacao;
        this.imagemCaminho = imagemCaminho;
        this.usuario = usuario;
        this.dataHora = dataHora;
    }

    // Getters e Setters
    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    public String getImagemCaminho() {
        return imagemCaminho;
    }

    public void setImagemCaminho(String imagemCaminho) {
        this.imagemCaminho = imagemCaminho;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    // Metodo toString para exibir informações da notificação
    @Override
    public String toString() {
        return "Descrição: " + descricao +
                "\nLocalização: " + localizacao +
                "\nUsuário: " + usuario.toString() +
                "\nData/Hora: " + dataHora.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
    }
}
