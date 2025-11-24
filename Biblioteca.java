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
        for (Livro l : livros)
            System.out.println(l.getId() + " - " + l.getTitulo() + " (" + l.getAutor() + ") - " +
                (l.isDisponivel() ? "Disponível" : "Emprestado"));
    }

    public void listarUsuarios() {
        if (usuarios.isEmpty()) {
            System.out.println("Nenhum usuário cadastrado.");
            return;
        }
        for (Usuario u : usuarios)
            System.out.println(u.getId() + " - " + u.getNome());
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

    public void salvarTudo() {
        salvarLivros();
        salvarUsuarios();
        salvarEmprestimos();
        System.out.println("Dados salvos.");
    }

    public void carregarTudo() {
        carregarLivros();
        carregarUsuarios();
        carregarEmprestimos();

        idLivro = livros.size() + 1;
        idUsuario = usuarios.size() + 1;
        idEmprestimo = emprestimos.size() + 1;

        System.out.println("Dados carregados.");
    }

    public void salvarLivros() {
        try (PrintWriter pw = new PrintWriter(new FileWriter("livros.txt"))) {
            for (Livro l : livros)
                pw.println(l.getId() + ";" + l.getTitulo() + ";" + l.getAutor() + ";" + l.isDisponivel());
        } catch (Exception e) {
            System.out.println("Erro ao salvar livros.");
        }
    }

    public void salvarUsuarios() {
        try (PrintWriter pw = new PrintWriter(new FileWriter("usuarios.txt"))) {
            for (Usuario u : usuarios)
                pw.println(u.getId() + ";" + u.getNome());
        } catch (Exception e) {
            System.out.println("Erro ao salvar usuários.");
        }
    }

    public void salvarEmprestimos() {
        try (PrintWriter pw = new PrintWriter(new FileWriter("emprestimos.txt"))) {
            for (Emprestimo e : emprestimos)
                pw.println(e.getId() + ";" + e.getLivroId() + ";" + e.getUsuarioId() + ";" + e.getDataEmprestimo() + ";" + e.getDataDevolucao());
        } catch (Exception e) {
            System.out.println("Erro ao salvar empréstimos.");
        }
    }

    public void carregarLivros() {
        livros.clear();
        try (BufferedReader br = new BufferedReader(new FileReader("livros.txt"))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] d = linha.split(";");
                Livro l = new Livro(Integer.parseInt(d[0]), d[1], d[2], Boolean.parseBoolean(d[3]));
                livros.add(l);
            }
        } catch (Exception e) { }
    }

    public void carregarUsuarios() {
        usuarios.clear();
        try (BufferedReader br = new BufferedReader(new FileReader("usuarios.txt"))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] d = linha.split(";");
                Usuario u = new Usuario(Integer.parseInt(d[0]), d[1]);
                usuarios.add(u);
            }
        } catch (Exception e) { }
    }

    public void carregarEmprestimos() {
        emprestimos.clear();
        try (BufferedReader br = new BufferedReader(new FileReader("emprestimos.txt"))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] d = linha.split(";");
                Emprestimo e = new Emprestimo(
                    Integer.parseInt(d[0]),
                    Integer.parseInt(d[1]),
                    Integer.parseInt(d[2]),
                    d[3],
                    d[4]
                );
                emprestimos.add(e);
            }
        } catch (Exception e) { }
    }
}
