import api from "./api";

export const adminService = {
    getUsers: async () => {
        const res = await api.get("/admin/users");
        return res.data;
    },

    getQRCodes: async () => {
        const res = await api.get("/admin/qrcodes");
        return res.data;
    },

    deleteQRCode: async (id) => {
        await api.delete(`/admin/qrcodes/${id}`);
    },
};
