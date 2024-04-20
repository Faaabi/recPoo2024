import java.sql.*;

public class Main {
    static final String DB_URL = "jdbc:sqlite:car_inventory.db";

    public static void main(String[] args) {
        Connection conn = null;
        Statement stmt = null;

        try {
            conn = DriverManager.getConnection(DB_URL);

            // Criar tabela "carros" se não existir
            criarTabelaCarros(conn);

            // Inserir um novo registro na tabela
            System.out.println("Inserindo registro na tabela...");
            inserirCarro(conn, new Carro("Toyota Corolla", "Sedan", 2022, "Preto", 35000.00));
            System.out.println("Registro inserido com sucesso!");

            // Atualizar um registro na tabela
            System.out.println("Atualizando registro na tabela...");
            atualizarPrecoCarro(conn, "Toyota Corolla", 32000.00);
            System.out.println("Registro atualizado com sucesso!");

            // Excluir um registro na tabela
            System.out.println("Excluindo registro da tabela...");
            excluirCarro(conn, "Toyota Corolla");
            System.out.println("Registro excluído com sucesso!");

            // Consultar os registros na tabela
            System.out.println("Consultando registros na tabela...");
            consultarCarros(conn);

        } catch (SQLException se) {
            // Tratamento de erros do JDBC
            se.printStackTrace();
        } finally {
            // Fechando os recursos em um bloco finally
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException se2) {
                // Nada a fazer
            }
            try {
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        System.out.println("Fim do programa");
    }

    // Método para criar a tabela "carros" se não existir
    private static void criarTabelaCarros(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS carros (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "modelo TEXT," +
                    "tipo TEXT," +
                    "ano INTEGER," +
                    "cor TEXT," +
                    "preco REAL)";
            stmt.executeUpdate(sql);
        }
    }

    // Método para inserir um novo carro na tabela
    private static void inserirCarro(Connection conn, Carro carro) throws SQLException {
        String sql = "INSERT INTO carros (modelo, tipo, ano, cor, preco) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, carro.getModelo());
            stmt.setString(2, carro.getTipo());
            stmt.setInt(3, carro.getAno());
            stmt.setString(4, carro.getCor());
            stmt.setDouble(5, carro.getPreco());
            stmt.executeUpdate();
        }
    }

    // Método para atualizar o preço de um carro na tabela
    private static void atualizarPrecoCarro(Connection conn, String modelo, double novoPreco) throws SQLException {
        String sql = "UPDATE carros SET preco=? WHERE modelo=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, novoPreco);
            stmt.setString(2, modelo);
            stmt.executeUpdate();
        }
    }

    // Método para excluir um carro da tabela
    private static void excluirCarro(Connection conn, String modelo) throws SQLException {
        String sql = "DELETE FROM carros WHERE modelo=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, modelo);
            stmt.executeUpdate();
        }
    }

    // Método para consultar os carros na tabela e imprimir os registros
    private static void consultarCarros(Connection conn) throws SQLException {
        String sql = "SELECT * FROM carros";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String modelo = rs.getString("modelo");
                String tipo = rs.getString("tipo");
                int ano = rs.getInt("ano");
                String cor = rs.getString("cor");
                double preco = rs.getDouble("preco");

                System.out.println("ID: " + id);
                System.out.println("Modelo: " + modelo);
                System.out.println("Tipo: " + tipo);
                System.out.println("Ano: " + ano);
                System.out.println("Cor: " + cor);
                System.out.println("Preço: " + preco);
                System.out.println();
            }
        }
    }
}

// Classe Carro
class Carro {
    private String modelo;
    private String tipo;
    private int ano;
    private String cor;
    private double preco;

    public Carro(String modelo, String tipo, int ano, String cor, double preco) {
        this.modelo = modelo;
        this.tipo = tipo;
        this.ano = ano;
        this.cor = cor;
        this.preco = preco;
    }

    public String getModelo() {
        return modelo;
    }

    public String getTipo() {
        return tipo;
    }

    public int getAno() {
        return ano;
    }

    public String getCor() {
        return cor;
    }

    public double getPreco() {
        return preco;
    }
}
