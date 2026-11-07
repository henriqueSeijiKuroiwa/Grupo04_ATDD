<template>
  <div class="painel-container">

    <div v-if="carregando" class="loading-state">
      A carregar os dados do seu perfil...
    </div>

    <!-- O v-else garante que a tela só renderiza quando a API responder -->
    <header v-else class="painel-header sua-classe-do-cartao-branco">
      <div class="perfil-info">
        <h2>Olá, {{ aluno.nome }}!</h2>
        <div class="badges-status">
          <span :class="['badge-plano', aluno.plano.toLowerCase()]">
            Plano {{ aluno.plano }}
          </span>
          <span v-if="aluno.voucherAtivo" class="badge-voucher">
            Voucher de Projeto Ativo
          </span>
        </div>
      </div>

      <!-- Banner para usuários sem plano -->
      <div v-if="aluno.plano === 'NENHUM'" class="alerta-assinatura">
        <p>Você não possui uma assinatura ativa no momento.</p>
        <button @click="assinarPlano('BASICO')" class="btn-assinar">
          Assinar Plano Básico (Grátis)
        </button>
      </div>

      <div class="recursos-info">
        <div class="recurso-card">
          <span class="recurso-label">Créditos de Cursos:</span>
          <strong class="recurso-valor">{{ aluno.creditosCursos }}</strong>
        </div>
        <div class="recurso-card">
          <span class="recurso-label">Moedas:</span>
          <strong class="recurso-valor">{{ aluno.moedas }} 🪙</strong>
          <button
              v-if="aluno.moedas >= 3"
              class="btn-trocar"
              @click="trocarMoedas">
            Trocar por Crédito
          </button>
        </div>
      </div>
    </header>

    <!-- Seção de Cursos -->
    <section class="cursos-section">
      <h3>Cursos Disponíveis</h3>

      <div class="cursos-grid">
        <div class="curso-card" v-for="curso in cursos" :key="curso.id">
          <h4>{{ curso.titulo }}</h4>
          <p>{{ curso.descricao }}</p>

          <!-- Tag visual (Azul ou Amarela) -->
          <span :class="['curso-tag', curso.planoRequerido === 'PREMIUM' ? 'tag-premium' : 'tag-basico']">
            {{ curso.planoRequerido }}
          </span>

          <!-- Botão Inteligente de Matrícula -->
          <button
              class="btn-matricular"
              @click="matricular(curso)"
              :disabled="aluno.plano === 'NENHUM' || (curso.planoRequerido === 'PREMIUM' && aluno.creditosCursos <= 0)">

            {{ curso.planoRequerido === 'BASICO' ? 'Matricular (Grátis)' : 'Matricular (Usa 1 Crédito)' }}
          </button>
        </div>
      </div>
    </section>

  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'

// Estado reativo inicializado vazio
const aluno = ref({
  nome: '',
  plano: 'BASICO',
  creditosCursos: 0,
  moedas: 0,
  voucherAtivo: false
})
const cursos = ref([
  { id: 1, titulo: 'Java Spring Boot Avançado', descricao: 'Arquitetura modular, testes e REST APIs com Java.', planoRequerido: 'PREMIUM' },
  { id: 2, titulo: 'Vue.js 3 com Vite', descricao: 'Criação de SPAs modernas usando Composition API.', planoRequerido: 'BASICO' },
  { id: 3, titulo: 'CI/CD com Jenkins e Docker', descricao: 'Pipelines automatizados e orquestração de containers.', planoRequerido: 'PREMIUM' },
  { id: 4, titulo: 'Game Design e Mecânicas de Tempo', descricao: 'Desenvolvimento de jogos inspirados em clássicos de culinária e NES.', planoRequerido: 'BASICO' },
  { id: 5, titulo: 'Eletrônica e Sistemas Embarcados', descricao: 'Prototipagem de circuitos e computadores de 8-bits.', planoRequerido: 'PREMIUM' }
])
const carregando = ref(true)

// Função que liga o Front-end ao Back-end
const carregarDadosPerfil = async () => {
  // Vai buscar o ID que guardámos no ecrã de Login (por defeito usa o 1 se não encontrar)
  const id = localStorage.getItem('alunoId') || 1

  try {
    const resposta = await fetch(`http://localhost:8080/api/alunos/${id}`)
    if (!resposta.ok) throw new Error('Falha na comunicação com a API')

    // Atualiza a variável reativa, o que faz o Vue alterar o HTML automaticamente
    aluno.value = await resposta.json()
  } catch (erro) {
    console.error('Erro ao obter perfil:', erro)
    alert('Erro ao carregar os dados. O servidor Spring Boot está a correr?')
  } finally {
    carregando.value = false
  }
}

const assinarPlano = async (novoPlano) => {
  const id = localStorage.getItem('alunoId') || 1

  try {
    // Fazemos um PUT passando o novoPlano como parâmetro na URL
    const resposta = await fetch(`http://localhost:8080/api/alunos/${id}/assinatura?novoPlano=${novoPlano}`, {
      method: 'PUT'
    })

    if (!resposta.ok) throw new Error('Falha ao processar a assinatura.')

    // Atualiza a tela automaticamente com os novos dados devolvidos pelo banco
    aluno.value = await resposta.json()
    alert(`Parabéns! Sua assinatura ${novoPlano} foi ativada com sucesso.`)

  } catch (erro) {
    console.error('Erro na assinatura:', erro)
    alert('Não foi possível processar a assinatura agora.')
  }
}

const trocarMoedas = () => {
  // Logica base local (no futuro, isto também chamará um endpoint PUT)
  if (aluno.value.moedas >= 1) {
    aluno.value.moedas -= 1
    aluno.value.creditosCursos += 1
    alert('Troca realizada com sucesso!')
  }
}

const matricular = (curso) => {
  if (curso.planoRequerido === 'PREMIUM') {
    aluno.value.creditosCursos -= 1
  }
  alert(`Matrícula realizada com sucesso no curso:\n${curso.titulo}`)
}

// Quando o ecrã for montado, dispara o pedido de dados
onMounted(() => {
  carregarDadosPerfil()
})
</script>

<style scoped>
/* Adicione estas regras para garantir que o texto não desaparece com a remoção do Dark Mode */
.sua-classe-do-cartao-branco {
  background-color: #ffffff;
  color: #333333;
  display: flex;
  justify-content: space-between;
  padding: 1.5rem;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
  margin-bottom: 2rem;
}

.loading-state {
  text-align: center;
  padding: 2rem;
  font-size: 1.2rem;
  color: #666;
}

.recurso-card {
  color: #333333;
  /* ... resto do seu CSS para os cartões ... */
}
/* ... resto dos seus estilos ... */

.alerta-assinatura {
  background-color: #fff3cd;
  color: #856404;
  padding: 1rem;
  border-radius: 8px;
  border: 1px solid #ffeeba;
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1.5rem;
  width: 100%;
}

.btn-assinar {
  background-color: #28a745;
  color: white;
  border: none;
  padding: 0.6rem 1.2rem;
  border-radius: 4px;
  font-weight: bold;
  cursor: pointer;
  transition: 0.2s;
}

.btn-assinar:hover {
  background-color: #218838;
}

.curso-tag {
  display: inline-block;
  padding: 0.2rem 0.5rem;
  font-size: 0.75rem;
  font-weight: bold;
  border-radius: 4px;
  margin-bottom: 0.5rem;
}
.tag-basico {
  background-color: #e3f2fd;
  color: #1976d2;
}
.tag-premium {
  background-color: #fff3cd;
  color: #856404;
}

.cursos-section {
  margin-top: 2rem;
}

.cursos-section h3 {
  margin-bottom: 1rem;
  color: #333;
}

.cursos-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 1.5rem;
}

.curso-card {
  background-color: white;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  padding: 1.5rem;
  display: flex;
  flex-direction: column;
  box-shadow: 0 2px 4px rgba(0,0,0,0.05);
}

.curso-card h4 {
  color: #333;
  margin-bottom: 0.5rem;
  font-weight: bold;
}

.curso-card p {
  color: #666;
  font-size: 0.9rem;
  margin-bottom: 1rem;
  flex-grow: 1; /* Empurra o botão e a tag para o fundo */
}

.btn-matricular {
  margin-top: 0.5rem;
  padding: 0.8rem;
  border: none;
  border-radius: 4px;
  background-color: #2196f3;
  color: white;
  font-weight: bold;
  cursor: pointer;
  transition: background-color 0.2s;
}

.btn-matricular:hover:not(:disabled) {
  background-color: #1976d2;
}

.btn-matricular:disabled {
  background-color: #e0e0e0;
  color: #888;
  cursor: not-allowed;
}
</style>