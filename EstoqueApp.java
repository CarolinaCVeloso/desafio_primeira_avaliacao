public class EstoqueApp {
    public static void main(String[] args) {
        Estoque estoque = new Estoque();

        // Cadastro de produtos válidos
        try {
            estoque.adicionarProduto(new ProdutoComum("Caderno", 15.00, 10));
            estoque.adicionarProduto(new ProdutoComum("Caneta", 3.50, 100));
            estoque.adicionarProduto(new ProdutoPerecivel("Leite", 6.00, 20, 2));   // <= 3 dias: 20% off
            estoque.adicionarProduto(new ProdutoPerecivel("Iogurte", 4.00, 30, 10)); // sem desconto
        } catch (QuantidadeInvalidaException e) {
            System.out.println("Erro no cadastro: " + e.getMessage());
        }

        // Cadastro inválido (quantidade negativa)
        try {
            estoque.adicionarProduto(new ProdutoComum("Borracha", 2.00, -5));
        } catch (QuantidadeInvalidaException e) {
            System.out.println("Exceção capturada (QuantidadeInvalida): " + e.getMessage());
        }

        System.out.println("\n=== Produtos cadastrados ===");
        estoque.listarProdutos();
        System.out.printf("%nValor total do estoque: R$ %.2f%n", estoque.calcularValorTotalEstoque());

        // Venda válida
        try {
            estoque.venderProduto(0, 4);
            System.out.println("\nVenda bem-sucedida: 4 unidades de Caderno.");
        } catch (ProdutoIndisponivelException e) {
            System.out.println("Exceção capturada (ProdutoIndisponivel): " + e.getMessage());
        }

        // Venda inválida (acima do estoque)
        try {
            estoque.venderProduto(0, 50);
        } catch (ProdutoIndisponivelException e) {
            System.out.println("Exceção capturada (ProdutoIndisponivel): " + e.getMessage());
        }

        // Catches na ordem correta: específicos primeiro, EstoqueException por último
        try {
            estoque.venderProduto(1, 5);
            estoque.adicionarProduto(new ProdutoComum("Lápis", -1.00, 10));
        } catch (QuantidadeInvalidaException e) {
            System.out.println("Exceção capturada (QuantidadeInvalida): " + e.getMessage());
        } catch (ProdutoIndisponivelException e) {
            System.out.println("Exceção capturada (ProdutoIndisponivel): " + e.getMessage());
        } catch (EstoqueException e) {
            System.out.println("Exceção genérica de estoque: " + e.getMessage());
        }

        System.out.println("\n=== Após as vendas ===");
        estoque.listarProdutos();
        System.out.printf("%nValor total do estoque: R$ %.2f%n", estoque.calcularValorTotalEstoque());
    }
}
