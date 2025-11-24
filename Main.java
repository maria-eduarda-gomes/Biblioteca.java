import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Biblioteca b = new Biblioteca();
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== SISTEMA DE BIBLIOTECA =====");
            System.out.println("1. Cadastrar Livro");
            System.out.println("2. Listar Livros");
            System.out.println("3. Cadastrar Usuário");
            System.out.println("4. Listar Usuários");
            System.out.println("5. Emprestar Livro");
            System.out.println("6. Devolver Livro");
            System.out.println("7. Salvar");
            System.out.println("8. Carregar");
            System.out.println("9. Sair");
            System.out.print("Opção: ");

            int op = sc.nextInt();
            sc.nextLine();

            switch (op) {
                case 1:
                    System.out.print("Título: ");
                    String titulo = sc.nextLine();
                    System.out.print("Autor: ");
                    String autor = sc.nextLine();
                    b.cadastrarLivro(titulo, autor);
                    break;

                case 2:
                    b.listarLivros();
                    break;

                case 3:
                    System.out.print("Nome do usuário: ");
                    String nome = sc.nextLine();
                    b.cadastrarUsuario(nome);
                    break;

                case 4:
                    b.listarUsuarios();
                    break;

                case 5:
                    System.out.print("ID do livro: ");
                    int livroId = sc.nextInt();
                    System.out.print("ID do usuário: ");
                    int userId = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Data do empréstimo: ");
                    String dataEmp = sc.nextLine();
                    b.emprestarLivro(livroId, userId, dataEmp);
                    break;

                case 6:
                    System.out.print("ID do empréstimo: ");
                    int empId = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Data da devolução: ");
                    String dataDev = sc.nextLine();
                    b.devolverLivro(empId, dataDev);
                    break;

                case 7:
                    b.salvar();
                    break;

                case 8:
                    b.carregar();
                    break;

                case 9:
                    System.out.println("Saindo...");
                    return;

                default:
                    System.out.println("Opção inválida.");
            }
        }
    }
}
