use master
EXEC sp_rename 'seg_categorias_produtos.id_categoria_produto', 'id', 'COLUMN';
EXEC sp_rename 'seg_categorias_produtos.codigo_categoria_produto', 'codigo', 'COLUMN';
EXEC sp_rename 'seg_categorias_produtos.nome_categoria_produto', 'nome', 'COLUMN';
EXEC sp_rename 'seg_categorias_produtos.id_fornecedor_categoria_produto', 'id_fornecedor', 'COLUMN';