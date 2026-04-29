package com.acesseaqui.model;

public class Usuario extends Pessoa{
    private String genero;

    public Usuario(String nome, String email, String genero, int idade) {
        super(nome, email, idade);
        this.genero = genero;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    @Override
    public String toString() {
        return super.toString() + "\nGênero: " + genero;
    }
}