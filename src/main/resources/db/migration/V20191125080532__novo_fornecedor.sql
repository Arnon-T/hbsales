DROP TABLE seg_fornecedor;
create table seg_fornecedores
(
    id_fornecedor    BIGINT IDENTITY (1, 1) NOT NULL PRIMARY KEY,
    razao_social_fornecedor VARCHAR(100)           NOT NULL,
    cnpj_fornecedor VARCHAR(20)           NOT NULL,
    nome_fantasia_fornecedor VARCHAR(100)           NOT NULL,
    endereco_fornecedor VARCHAR(255)           NOT NULL,
    telefone_fornecedor  INT            NOT NULL,
    email_fornecedor  VARCHAR(20)            NOT NULL
);

CREATE TABLE seg_categorias_produtos(
    id_categoria_produto BIGINT IDENTITY (1,1) NOT NULL PRIMARY KEY,
    nome_categoria_produto VARCHAR (100) NOT NULL,
    id_fornecedor_categoria_produto BIGINT FOREIGN KEY REFERENCES seg_fornecedores(id_fornecedor)
);
