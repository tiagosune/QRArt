# 🚀 QrArt — Dynamic & Custom QR Code Platform

**QrArt** é uma aplicação web SaaS para criação de QR Codes dinâmicos e personalizados, desenvolvida como projeto de portfólio full-stack.

O sistema permite que usuários criem QR Codes com logo personalizada, utilizem redirecionamento dinâmico e realizem pagamentos via Stripe para desbloquear recursos.

> 💡 Este é o projeto mais desafiador que já desenvolvi até agora, envolvendo backend robusto, frontend SPA, autenticação JWT, integração com pagamentos e deploy em produção na AWS com HTTPS.

---

## 🌐 Demo

🔗 **[https://qrart.com.br](https://qrart.com.br)**

---

## 🧠 Funcionalidades

### 👤 Autenticação
- Cadastro de usuário
- Login com JWT
- Controle de acesso por roles (`USER` / `ADMIN`)
- Endpoint `/api/users/me`

### 🔳 QR Code Dinâmico
- Geração de QR Code personalizado
- Upload de logo (PNG / JPG)
- Correção de erro nível H
- QR aponta para: `https://qrart.com.br/r/{id}`
- **Redirecionamento dinâmico** (permite alterar o destino sem gerar novo QR)

### 👨‍💼 Painel Admin
- Listagem de todos usuários
- Listagem de todos QR Codes
- Exclusão administrativa

### 💳 Pagamentos
- Integração com **Stripe**
- Webhook configurado
- Controle de QR pago vs pendente

### 🌙 Interface
- SPA em React
- Dark / Light mode
- Axios para requisições
- Token armazenado no `localStorage`

---

## 🏗️ Arquitetura

### Backend
- **Java 21** (LTS)
- **Spring Boot 3**
- **Spring Security**
- **JWT**
- **Spring Data JPA**
- **PostgreSQL** (AWS RDS)
- **ZXing** (geração de QR)
- **Stripe SDK**
- Deploy em **EC2**
- **Nginx** Reverse Proxy
- **HTTPS** com Let's Encrypt

### Frontend
- **React** + **Vite**
- **Axios**
- **Context API**
- SPA servida pelo Nginx

### Infraestrutura
- **AWS EC2** (Ubuntu)
- **AWS RDS** (PostgreSQL)
- **Nginx**
- **Certbot** (SSL)
- **Systemd** service

---

## 📂 Estrutura do Projeto

```
qrart/
 ├── src/ (backend)
 ├── qrart-frontend/
 ├── pom.xml
 └── README.md
```

📁 Uploads são armazenados em: `/opt/qrart/uploads/qrcodes/`

---

## 🔐 Variáveis de Ambiente (Produção)

```env
DB_URL=
DB_USERNAME=
DB_PASSWORD=

STRIPE_SECRET=
STRIPE_WEBHOOK_SECRET=

APP_BASE_URL=https://qrart.com.br
```

---

## 🚀 Deploy

O projeto está hospedado na **AWS**:

- **EC2** (backend)
- **RDS** (PostgreSQL)
- **Nginx** (reverse proxy + SPA)
- **HTTPS** via Let's Encrypt
- Serviço configurado via **systemd**

---

## 🧩 Principais Desafios Resolvidos

✅ Configuração completa de Spring Security + JWT  
✅ Controle de roles  
✅ CORS em produção  
✅ Reverse proxy com Nginx  
✅ Certificado SSL  
✅ Upload seguro de arquivos  
✅ QR Code ilegível por sobreposição de logo  
✅ Erros 403 / 413 em produção  
✅ Integração Stripe com webhook em HTTPS  
✅ Deploy manual completo na AWS  

---

## 📈 Próximas Melhorias

- [ ] Estatísticas de acesso ao QR
- [ ] Dashboard com métricas
- [ ] Armazenamento de imagens no **S3**
- [ ] Dockerização
- [ ] CI/CD
- [ ] Refresh token

---

## 👨‍💻 Sobre o Projeto

**Desenvolvido por Tiago Duarte**  
*Desenvolvedor Java Júnior*

Este projeto representa meu maior desafio até o momento, unindo:

- ✨ Backend enterprise
- 🎨 Frontend SPA
- 💳 Integração com pagamentos
- ☁️ Infraestrutura real em cloud
- 🌐 Deploy em produção com domínio próprio

---

## 📞 Contato

[![LinkedIn](https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white)](www.linkedin.com/in/tiagosunedev)
[![GitHub](https://img.shields.io/badge/GitHub-100000?style=for-the-badge&logo=github&logoColor=white)](https://github.com/tiagosune)
[![Email](https://img.shields.io/badge/Email-D14836?style=for-the-badge&logo=gmail&logoColor=white)](mailto:tiagosune1@hotmail.com)

---

## 📄 Licença

Projeto para fins educacionais e portfólio.

---

⭐ **Se este projeto te ajudou de alguma forma, considere dar uma estrela!** ⭐
