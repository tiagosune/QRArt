import api from './api';

export const qrcodeService = {
  getAll: async () => {
    const response = await api.get('/qrcode');  // 🔥 Mudou de /qrcodes para /qrcode
    return response.data;
  },

  getById: async (id) => {
    const response = await api.get(`/qrcode/${id}`);  // 🔥 Você precisa criar esse endpoint no backend
    return response.data;
  },

  create: async (title, text, logoFile) => {
    const formData = new FormData();
    formData.append('title', title);
    formData.append('text', text);

    if (logoFile) {
      formData.append('logoFile', logoFile);
    }

    const response = await api.post('/qrcode/create', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    });
    return response.data;
  },

  update: async (id, title, text) => {
    const response = await api.put(`/qrcode/${id}`, { title, text });
    return response.data;
  },

  delete: async (id) => {
    await api.delete(`/qrcode/${id}`);
  },

};
