<template>
  <div class="login-container">
    <div class="login-card">
      <h2>Acesso à Plataforma</h2>
      <p>Faça login para acessar seus cursos e ranking.</p>

      <div class="form-group">
        <label for="email">Email</label>
        <input
            id="email"
            v-model="email"
            type="email"
            placeholder="aluno@facens.br"
        />
      </div>

      <div class="form-group">
        <label for="senha">Senha</label>
        <input
            id="senha"
            v-model="senha"
            type="password"
            placeholder="••••••••"
        />
      </div>

      <button @click="entrar" class="btn-login">Entrar</button>

      <p class="mock-aviso">
        *Protótipo: O acesso está liberado. Clique em "Entrar" para continuar.
      </p>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const email = ref('')
const senha = ref('')
const router = useRouter()

const entrar = () => {
  localStorage.setItem('alunoId', '1')

  router.push('/cursos')
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f4f6f9;
}
.login-card {
  background: white;
  padding: 2.5rem;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
  width: 100%;
  max-width: 400px;
}
.form-group {
  margin-bottom: 1.5rem;
  display: flex;
  flex-direction: column;
}
.form-group label {
  margin-bottom: 0.5rem;
  font-weight: 500;
}
.form-group input {
  padding: 0.8rem;
  border: 1px solid #ccc;
  border-radius: 4px;
}
.btn-login {
  width: 100%;
  padding: 1rem;
  background-color: #2196f3;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 1rem;
  font-weight: bold;
  cursor: pointer;
}
.btn-login:hover {
  background-color: #1976d2;
}
.mock-aviso {
  margin-top: 1rem;
  font-size: 0.85rem;
  color: #757575;
  text-align: center;
}
</style>