ALTER TABLE seg_fornecedores ADD CONSTRAINT NN_endereco
CHECK (endereco IS NOT NULL)
GO
ALTER TABLE seg_fornecedores ADD CONSTRAINT NN_telefone
CHECK (telefone IS NOT NULL)
GO
ALTER TABLE seg_fornecedores ADD CONSTRAINT NN_email
CHECK (email IS NOT NULL)
GO

ALTER TABLE seg_categorias_produtos ADD CONSTRAINT NN_codigo_cat CHECK (codigo IS NOT NULL) GO
ALTER TABLE seg_categorias_produtos ADD CONSTRAINT NN_nome_cat CHECK (codigo IS NOT NULL) GO
ALTER TABLE seg_categorias_produtos ADD CONSTRAINT NN_fornecedor CHECK (id_fornecedor IS NOT NULL) GO
ALTER TABLE seg_categorias_produtos ADD CONSTRAINT UQ_codigo_cat UNIQUE (codigo);

ALTER TABLE seg_linhas_categorias ADD CONSTRAINT NN_codigo_linha CHECK (codigo IS NOT NULL) GO
ALTER TABLE seg_linhas_categorias ADD CONSTRAINT NN_nome_linha CHECK (codigo IS NOT NULL) GO
ALTER TABLE seg_linhas_categorias ADD CONSTRAINT UQ_codigo_linha UNIQUE (codigo);


ALTER TABLE seg_produtos ADD CONSTRAINT NN_nome_prod CHECK (nome IS NOT NULL) GO
ALTER TABLE seg_produtos ADD CONSTRAINT NN_codigo_prod CHECK (codigo IS NOT NULL) GO
ALTER TABLE seg_produtos ADD CONSTRAINT NN_linha_prod CHECK (id_linha_categoria IS NOT NULL) GO
ALTER TABLE seg_produtos ADD CONSTRAINT NN_unidades_caixa CHECK (unidades_caixa IS NOT NULL) GO
ALTER TABLE seg_produtos ADD CONSTRAINT NN_peso_unidade CHECK (peso_unidade IS NOT NULL) GO
ALTER TABLE seg_produtos ADD CONSTRAINT NN_data_validade CHECK (data_validade IS NOT NULL) GO
ALTER TABLE seg_produtos ADD CONSTRAINT NN_unidade_medida CHECK (unidade_medida IS NOT NULL) GO
ALTER TABLE seg_produtos ADD CONSTRAINT UQ_codigo_prod UNIQUE (codigo);

ALTER TABLE seg_categorias_produtos
ADD CONSTRAINT AK_categoria_fornecedor UNIQUE(codigo, id_fornecedor);

ALTER TABLE seg_linhas_categorias
ADD CONSTRAINT AK_linha_categoria UNIQUE(codigo, id_categoria_produto);

ALTER TABLE seg_produtos
ADD CONSTRAINT AK_produto_linha UNIQUE(codigo, id_linha_categoria);