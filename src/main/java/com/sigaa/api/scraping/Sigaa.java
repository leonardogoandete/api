package com.sigaa.api.scraping;

import com.microsoft.playwright.*;

import java.nio.file.Paths;
import java.util.List;

public class Sigaa {
    public static void main(String[] args) {
        try (Playwright playwright = Playwright.create()) {
            // Inicia o navegador
            //Para exibir o navegador
            //Browser browser = playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(false));
            //Sem exibir
            Browser browser = playwright.firefox().launch();
            // Abre uma nova página
            Page page = browser.newPage();
            // Navega até a página de login do SIGAA e aguarda o carregamento completo
            page.navigate("https://sig.ifrs.edu.br/sigaa/verTelaLogin.do");

            // Preenche o formulário de login
            page.fill("input[name='user.login']", "");
            page.fill("input[name='user.senha']", "");

            // Clica no botão "Entrar"
            page.click("input[type='submit']");

            // Clica na opção "Ensino" no menu
            page.click("td.ThemeOfficeMainItem:nth-child(1)");

            // Aguarda o submenu "Ensino" aparecer
            page.waitForSelector("#cmSubMenuID1");

            // Clica na opção "Consultar Minhas Notas" no submenu "Ensino"
            page.click("tr.ThemeOfficeMenuItem:nth-child(1)");

            // Aguarda até que a tabela esteja carregada na página
            page.waitForSelector("table.tabelaRelatorio");

            // Encontra todas as tabelas na página
            List<ElementHandle> tabelas = page.querySelectorAll("table.tabelaRelatorio");
            // Itera sobre cada tabela
            for (ElementHandle tabela : tabelas) {
                // Verifica se a tabela é referente ao ano de 2024.1
                String caption = tabela.querySelector("caption").innerText();
                if (caption.equals("2023.2")) {
                    // Encontra todas as linhas da tabela
                    List<ElementHandle> linhas = tabela.querySelectorAll("tbody tr");
                    // Itera sobre cada linha
                    for (ElementHandle linha : linhas) {
                        // Extrai e exibe o código, a disciplina, as notas e a situação
                        String codigo = linha.querySelector("td:nth-child(1)").innerText();
                        String disciplina = linha.querySelector("td:nth-child(2)").innerText();
                        String unidade1 = linha.querySelector("td:nth-child(3)").innerText();
                        String unidade2 = linha.querySelector("td:nth-child(4)").innerText();
                        String recuperacao = linha.querySelector("td:nth-child(5)").innerText();
                        String resultado = linha.querySelector("td:nth-child(6)").innerText();
                        String faltas = linha.querySelector("td:nth-child(7)").innerText();
                        String situacao = linha.querySelector("td:nth-child(8)").innerText();

                        // Exibe os dados
                        System.out.println("Código: " + codigo);
                        System.out.println("Disciplina: " + disciplina);
                        System.out.println("Unidade 1: " + unidade1);
                        System.out.println("Unidade 2: " + unidade2);
                        System.out.println("Recuperação: " + recuperacao);
                        System.out.println("Resultado: " + resultado);
                        System.out.println("Faltas: " + faltas);
                        System.out.println("Situação: " + situacao);
                        System.out.println();
                    }
                }
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
