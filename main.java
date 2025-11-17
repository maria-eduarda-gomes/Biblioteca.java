import java.util.*;
import java.io.*;

abstract class ItemBiblioteca {
    protected String id;
    protected String titulo;
    protected int anoPublicacao;
    protected boolean disponivel;
    
    public ItemBiblioteca(String id, String titulo, int anoPublicacao) {
        this.id = id;
        this.titulo = titulo;
        this.anoPublicacao = anoPublicacao;
        this.disponivel = true;
    }
    
    public String getId() { return id; }
    public String getTitulo() { return titulo; }
    public int getAnoPublicacao() { return anoPublicacao; }
    public boolean isDisponivel() { return disponivel; }
    public void setDisponivel(boolean disponivel) { this.disponivel = disponivel; }
    
    public abstract String getTipo();
    public abstract String toFileString();
}

class Livro extends ItemBiblioteca {
    private String autor;
    private String isbn;
    
    public Livro(String id, String titulo, String autor, int anoPublicacao, String isbn) {
        super(id, titulo, anoPublicacao);
        this.autor = autor;
        this.isbn = isbn;
    }
    
    public String getAutor() { return autor; }
    public String getIsbn() { return isbn; }
    
    @Override
    public String getTipo() { return "LIVRO"; }
    
    @Override
    public String toFileString() {
        return String.format("LIVRO|%s|%s|%s|%d|%s|%b", 
            id, titulo, autor, anoPublicacao, isbn, disponivel);
    }
    
    @Override
    public String toString() {
        return String.format("[%s] %s - %s (%d) ISBN: %s [%s]", 
            id, titulo, autor, anoPublicacao, isbn, disponivel ? "Disponível" : "Emprestado");
    }
}

class Revista extends ItemBiblioteca {
    private String editora;
    private int edicao;
    
    public Revista(String id, String titulo, String editora, int anoPublicacao, int edicao) {
        super(id, titulo, anoPublicacao);
        this.editora = editora;
        this.edicao = edicao;
    }
    
    public String getEditora() { return editora; }
    public int getEdicao() { return edicao; }
    
    @Override
    public String getTipo() { return "REVISTA"; }
    
    @Override
    public String toFileString() {
        return String.format("REVISTA|%s|%s|%s|%d|%d|%b", 
            id, titulo, editora, anoPublicacao, edicao, disponivel);
    }
    
    @Override
    public String toString() {
        return String.format("[%s] %s - Ed. %s (%d) #%d [%s]", 
            id, titulo, editora, anoPublicacao, edicao, disponivel ? "Disponível" : "Emprestado");
    }
}

class Usuario {
    private String id;
    private String nome;
    private String email;
    private Set<String> itensEmprestados;
    
    public Usuario(String id, String nome, String email) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.itensEmprestados = new HashSet<>();
    }
    
    public String getId() { return id; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public Set<String> getItensEmprestados() { return itensEmprestados; }
    
    public void adicionarEmprestimo(String itemId) {
        itensEmprestados.add(itemId);
    }
    
    public void removerEmprestimo(String itemId) {
        itensEmprestados.remove(itemId);
    }
    
    public String toFileString() {
        return String.format("USUARIO|%s|%s|%s|%s", 
            id, nome, email, String.join(",", itensEmprestados));
    }
    
    @Override
    public String toString() {
        return String.format("[%s] %s (%s) - %d item(ns) emprestado(s)", 
            id, nome, email, itensEmprestados.size());
    }
}

class Emprestimo {
    private String id;
    private String usuarioId;
    private String itemId;
    private Date dataEmprestimo;
    private Date dataDevolucao;
    
    public Emprestimo(String id, String usuarioId, String itemId, Date dataEmprestimo) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.itemId = itemId;
        this.dataEmprestimo = dataEmprestimo;
        this.dataDevolucao = null;
    }
    
    public String getId() { return id; }
    public String getUsuarioId() { return usuarioId; }
    public String getItemId() { return itemId; }
    public Date getDataEmprestimo() { return dataEmprestimo; }
    public Date getDataDevolucao() { return dataDevolucao; }
    public void setDataDevolucao(Date data) { this.dataDevolucao = data; }
    
    public boolean isAtivo() { return dataDevolucao == null; }
    
    public String toFileString() {
        return String.format("EMPRESTIMO|%s|%s|%s|%d|%s", 
            id, usuarioId, itemId, dataEmprestimo.getTime(),
            dataDevolucao != null ? String.valueOf(dataDevolucao.getTime()) : "null");
    }
}

// ==================== SISTEMA DE GERENCIAMENTO ====================

class Biblioteca {
    private Map<String, ItemBiblioteca> acervo;
    private Map<String, Usuario> usuarios;
    private List<Emprestimo> emprestimos;
    private int proximoIdItem;
    private int proximoIdUsuario;
    private int proximoIdEmprestimo;
    
    private static final String ARQUIVO_ITENS = "biblioteca_itens.txt";
    private static final String ARQUIVO_USUARIOS = "biblioteca_usuarios.txt";
    private static final String ARQUIVO_EMPRESTIMOS = "biblioteca_emprestimos.txt";
    
    public Biblioteca() {
        acervo = new HashMap<>();
        usuarios = new HashMap<>();
        emprestimos = new ArrayList<>();
        proximoIdItem = 1;
        proximoIdUsuario = 1;
        proximoIdEmprestimo = 1;
        carregarDados();
    }
    
    // ========== MÉTODOS DE PERSISTÊNCIA ==========
    
    public void salvarDados() {
        salvarItens();
        salvarUsuarios();
        salvarEmprestimos();
        System.out.println("✓ Dados salvos com sucesso!");
    }
    
    private void salvarItens() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ARQUIVO_ITENS))) {
            for (ItemBiblioteca item : acervo.values()) {
                writer.println(item.toFileString());
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar itens: " + e.getMessage());
        }
    }
    
    private void salvarUsuarios() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ARQUIVO_USUARIOS))) {
            for (Usuario usuario : usuarios.values()) {
                writer.println(usuario.toFileString());
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar usuários: " + e.getMessage());
        }
    }
    
    private void salvarEmprestimos() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ARQUIVO_EMPRESTIMOS))) {
            for (Emprestimo emp : emprestimos) {
                writer.println(emp.toFileString());
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar empréstimos: " + e.getMessage());
        }
    }
    
    private void carregarDados() {
        carregarItens();
        carregarUsuarios();
        carregarEmprestimos();
    }
    
    private void carregarItens() {
        File file = new File(ARQUIVO_ITENS);
        if (!file.exists()) return;
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] partes = linha.split("\\|");
                if (partes[0].equals("LIVRO")) {
                    Livro livro = new Livro(partes[1], partes[2], partes[3], 
                        Integer.parseInt(partes[4]), partes[5]);
                    livro.setDisponivel(Boolean.parseBoolean(partes[6]));
                    acervo.put(livro.getId(), livro);
                    atualizarProximoId(livro.getId());
                } else if (partes[0].equals("REVISTA")) {
                    Revista revista = new Revista(partes[1], partes[2], partes[3], 
                        Integer.parseInt(partes[4]), Integer.parseInt(partes[5]));
                    revista.setDisponivel(Boolean.parseBoolean(partes[6]));
                    acervo.put(revista.getId(), revista);
                    atualizarProximoId(revista.getId());
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao carregar itens: " + e.getMessage());
        }
    }
    
    private void carregarUsuarios() {
        File file = new File(ARQUIVO_USUARIOS);
        if (!file.exists()) return;
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] partes = linha.split("\\|");
                Usuario usuario = new Usuario(partes[1], partes[2], partes[3]);
                if (partes.length > 4 && !partes[4].isEmpty()) {
                    String[] itens = partes[4].split(",");
                    for (String item : itens) {
                        usuario.adicionarEmprestimo(item);
                    }
                }
                usuarios.put(usuario.getId(), usuario);
                atualizarProximoIdUsuario(usuario.getId());
            }
        } catch (IOException e) {
            System.out.println("Erro ao carregar usuários: " + e.getMessage());
        }
    }
    
    private void carregarEmprestimos() {
        File file = new File(ARQUIVO_EMPRESTIMOS);
        if (!file.exists()) return;
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] partes = linha.split("\\|");
                Emprestimo emp = new Emprestimo(partes[1], partes[2], partes[3],
                    new Date(Long.parseLong(partes[4])));
                if (!partes[5].equals("null")) {
                    emp.setDataDevolucao(new Date(Long.parseLong(partes[5])));
                }
                emprestimos.add(emp);
                atualizarProximoIdEmprestimo(emp.getId());
            }
        } catch (IOException e) {
            System.out.println("Erro ao carregar empréstimos: " + e.getMessage());
        }
    }
    
    private void atualizarProximoId(String id) {
        int num = Integer.parseInt(id.substring(1));
        if (num >= proximoIdItem) proximoIdItem = num + 1;
    }
    
    private void atualizarProximoIdUsuario(String id) {
        int num = Integer.parseInt(id.substring(1));
        if (num >= proximoIdUsuario) proximoIdUsuario = num + 1;
    }
    
    private void atualizarProximoIdEmprestimo(String id) {
        int num = Integer.parseInt(id.substring(1));
        if (num >= proximoIdEmprestimo) proximoIdEmprestimo = num + 1;
    }
    
    // ========== MÉTODOS DE GERENCIAMENTO ==========
    
    public void adicionarLivro(String titulo, String autor, int ano, String isbn) {
        String id = "L" + proximoIdItem++;
        Livro livro = new Livro(id, titulo, autor, ano, isbn);
        acervo.put(id, livro);
        System.out.println("✓ Livro adicionado com ID: " + id);
    }
    
    public void adicionarRevista(String titulo, String editora, int ano, int edicao) {
        String id = "R" + proximoIdItem++;
        Revista revista = new Revista(id, titulo, editora, ano, edicao);
        acervo.put(id, revista);
        System.out.println("✓ Revista adicionada com ID: " + id);
    }
    
    public void cadastrarUsuario(String nome, String email) {
        String id = "U" + proximoIdUsuario++;
        Usuario usuario = new Usuario(id, nome, email);
        usuarios.put(id, usuario);
        System.out.println("✓ Usuário cadastrado com ID: " + id);
    }
    
    public void realizarEmprestimo(String usuarioId, String itemId) {
        Usuario usuario = usuarios.get(usuarioId);
        ItemBiblioteca item = acervo.get(itemId);
        
        if (usuario == null) {
            System.out.println("✗ Usuário não encontrado!");
            return;
        }
        if (item == null) {
            System.out.println("✗ Item não encontrado!");
            return;
        }
        if (!item.isDisponivel()) {
            System.out.println("✗ Item já está emprestado!");
            return;
        }
        
        String empId = "E" + proximoIdEmprestimo++;
        Emprestimo emprestimo = new Emprestimo(empId, usuarioId, itemId, new Date());
        emprestimos.add(emprestimo);
        item.setDisponivel(false);
        usuario.adicionarEmprestimo(itemId);
        
        System.out.println("✓ Empréstimo realizado com sucesso! ID: " + empId);
    }
    
    public void realizarDevolucao(String itemId) {
        ItemBiblioteca item = acervo.get(itemId);
        
        if (item == null) {
            System.out.println("✗ Item não encontrado!");
            return;
        }
        if (item.isDisponivel()) {
            System.out.println("✗ Item não está emprestado!");
            return;
        }
        
        for (Emprestimo emp : emprestimos) {
            if (emp.getItemId().equals(itemId) && emp.isAtivo()) {
                emp.setDataDevolucao(new Date());
                item.setDisponivel(true);
                Usuario usuario = usuarios.get(emp.getUsuarioId());
                if (usuario != null) {
                    usuario.removerEmprestimo(itemId);
                }
                System.out.println("✓ Devolução realizada com sucesso!");
                return;
            }
        }
    }
    
    public void listarAcervo() {
        if (acervo.isEmpty()) {
            System.out.println("Acervo vazio.");
            return;
        }
        
        System.out.println("\n=== ACERVO DA BIBLIOTECA ===");
        for (ItemBiblioteca item : acervo.values()) {
            System.out.println(item);
        }
    }
    
    public void listarUsuarios() {
        if (usuarios.isEmpty()) {
            System.out.println("Nenhum usuário cadastrado.");
            return;
        }
        
        System.out.println("\n=== USUÁRIOS CADASTRADOS ===");
        for (Usuario usuario : usuarios.values()) {
            System.out.println(usuario);
        }
    }
    
    public void listarEmprestimosAtivos() {
        List<Emprestimo> ativos = new ArrayList<>();
        for (Emprestimo emp : emprestimos) {
            if (emp.isAtivo()) {
                ativos.add(emp);
            }
        }
        
        if (ativos.isEmpty()) {
            System.out.println("Nenhum empréstimo ativo.");
            return;
        }
        
        System.out.println("\n=== EMPRÉSTIMOS ATIVOS ===");
        for (Emprestimo emp : ativos) {
            Usuario usuario = usuarios.get(emp.getUsuarioId());
            ItemBiblioteca item = acervo.get(emp.getItemId());
            System.out.printf("[%s] %s emprestou: %s (desde %s)\n",
                emp.getId(), usuario.getNome(), item.getTitulo(), emp.getDataEmprestimo());
        }
    }
    
    public void buscarPorTitulo(String titulo) {
        List<ItemBiblioteca> resultados = new ArrayList<>();
        for (ItemBiblioteca item : acervo.values()) {
            if (item.getTitulo().toLowerCase().contains(titulo.toLowerCase())) {
                resultados.add(item);
            }
        }
        
        if (resultados.isEmpty()) {
            System.out.println("Nenhum item encontrado.");
            return;
        }
        
        System.out.println("\n=== RESULTADOS DA BUSCA ===");
        for (ItemBiblioteca item : resultados) {
            System.out.println(item);
        }
    }
}

// ==================== INTERFACE DO USUÁRIO ====================

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static Biblioteca biblioteca = new Biblioteca();
    
    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════╗");
        System.out.println("║  SISTEMA DE BIBLIOTECA - v1.0      ║");
        System.out.println("╚════════════════════════════════════╝\n");
        
        boolean executando = true;
        while (executando) {
            exibirMenu();
            int opcao = lerOpcao();
            
            switch (opcao) {
                case 1: adicionarLivro(); break;
                case 2: adicionarRevista(); break;
                case 3: cadastrarUsuario(); break;
                case 4: realizarEmprestimo(); break;
                case 5: realizarDevolucao(); break;
                case 6: biblioteca.listarAcervo(); break;
                case 7: biblioteca.listarUsuarios(); break;
                case 8: biblioteca.listarEmprestimosAtivos(); break;
                case 9: buscarItem(); break;
                case 0: 
                    biblioteca.salvarDados();
                    executando = false;
                    System.out.println("\nEncerrando sistema. Até logo!");
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
            
            if (executando) {
                System.out.println("\nPressione ENTER para continuar...");
                scanner.nextLine();
            }
        }
        
        scanner.close();
    }
    
    private static void exibirMenu() {
        System.out.println("\n┌──────────────────────────────────┐");
        System.out.println("│         MENU PRINCIPAL           │");
        System.out.println("├──────────────────────────────────┤");
        System.out.println("│ 1. Adicionar Livro               │");
        System.out.println("│ 2. Adicionar Revista             │");
        System.out.println("│ 3. Cadastrar Usuário             │");
        System.out.println("│ 4. Realizar Empréstimo           │");
        System.out.println("│ 5. Realizar Devolução            │");
        System.out.println("│ 6. Listar Acervo                 │");
        System.out.println("│ 7. Listar Usuários               │");
        System.out.println("│ 8. Listar Empréstimos Ativos     │");
        System.out.println("│ 9. Buscar por Título             │");
        System.out.println("│ 0. Sair                          │");
        System.out.println("└──────────────────────────────────┘");
        System.out.print("Escolha uma opção: ");
    }
    
    private static int lerOpcao() {
        try {
            int opcao = Integer.parseInt(scanner.nextLine());
            return opcao;
        } catch (NumberFormatException e) {
            return -1;
        }
    }
    
    private static void adicionarLivro() {
        System.out.println("\n=== ADICIONAR LIVRO ===");
        System.out.print("Título: ");
        String titulo = scanner.nextLine();
        System.out.print("Autor: ");
        String autor = scanner.nextLine();
        System.out.print("Ano de publicação: ");
        int ano = Integer.parseInt(scanner.nextLine());
        System.out.print("ISBN: ");
        String isbn = scanner.nextLine();
        
        biblioteca.adicionarLivro(titulo, autor, ano, isbn);
    }
    
    private static void adicionarRevista() {
        System.out.println("\n=== ADICIONAR REVISTA ===");
        System.out.print("Título: ");
        String titulo = scanner.nextLine();
        System.out.print("Editora: ");
        String editora = scanner.nextLine();
        System.out.print("Ano de publicação: ");
        int ano = Integer.parseInt(scanner.nextLine());
        System.out.print("Edição: ");
        int edicao = Integer.parseInt(scanner.nextLine());
        
        biblioteca.adicionarRevista(titulo, editora, ano, edicao);
    }
    
    private static void cadastrarUsuario() {
        System.out.println("\n=== CADASTRAR USUÁRIO ===");
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        
        biblioteca.cadastrarUsuario(nome, email);
    }
    
    private static void realizarEmprestimo() {
        System.out.println("\n=== REALIZAR EMPRÉSTIMO ===");
        System.out.print("ID do Usuário: ");
        String usuarioId = scanner.nextLine();
        System.out.print("ID do Item: ");
        String itemId = scanner.nextLine();
        
        biblioteca.realizarEmprestimo(usuarioId, itemId);
    }
    
    private static void realizarDevolucao() {
        System.out.println("\n=== REALIZAR DEVOLUÇÃO ===");
        System.out.print("ID do Item: ");
        String itemId = scanner.nextLine();
        
        biblioteca.realizarDevolucao(itemId);
    }
    
    private static void buscarItem() {
        System.out.println("\n=== BUSCAR ITEM ===");
        System.out.print("Digite o título ou parte dele: ");
        String titulo = scanner.nextLine();
        
        biblioteca.buscarPorTitulo(titulo);
    }
}