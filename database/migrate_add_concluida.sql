-- Script de migração para adicionar coluna concluida à tabela manutencoes
-- Execute este script se o banco de dados já existir sem a coluna concluida

USE bidesk_db;

-- Adicionar coluna concluida se não existir
ALTER TABLE manutencoes 
ADD COLUMN IF NOT EXISTS concluida BOOLEAN DEFAULT FALSE;

-- Atualizar valores existentes para FALSE (pendentes)
UPDATE manutencoes SET concluida = FALSE WHERE concluida IS NULL;

