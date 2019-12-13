CREATE TABLE seg_produtos(
    id BIGINT IDENTITY(1,1) NOT NULL PRIMARY KEY,
    codigo BIGINT NOT NULL UNIQUE,
    nome VARCHAR (100) NOT NULL,
    preco decimal (7,2) NOT NULL,
    id_linha_categoria BIGINT FOREIGN KEY REFERENCES seg_linhas_categorias(id_linha_categoria),
    unidades_caixa decimal (6,2) NOT NULL,
    peso_unidade decimal (6,3) NOT NULL,
    data_validade DATE NOT NULL
);
