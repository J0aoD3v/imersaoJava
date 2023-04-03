//import java.util.Scanner;
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
        //String url = "https://raw.githubusercontent.com/J0aoD3v/imersaoJava.aula1.aluraStickers/main/topmoviesai.json";
        
        //backup das api via alura
        String url = "https://raw.githubusercontent.com/alura-cursos/imersao-java-2-api/main/TopMovies.json";
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
        //System.out.println(body);

        //passo 2: extrair só os dados que interesssam (titulo, poster, classificação)
        var parser = new JsonParser();
        List<Map<String, String>> listaDeFilmes = parser.parse(body);
        //quantidade de filmes da lista
        System.out.println("Quantidade de filmes: " + listaDeFilmes.size()+"\n");
        //metadados do primeiro da lista
        System.out.println("Metadados: \n" + listaDeFilmes.get(0)+"\n");

        
        //passo 3: exibir e manipular os dados
        var geradora = new GeradoraDeFigurinhas();
        for (Map<String,String> filme : listaDeFilmes) {

            String urlImagem = filme.get("image");
            String titulo = filme.get("title");
            
            
            InputStream inputStream = new URL(urlImagem).openStream();
            String nomeArquivo = titulo + ".png";

            geradora.cria(inputStream, nomeArquivo);

            System.out.println(titulo);
            System.out.println();
        }
        

        //passo 3.1: limitar quantidade de filmes mostrados + console decorado
        /*
        for (int i = 0; i < 10; i++) {
            Map<String, String> filme = listaDeFilmes.get(i);
            System.out.println("\u001b[1mTitulo: \u001b[m" + filme.get("title"));
            System.out.println("\u001b[1mPoster: \u001b[m\u001b[34m\u001b[3m" + filme.get("image"));
            //mudar imDbRating para inteiro (sem decimal)
            //double classificacao = Double.parseDouble(filme.get("imDbRating"));
            //int numeroEstrelinhas = (int) classificacao;

            String imdbRatingString = filme.get("imDbRating"); // Aqui você deve pegar o valor de imdbRating da sua API

            double classificacao = 0.0;
            int numeroEstrelinhas = 0;

            try {
            classificacao = Double.parseDouble(imdbRatingString);
            numeroEstrelinhas = (int) (classificacao); // arredondando para o inteiro mais próximo
            } catch (NumberFormatException e) {
            classificacao = Double.NaN;
            }

            //nota da classificação
            if (classificacao > 7) {
                System.out.println("\u001b[m\u001b[1mClassificação: \u001b[33m" + classificacao);
            } else {
                System.out.println("\u001b[m\u001b[1mClassificação: \u001b[31m" + classificacao);
            }
            //emoji da classificação
            if (Double.isNaN(classificacao)) {
                System.out.print("\u001b[41m🚫 " + "\u001b[m");
            } else if (numeroEstrelinhas > 6) {
                for (int n = 1; n <= numeroEstrelinhas; n++) {
                    System.out.print("\u001b[43m⭐ " + "\u001b[m");
                }
            } else {
                for (int n = 1; n <= numeroEstrelinhas; n++) {
                    System.out.print("\u001b[41m🍅 " + "\u001b[m");
                }
            }
            System.out.println("\n");
        }
        */
    }
}
