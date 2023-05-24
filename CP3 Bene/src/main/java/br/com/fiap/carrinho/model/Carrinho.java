package br.com.fiap.carrinho.model;

import br.com.fiap.cliente.model.Cliente;
import br.com.fiap.produto.model.Produto;
import br.com.fiap.produto.model.ProdutoPerecivel;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;

@Entity
@Table(name = "TB_CARRINHO")
public class Carrinho {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_CARRINHO")
    private Long id;
    @Column(name = "DT_CRIACAO")
    private LocalDateTime criacao = LocalDateTime.now();
    @Column(name = "DT_ENCERRAMENTO")
    private LocalDateTime encerramento = criacao.plusHours(24);
    @Column(name = "VALOR_TOTAL")
    private double valorTotal;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "TB_CARRINHO_PRODUTO",
            joinColumns = @JoinColumn(name = "ID_CARRINHO",
                    foreignKey = @ForeignKey(name = "FK_CARRINHO_PRODUTO",
                            value = ConstraintMode.CONSTRAINT)
            ),
            inverseJoinColumns = @JoinColumn(name = "ID_PRODUTO",
                    foreignKey = @ForeignKey(name = "FK_PRODUTO_CARRINHO",
                            value = ConstraintMode.CONSTRAINT)
            )
    )
    Collection<Produto> produtos = new LinkedHashSet<>();

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "ID_CLIENTE", referencedColumnName = "ID_CLIENTE",
            foreignKey = @ForeignKey(name = "FK_CARRINHO_CLIENTE", value = ConstraintMode.CONSTRAINT))
    private Cliente cliente;

    public Carrinho() {
    }

    public Carrinho(Long id, LocalDateTime criacao, LocalDateTime encerramento, double valorTotal, Collection<Produto> produtos, Cliente cliente) {
        this.id = id;
        this.criacao = criacao;
        this.encerramento = encerramento;
        this.valorTotal = valorTotal;
        this.produtos = produtos;
        this.cliente = cliente;
    }


    public Collection<Produto> getProdutos() {
        return Collections.unmodifiableCollection(this.produtos);
    }


    public LocalDateTime getCriacao() {
        return criacao;
    }


    public LocalDateTime getEncerramento() {
        return encerramento;
    }


    public double getValorTotal() {
        return valorTotal;
    }

    public Long getId() {
        return id;
    }

    public Carrinho setId(Long id) {
        this.id = id;
        return this;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Carrinho setCliente(Cliente cliente) {
        this.cliente = cliente;
        return this;
    }

    public Carrinho addProduto(Produto p) {
        this.produtos.add(p);
        calcValorTotal();
        return this;
    }

    public Carrinho removeProduto(Produto p) {
        this.produtos.remove(p);
        calcValorTotal();
        return this;
    }

    public double calcValorTotal() {
        valorTotal = 0;
        double tot = 0;
        for (Produto p : produtos) {
            var v = calcularValorTotal((ProdutoPerecivel) p);
            tot += v;
        }
        valorTotal = tot;
        return tot;
    }


    public double calcularValorTotal(ProdutoPerecivel p) {
        var hoje = LocalDate.now();
        var fabricado = p.getFabricacao();
        int dias = hoje.atStartOfDay().compareTo(fabricado);
        int prazo = p.getDiasValidade() - dias;
        double desconto = 0;
        if (prazo > 0) {
            switch (prazo) {
                case 5 -> desconto = 10;
                case 4 -> desconto = 20;
                case 3 -> desconto = 30;
                case 2 -> desconto = 40;
                case 1 -> desconto = 50;
            }
        }
        //TODO: Testar

        var vlr = p.getPreco();
        double desc = vlr * desconto / 100;
        return p.getPreco() - desc;
    }


    @Override
    public String toString() {
        return "Carrinho{" +
                "id=" + id +
                ", criacao=" + criacao +
                ", encerramento=" + encerramento +
                ", valorTotal=" + valorTotal +
                ", produtos=" + produtos +
                ", cliente=" + cliente +
                '}';
    }
}