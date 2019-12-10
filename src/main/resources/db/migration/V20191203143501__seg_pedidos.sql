CREATE TABLE seg_pedidos(
    id BIGINT IDENTITY (1,1) NOT NULL PRIMARY KEY,
    id_funcionario BIGINT NOT NULL FOREIGN KEY REFERENCES seg_funcionarios(id),
    valor_total DECIMAL (15,2),
    data DATE
);
