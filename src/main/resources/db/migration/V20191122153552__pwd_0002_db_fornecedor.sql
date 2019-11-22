create table seg_fornecedor
(
    id    BIGINT IDENTITY (1, 1) NOT NULL,
    razao_social_fornecedor VARCHAR(100)           NOT NULL,
    cnpj_fornecedor VARCHAR(20)           NOT NULL,
    nome_fantasia_fornecedor VARCHAR(100)           NOT NULL,
    endereco_fornecedor VARCHAR(255)           NOT NULL,
    telefone_fornecedor  INT            NOT NULL,
    email_fornecedor  VARCHAR(20)            NOT NULL
);
