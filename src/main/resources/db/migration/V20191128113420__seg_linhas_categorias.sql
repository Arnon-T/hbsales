CREATE TABLE seg_linhas_categorias(
    id_linha_categoria BIGINT IDENTITY(1,1) NOT NULL PRIMARY KEY,
    id_categoria_produto BIGINT FOREIGN KEY REFERENCES seg_categorias_produtos(id_categoria_produto),
    nome_linha_categoria VARCHAR (100) NOT NULL
);
