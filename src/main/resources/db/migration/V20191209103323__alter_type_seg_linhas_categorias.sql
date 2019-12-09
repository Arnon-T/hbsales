ALTER TABLE seg_linhas_categorias DROP CONSTRAINT AK_linha_categoria;
ALTER TABLE seg_linhas_categorias
ALTER COLUMN nome VARCHAR(50);

