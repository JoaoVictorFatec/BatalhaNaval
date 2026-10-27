import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class TelaInicialNaval extends JFrame {

    JLabel lblTitulo = new JLabel("BATALHA NAVAL", SwingConstants.CENTER);
    JLabel lblSubtitulo = new JLabel("Destrua a frota inimiga com o menor número de disparos!", SwingConstants.CENTER);
    JLabel lblDificuldade = new JLabel("Selecione a Dificuldade:");
    String[] opcoes = { "Nível Fácil (Matriz 6x6 - 30 Tiros)", "Nível Difícil (Matriz 8x8 - 40 Tiros)" };
    JComboBox cboDificuldade = new JComboBox(opcoes);
    JButton btnIniciar = new JButton("Iniciar Jogo");
    JButton btnSair = new JButton("Sair");
    JButton btnTutorial = new JButton("Como Jogar");

    public TelaInicialNaval() {

        setTitle("Batalha Naval - Menu Principal");
        
        setSize(800, 520);
        setMinimumSize(new Dimension(800, 520));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(20, 30, 50));

        setLayout(new GridBagLayout()); 

        JPanel painelMenu = new JPanel();
        painelMenu.setPreferredSize(new Dimension(768, 480));
        painelMenu.setBackground(new Color(20, 30, 50));
        painelMenu.setLayout(null);

        lblTitulo.setBounds(84, 40, 600, 40);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 32));
        lblTitulo.setForeground(new Color(50, 180, 255));

        lblSubtitulo.setBounds(84, 90, 600, 25);
        lblSubtitulo.setFont(new Font("Arial", Font.PLAIN, 15));
        lblSubtitulo.setForeground(Color.WHITE);

        lblDificuldade.setBounds(184, 160, 400, 25);
        lblDificuldade.setFont(new Font("Arial", Font.BOLD, 14));
        lblDificuldade.setForeground(Color.WHITE);

        cboDificuldade.setBounds(184, 190, 400, 35);
        cboDificuldade.setFont(new Font("Arial", Font.PLAIN, 14));

        btnSair.setBounds(94, 320, 160, 45);
        btnSair.setFont(new Font("Arial", Font.BOLD, 15));
        btnSair.setBackground(new Color(200, 50, 50));
        btnSair.setForeground(Color.WHITE);

        btnIniciar.setBounds(284, 320, 160, 45);
        btnIniciar.setFont(new Font("Arial", Font.BOLD, 15));
        btnIniciar.setBackground(new Color(40, 160, 90));
        btnIniciar.setForeground(Color.WHITE);

        btnTutorial.setBounds(474, 320, 160, 45);
        btnTutorial.setFont(new Font("Arial", Font.BOLD, 15));
        btnTutorial.setBackground(new Color(220, 150, 30));
        btnTutorial.setForeground(Color.WHITE);

        painelMenu.add(lblTitulo);
        painelMenu.add(lblSubtitulo);
        painelMenu.add(lblDificuldade);
        painelMenu.add(cboDificuldade);
        painelMenu.add(btnSair);
        painelMenu.add(btnIniciar);
        painelMenu.add(btnTutorial);

        add(painelMenu);

        btnIniciar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int opcao = cboDificuldade.getSelectedIndex();
                setVisible(false);
                TelaJogoNaval jogo = new TelaJogoNaval(opcao);
                jogo.setVisible(true);
                if (opcao == 0) {
                    JOptionPane.showMessageDialog(null,
                            "Nível Fácil Selecionado!\n" +
                                    "Tabuleiro: 6x6\n" +
                                    "Tiros Disponíveis: 30",
                            "Batalha Naval",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null,
                            "Nível Difícil Selecionado!\n" +
                                    "Tabuleiro: 8x8\n" +
                                    "Tiros Disponíveis: 40",
                            "Batalha Naval",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        btnTutorial.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String mensagemTutorial = "=================== REGRAS DO JOGO ===================\n\n" +
                        "OBJETIVO:\n" +
                        "Encontrar e afundar todas as embarcações com o menor número de tiros.\n\n" +
                        "DIFICULDADES:\n" +
                        "-> Nível Fácil: Matriz 6x6 | 30 Tiros\n" +
                        " - Frota: 4 Submarinos, 2 Destroiers, 1 Cruzador, 1 Encouraçado e 1 Porta-Aviões.\n\n" +
                        "-> Nível Difícil: Matriz 8x8 | 40 Tiros\n" +
                        " - Frota: 6 Submarinos, 2 Destroiers, 2 Cruzadores, 2 Encouraçados e 1 Porta-Aviões.\n\n" +
                        "TAMANHO DAS EMBARCAÇÕES:\n" +
                        "- Submarino: 1 casa\n" +
                        "- Destroier: 2 casas\n" +
                        "- Cruzador: 3 casas\n" +
                        "- Encouraçado: 4 casas\n" +
                        "- Porta-Aviões: 5 casas\n\n" +
                        "COMO GANHAR OU PERDER:\n" +
                        "- Você vence ao acertar todas as partes de todas as embarcações.\n" +
                        "- Você perde se esgotar o limite de tiros antes de destruir toda a frota.\n";

                JOptionPane.showMessageDialog(null, mensagemTutorial, "Como Jogar - Tutorial",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });

        btnSair.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Encerrando o jogo...");
                System.exit(0);
            }
        });
    }

    public static void main(String[] args) {
        TelaInicialNaval tela = new TelaInicialNaval();
        tela.setVisible(true);
    }
}