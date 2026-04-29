package view;

import model.Notificacao;
import model.Usuario;
import service.Cliente;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.*;
import java.util.ArrayList;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;

public class AppInterface {

    private final ArrayList<Notificacao> notificacoes = new ArrayList<>();
    private JFrame frame;
    private JTabbedPane tabbedPane;
    private JList<Notificacao> notificacoesList;
    private DefaultListModel<Notificacao> notificacoesListModel;
    private Notificacao notificacaoSendoEditada = null;
    private JTextField campoBusca;

    // Campos do formulário para acesso global
    private JTextField campoNome;
    private JTextField campoEmail;
    private JTextField campoDescricao;
    private JTextField campoLocalizacao;
    private JComboBox<String> comboGenero;
    private JSpinner spinnerIdade;
    private JLabel imagemLabel;
    private final File[] imagemSelecionada = {null};

    private final Color COLOR_PRIMARY = new Color(25, 42, 86); // Azul Marinho Profundo (Melhor contraste)
    private final Color COLOR_ACCENT = new Color(44, 58, 71);  // Cinza Profundo
    private final Color COLOR_BG = new Color(245, 247, 250);

    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
            // Ajustes globais para garantir contraste no Nimbus
            UIManager.put("Button.textForeground", Color.WHITE);
            UIManager.put("nimbusBase", new Color(25, 42, 86));
            UIManager.put("nimbusBlueGrey", new Color(25, 42, 86));
            UIManager.put("control", new Color(245, 247, 250));
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(AppInterface::new);
    }


    private final String ARQUIVO_DADOS = definirCaminhoArquivo();

    private String definirCaminhoArquivo() {
        File noSrc = new File("src/notificacoes.dat");
        if (new File("src").exists()) {
            return "src/notificacoes.dat";
        }
        return "notificacoes.dat";
    }

    public AppInterface() {
        carregarDados();
        frame = new JFrame("AcesseAqui - Relatórios de Acessibilidade");
        frame.setSize(1000, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));

        // Aba de Formulário
        JPanel panelForm = criarPainelFormulario();
        tabbedPane.addTab("Novo Relato", new ImageIcon(), panelForm, "Criar uma nova notificação");

        // Aba de Lista
        JPanel panelList = criarPainelLista();
        tabbedPane.addTab("Relatórios Registrados", new ImageIcon(), panelList, "Ver notificações existentes");

        frame.add(tabbedPane);
        frame.setVisible(true);
    }

    private JPanel criarPainelFormulario() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(COLOR_BG);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        contentPanel.setBackground(COLOR_BG);

        JLabel titleLabel = new JLabel("Formulário de Acessibilidade");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(COLOR_PRIMARY);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        contentPanel.add(titleLabel);

        JPanel formPanel = new JPanel(new GridLayout(6, 2, 15, 15));
        formPanel.setBackground(COLOR_BG);
        formPanel.setMaximumSize(new Dimension(800, 300));

        campoNome = criarCampoTexto("Digite seu nome completo");
        campoNome.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (Character.isDigit(c)) e.consume();
            }
        });

        campoEmail = criarCampoTexto("exemplo@gmail.com");
        campoEmail.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                validarEmail();
            }
        });

        campoDescricao = criarCampoTexto("Descreva o problema em detalhes");
        campoLocalizacao = criarCampoTexto("Rua, Número, Bairro, Cidade");

        String[] generos = {"Masculino", "Feminino", "Outros"};
        comboGenero = new JComboBox<>(generos);
        comboGenero.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        spinnerIdade = new JSpinner(new SpinnerNumberModel(18, 1, 120, 1));
        spinnerIdade.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        adicionarAoForm(formPanel, "Nome Completo:", campoNome);
        adicionarAoForm(formPanel, "E-mail de Contato:", campoEmail);
        adicionarAoForm(formPanel, "Descrição do Problema:", campoDescricao);
        adicionarAoForm(formPanel, "Localização:", campoLocalizacao);
        adicionarAoForm(formPanel, "Gênero:", comboGenero);
        adicionarAoForm(formPanel, "Idade:", spinnerIdade);

        contentPanel.add(formPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Seção de Imagem
        JPanel imageSection = new JPanel(new BorderLayout(10, 10));
        imageSection.setBackground(COLOR_BG);
        imageSection.setMaximumSize(new Dimension(800, 220));

        imagemLabel = new JLabel("Nenhuma imagem selecionada", SwingConstants.CENTER);
        imagemLabel.setPreferredSize(new Dimension(300, 180));
        imagemLabel.setBorder(BorderFactory.createDashedBorder(Color.GRAY, 2, 5, 2, false));

        JButton uploadButton = new JButton("Anexar Foto");
        estilizarBotao(uploadButton, COLOR_ACCENT);
        uploadButton.addActionListener(e -> selecionarImagem());

        imageSection.add(uploadButton, BorderLayout.NORTH);
        imageSection.add(imagemLabel, BorderLayout.CENTER);
        contentPanel.add(imageSection);

        // Botões de Ação
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel.setBackground(COLOR_BG);

        JButton enviarButton = new JButton("Enviar Relatório");
        estilizarBotao(enviarButton, COLOR_PRIMARY);
        enviarButton.setPreferredSize(new Dimension(200, 45));
        enviarButton.addActionListener(e -> acaoEnviar());

        JButton limparButton = new JButton("Limpar Campos");
        estilizarBotao(limparButton, Color.GRAY);
        limparButton.addActionListener(e -> limparFormulario());

        buttonPanel.add(limparButton);
        buttonPanel.add(enviarButton);
        contentPanel.add(buttonPanel);

        mainPanel.add(new JScrollPane(contentPanel), BorderLayout.CENTER);
        return mainPanel;
    }

    private JPanel criarPainelLista() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(COLOR_BG);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Busca
        JPanel buscaPanel = new JPanel(new BorderLayout(10, 10));
        buscaPanel.setBackground(COLOR_BG);
        buscaPanel.setBorder(BorderFactory.createTitledBorder("Filtrar Ocorrências"));

        campoBusca = new JTextField();
        campoBusca.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        campoBusca.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                atualizarListaNotificacoes(campoBusca.getText().trim());
            }
        });

        buscaPanel.add(new JLabel(" Pesquisar: "), BorderLayout.WEST);
        buscaPanel.add(campoBusca, BorderLayout.CENTER);
        panel.add(buscaPanel, BorderLayout.NORTH);

        // Lista
        notificacoesListModel = new DefaultListModel<>();
        notificacoesList = new JList<>(notificacoesListModel);
        notificacoesList.setCellRenderer(new NotificacaoListCellRenderer());
        notificacoesList.setBackground(COLOR_BG);
        notificacoesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        notificacoesList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                lidarCliqueLista(e);
            }
        });

        JScrollPane scroll = new JScrollPane(notificacoesList);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    private JTextField criarCampoTexto(String placeholder) {
        JTextField campo = new JTextField(20);
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        campo.setToolTipText(placeholder);
        return campo;
    }

    private void adicionarAoForm(JPanel panel, String label, JComponent comp) {
        JLabel jl = new JLabel(label);
        jl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panel.add(jl);
        panel.add(comp);
    }

    private void estilizarBotao(JButton btn, Color cor) {
        btn.setBackground(cor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Forçar renderização de texto branco puro no Nimbus
        btn.putClientProperty("Nimbus.Overrides", null);
        btn.putClientProperty("Nimbus.Overrides.InheritDefaults", false);

        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(cor.darker(), 2),
                BorderFactory.createEmptyBorder(10, 25, 10, 25)
        ));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(cor.brighter());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(cor);
            }
        });
    }

    private void selecionarImagem() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Imagens (JPG, PNG)", "jpg", "png", "jpeg"));
        if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
            imagemSelecionada[0] = fileChooser.getSelectedFile();
            ImageIcon icon = new ImageIcon(new ImageIcon(imagemSelecionada[0].getAbsolutePath())
                    .getImage().getScaledInstance(300, 180, Image.SCALE_SMOOTH));
            imagemLabel.setIcon(icon);
            imagemLabel.setText("");
            imagemLabel.setBorder(BorderFactory.createLineBorder(COLOR_ACCENT, 2));
        }
    }

    private void validarEmail() {
        String email = campoEmail.getText().trim();
        if (!email.isEmpty() && !email.matches(".*@(gmail|hotmail)\\.com")) {
            campoEmail.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
        } else {
            campoEmail.setBorder(UIManager.getLookAndFeel().getDefaults().getBorder("TextField.border"));
        }
    }

    private void acaoEnviar() {
        String nome = campoNome.getText().trim();
        String email = campoEmail.getText().trim();
        String descricao = campoDescricao.getText().trim();
        String localizacao = campoLocalizacao.getText().trim();
        String genero = (String) comboGenero.getSelectedItem();
        int idade = (Integer) spinnerIdade.getValue();

        nome = formatarNome(nome);
        StringBuilder erros = new StringBuilder();

        if (nome.length() < 3) erros.append("- Nome muito curto (mínimo 3 caracteres).\n");
        if (!email.matches(".*@(gmail|hotmail)\\.com")) erros.append("- E-mail deve ser @gmail.com ou @hotmail.com.\n");
        if (descricao.length() < 10) erros.append("- Descrição muito curta (mínimo 10 caracteres).\n");
        if (localizacao.isEmpty()) erros.append("- Localização é obrigatória.\n");

        if (erros.length() > 0) {
            JOptionPane.showMessageDialog(frame, erros.toString(), "Validação", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Usuario usuario = new Usuario(nome, email, genero, idade);
        Notificacao notificacao;

        if (notificacaoSendoEditada != null) {
            notificacao = notificacaoSendoEditada;
            notificacao.setDescricao(descricao);
            notificacao.setLocalizacao(localizacao);
            notificacao.setImagemCaminho(imagemSelecionada[0] != null ? imagemSelecionada[0].getAbsolutePath() : notificacao.getImagemCaminho());
            notificacao.setUsuario(usuario);
            JOptionPane.showMessageDialog(frame, "Relatório atualizado!");
            notificacaoSendoEditada = null;
        } else {
            notificacao = new Notificacao(descricao, localizacao,
                    imagemSelecionada[0] != null ? imagemSelecionada[0].getAbsolutePath() : "Sem imagem",
                    usuario, LocalDateTime.now());
            notificacoes.add(notificacao);
            JOptionPane.showMessageDialog(frame, "Relatório enviado com sucesso!");
        }

        salvarDados();
        Cliente.enviarMensagem("localhost", 12345, notificacao.toString());
        limparFormulario();
        atualizarListaNotificacoes();
    }

    private void lidarCliqueLista(MouseEvent e) {
        int index = notificacoesList.locationToIndex(e.getPoint());
        if (index == -1) return;

        Notificacao n = notificacoesListModel.getElementAt(index);

        if (SwingUtilities.isRightMouseButton(e)) {
            int op = JOptionPane.showConfirmDialog(frame, "Excluir este relatório?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (op == JOptionPane.YES_OPTION) {
                notificacoes.remove(n);
                salvarDados();
                atualizarListaNotificacoes();
            }
        } else if (e.getClickCount() == 2) {
            notificacaoSendoEditada = n;
            preencherFormularioParaEdicao(n);
            tabbedPane.setSelectedIndex(0);
        }
    }

    private void limparFormulario() {
        campoNome.setText("");
        campoEmail.setText("");
        campoDescricao.setText("");
        campoLocalizacao.setText("");
        spinnerIdade.setValue(18);
        imagemLabel.setIcon(null);
        imagemLabel.setText("Nenhuma imagem selecionada");
        imagemLabel.setBorder(BorderFactory.createDashedBorder(Color.GRAY, 2, 5, 2, false));
        imagemSelecionada[0] = null;
        notificacaoSendoEditada = null;
    }

    private void preencherFormularioParaEdicao(Notificacao n) {
        campoNome.setText(n.getUsuario().getNome());
        campoEmail.setText(n.getUsuario().getEmail());
        comboGenero.setSelectedItem(n.getUsuario().getGenero());
        spinnerIdade.setValue(n.getUsuario().getIdade());
        campoDescricao.setText(n.getDescricao());
        campoLocalizacao.setText(n.getLocalizacao());

        if (!n.getImagemCaminho().equals("Sem imagem")) {
            File imgFile = new File(n.getImagemCaminho());
            if (imgFile.exists()) {
                imagemSelecionada[0] = imgFile;
                ImageIcon icon = new ImageIcon(new ImageIcon(imgFile.getAbsolutePath())
                        .getImage().getScaledInstance(300, 180, Image.SCALE_SMOOTH));
                imagemLabel.setIcon(icon);
                imagemLabel.setText("");
            }
        }
    }

    private void atualizarListaNotificacoes() {
        atualizarListaNotificacoes(campoBusca != null ? campoBusca.getText().trim() : "");
    }

    private void atualizarListaNotificacoes(String filtro) {
        if (notificacoesListModel == null) return;
        notificacoesListModel.clear();
        for (Notificacao n : notificacoes) {
            if (filtro.isEmpty() ||
                n.getDescricao().toLowerCase().contains(filtro.toLowerCase()) ||
                n.getLocalizacao().toLowerCase().contains(filtro.toLowerCase()) ||
                n.getUsuario().getNome().toLowerCase().contains(filtro.toLowerCase())) {
                notificacoesListModel.addElement(n);
            }
        }
    }

    private void salvarDados() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARQUIVO_DADOS))) {
            oos.writeObject(notificacoes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private void carregarDados() {
        File arquivo = new File(ARQUIVO_DADOS);
        if (arquivo.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ARQUIVO_DADOS))) {
                ArrayList<Notificacao> dados = (ArrayList<Notificacao>) ois.readObject();
                notificacoes.clear();
                notificacoes.addAll(dados);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String formatarNome(String nome) {
        if (nome == null || nome.isEmpty()) return "";
        String[] partes = nome.toLowerCase().split("\\s+");
        StringBuilder sb = new StringBuilder();
        for (String parte : partes) {
            if (parte.length() > 0) {
                sb.append(Character.toUpperCase(parte.charAt(0))).append(parte.substring(1)).append(" ");
            }
        }
        return sb.toString().trim();
    }
}
