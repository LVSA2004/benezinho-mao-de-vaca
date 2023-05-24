package br.com.fiap.produto.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("PNP")
public class ProdutoNaoPerecivel extends Produto {

}
