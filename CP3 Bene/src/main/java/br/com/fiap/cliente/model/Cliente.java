package br.com.fiap.cliente.model;

import br.com.fiap.pessoa.model.Pessoa;
import jakarta.persistence.*;

@Entity
@Table(name = "TB_CLIENTE")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_CLIENTE")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "ID_PESSOA", referencedColumnName = "ID_PESSOA",
            foreignKey = @ForeignKey(name = "FK_CLIENTE_PESSOA", value = ConstraintMode.CONSTRAINT))
    private Pessoa pessoa;

    public Cliente() {
    }

    public Cliente(Long id, Pessoa pessoa) {
        this.id = id;
        this.pessoa = pessoa;
    }

    public Long getId() {
        return id;
    }

    public Cliente setId(Long id) {
        this.id = id;
        return this;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public Cliente setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
        return this;
    }


    @Override
    public String toString() {
        return "Cliente{" +
                "id=" + id +
                ", pessoa=" + pessoa +
                '}';
    }
}