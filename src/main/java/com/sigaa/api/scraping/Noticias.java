package com.sigaa.api.scraping;

import com.microsoft.playwright.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Paths;
import java.util.List;

public class Noticias {
    public static void main(String[] args) {
        //        try (Playwright playwright = Playwright.create()) {
//            // Inicia o navegador
//            //Para exibir o navegador
//            Browser browser = playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(false));
//            //Sem exibir
//            //Browser browser = playwright.firefox().launch();
//            // Abre uma nova página
//            Page page = browser.newPage();
//            // Navega até a página de login do SIGAA e aguarda o carregamento completo
//            page.navigate("https://poa.ifrs.edu.br/index.php/ultimas-noticias/noticias-principais");
//
//            List<ElementHandle> noticias = page.querySelectorAll("div.tileItem");
//            for (ElementHandle noticia : noticias) {
//                // Extrai o título e o resumo da notícia
//                String titulo = noticia.querySelector("h2.tileHeadline > a").innerText();
//                String resumo = noticia.querySelector("span.description > p").innerText();
//
//                // Extrai a data e a hora da publicação
//                String dataPublicacao = noticia.querySelector("div.span2.tileInfo > ul > li:nth-child(3)").innerText();
//                String horaPublicacao = noticia.querySelector("div.span2.tileInfo > ul > li:nth-child(4)").innerText();
//
//                System.out.println("Título: " + titulo);
//                System.out.println("Resumo: " + resumo);
//                System.out.println("Data da publicação: " + dataPublicacao);
//                System.out.println("Hora da publicação: " + horaPublicacao);
//                System.out.println();
//
//            }
//
//            // Tira uma captura de tela (opcional)
//            page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("exemplo.png")));
//
//            // Fecha o navegador
//            browser.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        try (Playwright playwright = Playwright.create()) {
            // Inicia o navegador
            //Para exibir o navegador
            //Browser browser = playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(false));
            //Sem exibir
            Browser browser = playwright.firefox().launch();
            // Abre uma nova página
            Page page = browser.newPage();

            // Realiza a solicitação POST para obter a página desejada
            String base = "https://poa.ifrs.edu.br";
            String url = "https://poa.ifrs.edu.br/index.php/ultimas-noticias/noticias-principais";
            String postData = "limit=100"; // Se houver dados a serem enviados no corpo da solicitação POST, adicione aqui
            String contentType = "application/x-www-form-urlencoded"; // O tipo de conteúdo pode variar conforme necessário

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .header("Content-Type", contentType)
                    .POST(HttpRequest.BodyPublishers.ofString(postData))
                    .build();

            HttpClient httpClient = HttpClient.newHttpClient();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            // Carrega o conteúdo HTML da página
            String htmlContent = response.body();
            page.setContent(htmlContent);

            // Realize a extração de dados da página como antes
            List<ElementHandle> noticias = page.querySelectorAll("div.tileItem");
            for (ElementHandle noticia : noticias) {
                // Extrai o título e o resumo da notícia
                String titulo = noticia.querySelector("h2.tileHeadline > a").innerText();
                String linkNoticia = noticia.querySelector("h2.tileHeadline > a").getAttribute("href");
                String resumo = noticia.querySelector("span.description > p").innerText();

                // Extrai a data e a hora da publicação
                String dataPublicacao = noticia.querySelector("div.span2.tileInfo > ul > li:nth-child(3)").innerText();
                String horaPublicacao = noticia.querySelector("div.span2.tileInfo > ul > li:nth-child(4)").innerText();

                System.out.println("Link: "+base+linkNoticia);
                System.out.println("Título: " + titulo);
                System.out.println("Resumo: " + resumo);
                System.out.println("Data da publicação: " + dataPublicacao);
                System.out.println("Hora da publicação: " + horaPublicacao);
                System.out.println();

            }

            // Tira uma captura de tela (opcional)
            page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("exemplo.png")));

            // Fecha o navegador
            browser.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
