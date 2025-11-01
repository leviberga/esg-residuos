-- V3__insert_sample_data.sql
-- Inserção de dados de exemplo nas tabelas 'esg_'

-- Insere um usuário admin (senha 'admin')
INSERT INTO esg_usuarios (id, email, senha, role)
VALUES (esg_seq_usuario.NEXTVAL, 'admin@coleta.com', 'admin', 'ROLE_ADMIN');

-- Insere alguns pontos de coleta
INSERT INTO esg_pontos_coleta (id, nome, endereco, cidade, tipo, volume_maximo)
VALUES (esg_seq_ponto_coleta.NEXTVAL, 'Ponto Coleta Centro', 'Rua Principal, 100', 'São Paulo', 'plástico', 50.0);

INSERT INTO esg_pontos_coleta (id, nome, endereco, cidade, tipo, volume_maximo)
VALUES (esg_seq_ponto_coleta.NEXTVAL, 'EcoPonto Vila Mariana', 'Av. Paulista, 500', 'São Paulo', 'vidro', 25.0);

-- Insere um registro de coleta para o primeiro ponto (ID=1)
INSERT INTO esg_registros_coleta (id, ponto_coleta_id, volume_coletado, data_coleta)
VALUES (esg_seq_registro_coleta.NEXTVAL, 1, 15.5, CURRENT_TIMESTAMP);

-- Confirma as inserções
COMMIT;