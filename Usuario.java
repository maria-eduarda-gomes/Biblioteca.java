public class Usuario {
    private int id;
    private String nome;

    public Usuario(int id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public int getId() { return id; }
    public String getNome() { return nome; }

    @Override
    public String toString() {
        return id + ";" + nome;
    }

    public static Usuario fromString(String linha) {
        String[] p = linha.split(";");
        return new Usuario(Integer.parseInt(p[0]), p[1]);
    }
}
