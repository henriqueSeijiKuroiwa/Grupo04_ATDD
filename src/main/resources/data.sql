-- ==========================================
-- 1. Inserção de Cursos
-- ==========================================
INSERT INTO tb_curso (id, titulo, descricao, plano_requerido) VALUES
                                                                  (1, 'Java Spring Boot Avançado', 'Arquitetura modular, testes e REST APIs com Java.', 'PREMIUM'),
                                                                  (2, 'Vue.js 3 com Vite', 'Criação de SPAs modernas usando Composition API.', 'BASICO'),
                                                                  (3, 'CI/CD com Jenkins e Docker', 'Pipelines automatizados e orquestração de containers.', 'PREMIUM'),
                                                                  (4, 'Game Design e Mecânicas de Tempo', 'Desenvolvimento de jogos inspirados em clássicos de culinária e NES.', 'BASICO'),
                                                                  (5, 'Eletrônica e Sistemas Embarcados', 'Prototipagem de circuitos e computadores de 8-bits.', 'PREMIUM');

ALTER SEQUENCE tb_curso_id_seq RESTART WITH 6;

-- ==========================================
-- 2. Inserção de Alunos (Cenários de Teste)
-- ==========================================
-- Aluno 1: Sem plano (Para testar o botão de assinar)
INSERT INTO tb_aluno (id, nome, plano, cursos_concluidos, creditos_cursos, moedas, voucher_ativo, pontos_ranking) VALUES
    (1, 'João Silva', 'NENHUM', 0, 0, 0, false, 0);

-- Aluno 2: Quase Premium
INSERT INTO tb_aluno (id, nome, plano, cursos_concluidos, creditos_cursos, moedas, voucher_ativo, pontos_ranking) VALUES
    (2, 'Maria Souza', 'BASICO', 11, 0, 0, false, 0);

-- Aluno 3: Premium com saldo
INSERT INTO tb_aluno (id, nome, plano, cursos_concluidos, creditos_cursos, moedas, voucher_ativo, pontos_ranking) VALUES
    (3, 'Henrique', 'PREMIUM', 15, 1, 5, true, 0);

ALTER SEQUENCE tb_aluno_id_seq RESTART WITH 4;

-- ==========================================
-- 3. Inserção de Matrículas Iniciais
-- ==========================================
-- Matrícula 1: Aluno 2 cursando Spring Boot (Pronto para ser finalizado via API)
INSERT INTO tb_matricula (id, aluno_id, curso_id, status, nota_final) VALUES
    (1, 2, 1, 'EM_ANDAMENTO', null);

-- Matrícula 2: Aluno 1 cursando Vue.js
INSERT INTO tb_matricula (id, aluno_id, curso_id, status, nota_final) VALUES
    (2, 1, 2, 'EM_ANDAMENTO', null);

ALTER SEQUENCE tb_matricula_id_seq RESTART WITH 3;