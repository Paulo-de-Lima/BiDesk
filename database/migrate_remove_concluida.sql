-- Script de migração para remover coluna concluida da tabela manutencoes
-- Execute este script se o banco de dados já existir com a coluna concluida

USE bidesk_db;

-- Remover coluna concluida se existir
ALTER TABLE manutencoes 
DROP COLUMN IF EXISTS concluida;

