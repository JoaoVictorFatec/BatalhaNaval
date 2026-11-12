import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.Random;
import javax.swing.*;

public class TelaJogoNaval extends JFrame {

    int tamanhoMatriz;
    int tirosRestantes;
    int partesNaviosRestantes = 0;

    int[][] matrizLogica;
    boolean[][] tirosRealizados;
    int[][] orientacaoNavio; // 0 = Horizontal, 1 = Vertical
    int[][] indiceParteNavio;

    JButton[][] botoesMatriz;

    JLabel lblStatus = new JLabel("", SwingConstants.CENTER);
    JPanel painelTabuleiro = new JPanel();

    Random random = new Random();

    ImageIcon imagemAgua;
    ImageIcon imagemSubmarino;
    ImageIcon imagemDestroier;
    ImageIcon imagemCruzador;
    ImageIcon imagemEncouracado;
    ImageIcon imagemPortaAvioes;

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
        tirosRealizados = new boolean[tamanhoMatriz][tamanhoMatriz];
        orientacaoNavio = new int[tamanhoMatriz][tamanhoMatriz];
        indiceParteNavio = new int[tamanhoMatriz][tamanhoMatriz];
        botoesMatriz = new JButton[tamanhoMatriz][tamanhoMatriz];

        carregarImagens();

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
        painelTabuleiro.setLayout(
                new GridLayout(tamanhoMatriz, tamanhoMatriz, 2, 2)
        );
        painelTabuleiro.setBackground(new Color(20, 30, 50));

        if (modoDificuldade == 0) {
            alocarFrotaFacil();
        } else {
            alocarFrotaDificil();
        }

        lblStatus.setText(
                "Tiros Restantes: " + tirosRestantes +
                " | Navios Restantes: " + partesNaviosRestantes + " Partes"
        );

        for (int i = 0; i < tamanhoMatriz; i++) {
            for (int j = 0; j < tamanhoMatriz; j++) {

                JButton btn = new JButton();

                btn.setFocusPainted(false);
                btn.setBorderPainted(false);
                btn.setContentAreaFilled(false);
                btn.setOpaque(true);

                btn.setIcon(
                        redimensionarImagem(
                                imagemAgua,
                                calcularTamanhoCelula(),
                                calcularTamanhoCelula()
                        )
                );

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

    void carregarImagens() {

        imagemAgua = carregarImagem("agua.jpg");
        imagemSubmarino = carregarImagem("submarino.jpg");
        imagemDestroier = carregarImagem("destroier.jpg");
        imagemCruzador = carregarImagem("cruzador.jpg");
        imagemEncouracado = carregarImagem("encouracado.jpg");
        imagemPortaAvioes = carregarImagem("portavioes.jpg");

        if (imagemAgua == null ||
            imagemSubmarino == null ||
            imagemDestroier == null ||
            imagemCruzador == null ||
            imagemEncouracado == null ||
            imagemPortaAvioes == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Não foi possível carregar uma ou mais imagens.\n\n" +
                    "Diretório de execução:\n" +
                    new File(".").getAbsolutePath(),
                    "Erro ao carregar imagens",
                    JOptionPane.ERROR_MESSAGE
            );

            System.exit(1);
        }
    }

    ImageIcon carregarImagem(String nomeArquivo) {
        try {
            URL url = getClass().getResource(nomeArquivo);
            if (url != null) {
                return new ImageIcon(url);
            }
        } catch (Exception e) {
            // Ignora
        }

        File arquivo = new File(nomeArquivo);
        if (arquivo.exists() && arquivo.isFile()) {
            return new ImageIcon(arquivo.getAbsolutePath());
        }

        File arquivoSub = new File("projetoNavio" + File.separator + nomeArquivo);
        if (arquivoSub.exists() && arquivoSub.isFile()) {
            return new ImageIcon(arquivoSub.getAbsolutePath());
        }

        return null;
    }

    int calcularTamanhoCelula() {
        if (tamanhoMatriz == 6) {
            return 64;
        } else {
            return 48;
        }
    }

    ImageIcon redimensionarImagem(
            ImageIcon imagem,
            int largura,
            int altura
    ) {
        if (imagem == null) {
            return null;
        }

        Image imagemOriginal = imagem.getImage();
        Image imagemRedimensionada = imagemOriginal.getScaledInstance(
                largura,
                altura,
                Image.SCALE_SMOOTH
        );

        return new ImageIcon(imagemRedimensionada);
    }

    // Cria a textura da água com um efeito visual de respingo/impacto (bomba) desenhado em cima
    ImageIcon criarImagemBombaNaAgua(int largura, int altura) {
        ImageIcon iconOriginal = imagemAgua;
        if (iconOriginal == null) return null;

        Image imgOriginal = iconOriginal.getImage();
        BufferedImage bImg = new BufferedImage(largura, altura, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bImg.createGraphics();
        
        // Desenha a textura da água redimensionada para a célula
        g2d.drawImage(imgOriginal, 0, 0, largura, altura, null);

        // Desenha o efeito de impacto (anéis concêntricos de respingo combinados com a paleta de cores da água)
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int cx = largura / 2;
        int cy = altura / 2;

        // Anel externo translúcido simulando onda de choque do tiro
        g2d.setColor(new Color(255, 255, 255, 120));
        g2d.setStroke(new BasicStroke(2.5f));
        g2d.drawOval(cx - 14, cy - 14, 28, 28);

        // Anel interno menor
        g2d.setColor(new Color(180, 230, 255, 180));
        g2d.drawOval(cx - 7, cy - 7, 14, 14);

        // Ponto central do impacto (respingo d'água)
        g2d.setColor(new Color(255, 255, 255, 220));
        g2d.fillOval(cx - 3, cy - 3, 6, 6);

        g2d.dispose();
        return new ImageIcon(bImg);
    }

    ImageIcon obterFatiaNavio(int codigo, int parteIndex, int tamanhoTotal, int orientacao, int larguraDestino, int alturaDestino) {
        ImageIcon iconOriginal = obterImagemNavio(codigo);
        if (iconOriginal == null) return null;

        Image imgOriginal = iconOriginal.getImage();
        int largOriginal = imgOriginal.getWidth(null);
        int altOriginal = imgOriginal.getHeight(null);

        if (largOriginal <= 0 || altOriginal <= 0) {
            return redimensionarImagem(iconOriginal, larguraDestino, alturaDestino);
        }

        BufferedImage bImg = new BufferedImage(largOriginal, altOriginal, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bImg.createGraphics();
        g2d.drawImage(imgOriginal, 0, 0, null);
        g2d.dispose();

        int larguraParte = largOriginal / tamanhoTotal;
        int x = parteIndex * larguraParte;
        if (parteIndex == tamanhoTotal - 1) {
            larguraParte = largOriginal - x;
        }
        BufferedImage subImg = bImg.getSubimage(x, 0, larguraParte, altOriginal);

        if (orientacao == 1) {
            int subW = subImg.getWidth();
            int subH = subImg.getHeight();
            BufferedImage rotacionada = new BufferedImage(subH, subW, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2rot = rotacionada.createGraphics();
            AffineTransform at = new AffineTransform();
            at.translate(subH / 2.0, subW / 2.0);
            at.rotate(Math.toRadians(90));
            at.translate(-subW / 2.0, -subH / 2.0);
            g2rot.drawImage(subImg, at, null);
            g2rot.dispose();
            subImg = rotacionada;
        }

        Image imgRedimensionada = subImg.getScaledInstance(larguraDestino, alturaDestino, Image.SCALE_SMOOTH);
        return new ImageIcon(imgRedimensionada);
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
        boolean posicionado = false;
        int tentativas = 0;

        while (!posicionado && tentativas < 10000) {
            tentativas++;

            int orientacao = random.nextInt(2);
            int linha;
            int coluna;

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
                        orientacaoNavio[linha][coluna + k] = 0;
                        indiceParteNavio[linha][coluna + k] = k;
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
                        orientacaoNavio[linha + k][coluna] = 1;
                        indiceParteNavio[linha + k][coluna] = k;
                    }
                    posicionado = true;
                    partesNaviosRestantes += tamanho;
                }
            }
        }

        if (!posicionado) {
            JOptionPane.showMessageDialog(
                    this,
                    "Não foi possível posicionar toda a frota."
            );
            System.exit(1);
        }
    }

    void efetuarTiro(int linha, int coluna, JButton btn) {
        if (tirosRealizados[linha][coluna]) {
            return;
        }

        tirosRealizados[linha][coluna] = true;
        tirosRestantes--;

        for (ActionListener al : btn.getActionListeners()) {
            btn.removeActionListener(al);
        }

        int valorPosicao = matrizLogica[linha][coluna];

        if (valorPosicao == 0) {
            // Aplica a textura da água com o respingo de bomba estilizado por cima
            btn.setIcon(
                    criarImagemBombaNaAgua(
                            calcularTamanhoCelula(),
                            calcularTamanhoCelula()
                    )
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Tiro na Água!",
                    "Resultado",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } else {
            partesNaviosRestantes--;

            int tamanhoNavio = obterTamanhoNavio(valorPosicao);
            int orient = orientacaoNavio[linha][coluna];
            int parteIdx = indiceParteNavio[linha][coluna];

            ImageIcon fatiaNavio = obterFatiaNavio(
                    valorPosicao,
                    parteIdx,
                    tamanhoNavio,
                    orient,
                    calcularTamanhoCelula(),
                    calcularTamanhoCelula()
            );

            btn.setIcon(fatiaNavio);

            String nomeNavio = obterNomeNavio(valorPosicao);

            JOptionPane.showMessageDialog(
                    this,
                    "Você acertou um " + nomeNavio + "!",
                    "Resultado",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }

        lblStatus.setText(
                "Tiros Restantes: " + tirosRestantes +
                " | Navios Restantes: " + partesNaviosRestantes + " Partes"
        );

        if (partesNaviosRestantes == 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "PARABÉNS! Você destruiu toda a frota inimiga!",
                    "Vitória",
                    JOptionPane.INFORMATION_MESSAGE
            );

            encerrarPartida();

        } else if (tirosRestantes == 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "GAME OVER! Seus tiros acabaram.",
                    "Derrota",
                    JOptionPane.ERROR_MESSAGE
            );

            encerrarPartida();
        }
    }

    int obterTamanhoNavio(int codigo) {
        if (codigo == 1) return 1;
        if (codigo == 2) return 2;
        if (codigo == 3) return 3;
        if (codigo == 4) return 4;
        if (codigo == 5) return 5;
        return 1;
    }

    String obterNomeNavio(int codigo) {
        if (codigo == 1) {
            return "Submarino";
        } else if (codigo == 2) {
            return "Destroier";
        } else if (codigo == 3) {
            return "Cruzador";
        } else if (codigo == 4) {
            return "Encouraçado";
        } else if (codigo == 5) {
            return "Porta-Aviões";
        }
        return "Navio";
    }

    ImageIcon obterImagemNavio(int codigo) {
        if (codigo == 1) {
            return imagemSubmarino;
        } else if (codigo == 2) {
            return imagemDestroier;
        } else if (codigo == 3) {
            return imagemCruzador;
        } else if (codigo == 4) {
            return imagemEncouracado;
        } else if (codigo == 5) {
            return imagemPortaAvioes;
        }
        return imagemAgua;
    }

    void encerrarPartida() {
        dispose();

        TelaInicialNaval menu = new TelaInicialNaval();
        menu.setVisible(true);
    }
}