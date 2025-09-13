import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import javax.swing.*;

public class App {
    JLabel titulo;
    JButton apagar;
    JButton info;
    public static void main(String[] args) throws Exception {
        App app = new App();
        System.out.println("====== GlowImg ========");

        if (args.length == 0) {
            System.out.println("Nunhuma foto foi passada");
            System.exit(0);
        }

        String caminho = args[0];
        System.out.println("Caminho da foto: " + caminho);

        JFrame tela = new JFrame("GlowImg - " + windowTitle(caminho));
        tela.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        tela.setResizable(false);
        tela.setSize(1024, 720);
        tela.setVisible(true);
        tela.setLocationRelativeTo(null);

        ImageIcon fotoIcon = new ImageIcon(caminho);
        
        int larguraOriginal = fotoIcon.getIconWidth();
        int alturaOriginal = fotoIcon.getIconHeight();

        double escala = Math.min(
            (double) 1020 / larguraOriginal,
            (double) 660 / alturaOriginal
        );

        int novaLargura = (int) (larguraOriginal * escala);
        int novaAltura = (int) (alturaOriginal * escala);

        Image imgResized = fotoIcon.getImage().getScaledInstance(novaLargura, novaAltura, Image.SCALE_SMOOTH);
        ImageIcon iconSized = new ImageIcon(imgResized);

        JLabel labelFoto = new JLabel(iconSized);
        labelFoto.setLocation(0, 0);
        labelFoto.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent a) {
                setTile(app);
            }
        });
        
        app.titulo = new JLabel("    " + caminho);
        app.titulo.setBounds(10, 10, 980 + 10, 34);
        app.titulo.setFont(new Font("Arial", Font.BOLD, 16));
        app.titulo.setOpaque(true);
        app.titulo.setBackground(new Color(75, 75, 75, 228));
        app.titulo.setForeground(Color.WHITE);

        app.apagar = new JButton("Apagar");
        app.apagar.setBounds(10, 50, 100, 34);
        app.apagar.setFont(new Font("Arial", Font.PLAIN, 16));
        app.apagar.setBackground(new Color(75, 75, 75, 228));
        app.apagar.setForeground(Color.WHITE);
        app.apagar.addActionListener(_ -> deletePhoto(caminho));

        app.info = new JButton("Info");
        app.info.setBounds(120, 50, 70, 34);
        app.info.setFont(new Font("Arial", Font.PLAIN, 16));
        app.info.setBackground(new Color(75, 75, 75, 228));
        app.info.setForeground(Color.WHITE);
        app.info.addActionListener(_ -> aboutPhoto(caminho, larguraOriginal, alturaOriginal));

        tela.add(app.info);
        tela.add(app.apagar);
        tela.add(app.titulo);
        tela.add(labelFoto);

        tela.repaint();
        tela.revalidate();
    }

    public static void setTile(App app) {
        boolean isVisible = app.titulo.isVisible();
        
        if (isVisible) {
            app.titulo.setVisible(false);
            app.apagar.setVisible(false);
            app.info.setVisible(false);
        } else {
            app.titulo.setVisible(true);
            app.apagar.setVisible(true);
            app.info.setVisible(true);
        }
    }

    public static String windowTitle(String caminho) {
        File img = new File(caminho);
        return img.getName();
    }

    public static void deletePhoto(String caminho) {
        int resposta = JOptionPane.showConfirmDialog(null, "Tem certeza que quer apagar a foto permanentemente?", null, JOptionPane.WARNING_MESSAGE);

        if (resposta == JOptionPane.YES_OPTION) {
            File img = new File(caminho);

            try {
                img.delete();
                JOptionPane.showMessageDialog(null, "A foto em " + img.getName() + " foi apagada!");
            } catch (Exception a) {
                JOptionPane.showMessageDialog(null, "ERRO: " + a);
            }
        }
    }

    public static void aboutPhoto(String caminho, int largura, int altura) {
        File img = new File(caminho);

        JOptionPane.showMessageDialog(null, "Nome da foto: " + img.getName() + "\nPasta: " + img.getAbsolutePath() + "\nTamanho (largura x altura): " + largura + " x " + altura + "\nTamanho: " + imgSize(caminho), "Sobre a foto", JOptionPane.INFORMATION_MESSAGE);
    }

    public static String imgSize(String caminho) {
        File img = new File(caminho);
        long tamanho = img.length();

        if (tamanho >= 1048576) {
            return tamanho / (1024 * 1024) + " MB";
        } else if (tamanho >= 1024) {
            return tamanho / 1024 + " KB";
        } else {
            return tamanho + " bytes";
        }
    }
}