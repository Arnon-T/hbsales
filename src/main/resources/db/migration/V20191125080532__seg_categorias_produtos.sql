CREATE TABLE seg_categorias_produtos(
    id_categoria_produto BIGINT IDENTITY (1,1) NOT NULL PRIMARY KEY,
    codigo_categoria_produto BIGINT NOT NULL,
    nome_categoria_produto VARCHAR (100) NOT NULL,
    id_fornecedor_categoria_produto BIGINT FOREIGN KEY REFERENCES seg_fornecedores(id_fornecedor)
);
