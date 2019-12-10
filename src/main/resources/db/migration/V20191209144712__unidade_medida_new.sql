DROP TABLE enum_unidade_medida;
CREATE TABLE unidade_medida(
    id BIGINT IDENTITY(1,1) NOT NULL PRIMARY KEY,
    unidade_medida VARCHAR(10) not null
);
