CREATE TABLE seg_periodo_vendas(
    id BIGINT IDENTITY(1,1) NOT NULL PRIMARY KEY,
    id_fornecedor BIGINT UNIQUE FOREIGN KEY REFERENCES seg_fornecedores(id_fornecedor),
    data_inicio DATE NOT NULL,
    data_fim DATE NOT NULL,
    data_retirada DATE NOT NULL
);
