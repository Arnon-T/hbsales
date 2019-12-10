ALTER TABLE seg_fornecedores ADD CONSTRAINT UQ_cnpj UNIQUE (cnpj);
ALTER TABLE seg_fornecedores ADD CONSTRAINT NN_cnpj
CHECK (cnpj IS NOT NULL)
GO