<!-- src/views/AvaliacaoView.vue -->
<template>
  <div class="avaliacao-container">
    <h2>Avaliação Final do Curso</h2>
    <p>Selecione a nota obtida pelo aluno neste protótipo:</p>

    <div class="form-group">
      <label for="nota">Nota Final:</label>
      <select id="nota" v-model="notaSelecionada">
        <option disabled value="">Escolha uma nota...</option>
        <option value="5.0">5.0 (Abaixo da média)</option>
        <option value="7.5">7.5 (Acima da média - Ganha 3 cursos)</option>
        <option value="10.0">10.0 (Nota Máxima - Ganha 3 cursos)</option>
      </select>
    </div>

    <button @click="enviarAvaliacao" :disabled="!notaSelecionada">
      Finalizar Curso
    </button>

    <p v-if="mensagem" class="feedback">{{ mensagem }}</p>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRoute } from 'vue-router'
// import api from '../api' // Instância do Axios

const route = useRoute()
const notaSelecionada = ref('')
const mensagem = ref('')

const enviarAvaliacao = async () => {
  try {
    const payload = { nota: parseFloat(notaSelecionada.value) }

    // Chamada simulada para o back-end
    // await api.post(`/api/matriculas/${route.params.matriculaId}/finalizar`, payload)

    mensagem.value = `Curso finalizado com sucesso! Nota: ${notaSelecionada.value}`

    if (parseFloat(notaSelecionada.value) > 7.0) {
      mensagem.value += ' Parabéns, você ganhou 3 novos créditos de curso!'
    }
  } catch (error) {
    mensagem.value = 'Erro ao processar a avaliação.'
  }
}
</script>

<style scoped>
.avaliacao-container {
  max-width: 400px;
  margin: 2rem auto;
  padding: 1.5rem;
  border: 1px solid #ccc;
  border-radius: 8px;
}
.form-group {
  margin-bottom: 1rem;
}
select, button {
  width: 100%;
  padding: 0.5rem;
  margin-top: 0.5rem;
}
.feedback {
  margin-top: 1rem;
  color: green;
  font-weight: bold;
}
</style>