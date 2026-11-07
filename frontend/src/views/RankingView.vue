<!-- src/views/RankingView.vue -->
<template>
  <div class="ranking-container">
    <header class="ranking-header">
      <h2>🏆 Ranking de Alunos</h2>
      <p>Acompanhe os líderes da plataforma e suas conquistas.</p>
    </header>

    <div v-if="carregando" class="loading">
      Carregando o ranking...
    </div>

    <div v-else-if="erro" class="error">
      {{ erro }}
    </div>

    <div v-else class="tabela-container">
      <table class="ranking-table">
        <thead>
        <tr>
          <th>Posição</th>
          <th>Aluno</th>
          <th>Plano</th>
          <th>Pontos</th>
          <th>Badges Conquistadas</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="(aluno, index) in ranking" :key="aluno.alunoId" :class="{'top-3': index < 3}">
          <td class="posicao">
            <span v-if="index === 0">🥇 1º</span>
            <span v-else-if="index === 1">🥈 2º</span>
            <span v-else-if="index === 2">🥉 3º</span>
            <span v-else>{{ index + 1 }}º</span>
          </td>

          <!-- Como o DTO atual tem apenas o ID, exibimos "Aluno #ID".
               No futuro, você pode adicionar o atributo 'nome' na entidade e no DTO. -->
          <td class="nome">Aluno #{{ aluno.alunoId }}</td>

          <td>
              <span :class="['badge-plano', aluno.plano.toLowerCase()]">
                {{ aluno.plano }}
              </span>
          </td>

          <td class="pontos"><strong>{{ aluno.pontos }}</strong> pts</td>

          <td class="badges">
            <span v-if="aluno.badges.length === 0" class="sem-badge">-</span>
            <span
                v-for="badge in aluno.badges"
                :key="badge"
                class="badge-chip">
                {{ obterIconeBadge(badge) }} {{ badge }}
              </span>
          </td>
        </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'

const ranking = ref([])
const carregando = ref(true)
const erro = ref(null)

// Função para buscar os dados no back-end
const buscarRanking = async () => {
  try {
    carregando.value = true
    // Substitua a URL base pela configurada no seu Axios se estiver usando (ex: api.get('/api/ranking'))
    const resposta = await fetch('http://localhost:8080/api/ranking?limite=10')

    if (!resposta.ok) {
      throw new Error('Falha ao carregar o ranking da API')
    }

    ranking.value = await resposta.json()
  } catch (err) {
    erro.value = 'Não foi possível conectar ao servidor para carregar o ranking.'
    console.error(err)
  } finally {
    carregando.value = false
  }
}

// Método auxiliar para dar mais vida visual às badges
const obterIconeBadge = (nomeBadge) => {
  const icones = {
    'Primeiro Passo': '🔰',
    'Aluno Ouro': '⭐',
    'Mestre Premium': '👑'
  }
  return icones[nomeBadge] || '🏅'
}

// Dispara a requisição assim que a página é carregada
onMounted(() => {
  buscarRanking()
})
</script>

<style scoped>
.ranking-container {
  max-width: 900px;
  margin: 2rem auto;
  padding: 0 1rem;
  font-family: sans-serif;
}

.ranking-header {
  text-align: center;
  margin-bottom: 2rem;
}

.loading, .error {
  text-align: center;
  padding: 2rem;
  font-weight: bold;
}

.error {
  color: #d32f2f;
  background: #ffebee;
  border-radius: 8px;
}

.tabela-container {
  overflow-x: auto;
  background: #ffffff;
  border-radius: 8px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.ranking-table {
  width: 100%;
  border-collapse: collapse;
  text-align: left;
}

.ranking-table th, .ranking-table td {
  padding: 1rem;
  border-bottom: 1px solid #eee;
}

.ranking-table th {
  background-color: #f8f9fa;
  font-weight: 600;
  color: #333;
}

.ranking-table tbody tr:hover {
  background-color: #f9f9f9;
}

.top-3 {
  background-color: #fffdf5; /* Destaque sutil para os primeiros colocados */
}

.posicao {
  font-weight: bold;
  font-size: 1.1rem;
}

.badge-plano {
  padding: 0.3rem 0.6rem;
  border-radius: 12px;
  font-size: 0.85rem;
  font-weight: 600;
}
.badge-plano.basico { background: #e0e0e0; color: #424242; }
.badge-plano.premium { background: #ffd700; color: #000; }

.pontos {
  color: #2196f3;
}

.badges {
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
}

.badge-chip {
  background: #e3f2fd;
  color: #1976d2;
  padding: 0.3rem 0.6rem;
  border-radius: 16px;
  font-size: 0.85rem;
  font-weight: 500;
  border: 1px solid #bbdefb;
}

.sem-badge {
  color: #9e9e9e;
}
</style>