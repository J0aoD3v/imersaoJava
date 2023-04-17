//import java.util.Scanner;
import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.List;
import java.util.Map;

public class App {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello, World!\n");

        //passo 1: fazer uma conexão HTTP e buscar os filmes da IMDB
        //minha api
        String url = "https://raw.githubusercontent.com/J0aoD3v/imersaoJava/main/filmes.ai/listMovieAi.json";
        
        //backup das api via alura
        //String url = "https://raw.githubusercontent.com/alura-cursos/imersao-java-2-api/main/TopMovies.json";
        //String url = "https://raw.githubusercontent.com/alura-cursos/imersao-java-2-api/main/TopTVs.json";
        //String url = "https://raw.githubusercontent.com/alura-cursos/imersao-java-2-api/main/MostPopularMovies.json";
        //String url = "https://raw.githubusercontent.com/alura-cursos/imersao-java-2-api/main/MostPopularTVs.json";

        //usar chave de uma api do imdb
        /*
        Scanner scanner = new Scanner(System.in);
        System.out.println("Digite a sua chave da API do IMDb:");
        String imbdKey = scanner.nextLine();
        String url = "https://imdb-api.com/en/API/MostPopularMovies/" + imbdKey;
        scanner.close();
        */
        //usar variável de ambiente para guardar chave api
        //linux export IMDB_API_KEY="chave" e echo $IMDB_API_KEY (Tab)
        //windows $Env:IMDB_API_KEY="chave" e $Env:IMDB_API_KEY
        //String imbdKey = System.getenv("IMDB_API_KEY");
        //String url = "https://imdb-api.com/en/API/MostPopularMovies/" + imbdKey;

        URI endereco = URI.create(url);
        var client = HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder(endereco).GET().build();
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        String body = response.body();

        //passo 2: extrair só os dados que interesssam (titulo, poster, classificação)
        var parser = new JsonParser();
        List<Map<String, String>> listaDeFilmes = parser.parse(body);
        //quantidade de filmes da lista
        System.out.println("Quantidade de filmes: " + listaDeFilmes.size()+"\n");

        var diretorio = new File("aula.2/alura.stickers/figurinhas/");
        diretorio.mkdir();

        //passo 3: exibir e manipular os dados
        var geradora = new GeradoraDeFigurinhas();
        //for (Map<String,String> filme : listaDeFilmes) {
        for (int i = 0; i < 5; i++) {
            var filme = listaDeFilmes.get(i);

            String urlImagem = filme.get("image");
            String titulo = filme.get("title");
            
            String imDbRating = filme.get("imDbRating");
            double classificacao = 0.0;
            if (imDbRating != null && !imDbRating.isEmpty()) {
                classificacao = Double.parseDouble(imDbRating);
            }

            String textoFigurinha;
            if (classificacao >= 7.0) {
                textoFigurinha = "TOPZERA";
            } else if (classificacao >= 5.0) {
                textoFigurinha = "HMMMMMM...";
            } else {
                textoFigurinha = "Meh...";
            }

            InputStream inputStream = new URL(urlImagem).openStream();
            String nomeArquivo = diretorio.getPath() + "/" + titulo + ".png";

            geradora.cria(inputStream, nomeArquivo, textoFigurinha);

            System.out.println(titulo);
            System.out.println();
        }
    }
}
