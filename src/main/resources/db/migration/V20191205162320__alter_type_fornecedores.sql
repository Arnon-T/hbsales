ALTER TABLE seg_fornecedores
ALTER COLUMN endereco VARCHAR(100);
ALTER TABLE seg_fornecedores
ALTER COLUMN telefone VARCHAR(12);
ALTER TABLE seg_fornecedores
ALTER COLUMN email VARCHAR(20);
ALTER TABLE seg_fornecedores DROP CONSTRAINT UQ__seg_forn__B3C9C5897724E96D;
ALTER TABLE seg_fornecedores
ALTER COLUMN cnpj VARCHAR(14);
