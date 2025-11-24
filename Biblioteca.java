import java.io.*;
import java.util.*;

public class Biblioteca {

    private List<Livro> livros = new ArrayList<>();
    private List<Usuario> usuarios = new ArrayList<>();
    private List<Emprestimo> emprestimos = new ArrayList<>();

    private int idLivro = 1;
    private int idUsuario = 1;
    private int idEmprestimo = 1;

    public void cadastrarLivro(String titulo, String autor) {
        Livro l = new Livro(idLivro++, titulo, autor, true);
        livros.add(l);
        System.out.println("Livro cadastrado.");
    }

    public void cadastrarUsuario(String nome) {
        Usuario u = new Usuario(idUsuario++, nome);
        usuarios.add(u);
        System.out.println("Usuário cadastrado.");
    }

    public void listarLivros() {
        if (livros.isEmpty()) {
            System.out.println("Nenhum livro cadastrado.");
            return;
        }
        livros.forEach(l ->
            System.out.println(l.getId() + " - " + l.getTitulo() + " (" + l.getAutor() + ") - " +
            (l.isDisponivel() ? "Disponível" : "Emprestado"))
        );
    }

    public void listarUsuarios() {
        if (usuarios.isEmpty()) {
            System.out.println("Nenhum usuário cadastrado.");
            return;
        }
        usuarios.forEach(u ->
            System.out.println(u.getId() + " - " + u.getNome())
        );
    }

    public void emprestarLivro(int livroId, int usuarioId, String data) {
        Livro livro = buscarLivro(livroId);
        Usuario usuario = buscarUsuario(usuarioId);

        if (livro == null) {
            System.out.println("Livro não encontrado.");
            return;
        }
        if (!livro.isDisponivel()) {
            System.out.println("Livro já está emprestado.");
            return;
        }
        if (usuario == null) {
            System.out.println("Usuário inexistente.");
            return;
        }

        livro.setDisponivel(false);
        emprestimos.add(new Emprestimo(idEmprestimo++, livroId, usuarioId, data, "null"));
        System.out.println("Empréstimo realizado.");
    }

    public void devolverLivro(int emprestimoId, String data) {
        Emprestimo e = buscarEmprestimo(emprestimoId);

        if (e == null) {
            System.out.println("Empréstimo não encontrado.");
            return;
        }

        Livro l = buscarLivro(e.getLivroId());
        l.setDisponivel(true);

        e.setDataDevolucao(data);
        System.out.println("Livro devolvido.");
    }

    private Livro buscarLivro(int id) {
        for (Livro l : livros)
            if (l.getId() == id) return l;
        return null;
    }

    private Usuario buscarUsuario(int id) {
        for (Usuario u : usuarios)
            if (u.getId() == id) return u;
        return null;
    }

    private Emprestimo buscarEmprestimo(int id) {
        for (Emprestimo e : emprestimos)
            if (e.getId() == id) return e;
        return null;
    }

    public void salvar() {
        salvarArquivo("livros.txt", livros);
        salvarArquivo("usuarios.txt", usuarios);
        salvarArquivo("emprestimos.txt", emprestimos);
        System.out.println("Dados salvos.");
    }

    private void salvarArquivo(String nome, List<?> lista) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(nome))) {
            for (Object o : lista)
                bw.write(o.toString() + "\n");
        } catch (IOException e) { }
    }

    public void carregar() {
        livros = carregarLivros();
        usuarios = carregarUsuarios();
        emprestimos = carregarEmprestimos();

        idLivro = livros.size() + 1;
        idUsuario = usuarios.size() + 1;
        idEmprestimo = emprestimos.size() + 1;

        System.out.println("Dados carregados.");
    }

    private List<Livro> carregarLivros() {
        List<Livro> lista = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("livros.txt"))) {
            String linha;
            while ((linha = br.readLine()) != null)
                lista.add(Livro.fromString(linha));
        } catch (IOException e) { }
        return lista;
    }

    private List<Usuario> carregarUsuarios() {
        List<Usuario> lista = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("usuarios.txt"))) {
            String linha;
            while ((linha = br.readLine()) != null)
                lista.add(Usuario.fromString(linha));
        } catch (IOException e) { }
        return lista;
    }

    private List<Emprestimo> carregarEmprestimos() {
        List<Emprestimo> lista = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("emprestimos.txt"))) {
            String linha;
            while ((linha = br.readLine()) != null)
                lista.add(Emprestimo.fromString(linha));
        } catch (IOException e) { }
        return lista;
    }
}
