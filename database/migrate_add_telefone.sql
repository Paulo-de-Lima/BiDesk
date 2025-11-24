-- database/migrate_add_telefone.sql
USE bidesk_db;

-- Adicionar coluna telefone se não existir (permite NULL para não quebrar dados existentes)
ALTER TABLE clientes 
ADD COLUMN IF NOT EXISTS telefone VARCHAR(20) NULL AFTER cidade;