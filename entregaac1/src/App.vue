<template>
  <div class="container">
    <header>
      <h1>🎓 Educação Continuada Gamificada</h1>
    </header>

    <!-- SESSÃO: Cadastro de Aluno -->
    <section class="card">
      <h2>Novo Aluno</h2>
      <div class="form-group">
        <input v-model="novoAluno" placeholder="Digite o nome do aluno..." />
        <button class="btn-primary" @click="cadastrarAluno">Cadastrar</button>
      </div>
    </section>

    <!-- SESSÃO: Lista de Alunos e Gestão de Cursos -->
    <section class="alunos-list">
      <h2>Alunos Matriculados</h2>
      <div v-if="alunos.length === 0" class="empty-state">Nenhum aluno cadastrado ainda.</div>

      <div v-for="aluno in alunos" :key="aluno.id" class="aluno-card">
        <div class="aluno-header">
          <h3>{{ aluno.nome }}</h3>
          <div class="badges">
            <span class="badge extra">⭐ Cursos Extras: {{ aluno.cursosExtras }}</span>
            <span :class="['badge', aluno.assinaturaAtiva ? 'ativo' : 'inativo']">
              {{ aluno.assinaturaAtiva ? 'Assinatura Ativa' : 'Assinatura Inativa' }}
            </span>
          </div>
        </div>

        <!-- Formulário para adicionar curso ao aluno específico -->
        <div class="curso-form">
          <input v-model="formCursos[aluno.id].nome" placeholder="Nome do Curso" />
          <input v-model="formCursos[aluno.id].conteudo" placeholder="Conteúdo (ex: SOLID)" />
          <button class="btn-secondary" @click="adicionarCurso(aluno.id)">+ Adicionar Curso</button>
        </div>

        <!-- Lista de Cursos do Aluno -->
        <div v-if="aluno.cursos && aluno.cursos.length > 0" class="cursos-lista">
          <h4>Meus Cursos</h4>
          <ul>
            <li v-for="curso in aluno.cursos" :key="curso.id" :class="['curso-item', curso.status]">
              <div class="curso-info">
                <strong>{{ curso.nome }}</strong> - {{ curso.conteudo }}
                <span v-if="curso.status === 'FINALIZADO'" class="nota">(Nota: {{ curso.media }})</span>
              </div>

              <!-- Botão para finalizar curso -->
              <div v-if="curso.status !== 'FINALIZADO'" class="curso-acoes">
                <input type="number" v-model="formNotas[curso.id]" placeholder="Nota" min="0" max="10" step="0.1" />
                <button class="btn-success" @click="finalizarCurso(aluno.id, curso.id)">Finalizar</button>
              </div>
              <span v-else class="status-finalizado">✅ Concluído</span>
            </li>
          </ul>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';

const api = 'http://localhost:8080/api/alunos';
const alunos = ref([]);
const novoAluno = ref('');

// Objetos reativos para guardar os dados digitados nos formulários individuais
const formCursos = ref({});
const formNotas = ref({});

const inicializarFormularios = (listaAlunos) => {
  listaAlunos.forEach(aluno => {
    if (!formCursos.value[aluno.id]) {
      formCursos.value[aluno.id] = { nome: '', conteudo: '' };
    }
    if (aluno.cursos) {
      aluno.cursos.forEach(curso => {
        if (!formNotas.value[curso.id]) formNotas.value[curso.id] = '';
      });
    }
  });
};

const carregarAlunos = async () => {
  try {
    const resposta = await axios.get(api);
    alunos.value = resposta.data;
    inicializarFormularios(alunos.value);
  } catch (erro) {
    console.error("Erro ao buscar alunos:", erro);
  }
};

const cadastrarAluno = async () => {
  if (!novoAluno.value.trim()) return;
  try {
    await axios.post(api, { nome: novoAluno.value });
    novoAluno.value = '';
    carregarAlunos();
  } catch (erro) {
    console.error("Erro ao cadastrar aluno:", erro);
  }
};

const adicionarCurso = async (alunoId) => {
  const cursoDados = formCursos.value[alunoId];
  if (!cursoDados.nome.trim() || !cursoDados.conteudo.trim()) return;

  try {
    await axios.post(`${api}/${alunoId}/cursos`, cursoDados);
    formCursos.value[alunoId] = { nome: '', conteudo: '' }; // Limpa o form
    carregarAlunos();
  } catch (erro) {
    console.error("Erro ao adicionar curso:", erro);
  }
};

const finalizarCurso = async (alunoId, cursoId) => {
  const nota = formNotas.value[cursoId];
  if (nota === '' || nota < 0 || nota > 10) {
    alert("Insira uma nota válida entre 0 e 10.");
    return;
  }

  try {
    // Passa a nota via Query Parameter (?media=7.5) conforme configurado no Spring
    await axios.put(`${api}/${alunoId}/cursos/${cursoId}/finalizar?media=${nota}`);
    carregarAlunos();
  } catch (erro) {
    console.error("Erro ao finalizar curso:", erro);
  }
};

onMounted(() => {
  carregarAlunos();
});
</script>

<style scoped>
/* Estilos modernizados para dar cara de projeto final */
.container {
  max-width: 800px;
  margin: 0 auto;
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  color: #333;
  padding: 20px;
}
header h1 { text-align: center; color: #2c3e50; }
.card { background: #f8f9fa; padding: 20px; border-radius: 8px; margin-bottom: 30px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }
.form-group { display: flex; gap: 10px; }
input { flex: 1; padding: 10px; border: 1px solid #ccc; border-radius: 4px; }
button { padding: 10px 15px; border: none; border-radius: 4px; cursor: pointer; font-weight: bold; color: white; }
.btn-primary { background: #3498db; }
.btn-primary:hover { background: #2980b9; }
.btn-secondary { background: #95a5a6; padding: 8px 12px; }
.btn-success { background: #2ecc71; padding: 6px 12px; }

.aluno-card { background: white; border: 1px solid #e0e0e0; border-radius: 8px; padding: 20px; margin-bottom: 20px; box-shadow: 0 2px 4px rgba(0,0,0,0.05); }
.aluno-header { display: flex; justify-content: space-between; align-items: center; border-bottom: 1px solid #eee; padding-bottom: 10px; margin-bottom: 15px; }
.aluno-header h3 { margin: 0; color: #2c3e50; }
.badges { display: flex; gap: 10px; }
.badge { padding: 5px 10px; border-radius: 12px; font-size: 0.85em; font-weight: bold; }
.badge.extra { background: #f1c40f; color: #8a6d3b; }
.badge.ativo { background: #e8f8f5; color: #1abc9c; }
.badge.inativo { background: #fdedec; color: #e74c3c; }

.curso-form { display: flex; gap: 10px; margin-bottom: 15px; }
.cursos-lista ul { list-style: none; padding: 0; }
.curso-item { display: flex; justify-content: space-between; align-items: center; background: #f9f9f9; padding: 10px; border-radius: 4px; margin-bottom: 8px; border-left: 4px solid #3498db; }
.curso-item.FINALIZADO { border-left-color: #2ecc71; background: #f4fdf8; }
.curso-acoes { display: flex; gap: 10px; }
.curso-acoes input { width: 70px; padding: 5px; }
.status-finalizado { color: #2ecc71; font-weight: bold; }
.nota { color: #555; font-size: 0.9em; margin-left: 10px; }
</style>