public abstract class Product implements Vendavel {
    private String nome;
    private double preco;
    private int quantidade;

    public Product(String nome, double preco, int quantidade) throws QuantidadeInvalidaException {
        if (preco < 0) {
            throw new QuantidadeInvalidaException("Preço não pode ser negativo: " + preco);
        }
        if (quantidade < 0) {
            throw new QuantidadeInvalidaException("Quantidade não pode ser negativa: " + quantidade);
        }
        this.nome = nome;
        this.preco = preco;
        this.quantidade = quantidade;
    }

    public String getNome() { return nome; }
    public double getPreco() { return preco; }
    public int getQuantidade() { return quantidade; }

    public abstract double calcularValorTotal();

    public String getDescricao() {
        return String.format("%s | Preço: R$ %.2f | Quantidade: %d", nome, preco, quantidade);
    }

    @Override
    public void vender(int quantidadeDesejada) throws ProdutoIndisponivelException {
        if (quantidadeDesejada <= 0) {
            throw new ProdutoIndisponivelException("Quantidade de venda deve ser positiva: " + quantidadeDesejada);
        }
        if (quantidadeDesejada > quantidade) {
            throw new ProdutoIndisponivelException(String.format(
                "Estoque insuficiente de '%s': desejado %d, disponível %d",
                nome, quantidadeDesejada, quantidade));
        }
        quantidade -= quantidadeDesejada;
    }

    // Sobrecarga (polimorfismo estático)
    public void aplicarDesconto(double percentual) {
        preco = preco * (1 - percentual / 100.0);
    }

    public void aplicarDesconto(double percentual, double descontoMaximo) {
        double aplicado = Math.min(percentual, descontoMaximo);
        aplicarDesconto(aplicado);
    }
}
