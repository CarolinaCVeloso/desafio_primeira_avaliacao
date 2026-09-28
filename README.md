
# Sistema de Estoque de Produtos (desafio Primeira Avaliação)

Exercício de Java sobre **classes abstratas, herança, interfaces, polimorfismo, composição e tratamento de exceções**.

O programa cadastra produtos de tipos diferentes (comuns e perecíveis), controla a venda de itens do estoque e trata situações inválidas por meio de uma hierarquia de exceções própria.

## Estrutura do projeto

```
.
├── EstoqueException.java             # exceção base (checked)
├── QuantidadeInvalidaException.java  # preço/quantidade negativos
├── ProdutoIndisponivelException.java # venda acima do estoque
├── Vendavel.java                     # interface com vender()
├── Product.java                      # classe abstrata (implementa Vendavel)
├── ProdutoComum.java                 # preco × quantidade
├── ProdutoPerecivel.java             # 20% de desconto se diasParaVencer <= 3
├── Estoque.java                      # composição: TEM UMA lista de Product
└── EstoqueApp.java                   # main() com o roteiro de demonstração
```

## Conceitos aplicados

| Conceito | Onde aparece |
|---|---|
| Hierarquia de exceções | `EstoqueException` → `QuantidadeInvalidaException` e `ProdutoIndisponivelException` |
| Classe abstrata | `Product`, com `calcularValorTotal()` abstrato e `getDescricao()` concreto |
| Encapsulamento | atributos `private` em `Product`, acesso por getters |
| Herança + `@Override` | `ProdutoComum` e `ProdutoPerecivel` sobrescrevem `calcularValorTotal()`; o perecível também sobrescreve `getDescricao()` |
| Interface | `Vendavel`, implementada por `Product` |
| Polimorfismo dinâmico | `Estoque.calcularValorTotalEstoque()` soma os produtos sem saber o subtipo |
| Polimorfismo estático (sobrecarga) | `aplicarDesconto(double)` e `aplicarDesconto(double, double)` |
| Composição | `Estoque` mantém uma `List<Product>` e não herda de `Product` |
| Ordem dos `catch` | específicos primeiro, `EstoqueException` por último (`EstoqueApp`) |

## Regras de negócio

- O construtor de `Product` lança `QuantidadeInvalidaException` se `preco` ou `quantidade` forem negativos.
- `vender(int)` lança `ProdutoIndisponivelException` se a quantidade desejada for maior que o estoque disponível; caso contrário, subtrai a quantidade vendida.
- `ProdutoPerecivel.calcularValorTotal()` aplica **20% de desconto** quando `diasParaVencer <= 3`.
- `aplicarDesconto(percentual)` reduz o preço em `percentual`%. `aplicarDesconto(percentual, descontoMaximo)` faz o mesmo, limitando o percentual ao teto informado.

### Decisões adicionais (além do enunciado)

- `vender()` também lança `ProdutoIndisponivelException` quando a quantidade é `<= 0`, para que uma venda negativa não aumente o estoque.
- `Estoque.venderProduto()` lança `ProdutoIndisponivelException` para um índice inexistente.

## Como compilar e executar

Requisito: JDK 8 ou superior.

```bash
javac *.java
java EstoqueApp
```

## Exemplo de saída

```
Exceção capturada (QuantidadeInvalida): Quantidade não pode ser negativa: -5

=== Produtos cadastrados ===
[0] Caderno | Preço: R$ 15.00 | Quantidade: 10
[1] Caneta | Preço: R$ 3.50 | Quantidade: 100
[2] Leite | Preço: R$ 6.00 | Quantidade: 20 | Vence em 2 dia(s)
[3] Iogurte | Preço: R$ 4.00 | Quantidade: 30 | Vence em 10 dia(s)

Valor total do estoque: R$ 716.00

Venda bem-sucedida: 4 unidades de Caderno.
Exceção capturada (ProdutoIndisponivel): Estoque insuficiente de 'Caderno': desejado 50, disponível 6
Exceção capturada (QuantidadeInvalida): Preço não pode ser negativo: -1.0

=== Após as vendas ===
[0] Caderno | Preço: R$ 15.00 | Quantidade: 6
[1] Caneta | Preço: R$ 3.50 | Quantidade: 95
[2] Leite | Preço: R$ 6.00 | Quantidade: 20 | Vence em 2 dia(s)
[3] Iogurte | Preço: R$ 4.00 | Quantidade: 30 | Vence em 10 dia(s)

Valor total do estoque: R$ 638.50
```

### Conferência do valor total inicial

| Produto | Cálculo | Valor |
|---|---|---|
| Caderno (comum) | 15,00 × 10 | R$ 150,00 |
| Caneta (comum) | 3,50 × 100 | R$ 350,00 |
| Leite (perecível, 2 dias) | 6,00 × 20 × 0,80 | R$ 96,00 |
| Iogurte (perecível, 10 dias) | 4,00 × 30 | R$ 120,00 |
| **Total** | | **R$ 716,00** |

## Observação sobre o compilador

Ao compilar com `-Xlint:all`, o `javac` pode avisar que o `catch (EstoqueException)` é inalcançável. Isso ocorre porque as duas subclasses já cobrem todas as exceções lançadas naquele bloco. É apenas um aviso, não impede a compilação, e o `catch` genérico foi mantido de propósito para demonstrar a ordem correta dos blocos.
