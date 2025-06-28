# Meu Balanço - Controle Financeiro Pessoal  

## Descrição do Projeto  
Aplicativo Android para gerenciamento de receitas e despesas, desenvolvido em **Java** com:  
- Armazenamento local (**Room Database**)  
- Interface Material Design  
- Relatório de Saldo  

## Autores  
- **Pedro Lucas Cassiano Martins**  
- *Turma B - PDM - CEUB*  

## Instruções de Instalação  
**Requisitos**:  
   - Android Studio Hedgehog (2023.1.1+)  
   - Dispositivo/emulador com Android 6.0+
   - Pelo menos 30mb de espaço livre


**Opção 1: Via Google Drive**
Acesse o link do APK no Google Drive:
🔗 (Link para Download)[https://drive.google.com/file/d/1ALcQQOqZ_k_LXinIQOxkn--Sz8THBeFj/view?usp=drive_link)

- Baixe o arquivo app-release.apk.

Instale no seu Android:

- Toque no arquivo baixado > "Instalar".

Se aparecer um aviso de "Fontes desconhecidas", siga:
Configurações > Segurança > Permitir instalação de fontes desconhecidas.

**Opção 2: Direto do Repositório (APK na Pasta app/release)**
Navegue até a pasta do projeto:

/MeuBalanco_v1.0/app/release/app-release.apk
Transfira o APK para seu dispositivo via USB, Bluetooth ou e-mail.

Siga os mesmos passos de instalação da Opção 1.

**Opção 3: Direto do Repositório com emulador**
    1. Em um computador, abra o Android Studio com nenhum projeto aberto.
    2. Clique nos 3 pontinhos verticais e selecione "Profile or Debug APK"
    3. Navegue até a pasta do projeto:
       /MeuBalanco_v1.0/app/release
    4. Selecione a apk "meu-balanco.apk"
    5. Clique no 'Run'.
    6. Espere instalar o aplicativo, abra e teste!

## Funcionalidades Desenvolvidas

1. Cadastro de Transações
	É possível cadastrar transações com o tipo (Receita/Despesa), valor em Real, categoria de transação e descrição.
2. Edição de Transações
	É possível editar as transações cadastradas podendo editar o tipo (Receita/Despesa), valor em Real, categoria de transação, descrição e data.
3. Exclusão de Transações
	É possível excluir as transações cadastradas.
4. Relatório do Saldo
	Uma tela criada para ver a soma de receitas, despesas e o saldo.
5. Cadastro de Categorias
	É possível cadastrar novas categorias para serem utilizadas nas transações
6. Notificações
	O usuário é notificado ao cadastrar transações
