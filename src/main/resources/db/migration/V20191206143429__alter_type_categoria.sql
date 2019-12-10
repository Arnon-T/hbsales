use master
ALTER TABLE seg_categorias_produtos DROP CONSTRAINT AK_categoria_fornecedor;
ALTER TABLE seg_categorias_produtos
ALTER COLUMN codigo VARCHAR(10);
ALTER TABLE seg_categorias_produtos
ALTER COLUMN nome VARCHAR(50);
