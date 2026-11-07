<template>
  <div class="aula-container">
    <header class="aula-header">
      <button @click="voltar" class="btn-voltar">← Voltar aos Cursos</button>
      <h2>Módulo de Estudo (Curso ID: {{ cursoId }})</h2>
    </header>

    <main class="video-placeholder">
      <div class="player-mock">
        <span class="icone-play">▶</span>
        <p>O conteúdo da aula será exibido aqui.</p>
        <small>(Protótipo sem conteúdo multimídia)</small>
      </div>
    </main>

    <footer class="aula-footer">
      <p>Já concluiu o conteúdo? Faça a avaliação para garantir seus pontos e badges!</p>
      <button @click="irParaAvaliacao" class="btn-avaliar">
        Ir para a Avaliação Final
      </button>
    </footer>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()

// Captura o ID do curso passado na URL (/aula/101)
const cursoId = computed(() => route.params.id)

const voltar = () => {
  router.push('/cursos')
}

const irParaAvaliacao = () => {
  // Para testar o endpoint de finalizar curso gerado anteriormente,
  // estamos simulando que o aluno está na matrícula de ID 1.
  router.push('/avaliacao/1')
}
</script>

<style scoped>
.aula-container {
  max-width: 800px;
  margin: 2rem auto;
  padding: 1rem;
}
.aula-header {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  margin-bottom: 2rem;
}
.btn-voltar {
  background: none;
  border: none;
  color: #1976d2;
  cursor: pointer;
  margin-bottom: 1rem;
  font-weight: bold;
}
.video-placeholder {
  background: #000;
  color: #fff;
  aspect-ratio: 16 / 9;
  display: flex;
  justify-content: center;
  align-items: center;
  border-radius: 8px;
  margin-bottom: 2rem;
}
.player-mock {
  text-align: center;
}
.icone-play {
  font-size: 3rem;
  display: block;
  margin-bottom: 1rem;
}
.aula-footer {
  text-align: center;
  background: #f8f9fa;
  padding: 1.5rem;
  border-radius: 8px;
  border: 1px solid #e0e0e0;
}
.btn-avaliar {
  margin-top: 1rem;
  padding: 1rem 2rem;
  background: #4caf50;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 1.1rem;
  font-weight: bold;
  cursor: pointer;
}
.btn-avaliar:hover {
  background: #388e3c;
}
</style>