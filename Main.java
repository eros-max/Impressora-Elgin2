import com.sun.jna.Library;
import com.sun.jna.Native;
import java.util.Scanner;
import javax.swing.JFileChooser;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.io.FileInputStream;

public class Main {

    // Interface que representa a DLL, usando JNA
    public interface ImpressoraDLL extends Library {

        // Caminho completo para a DLL
        ImpressoraDLL INSTANCE = (ImpressoraDLL) Native.load(
                "C:\\Users\\eros_max\\Downloads\\wetransfer_java-aluno-em_2025-11-04_1357\\Java-Aluno EM\\E1_Impressora01.dll",
                ImpressoraDLL.class
        );

        //Funções Elgin Utilizadas ao longo do programa
        int AbreConexaoImpressora(int tipo, String modelo, String conexao, int param); //Abre conexão com a impressora.
        int FechaConexaoImpressora(); //Fecha conexão com a impressora.
        int ImpressaoTexto(String dados, int posicao, int estilo, int tamanho); //Envia informações de texto para o buffer da impressora.
        int Corte(int avanco); //Realiza o corte do papel.
        int ImpressaoQRCode(String dados, int tamanho, int nivelCorrecao); //Impressão de QRCode.
        int ImpressaoCodigoBarras(int tipo, String dados, int altura, int largura, int HRI); //Impressão de código de barras.
        int AvancaPapel(int linhas); //Imprime informações no buffer e avança o papel.
        int StatusImpressora(int param); //Obtém o status da impressora.
        int AbreGavetaElgin(); //Abre gavetas Elgin.
        int AbreGaveta(int pino, int ti, int tf); //Abre gaveta.
        int SinalSonoro(int qtd, int tempoInicio, int tempoFim); //Emite sinal sonoro.
        int ModoPagina(); //Habilita Modo Página.
        int LimpaBufferModoPagina(); //Limpa Buffer em Modo Página.
        int ImprimeModoPagina(); //Imprime Modo Página.
        int ModoPadrao(); //Retorna ao Modo Padrão.
        int PosicaoImpressaoHorizontal(int posicao); //Define Posição de Impressão Horizontal.
        int PosicaoImpressaoVertical(int posicao); //Define a Posição da Impressão Vertical.
        int ImprimeXMLSAT(String dados, int param); //Imprime Danfe SAT.
        int ImprimeXMLCancelamentoSAT(String dados, String assQRCode, int param); //Imprime Danfe de cancelamento SAT.
    }

    private static boolean conexaoAberta = false;
    private static int tipo;
    private static String modelo;
    private static String conexao;
    private static int parametro;
    private static final Scanner scanner = new Scanner(System.in);

    private static String capturarEntrada(String mensagem) {
        System.out.print(mensagem);
        return scanner.nextLine();
    }

    public static void configurarConexao() {
        if (!conexaoAberta) {
            tipo = Integer.parseInt(capturarEntrada("Digite o tipo de conexão (ex: 1 para USB, 2 para serial, etc.): "));
            modelo = capturarEntrada("Digite o modelo da impressora (ex: 'i9'): ");
            conexao = capturarEntrada("Digite a porta de conexão (ex: 'lp0' para Linux ou 'COM3' para Windows): ");
            parametro = Integer.parseInt(capturarEntrada("Digite o parâmetro adicional (ex: 0 para padrão): "));
            System.out.println("Parâmetros de conexão configurados com sucesso.");
        } else {
            System.out.println("Parâmetros de conexão já configurados. Pronto para abrir a conexão.");
        }
    }

    public static void abrirConexao() {
        if (!conexaoAberta){
            int retorno = ImpressoraDLL.INSTANCE.AbreConexaoImpressora(tipo, modelo, conexao, parametro);

            if (retorno == 0){
                conexaoAberta = true;
                System.out.println("Conexao aberta com sucess");
            } else {
                System.out.println("ERRO. Retorno: " + retorno);
            }
        } else {
            System.out.println("\n-CONEXÃO JÁ ESTÁ ABERTA-");
        }
    }

    public static void fecharConexao() {
        if (conexaoAberta){
            int retorno = ImpressoraDLL.INSTANCE.FechaConexaoImpressora();

            if (retorno == 0){
                conexaoAberta = false;
                System.out.println("fechada com sucesso");
            }
            else {
                System.out.println("ERRO. Retorno: " + retorno);
            }
        }
        else {
            System.out.println("\n-CONEXÃO JÁ ESTÁ FECHADA-");
        }
    }

    public static void ImpressaoTexto() {
        if (conexaoAberta){
            System.out.println("Digite os dados a serem impressos: ");
            String dados = scanner.nextLine();

            int retorno = ImpressoraDLL.INSTANCE.ImpressaoTexto(dados, 1, 0, 1);

            if (retorno == 0){
                System.out.println("Dados Impressos com sucesso");
                ImpressoraDLL.INSTANCE.Corte(5);
            }
            else {
                System.out.println("ERRO. Retorno: " + retorno);
            }
        }
        else {
            System.out.println("\n-CONEXÃO ESTÁ FECHADA-");
        }
    }

    public static void ImpressaoCodigoBarras() {
        if (conexaoAberta){

            // (8, "{A012345678912", 100, 2, 3)
            int retorno = ImpressoraDLL.INSTANCE.ImpressaoCodigoBarras(8, "{A012345678912",100,2,3);

            if (retorno == 0){
                System.out.println("Dados Impressos com sucesso");
                ImpressoraDLL.INSTANCE.Corte(5);
            }
            else {
                System.out.println("ERRO. Retorno: " + retorno);
            }
        }
        else {
            System.out.println("\n-CONEXÃO ESTÁ FECHADA-");
        }
    }

    public static void ImpressaoQRCode() {
        if (conexaoAberta){

            System.out.println("Digite os dados do QRCode(informações que irão compor o QRCode): ");
            String dados = scanner.nextLine();


            int retorno = ImpressoraDLL.INSTANCE.ImpressaoQRCode(dados, 6, 4);

            if (retorno == 0){
                System.out.println("QRCode impresso com sucesso");

            }
            else {
                System.out.println("ERRO. Retorno: " + retorno);
            }
        }
        else {
            System.out.println("\n-CONEXÃO ESTÁ FECHADA-");
        }
    }

    public static void AbreGaveta() {
        if (conexaoAberta){

            int retorno = ImpressoraDLL.INSTANCE.AbreGaveta(1, 1, 1);

            if (retorno == 0){
                System.out.println("Gaveta foi aberta com sucesso");
            }
            else {
                System.out.println("ERRO. Retorno: " + retorno);
            }
        }
        else {
            System.out.println("\n-CONEXÃO ESTÁ FECHADA-");
        }
    }

    public static void AbreGavetaElgin() {
        if (conexaoAberta){
            int retorno = ImpressoraDLL.INSTANCE.AbreGavetaElgin();

            if (retorno == 0) {
                System.out.println("Gaveta Elgin aberta com sucesso!");
            }
            else {
                System.out.println("ERRO. Retorno: " + retorno);
            }
        }
        else {
            System.out.println("\n-CONEXÃO ESTÁ FECHADA-");
        }
    }

    public static void SinalSonoro() {
        if (conexaoAberta){

            int retorno = ImpressoraDLL.INSTANCE.SinalSonoro(1, 4, 1);

            if (retorno == 0){
                System.out.println("Sinal sonoro emitido na impressora com sucesso");
            }
            else {
                System.out.println("ERRO. Retorno: " + retorno);
            }
        }
        else {
            System.out.println("\n-CONEXÃO ESTÁ FECHADA-");
        }
    }

    public static void ImprimeXMLCancelamentoSAT() {
        if (conexaoAberta){

            String dados = "path=C:\\Users\\eros_max\\Downloads\\wetransfer_java-aluno-em_2025-11-04_1357\\Java-Aluno EM\\CANC_SAT.xml";
            String assQRCode = "Q5DLkpdRijIRGY6YSSNsTWK1TztHL1vD0V1Jc4spo/CEUqICEb9SFy82ym8EhBRZjbh3btsZhF+sjHqEMR159i4agru9x6KsepK/q0E2e5xlU5cv3m1woYfgHyOkWDNcSdMsS6bBh2Bpq6s89yJ9Q6qh/J8YHi306ce9Tqb/drKvN2XdE5noRSS32TAWuaQEVd7u+TrvXlOQsE3fHR1D5f1saUwQLPSdIv01NF6Ny7jZwjCwv1uNDgGZONJdlTJ6p0ccqnZvuE70aHOI09elpjEO6Cd+orI7XHHrFCwhFhAcbalc+ZfO5b/+vkyAHS6CYVFCDtYR9Hi5qgdk31v23w==";

            int retorno = ImpressoraDLL.INSTANCE.ImprimeXMLCancelamentoSAT(dados, assQRCode, 0);

            if (retorno == 0) {
                System.out.println("XML Impresso com sucesso!");
            }
            else {
                System.out.println("ERRO. Retorno: " + retorno);
            }
        }
        else {
            System.out.println("\n-CONEXÃO ESTÁ FECHADA-");
        }
    }

    public static void ImprimeXMLSAT() {
        if (conexaoAberta){

            String dados = "path=C:\\Users\\eros_max\\Downloads\\wetransfer_java-aluno-em_2025-11-04_1357\\Java-Aluno EM\\XMLSAT.xml";

            int retorno = ImpressoraDLL.INSTANCE.ImprimeXMLSAT(dados, 0);

            if (retorno == 0) {
                System.out.println("XML Impresso com sucesso!");
            }
            else {
                System.out.println("ERRO. Retorno: " + retorno);
            }
        }
        else {
            System.out.println("\n-CONEXÃO ESTÁ FECHADA-");
        }
    }

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n*************************************************");
            System.out.println("**************** MENU IMPRESSORA *******************");
            System.out.println("*************************************************\n");

            System.out.println("1  - Configurar Conexao");
            System.out.println("2  - Abrir Conexao");
            System.out.println("3  - Impressao Texto");
            System.out.println("4  - Impressao QRCode");
            System.out.println("5  - Impressao Cod Barras");
            System.out.println("6  - Impressao XML SAT");
            System.out.println("7  - Impressao XML Canc SAT");
            System.out.println("8  - Abrir Gaveta Elgin");
            System.out.println("9  - Abrir Gaveta");
            System.out.println("10 - Sinal Sonoro");
            System.out.println("0  - Fechar Conexao e Sair");
            System.out.println("--------------------------------------");

            String escolha = capturarEntrada("\nDigite a opção desejada: ");

            if (escolha.equals("0")) {
                fecharConexao();
                System.out.println("Programa encerrado.");
                break;
            }

            switch (escolha) {
                case "1":
                    configurarConexao();
                    break;
                case "2":
                    abrirConexao();
                    break;
                case "3":
                    ImpressaoTexto();
                    ImpressoraDLL.INSTANCE.Corte(5);
                    break;
                case "4":
                    ImpressaoQRCode();
                    ImpressoraDLL.INSTANCE.Corte(5);
                    break;
                case "5":
                    ImpressaoCodigoBarras();
                    ImpressoraDLL.INSTANCE.Corte(5);
                    break;
                case "6":
                    ImprimeXMLSAT();
                    ImpressoraDLL.INSTANCE.Corte(5);
                    break;

                case "7":
                    ImprimeXMLCancelamentoSAT();
                    ImpressoraDLL.INSTANCE.Corte(5);
                    break;
                case "8":
                    AbreGavetaElgin();
                    break;
                case "9":
                    AbreGaveta();
                    break;
                case "10":
                    SinalSonoro();
                    break;
                default:
                    System.out.println("OPÇÃO INVÁLIDA");
            }
        }

        scanner.close();
    }

    private static String lerArquivoComoString(String path) throws IOException {
        FileInputStream fis = new FileInputStream(path);
        byte[] data = fis.readAllBytes();
        fis.close();
        return new String(data, StandardCharsets.UTF_8);
    }

}