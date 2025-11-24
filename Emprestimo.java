public class Emprestimo {
    private int id;
    private int livroId;
    private int usuarioId;
    private String dataEmprestimo;
    private String dataDevolucao;

    public Emprestimo(int id, int livroId, int usuarioId, String dataEmprestimo, String dataDevolucao) {
        this.id = id;
        this.livroId = livroId;
        this.usuarioId = usuarioId;
        this.dataEmprestimo = dataEmprestimo;
        this.dataDevolucao = dataDevolucao;
    }

    public int getId() { return id; }
    public int getLivroId() { return livroId; }
    public int getUsuarioId() { return usuarioId; }
    public String getDataEmprestimo() { return dataEmprestimo; }
    public String getDataDevolucao() { return dataDevolucao; }
    public void setDataDevolucao(String data) { this.dataDevolucao = data; }

    @Override
    public String toString() {
        return id + ";" + livroId + ";" + usuarioId + ";" + dataEmprestimo + ";" + dataDevolucao;
    }

    public static Emprestimo fromString(String linha) {
        String[] p = linha.split(";");
        return new Emprestimo(
            Integer.parseInt(p[0]),
            Integer.parseInt(p[1]),
            Integer.parseInt(p[2]),
            p[3],
            p.length > 4 ? p[4] : "null"
        );
    }
}
