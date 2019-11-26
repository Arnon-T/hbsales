create table seg_fornecedores
(
    id_fornecedor BIGINT IDENTITY (1, 1) NOT NULL PRIMARY KEY,
    razao_social_fornecedor VARCHAR(100) NOT NULL,
    cnpj_fornecedor VARCHAR(20) NOT NULL UNIQUE,
    nome_fantasia_fornecedor VARCHAR(100) NOT NULL,
    endereco_fornecedor VARCHAR(255) NOT NULL,
    telefone_fornecedor  INT NOT NULL,
    email_fornecedor  VARCHAR(20) NOT NULL
);
