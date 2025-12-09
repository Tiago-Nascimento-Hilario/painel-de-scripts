import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        // Janela Principal
        JFrame frame = new JFrame("Painel de scripts e instalação");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        // Painel principal com layout de grades
        JPanel panel = new JPanel(new GridLayout(0, 3, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        String pastaDeScrips = "C:/scripts";

        File diretorio = new File(pastaDeScrips);

        if (diretorio.exists() && diretorio.isDirectory()) {
            File[] arquivos = diretorio.listFiles((dir, nome) -> nome.toLowerCase().endsWith(".bat"));

            if (arquivos != null && arquivos.length > 0) {
                for (File arquivo : arquivos) {
                    criaBotaoScript(panel, arquivo);
                }
            } else {
                JLabel label = new JLabel("Nenhum script .bat encontrado!");
                label.setHorizontalAlignment(SwingConstants.CENTER);
                panel.add(label);
            }
        } else {
            JLabel label = new JLabel("Pasta não encontrada: " + pastaDeScrips);
            label.setHorizontalAlignment(SwingConstants.CENTER);
            panel.add(label);
        }

        // Adiciona botão para abrir a pasta
        JButton btnAbrirPasta = new JButton("Abrir pasta dos Scripts");
        btnAbrirPasta.addActionListener(e -> abrirPasta(pastaDeScrips));
        panel.add(btnAbrirPasta);

        // Adiciona botão para atualizar lista
        JButton btnAtualizar = new JButton("Atualizar Lista");
        btnAtualizar.addActionListener(e -> {
            panel.removeAll();
            recarregarScripts(panel);
            panel.revalidate();
            panel.repaint();
        });
        panel.add(btnAtualizar);

        // Adiciona um scroll nos botões
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        frame.add(scrollPane);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static void recarregarScripts(JPanel panel) {
        String pastaDeScrips = "C:/scripts";
        File diretorio = new File(pastaDeScrips);

        if (diretorio.exists() && diretorio.isDirectory()) {
            File[] arquivos = diretorio.listFiles((dir, nome) -> nome.toLowerCase().endsWith(".bat"));

            if (arquivos != null && arquivos.length > 0) {
                for (File arquivo : arquivos) {
                    criaBotaoScript(panel, arquivo);
                }
            } else {
                JLabel label = new JLabel("Nenhum script .bat encontrado!");
                label.setHorizontalAlignment(SwingConstants.CENTER);
                panel.add(label);
            }
        } else {
            JLabel label = new JLabel("Pasta não encontrada: " + pastaDeScrips);
            label.setHorizontalAlignment(SwingConstants.CENTER);
            panel.add(label);
        }

        // Re-adiciona os botões fixos
        JButton btnAbrirPasta = new JButton("Abrir pasta dos Scripts");
        btnAbrirPasta.addActionListener(e -> abrirPasta("C:/scripts"));
        panel.add(btnAbrirPasta);

        JButton btnAtualizar = new JButton("Atualizar Lista");
        btnAtualizar.addActionListener(e -> {
            panel.removeAll();
            recarregarScripts(panel);
            panel.revalidate();
            panel.repaint();
        });
        panel.add(btnAtualizar);
    }

    private static void criaBotaoScript(JPanel panel, File arquivo) {
        String nome = arquivo.getName().replace(".bat", "");

        JButton botao = new JButton(formatarNome(nome));
        botao.setToolTipText("<html>Clique para executar:<br>" + arquivo.getName() +
                "<br><br>Dica: Scripts com CMD interativo usarão /k</html>");

        // Cor diferente para scripts que podem ser interativos
        if (nome.toLowerCase().contains("cmd") || nome.toLowerCase().contains("terminal")) {
            botao.setBackground(new Color(220, 120, 0)); // Laranja para scripts CMD
        } else {
            botao.setBackground(new Color(70, 130, 180)); // Azul para scripts normais
        }

        botao.setForeground(Color.WHITE);
        botao.setFocusPainted(false);
        botao.setFont(new Font("Arial", Font.BOLD, 12));

        botao.addActionListener(e -> executarScript(arquivo));
        panel.add(botao);
    }

    private static String formatarNome(String nome) {
        return nome.replace("_", " ").replace("-", " ").toUpperCase();
    }
    private static void executarScript(File arquivo) {
        try {
            int resposta = JOptionPane.showConfirmDialog(
                    null,
                    "Executar script:\n" + arquivo.getName() + "?",
                    "Confirmação",
                    JOptionPane.YES_NO_OPTION
            );

            if (resposta == JOptionPane.YES_OPTION) {

                // COMANDO MÁGICO que funciona igual ao clique duplo:
                // Abre nova janela CMD no diretório do script e executa
                String comando = String.format(
                        "cmd /c start \"TRT Script\" /D \"%s\" cmd /k \"%s\"",
                        arquivo.getParent(),  // Diretório do script
                        arquivo.getName()     // Nome do arquivo
                );

                ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c", comando);
                pb.start(); // Não precisa esperar, o CMD fica visível

                // Mensagem de confirmação
                JOptionPane.showMessageDialog(
                        null,
                        "✅ Janela do CMD aberta!\n" +
                                "Siga as instruções na tela.",
                        "Script iniciado",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(
                    null,
                    "❌ Erro: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private static void abrirPasta(String caminho) {
        try {
            Desktop.getDesktop().open(new File(caminho));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(
                    null,
                    "Não foi possível abrir a pasta:\n" + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}