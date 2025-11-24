-- Migração: Corrigir tabela despesas_materiais
USE bidesk_db;

-- Remover a tabela se existir com erro de sintaxe
DROP TABLE IF EXISTS despesas_materiais;

-- Recriar a tabela corretamente
CREATE TABLE IF NOT EXISTS despesas_materiais (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    despesa_id INT NULL,
    gasto DECIMAL(10, 2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);




