package com.acesseaqui.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;

import com.acesseaqui.model.Notificacao;

public class NotificacaoListCellRenderer extends JPanel implements ListCellRenderer<Notificacao> {

    private final JLabel descricaoLabel;
    private final JLabel localizacaoLabel;
    private final JLabel usuarioLabel;
    private final JLabel dataLabel;
    private final JLabel imagemLabel;

    private final Color COLOR_PRIMARY = new Color(41, 128, 185);
    private final Color COLOR_TEXT = new Color(44, 62, 80);
    private final Color COLOR_SUBTEXT = new Color(127, 140, 141);

    public NotificacaoListCellRenderer() {
        setLayout(new BorderLayout(15, 10));
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(8, 10, 8, 10),
                BorderFactory.createLineBorder(new Color(230, 230, 230), 1)
        ));
        setBackground(Color.WHITE);

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(Color.WHITE);
        textPanel.setOpaque(false);

        descricaoLabel = new JLabel();
        descricaoLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        descricaoLabel.setForeground(COLOR_TEXT);

        localizacaoLabel = new JLabel();
        localizacaoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        localizacaoLabel.setForeground(COLOR_PRIMARY);

        usuarioLabel = new JLabel();
        usuarioLabel.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        usuarioLabel.setForeground(COLOR_SUBTEXT);

        dataLabel = new JLabel();
        dataLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        dataLabel.setForeground(COLOR_SUBTEXT);

        textPanel.add(descricaoLabel);
        textPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        textPanel.add(localizacaoLabel);
        textPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        textPanel.add(usuarioLabel);
        textPanel.add(Box.createRigidArea(new Dimension(0, 2)));
        textPanel.add(dataLabel);

        imagemLabel = new JLabel();
        imagemLabel.setPreferredSize(new Dimension(100, 100));
        imagemLabel.setBorder(BorderFactory.createLineBorder(new Color(240, 240, 240)));
        imagemLabel.setHorizontalAlignment(SwingConstants.CENTER);

        add(imagemLabel, BorderLayout.WEST);
        add(textPanel, BorderLayout.CENTER);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends Notificacao> list, Notificacao notificacao,
                                                  int index, boolean isSelected, boolean cellHasFocus) {

        descricaoLabel.setText(notificacao.getDescricao());
        localizacaoLabel.setText("📍 " + notificacao.getLocalizacao());
        usuarioLabel.setText("👤 Relatado por: " + notificacao.getUsuario().getNome() + " (" + notificacao.getUsuario().getIdade() + " anos)");

        String dataStr = notificacao.getDataHora().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        dataLabel.setText("🕒 " + dataStr);

        if (!notificacao.getImagemCaminho().equals("Sem imagem")) {
            File imgFile = new File(notificacao.getImagemCaminho());
            if (imgFile.exists()) {
                ImageIcon originalIcon = new ImageIcon(imgFile.getAbsolutePath());
                Image scaledImage = originalIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                imagemLabel.setIcon(new ImageIcon(scaledImage));
                imagemLabel.setText("");
            } else {
                imagemLabel.setIcon(null);
                imagemLabel.setText("📷 N/A");
            }
        } else {
            imagemLabel.setIcon(null);
            imagemLabel.setText("📷 Sem foto");
        }

        if (isSelected) {
            setBackground(new Color(235, 245, 251));
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createEmptyBorder(8, 10, 8, 10),
                    BorderFactory.createLineBorder(COLOR_PRIMARY, 2)
            ));
        } else {
            setBackground(Color.WHITE);
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createEmptyBorder(8, 10, 8, 10),
                    BorderFactory.createLineBorder(new Color(240, 240, 240), 1)
            ));
        }

        return this;
    }
}