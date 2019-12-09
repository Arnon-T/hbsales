use master
EXEC sp_rename 'seg_linhas_categorias.id_linha_categoria', 'id', 'COLUMN';
EXEC sp_rename 'seg_linhas_categorias.nome_linha_categoria', 'nome', 'COLUMN';