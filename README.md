# Meu Balanço - Aplicativo de Controle Financeiro Pessoal  

![Android](https://img.shields.io/badge/Platform-Android-green.svg)
![Java](https://img.shields.io/badge/Language-Java_11-orange.svg)
![Architecture](https://img.shields.io/badge/Architecture-MVVM-blue.svg)
![Database](https://img.shields.io/badge/Database-Room_SQLite-lightgrey.svg)
![License](https://img.shields.io/badge/License-MIT-brightgreen.svg)

## Descrição do Projeto
**Meu Balanço** é um aplicativo Android focado no gerenciamento financeiro pessoal simplificado, prático e intuitivo. Projetado para estudantes, jovens profissionais, autônomos e famílias, o aplicativo permite registrar e categorizar receitas e despesas com total privacidade e persistência de dados offline.

- Armazenamento local (**Room Database**)
- Interface Material Design
- Relatório de Saldo

- **Protótipo Interativo no Figma:** [Acessar Design no Figma](https://www.figma.com/design/jqvhNvl8NR8APuQBnc2Ucu/Meu-Balan%C3%A7o?node-id=10-83&t=QCK2cL59THQfwJew-1)

---

## Recursos Principais

- **Gestão de Transações:** Cadastro, listagem e edição rápida de receitas e despesas.
- **Categorização:** Organização de lançamentos por categorias (alimentação, salário, freelances, etc.).
- **Relatórios Financeiros:** Visualização consolidada de totais de receitas, despesas e saldo líquido disponível.
- **Privacidade & Segurança:** Dados armazenados 100% de forma local no dispositivo através do Room Database.
- **Interface Moderna:** Design responsivo e adaptável construído sobre as diretrizes do Material Design.

---

## Fluxo de Navegação (User Flow)

O aplicativo foi projetado garantindo uma navegação fluida entre os principais fluxos de gerenciamento e relatórios:

![User Flow UX](https://github.com/PedroLucasCM/MeuBalanco/blob/readme.md/assets/images/User_Flow_UX_(Meu_Balanço).jpg?raw=true)

---

## Capturas de Tela (Screenshots)

| Listagem de Transações | Adicionar Transação | Relatórios Financeiros | Edição de Transação |
| :---: | :---: | :---: | :---: |
| ![Home](https://github.com/PedroLucasCM/MeuBalanco/blob/readme.md/assets/images/home.png?raw=true) | ![Adicionar Transacao](https://github.com/PedroLucasCM/MeuBalanco/blob/readme.md/assets/images/adicionar.png?raw=true) | ![Relatorio](https://github.com/PedroLucasCM/MeuBalanco/blob/readme.md/assets/images/relatorio.png?raw=true) | ![Edicao de Transacao](https://github.com/PedroLucasCM/MeuBalanco/blob/readme.md/assets/images/editar.png?raw=true) |

---

## Arquitetura e Tecnologias

O projeto foi construído utilizando as melhores práticas da engenharia de software Android:

- **Linguagem:** Java 11
- **Arquitetura:** MVVM (Model-View-ViewModel com LiveData)
- **Persistência de Dados:** Room Database (SQLite local)
- **Interface Gráfica:** XML Layouts com Material Design Components
- **Build System:** Gradle
- **Ambiente de Testes:** Android Studio Hedgehog / Emulador API 33 & Dispositivo Físico

---

## Instruções de Instalação  
**Pré-Requisitos**:  
   - Android Studio Hedgehog (2023.1.1+)  
   - Dispositivo/emulador com Android 6.0+
   - Pelo menos 30mb de espaço livre


**Opção 1: Via Google Drive**
Acesse o link do APK no Google Drive:

[Link para Download](https://drive.google.com/file/d/1ALcQQOqZ_k_LXinIQOxkn--Sz8THBeFj/view?usp=drive_link)

1. Baixe o arquivo app-release.apk.
2. Instale no seu Android:
- Toque no arquivo baixado > "Instalar".
3. Se aparecer um aviso de "Fontes desconhecidas", siga:
- Configurações > Segurança > Permitir instalação de fontes desconhecidas.

**Opção 2: Direto do Repositório (APK na Pasta app/release)**

1. Navegue até a pasta do projeto: /MeuBalanco_v1.0/app/release/app-release.apk
2. Transfira o APK para seu dispositivo via USB, Bluetooth ou e-mail.
3. Siga os mesmos passos de instalação da Opção 1.

**Opção 3: Direto do Repositório com emulador**
1. Em um computador, abra o Android Studio com nenhum projeto aberto.
2. Clique nos 3 pontinhos verticais e selecione "Profile or Debug APK"
3. Navegue até a pasta do projeto: /MeuBalanco_v1.0/app/release
4. Selecione a apk "meu-balanco.apk"
5. Clique no 'Run'.
6. Espere instalar o aplicativo, abra e teste!

## Funcionalidades Desenvolvidas

**1. Cadastro de Transações**
   
	É possível cadastrar transações com o tipo (Receita/Despesa), valor em Real, categoria de transação e descrição.

**3. Edição de Transações**
   
	É possível editar as transações cadastradas podendo editar o tipo (Receita/Despesa), valor em Real, categoria de transação, descrição e data.
**5. Exclusão de Transações**
   
	É possível excluir as transações cadastradas.

**7. Relatório do Saldo**
   
	Uma tela criada para ver a soma de receitas, despesas e o saldo.

**9. Cadastro de Categorias**
    
	É possível cadastrar novas categorias para serem utilizadas nas transações

**11. Notificações**
    
	O usuário é notificado ao cadastrar transações


## Documentação Acadêmica (PI3)

Este projeto integra as atividades do Projeto Integrador III (PI3) do curso de Análise e Desenvolvimento de Software do Centro Universitário de Brasília (CEUB).

    Docente Responsável: Prof. Ricardo Neiva

    Evidências de Entrega: Artefatos interativos, protótipos de alta fidelidade e fluxos funcionais para validação extensionista (ACE).   

## Autor

**Pedro Lucas Cassiano Martins**

- Matrícula: 72451202

- GitHub: @PedroLucasCM

- Curso: Análise e Desenvolvimento de Software — CEUB