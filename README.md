# AcesseAqui ♿

Ferramenta de relatório de problemas de acessibilidade urbana, desenvolvida em Java com interface Swing moderna e persistência de dados.

## 🚀 Funcionalidades
- **Interface Moderna:** Uso de `JTabbedPane` e tema `Nimbus` para uma experiência fluida.
- **Arquitetura MVC:** Separação clara entre Modelos, Visão e Serviços.
- **Persistência de Dados:** Salvamento automático de relatos em arquivo binário (`.dat`) via Serialização.
- **Comunicação em Rede:** Envio de notificações via Sockets TCP para servidores remotos.
- **Validação de Dados:** Filtros de input (Regex, Spinners) e tratamento de erros em tempo real.
- **Design de Cards:** Lista de notificações visualmente agradável com suporte a imagens.

## 🛠️ Tecnologias Utilizadas
- Java SE (Swing, AWT, Networking, I/O)
- Padrão MVC
- Sockets TCP

## 📦 Como Executar
1. Certifique-se de ter o JDK 11 ou superior instalado.
2. Clone o repositório ou baixe os arquivos.
3. No terminal, execute:
   ```bash
   javac -d bin src/model/*.java src/service/*.java src/view/*.java
   java -cp bin view.AppInterface
   ```

## 📂 Estrutura do Projeto
- `src/model`: Classes de domínio (Usuario, Notificacao, Pessoa).
- `src/view`: Interface gráfica e renderizadores.
- `src/service`: Lógica de comunicação de rede.

---
Desenvolvido como uma solução para melhoria da acessibilidade urbana.
