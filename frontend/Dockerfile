# 1. Utiliza uma imagem oficial e leve do Node.js
FROM node:20-alpine

# 2. Define a pasta de trabalho dentro do contentor
WORKDIR /app

# 3. Copia apenas os ficheiros de dependências primeiro
# (Isto otimiza a cache do Docker, evitando reinstalar tudo se o código mudar)
COPY package*.json ./

# 4. Instala as dependências do projeto
RUN npm install

# 5. Copia o resto dos ficheiros do front-end para o contentor
COPY . .

# 6. Expõe a porta padrão que o Vite utiliza
EXPOSE 5173

# 7. Inicia o servidor de desenvolvimento.
# O parâmetro "--host" é obrigatório no Docker para que o Vite
# fique acessível externamente (fora do contentor).
CMD ["npm", "run", "dev", "--", "--host"]