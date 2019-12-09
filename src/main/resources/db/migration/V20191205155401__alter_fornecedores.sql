use master
EXEC sp_rename 'seg_fornecedores.id_fornecedor', 'id', 'COLUMN';
EXEC sp_rename 'seg_fornecedores.razao_social_fornecedor', 'razao_social', 'COLUMN';
EXEC sp_rename 'seg_fornecedores.cnpj_fornecedor', 'cnpj', 'COLUMN';
EXEC sp_rename 'seg_fornecedores.endereco_fornecedor', 'endereco', 'COLUMN';
EXEC sp_rename 'seg_fornecedores.telefone_fornecedor', 'telefone', 'COLUMN';
EXEC sp_rename 'seg_fornecedores.email_fornecedor', 'email', 'COLUMN';
