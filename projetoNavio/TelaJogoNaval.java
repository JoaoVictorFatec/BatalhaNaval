import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.*;

public class TelaJogoNaval extends JFrame {

    int tamanhoMatriz;
    int tirosRestantes;
    int partesNaviosRestantes = 0;
    int[][] matrizLogica;
    JButton[][] botoesMatriz;

    JLabel lblStatus = new JLabel("", SwingConstants.CENTER);
    JPanel painelTabuleiro = new JPanel();

    public TelaJogoNaval(int modoDificuldade) {

        if (modoDificuldade == 0) {
            tamanhoMatriz = 6;
            tirosRestantes = 30;
            setTitle("Batalha Naval - Nível Fácil (6x6)");
        } else {
            tamanhoMatriz = 8;
            tirosRestantes = 40;
            setTitle("Batalha Naval - Nível Difícil (8x8)");
        }

        matrizLogica = new int[tamanhoMatriz][tamanhoMatriz];
        botoesMatriz = new JButton[tamanhoMatriz][tamanhoMatriz];

        setSize(800, 580);
        setMinimumSize(new Dimension(800, 580));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(20, 30, 50));

        setLayout(new GridBagLayout());

        JPanel painelJogo = new JPanel();
        painelJogo.setPreferredSize(new Dimension(768, 520));
        painelJogo.setBackground(new Color(20, 30, 50));
        painelJogo.setLayout(null);

        lblStatus.setBounds(84, 15, 600, 30);
        lblStatus.setFont(new Font("Arial", Font.BOLD, 18));
        lblStatus.setForeground(new Color(50, 180, 255));

        painelTabuleiro.setBounds(184, 60, 400, 400);
        painelTabuleiro.setLayout(new GridLayout(tamanhoMatriz, tamanhoMatriz, 2, 2));
        painelTabuleiro.setBackground(new Color(20, 30, 50));

        if (modoDificuldade == 0) {
            alocarFrotaFacil();
        } else {
            alocarFrotaDificil();
        }

        lblStatus.setText("Tiros Restantes: " + tirosRestantes + " | Navios Restantes: " + partesNaviosRestantes + " Partes");

        for (int i = 0; i < tamanhoMatriz; i++) {
            for (int j = 0; j < tamanhoMatriz; j++) {
                JButton btn = new JButton();
                btn.setFont(new Font("Arial", Font.BOLD, 11));
                btn.setBackground(new Color(40, 90, 150));
                btn.setForeground(Color.WHITE);

                int l = i;
                int c = j;

                btn.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        efetuarTiro(l, c, btn);
                    }
                });

                botoesMatriz[i][j] = btn;
                painelTabuleiro.add(btn);
            }
        }

        painelJogo.add(lblStatus);
        painelJogo.add(painelTabuleiro);

        add(painelJogo);
    }

    void alocarFrotaFacil() {
        alocarNavio(5, 5);
        alocarNavio(4, 4);
        alocarNavio(3, 3);
        alocarNavio(2, 2);
        alocarNavio(2, 2);
        alocarNavio(1, 1);
        alocarNavio(1, 1);
        alocarNavio(1, 1);
        alocarNavio(1, 1);
    }

    void alocarFrotaDificil() {
        alocarNavio(5, 5);
        alocarNavio(4, 4);
        alocarNavio(4, 4);
        alocarNavio(3, 3);
        alocarNavio(3, 3);
        alocarNavio(2, 2);
        alocarNavio(2, 2);
        alocarNavio(1, 1);
        alocarNavio(1, 1);
        alocarNavio(1, 1);
        alocarNavio(1, 1);
        alocarNavio(1, 1);
        alocarNavio(1, 1);
    }

    void alocarNavio(int tamanho, int codigo) {
        Random random = new Random();
        boolean posicionado = false;

        while (!posicionado) {
            int orientacao = random.nextInt(2);
            int linha, coluna;

            if (orientacao == 0) {
                linha = random.nextInt(tamanhoMatriz);
                coluna = random.nextInt(tamanhoMatriz - tamanho + 1);

                boolean livre = true;
                for (int k = 0; k < tamanho; k++) {
                    if (matrizLogica[linha][coluna + k] != 0) {
                        livre = false;
                        break;
                    }
                }
                if (livre) {
                    for (int k = 0; k < tamanho; k++) {
                        matrizLogica[linha][coluna + k] = codigo;
                    }
                    posicionado = true;
                    partesNaviosRestantes += tamanho;
                }
            } else {
                linha = random.nextInt(tamanhoMatriz - tamanho + 1);
                coluna = random.nextInt(tamanhoMatriz);

                boolean livre = true;
                for (int k = 0; k < tamanho; k++) {
                    if (matrizLogica[linha + k][coluna] != 0) {
                        livre = false;
                        break;
                    }
                }
                if (livre) {
                    for (int k = 0; k < tamanho; k++) {
                        matrizLogica[linha + k][coluna] = codigo;
                    }
                    posicionado = true;
                    partesNaviosRestantes += tamanho;
                }
            }
        }
    }

    void efetuarTiro(int linha, int coluna, JButton btn) {
        tirosRestantes--;
        btn.setEnabled(false);

        int valorPosicao = matrizLogica[linha][coluna];

        if (valorPosicao == 0) {
            btn.setBackground(new Color(100, 100, 100));
            btn.setText("ÁGUA");
            JOptionPane.showMessageDialog(this, "Tiro na Água!", "Resultado", JOptionPane.INFORMATION_MESSAGE);
        } else {
            partesNaviosRestantes--;
            btn.setBackground(new Color(200, 50, 50));

            String nomeNavio = "";
            if (valorPosicao == 1) nomeNavio = "Submarino";
            else if (valorPosicao == 2) nomeNavio = "Destroier";
            else if (valorPosicao == 3) nomeNavio = "Cruzador";
            else if (valorPosicao == 4) nomeNavio = "Encouraçado";
            else if (valorPosicao == 5) nomeNavio = "Porta-Aviões";

            btn.setText("FOGO");
            JOptionPane.showMessageDialog(this, "Você acertou um " + nomeNavio + "!", "Resultado", JOptionPane.INFORMATION_MESSAGE);
        }

        lblStatus.setText("Tiros Restantes: " + tirosRestantes + " | Navios Restantes: " + partesNaviosRestantes + " Partes");

        if (partesNaviosRestantes == 0) {
            JOptionPane.showMessageDialog(this, "PARABÉNS! Você destruiu toda a frota inimiga!", "Vitória", JOptionPane.INFORMATION_MESSAGE);
            encerrarPartida();
        } else if (tirosRestantes == 0) {
            JOptionPane.showMessageDialog(this, "GAME OVER! Seus tiros acabaram.", "Derrota", JOptionPane.ERROR_MESSAGE);
            encerrarPartida();
        }
    }

    void encerrarPartida() {
        dispose();
        TelaInicialNaval menu = new TelaInicialNaval();
        menu.setVisible(true);
    }
}