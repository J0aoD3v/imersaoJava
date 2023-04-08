//import java.io.FileInputStream;
//import java.net.URL;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.awt.font.TextLayout;

import javax.imageio.ImageIO;

public class GeradoraDeFigurinhas {
    

    public void cria(InputStream inputStream, String nomeArquivo) throws Exception {

        // Leitura da imagem
        // InputStream inputStream = 
        //              new FileInputStream(new File("D:/ARQUIVOS/Downloads/imersaoJava/aula.2/alura.stickers/entrada/Filme-maior.jpg"));       
        // InputStream inputStream = 
        //                 new URL("https://m.media-amazon.com/images/M/MV5BNDE3ODcxYzMtY2YzZC00NmNlLWJiNDMtZDViZWM2MzIxZDYwXkEyXkFqcGdeQXVyNjAwNDUxODI@.jpg")
        //                 .openStream();
        
        BufferedImage imagemOrigiral = ImageIO.read(inputStream);

        // Cria nova imagem em memória com transparência e com tamanho novo
        int largura = imagemOrigiral.getWidth();
        int altura = imagemOrigiral.getHeight();
        int novaAltura = altura + 200;
        BufferedImage novaImagem = new BufferedImage(largura, novaAltura, BufferedImage.TRANSLUCENT);

        // Copiar a imagem original para nova iamgem (em memória)
        Graphics2D graphics = (Graphics2D) novaImagem.getGraphics();
        graphics.drawImage(imagemOrigiral, 0, 0, null);

        // Configurar a fonte
        //var fonte = new Font(Font.SANS_SERIF, Font.BOLD, 128);
        var fonte = new Font("IMPACT", Font.BOLD, 128);
        graphics.setColor(Color.YELLOW);
        graphics.setFont(fonte);

        // Escrever uma frasse na nova imagem
        String texto = "TOPZERA";
        FontMetrics fontMetrics = graphics.getFontMetrics();
        Rectangle2D retangulo = fontMetrics.getStringBounds(texto, graphics);
        int larguraTexto = (int) retangulo.getWidth();
        int posicaoTextoX = (largura - larguraTexto) / 2;
        int posicaoTextoY = novaAltura - 50;
        graphics.drawString(texto, posicaoTextoX, posicaoTextoY);

        // criando borda do texto
        FontRenderContext fontRenderContext = graphics.getFontRenderContext();
        var textLayout = new TextLayout(texto, fonte, fontRenderContext);

        Shape outline = textLayout.getOutline(null);
        AffineTransform transform = graphics.getTransform();
        transform.translate(posicaoTextoX, posicaoTextoY);
        graphics.setTransform(transform);

        var outlineStroke = new BasicStroke(largura * 0.004f);
        graphics.setStroke(outlineStroke);

        graphics.setColor(Color.black);
        graphics.draw(outline);
        graphics.setClip(outline);

        // Escrever uma imagem nova em um arquivo
        //ImageIO.write(novaImagem, "png", new File("aula.2/alura.stickers/saida/figurinha.png"));
        nomeArquivo = nomeArquivo.replace(":", " -");
        ImageIO.write(novaImagem, "png", new File(nomeArquivo));
    }

  /*public static void main(String[] args) throws Exception {
        var geradora = new GeradoraDeFigurinhas();
        geradora.cria();
    }*/
}
